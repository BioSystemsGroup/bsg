/**
 * Copyright 2013-2020 - Regents of the University of California, San
 * Francisco.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */
package bsg.util;

/**
 *                       (RefX-RefY)
 * ref = RefY + value * ------------- 
 *                       (ValX-ValY)
 */
public class LinearGradient extends Gradient {
  private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger( LinearGradient.class );

  public LinearGradient(double[] p) {
    super(p);
  }
  public static double eval(double refX, double refY, double valX, double valY, double x) {
    double retVal = refX + x * (refY-refX)/(valY-valX);
    //log.debug(retVal+" = "+refX+" + "+x+" * ("+refY+"-"+refX+")/("+valY+"-"+valX+")");
    return retVal;
  }
  public static double eval(double Y1, double Y2, double totaldist, double d) {
    double retVal = Y1 + (d/totaldist) * (Y2 - Y1);
    //log.debug(retVal+" = "+Y1+" + ("+d+"/"+totaldist+") * ("+Y2+"-"+Y1+")");
    return retVal;
  }
  public static void main(String[] args) {
    for (int i=0; i<10 ; i++) {
        log.debug(i+" => "+ LinearGradient.eval(0.9, 0.0, 0.0, 10, (double)i));
        log.debug(i+" => "+ LinearGradient.eval(0.9, 0.0, 10, (double)i));
    }
  }
}
