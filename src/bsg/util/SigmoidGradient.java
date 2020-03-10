/**
 * Copyright 2013-2020 - Regents of the University of California, San
 * Francisco.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */
package bsg.util;

public class SigmoidGradient extends Gradient {
  private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger( LinearGradient.class );

  public SigmoidGradient(double[] p) {
    super(p);
  }
  public static double eval(double refX, double refY, double intensity, double valX, double valY, double x) {
    double val = LinearGradient.eval(10, 0, valX, valY, x);
    double retVal = refX + (refY-refX)/(1.0+StrictMath.exp(intensity*(val-5)));
    return retVal;
  }
  public static void main(String[] args) {
    for (double i=0; i<10 ; i += 1) {
        System.out.println(i+", "+ SigmoidGradient.eval(5, 100, 1, 0, 10, i));
    }
  }
  

}
