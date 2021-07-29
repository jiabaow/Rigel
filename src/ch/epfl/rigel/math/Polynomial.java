package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

//represents a polynomial function
//by Marin Cohu & Jiabao WEN
public final class Polynomial {

    private final double[] coeffs;

    private Polynomial(double[] coeffs) {
        this.coeffs = coeffs;
    }

    /**
     * @param coefficientN the greatest coefficient
     * @param coefficients other coefficients
     * @return polynomial of the given coefficient(s)
     */
    public static Polynomial of(double coefficientN, double... coefficients) {
        Preconditions.checkArgument(coefficientN != 0);
        double[] coeff = new double[coefficients.length + 1];
        coeff[0] = coefficientN;
        System.arraycopy(coefficients, 0, coeff, 1, coefficients.length);

        return new Polynomial(coeff);
    }

    /**
     * @param x in double
     * @return value of the function at given x
     */
    public double at(double x) {
        int n = coeffs.length;
        double result = coeffs[0];
        for (int i = 1; i < n; i++) {
            result = result * x + coeffs[i];
        }
        return result;
    }

    /**
     * @see String#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int tableSize = coeffs.length;
        if (tableSize == 1) {
            sb.append(coeffs[0]);
        } else if (tableSize > 1) {
            //treatement of all the elements except the las one
            for (int i = 0; i < tableSize - 1; i++) {
                //first element
                if (i == 0) {
                    // whether coeffs[0] is of form "x^y" or "x"
                    if (tableSize - 1 - i == 1) {
                        if (coeffs[0] == 1) {
                            sb.append("x");
                        } else if (coeffs[0] == -1) {
                            sb.append("-x");
                        } else {
                            sb.append(coeffs[0]).append("x");
                        }
                    } else {
                        if (coeffs[0] == 1) {
                            sb.append("x^").append(tableSize - 1 - i);
                        } else if (coeffs[0] == -1) {
                            sb.append("-x^").append(tableSize - 1 - i);
                        } else {
                            sb.append(coeffs[0]).append("x^").append(tableSize - 1 - i);
                        }
                    }
                    //end of first element

                    //start of all the other elements (except the last one)
                } else if (coeffs[i] != 0) {
                    if (tableSize - 1 - i == 1) {
                        if (coeffs[i] < 0) {
                            if (coeffs[i] == -1) {
                                sb.append("-x");
                            } else {
                                sb.append(coeffs[i]).append("x");
                            }
                        } else {
                            if (coeffs[i] == 1) {
                                sb.append("+x");
                            } else {
                                sb.append("+").append(coeffs[i]).append("x");
                            }
                        }

                    } else if (tableSize - 1 - i > 1) {
                        if (coeffs[i] < 0) {
                            if (coeffs[i] == -1) {
                                sb.append("-x^").append(tableSize - 1 - i);
                            } else {
                                sb.append(coeffs[i]).append("x^").append(tableSize - 1 - i);
                            }
                        } else {
                            if (coeffs[i] == 1) {
                                sb.append("+x^").append(tableSize - 1 - i);
                            } else {
                                sb.append("+").append(coeffs[i]).append("x^").append(tableSize - 1 - i);
                            }
                        }
                    }
                }
            }
            //treatement of the last element
            if (coeffs[tableSize - 1] < 0) {
                sb.append(coeffs[tableSize - 1]);
            } else if (coeffs[tableSize - 1] > 0) {
                sb.append("+").append(coeffs[tableSize - 1]);
            }
        }
        return sb.toString();
    }


    /**
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(final Object o) {
        throw new UnsupportedOperationException();
    }


}
