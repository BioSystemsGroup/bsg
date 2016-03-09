/*
 * Copyright 2014-2016 - Regents of the University of California, San
 * Francisco.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */
package bsg.util;

import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

public class SRControl {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger( SRControl.class );
    
    public static final String DELIM = ".";
    public static final String NUMISLPARAMS = "numislparams";
    public static final String NUMDELIVERYPARAMS = "numdeliveryparams";
    public static final String ISL = "isl";
    public static final String DELIVERY = "delivery";
    public static final String PARAM = "param";
    public static final String TAG = "tag";
    public static final String DOSE = "dose";
    public static final String NUMDOSES = "numDoses";
    public static final String SOLUTE = "solute";
    public static final String PROPERTY = "property";
    public static final String NUMPROPS = "numProps";
    public static final String NAME = "name";
    public static final String VARY = "vary";
    public static final String DEFAULT = "default";
    public static final String KEY = "key";
    public static final String VAL = "val";
    public static final String TYPE = "type";
    public static final String REACTIONPRODUCTS = "rxnProducts";
    public static final String MAP = "map";
    public static final String MIN = "min";
    public static final String MAX = "max";
    public static final String NAN = "nan";
    public static final String INF = "inf";
    
    // database of the parameters to vary in the SR
    public ec.util.ParameterDatabase SRpd = null;
    // database of the deilvery parameters
    public ec.util.ParameterDatabase Deliverypd = null;
    // database of the isl paramaters
    public ec.util.ParameterDatabase ISLpd = null;
    
    /*
    * constructor
    */
    public SRControl() {
        // load parameters to vary in SR from file "SRparameters.properties"
        SRpd = loadSRParams();
        // load parameters from "delivery.properties"
        Deliverypd = loadDeliveryParams();
        // load parameters from "isl/isl.properties"
        ISLpd = loadISLParams();
    }
    
    // methods to load parameters into databases
    public ec.util.ParameterDatabase loadSRParams() {
        // "SRparameter.properties located in same directory as "batch_control.properties"
        try {
             SRpd = new ec.util.ParameterDatabase( this.getClass().getClassLoader().getResourceAsStream("SR.properties") );
            }   catch ( Exception e ) {
        System.err.println( e.getMessage() );
        System.exit( -1 );
        }
        return SRpd;
    }
    
    public ec.util.ParameterDatabase loadDeliveryParams() {
        try {
             Deliverypd = new ec.util.ParameterDatabase( this.getClass().getClassLoader().getResourceAsStream("isl/delivery.properties") );
            }   catch ( Exception e ) {
        System.err.println( e.getMessage() );
        System.exit( -1 );
        }
        return Deliverypd;
    }
    
    public ec.util.ParameterDatabase loadISLParams() {
        try {
             ISLpd = new ec.util.ParameterDatabase( this.getClass().getClassLoader().getResourceAsStream("isl/isl.properties") );
            }   catch ( Exception e ) {
        System.err.println( e.getMessage() );
        System.exit( -1 );
        }
        return ISLpd;
    }
    
    // methods to vary the parameters in databases
    public void varyRandom() {
        if (SRpd == null) {
            throw new RuntimeException("SRpd is null");
        }
        if (ISLpd == null) {
            throw new RuntimeException("ISLpd is null");
        } else {
            varyISLRandom();
        }
        if (Deliverypd == null) {
            throw new RuntimeException("Deliverypd is null");
        } else {
            varyDeliveryRandom();
        }
    }
    
    public void varyISLRandom() {
        ec.util.Parameter param = null;
        ec.util.Parameter islparam = null;
        String pstr = null;
        
        // set random number generator
        ec.util.MersenneTwisterFast rng = new ec.util.MersenneTwisterFast();
        
        // get number of isl params to vary in SR.properties
        if (!SRpd.exists(param = new ec.util.Parameter(NUMISLPARAMS), null)) {
                throw new RuntimeException(NUMISLPARAMS + "does not exist in SR.properties");
        }
        int numislparams = SRpd.getInt(param = new ec.util.Parameter(NUMISLPARAMS), null);
        if (numislparams < 1) {
          throw new RuntimeException(NUMISLPARAMS + " must be >= 1.");
        }
        
        // loop over SR parameters, find param in corresponding database,
        // and make random variation
        for (int i = 0; i < numislparams; i++) {
            String base = ISL + DELIM + i + DELIM;
            //log.debug("doing param base = " + base);
            pstr = base + NAME;
            if (!SRpd.exists(param = new ec.util.Parameter(pstr), null)) {
                throw new RuntimeException(pstr + "does not exist in SR.properties");
            }
            String name = SRpd.getString(param = new ec.util.Parameter(pstr), null);
            if (name == null || name.equals("")) {
                throw new RuntimeException(pstr + " must be non-null.");
            }
            islparam = new ec.util.Parameter(name);
            if (!ISLpd.exists(islparam, null)) {
                throw new RuntimeException(name + " does not exist in isl.properties");
            }
            
            // randomly decide to vary or keep default 
            // if false, default and if true, vary
            //boolean vary = rng.nextBoolean();
            //if (!vary) {
                // do nothing further, and continue to next parameter
                //continue;
            //}
            // check to see if the parameter is to be varied or not
            pstr = base + VARY;
            if (!SRpd.exists(param = new ec.util.Parameter(pstr), null)) {
                throw new RuntimeException(pstr + "does not exist in SR.properties");
            }
            boolean vary = SRpd.getBoolean(param = new ec.util.Parameter(pstr), null, false);
            if (!vary) {
                // do nothing further, and continue to next parameter
                continue;
            }
            
            // parse isl parameter for format, i.e. single (constant) or < , > (spatial)
            boolean spatial = false;
            boolean constant = false;
            String islasstr = ISLpd.getString(islparam, null);
            // if (islparam.matches("<.*,.*>")?
            if (islasstr.startsWith("<") && islasstr.endsWith(">")) {
                spatial = true;
                //String[] valstr = islpstr.split(",");
                //double v1 = Double.parseDouble(valstr[0].replace('<', ' '));
                //double v2 = Double.parseDouble(valstr[1].replace('>', ' '));
            } else if (islasstr.isEmpty()) {
                throw new RuntimeException("isl parameter" + islasstr + "is empty");
            }
            
            // get parameter type, either real, int, or boolean
            pstr = base + TYPE;
            if (!SRpd.exists(param = new ec.util.Parameter(pstr), null)) {
                throw new RuntimeException(pstr + "does not exist in SR.properties");
            }
            String type = SRpd.getString(param = new ec.util.Parameter(pstr),null);
            
            // get minimum and maximum value within each type and make variation
            String pstrmin = base + MIN;
            String pstrmax = base + MAX;
            if (!SRpd.exists(param = new ec.util.Parameter(pstrmin), null)) {
                throw new RuntimeException(pstrmin + "does not exist in SR.properties");
            }
            if (!SRpd.exists(param = new ec.util.Parameter(pstrmax), null)) {
                throw new RuntimeException(pstrmax + "does not exist in SR.properties");
            }
            if (type.equalsIgnoreCase("real")) {
                Double dmin = null;
                Double dmax = null;
                Double PVmin = null;
                Double PVmax = null;
                Double CVmin = null;
                Double CVmax = null;
                String minvalue = SRpd.getString(param = new ec.util.Parameter(pstrmin), null);
                //log.debug("minvalue = "+minvalue);
                String[] splitstr = minvalue.split(":");
                if (splitstr.length > 1) {
                    // only for spatial parameters
                    String PVstr = splitstr[0];
                    String CVstr = splitstr[1];
                    if (PVstr.equalsIgnoreCase(NAN)) {
                        PVmin = Double.MIN_VALUE;
                    } else if (PVstr.equalsIgnoreCase(INF)) {
                        PVmin = Double.MIN_VALUE;
                    } else {
                        PVmin = Double.valueOf(PVstr);
                        //log.debug("the PVmin value for " +pstrmin+ " = " + PVmin.toString());
                    }
                    if (CVstr.equalsIgnoreCase(NAN)) {
                        CVmin = Double.MIN_VALUE;
                    } else if (CVstr.equalsIgnoreCase(INF)) {
                        CVmin = Double.MIN_VALUE;
                    } else {
                        CVmin = Double.valueOf(CVstr);
                        //log.debug("the CVmin value for " +pstrmin+ " = " + CVmin.toString());
                    }
                } else {
                    if (spatial) {
                        constant = true; 
                    }
                    if (minvalue.equalsIgnoreCase(NAN)) {
                        dmin = Double.MIN_VALUE;
                    } else if (minvalue.equalsIgnoreCase(INF)) {
                        dmin = Double.MIN_VALUE;
                    } else {
                        dmin = SRpd.getDouble(param = new ec.util.Parameter(pstrmin), null, 0.0);
                        //log.debug("the dmin value for " +pstrmin+ " = " + dmin.toString());
                    }      
                } 
                // if (min < 0) throw new RuntimeException("SR parameter" + pstrmin + "is < 0");
                String maxvalue = SRpd.getString(param = new ec.util.Parameter(pstrmax), null);
                //log.debug("maxvalue = "+maxvalue);
                splitstr = maxvalue.split(":");
                if (splitstr.length > 1) {
                    // only for spatial parameters
                    String PVstr = splitstr[0];
                    String CVstr = splitstr[1];
                    if (PVstr.equalsIgnoreCase(NAN)) {
                        PVmax = Double.MAX_VALUE;
                    } else if (PVstr.equalsIgnoreCase(INF)) {
                        PVmax = Double.MAX_VALUE;
                    } else {
                        PVmax = Double.valueOf(PVstr);
                        //log.debug("the PVmax value for " +pstrmin+ " = " + PVmax.toString());
                    }
                    if (CVstr.equalsIgnoreCase(NAN)) {
                        CVmax = Double.MAX_VALUE;
                    } else if (CVstr.equalsIgnoreCase(INF)) {
                        CVmax = Double.MAX_VALUE;
                    } else {
                        CVmax = Double.valueOf(CVstr);
                        //log.debug("the CVmax value for " +pstrmin+ " = " + CVmax.toString());
                    }
                } else {
                    if (spatial) {
                        constant = true;
                    } 
                    if (maxvalue.equalsIgnoreCase(NAN)) {
                        dmax = Double.MAX_VALUE;
                    } else if (maxvalue.equalsIgnoreCase(INF)) {
                        dmax = Double.MAX_VALUE;
                    } else {
                        dmax = SRpd.getDouble(param = new ec.util.Parameter(pstrmax), null, 0.0);
                        //log.debug("the dmax value for " +pstrmax+ " = " + dmax.toString());
                    }     
                }
                if (spatial) {
                    if (constant) {
                        // PV = CV
                        Double value = dmin + (dmax - dmin)*rng.nextDouble(); 
                        ISLpd.set(islparam, "<" + value.toString() + "," + value.toString() + ">");
                    } else {
                        Double PVvalue1 = PVmin + (PVmax - PVmin)*rng.nextDouble();
                        Double CVvalue2 = CVmin + (CVmax - CVmin)*rng.nextDouble();
                        ISLpd.set(islparam, "<" + PVvalue1.toString() + "," + CVvalue2.toString() + ">");
                    }
                } else {
                    Double value = dmin + (dmax - dmin)*rng.nextDouble(); 
                    ISLpd.set(islparam, value.toString());
                }
            } else if (type.equalsIgnoreCase("int")) {
                Integer imin = null;
                Integer imax = null;
                Integer PVmin = null;
                Integer PVmax = null;
                Integer CVmin = null;
                Integer CVmax = null;
                String minvalue = SRpd.getString(param = new ec.util.Parameter(pstrmin), null);
                //log.debug("minvalue = "+minvalue);
                String[] splitstr = minvalue.split(":");
                if (splitstr.length > 1) {
                    // only for spatial parameters
                    String PVstr = splitstr[0];
                    String CVstr = splitstr[1];
                    if (PVstr.equalsIgnoreCase(NAN)) {
                        PVmin = Integer.MIN_VALUE;
                    } else if (PVstr.equalsIgnoreCase(INF)) {
                        PVmin = Integer.MIN_VALUE;
                    } else {
                        PVmin = Integer.valueOf(PVstr);
                        //log.debug("the PVmin value for " +pstrmin+ " = " + PVmin.toString());
                    }
                    if (CVstr.equalsIgnoreCase(NAN)) {
                        CVmin = Integer.MIN_VALUE;
                    } else if (CVstr.equalsIgnoreCase(INF)) {
                        CVmin = Integer.MIN_VALUE;
                    } else {
                        CVmin = Integer.valueOf(CVstr);
                        //log.debug("the CVmin value for " +pstrmin+ " = " + CVmin.toString());
                    }
                } else {
                    if (spatial) {
                        constant = true;
                    }
                    if (minvalue.equalsIgnoreCase(NAN)) {
                        imin = Integer.MIN_VALUE;
                    } else if (minvalue.equalsIgnoreCase(INF)) {
                        imin = Integer.MIN_VALUE;
                    } else {
                        imin = SRpd.getInt(param = new ec.util.Parameter(pstrmin), null, 0);
                        //log.debug("the imin value for " +pstrmin+ " = " + imin.toString());
                    }
                }
                // if (min < 0) throw new RuntimeException("SR parameter" + pstrmin + "is < 0");
                String maxvalue = SRpd.getString(param = new ec.util.Parameter(pstrmax), null);
                //log.debug("maxvalue = "+maxvalue);
                splitstr = maxvalue.split(":");
                if (splitstr.length > 1) {
                    // only for spatial parameters
                    String PVstr = splitstr[0];
                    String CVstr = splitstr[1];
                    if (PVstr.equalsIgnoreCase(NAN)) {
                        PVmax = Integer.MAX_VALUE;
                    } else if (PVstr.equalsIgnoreCase(INF)) {
                        PVmax = Integer.MAX_VALUE;
                    } else {
                        PVmax = Integer.valueOf(PVstr);
                        //log.debug("the PVmax value for " +pstrmin+ " = " + PVmax.toString());
                    }
                    if (CVstr.equalsIgnoreCase(NAN)) {
                        CVmax = Integer.MAX_VALUE;
                    } else if (CVstr.equalsIgnoreCase(INF)) {
                        CVmax = Integer.MAX_VALUE;
                    } else {
                        CVmax = Integer.valueOf(CVstr);
                        //log.debug("the CVmax value for " +pstrmin+ " = " + CVmax.toString());
                    }
                } else {
                    if (spatial) {
                        constant = true;
                    }
                    //log.debug("maxvalue = "+maxvalue);
                    if (maxvalue.equalsIgnoreCase(NAN)) {
                        imax = Integer.MAX_VALUE;
                    } else if (maxvalue.equalsIgnoreCase(INF)) {
                        imax = Integer.MAX_VALUE;
                    } else {
                        //imax = SRpd.getInt(param = new ec.util.Parameter(pstrmax), null);
                        imax = SRpd.getIntWithMax(param = new ec.util.Parameter(pstrmax), null, 0, Integer.MAX_VALUE);
                        //log.debug("the imax value for " +pstrmax+ " = " + imax.toString());
                    }
                }
                if (spatial) {
                    if (constant) {
                        // PV = CV
                        Integer value = imin + rng.nextInt(imax - imin + 1); 
                        ISLpd.set(islparam, "<" + value.toString() + "," + value.toString() + ">");
                    } else {
                        Integer value1 = PVmin + rng.nextInt(PVmax - PVmin + 1);
                        Integer value2 = CVmin + rng.nextInt(CVmax - CVmin + 1);
                        ISLpd.set(islparam, "<" + value1.toString() + "," + value2.toString() + ">");
                    } 
                } else { 
                    //log.debug("imax = " + imax.toString() + " param number = " + i);
                    Integer value = imin + rng.nextInt(imax - imin + 1);
                    ISLpd.set(islparam, value.toString());
                }
            } else if (type.equalsIgnoreCase("boolean")) {
                if (spatial) {
                    // this probably will never happen, but...
                    String[] valstr = islasstr.split(",");
                    String v1 = valstr[0].replace('<', ' ');
                    v1 = v1.trim();
                    String v2 = valstr[1].replace('>', ' ');
                    v2 = v2.trim();
                    if (v1.equalsIgnoreCase("true") && v2.equalsIgnoreCase("true")) {
                        ISLpd.set(islparam, "<" + "false" + "," + "false" + ">");
                    } else if (v1.equalsIgnoreCase("true") && v2.equalsIgnoreCase("false")) {
                        ISLpd.set(islparam, "<" + "false" + "," + "true" + ">");
                    } else if (v1.equalsIgnoreCase("false") && v2.equalsIgnoreCase("true")) {
                        ISLpd.set(islparam, "<" + "true" + "," + "false" + ">");
                    } else if (v1.equalsIgnoreCase("false") && v2.equalsIgnoreCase("false")) {
                        ISLpd.set(islparam, "<" + "true" + "," + "true" + ">");
                    }
                } else {
                    boolean islvalue = ISLpd.getBoolean(islparam, null, false);
                    if (islvalue) {
                        ISLpd.set(islparam, "false");
                    } else {
                        ISLpd.set(islparam, "true");
                    }
                    //if (islasstr.equalsIgnoreCase("true")) {
                    //    ISLpd.set(islparam, "false");
                    //} else {
                    //    ISLpd.set(islparam, "true");
                    //}   
                }
            } else throw new RuntimeException("type must be real, int, or boolean"); 
            // would a switch case be faster? 
            //switch (type) {
            //    case "real": 
            //        break;
            //    case "int":
            //        break;
            //    case "boolean":
            //        break;
            //    default:
            //        break;
            //}
        }  
    }
    
    public void varyDeliveryRandom() {
        ec.util.Parameter param = null;
        ec.util.Parameter srparam = null;
        ec.util.Parameter deliveryparam = null;
        String pstr = null;
        String[] splitstr = null;
        
        // set random number generator
        ec.util.MersenneTwisterFast rng = new ec.util.MersenneTwisterFast();
        
        // get number of delivery params to vary in SR.properties
        if (!SRpd.exists(param = new ec.util.Parameter(NUMDELIVERYPARAMS), null)) {
                throw new RuntimeException(NUMDELIVERYPARAMS + "does not exist in SR.properties");
        }
        int numdeliveryparams = SRpd.getInt(srparam = new ec.util.Parameter(NUMDELIVERYPARAMS), null);
        if (numdeliveryparams < 1) {
          throw new RuntimeException(NUMDELIVERYPARAMS + " must be >= 1.");
        }
        
        // parse entire database as String to find parameter name and value
        // pd.toString() is a method that prints the entire database as a 
        // comma separated String
        String pdstr = Deliverypd.toString();
        //log.debug("pdstr = "+pdstr);
        // remove starting and ending curly brackets or braces
        pdstr = pdstr.replace("{"," ");
        pdstr = pdstr.replace("}"," ");
        pdstr = pdstr.trim();
        //log.debug("pdstr = "+pdstr);
        // parse on commas but not ones between angle brackets
        // regex for "," but not for "," between < and > for spatial parameters
        String[] delivpstr = pdstr.split("(?<!\\<[\\d\\.]{0,20}),(?![\\d\\.]{0,20}\\>)");
        //ArrayList<ArrayList<String>> dpstr = new ArrayList<ArrayList<String>>();
        ArrayList<String> dpdname = new ArrayList<>();
        ArrayList<String> dpdvalue = new ArrayList<>();
        int dpcount = 0;
        for (String s : delivpstr) {
            s = s.trim();
            //log.debug("delivpstr = "+s+"\n");
            splitstr = s.split("=(?!\\>)");
            if (splitstr.length != 2) {
                throw new RuntimeException(s+" length != 2");
            }
            // str[0] contains delivery parameter name as string
            // str[1] contains delivery parameter value as string
            //log.debug("str[0] = "+splitstr[0]+"\n");
            dpdname.add(dpcount,splitstr[0]);
            //log.debug("str[1] = "+splitstr[1]+"\n");
            dpdvalue.add(dpcount,splitstr[1]);
            dpcount++;
        }
        //log.debug("dpdname = "+dpdname.toString());
        //log.debug("dpdvalue = "+dpdvalue.toString());
        //log.debug("dpcount = "+dpcount+"\n");
        //log.debug("size of dpdname = "+dpdname.size());
        //log.debug("size of dpdvalue = "+dpdvalue.size());
        
        
        // loop over SR parameters, find param in corresponding database,
        // and make random variation
        for (int i = 0; i < numdeliveryparams; i++) {
            String base = DELIVERY + DELIM + i + DELIM;
            //log.debug("doing param base = " + base);
            
            // randomly decide to vary or keep default 
            // if false, default and if true, vary
            //boolean vary = rng.nextBoolean();
            //if (!vary) {
                // do nothing further, and continue to next parameter
                //continue;
            //}
            // check to see if the parameter is to be varied or not
            pstr = base + VARY;
            if (!SRpd.exists(param = new ec.util.Parameter(pstr), null)) {
                throw new RuntimeException(pstr + "does not exist in SR.properties");
            }
            boolean vary = SRpd.getBoolean(srparam = new ec.util.Parameter(pstr), null, false);
            if (!vary) {
                // do nothing further, and continue to next parameter
                continue;
            }
            
            // get solute tag and name of the delivery parameter in SR.properties
            // process tag and name for dose
            pstr = base + TAG;
            if (!SRpd.exists(param = new ec.util.Parameter(pstr), null)) {
                throw new RuntimeException(pstr + "does not exist in SR.properties");
            }
            String tag = SRpd.getString(srparam = new ec.util.Parameter(pstr), null);
            if (tag == null || tag.equals("")) {
                throw new RuntimeException(pstr + " must be non-null.");
            }
            splitstr = tag.split(":");
            tag = splitstr[0];
            String tagdose = splitstr[1];
            //log.debug("tag = "+tag);
            //log.debug("tagdose = "+tagdose);
            ec.util.Parameter numDoses = new ec.util.Parameter(NUMDOSES);
            if (!Deliverypd.exists(numDoses, null)) {
                throw new RuntimeException("numDoses does not exist in delivery.properties");
            } 
            int ndoses = Deliverypd.getInt(numDoses, null);
            ArrayList<String> dosestr = new ArrayList<>();
            for (int j = 0; j < ndoses; j++) {
                dosestr.add(j,DOSE+DELIM+j+DELIM);
            }
            pstr = base + NAME;
            if (!SRpd.exists(param = new ec.util.Parameter(pstr), null)) {
                throw new RuntimeException(pstr + "does not exist in SR.properties");
            }
            String name = SRpd.getString(srparam = new ec.util.Parameter(pstr), null);
            if (name == null || name.equals("")) {
                throw new RuntimeException(pstr + " must be non-null.");
            }
            // use tag and name of SR parameter to find corresponding parameter
            // in delivery parameter database represented in dpdname, dpdvalue
            if (!dpdvalue.contains(tag)) {
                throw new RuntimeException(tag+" is not in delivery.properties");
            }
            int ioftag = -1;
            String begintagstr = null;
            String tagstr = null;
            if (tagdose.equalsIgnoreCase("dose1")) {
                begintagstr = dosestr.get(0);
                
            }
            if (tagdose.equalsIgnoreCase("dose2")) {
                begintagstr = dosestr.get(1);  
            }
            for (String s : dpdname) {
                if (s.startsWith(begintagstr) && s.endsWith(TAG)) {
                    String value = Deliverypd.getString(param = new ec.util.Parameter(s), null);
                    if (value.equalsIgnoreCase(tag)) {
                        ioftag = dpdname.indexOf(s);
                        tagstr = s;
                    } 
                }
            }
            if (tagstr == null) throw new RuntimeException("tag string = null");
            //String tagstr = dpdname.elementAt(ioftag);
            if (!tagstr.startsWith(begintagstr)) {
                    throw new RuntimeException(begintagstr+"is not the beginning of "+tagstr);
            }
            //log.debug("tagstr = "+tagstr);
            String tagstrwotag = tagstr.replace(TAG,"");
            tagstrwotag = tagstrwotag.trim();
            //log.debug("tagstrwotag = "+tagstrwotag);
            String mapRxnProduct = null;
            if (name.startsWith(REACTIONPRODUCTS)) {
                splitstr = name.split(":");
                name = splitstr[0];
                mapRxnProduct = splitstr[1];
                //log.debug("mapRxnProduct = "+mapRxnProduct);
            }
            //log.debug("name = "+name);
            String dpnamestr = null;
            String dptypestr = null;
            String dpvalstr = null;
            int iofname = -1;
            // prop to specify if delivery parameter is a property or not
            boolean prop = false;
            if (dpdname.contains(tagstrwotag+name)) {
                dpnamestr = tagstrwotag+name;
                iofname = dpdname.indexOf(dpnamestr);
                //log.debug("dpnamestr = "+dpnamestr);
                deliveryparam = new ec.util.Parameter(dpnamestr);
                if (!Deliverypd.exists(deliveryparam, null)) {
                    throw new RuntimeException(dpnamestr+" does not exist in delivery.properties");
                }
            } else {
                String propstr = tagstrwotag+NUMPROPS;
                // get number of isl params to vary in SR.properties
                int numProps = Deliverypd.getInt(param = new ec.util.Parameter(propstr), null);
                if (numProps < 1) {
                    throw new RuntimeException(numProps+" in delivery pd "+" must be >= 1.");
                }
                for (int j = 0; j < numProps; j++) {
                    String dpkeystr = tagstrwotag+PROPERTY+DELIM+j+DELIM+KEY;
                    String value = Deliverypd.getString(param = new ec.util.Parameter(dpkeystr), null);
                    if (value.equalsIgnoreCase(name)) {
                        prop = true;
                        dpnamestr = dpkeystr;
                        //log.debug("dpnamestr = "+dpnamestr);
                        //log.debug("value = "+value);
                        iofname = dpdname.indexOf(dpnamestr);
                        dptypestr = tagstrwotag+PROPERTY+DELIM+j+DELIM+TYPE;
                        //log.debug("dptypestr = "+dptypestr);
                        dpvalstr = tagstrwotag+PROPERTY+DELIM+j+DELIM+VAL;
                        //log.debug("dpvalstr = "+dpvalstr);
                        deliveryparam = new ec.util.Parameter(dpvalstr);
                        if (!Deliverypd.exists(deliveryparam, null)) {
                            throw new RuntimeException(dpvalstr+" does not exist in delivery.properties");
                        }
                    }    
                }
            }
            
            // if prop, get delivery parameter type using dptypestr  
            String dptype = null;
            String dpval = null;
            ArrayList<String> otherdosestr = null;
            boolean doseratio = false;
            if (prop) {
                // get type and value from delivery parameter database
                // types are real, int, string, boolean, or map
                dptype = Deliverypd.getString(param = new ec.util.Parameter(dptypestr), null);
                dpval = Deliverypd.getString(param = new ec.util.Parameter(dpvalstr), null);
            } else {
                // get SR parameter type, either real, int, or boolean
                // deliveryparam from dpnamestr contains the value
                pstr = base + TYPE;
                if (!SRpd.exists(srparam = new ec.util.Parameter(pstr), null)) {
                    throw new RuntimeException(pstr + "does not exist in SR.properties");
                }
                dptype = SRpd.getString(srparam = new ec.util.Parameter(pstr),null);
                dpval = Deliverypd.getString(deliveryparam,null);
                // if delivery parameter = doseRatio, need to know the ratios
                // of the other solutes of that dose because the sum must = 1.0
                if (name.equalsIgnoreCase("doseRatio")) {
                    doseratio = true;
                    otherdosestr = new ArrayList<String>();
                    for (String s : dpdname) {
                        String value = Deliverypd.getString(param = new ec.util.Parameter(s), null);
                        // begintagstr = dose.#.
                        // tagstrwotag = dose.#.solute.#.
                        if (s.startsWith(begintagstr) && s.endsWith(TAG) && !s.startsWith(tagstrwotag) && !value.equalsIgnoreCase(tag)) {
                            s = s.replace(TAG,"doseRatio");
                            otherdosestr.add(s);
                            //log.debug("other dose = "+s);
                        }
                    }
                }
                
            }
            //log.debug(dpnamestr+" = "+name);
            //log.debug(dptypestr+" = "+dptype);
            //log.debug(dpvalstr+" = "+dpval);
            
            // get minimum and maximum value for the delivery parameter 
            String pstrmin = base + MIN;
            String pstrmax = base + MAX;
            if (!SRpd.exists(srparam = new ec.util.Parameter(pstrmin), null)) {
                throw new RuntimeException(pstrmin + "does not exist in SR.properties");
            }
            if (!SRpd.exists(srparam = new ec.util.Parameter(pstrmax), null)) {
                throw new RuntimeException(pstrmax + "does not exist in SR.properties");
            }
            
            // if-else block to control dptype and make variation
            if (dptype.equalsIgnoreCase("real")) {
                Double dmin = Double.NaN;
                Double dmax = Double.NaN;
                String minvalue = SRpd.getString(srparam = new ec.util.Parameter(pstrmin), null);
                //log.debug("minvalue = "+minvalue);
                if (minvalue.equalsIgnoreCase(NAN)) { 
                    dmin = Double.MIN_VALUE;
                    // double max = Double.NaN ?
                } else if (minvalue.equalsIgnoreCase(INF)) {
                    dmin = Double.MIN_VALUE;
                    // double max = Double.NEGATIVE_INFINITY ?
                } else {
                    dmin = SRpd.getDouble(srparam = new ec.util.Parameter(pstrmin), null, 0.0);
                    //log.debug("the dmin value for " +pstrmin+ " = " + dmin.toString());
                }
                // if (min < 0) throw new RuntimeException("SR parameter" + pstrmin + "is < 0");
                String maxvalue = SRpd.getString(srparam = new ec.util.Parameter(pstrmax), null);
                //log.debug("maxvalue = "+maxvalue);
                if (maxvalue.equalsIgnoreCase(NAN)) {
                    dmax = Double.MAX_VALUE;
                    // double max = Double.NaN ?
                } else if (maxvalue.equalsIgnoreCase(INF)) {
                    dmax = Double.MAX_VALUE;
                    // double max = Double.POSITIVE_INFINITY ?
                } else {
                    dmax = SRpd.getDouble(srparam = new ec.util.Parameter(pstrmax), null, 0.0);
                    //log.debug("the dmax value for " +pstrmax+ " = " + dmax.toString());
                }
                Double value = dmin + (dmax - dmin)*rng.nextDouble();
                //log.debug("new value for "+dpnamestr+" = "+value.toString());
                // if doseratio, have to modify other solute doseratios so that
                // the sum = 1.0
                if (doseratio) {
                    int ndoser = 0;
                    ArrayList<String> drstr = new ArrayList<>();
                    for (String s : otherdosestr) {
                        Double v = Deliverypd.getDouble(param = new ec.util.Parameter(s), null, 0.0);
                        if (!v.equals(0.0)) {
                            ndoser++;
                            drstr.add(s);
                        }
                    }
                    Double ov = Deliverypd.getDouble(deliveryparam, null, 0.0);
                    Double dv = value - ov;
                    //log.debug("dv = "+dv);
                    //log.debug("ndoser = "+ndoser);
                    Double dosesum = value;
                    for (String s : drstr) {
                        Double v = Deliverypd.getDouble(param = new ec.util.Parameter(s), null, 0.0);
                        v = v - dv/(double)ndoser;
                        dosesum+=v;
                        //log.debug("new value for "+s+" = "+v.toString());
                        Deliverypd.set(param, v.toString());
                    }
                    if (ov.equals(1.0D) && ndoser == 0) {
                        Deliverypd.set(deliveryparam, ov.toString());
                    } else {
                        Deliverypd.set(deliveryparam, value.toString());
                        //log.debug("sum of doseratios = "+" for "+dpnamestr+" "+dosesum.toString());
                        if (!(MathUtils.equals(dosesum, 1.0, Double.MIN_VALUE)) ) {
                            throw new RuntimeException("sum of doseratio values "+dosesum+" for "+dpnamestr+" != 1.0");
                        }
                    }  
                } else {
                    Deliverypd.set(deliveryparam, value.toString());
                }  
            } else if (dptype.equalsIgnoreCase("int")) {
                Integer imin = Integer.MIN_VALUE;
                Integer imax = Integer.MAX_VALUE;
                String minvalue = SRpd.getString(srparam = new ec.util.Parameter(pstrmin), null);
                //log.debug("minvalue = "+minvalue);
                if (minvalue.equalsIgnoreCase(NAN)) {
                    imin = Integer.MIN_VALUE;
                } else if (minvalue.equalsIgnoreCase(INF)) {
                    imin = Integer.MIN_VALUE;
                } else {
                    imin = SRpd.getInt(srparam = new ec.util.Parameter(pstrmin), null, 0);
                    //log.debug("the imin value for " +pstrmin+ " = " + imin.toString());
                }
                // if (min < 0) throw new RuntimeException("SR parameter" + pstrmin + "is < 0");
                String maxvalue = SRpd.getString(srparam = new ec.util.Parameter(pstrmax), null);
                //log.debug("maxvalue = "+maxvalue);
                if (maxvalue.equalsIgnoreCase(NAN)) {
                    imax = Integer.MAX_VALUE;
                } else if (maxvalue.equalsIgnoreCase(INF)) {
                    imax = Integer.MAX_VALUE;
                } else {
                    //imax = SRpd.getInt(param = new ec.util.Parameter(pstrmax), null);
                    imax = SRpd.getIntWithMax(srparam = new ec.util.Parameter(pstrmax), null, 0, Integer.MAX_VALUE);
                    //log.debug("the imax value for " +pstrmax+ " = " + imax.toString());
                }
                //log.debug(new String("imax = " + imax.toString() + " param number = " + i));
                Integer value = imin + rng.nextInt(imax - imin + 1);
                //log.debug("new value = "+value.toString());
                Deliverypd.set(deliveryparam, value.toString());
            } else if (dptype.equalsIgnoreCase("boolean")) {
                boolean deliveryvalue = Deliverypd.getBoolean(deliveryparam, null, false);
                if (deliveryvalue) {
                    Deliverypd.set(deliveryparam, "false");
                    //log.debug("new value = false");
                } else {
                    Deliverypd.set(deliveryparam, "true");
                    //log.debug("new value = true");
                }
                
            } else if (dptype.equalsIgnoreCase("string")) {
                // this is for a very specific delivery parameter: rxnProbGradient
                // currently, there is only "sigmoid" or "linear"
                if (dpval.equalsIgnoreCase("sigmoid")) {
                    Deliverypd.set(deliveryparam, "linear");
                    //log.debug("new value = linear");
                }
                if (dpval.equalsIgnoreCase("linear")) {
                    Deliverypd.set(deliveryparam, "sigmoid");
                    //log.debug("new value = sigmoid");
                }
            } else if (dptype.equalsIgnoreCase("map")) {
                // variable "mapRxnProduct" contains the reaction product which 
                // spatial production needs to be varied
                // spatial production values between angle brackets: <#,#>
                // value type for prod. map = real, min = 0.0, max = 1.0
                boolean constant = false;
                Double PVmin = null;
                Double PVmax = null;
                Double CVmin = null;
                Double CVmax = null;
                String minvalue = SRpd.getString(srparam = new ec.util.Parameter(pstrmin), null);
                //log.debug("minvalue = "+minvalue);
                splitstr = minvalue.split(":");
                if (splitstr.length > 1) {
                    // only for spatial parameters
                    String PVstr = splitstr[0];
                    String CVstr = splitstr[1];
                    if (PVstr.equalsIgnoreCase(NAN)) {
                        PVmin = Double.MIN_VALUE;
                    } else if (PVstr.equalsIgnoreCase(INF)) {
                        PVmin = Double.MIN_VALUE;
                    } else {
                        PVmin = Double.valueOf(PVstr);
                        //log.debug("the PVmin value for " +pstrmin+ " = " + PVmin.toString());
                    }
                    if (CVstr.equalsIgnoreCase(NAN)) {
                        CVmin = Double.MIN_VALUE;
                    } else if (CVstr.equalsIgnoreCase(INF)) {
                        CVmin = Double.MIN_VALUE;
                    } else {
                        CVmin = Double.valueOf(CVstr);
                        //log.debug("the CVmin value for " +pstrmin+ " = " + CVmin.toString());
                    }
                } else {
                    constant = true;
                    if (minvalue.equalsIgnoreCase(NAN)) {
                        PVmin = Double.MIN_VALUE;
                        CVmin = PVmin;
                    } else if (minvalue.equalsIgnoreCase(INF)) {
                        PVmin = Double.MIN_VALUE;
                        CVmin = PVmin;
                    } else {
                        PVmin = SRpd.getDouble(param = new ec.util.Parameter(pstrmin), null, 0.0);
                        CVmin = PVmin;
                        //log.debug("the dmin value for " +pstrmin+ " = " + dmin.toString());
                    }
                }
                // if (min < 0) throw new RuntimeException("SR parameter" + pstrmin + "is < 0");
                String maxvalue = SRpd.getString(srparam = new ec.util.Parameter(pstrmax), null);
                //log.debug("maxvalue = "+maxvalue);
                splitstr = maxvalue.split(":");
                if (splitstr.length > 1) {
                    // only for spatial parameters
                    String PVstr = splitstr[0];
                    String CVstr = splitstr[1];
                    if (PVstr.equalsIgnoreCase(NAN)) {
                        PVmax = Double.MAX_VALUE;
                    } else if (PVstr.equalsIgnoreCase(INF)) {
                        PVmax = Double.MAX_VALUE;
                    } else {
                        PVmax = Double.valueOf(PVstr);
                        //log.debug("the PVmax value for " +pstrmin+ " = " + PVmax.toString());
                    }
                    if (CVstr.equalsIgnoreCase(NAN)) {
                        CVmax = Double.MAX_VALUE;
                    } else if (CVstr.equalsIgnoreCase(INF)) {
                        CVmax = Double.MAX_VALUE;
                    } else {
                        CVmax = Double.valueOf(CVstr);
                        //log.debug("the CVmax value for " +pstrmin+ " = " + CVmax.toString());
                    }
                } else {
                    constant = true;
                    if (maxvalue.equalsIgnoreCase(NAN)) {
                        PVmax = Double.MAX_VALUE;
                        CVmax = PVmax;
                    } else if (maxvalue.equalsIgnoreCase(INF)) {
                        PVmax = Double.MAX_VALUE;
                        CVmax = PVmax;
                    } else {
                        PVmax = SRpd.getDouble(param = new ec.util.Parameter(pstrmax), null, 0.0);
                        CVmax = PVmax;
                        //log.debug("the dmax value for " +pstrmax+ " = " + dmax.toString());
                    }
                }
                // check if mapRxnProduct is within dpval
                if (!dpval.contains(mapRxnProduct)) {
                    throw new RuntimeException(mapRxnProduct+" not in "+dpval);
                }
                Double value1 = null;
                Double value2 = null;
                if (constant) {
                    value1 = PVmin + (PVmax - PVmin)*rng.nextDouble();
                    value2 = value1;
                } else {
                    value1 = PVmin + (PVmax - PVmin)*rng.nextDouble();
                    //log.debug("value1 = "+value1.toString());
                    //String vstr1 = String.format("%01.3f", value1);
                    //log.debug("vstr1 = "+vstr1);
                    //value1 = Double.parseDouble(vstr1);
                    value2 = CVmin + (CVmax - CVmin)*rng.nextDouble();
                    //log.debug("value2 = "+value2.toString());
                    //String vstr2 = String.format("%01.3f", value2);
                    //log.debug("vstr2 = "+vstr2);
                    //value2 = Double.parseDouble(vstr2);
                }
                
                // parse reaction product production brackets in order to force 
                // the sum to equal 1.0
                String[] metprod = dpval.split(";");
                int nummet = metprod.length;
                //String nmetstr = String.format("%03d", nummet);
                //double nmet = Double.parseDouble(nmetstr);
                //log.debug("nmet = "+nmet);
                String[] met = new String[nummet];
                double[] pv1 = new double[nummet];
                double[] pv2 = new double[nummet];
                double[] v1 = new double[nummet];
                double[] v2 = new double[nummet];
                double dv1 = 0.0;
                double dv2 = 0.0;
                int metcount = 0;
                for (String s : metprod) {
                    s = s.trim();
                    splitstr = s.split("=>");
                    met[metcount] = splitstr[0];
                    met[metcount] = met[metcount].trim();
                    //log.debug("met = "+met[metcount]);
                    String prod = splitstr[1];
                    //log.debug("prod = "+prod);
                    prod = prod.trim();
                    splitstr = prod.split(",");
                    pv1[metcount] = Double.parseDouble(splitstr[0].replace('<', ' '));
                    //String pvstr1 = String.format("%01.3f", pv1[metcount]);
                    //pv1[metcount] = Double.parseDouble(pvstr1);
                    //log.debug("pv1 = "+pv1[metcount]);
                    pv2[metcount] = Double.parseDouble(splitstr[1].replace('>', ' '));
                    //String pvstr2 = String.format("%01.3f", pv2[metcount]);
                    //pv2[metcount] = Double.parseDouble(pvstr2);
                    //log.debug("pv2 = "+pv2[metcount]);
                    //log.debug("mapRxnProduct = "+mapRxnProduct);
                    if (met[metcount].equalsIgnoreCase(mapRxnProduct)) {
                        dv1 = value1 - pv1[metcount];
                        //String dvstr1 = String.format("%01.3f", dv1);
                        //dv1 = Double.parseDouble(dvstr1);
                        //log.debug("dv1 = "+dv1);
                        dv2 = value2 - pv2[metcount];
                        //String dvstr2 = String.format("%01.3f", dv2);
                        //dv2 = Double.parseDouble(dvstr2);
                        //log.debug("dv2 = "+dv2);
                    }
                    metcount++;
                }
                String bracket = "";
                String mp = "";
                String valstr = "";
                double sum1 = 0.0;
                double sum2 = 0.0;
                for (int j = 0; j < nummet; j++) {
                    if (met[j].equalsIgnoreCase(mapRxnProduct)) {
                        v1[j] = value1;
                        v2[j] = value2;
                    } else {
                        v1[j] = pv1[j] - dv1/(nummet-1.0);
                        v2[j] = pv2[j] - dv2/(nummet-1.0);
                    }
                    //log.debug("v1[j] = "+v1[j]);
                    //log.debug("v2[j] = "+v2[j]);
                    //bracket = "<"+v1[j].toString()+","+v2[j].toString()+">"+"; ";
                    //log.debug("String.format of v1[j] = "+String.format("%01.3f",v1[j]));
                    bracket = "<"+v1[j]+","+v2[j]+">"+"; ";
                    //bracket = "<"+String.format("%01.3f",v1[j])+","+String.format("%01.3f",v2[j])+">"+"; ";
                    mp = met[j]+" => "+bracket;
                    valstr+=mp;
                    //String v1str = String.format("%01.3f",v1[j]);
                    //v1[j] = Double.parseDouble(v1str);
                    //String v2str = String.format("%01.3f",v2[j]);
                    //v2[j] = Double.parseDouble(v2str);
                    //log.debug("v1[j] = "+v1[j]);
                    //log.debug("v2[j] = "+v2[j]);
                    sum1+=v1[j];
                    sum2+=v2[j];
                }
                //log.debug("sum "+"=> <"+sum1+", "+sum2+">");
                double ssum1 = 0.0;
                double ssum2 = 0.0;
                for (int j = 0; j < nummet; j++) {
                    v1[j] = v1[j]*(1.0/sum1);
                    v2[j] = v2[j]*(1.0/sum2);
                    ssum1+=v1[j];
                    ssum2+=v2[j];
                }
                //log.debug("ssum "+"=> <"+ssum1+", "+ssum2+">");
                if (!(MathUtils.equals(ssum1, 1.0, Double.MIN_VALUE) && MathUtils.equals(ssum2, 1.0, Double.MIN_VALUE)) ) {
                        throw new RuntimeException("sum of map values <"+sum1+","+sum2+"> != 1.0");
                        //Exception in thread "main" java.lang.RuntimeException: sum of map values <1.0000000000000002,1.0> != 1.0
                }
                // maybe should use StringBuilder for valstr here
                int index = valstr.lastIndexOf(";");
                valstr = valstr.substring(0, index);
                //log.debug("valstr = "+valstr);
                //String valstr = dpval.replace(mapRxnProduct+" => ", mapRxnProduct+" => "+bracket);
                Deliverypd.set(deliveryparam, valstr);
            } else {
                throw new RuntimeException("type must be real, int, string, map, or boolean");
            }
                    
            // would a switch case be faster? 
            //switch (type) {
            //    case "real": 
            //        break;
            //    case "int":
            //        break;
            //    case "boolean":
            //        break;
            //    default:
            //        break;
            //}
        }
    }
    
    // methods to write all parameters, including the ones varied, back to their files
    public void writeParamFiles() throws java.io.FileNotFoundException, java.io.IOException {
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("build/classes/isl/isl.properties")));
        ISLpd.list(pw);
        pw = new PrintWriter(new BufferedWriter(new FileWriter("build/classes/isl/delivery.properties")));
        Deliverypd.list(pw);
        //int xp = 1;
        //writeISLpd(xp);
    }
    
    private static final String expBaseLocation = "../exp";
    public void writeISLpd(int xp) throws java.io.FileNotFoundException, java.io.IOException {
        String xp_s = String.format("%03d", xp);

        java.io.File outDir = new java.io.File(expBaseLocation);
        if (!outDir.exists()) outDir.mkdir();
        outDir = new java.io.File(expBaseLocation+"/"+xp_s);
        if (!outDir.exists()) outDir.mkdir();
        //java.io.File outFile = new java.io.File( expBaseLocation+"/"+xp_s+"/isl.properties");
        //java.io.FileOutputStream outStream = new java.io.FileOutputStream(outFile);
        //ISLpd.store(outStream, "Experiment"+xp_s);
        //outStream.close();
        // alternative method
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(expBaseLocation+"/"+xp_s+"/isl.properties"))); 
        ISLpd.list(pw);
    }
    
    public void writeDeliverypd(int xp) throws java.io.FileNotFoundException, java.io.IOException {
        String xp_s = String.format("%03d", xp);

        java.io.File outDir = new java.io.File(expBaseLocation);
        if (!outDir.exists()) outDir.mkdir();
        outDir = new java.io.File(expBaseLocation+"/"+xp_s);
        if (!outDir.exists()) outDir.mkdir();
        //java.io.File outFile = new java.io.File( expBaseLocation+"/"+xp_s+"/delivery.properties");
        //java.io.FileOutputStream outStream = new java.io.FileOutputStream(outFile);
        //Deliverypd.store(outStream, "Experiment"+xp_s);
        //outStream.close();
        // alternative method
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(expBaseLocation+"/"+xp_s+"/delivery.properties"))); 
        Deliverypd.list(pw);
    }

    public static void main(String[] args) {
        System.out.println("Running SRControl.");
        SRControl sr = new SRControl();
        //System.err.println("\nprinting all SR parameters\n");
        //sr.SRpd.list(new PrintWriter(System.err, true));
        //System.err.println("\nprinting all isl parameters\n");
        //sr.ISLpd.list(new PrintWriter(System.err, true));
        //System.err.println("\nprinting all delivery parameters\n");
        //sr.Deliverypd.list(new PrintWriter(System.err, true));
        // get number of SR experiments to create in SR.properties
        ec.util.Parameter param = null;
        int numSRexps = sr.SRpd.getInt(param = new ec.util.Parameter("numSRexps"), null);
        if (numSRexps < 1) {
          throw new RuntimeException("numSRexps must be >= 1.");
        }
        for (int xp=0 ; xp<numSRexps ; xp++) {
            sr.varyRandom();
            //sr.varyISLRandom();
            //sr.varyDeliveryRandom();
            //System.err.println("\nprinting all isl parameters after varying\n");
            //sr.ISLpd.list(new PrintWriter(System.err, true));
            //System.err.println("\nprinting all delivery parameters after varying\n");
            //sr.Deliverypd.list(new PrintWriter(System.err, true));
            //System.err.println("\nwriting parameters to files\n");
            try {
                sr.writeISLpd(xp);
                sr.writeDeliverypd(xp);
            } catch (Exception e) { System.err.println("Could not write Parameter files."); }
        }
        System.out.println("Finished.");
    }
}