/* 
 * Copyright 2016 - Regents of the University of California, San
 * Francisco.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */

/* global Java */
var jsException = Java.type("javax.script.ScriptException");
function check(k, v, ref) {
  // exception for array & length & length match
  if (k.length !== v.length) throw new jsException("|keys| ≠ |vals|") ;
  if (k.length !== Object.keys(ref).length) throw new jsException("Expected "+Object.keys(ref).length+" parameters.  Got "+k.length);
  for (var ndx=0 ; ndx<k.length ; ndx++) {
    // exceptions for types
    if (ref[k[ndx]] === null) throw new jsException("Invalid Parameter: "+k[ndx]);
    else if (!(v[ndx] instanceof ref[k[ndx]])) 
      throw new jsException("Type Error! Got: "+v[ndx]+", Expected instanceof: "+ref[k[ndx]]);
  };
}
  