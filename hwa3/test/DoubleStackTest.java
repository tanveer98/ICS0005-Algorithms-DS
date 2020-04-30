
import static org.junit.Assert.*;
import org.junit.Test;

/** Testklass.
 * @author jaanus
 */
public class DoubleStackTest {

   public static double delta = 0.000000001;

   @Test (timeout=1000)
   public void testNewStack() { 
      DoubleStack m = new DoubleStack();
      assertTrue ("new stack must be empty;", m.stEmpty());
      m.push (1.);
      m.pop();
      assertTrue ("stack must be empty after one push and one pop; ", m.stEmpty());
   }

   @Test (timeout=1000)
   public void testLIFO() {
      DoubleStack m = new DoubleStack();
      m.push (6.);
      m.push (-3.);
      double i1 = m.pop();
      double i2 = m.pop();
      assertTrue ("After two pushes and two pops stack must be empty;",
         m.stEmpty());
      assertTrue ("LIFO order must hold: 6. -3. returns -3. first;",
         (i1 == -3.) && (i2 == 6.));
   }

   @Test (timeout=1000)
   public void testOp() {
      double tt = 0.;
      DoubleStack m = new DoubleStack();
      m.push (5.);
      m.push (3.);
      m.op ("+");
      tt = m.pop();
      assertTrue ("5. + 3. must be 8.; ", tt==8.);
      assertTrue ("push push op pop must not grow the stack; ", m.stEmpty());
      m.push (5.);
      m.push (3.);
      m.op ("-");
      tt = m.pop();
      assertTrue ("5. - 3. must be 2.; ", tt==2.);
      assertTrue ("push push op pop must not grow the stack; ", m.stEmpty());
      m.push (5.);
      m.push (3.);
      m.op ("*");
      tt = m.pop();
      assertTrue ("5. * 3. must be 15.; ", tt==15.);
      assertTrue ("push push op pop must not grow the stack; ", m.stEmpty());
      m.push (51.);
      m.push (3.);
      m.op ("/");
      tt = m.pop();
      assertTrue ("51. / 3. must be 17.; ", tt==17.);
      assertTrue ("push push op pop must not grow the stack; ", m.stEmpty());
   } 

   @Test (timeout=1000)
   public void testTos() {
      DoubleStack m = new DoubleStack();
      m.push (2.);
      m.push (5.);
      double k = m.tos();
      double k2 = m.pop();
      assertEquals ("5. must be on top ", 5., k, delta);
      assertEquals ("tos must not change the top;", 5., k2, delta);
      double k3 = m.pop();
      assertEquals ("tos must not change the stack;", 2., k3, delta);
      assertTrue ("tos must not pop;", m.stEmpty());
   }

   @Test (timeout=1000)
   public void testEquals() {
      DoubleStack m1 = new DoubleStack();
      DoubleStack m2 = new DoubleStack();
      assertTrue ("two empty stacks must be equal;", m1.equals(m2));
      m1.push (1.);
      m2.push (1.);
      assertTrue ("1. in both stacks - stacks must be equal; ", m1.equals(m2));
      m1.push (0.);
      assertFalse ("1. 0. and just 1. must not be equal;", m1.equals(m2));
      m2.push (3.);
      assertFalse ("1. 0. and 1. 3. must not be equal;", m1.equals(m2));
      m1.pop();
      m2.pop();
      assertTrue ("1. in stacks with different history, stacks must be equal;", 
         m1.equals(m2));
      m1.pop();
      assertFalse ("first empty, second contains 1., must not be equal;", 
         m1.equals(m2));
   }

   @Test (expected=RuntimeException.class)
   public void testPopEmpty() {
      DoubleStack m = new DoubleStack();
      m.pop();
   }

   @Test (expected=RuntimeException.class)
   public void testOpUnderflow() {
      DoubleStack m = new DoubleStack();
      m.push (4.);
      m.op ("+");
   }

   @Test (timeout=1000)
   public void testClone() {
      DoubleStack m1 = new DoubleStack();
      m1.push (5.);
      m1.push (4.);
      DoubleStack m2 = null;
      try {
         m2 = (DoubleStack)m1.clone();
      } catch (CloneNotSupportedException e) {};
      assertNotSame ("clone must differ from original;", m2, m1);
      assertEquals ("clone must be equal to original;", m2, m1);
      m1.pop();
      m1.push (6.);
      assertFalse ("clone must be independent;", m1.equals(m2));
   }

   @Test (timeout=1000)
   public void testToString() {
      DoubleStack m = new DoubleStack();
      assertNotNull ("empty stack must be ok;", m.toString());
      m.push (-8.5);
      m.push (7.14);
      String s1 = m.toString().substring (0, 8);
      m.push (2.73);
      String s2 = m.toString().substring (0, 8);
      assertEquals (
 "top must be the last element; toString from bottom must start with -8.5 7.14 ", 
         s1, s2);
   } 

   @Test (expected=RuntimeException.class)
   public void testTosUnderflow() {
      DoubleStack m = new DoubleStack();
      m.tos();
   }

   @Test (timeout=1000)
   public void testInterpret() {
      String s = "1.";
      assertEquals ("expression: " + s, 1., DoubleStack.interpret (s), delta);
      s = "2. 5. -";
      assertEquals ("expression: " + s, -3., DoubleStack.interpret (s), delta);
      s = "35. 10. -3. + /";
      assertEquals ("expression: " + s, 5., DoubleStack.interpret (s), delta);
   }

   @Test (expected=RuntimeException.class)
   public void testInterpretStackbalance() {
      String s = "35. 10. -3. + / 2.";
      DoubleStack.interpret (s);
   }

   @Test (expected=RuntimeException.class)
   public void testInterpretIllegalArg1() {
      String s = "35. 10. -3. + x 2.";
      DoubleStack.interpret (s);
   }

   @Test (expected=RuntimeException.class)
   public void testInterpretIllegalArg2() {
      String s = "35. y 10. -3. + - +";
      DoubleStack.interpret (s);
   }

   @Test (expected=RuntimeException.class)
   public void testInterpretUnderflow() {
      String s = "35. 10. + -";
      DoubleStack.interpret (s);
   }

   @Test (expected=RuntimeException.class)
   public void testInterpretNull() {
      String s = null;
      DoubleStack.interpret (s);
   }

   @Test (expected=RuntimeException.class)
   public void testInterpretEmpty() {
      String s = "";
      DoubleStack.interpret (s);
   }

   @Test (expected=RuntimeException.class)
   public void testInterpretOpfirst() {
      String s = "- 3. 2.";
      DoubleStack.interpret (s);
   }

   @Test (timeout=1000)
   public void testInterpretLong() {
      String s = "1. -10. 4. 8. 3. - + * +";
      assertEquals ("expression: " + Aout.toString (s), -89.,
         DoubleStack.interpret (s), delta);
      s = "156. 154. 152. - 3. + -";
      assertEquals ("expression: " + Aout.toString (s), 151.,
         DoubleStack.interpret (s), delta);
   }

   @Test (timeout=1000)
   public void testInterpretTokenizer() {
      String s = "1.  2.    +";
      assertEquals ("expression: " + Aout.toString (s), 3., 
         DoubleStack.interpret (s), delta);
      s = "   \t \t356.  \t \t";
      assertEquals ("expression: " + Aout.toString (s), 356.,
         DoubleStack.interpret (s), delta);
      s = "\t2. \t5. +   \t";
      assertEquals ("expression: " + Aout.toString (s), 7., 
         DoubleStack.interpret (s), delta);
   }
}

