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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TBD: Add class description
 *
 * @future -
 *
 * @author Ken Cline
 * @see Class
 * @since 1.0
 * @version $Revision: $ $Date: $
 *
 * $Id: $
 */
public class ClassUtils {

  // TBD: Add doc
  private static final Logger log = LoggerFactory.getLogger(ClassUtils.class);

  // === [ Class and Class Name Utilities ] ===============================
  
  /**
   * Convenience method, mainly for logging and exception construction, so
   * caller doesn't have to worry about NPEs
   */
  public static Class<?> getClass(Object obj) {
    // Quick-n-dirty right now...  Might add more to this later
    return (obj == null ? null : obj.getClass());
  }

  /**
   * Convenience method, mainly for logging and exception construction, so
   * caller doesn't have to worry about NPEs
   */
  public static String getClsName(Object obj) {
    // Quick-n-dirty right now...  Might add more to this later
    return (obj == null ? "null" : obj.getClass().getSimpleName());
  }

  /**
   * Checks if string is a fully-qualified class name
   */
  public static boolean isFQN(String name) {
    // Quick-n-dirty right now...  Might add more to this later
    return (name == null ? false : name.indexOf('.') >= 0);
  }
  // TBD: Other than verifying that name contains a '.', what else
  //      should we do to check if the name is fully-qualified?
  //      We could check if the class exists using Class.forName
  //      or ClassLoader.findClass.  But if that fails, does it
  //      necessarily imply that the name is not fully-qualified?

  // TBD: Perhaps we should just call this method 'containsDot'
  //      (and move it to the StringUtils class) because that's
  //      a better definition of what we are doing.
  // TBD: And/Or perhaps we should change this method to be
  //      ISL specific: i.e. check if the class name starts with
  //      "isl.".  If so, then should this method be called
  //      something like: isISLClassName ?
  /**
   * TBD: Add documentation
   *
   */
  public static boolean isInstanceOfAny(Object obj, Class<?>... classes) {

    for (Class<?> cls : classes) {
      if (cls.isInstance(obj)) {
        //log.debug( "Object '{}' is an instance of '{}' -- returning true",
        //           obj, cls );
        return true;
      }
    }

    //log.debug( "Object '{}' was NOT an instance of any specified classes: {}",
    //           obj, classes );
    return false;
  }

  public static boolean isInstanceOfAll(Object obj, Class<?>... classes) {

    for (Class<?> cls : classes) {
      if (!cls.isInstance(obj)) {
        log.debug("Object '{}' is NOT an instance of '{}' -- returning false",
                obj, cls);
        return false;
      }
    }

    log.debug("Object '{}' is an instance of all specified classes: {}",
            obj, classes);
    return true;
  }

  // TBD: isInstanceOf{Any,All} methods are a special case of Collection
  //      Filter utilities acceptsAny and acceptsAll (not yet implemented).
  //      The Collection Filter utilities apply Filter objects to a
  //      collection, e.g. array, List, Set, Map and etc.  Here we would
  //      be applying the InstanceOfFilter (not yet implemented).
  //
  //      Once again I have all this code sitting on CDs somewhere, written
  //      year ago.  I need to look for ASAP. [sigh]
  public static Class<?> findClass(String pkg, String name)
          throws ClassNotFoundException {
    Class<?> cls = null;

    if (!isFQN(name)) {
      if (StringUtils.isNotEmpty(pkg)) {
        //log.debug( "Provided class name '{}' is not fully qualified"
        //         + " -- prepending package name provided as context: '{}'",
        //           name, pkg );
        name = pkg + '.' + name;
      } else {
        log.warn("Unable to create a fully qualified class name"
                + " -- No parent package provided as context;"
                + " attempting to find class using unqualified name: '{}'",
                name);
      }
    }

    cls = Class.forName(name);
    //log.debug( "Found class for name '{}' and package '{}'", name, pkg );

    return cls;
  }  // end --  findClass( String pkg, String name )

  public static Class<?> findClass(Object ctx, String name)
          throws ClassNotFoundException {
    //log.debug( "Searching for class with context: '{}' and name: '{}'",
    //           ctx, name );

    // TBD: Should we check if name is fully qualified and, if so,
    //      skip the remainder of this method?
    if (isFQN(name)) {
      return findClass("", name);
    }

    if (ctx == null) {
      // TBD: Once I complete the 'getCallerClass' method, then we can
      //      dynamically determine the package of the caller and
      //      assume that non-fully-qualified names should belong to
      //      the caller's package
      // ctx = getCallerClass( ClassUtils.class, -1 );

      //      For now though, we'll just default to no context
      ctx = "";
    }

    if (ctx instanceof String) {
      return findClass((String) ctx, name);
    }

    // For any context object _other_ than a Class or Package,
    // get the object's class
    if (!isInstanceOfAny(ctx, Class.class, Package.class)) {
      ctx = ctx.getClass();
    }

    // If context is a class, then get its parent package
    if (ctx instanceof Class) {
      ctx = ((Class) ctx).getPackage();
    }

    // If context is a package, then get its name
    if (ctx instanceof Package) {
      ctx = ((Package) ctx).getName();
    }

    // No matter what we started with, the context object should now
    // be a string by the time we get here; but just in case...
    if (!(ctx instanceof String)) {
      ctx = ctx.toString();
    }

    return findClass((String) ctx, name);

  }  // end --  findClass( Object ctx, String name )

  // === [ Factory (Instance Construction) Methods ] ======================
  // TBD: Do these method belong in Class utilities or Object utilities?
  //      I originally put them in ObjectUtils, but then decided to move
  //      them to ClassUtils... I might move them back.
  public static Object createInstance(Class<?> cls, Class[] types,
          Object... args)
          throws NoSuchMethodException,
          InstantiationException,
          IllegalAccessException,
          InvocationTargetException {
    Constructor cnstr = null;
    String msg = null;

    //log.debug( "Creating instance of '{}' with args: {} (types: {})",
    //           new Object[]{cls,args,types} );
    cnstr = cls.getConstructor(types);

    // TBD: Check if args.length == types.length
    //      If shorter, then fill with null.
    //      If longer, then throw an exception.
    // TBD: Check if args[i].class instanceof types[i] for each i
    //      If not assignable class, then use 'toType' to convert.
    //      If we add this, then we need to check for recursion,
    //      i.e. toType --> ... --> createInstance --> toType --> ...
    return cnstr.newInstance(args);

  }  // end --  createInstance( Class<?> cls, Class [] types, Object ... args )

  public static Object createInstance(Class<?> cls, Object... args)
          throws NoSuchMethodException,
          InstantiationException,
          IllegalAccessException,
          InvocationTargetException {
    Class[] types = null;
    String msg = null;
    int n = (args == null ? 0 : args.length);
    Object arg = null;
    boolean allStrings = true;

    //log.debug( "Creating instance of '{}' with args: {}", cls, args );
    types = new Class[n];
    for (int i = 0; i < n; i++) {
      if (args[i] == null) {
        msg = "Unable to create instance of '" + cls + "'"
                + " -- Constructor parameter type can not be determined"
                + " for arg " + i + " (null)";
        throw new IllegalArgumentException(msg);
      }
      types[i] = args[i].getClass();
      allStrings = (allStrings && (types[i] == String.class));
    }

    // If all the arguments are Strings then we just take one shot
    // at finding the constructor and creating a new instance...
    if (allStrings) {
      return createInstance(cls, types, args);
    }

    // ... Otherwise, we'll try to find the constructor with a
    // signature that most closely matches the provided argument
    // list.  If no matching constructor is found, then we'll
    // try to find a constructor that takes all strings instead.
    // TBD: If we wanted to be a little more clever, we could
    //      convert elements from args one at a time to a String
    //      and after each conversion try to find a constructor.
    //      However, to do it correctly, we also need to check
    //      whether args[i] is already a String in which case
    //      we need to skip to the next k such that args[k] is
    //      not already a String.  The logic is bit tricky but
    //      if I did the loop right, we could also get rid of
    //      the 'allStrings' flag.  Perhaps use a local variable
    //      to store the last (or first?) exception and then
    //      after the loop just throw that exception...
    try {
      return createInstance(cls, types, args);
    } catch (NoSuchMethodException nsme) {
      log.debug("Unable to create a new instance of '{}'"
              + " with args: {} (types: {}) -- Error: {};"
              + " converting args to strings and trying again...",
              new Object[]{cls, args, types, nsme});
    }

    for (int i = 0; i < n; i++) {
      arg = args[i];
      types[i] = String.class;
      if (arg == null || arg instanceof String) {
        continue;
      }
      arg = arg.toString();
      args[i] = arg;
      types[i] = (arg == null ? String.class : arg.getClass());
      // Should we just trust that 'toString' returns a String ???
    }

    return createInstance(cls, types, args);

  }  // end --  createInstance( Class<?> cls, Object ... args )

  public static Object createInstance(String name, Object... args)
          throws NoSuchMethodException,
          InstantiationException,
          IllegalAccessException,
          InvocationTargetException,
          ClassNotFoundException {
    return createInstance(findClass(null, name), args);
  }

  // === [ Resource Find/Lookup Methods ] =================================
  public static File getResourceFile(String name, Class ref)
          throws IOException {
    ClassLoader clsLoader = null;
    URL url = null;

    log.debug("Searching for resource file '{}' in CLASSPATH", name);

    clsLoader = (ref != null ? ref.getClassLoader()
            : ClassLoader.getSystemClassLoader());
    url = clsLoader.getResource(name);
    if (url == null) {
      throw new FileNotFoundException("Resource file '" + name + "'"
              + " not found in CLASSPATH");
    }

    log.debug("Path to resource file: '{}'", url.getPath());
    return new File(java.net.URLDecoder.decode(url.getPath()));
  }

  public static File getResourceFile(String name, Object ref)
          throws IOException {
    return getResourceFile(name, (Class) (ref != null ? ref.getClass() : null));
  }

  public static File getResourceFile(String name) throws IOException {
    return getResourceFile(name, (Class) null);
  }

  // === [ Caller Lookup Methods ] ========================================
  // NOTE: This code was originally in the CSVDataModel class and then
  //       moved to the LoggingUtil class.  However these utilities
  //       are more general than that so I think they belong in either
  //       ClassUtils or arguably ObjectUtils.
  //
  //       The ability to lookup the calling class is obviously most 
  //       useful in logging, especially for the getLogger method.
  //       There is at least one other place I'm considering using it:
  //       in the 'createInstance' method where the class name is
  //       provided and, if that name is not fully-qualified, then
  //       (as a convenience) we could assume that caller wishes to
  //       specify another class in the same package, ie package local.
  //       See 'createInstance( String name, Object ... args )' above.
  //
  //       Using reflection to determine the calling class is of course
  //       expensive so obviously we should try avoiding using this
  //       capability in place other than initialization/loading,
  //       exporting/saving, or disable-able/optional code such as
  //       debug statements.
  public static Class<?> getCallerClass(Class<?> cls, int offset) {
    // TBD: This method will get the entire call stack and search
    //      "backwards" (ie from most recent stack element) until
    //      it finds the occurence of the specified class, 'cls'.
    //      If offset is +n, then class n elements before that class
    //      is returned.  If offset -n, then the class n elements
    //      after (i.e. further into the stack) is returned.

    // TBD: Create a method that returns the call stack as either
    //      a List or perhaps an Iterator.
    //
    //      Then create a "Grouping" wrapper List or Iterator that
    //      combines elements of the underlying/backing list based
    //      on some criteria, e.g. in this case we want to collapse
    //      all stack _consecutive_ elements with the same class
    //      name into a single entry.
    // TBD: Create an enum for specifying relative offsets for
    //      elements in a sequential data structure, e.g. List,
    //      array, etc.
    //      E.g. FIRST, LAST, NEXT, PREV, PENULTIMATE, etc
    //
    //      These might be combined with filter object or predicates
    //      to make it easy to define simple search criteria.
    //
    //      We might be about to use these with Map, especially
    //      sorted map structures as well.
    throw new UnsupportedOperationException("No yet implemented");
  }

  public static Class<?> getCallerClass(int depth) {
    //   // Add 2 to depth:
    //   //    +1 for this method, ie LoggingUtils.getCallerClass(int)
    //   //    +1 for the sun.reflect.Reflection.getCallerClass(int) method
    //   // Therefore, if method called with depth = 0, the class returned
    //   // is the caller of this method.  If the depth = 1, then the
    //   // class returned is the caller of the caller of this method,
    //   // i.e. what is typically wanted.
    //   // If depth > stack depth, then null is returned.
    //   return sun.reflect.Reflection.getCallerClass( depth + 2 );
    throw new UnsupportedOperationException("No yet implemented");
  }

  public static Class<?> getCallerClass() {
    // Returns the caller of the caller of this method, so we need to
    // add 2:
    //    +1 for this method, ie LoggingUtils.getCallerClass()
    //    +1 for the caller of this method
    return getCallerClass(2);
  }

  // === [ Main Method (for testing only) ] ===============================
  public static void main(String[] args) {
    String[] names = {"isl.model.ref.ConventionalCDModel",
      "isl.model.ref.ExtendedCDModel",};
    Object[] params = null;
    String pkgName = null;
    String clsName = null;
    Package pkg = null;
    Class cls = null;
    Object obj = null;

    for (String name : names) {
      try {
        pkgName = name.substring(0, name.lastIndexOf('.'));
        clsName = name.substring(pkgName.length() + 1);

        pkg = Package.getPackage(pkgName);
        cls = Class.forName(name);

        // HACK
        if (pkg == null && cls != null) {
          pkg = cls.getPackage();
        }

        log.debug("Name: '{}', package name: '{}', class name: '{}',"
                + " package object: '{}', class object: '{}'",
                new Object[]{name, pkgName, clsName, pkg, cls});

        // Without constructor params
        obj = createInstance(Class.forName(name));
        obj = createInstance(name);
        obj = createInstance(findClass(pkgName, name));
        obj = createInstance(findClass(pkgName, clsName));
        obj = pkgName;
        obj = createInstance(findClass(obj, clsName));
        obj = createInstance(findClass(pkg, clsName));
        obj = createInstance(findClass(cls, clsName));
        obj = createInstance(findClass(obj, clsName));

        // With constructor params
        obj = createInstance(Class.forName(name), params);
        obj = createInstance(name, params);
        obj = createInstance(findClass(pkgName, name), params);
        obj = createInstance(findClass(pkgName, clsName), params);
        obj = pkgName;
        obj = createInstance(findClass(obj, clsName), params);
        obj = createInstance(findClass(pkg, clsName), params);
        obj = createInstance(findClass(cls, clsName), params);
        obj = createInstance(findClass(obj, clsName), params);

      } catch (Exception e) {
        log.error("Failed creating instance of '" + name + "' -- Error: ", e);
      }
    }

  }

}  // end of ClassUtils class
