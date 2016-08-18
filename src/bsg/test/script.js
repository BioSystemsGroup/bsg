/* 
 * Copyright 2016 - Regents of the University of California, San
 * Francisco.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */
//parameters -- need not be ordered
var keys,vals;

// expected keys → types -- must be ordered
var eMap = {}
eMap["you"] = Java.type("java.lang.Double");
eMap["are"] = Java.type("java.lang.Boolean");
eMap["blue"] = Java.type("java.lang.String");

function eval() {
  check(keys, vals, eMap);
  return Math.E;
}
