import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.*;

public class Puzzle {
   // #region classdef
   int count = 0; // number of solution
   final int CHAR_MAX = 0xFF; // 0xFF is max ascii value. we are using it so that we can map the index to
                              // ascii value, i.e index of 65 == 'A', 101 == 'e' etc.
   int charValues[] = null; // to store each letters digit value. size == CHAR_MAX
   int lowerValues[] = null; // to store lowest possible value for each digit. size == CHAR_MAX
   final Deque<CharNode> queue; // parse the addend and sum strings to store size == CHAR_MAX the char values in
                                // stack.
   boolean usedDigits[] = null; // size 10

   private class CharNode {
      char ch;
      char operand;

      CharNode(char ch, char op) {
         this.ch = ch;
         this.operand = op;
      }

      @Override
      public String toString() {
         StringBuilder sb = new StringBuilder();
         sb.append(operand).append(ch);
         return sb.toString();
      }
   }

   /**
    * Constructor
    */
   public Puzzle() {
      usedDigits = new boolean[10];
      charValues = new int[CHAR_MAX];
      lowerValues = new int[CHAR_MAX];
      queue = new ArrayDeque<>();
   }
   // #endregion


   // #region Methods
   
   /**
    * Solve the word puzzle.
    * 
    * @param args three words (addend1 addend2 sum)
    */
   public static void main(String[] argv) {

      if (argv.length != 3) {
         System.out.println("Usage java puzzle [addend1] [addend2] [sum]");
         System.exit(77);
      }

      Puzzle p = new Puzzle();
      p.solve(argv);
   }

   /**
    * Main method to solve cryptarithmetics
    */
   public void solve(String[] argv) {
      int sum = 0;
      this.populateDS(argv);
      System.out.println("Calculating " + argv[1] + "+" + argv[2] + "=" + argv[2]);
      this.calcuate(this.queue, sum);
      System.out.println("Total Solutions (for " + argv[1] + " + " + argv[2] + " = " + argv[2] +  "):" + this.count);
   }

   /**
    * Initalizes the datastructres of the class, for example, The queue with
    * characters of input string, For the lowerValue array, it sets the the value 1
    * for letters which appear at the beginning of string. (since they cannot have
    * value of 0)
    * 
    * @param argv array of strings
    */
   private void populateDS(String[] argv) {
      String add1 = argv[0];
      String add2 = argv[1];
      String sum = argv[2];
      boolean isRunning = true;
      CharacterIterator ci1 = new StringCharacterIterator(add1);
      CharacterIterator ci2 = new StringCharacterIterator(add2);
      CharacterIterator si = new StringCharacterIterator(sum);
      char ch1, ch2, s;

      ch1 = ci1.last();
      ch2 = ci2.last();
      s = si.last();

      // populate stack.
      while (isRunning) {
         // add chars at the end of the queue.
         if (ch1 != CharacterIterator.DONE) {
            queue.addLast(new CharNode(ch1, '+'));
         }
         if (ch2 != CharacterIterator.DONE) {
            queue.addLast(new CharNode(ch2, '+'));
         }
         if (s != CharacterIterator.DONE) {
            queue.addLast(new CharNode(s, '='));
         } else {
            // all the chars have been extracted. break loop.
            break;
         }

         ch1 = ci1.previous();
         ch2 = ci2.previous();
         s = si.previous();
      }

      ch1 = add1.charAt(0);
      ch2 = add2.charAt(0);
      s = sum.charAt(0);

      // populate lowerValue array.
      // use chars ascii position as index
      this.lowerValues[(int) ch1] = 1;
      this.lowerValues[(int) ch2] = 1;
      this.lowerValues[(int) s] = 1;

      // fills the charValues with -1
      // since default 0 is a valid value for a char.
      Arrays.fill(this.charValues, -1);

      return;
   }

   /**
    * This method calculates value for each letter. It brute forces all possible
    * solution for each letter.
    * 
    * @param q Queue containing characters
    * @param sum total accumulated sum of addend letters,
    */
   private void calcuate(Deque<CharNode> q, int sum) {

      // we have successfully calculated one sum
      if (q.isEmpty() && sum == 0) {
         this.count++; // increment count
         int ch = 0;
         for (int i : charValues) {
            if (i >= 0) {
               System.out.print((char) ch + " = " + i + " ");
            }
            ch++;
         }
         System.out.println(" ");
         return;
      }

      Deque<CharNode> n = new ArrayDeque<>(q); // create copy;
      CharNode cNode = n.getFirst();
      n.removeFirst(); // pop out the value.
      int c = (int) cNode.ch;
      int val = charValues[c];
      int digit;

      switch (cNode.operand) {
         case '+': {
            /** Start Case */
            // this character already has a value assigned, move to the next char.
            if (val >= 0) {
               calcuate(n, sum + val);
            } else {
               /** Start else */
               for (digit = lowerValues[c]; digit < 10; digit++) {
                  /** Start Loop */
                  // if this digit value is not in use.
                  if (usedDigits[digit] == false) {
                     usedDigits[digit] = true;
                     charValues[c] = digit; // char c is assigned the value digit.

                     calcuate(n, sum + digit);

                     usedDigits[digit] = false;
                     charValues[c] = -1; // unassign the from the char, so we can try another permutation.
                  }
                  /** End Loop */
               }
               /** End else */
            }
            break;
            /** End case */
         }

         case '=': {
            /** Start Case */
            digit = sum % 10; // whatever the sum of previous two chars were, we only use the least
                                  // significant digit.                 
            if (val >= 0) {
               /** Start if */
               // one column successfullu calculated, move to the next.
               if (val == digit) {
                  calcuate(n, (sum / 10));
               }
               /** End if */
            } else {
               /** Start Else */
               if (usedDigits[digit] == false && digit >= lowerValues[c]) {
                  usedDigits[digit] = true;
                  charValues[c] = digit;

                  calcuate(n, (sum / 10));

                  usedDigits[digit] = false;
                  charValues[c] = -1;
               }
               /** End Else */
            }
            /** End Case */
            break;
         }

         default: {
            break;
         }
      }
      return;
   }
   // #endregion
}
