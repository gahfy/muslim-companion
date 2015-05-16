package net.gahfy.muslimcompanion.utils;

public class MathUtils {
    /**
     * Calculate the exponentialSmoothing for a smoother rotation of the compass
     * @param input the previous values
     * @param output the gross values to go to
     * @param alpha The coefficient of smoothness
     * @return the array with exponential smoothing applied on it
     * @link http://en.wikipedia.org/wiki/Exponential_smoothing
     */
    public static float[] exponentialSmoothing( float[] input, float[] output, float alpha ) {
        if ( output == null )
            return input;
        for ( int i=0; i<input.length; i++ ) {
            output[i] = output[i] + alpha * (input[i] - output[i]);
        }
        return output;
    }

    public static double acot ( double value ){
        return Math.atan( 1.0 / value );
    }
}
