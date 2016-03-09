/*
 * Copyright 2003-2016 - Regents of the University of California, San
 * Francisco.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */
package bsg.util;

/**
 * Wraps ec.util.MersenneTwisterFast to allow the MASON pRNG to be used 
 * in cern.jet.random.
 * 
 * Details:
 * nextDouble() returns (0.0, 1.0) unlike MASON's nextDouble() which returns [0.0, 1.0)
 * nextFloat() returns (0.0f, 1.0f) unlike MASON's nextFloat() which returns [0.0f, 1.0f)
 */
@SuppressWarnings("serial") // including serialVersionUID hides the one from RandomEngine
public class PRNGWrapper extends cern.jet.random.engine.RandomEngine implements Cloneable,
      cern.colt.function.DoubleFunction, cern.colt.function.IntFunction {

  private ec.util.MersenneTwisterFast mrng = null;

  public PRNGWrapper (ec.util.MersenneTwisterFast rng) {
    if (rng != null) mrng = rng;
    else throw new RuntimeException("MASON rng cannot be null.");
  }

  public ec.util.MersenneTwisterFast getMRNG() { return mrng; }

  public void setMRNG(ec.util.MersenneTwisterFast mtf) {
    if (mtf != null) mrng = mtf;
    else throw new RuntimeException("MASON RNG cannot be null.");
  }

  @Override
  public double apply(double argument) { return raw(); }

  @Override
  public int apply(int argument) { return nextInt(); }

  @Override
  public double nextDouble() { return mrng.nextDouble(); }

  @Override
  public float nextFloat() { return mrng.nextFloat(); }

  @Override
  public int nextInt() { return mrng.nextInt(); }

  @Override
  public long nextLong() { return mrng.nextLong(); }

  @Override
  public double raw() { return mrng.nextFloat(); }

  public static void main(String[] args) {
    PRNGWrapper prngw = new PRNGWrapper(new ec.util.MersenneTwisterFast(System.currentTimeMillis()));
    System.out.println("Testing MASON prng wrapper for COLT.");
    //for (int ndx=0; ndx<=500; ndx++) System.out.println(prngw.nextInt());

    System.out.println("\ntesting distribution.");
    cern.colt.list.DoubleArrayList l = new cern.colt.list.DoubleArrayList(500);
    cern.jet.random.Beta gd = new cern.jet.random.Beta(2.0, 2.0, prngw);
    for (int ndx=0; ndx<=500; ndx++) {
      int val = gd.nextInt();
      l.add(val);
      System.out.print(val + " ");
    }
    double mean = cern.jet.stat.Descriptive.mean(l);
    System.out.println("\naverage = "+mean);
    System.out.println("variance = "+cern.jet.stat.Descriptive.sampleVariance(l, mean));


    System.out.println("\ntesting distribution.");
    l.clear();
    gd = new cern.jet.random.Beta(2.0, 2.0, prngw);
    for (int ndx=0; ndx<=500; ndx++) {
      double val = gd.nextDouble();
      l.add(val);
      System.out.print(val + " ");
    }
    mean = cern.jet.stat.Descriptive.mean(l);
    System.out.println("\naverage = "+mean);
    System.out.println("variance = "+cern.jet.stat.Descriptive.sampleVariance(l, mean));

  }

}
