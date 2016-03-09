/**
 * Copyright 2013-2016 - Regents of the University of California, San
 * Francisco.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */
package bsg.util;

public class SigmoidGradient extends LinearGradient {
  private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger( LinearGradient.class );

  public SigmoidGradient(double[] p) {
    super(p);
  }
  /**
   * 
   * @param refX = startProb
   * @param refY = finishProb
   * @param valX = start
   * @param valY = finish
   * @param x = position
   * @return 
   */
  public static double eval(double refX, double refY, double valX, double valY, double x) {
    double val = LinearGradient.eval(10.0, 0.0, valX, valY, x);
    //log.debug("SigmoidGradient: "+val+" = 0.0 + "+x+" * (10.0-0.0)/("+valY+"-"+valX+")");
    double retVal = refX + (refY-refX)/(1.0+StrictMath.exp(val-5.0));
    //log.debug("SigmoidGradient: "+retVal+" = "+refX+" + ("+refY+"-"+refX+")/(1.0+e^("+val+"-5.0))");
    return retVal;
  }
  public static void main(String[] args) {
    int min=17, max = 95;
    double mean = 32.2611, median=28.0;
    //INFO  isl.model.ISL - Lobule.init() - min = 17, mean = 32.26111111111111, median = 28.0, max = 95
    log.debug("min");
    for (int i=0; i<min ; i++) {
        log.debug(i+" => "+ SigmoidGradient.eval(0.9, 0.0, 0.0, (double)min, (double)i));
    }
    log.debug("mean");
    for (int i=0; i<Math.round(mean) ; i++) {
        log.debug(i+" => "+ SigmoidGradient.eval(0.9, 0.0, 0.0, mean, (double)i));
    }
    log.debug("median");
    for (int i=0; i<Math.round(median) ; i++) {
        log.debug(i+" => "+ SigmoidGradient.eval(0.9, 0.0, 0.0, median, (double)i));
    }
    log.debug("max");
    for (int i=0; i<max ; i++) {
        log.debug(i+" => "+ SigmoidGradient.eval(0.9, 0.0, 0.0, (double)max, (double)i));
    }
  }
  

}
