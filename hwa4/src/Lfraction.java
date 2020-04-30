import java.util.*;

/**
 * This class represents fractions of form n/d where n and d are long integer
 * numbers. Basic operations and arithmetics for fractions are provided.
 */
public class Lfraction implements Comparable<Lfraction> {

   /** Main method. Different tests. */
   public static void main(String[] param) {
      Lfraction pi = Lfraction.valueOf(" 5/3 Sneed");
      System.out.println(pi.toString());

   }

   private final long numerator;
   private final long denominator;

   private static long gcd(long num, long denom) {
      if (denom == 0L)
         return num;
      return gcd(denom, num % denom);
   }

   private static long lcm(long a, long b) {
      long gcd = Lfraction.gcd(a, b);
      long lcm = (a * b) / gcd;
      return lcm;
   }

   /**
    * Constructor.
    * 
    * @param a numerator
    * @param b denominator > 0
    */
   public Lfraction(long num, long denom) {
      //if negative denom, make denom positive and num negative.
      if(denom < 0) {
         num *= -1;
         denom *= -1;
      }
      if (denom <= 0L) {
         throw new RuntimeException("Invalid value for denominator " + denom);
      } else {
         long gcd = (Lfraction.gcd(num, denom));
         gcd = ((gcd < 0) ? (-gcd) : (gcd));
         numerator = (long) (num / gcd);
         denominator = (long) (denom / gcd);
      }
   }

   /**
    * Public method to access the numerator field.
    * 
    * @return numerator
    */
   public long getNumerator() {
      return numerator; // or num * gcd?
   }

   /**
    * Public method to access the denominator field.
    * 
    * @return denominator
    */
   public long getDenominator() {
      return denominator; // or denom * gcd?
   }

   /**
    * Conversion to string.
    * 
    * @return string representation of the fraction
    */
   @Override
   public String toString() {
      String s = String.valueOf(getNumerator()) + "/" + String.valueOf(getDenominator());
      return s;
   }

   /**
    * Equality test.
    * 
    * @param m second fraction
    * @return true if fractions this and m are equal
    */
   @Override
   public boolean equals(Object m) {
      Lfraction rhs = (Lfraction) m;
      if (compareTo(rhs) == 0)
         return true;
      return false;
   }

   /**
    * Hashcode has to be equal for equal fractions.
    * 
    * @return hashcode
    */
   @Override
   public int hashCode() {
     int hash = (int)(numerator * 101 + denominator * 97);
     return hash;
   }

   /**
    * Sum of fractions.
    * 
    * @param m second addend
    * @return this+m
    */
   public Lfraction plus(Lfraction m) {
      if(getDenominator() == m.getDenominator()) {
         long n = getNumerator() + m.getNumerator();
         long d = getDenominator();
         assert (d > 0L);
         return new Lfraction(n, d);
      } else {
         //when both have "unlike" denominators.
         long lcd = lcm(getDenominator(), m.getDenominator());
         assert (lcd > 0L);
         
         long lhsNum = getNumerator() * (lcd / getDenominator());
         long rhsNum = m.getNumerator() * (lcd / m.getDenominator());
         Lfraction res = new Lfraction(lhsNum + rhsNum, lcd);
         return res;
      }
   }

   /**
    * Multiplication of fractions.
    * 
    * @param m second factor
    * @return this*m
    */
   public Lfraction times(Lfraction rhs) {
      long num = getNumerator() * rhs.getNumerator();
      long denom = getDenominator() * rhs.getDenominator();
      Lfraction res = new Lfraction(num, denom);
      return res;
   }

   /**
    * Inverse of the fraction. n/d becomes d/n.
    * 
    * @return inverse of this fraction: 1/this
    */
   public Lfraction inverse() {
      
      if(getNumerator() == 0) {
         throw new RuntimeException("Cannot inverse fraction with 0 as numerator >" + this.toString());
      } else {
         long num = getDenominator();
         long denom = getNumerator();
         return new Lfraction(num, denom);
      }
   }

   /**
    * Opposite of the fraction. n/d becomes -n/d.
    * 
    * @return opposite of this fraction: -this
    */
   public Lfraction opposite() {
      return new Lfraction((-1 * getNumerator()), getDenominator());
   }

   /**
    * Difference of fractions.
    * 
    * @param m subtrahend
    * @return this-m
    */
   public Lfraction minus(Lfraction m) {
      Lfraction rhs = m.opposite();
      return plus(rhs);
   }

   /**
    * Quotient of fractions.
    * 
    * @param m divisor
    * @return this/m
    */
   public Lfraction divideBy(Lfraction m) {
      if(m.getNumerator() == 0) {
         throw new RuntimeException("cannot divide by zero! Error value is >" + m.toString());
      }
      return times(m.inverse());
   }

   /**
    * Comparision of fractions.
    * 
    * @param m second fraction
    * @return -1 if this < m; 0 if this==m; 1 if this > m
    */
   @Override
   public int compareTo(Lfraction m) {
      Lfraction res = minus(m);
      if(res.getNumerator() > 0) {
         return 1;
      } else if(res.getNumerator() < 0) {
         return -1;
      } else {
         return 0;
      }
   }

   /**
    * Clone of the fraction.
    * 
    * @return new fraction equal to this
    */
   @Override
   public Object clone() throws CloneNotSupportedException {
      return new Lfraction(getNumerator(), getDenominator());
   }

   /**
    * Integer part of the (improper) fraction.
    * 
    * @return integer part of this fraction
    */
   public long integerPart() {
      long integerPart = (long) Math.floor(getNumerator() / getDenominator());
      return  integerPart;
   }

   /**
    * Extract fraction part of the (improper) fraction (a proper fraction without
    * the integer part).
    * 
    * @return fraction part of this fraction
    */
   public Lfraction fractionPart() {
      long integerPart = integerPart();
      long newNum = getNumerator() - ( integerPart * getDenominator() );
      return new Lfraction(newNum, getDenominator());
   }

   /**
    * Approximate value of the fraction.
    * 
    * @return numeric value of this fraction
    */
   public double toDouble() {
      return ((double) getNumerator()) / getDenominator(); // a/b without casting either as double performs integer division.
   }

   /**
    * Double value f presented as a fraction with denominator d > 0.
    * 
    * @param f real number
    * @param d positive denominator for the result
    * @return f as an approximate fraction of form n/d
    */
   public static Lfraction toLfraction(double f, long d) {
      double num = f * d; //round it up!
      if( (num - Math.floor(f)) > 0.5d ) {
         //round up
         num = Math.ceil(num);
      } else {
         num = Math.floor(num);
      }
      return new Lfraction((long) num, d);
   }

   /**
    * Conversion from string to the fraction. Accepts strings of form that is
    * defined by the toString method.
    * 
    * @param s string form (as produced by toString) of the fraction
    * @return fraction represented by s
    */
   public static Lfraction valueOf(String s) {
      StringTokenizer st = new StringTokenizer(s," \n\r\t/");
      Long num = null,denom = null;
      try {
         num = Long.valueOf(st.nextToken());
         denom = Long.valueOf(st.nextToken());
      } catch (Exception e) {
         System.out.println("Error in String >" + s + " Please provide valid string to parse!");
         e.printStackTrace();
      }

      if( denom == null || denom.equals(0L)) {
         throw new RuntimeException("Invalid string! >" + s);
      }
      return new Lfraction(num, denom);
   }
}