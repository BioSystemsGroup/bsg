/*
 * Copyright 2021 - Regents of the University of California, San
 * Francisco.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */

package bsg.util;

public class MutableDouble extends Number implements MutableNumber {
   public double val = Double.NaN;
   public MutableDouble() {}
   public MutableDouble(double i) {
      val = i;
   }
   @Override
   public MutableDouble set(Number i) { val = i.doubleValue(); return this;}
   @Override
   public MutableDouble add(Number i) { val += i.doubleValue(); return this;}
   @Override
   public MutableDouble sub(Number i) { val -= i.doubleValue(); return this;}
   
  @Override
   public double doubleValue() { return (double)val; }
  @Override
   public float floatValue() { return (float)val; }
  @Override
   public long longValue() { return (long)val; }
  @Override
   public int intValue() { return (int)val; }

  @Override
  public String toString() { return Double.toString(val); }
   
}
