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

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * TBD: Add class description
 *
 * @future
 *    - 
 *
 * @author Ken Cline
 * @see Collections
 * @see Collection
 * @since 1.0
 * @version $Revision: $  $Date: $
 *
 * $Id: $
 */
public class CollectionUtils
{

  // TBD: Add doc
  private static final Logger log = LoggerFactory.getLogger( CollectionUtils.class );


  /**
   * TBD: Add doc
   *
   */
  public static final Comparator<Object> CASE_INSENSITIVE
    = new Comparator<Object> () {
    @Override
    public int compare ( Object obj1, Object obj2 ) {
      if ( obj1 == obj2 ) return  0;
      if ( obj1 == null ) return -1;
      if ( obj2 == null ) return +1;
      return String.CASE_INSENSITIVE_ORDER.compare( obj1.toString(),
                                                    obj2.toString()  );
    }
  };


  /**
   * Returns the index of the first occurrence of an element from the
   * list that is equal to the specified object as determined by the
   * provided @{link Comparator}, or -1 if no equivalent element is
   * found.  Each element in the list is compared to the <code>obj</code>
   * using the <code>cmp</code> @{link Comparator}.  If the <code>Comparator</code>
   * returns zero (<code>0</code>), indicating that the objects are equivalent,
   * then the index of the current, matching element is returned.
   *
   * @note The caller is responsible for synchronizing, i.e. locking,
   *       the List argument if other threads could possibly modify
   *       this List during the <code>indexOf</code> search.  If the
   *       elements of the List are changed, this method may return
   *       an incorrect, and possibly invalid, index.  Likewise, it is
   *       assumed that behavior of the Comparator and attributes of
   *       target object used by the Comparator to determine equivalence
   *       are also immutable during the search.  For example, if the
   *       object is a <code>StringBuffer</code> that is concurrently
   *       modified (by another thread) while the method is testing
   *       list elements for equivalence, then results would again be
   *       unpredictable.
   *
   * @param list    the list elements to search
   * @param obj     the target value to match list elements against
   * @param cmp     the Comparator which determines if the provided
   *                <code>obj</code> is equivalent the elements in the
   *                <code>list</code> by returning <code>0</code> from
   *                its @{link Comparator#compare compare} method
   * @return index of the first element that <code>cmp</code> indicates
   *         is equivalent to <code>obj</code> or <code>-1</code>
   *         if no such element exists
   * @throws NullPointerException if <code>list</code> or <code>cmp</code>
   *            are <code>null</code>
   * @see Comparator#compare
   */
  // public static <T> int indexOf ( List<T> list, T obj,
  //                                Comparator<? super T> cmp )
  //@SuppressWarnings( "unchecked" )
  public static int indexOf ( List<?> list, Object obj, Comparator<Object> cmp )
  {
    // T element = null;
    Object element = null;

    // TBD: Should we check for null or simply let the NPE be thrown?
    // if ( list == null ) {
    //   return -1;
    // }

    // TBD: If cmp == null, then should we just call list.indexOf(obj)
    //      or simply let the NPE be thrown?

    // Calling list.size(), instead of using a local variable, in the loop
    // conditional allows the list size to change.  However, if the list
    // is changed during this loop (i.e. by another thread) then the index
    // returned may not be valid and/or it might not be the first occurrence
    // of the object.

    for ( int i = 0 ;  i < list.size() ; i++ ) {
      element = list.get( i );
      if ( cmp.compare(obj,element) == 0 ) {
        return i;
      }
    }

    return -1;
  }

  /**
   * Returns the index of the last occurrence of an element from the
   * list that is equal to the specified object as determined by the
   * provided @{link Comparator}, or -1 if no equivalent element is
   * found.  Each element, starting at the end of the list, is compared
   * to the <code>obj</code> using the <code>cmp</code> @{link Comparator}.
   * If the <code>Comparator</code> returns zero (<code>0</code>),
   * indicating that the objects are equivalent, then the index of the
   * current, matching element is returned.
   *
   * {@seenote #indexOf(List<T>,T,Comparator<? super T>)}
   *
   * @param list    the list elements to search
   * @param obj     the target value to match list elements against
   * @param cmp     the Comparator which determines if the provided
   *                <code>obj</code> is equivalent the elements in the
   *                <code>list</code> by returning <code>0</code> from
   *                its @{link Comparator#compare compare} method
   * @return index of the last element that <code>cmp</code> indicates
   *         is equivalent to <code>obj</code> or <code>-1</code>
   *         if no such element exists
   * @throws NullPointerException if <code>list</code> or <code>cmp</code>
   *            are <code>null</code>
   * @see Comparator#compare
   */
  // public static <T> int lastIndexOf ( List<T> list, T obj,
  //                                     Comparator<? super T> cmp )
  //@SuppressWarnings( "unchecked" )
  public static int lastIndexOf ( List<?> list, Object obj, Comparator<Object> cmp )
  {
    // T element = null;
    Object element = null;

    // TBD: Should we check for null or simply let the NPE be thrown?
    // if ( list == null ) {
    //   return -1;
    // }

    // TBD: If cmp == null, then should we just call list.indexOf(obj)
    //      or simply let the NPE be thrown?

    for ( int i = list.size()-1 ;  i >= 0 ; i-- ) {
      element = list.get( i );
      if ( cmp.compare(obj,element) == 0 ) {
        return i;
      }
    }

    return -1;
  }

  // TBD: Should we synchronize, ie lock, on the list object during the
  //      indexOf searches?  I don't see synchronization occurring in the
  //      utility methods in the java.util.Collections class however they
  //      also, in some cases, copy the List into an array and then operate
  //      on the list.
  //      I guess we'll assume that synchronization is the responsibility
  //      of the caller method.


  // TBD: The indexOf/lastIndexOf search methods that take Comparators
  //      are simply special cases of a more general search using a
  //      Filter (or Predicate) object.  That is, loop through a List
  //      and stop when the search criteria is true, e.g. when the Filter
  //      accepts the element (or when the Predicate call with the
  //      element returns true).
  //
  //      If I can find the Filter stuff that I created 10-15 years ago
  //      I could simply reuse it here.  In fact, I think I had already
  //      implemented these indexOf search methods.
  //
  //      There's also the stuff in the Hamcrest package/library that
  //      looks nearly identical to my Filter mini-framework; I think
  //      they call it Match and they support chains and operators as
  //      I did, i.e. Filter f3 = f0.not( f1 ).and( f2 );
  //
  //      There's a Predicate interface in Apache Commons Collections
  //      which looks like does basically the same thing.
  //


  /**
   * Replace all values with 0.0.
   * @param m java.util.Map<String,sim.util.MutableDouble>
   */
  public static void zero_md(Map<String,sim.util.MutableDouble> m) {
    for (java.util.Map.Entry<String,sim.util.MutableDouble> me : m.entrySet())
      me.getValue().val = 0.0;
  }

  /**
   * Replace all values with 0.
   * @param m java.util.Map<String,isl.util.MyInt>
   */
  public static void zero_mi(Map<String,Number> m) {
    for (java.util.Map.Entry<String,Number> me : m.entrySet())
      ((MyInt)me.getValue()).set(0);
  }
  
  /**
   * Sum the values.
   * @param m java.util.Map<String,isl.util.MyInt>
   * @return long
   */
  public static long sum_mi(Map<String,Number> m) {
    long retVal = 0;
    for (Number i : m.values()) retVal += i.longValue();
    return retVal;
  }

  public static Map<String, Number> countTypes(java.util.ArrayList<? extends TypeString> al) {
    if (al != null) {
      LinkedHashMap<String, Number> types = new LinkedHashMap<>();
      for (TypeString s : al) {
        if (types.containsKey(s.getType())) {
          ((MyInt)types.get(s.getType())).add(1);
        } else {
          types.put(s.getType(), new MyInt(1));
        }
      }
      return types;
    } else {
      return null;
    }
  }
  public static int countType(java.util.ArrayList<? extends TypeString> al, String type) {
    int retVal = 0;
    if (al != null) {
      for (TypeString s : al) {
        if (s.getType().equals(type)) retVal++;
      }
    }
    return retVal;
  }
  /** Shuffles (randomizes the order of) the ArrayList
      -- stolen from MASON 17 implementation for sim.util.Bag
   * @param al arraylist to shuffle
   * @param rng pRNG to use for the shuffling
   * @return pRNG shuffled arraylist
   */
  public static java.util.ArrayList<?> shuffle(java.util.ArrayList<?> al, ec.util.MersenneTwisterFast rng) {
      Object[] objs = al.toArray();
      int numObjs = objs.length;
      Object obj;
      int rand;

      for (int x = numObjs - 1; x >= 1; x--) {
          rand = rng.nextInt(x + 1);
          obj = objs[x];
          objs[x] = objs[rand];
          objs[rand] = obj;
      }
      java.util.ArrayList<Object> objs_al = new java.util.ArrayList<>(objs.length);
      for (int x = 0; x < objs.length; x++) {
          objs_al.add(objs[x]);
      }
      return objs_al;
  }

  /**
   * Return a String listing the <key,value> contents of the Map
   * @param map
   * @return 
   */
  public static String describe(Map<String,Number> map) {
    StringBuilder d = new StringBuilder("{");
    int count=0;
    for (Map.Entry<String,Number> me : map.entrySet()) {
      if (count++ > 0) d.append(", ");
      d.append(me.getKey()).append(" = ").append(me.getValue().toString());
    }
    d.append("}");
    return d.toString();
  }
  
  /**
   * Add the <key,value> contents of the giver into the receiver. If there are
   * entries in the receiver that don't exist in the giver, they'll stay the
   * same.  If there are entries in the giver that don't exist in the receiver,
   * they'll be inserted.
   * @param receiver
   * @param giver 
   */
  public static void addIn(Map<String,Number> receiver, Map<String,Number> giver) {
    for (Map.Entry<String,Number> me : giver.entrySet()) {
      if (receiver.containsKey(me.getKey())) {
        Number n = receiver.get(me.getKey());
        if (n instanceof MyInt) ((MyInt)n).add(me.getValue().intValue());
        else receiver.put(me.getKey(),n.intValue()+me.getValue().intValue());
      }
    }
  }
  
  public static void main (String[] args) {
      java.util.ArrayList<Integer> al = new java.util.ArrayList<>();
      for (int i=0 ; i<10 ; i++) al.add(i);
      @SuppressWarnings("unchecked")
      java.util.ArrayList<Integer> new_al = (java.util.ArrayList<Integer>) shuffle(al, new ec.util.MersenneTwisterFast());
      for (int i=0 ; i<al.size() ; i++) System.out.println(new_al.get(i).toString());
  }
}  // end of CollectionUtils class
