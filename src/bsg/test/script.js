/* 
 * Copyright 2016 - Regents of the University of California, San
 * Francisco.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */

var jsException = Java.type("javax.script.ScriptException");

//parameters -- need not be ordered
var keys,vals;

// expected keys → types -- must be ordered
var eMap = {}
eMap["you"] = Java.type("java.lang.Double");
eMap["are"] = Java.type("java.lang.Boolean");
eMap["blue"] = Java.type("java.lang.String");


// exception for array & length & length match
if (keys.length != vals.length) throw new jsException("|keys| ≠ |vals|") ;
if (keys.length != Object.keys(eMap).length) throw new jsException("Expected "+Object.keys(eMap).length+" parameters.  Got "+keys.length);
for (var ndx=0 ; ndx<keys.length ; ndx++) {
  // exceptions for types
  if (eMap[keys[ndx]] == null) throw new jsException("Invalid Parameter: "+keys[ndx]);
  else if (!(vals[ndx] instanceof eMap[keys[ndx]])) 
    throw new jsException("Type Error! Got: "+vals[ndx]+", Expected instanceof: "+eMap[keys[ndx]]);
  print(keys[ndx]+" → "+vals[ndx]);
};

var model_time;

output = model_time;
