/*
 * Copyright 2003-2016 - Regents of the University of California, San
 * Francisco.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */

package bsg.util;

public class MutableInt extends Number {
  private static final long serialVersionUID = -7399169793513727172L;
   public long val = -Integer.MAX_VALUE;
   
   public MutableInt(long i) {
      val = i;
   }
   public MutableInt set(long i) { val = i; return this;}
   public MutableInt add(long i) { val += i; return this;}
   public MutableInt sub(long i) { val -= i; return this;}
   
  @Override
   public double doubleValue() { return (double)val; }
  @Override
   public float floatValue() { return (float)val; }
  @Override
   public long longValue() { return val; }
  @Override
   public int intValue() { return (int)val; }

  @Override
  public String toString() { return Long.toString(val); }
   
}
