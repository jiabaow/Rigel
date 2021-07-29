
package ch.epfl.rigel.math;
import java.util.Objects;

//represent an interval
//by Marin Cohu and Jiabao WEN
public abstract class Interval {
    private final double low,high;

    /**
     * 
     * @param low lower bound
     * @param high upper bound
     */
    protected Interval(double low, double high){
        this.low = low;
        this.high = high;
    }

    /**
     * 
     * @return lower bound of the interval
     */
    public double low(){
        return low;
    }

    /**
     * 
     * @return upper bound of the interval
     */
    public double high(){
        return high;
    }

    /**
     *
     * @return size of the interval
     */
    public double size(){
        return high - low;
    }

    /**
     * 
     * @param v a value
     * @return true if v is in the interval false otherwise
     */
    public abstract boolean contains(double v);


   /**
    * @see Objects#hashCode(Object) 
    */
   @Override
   public final int hashCode(){
       throw new UnsupportedOperationException();
   }

   /**
    * @see Objects#equals(Object)  
    */
   @Override
    public final boolean equals(final Object o){
       throw new UnsupportedOperationException();
   }
}
