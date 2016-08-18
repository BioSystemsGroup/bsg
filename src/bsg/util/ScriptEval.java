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
    script = scriptLocation;
    try { engine.eval("load(\"classpath:bsg/util/preamble.js\")", scope); }
    catch (ScriptException se) { throw new RuntimeException("Couldn't load preamble.js.", se); }
    
    try { engine.eval("load(\""+script+"\");", scope); }
    catch (ScriptException se) { throw new RuntimeException("Couldn't load "+script+".", se); }
  }
  
  public double eval() throws ScriptException {
    double retVal = Double.NaN;
    Object result = null;
    
    result = engine.eval("eval();",scope);
    if (result instanceof Double)
      retVal = (Double)result;
    else
      retVal = ((Integer)result).doubleValue();
    return retVal;
  }

  public static void main(String args[]) {
    ScriptEval se1 = null;
    se1 = new ScriptEval("classpath:bsg/test/script.js");
    
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

}
