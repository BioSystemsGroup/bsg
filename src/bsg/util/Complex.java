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

//import java.lang.Math;
import java.util.Formatter;
import java.util.Formattable;
import static java.util.FormattableFlags.*;

/**
 * TBD: Add class description
 *
 * @future
 *    - 
 *
 * @author Ken Cline
 * @see org.apache.commons.math3.complex.complex.Complex
 * @since 1.0
 * @version $Revision: $  $Date: $
 *
 * $Id: $
 */
public class Complex extends     org.apache.commons.math3.complex.Complex
                     implements  Comparable<Complex>, Formattable

{
  // TBD: Generate serialization ID using serialver tool
  // TBD: Add doc
  private static final long serialVersionUID = 1L;    


  // TBD: Add doc
  private static final Logger log
                   = LoggerFactory.getLogger( Complex.class );
  // Future: private static final Logger log = LoggingUtils.getLogger();


  /**
   * TBD: Add doc
   *
   */
  public static final Complex NaN   = new Complex( Double.NaN, Double.NaN );
  public static final Complex INF   = new Complex( Double.POSITIVE_INFINITY,
                                                 Double.POSITIVE_INFINITY );
  public static final Complex INFINITY = INF;


  /**
   * The square root of -1, i.e. a complex number representing
   * "0.0 + 1.0i"
   */
  public static final Complex i     = new Complex( 0.0, 1.0 );

  // TBD: Does 'i' belong in Complex or in ComplexMath?
  //      PI should be in ComplexMath because regular PI is in Math.
  //      But perhaps 'i' should be defined here?

  /**
   * A complex number representing "0.0 + 0.0i"
   * A complex number representing "1.0 + 0.0i"
   * A complex number representing "2.0 + 0.0i"
   * A complex number representing "3.0 + 0.0i"
   * A complex number representing "4.0 + 0.0i"
   * A complex number representing "5.0 + 0.0i"
   */
  public static final Complex ZERO   = new Complex( 0.0, 0.0 );
  public static final Complex ONE    = new Complex( 1.0, 0.0 );
  public static final Complex TWO    = new Complex( 2.0, 0.0 );
  public static final Complex THREE  = new Complex( 3.0, 0.0 );
  public static final Complex FOUR   = new Complex( 4.0, 0.0 );
  public static final Complex FIVE   = new Complex( 5.0, 0.0 );


  // === [ Constructors ] =================================================

  /**
   * TBD: Add doc
   *
   */
  public Complex ( double real, double imag ) {
    super( real, imag );
    //log.debug( "Constructor called with args: {}", new Object[]{real,imag} );
  }

  /**
   * TBD: Add doc
   *
   */
  public Complex ( double real ) {
    this( real, 0.0 );
  }

  /**
   * TBD: Add doc
   *
   */
  public Complex ( Number real, Number imag ) {
    this( real.doubleValue(), imag.doubleValue() );
    //log.debug( "Constructor called with args: {}", new Object[]{real,imag} );
  }

  /**
   * TBD: Add doc
   *
   */
  public Complex ( Number real ) {
    this( real, 0.0 );
  }

  /**
   * TBD: Add doc
   *
   */
  public Complex ( org.apache.commons.math3.complex.Complex c ) {
    this( c.getReal(), c.getImaginary() );
  }

  /**
   * TBD: Add doc
   *
   */
  public Complex ( String s ) {
    this( Complex.fromString(s) );
  }

  // TBD: Add constructor: Complex( String s )

  /**
   * TBD: Add doc
   *
   */
  // protected org.apache.commons.math3.complex.complex.Complex createComplex ( double real,
  //                                                                   double imag )
  // {
  @Override
  protected Complex createComplex ( double real, double imag ) {
    //log.debug( "Creating complex from ({},{})", real, imag );
    return new Complex( real, imag );
  }


  // === [ Getter & Setter Methods ] ======================================

  /**
   * Access the imaginary part.
   *
   * @return the imaginary part
   */
  public double getImag () {
    return getImaginary();
  }

  /**
   * Access the real part.
   *
   * @return the real part
   */
  public double real () {
    return getReal();
  }

  /**
   * Access the imaginary part.
   *
   * @return the imaginary part
   */
  public double imag () {
    return getImaginary();
  }


  // === [ Arithmetic Operations ] ========================================

  // TBD: The add method should check if this object and/or the
  //      'real' argument is NaN or Inf.
  //      NOTE: The 'add(Complex)' method in the parent class does
  //            NOT check for NaN or Inf.

  /**
   * TBD: Add doc
   *
   */
  public Complex add ( Complex z ) {
    // log.debug( "Calculating ({}) + ({})", this, z );
    return new Complex( super.add(z) );
  }

  /**
   * TBD: Add doc
   *
   */
  public Complex add ( double real, double imag ) {
    return add( createComplex(real,imag) );
  }

  /**
   * TBD: Add doc
   *
   */
  public Complex add ( double real ) {
    // log.debug( "Calculating ({}) + ({})", this, real );
    if ( isNaN() || Double.isNaN(real) ) {
      return NaN;
    }
    return createComplex( getReal() + real, getImag() );
  }

  /**
   * TBD: Add doc
   *
   */
  public Complex add ( Number real ) {
    return add( real.doubleValue() );
  }

  /**
   * TBD: Add doc
   *
   */
  public Complex add ( Number real, Number imag ) {
    return add( real.doubleValue(), imag.doubleValue() );
  }


  // TBD: The subtract method should check if this object and/or the
  //      'real' argument is NaN or Inf.
  //      NOTE: The 'subtract(Complex)' method in the parent class does
  //            check for NaN but does NOT check for Inf.

  /**
   * TBD: Add doc
   *
   */
  public Complex subtract ( Complex z ) {
    // log.debug( "Calculating ({}) - ({})", this, z );
    return new Complex( super.subtract(z) );
  }

  /**
   * TBD: Add doc
   *
   */
  public Complex subtract ( double real, double imag ) {
    return subtract( createComplex(real,imag) );
  }

  /**
   * TBD: Add doc
   *
   */
  public Complex subtract ( double real ) {
    // log.debug( "Calculating ({}) - ({})", this, real );
    if ( isNaN() || Double.isNaN(real) ) {
      return NaN;
    }
    return createComplex( getReal() - real, getImag() );
  }

  /**
   * TBD: Add doc
   *
   */
  public Complex subtract ( Number real ) {
    return subtract( real.doubleValue() );
  }

  /**
   * TBD: Add doc
   *
   */
  public Complex subtract ( Number real, Number imag ) {
    return subtract( real.doubleValue(), imag.doubleValue() );
  }


  /**
   * TBD: Add doc
   *
   */
  public Complex multiply ( Complex z ) {
    // log.debug( "Calculating ({}) * ({})", this, z );
    return new Complex( super.multiply(z) );
  }

  /**
   * TBD: Add doc
   *
   */
  public Complex multiply ( double real, double imag ) {
    return multiply( createComplex(real,imag) );
  }

  /**
   * TBD: Add doc
   *
   */
  @Override
  public Complex multiply ( double real ) {
    // log.debug( "Calculating ({}) * ({})", this, real );
    return new Complex( super.multiply(real) );
  }

  /**
   * TBD: Add doc
   *
   */
  public Complex multiply ( Number real ) {
    return multiply( real.doubleValue() );
  }

  /**
   * TBD: Add doc
   *
   */
  public Complex multiply ( Number real, Number imag ) {
    return multiply( real.doubleValue(), imag.doubleValue() );
  }

  // TBD: I have some questions about how the Apache Commons implemented
  //      the 'divide' and 'multiply' ... I need to take a closer look
  //      at their code
  //      NOTE: the parent class's 'divide(Complex rhs)' method is not checking
  //            whether the imaginary part of rhs is 0, if it is, then you
  //            really dividing my a real number and the calculation is much
  //            simpler.

  // TBD: The Apache Commons Math Complex class implements the following
  //      logic in its divide method:
  //         any Complex / Complex.ZERO = NaN
  //      This does NOT match the behavior of real numbers (ie Doubles)
  //      where 'any value / 0.0 = Infinity'
  //         1.0 / 0.0 = Infinity
  //         Infinity / 0.0 = Infinity
  //         0.0 / Infinity = 0.0
  //         Infinity / 1.0 = Infinity
  //         1.0 / Infinity = 0.0
  //         Infinity / Infinity = NaN
  //

  // TBD: Should we throw an ArithmeticException if dividing by zero?

  /**
   * TBD: Add doc
   *
   */
  public Complex divide ( Complex z ) {
    // log.debug( "Calculating ({}) / ({})", this, z );
    return new Complex( super.divide(z) );
  }

  /**
   * TBD: Add doc
   *
   */
  public Complex divide ( double real, double imag ) {
    return divide( createComplex(real,imag) );
  }

  /**
   * TBD: Add doc
   *
   */
  public Complex divide ( double real ) {
    // log.debug( "Calculating ({}) / ({})", this, real );

    if ( isNaN() || Double.isNaN(real) ) {
      return NaN;
    }

    if ( real == 0.0 ) {
      return NaN;
      // return NaN for consistency with 'divide(Complex)' but this
      // should probably be Infinity instead
    }

    if ( Double.isInfinite(real) && ! isInfinite() ) {
      return ZERO;
    }

    // TBD: Shouldn't we be checking the inverse of above, i.e. if this
    //      Complex number is INF and 'real' is not, then the result
    //      is INF.
    //      If both are INF then what should we return? NaN?

    return createComplex( getReal() / real, getImag() / real );
  }

  /**
   * TBD: Add doc
   *
   */
  public Complex divide ( Number real ) {
    return divide( real.doubleValue() );
  }

  /**
   * TBD: Add doc
   *
   */
  public Complex divide ( Number real, Number imag ) {
    return divide( real.doubleValue(), imag.doubleValue() );
  }

  // TBD: Add support for 'modulo' calculation?
  // TBD: Add support for 'compareTo' and static 'compare' methods


  // public Complex + ( double real ) {
  // public Complex \u002B ( double real ) {
  // public Complex ＋ ( double real ) {
  // public Complex \uFF0B ( double real ) {
  //   return add( real );
  // }

  // public Complex × ( double real ) {
  // public Complex \u00D7 ( double real ) {
  //   return multiply( real );
  // }

  // public Complex ÷ ( double real ) {
  // public Complex \u00F7 ( double real ) {
  //   return divide( real );
  // }


  // === [ Comparison Methods ] ===========================================

  // TBD: Re-implement the 'equals' method from the Apache Commons Math
  //      Complex class.
  //      The parent class's 'equals' does not check if the value is
  //      infinite.  As I understand it, there is only one infinity for
  //      complex numbers, but as implemented, there can be many instances
  //      of this Complex class that are infinite because the real part
  //      could be positive or negative infinity, the imaginary part
  //      could be positive or negative infinity, or both could be
  //      positive or negative infinity.


  /**
   * TBD: Add doc
   *
   * Logic:
   *    <li>All NaN's are equal</li>
   *    <li>NaN is greater than non-Nan</li>
   *    <li>All Infinite values are equal</li>
   *    <li>Infinite values are greater than all values except NaN.</li>
   *
   */
  public static int compare ( Complex z1, Complex z2 ) {
    int result = 0;

    //log.debug( "Comparing complex number z1 ({}) to z2 ({})...", z1, z2 );

    if ( z1 == z2 ) return 0;

    // TBD: Should we handle null or allow an NPE to be thrown?
    //if ( z1 == null ) return -1; // z1 is null and null < non-null
    //if ( z2 == null ) return +1; // z2 is null and null < non-null

    if ( z1.isNaN() ) return ( z2.isNaN() ? 0 : +1 );
    if ( z2.isNaN() ) return -1;  // we know that z1 is non-NaN from above

    if ( z1.isInfinite() ) return ( z2.isInfinite() ? 0 : +1 );
    if ( z2.isInfinite() ) return -1;  // we know that z1 is non-Inf from above

    // Status: neither value is NaN or Infinite

    result = Double.compare( z1.abs(), z2.abs() );
    // log.debug( "Complex number comparison: z1 ({}) {} z2 ({})",
    //           new Object[]{z1,MathUtils.getCompareSymbol(result),z2} );

    return result;

  }

  /**
   * TBD: Add doc
   *
   */
  public int compareTo ( Complex z ) {
    return compare( this, z );
  }

  // === [ Complex Number Properties ] ====================================

  // TBD: Possible flaws or enhancements to Apache Commons Math Complex
  //      number class:
  //         * several method say they don't call Complex.isInfinite
  //           to avoid checking isNaN again, but the isInfinite does
  //           _not_ check isNaN
  //         * can an instance of Complex be both infinite and NaN?
  //           that should not be the case
  //         * there should be a function to calculate the Complex number's
  //           magnitude, ie distance from zero
  //         * the magnitude and argument (angle) should be cached in
  //           instance variables to accelerate calculations

  // === [ Parse & Format Methods ] =======================================

  // TBD: Implement the 'toString' and 'valueOf'/'fromString' methods

  protected static org.apache.commons.math3.complex.ComplexFormat fmt
                      = new org.apache.commons.math3.complex.ComplexFormat();

  /**
   * TBD: Add doc
   *
   */
  // HACK HACK
  @Override
  public String toString () {
    //return String.format( "%2.1f%+2.1fi", getReal(), getImag() );
    return fmt.format( this );
  }

  /**
   * TBD: Add doc
   *
   */
  // HACK HACK
  public static String toString ( double real, double imag ) {
    return new Complex( real, imag ).toString();
  }

  /**
   * TBD: Add doc
   *
   */
  // HACK HACK
  public static Complex fromString ( String s ) {
    // TBD: Try using Scanner to see if it parses faster
    try {
      return new Complex( fmt.parse(s) );
    } catch ( org.apache.commons.math3.exception.MathParseException pe ) {
      //throw new NumberFormatException( pe.getMessage(), pe );
      // NFE __should__ suppose a 'cause' argument to one of
      // its constructors (like most exceptions) but it doesn't
      throw new NumberFormatException( pe.getMessage() );
    }
  }

  /**
   * TBD: Add doc
   *
   */
  // HACK HACK
  public static Complex valueOf ( String s ) {
    return fromString( s );
  }


  /**
   * TBD: Add doc
   *
   */
  public void formatTo ( Formatter formatter, int flags,
                         int width, int precision )
  {
    String fmt   = "";
    String flagsStr  = "";
    String widthStr  = "";
    String precStr   = "";
    String convStr   = "f";
    double real  = getReal();
    double imag  = getImag();
    StringBuilder buf = null;
    int n = 0;

    // %[argument_index$][flags][width][.precision]conversion

    // NOTE: If percision is 0 and the real or imag component is large
    //       then the 'g' conversion throws an ArrayIndexOutOfBounds
    //       exception but the 'f' conversion does not.  Odd.

    if ( (flags & ALTERNATE) == ALTERNATE ) {
      // Format complex as a 2D coordinate pair, ie "(a,b)"
      //flagsStr += '#';
      widthStr = ( width > 4 ? "" + ((width - 3) / 2)
                             : (width >= 0 ? "1" : "") );
      precStr  = ( precision >= 0 ? "." + precision : "" );
      // See NOTE above
      convStr  = ( precision >  0 && width > 0  ? "g" : "f" );
      fmt = "(" + "%" + flagsStr + widthStr + precStr + convStr
                + ","
                + "%" + flagsStr + widthStr + precStr + convStr
          + ")";
    } else {
      // Format complex in the standard a + bi format
      widthStr = ( width > 4 ? "" + ((width - 3) / 2)
                             : (width >= 0 ? "1" : "") );
      precStr  = ( precision >= 0 ? "." + precision : "" );
      // See NOTE above
      convStr  = ( precision >  0 && width > 0 ? "g" : "f" );
      fmt = "%" + flagsStr + widthStr + precStr + convStr
          +     ( imag < 0 ? " - " : " + " )
          + "%" + flagsStr + widthStr + precStr + convStr
          + ( (flags & UPPERCASE) == UPPERCASE ? "I" : "i" );
      // Take absolute value of imaginary component since the
      // sign is already inserted into the format string above
      imag = Math.abs( imag );
    }

    try {
      buf = new StringBuilder();
      buf.append( String.format(fmt,real,imag) );

      // log.debug( "Formatted complex number (real: {}, imag: {})"
      //          + " with format string '{}': '{}'",
      //            new Object[]{getReal(),getImag(),fmt,buf} );

      // If width == -1 (ie no width specified) or if n > width (ie buffer
      // already greater than specified width) then the for-loop is skipped
      n = buf.length(); 
      for ( int i = 0 ; i < width - n ; i++ ) {
        if ( (flags & LEFT_JUSTIFY) == LEFT_JUSTIFY ) {
          buf.append( ' ' );
        } else {
          buf.insert( 0, ' ' );
        }
      }

      formatter.format( buf.toString() );
      // or would it be faster to:  formatter.out().append( buf );

    } catch ( RuntimeException re ) {
      log.error( "Unable to format complex number (real: {}, imag: {})"
               + " with format string '{}' -- Error: {}",
                 new Object[]{getReal(),getImag(),fmt,re.getMessage()} );
      throw re;
    }

  }

  //  Parameters:
  //      formatter - The formatter. Implementing classes may call formatter.out()
  //          or formatter.locale() to obtain the Appendable or Locale used by
  //          this formatter respectively.
  //      flags - The flags modify the output format. The value is interpreted
  //          as a bitmask. Any combination of the following flags may be set:
  //          FormattableFlags.LEFT_JUSTIFY, FormattableFlags.UPPERCASE, and
  //          FormattableFlags.ALTERNATE. If no flags are set, the default
  //          formatting of the implementing class will apply.
  //      width - The minimum number of characters to be written to the output.
  //          If the length of the converted value is less than the width then
  //          the output will be padded by '  ' until the total number of
  //          characters equals width. The padding is at the beginning by default.
  //          If the FormattableFlags.LEFT_JUSTIFY flag is set then the padding
  //          will be at the end. If width is -1 then there is no minimum.
  //      precision - The maximum number of characters to be written to the
  //          output. The precision is applied before the width, thus the output
  //          will be truncated to precision characters even if the width is
  //          greater than the precision. If precision is -1 then there is no
  //          explicit limit on the number of characters. 
  //  Throws:
  //      IllegalFormatException - If any of the parameters are invalid. For
  //          specification of all possible formatting errors, see the Details
  //          section of the formatter class specification.

  // === [ Main & Testing Methods ] =======================================

  /**
   * TBD: Add doc
   *
   */
  public static void testDoubleInf () {
    double x = 0.0;
    double y = 0.0;

    x = 0.0;
    y = 1.0;
    System.out.println( "" + x + " / " + y + " = " + (x/y) );

    x = 1.0;
    y = 0.0;
    System.out.println( "" + x + " / " + y + " = " + (x/y) );

    x = Double.POSITIVE_INFINITY;
    y = 0.0;
    System.out.println( "" + x + " / " + y + " = " + (x/y) );

    x = 0.0;
    y = Double.POSITIVE_INFINITY;
    System.out.println( "" + x + " / " + y + " = " + (x/y) );

    x = Double.POSITIVE_INFINITY;
    y = 1.0;
    System.out.println( "" + x + " / " + y + " = " + (x/y) );

    x = 1.0;
    y = Double.POSITIVE_INFINITY;
    System.out.println( "" + x + " / " + y + " = " + (x/y) );

    x = Double.POSITIVE_INFINITY;
    y = Double.POSITIVE_INFINITY;
    System.out.println( "" + x + " / " + y + " = " + (x/y) );

    Double d = Double.NaN;
    System.out.println( "d = " + d.doubleValue() );

  }

  /**
   * TBD: Add doc
   *
   */
  public static void main ( String[] args ) {
    Complex x = ONE;  // new Complex( 1.0 );
    Complex y = i;    // new Complex( 0.0, 1.0 );
    double  d = 1.0;
    String  s = null;

    System.out.println( "x + y = " + x.add(y)      );
    System.out.println( "x - y = " + x.subtract(y) );
    System.out.println( "x * y = " + x.multiply(y) );
    System.out.println( "x / y = " + x.divide(y)   );
    System.out.println( "-------------------------" );

    System.out.println( "y + x = " + y.add(x)      );
    System.out.println( "y - x = " + y.subtract(x) );
    System.out.println( "y * x = " + y.multiply(x) );
    System.out.println( "y / x = " + y.divide(x)   );
    System.out.println( "-------------------------" );

    d = 1.0;
    System.out.println( "y + " + d + " = " + y.add(d)      );
    System.out.println( "y - " + d + " = " + y.subtract(d) );
    System.out.println( "y * " + d + " = " + y.multiply(d) );
    System.out.println( "y / " + d + " = " + y.divide(d)   );
    System.out.println( "-------------------------" );

    s = "4+3i";
    x = Complex.fromString( s );
    System.out.println( "Complex.fromString( '" + s + "' )"
                      + " = " + x.getClass().getSimpleName() + '@' + x.hashCode()
                      + " = " + x );
    System.out.println( "-------------------------" );

    String [] values = { "4 + 3i", "11236661214274482 + 0i" };
    String [] fmts   = { "", "15", "15.3", ".0", "1.0" };
    Complex   z    = null;
    String    fmt  = null;
    for ( String value : values ) {
      z = new Complex( value );
      for ( String f : fmts ) {
        fmt = "%" + f + "s";
        System.out.println( "Format: '" + fmt + "'  "
                            + String.format( "z = '"  + fmt + "'", z ) );
        fmt = "%" + f + "S";
        System.out.println( "Format: '" + fmt + "'  "
                            + String.format( "z = '" + fmt + "'", z ) );
        fmt = "%#" + f + "s";
        System.out.println( "Format: '" + fmt + "' "
                            + String.format( "z = '" + fmt + "'", z ) );
      }
      System.out.println( "-------------------------" );
    }

  }

}  // end of Complex class



// TO DO 6/17
// -----
//    * Possibly re-implement all of the Apache Commons Math Complex class
//      here in our class because (a) they did not subclass Number and
//      (b) the real and imaginary values are private and hence not accessible
//    * Extend the java.lang.Number class
//    * Create a BigComplex class that stores the real and imaginary components
//      in instances of BigDecimal
//    * Create a ComplexMath utility class that provides standard functions
//      between Complex and non-Complex numbers as well as Complex and Complex
//      numbers.
//    * Should we have a special wrapper object for converting non-Complex
//      numbers, e.g. Double, Integer, etc, into Complex numbers?  If this
//      existed then calculations could check if the number was wrapped
//      and handle accordingly, e.g.
//         add ( Complex c1, Complex c2 ) {
//           if ( c1.isReal() && c2.isReal() ) {
//             return new Complex( c1.getReal() + c2.getReal() );
//           }
//           if ( c1.isReal() ) {
//             return new Complex( c1.getReal() + c2.getReal(), c2.getImag() );
//           }
//           if ( c2.isReal() ) {
//             return new Complex( c1.getReal() + c2.getReal(), c1.getImag() );
//           }
//           return new Complex( c1.getReal() + c2.getReal(),
//                               c1.getImag() + c2.getImag() );
//         }
//    * Create a MutableComplexNumber class that does not keep creating new
//      instances of Complex for every arithmetic operation.
//    * Implement a version of Complex that uses generics, i.e. Complex<T>
//      where T is the numeric type for both the real and imaginary components
//    * 
// 

// TODO 6/19
//    * Implement the Formattable interface so that Complex objects can be
//      formatted by Formatter and the precision can be controlled.
//    *
//    *
//


