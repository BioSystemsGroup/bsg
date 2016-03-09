/*
 * Copyright 2003-2016 - Regents of the University of California, San
 * Francisco.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 */

package bsg.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.beans.PropertyDescriptor;

import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


// TBD: Should ObjectUtils extend ClassUtils or should they be separate?
//      Or perhaps they should be conbined into one class?


/**
 * TBD: Add class description
 *
 * @future
 *    - 
 *
 * @author Ken Cline
 * @see Object
 * @since 1.0
 * @version $Revision: $  $Date: $
 *
 * $Id: $
 */
public class ObjectUtils extends ClassUtils
{

  // TBD: Add doc
  private static final Logger log = LoggerFactory.getLogger( ObjectUtils.class );


  // === [ Bean-related Utilities ] =======================================

  /**
   * TBD: Add documentation
   *
   */
  protected static Map<Class,Map<String,PropertyDescriptor>> propertyDescriptors
    = new java.util.LinkedHashMap<Class,Map<String,PropertyDescriptor>>();


  /**
   * TBD: Add documentation
   *
   * TBD: Possibly create a registry of property name comparators,
   *      i.e. a Map<Class,Comparator<String>>, and then, before
   *      creating an empty map, we check to see if there is a
   *      comparator defined.  If so, then use the comparator
   *      when creating the empty property descriptor map.
   *
   */
  public static Map<String,PropertyDescriptor> createEmptyPropertyDescriptorMap
    ( Class cls )
  {
    //log.debug( "Creating empty property descriptor map for class '{}'", cls ); 

    //return new java.util.LinkedLinkedLinkedHashMap<String,PropertyDescriptor>();

    return new java.util.TreeMap<String,PropertyDescriptor>();
  }


  /**
   * TBD: Add documentation
   *
   * TBD: We could generalize this to take a 'type' param that specified
   *      which descriptor type to get, i.e. Property, Method, EventSet,
   *      Bean, Parameter or Feature (all of the above).  The 'type'
   *      param would be a bit mask and all descriptors of the type(s)
   *      specified would be put into the map.
   *
   * TBD: Import my Filter framework which include the definition of
   *      PropertyDescriptorFilter, i.e. an object that accepts/rejects
   *      PropertyDescriptors.  As I recall I had a factory method that
   *      allowed the user to create such filters from a list of names
   *      e.g.
   *         filter = createPropertyDescriptorFilter( REJECT, "class" );
   *         createPropertyDescriptorMap( cls, filter );
   *
   * TBD: Create registry of filters, i.e. Map<Class,PropertyDescriptorFilter>,
   *      and a mechanism for initializing the registry via a configuration
   *      file (possibly).  Like logging, filtering could be inherited
   *      or the user could possibly override that.
   *
   */
  public static Map<String,PropertyDescriptor> createPropertyDescriptorMap
    ( Class cls )
  {
    Map<String,PropertyDescriptor>  map   = null;
    java.beans.BeanInfo             info  = null;
    String                          name  = null;

    try {
      //log.debug( "Creating property descriptor map for class '{}'", cls ); 

      map = createEmptyPropertyDescriptorMap( cls );

      info = java.beans.Introspector.getBeanInfo( cls );
      for ( PropertyDescriptor pd : info.getPropertyDescriptors() ) {
        // HACK HACK
        // TBD: Use a (user-specified or global) PropertyDescriptorFilter
        //      instead of hardcoded checks
        name = pd.getName();
        if ( "class".equals(name) ) {
          continue;
        }

        map.put( name, pd );
      }

      //log.debug( "Created property descriptor map for class '{}': {}",
      //           cls, map ); 

    } catch ( java.beans.IntrospectionException ie ) {
      log.error( "Unable to get BeanInfo for class '{}' -- Error: {};"
               + " returning empty Map", cls, ie );
      // TBD: Suppress this error for now... but it might be better to
      //      allow the exception to propagate instead
      // throw new RuntimeException( "Unable to get BeanInfo"
      //                           + " for class '" + cls + "'", ie );
    }

    return map;
  }


  /**
   * TBD: Add documentation
   *
   */
  public static synchronized Map<String,PropertyDescriptor>
    getPropertyDescriptorMap ( Class cls )
  {
    Map<String,PropertyDescriptor>  map  = null;

    //log.debug( "Retrieving the property descriptor map for class '{}'", cls ); 

    map = propertyDescriptors.get( cls );
    if ( map == null ) {
      map = createPropertyDescriptorMap( cls );
      propertyDescriptors.put( cls, map );
    }

    //log.debug( "Property descriptor map for class '{}': {}", cls, map ); 

    return map;
  }

  /**
   * TBD: Add documentation
   *
   */
  public static Map<String,PropertyDescriptor>
    getPropertyDescriptorMap ( Object obj )
  {
    return getPropertyDescriptorMap( obj.getClass() );
  }


  /**
   * TBD: Add documentation
   *
   */
  public static Set<String> getPropertyNames ( Class cls )
  {
    try {
      return getPropertyDescriptorMap( cls ).keySet();
    } catch ( Exception e ) {
      // Since this is meant to be a convenience method, I've
      // decided to suppress exceptions for the caller.
      // I may change my mind about this though. :)
      String msg = "Unable to retrieve property descriptor map"
                 + " for '" + cls + "'"
                 + " -- returning empty key set instead";
      log.error( msg, e );
    }

    return Collections.emptySet();
  }

  /**
   * TBD: Add documentation
   *
   */
  public static Set<String> getPropertyNames ( Object obj )
  {
    return getPropertyNames( obj.getClass() );
  }


  /**
   * TBD: Add documentation
   *
   */
  public static String listPropertyDescriptors ( Class cls )
  {
    Map<String,PropertyDescriptor>  map   = null;
    PropertyDescriptor              pd    = null;
    String                          fmt   = "\t%s: %s\n";
    String                          list  = "";

    //log.debug( "Listing property descriptors for class '{}'", cls ); 

    map = getPropertyDescriptorMap( cls );

    list = "Properties for class '" + toString(cls) + "':\n";
    for ( String key : map.keySet() ) {
      list += String.format( fmt, key, toString(map.get(key)) );
    }

    return list;
  }


  /**
   * TBD: Add documentation
   *
   */
  public static PropertyDescriptor getPropertyDescriptor ( Class cls,
                                                           String name )
  {
    Map<String,PropertyDescriptor>  map  = null;
    PropertyDescriptor              pd   = null;

    //log.debug( "Looking up the '{}' property descriptor for class '{}'",
    //           name, cls ); 

    map = getPropertyDescriptorMap( cls );
    pd = map.get( name );
    // TBD: Check is 'pd' is null and if so then ???

    //log.debug( "Descriptor for class '{}', property '{}': {}",
    //           new Object[]{cls,name,pd} ); 

    return pd;
  }


  /**
   * TBD: Add documentation
   *
   */
  public static PropertyDescriptor getPropertyDescriptor ( Object obj,
                                                           String name )
  {
    return getPropertyDescriptor( obj.getClass(), name );
  }


  /**
   * TBD: Add documentation
   *
   */
  public static String toString ( Class cls ) {
    if ( cls == null ) {
      return "null";
    }
    return cls.getSimpleName();
  }


  /**
   * TBD: Add documentation
   *
   */
  public static String toString ( Method mthd ) {
    StringBuilder sb = new StringBuilder();

    if ( mthd == null ) {
      return "null";
    }

    for ( Class<?> param : mthd.getParameterTypes() ) {
      if ( sb.length() > 0 ) sb.append( "," );
      sb.append( toString(param) );
    }
    sb.insert( 0, "(").append( ")" );
    sb.insert( 0, mthd.getName() );

    return sb.toString();
  }


  /**
   * TBD: Add documentation
   *
   */
  public static String toString ( PropertyDescriptor pd )
  {
    StringBuilder sb      = new StringBuilder();
    StringBuilder attrs   = new StringBuilder();
    Class<?>      type    = null;
    Class<?>      editor  = null;

    if ( pd == null ) {
      return "null";
    }

    //attrs.append( "(" );
    //for ( String name : Collections.list(pd.attributeNames()) ) {
    //  attrs.append( name ).append( "=" ).append( pd.getValue(name) );
    //}
    //attrs.append( ")" );

    type = pd.getPropertyType();
    //editor = pd.getPropertyEditorClass();

    sb.append( toString(pd.getClass()) ); //.append( "@" ).append( pd.hashCode() );

    sb.append( "{ " );
    sb.append( "name="       ).append( pd.getName()           ).append( ", " );
    sb.append( "type="       ).append( toString(type)         ).append( ", " );
    sb.append( "display="    ).append( pd.getDisplayName()    ).append( ", " );
    //sb.append( "description=" ).append( pd.getShortDescription() ).append( ", " );
    //sb.append( "getter="     ).append( pd.getReadMethod()     ).append( ", " );
    //sb.append( "setter="     ).append( pd.getWriteMethod()    ).append( ", " );
    sb.append( "getter="   ).append( toString(pd.getReadMethod())  ).append( ", " );
    sb.append( "setter="   ).append( toString(pd.getWriteMethod()) ).append( ", " );
    //sb.append( "editor="     ).append( toString(editor)       ).append( ", " );
    //sb.append( "expert="     ).append( pd.isExpert()          ).append( ", " );
    //sb.append( "hidden="     ).append( pd.isHidden()          ).append( ", " );
    //sb.append( "preferred="   ).append( pd.isPreferred()      ).append( ", " );
    sb.append( "bound="       ).append( pd.isBound()          ).append( ", " );
    sb.append( "constrained=" ).append( pd.isConstrained()    ).append( ", " );
    //sb.append( "attributes="  ).append( attrs );
    sb.append( " }" );

    return sb.toString();
  }


  /**
   * TBD: Add documentation
   *
   */
  public static Method getReadMethod ( Object obj, String name ) {
    PropertyDescriptor  pd      = null;
    Method              getter  = null;
    String              msg     = null;

    if ( obj == null ) {
      msg = "Invalid Argument -- Target object is null;"
          + " unable to get read method for property '" + name + "'";
      // TBD: Throw exception or return null?
      throw new NullPointerException( msg );
    }

    pd = getPropertyDescriptor( obj, name );
    if ( pd == null ) {
      log.warn( "No descriptor exists for property '{}' in class '{}'"
              + " -- the property may not exist or this class may not"
              + " follow the Beans naming conventions; returning null",
                name, obj.getClass() );
      return null;
    }

    getter = pd.getReadMethod();

    return getter;
  }


  /**
   * TBD: Add documentation
   *
   */
  public static Method getWriteMethod ( Object obj, String name, Object value )
  {
    PropertyDescriptor  pd      = null;
    Method              setter  = null;
    String              msg     = null;

    if ( obj == null ) {
      msg = "Invalid Argument -- Target object is null;"
          + " unable to get write method for property '" + name + "'";
      // TBD: Throw exception or return null?
      throw new NullPointerException( msg );
    }

    pd = getPropertyDescriptor( obj, name );
    if ( pd == null ) {
      log.warn( "No descriptor exists for property '{}' in class '{}'"
              + " -- the property may not exist or this class may not"
              + " follow the Beans naming conventions; returning null",
                name, obj.getClass() );
      return null;
    }

    setter = pd.getWriteMethod();

    // TBD: Check is param type for the default write method take a
    //      value assignable from value.getClass and, if not, then
    //      search for other setters with the same name that do take
    //      that parameter type

    return setter;
  }


  // === [ Object Properties Getter and Setter ] ==========================

  /**
   * TBD: Add documentation
   *
   */
  public static Object get ( Object obj, String name ) {
    Method  getter  = null;
    Object  value   = null;
    String  msg     = null;

    try {
      getter = getReadMethod( obj, name );
      if ( getter == null ) {
        log.warn( "No read method exists for property '{}' in class '{}'"
                + " -- the property is not readable; returning null",
                  name, obj.getClass() );
        return null;
      }

      value = getter.invoke( obj );
      //log.debug( "Value '{}' from object '{}' (class: '{}'): {}",
      //           new Object[]{name,obj,obj.getClass(),value} );

      return value;

    } catch ( RuntimeException re ) {
      log.error( "Unable to get property '{}' from object '{}' (class: '{}')"
                 + " -- Error: {}", new Object[]{name,obj,obj.getClass(),re} );
      // TBD: Should we return null or throw an exception???
      //      Going with exception for now...
      throw re;
    } catch ( Exception e ) {
      msg = "Unable to get property '" + name + "' from object '" + obj + "'"
          + " (class: '" + obj.getClass() + "')";
      log.error( msg + " -- Error: {}", e );
      // TBD: Should we return null or throw an exception???
      //      Going with exception for now...
      throw new RuntimeException( msg, e );
    }

  }


  /**
   * TBD: Add documentation
   *
   */
  public static void set ( Object obj, String name, Object value ) {
    Method  setter  = null;
    String  msg     = null;

    try {
      setter = getWriteMethod( obj, name, value );
      if ( setter == null ) {
        msg = "No write method exists for property '" + name + "'"
            + " in class '" + obj.getClass() + "'"
            + " -- the property is not writable";
        throw new UnsupportedOperationException( msg );
      }

      value = toType( value, setter.getParameterTypes()[0] );
      setter.invoke( obj, value );
      //log.debug( "Set property '{}' in object '{}' (class: '{}')"
      //         + " to value: '{}'",
      //           new Object[]{name,obj,getClsName(obj),value} );
      log.debug( String.format( "Set property '%s' to value: '%s' (class: '%s')"
                              + " in object '%s' (class: '%s')",
                                name, value, getClsName(value),
                                obj, getClsName(obj) ) );

    } catch ( RuntimeException re ) {
      log.error( "Unable to set property '{}' in object '{}' (class: '{}')"
               + " to value '{}'"
                 + " -- Error: {}",
                 new Object[]{name,obj,getClsName(obj),value,re} );
      throw re;
    } catch ( Exception e ) {
      msg = "Unable to set property '" + name + "' in object '" + obj + "'"
          + " (class: '" + getClsName(obj) + "') to value: '" + value + "'";
      log.error( msg + " -- Error: {}", e );
      // TBD: Should we return null or throw an exception???
      //      Going with exception for now...
      throw new RuntimeException( msg, e );
    }

  }

  /**
   * TBD: Add documentation
   * TBD: Add an optional ErrorHandler arg (more below)
   *
   */
  @SuppressWarnings( "unchecked" )
  public static Map get ( Object obj, Iterator keys, Map values ) {
    Object  key    = null;
    Object  value  = null;

    if ( values == null ) {
      values = new java.util.LinkedHashMap();
    }

    while ( keys.hasNext() ) {
      key = keys.next();
      value = get( obj, key.toString() );
      values.put( key, value );
    }

    return values;
  }

  /**
   * TBD: Add documentation
   * TBD: Add an optional ErrorHandler arg (more below)
   *
   */
  public static Map get ( Object obj, Collection keys ) {
    return get( obj, keys.iterator(), null );
  }

  /**
   * TBD: Add documentation
   * TBD: Add an optional ErrorHandler arg (more below)
   *
   */
  public static Map get ( Object obj, String ... keys ) {
    return get( obj, Arrays.asList(keys) );
  }


  /**
   * TBD: Add documentation
   * TBD: Add an optional ErrorHandler arg (more below)
   *      Note: If we have an ErrorHandler arg, then we
   *            could use it for property names that are
   *            skipped because there's no matching entry
   *            in values, i.e. throw an exception when
   *            containsKey returns false.  We'd want to
   *            choose an exception type that could be
   *            easily distinguished from other failure
   *            modes.
   *
   */
  public static void set ( Object obj, Map values, Set<String> skipped ) {
    Object value = null;

    for ( String key : getPropertyNames(obj) ) {
      if ( ! values.containsKey(key) ) {
        log.debug( "No value provided for property '{}' of object '{}'"
                 + " -- skipping property...", key, obj );
        if ( skipped != null ) skipped.add( key );
        continue;
      }
      value = values.get( key );
      set( obj, key, value );
    }
  }

  /**
   * TBD: Add documentation
   *
   */
  public static void set ( Object obj, Map values ) {
    set( obj, values, null );
  }


  // NOTE: The 'set' methods above implement the "pull-style" processing
  //       while the 'setAll' methods below implement the "push-style".
  //
  //       I think in most cases the "pull-style" is what users would
  //       prefer because the Map of values can contain anything, e.g.
  //       the entire contents of a .properties configuration file and
  //       the 'set' method only pulls out what it needs for the specified
  //       object being initialized.  Another advantage is that the
  //       'values' Map decides whether it contains a particular property
  //       key.  If you want to do some simple key name transformations,
  //       e.g. put everything in lower case or add a prefix or suffix
  //       to the key name, then you can do this easily by overriding
  //       the Map's 'get' and 'containsKey' methods, e.g. with an inner
  //       class.
  //
  //       While the "pull-style" might be the best choice in most cases,
  //       there are advantages to using a "push-style".  For example,
  //       if the 'values' Map was significantly smaller than the list
  //       of all properties for an object.  Another advantage would be
  //       if you want all the entries in the 'values' Map to be used
  //       and if they can't be set then you want an exception to be
  //       thrown.
  //

  /**
   * TBD: Add documentation
   * TBD: Add an optional ErrorHandler arg (more below)
   *
   */
  public static void setAll ( Object obj, Map values ) {
    Object value = null;

    for ( Object key : values.keySet() ) {
      value = values.get( key );
      set( obj, key.toString(), value );
    }
  }

  /**
   * TBD: Add documentation
   * TBD: Add an optional ErrorHandler arg (more below)
   *
   */
  public static void setAll ( Object obj, Object[] ... values ) {
    String key    = null;
    Object value  = null;

    for ( Object[] entry : values ) {
      key = entry[0].toString();  // allow NPE if entry[0] is null
      value = ( entry.length > 1 ? entry[1] : null );
      set( obj, key, value );
    }
  }


  // === [ Object Converter Methods ] =====================================

  /**
   * TBD: Add documentation
   *
   */
  public static Class<?> normalizeType ( Class<?> type ) {
    if ( type == null )  return null;
    if ( type == Boolean.TYPE   || type == Boolean.class   ) return Boolean.class;
    if ( type == Character.TYPE || type == Character.class ) return Character.class;
    if ( type == Byte.TYPE      || type == Byte.class      ) return Byte.class;
    if ( type == Short.TYPE     || type == Short.class     ) return Short.class;
    if ( type == Integer.TYPE   || type == Integer.class   ) return Integer.class;
    if ( type == Long.TYPE      || type == Long.class      ) return Long.class;
    if ( type == Float.TYPE     || type == Float.class     ) return Float.class;
    if ( type == Double.TYPE    || type == Double.class    ) return Double.class;
    if ( type == Void.TYPE      || type == Void.class      ) return Void.class;
    return type;
  }


  /**
   * TBD: Add documentation
   *
   */
  public static Object toPrimitive ( Number obj, Class<?> type ) {

    // log.debug( "Converting Number instance '{}' to primitive type '{}'",
    //            obj, type );

    if ( obj == null ) {
      throw new InvalidConversionException( obj, type,
                         "Can not convert null to a primitive type" );
      //log.warn( "No object provided -- 'obj' argument is null;"
      //        + " returning null" );
      //return obj;
    }

    if ( type == null ) {
      throw new InvalidConversionException( obj, type, "No type specified" );
    }

    type = normalizeType( type );

    if ( type.isInstance(obj) ) {
      // log.debug( "Object '{}' is already an instance of type '{}'"
      //          + " returning object unchanged", obj, type );
      return obj;
    }

    if ( type == Boolean.class  ) return new Boolean( (obj.intValue() != 0) );
    if ( type == Byte.class     ) return new Byte(     obj.byteValue()      );
    if ( type == Short.class    ) return new Short(    obj.shortValue()     );
    if ( type == Integer.class  ) return new Integer(  obj.intValue()       );
    if ( type == Long.class     ) return new Long(     obj.longValue()      );
    if ( type == Float.class    ) return new Float(    obj.floatValue()     );
    if ( type == Double.class   ) return new Double(   obj.doubleValue()    );

    if ( type == Character.class ) {
      int codePt = obj.intValue();
      if ( Character.charCount(codePt) > 1 ) {
        throw new InvalidConversionException( obj, type,
                          "integer value of source object can not be"
                        + " represented as a single char value" );
      }
      return new Character( (char)codePt ); 
    }

    if ( type == Void.class ) {
      throw new InvalidConversionException( obj, type,
                 "objects can not be converted to VOID type" );
    }

    throw new InvalidConversionException( obj, type,
                            "unknown primitive type '" + type + "'" );
  }

  /**
   * TBD: Add documentation
   *
   */
  public static Object toPrimitive ( String str, Class<?> type ) {

    //log.debug( "Converting string '{}' to primitive type '{}'", str, type );

    if ( str == null ) {
      throw new InvalidConversionException( str, type,
                         "Can not convert null to a primitive type" );
      //log.warn( "No object provided -- 'str' argument is null;"
      //        + " returning null" );
      //return str;
    }

    if ( type == null ) {
      throw new InvalidConversionException( str, type, "No type specified" );
    }

    type = normalizeType( type );

    if ( type == Boolean.class  ) return new Boolean(  str  );
    if ( type == Byte.class     ) return new Byte(     str  );
    if ( type == Short.class    ) return new Short(    str  );
    if ( type == Integer.class  ) return new Integer(  str  );
    if ( type == Long.class     ) return new Long(     str  );
    if ( type == Float.class    ) return new Float(    str  );
    if ( type == Double.class   ) return new Double(   str  );

    if ( type == Character.class ) {
      str = str.trim();
      if ( str.codePointCount(0,str.length()) > 1 ) {
        throw new InvalidConversionException( str, type,
                          "source object can not be"
                        + " represented as a single char value" );
      }
      return new Character( str.charAt(0) ); 
    }

    if ( type == Void.class ) {
      throw new InvalidConversionException( str, type,
                 "objects can not be converted to VOID type" );
    }

    throw new InvalidConversionException( str, type,
                            "unknown primitive type '" + type + "'" );
  }

  /**
   * TBD: Add documentation
   *
   */
  public static Object toPrimitive ( Object obj, Class<?> type ) {
    //log.debug( "Converting source object '{}' to primitive type '{}'",
    //           obj, type );

    if ( obj == null ) {
      throw new InvalidConversionException( obj, type,
                         "Can not convert null to a primitive type" );
    }

    if ( obj instanceof Number ) {
      return toPrimitive( (Number)obj, type );
    }

    return toPrimitive( obj.toString(), type );
  }


  /**
   * TBD: Add documentation
   *
   */
  public static Object toType ( Object obj, Class<?> type ) {
    //Class<?> cls = null;

    //log.debug( "Converting object '{}' ({}) to type '{}'",
    //           new Object[]{obj,getClass(obj),type} );

    if ( obj == null ) {
      log.warn( "No object provided -- 'obj' argument is null;"
              + " returning null" );
      return obj;
    }

    if ( type == null ) {
      log.warn( "No type specified -- 'type' argument is null;"
              + " returning object '{}' unchanged", obj );
      return obj;
    }

    if ( type.isInstance(obj) ) {
      log.debug( "Object '{}' is already an instance of type '{}'"
               + " returning object unchanged", obj, type );
      return obj;
    }

    // cls = obj.getClass();
    // if ( type.isAssignableFrom(cls) ) {
    //   log.debug( "Object '{}' is already an instance of type '{}'"
    //            + " (i.e. type '{}' is assignable from '{}')"
    //            + "returning object unchanged",
    //              new Object[]{obj,type,type,cls} );
    //   return obj;
    // }

    if ( type.isPrimitive() || Number.class.isAssignableFrom(type) ) {
      return toPrimitive( obj, type );
    }

    if ( type == String.class ) {
      return obj.toString();
    }

    try {
      return createInstance( type, obj );
    } catch ( Exception e ) {
      throw new InvalidConversionException( obj, type, e );
    }

  }


  // === [ Main Method (for testing only) ] ===============================
  /* commented out as we separate bsg.util from isl.*
  public static void main ( String[] args ) {
    Class [] classes = { isl.model.ref.ConventionalCDModel.class,
                         isl.model.ref.ExtendedCDModel.class,
                       };
    Object obj    = null;
    Object value  = null;
    Object[][] settings = { { "dn", "0.000" },
                            { "k1", "0.111" },
                            { "k2", "0.222" },
                            { "ke", "0.333" },
                            { "t",   0.444 },
                            { "m",  new Double( 0.555 ) },  // new Float( 0.555 ) },
                            { "q",  "0.555" },  // new Double( 0.666 ) },
                            { "a",  new Complex( 1.0, 2.0 ) },
                            { "b",  "0.123 + 0.555i" },
                          };

    Map<String,Object> settingsMap = new java.util.TreeMap<String,Object>();
    for ( Object[] param : settings ) {
      settingsMap.put( (String)param[0], param[1] );
    }


    for ( Class cls : classes ) {
      try {
        //log.debug( "\n{}\n", listPropertyDescriptors(cls) );

        obj = cls.newInstance();

        //for ( String key : getPropertyDescriptorMap(cls).keySet() ) {
        //  value = get( obj, key );
        //  log.debug( "Class '{}', property '{}', value: '{}'",
        //             new Object[]{cls,key,value} );
        //}

        // for ( Object[] param : settings ) {
        //   set( obj, (String)param[0], param[1] );
        //   // log.debug( "Class '{}', property '{}' set to value: '{}'",
        //   //            new Object[]{cls,param[0],param[1]} );
        // }

        //set( obj, settings );
        set( obj, settingsMap );
        //setAll( obj, settingsMap );

      } catch ( Exception e ) {
        log.error( "Unable to test get and set methods"
                 + " for class '" + cls + "'", e );
      }
    }


  }
  */

}  // end of ObjectUtils class


// TBD: Perhaps some (all) of these methods belong in a utility class
//      more aptly named "BeanUtils" instead of ObjectUtils???


// TO DO:
//    * Add toString for PropertyDescriptor
//    * Add tests for getting properties
//    * Add support for specifying a comparator for get and set
//    * Complete set method
//    * Add tests for setting properties
//    * Add set method that takes a Map
//    * Add get method that takes a Set and returns a Map
//    * Add a copy method with flag to ignore errors
//


// ErrorHandler interface/classes
// ------------
// TBD: Define an ErrorHandler interface which the 'set' method
//      and other "batch-style" methods can call when a particular
//      step/element fails.  There would be several pre-defined
//      implementations:
//         FailFast -- re-throws any exception so the processing
//             stops on the first error
//         IgnoreAll -- consumes all throwables
//         IgnoreRunnables -- consumes all RuntimeExceptions
//         IgnoreSome -- consumes throwable if it is in a pre-specified
//             list of types to ignore
//         FailOn -- if exception is in a pre-specified list then
//             fail immediately, otherwise ignore
//         Buffering -- stores errors and (optionally) fails at
//             the end if any (or a particular type) of error has
//             occurred
//      Perhaps "buffering" should be a capability provided by
//      an abstract super/base class for ErrorHandlers so the
//      user could enable/disable buffering for any handler.
//
//      In addition to having a 'handle(Throwable)' method, we
//      probably need a way to tell the ErrorHandler when processing
//      has started (e.g. a 'start', 'begin', 'reset' or 'open' method)
//      and stopped (e.g. a 'stop', 'end' or 'close' method).  This
//      allows the handler to maintain state and implement behavior
//      such as "buffer all errors and then fail on the last error"
//      or "fail if more than one error".
//
//      We may also want to be able to store other task related
//      information within the handler, e.g. the Collection object
//      being "batch" processed, so that the handler can include
//      those relevant details in its logging and exception messages.
//


// Additional Get/Set Convenience Methods
// --------------------------------------
// TBD: Other convenience methods for getting/setting object properties?
//      Perhaps:
//         set( Object obj, String[][] values )
//         set( Object obj, Object[] ... values )
//         set( Object obj, Iterator<Map.Entry> values )
//
//      We could also support getting/setting object properties directly
//      from streams, e.g.
//         load( Object obj, InputStream stream )
//         load( Object obj, Reader reader )
//         save( Object obj, OutputStream stream )
//         save( Object obj, Writer writer )
//
//      The stream related methods really belong in an IO Utilities class
//      instead of ObjectUtils.  Also, w.r.t. the stream reading operation,
//      it would be nice to design the code to only read those properties
//      that belong to the object from the stream and stop reading once
//      it encounters an entry for another object.

// Get/Set Logic Design: Transforms, Push vs. Pull & etc
// ---------------------
// TBD: Create a Map wrapper that allows the user to specify transformations
//      for either the key or value or both.  The purpose is make easy to
//      set properties from input that may not have correct variable names
//      or contains prefixes/suffixes on the variable names.
//
//      For example, suppose you have a properties file that has all the
//      settings for object X but the property names have a prefix "x."
//      or may be upper case.  If we had a transform map class, then
//      we could possibly do something like:
//         transform = new TransformMap<String,Object> ( properties ) {
//           public String transformKey ( String key ) {
//             key = trimPrefix( key, "x." );
//             key = key.toLowerCase();
//             return key;
//           }
//         }; 
//
//         set( obj, transform );
//
//      Of course simple key transformations could be accomplished with
//      a Comparator.  That is, if the user is allowed to specify a
//      name comparator then the user can provide an instance of Comparator
//      that ignores case and/or performs some other type of manipulation
//      to decide if the provide name pair is "equal".
//
//      However, if we need to do something like combine multiple inputs
//      to create a value for one of the objects properties, then we'd
//      need something more sophisticated than a Comparator.  That is,
//      a Comparator only supports 1-1 mappings from ivar name to the
//      map key whereas a transform class could support many-to-1 or
//      1-to-many mappings.
//
//      More I think about it, the getter/setter code above should be
//      re-written to do a "pull-style" instead of a "push-style"
//      transfer.  That is, we should iterate on the object's property
//      names and then we ask the Map if it contains a key for that
//      property name.  This puts the control in the hands of the Map
//      argument and so we don't need to worry about transforming the
//      maps keys to match the property names, instead we transform
//      the property name to see if it matches a map key.  There isn't
//      really that much difference, but the "pull-style" operation is
//      (imho) a little cleaner and more flexible.
//
//      It could even be argued that the base 'get( Object obj, String name )'
//      and 'set( Object obj, String name, Object value )' methods should
//      take Object instead of String argument types for the name parameter.
//      The advantage is same as described above: "pulling" gives the
//      control to name argument to decide if it is the same as the
//      property name, e.g.
//          PropertyDescriptor pd = ...
//            ...
//          name.equals( pd.getName() );
//      instead of
//          PropertyDescriptor pd = ...
//            ...
//          pd.getName.equals( name );
//
//      This design though would mean that we'd iterate through all
//      the properties for every get operation.  That's a major drop
//      in efficiency over a Map lookup but if it only impacts one-time
//      object initialization then maybe we could live with it.
//
//      Anyway I'll keep what I have and take a wait and see on whether
//      to re-write to create something more flexible (but less efficient).
//

// Get/Set Logic Design: Defaults
// ---------------------
// TBD: Add support for specifying default values when setting and getting
//      properties from objects.  This makes the more sense if we switch
//      to a "pull-style" initialization.  That is, in the batch set method
//      we would loop through all of the object's properties and for each
//      check if the provided 'values' map contains a setting.  To this
//      we could add a 2nd defaults map that would be queried _if_ the 1st
//      'values' map does not have an entry for a particular property.
//
//      The most general way to implement this would be to create an
//      implementation of Map that combines two (or more) other Maps,
//      i.e. a primary and a secondary.  Or it could be a List of Maps,
//      if we want to go a step further.  With this class the user can
//      construct the Map with a backing "defaults" map if they wish or
//      we could still do it as a convenience.
//
