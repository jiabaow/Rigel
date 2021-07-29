package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.*;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.converter.LocalTimeStringConverter;
import javafx.util.converter.NumberStringConverter;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;


public class Main extends Application {

    /**
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    private InputStream resourceStream(String resourceName) {
        return getClass().getResourceAsStream(resourceName);
    }

    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     *
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     * </p>
     * <p>
     * show the window of the application
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages.
     * @throws Exception if something goes wrong
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        try (InputStream hs = resourceStream("/hygdata_v3.csv");
             InputStream as = resourceStream("/asterisms.txt")) {
            StarCatalogue catalogue = new StarCatalogue.Builder()
                    .loadFrom(hs, HygDatabaseLoader.INSTANCE)
                    .loadFrom(as, AsterismLoader.INSTANCE)
                    .build();
            BorderPane root = new BorderPane();

            primaryStage.setTitle("Rigel");
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(600);

            GeographicCoordinates position = GeographicCoordinates.ofDeg(6.57, 46.52);
            HorizontalCoordinates center = HorizontalCoordinates.ofDeg(180, 15);

            ZonedDateTime when = ZonedDateTime.now();
            DateTimeBean dateTimeBean = new DateTimeBean();
            dateTimeBean.setZonedDateTime(when);

            ObserverLocationBean observerLocationBean =
                    new ObserverLocationBean();
            observerLocationBean.setCoordinates(position);

            ViewingParametersBean viewingParametersBean = new ViewingParametersBean();
            viewingParametersBean.setCenter(center);
            viewingParametersBean.setFieldOfViewDeg(100);

            SkyCanvasManager canvasManager = new SkyCanvasManager(
                    catalogue,
                    dateTimeBean,
                    observerLocationBean,
                    viewingParametersBean);

            HBox controlBar = controlBar(observerLocationBean, dateTimeBean);

            HBox bottomInfoBar = new HBox();
            Button toQuiz = new Button("Test yourself!");
            toQuiz.setOnAction(e -> primaryStage.setScene(new Scene(quizPage())));
            bottomInfoBar.getChildren().addAll(toQuiz, moreInfo());
            bottomInfoBar.setPadding(new Insets(0, 200, 0, 200));
            bottomInfoBar.setSpacing(250);

            BorderPane infoBar = informationBar(viewingParametersBean, canvasManager);
            infoBar.setBottom(bottomInfoBar);

            Pane skyPane = skyPane(canvasManager);

            root.setTop(controlBar);
            root.setBottom(infoBar);
            root.setCenter(skyPane);

            primaryStage.setScene(new Scene(root));
            primaryStage.show();
            skyPane.requestFocus();
        }
    }

    private HBox controlBar(ObserverLocationBean observerLocationBean, DateTimeBean dateTimeBean) throws IOException {
        HBox controlBar = new HBox();

        //sub-pane
        HBox position = position(observerLocationBean);

        TimeAnimator timeAnimator = new TimeAnimator(dateTimeBean);
        HBox dateTime = dateTime(dateTimeBean, timeAnimator);

        HBox runningTime = runningTime(timeAnimator);

        Separator separator = new Separator(Orientation.VERTICAL);
        Separator separator1 = new Separator(Orientation.VERTICAL);

        controlBar.getChildren().addAll(position, separator, dateTime, separator1, runningTime);

        controlBar.setStyle("-fx-spacing: 4; -fx-padding: 4;");

        return controlBar;
    }

    private HBox position(ObserverLocationBean observerLocationBean) {
        HBox position = new HBox();
        position.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");

        Label longitude = new Label("Longitude(°):");
        Label latitude = new Label("Latitude(°):");

        String coordStyle = "-fx-pref-width: 60; -fx-alignment: baseline-right;";

        TextFormatter<Number> lonTextFormatter = PosTextFormatter(GeographicCoordinates::isValidLonDeg);
        TextField lonTextField = new TextField();
        lonTextField.setStyle(coordStyle);
        lonTextField.setTextFormatter(lonTextFormatter);
        lonTextFormatter.valueProperty().bindBidirectional(observerLocationBean.lonDegProperty());

        TextFormatter<Number> latTextFormatter = PosTextFormatter(GeographicCoordinates::isValidLatDeg);
        TextField latTextField = new TextField();
        latTextField.setStyle(coordStyle);
        latTextField.setTextFormatter(latTextFormatter);
        latTextFormatter.valueProperty().bindBidirectional(observerLocationBean.latDegProperty());

        position.getChildren().addAll(longitude, lonTextField, latitude, latTextField);

        return position;
    }
    
    private TextFormatter<Number> PosTextFormatter(Predicate<Double> predicate) {

        NumberStringConverter stringConverter = new NumberStringConverter("#0.00");

        UnaryOperator<TextFormatter.Change> posFilter = (change -> {
            try {
                String newText = change.getControlNewText();
                double newPosDeg = stringConverter.fromString(newText).doubleValue();
                return predicate.test(newPosDeg) ? change : null;
            } catch (Exception e) {
                return null;
            }
        });

        return new TextFormatter<Number>(stringConverter, 0, posFilter);
    }

    private HBox dateTime(DateTimeBean dateTimeBean, TimeAnimator timeAnimator) {
        HBox dateTime = new HBox();
        dateTime.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");

        Label date = new Label("Date : ");
        DatePicker datePicker = new DatePicker();
        datePicker.valueProperty().bindBidirectional(dateTimeBean.dateProperty());
        datePicker.setStyle("-fx-pref-width: 120;");
        datePicker.disableProperty().bind(timeAnimator.runningProperty());

        Label hms = new Label("Heure : ");
        TextField hmsTextField = new TextField();
        hmsTextField.setStyle("-fx-pref-width: 75; -fx-alignment: baseline-right;");
        DateTimeFormatter hmsFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTimeStringConverter stringConverter = new LocalTimeStringConverter(hmsFormatter, hmsFormatter);
        TextFormatter<LocalTime> timeFormatter = new TextFormatter<>(stringConverter);
        hmsTextField.setTextFormatter(timeFormatter);
        timeFormatter.valueProperty().bindBidirectional(dateTimeBean.timeProperty());
        hmsTextField.disableProperty().bind(timeAnimator.runningProperty());

        ComboBox<ZoneId> zone = new ComboBox<>();
        zone.setStyle("-fx-pref-width: 180;");
        zone.valueProperty().bindBidirectional(dateTimeBean.zoneProperty());
        List<String> availableZoneIds  = new ArrayList<>(ZoneId.getAvailableZoneIds());
        Collections.sort(availableZoneIds);
        for (String string : availableZoneIds) 
            zone.getItems().add(ZoneId.of(string));
        
        zone.disableProperty().bind(timeAnimator.runningProperty());

        dateTime.getChildren().addAll(date, datePicker, hms, hmsTextField, zone);

        return dateTime;
    }

    private HBox runningTime(TimeAnimator timeAnimator) throws IOException {
        HBox runningTime = new HBox();
        try (InputStream fontStream = getClass().getResourceAsStream("/Font Awesome 5 Free-Solid-900.otf")) {
            runningTime.setStyle("-fx-spacing: inherit;");

            ChoiceBox<NamedTimeAccelerator> timeMenu = new ChoiceBox<>();
            timeMenu.setItems(FXCollections.observableList(NamedTimeAccelerator.ALL));
            timeMenu.setValue(NamedTimeAccelerator.TIMES_300);
            timeAnimator.acceleratorProperty().bind(Bindings.select(timeMenu.valueProperty(), "accelerator"));

            Font fontAwesome = Font.loadFont(fontStream, 15);

            String undo = "\uf0e2";
            Button undoButton = new Button(undo);
            undoButton.setFont(fontAwesome);

            String play = "\uf04b";
            Button playButton = new Button(play);
            playButton.setFont(fontAwesome);

            String pause = "\uf04c";
            Button pauseButton = new Button(pause);
            pauseButton.setFont(fontAwesome);

            playButton.setOnAction(o -> {
                timeAnimator.start();
                playButton.setVisible(false);
                pauseButton.setVisible(true);
            });

            pauseButton.setOnAction(o -> {
                timeAnimator.stop();
                playButton.setVisible(true);
                pauseButton.setVisible(false);
            });

            runningTime.getChildren().addAll(timeMenu, undoButton, playButton, pauseButton);
        }
        return runningTime;
    }

    private Pane skyPane(SkyCanvasManager canvasManager) {
        Canvas sky = canvasManager.canvas();
        Pane skyPane = new Pane(sky);

        sky.widthProperty().bind(skyPane.widthProperty());
        sky.heightProperty().bind(skyPane.heightProperty());

        return skyPane;
    }

    private BorderPane informationBar(ViewingParametersBean viewingParametersBean,
                                      SkyCanvasManager skyCanvasManager) {
        BorderPane infoBar = new BorderPane();
        infoBar.setStyle("-fx-padding: 4; -fx-background-color: white;");

        StringExpression fov = Bindings.format(Locale.ROOT,
                "Champ de vue : %.1f °",
                viewingParametersBean.fieldOfViewDegProperty());
        Text left = new Text(fov.toString());
        left.textProperty().bind(fov);

        StringExpression mouseCoor = Bindings.format(Locale.ROOT,
                "Azimut : %.2f °, hauteur : %.2f °",
                skyCanvasManager.mouseAzDeg, skyCanvasManager.mouseAltDeg);
        Text right = new Text(mouseCoor.toString());
        right.textProperty().bind(mouseCoor);

        Text center = new Text();
        ObservableObjectValue<String> centerString = Bindings.createObjectBinding(
                () -> {
                    CelestialObject object = skyCanvasManager.objectUnderMouseProperty().get();
                    if (object != null)
                        return object.info();
                    return null;
                },
                skyCanvasManager.objectUnderMouseProperty()
        );
        center.textProperty().bind(centerString);

        infoBar.setLeft(left);
        infoBar.setCenter(center);
        infoBar.setRight(right);

        return infoBar;
    }

    private BorderPane quizPage() {
        BorderPane quizPage = new BorderPane();
        quizPage.setStyle("-fx-padding: 4; -fx-background-color: white;");
        quizPage.setMinHeight(600);
        quizPage.setMinWidth(800);

        Label top = new Label("Rigel Astronomy quiz");
        top.setStyle("-fx-font-size: 20pt");
        quizPage.setTop(top);

        ImageView imageSun = new ImageView("https://cdn.arstechnica.net/wp-content/uploads/2020/03/nasa_386_SunEmitsSolsticeFlare1200w-800x480.jpg");

        String q1 = "The day on which the Sun's direct rays cross the celestial equator is called:";
        String q2 = "Which of these objects is the farthest from the Sun?";
        Label question = new Label(q1);
        question.setStyle("-fx-font-size: 15pt ");
        VBox center = new VBox();
        center.getChildren().addAll(imageSun, question);
        quizPage.setCenter(center);

        Button a = new Button("A) the ecliptic");
        Button b = new Button("B) the equinox");
        Button c = new Button("C) the solstice");
        Button nextButton = new Button("Next question");
        String style = "-fx-background-color: #FFFFFF; -fx-font-size: 10pt; -fx-spacing: 80";
        a.setStyle(style);
        b.setStyle(style);
        c.setStyle(style);
        nextButton.setVisible(false);
        String redBackG = "-fx-background-color: #AB4642";
        a.setOnAction(event -> a.setStyle(redBackG));
        c.setOnAction(event -> c.setStyle(redBackG));
        b.setOnAction(e -> {
            b.setStyle("-fx-background-color: #A1B56C");
            nextButton.setVisible(true);
        });

        nextButton.setOnAction(e -> {
            b.setStyle(style);
            a.setStyle(style);
            c.setStyle(style);
            question.setText(q2);
            a.setText("A) Kuiper belt");
            b.setText("B) 90377 Sedna");
            c.setText("C) Neptune");
            nextButton.setVisible(false);
            nextButton.setText("You need to upgrade to Pro to unlock more questions");
        });

        HBox bottom = new HBox();
        bottom.getChildren().addAll(a, b, c, nextButton);
        quizPage.setBottom(bottom);

        return quizPage;
    }

    private MenuButton moreInfo() {
        MenuItem sun = new MenuItem("Sun");
        MenuItem moon = new MenuItem("Moon");
        MenuItem planets = new MenuItem("Planets");
        MenuItem stars = new MenuItem("Stars");

        sun.setOnAction(event -> openBrowser("https://solarsystem.nasa.gov/solar-system/sun/overview/"));
        moon.setOnAction(event -> openBrowser("https://solarsystem.nasa.gov/moons/earths-moon/overview/"));
        planets.setOnAction(event -> AlertBox.display("Planets",
                "Upgrade to Rigel Pro to get more information about planets."));
        stars.setOnAction(event -> AlertBox.display("Stars",
                "Upgrade to Rigel Pro to get more information about starts."));


        MenuButton objectsInfo = new MenuButton("Want to know more?", null, sun, moon, planets, stars);

        objectsInfo.setPopupSide(Side.RIGHT);

        return objectsInfo;
    }

    private void openBrowser(String link) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI(link));
            } catch (URISyntaxException | IOException e) {
                e.printStackTrace();
            }
        }
    }

   
}
