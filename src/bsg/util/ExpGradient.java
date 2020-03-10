/**
 * Copyright 2020 - Regents of the University of California, San
 * Francisco.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */
package bsg.util;

public class ExpGradient extends Gradient {
  private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger( LinearGradient.class );
  public ExpGradient(double[] p) {
    super(p);
  }
  public static double eval(double refX, double refY, double intensity, double valX, double valY, double x) {
    double retVal = Double.NaN;
    double valMin = Math.min(valX,valY), valMax = Math.max(valX,valY);
    // if it's inverted, invert the vals
    double unitPos = (refX < refY ? (x-valMin)/(valMax-valMin) : 1-((x-valMin)/(valMax-valMin)));
    retVal = unitPos*((StrictMath.exp(intensity*unitPos)))/(Math.pow(Math.E,intensity));
    // scale it
    double refMin = Math.min(refX,refY), refMax = Math.max(refX,refY);
    retVal = refMin + retVal*(refMax-refMin);    
    return retVal;
  }
  public static void main(String[] args) {
    for (double i=-12; i<0.5 ; i += 1) {
        System.out.println(i+", "+ExpGradient.eval(5, 10, 1, -12, 0, i));
    }
  }
}
