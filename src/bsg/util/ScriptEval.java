/*
 * Copyright 2013-2016 - Regents of the University of California, San
 * Francisco.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */
package bsg.util;

import javax.script.ScriptException;

public class ScriptEval {
  final static javax.script.ScriptEngineManager manager = new javax.script.ScriptEngineManager();
  final static javax.script.ScriptEngine engine = manager.getEngineByExtension("js");
  javax.script.ScriptContext context = new javax.script.SimpleScriptContext();
  public javax.script.Bindings scope = null;

  String script = null;
  
  public ScriptEval(String scriptLocation) {
    context.setBindings(engine.createBindings(), javax.script.ScriptContext.ENGINE_SCOPE);
    scope = context.getBindings(javax.script.ScriptContext.ENGINE_SCOPE);

    //script = StringUtils.convertStreamToString(
    //        getClass().getClassLoader().getResourceAsStream(fileName));
    script = "load(\""+scriptLocation+"\")";

  }
  
  public double eval() throws ScriptException {
    double retVal = Double.NaN;
    Object result = null;
    
    if (!scope.containsKey("started")) throw new ScriptException("You must put the boolean variable \"started\" into the script's scope before using a script.");
    
    result = engine.eval(script,scope);
    if (result instanceof Double)
      retVal = (Double)result;
    else
      retVal = ((Integer)result).doubleValue();
    return retVal;
  }

  public static void main(String args[]) {
    ScriptEval se1 = null;
    se1 = new ScriptEval("classpath:bsg/test/script.js");
    se1.scope.put("started", false);
    // case |k| ≠ |v|
    try {
      String[] k1 = {"hello", "there", "dude", "red"};
      Object[] v1 = {true, "string1", 0.1};
      testcase(se1,k1,v1);
    } catch (Exception e) { System.err.println(e); }

    // case |k| == |v| but |k| ≠ |expected|
    try {
      String[] k2 = {"hello", "there", "dude", "red"};
      Object[] v2 = {true, "string1", 0.1, false};
      testcase(se1,k2,v2);
    } catch (Exception e) { System.err.println(e); }
    
    // invalid parameter
    try {
      String[] k3 = {"hello", "are", "blue"};
      Object[] v3 = {false, false, "string2"};
      testcase(se1,k3,v3);
    } catch (Exception e) { System.err.println(e); }
    
    // invalid type
    try {
      String[] k4 = {"you", "are", "blue"};
      Object[] v4 = {false, false, "string2"};
      testcase(se1,k4,v4);
    } catch (Exception e) { System.err.println(e); }

    // pass
    try {
      String[] k5 = {"you", "are", "blue"};
      Object[] v5 = {Double.POSITIVE_INFINITY, false, "string2"};
      testcase(se1,k5,v5);
    } catch (Exception e) { System.err.println(e); }

  }

    private static void testcase(ScriptEval se, String[] k, Object[] v) throws ScriptException {
    se.scope.put("keys", k);
    se.scope.put("vals", v);
    System.out.println("output = "+se.eval());
  }
  
  /* Commented out while we extract bsg.util from isl.util
   * We need a bsg.util-specific test script spanning the features we
   * expect for the isl and ishc.
  public static void main(String args[]) {
    ScriptEval se1 = null, se2 = null;
    if (isl.measurement.view.BatchControl.keyExists("-ei", args, 0)) {
      print_ei_gradient_summary(args);
    }
    if (isl.measurement.view.BatchControl.keyExists("-cc", args, 0)) {
      se1 = new ScriptEval("isl/model/constant-conc-function.js");
      for (double t = 0.0; t < 2.0; t += 0.1) {
        System.out.print("1st constant-conc eval(" + t + ") = " + se1.eval(t) + " : ");
        System.out.println("2st constant-conc eval(" + t + ") = " + se1.eval(t * 10.0));
      }
    }
    if (isl.measurement.view.BatchControl.keyExists("-df", args, 0)) {
      se1 = new ScriptEval("isl/model/dose-function.js");
      se2 = new ScriptEval("isl/model/dose-function.js");
      se1.scope.put("initial_pool", 1000.0);
      se1.scope.put("started", false);
      se1.scope.put("prdraw", 1.00);
      se2.scope.put("initial_pool", 1000.0);
      se2.scope.put("started", false);
      se2.scope.put("prdraw", 0.50);
      
      for (double t = 0.0; t < 500.0; t += 10.0) {
        System.out.print("dose-function 1 eval(" + t + ") = " + se1.eval(t) + " : ");
        System.out.println("dose-function 2 eval(" + t + ") = " + se2.eval(t));
      }
    }
  }
  private static void print_ei_gradient_summary(String args[]) {
    ScriptEval se1 = null;
    double max_time = 21600;
    if (isl.measurement.view.BatchControl.keyExists("-maxtime", args, 0)) {
      String maxtime_s = isl.measurement.view.BatchControl.argumentForKey("-maxtime", args, 0);
      if (maxtime_s != null) {
        max_time = Double.parseDouble(maxtime_s);
      } else {
        System.err.println("Could not parse -maxtime argument. Using default = " + max_time);
      }
    }
    double min_dist = 0;
    if (isl.measurement.view.BatchControl.keyExists("-mindist", args, 0)) {
      String mindist_s = isl.measurement.view.BatchControl.argumentForKey("-mindist", args, 0);
      if (mindist_s != null) {
        min_dist = Double.parseDouble(mindist_s);
      } else {
        System.err.println("Could not parse -mindist argument.  Using default = " + min_dist);
      }
    }
    double max_dist = 1000;
    if (isl.measurement.view.BatchControl.keyExists("-maxdist", args, 0)) {
      String maxdist_s = isl.measurement.view.BatchControl.argumentForKey("-maxdist", args, 0);
      if (maxdist_s != null) {
        max_dist = Double.parseDouble(maxdist_s);
      } else {
        System.err.println("Could not parse -maxdist argument.  Using default = " + max_dist);
      }
    }
    double max_length = max_dist;
    if (isl.measurement.view.BatchControl.keyExists("-maxlength", args, 0)) {
      String maxlength_s = isl.measurement.view.BatchControl.argumentForKey("-maxlength", args, 0);
      if (maxlength_s != null) {
        max_length = Double.parseDouble(maxlength_s);
      } else {
        System.err.println("Could not parse -maxlength argument.  Using default = " + max_length);
      }
    }

    se1 = new ScriptEval("isl/model/ei-gradient-function.js");
    se1.scope.put("max_time", max_time);
    se1.scope.put("max_length", max_length);
    
    StringBuilder sb = new StringBuilder("ei-gradient-summary");
    for (String s : args) sb.append(s);
    isl.io.OutputLog ol = new isl.io.OutputLog(sb.append(".csv").toString(), false);
    ol.mon("Time");
    for (double dNdx=min_dist ; dNdx <= max_dist ; dNdx++) ol.mon(", "+dNdx);
    ol.monln("");

    cern.colt.list.DoubleArrayList gvals = new cern.colt.list.DoubleArrayList((int)Math.round(max_time*(max_dist-min_dist)));
    double mean = Double.NaN, min = Double.NaN, max = Double.NaN;
    for (double tNdx = 0.0; tNdx <= max_time; tNdx++) {
      ol.mon(Double.toString(tNdx));
      for (double dNdx = min_dist; dNdx <= max_dist; dNdx++) {
        se1.scope.put("distance_from_PV", dNdx);
        double gval = se1.eval(tNdx);
        ol.mon(", "+gval);
        gvals.add(gval);
      }
      ol.monln("");
    }
    mean = cern.jet.stat.Descriptive.mean(gvals);
    min = cern.jet.stat.Descriptive.min(gvals);
    max = cern.jet.stat.Descriptive.max(gvals);
    for (String s : args) System.out.print(s+" ");
    System.out.println(": mean = "+mean+", min = "+min+", max = "+max);
  }
  */
}
