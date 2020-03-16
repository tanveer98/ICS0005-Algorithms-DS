import java.util.*;
import java.util.regex.*;

public class DoubleStack implements Iterable<Double> {

   private Node top;
   private Node first;
   private int size;

   // Nodes in our custom made linkedList/stack
   private class Node {
      double val;
      private Node next = null;
      private Node prev = null;

      // no args ctor
      Node() {
      }

      // Node from double
      Node(double a) {
         this.val = a;
      }

      public Node getNext() {
         return this.next;
      }

      public void setNext(Node next) {
         this.next = next;
      }

      public Node getPrev() {
         return this.prev;
      }

      public void setPrev(Node prev) {
         this.prev = prev;
      }
   }

   DoubleStack() {
      // TODO!!! Your constructor here!
      this.top = null; // creates an empty linkedList.
      this.first = null;
   }

   public int length() {
      return this.size;
   }

   @Override
   /** Iterable **/
   public Iterator<Double> iterator() {
      return new DoubleStackIterator(this);
   }

   @Override
   public Object clone() throws CloneNotSupportedException {
      DoubleStack clone = new DoubleStack();
      Node bottom = this.first;

      while (bottom != null) {
         clone.push(bottom.val);
         bottom = bottom.getPrev();
      }

      return clone; // TODO!!! Your code here!
   }

   public boolean stEmpty() {
      return top == null;
   }

   public void push(double a) {
      // TODO!!! Your code here!
      Node newTop = new Node(a);
      if (this.stEmpty()) {
         first = newTop;
         top = newTop;
         size++;
         return;
      }
      newTop.setNext(top);
      top.setPrev(newTop);
      top = newTop;
      size++;
   }

   public double pop() throws RuntimeException {
      // TODO!!! Your code here!
      if (this.stEmpty()) {
         System.out.println("Stack empty!, nothing more to pop!");
         throw new RuntimeException("Stack empty!, nothing more to peek");
      }
      double ret = top.val;
      top = top.next;
      if (top != null) {
         top.prev = null;
      }
      size--;
      return ret;
   }

   public void op(String s) throws RuntimeException {
      // TODO!!!
      StringTokenizer st = new StringTokenizer(s);
      String operand = null;

      if (st.countTokens() > 1) {
         throw new RuntimeException("op " + s + "has more operands than necessary");
      }

      if (this.length() < 2) {
         throw new RuntimeException("Stack size of too small for operation! " + s);
      }

      operand = st.nextToken();
      switch (operand) {
         case "+": {
            double d1 = this.pop(); 
            double d2 = this.pop();
            this.push((d2 + d1));
            break;
         }
         case "-": {
            double d1 = this.pop();
            double d2 = this.pop();
            this.push((d2 - d1));
            break;
         }
         case "*": {
            double d1 = this.pop();
            double d2 = this.pop();
            this.push((d2 * d1));
            break;
         }
         case "/": {    
            double d1 = this.pop();
            double d2 = this.pop();
            this.push((d2 / d1));
            break;
         }
         case "SWAP": {
            double d1 = this.pop();
            double d2 = this.pop();
            this.push(d1);
            this.push(d2);
            break;
         }
         case "ROT": {
         if (this.length() < 3) {
            throw new RuntimeException("Stack size of too small for operation! " + s);
         }
            double c = this.pop();
            double b = this.pop();
            double a = this.pop();

            this.push(b);
            this.push(c);
            this.push(a);
            break;
         }
         default: {
            throw new RuntimeException("Invalid operand " + operand + "in  op string " + s);
         }

      }
   }

   // akin to peek()
   public double tos() {
      if (stEmpty()) {
         System.out.println("Stack empty!, nothing more to peek!");
         throw new RuntimeException("Stack empty!, nothing more to peek");
      }
      return top.val; // TODO!!! Your code here!
   }

   @Override
   public boolean equals(Object o) {
      DoubleStack other = (DoubleStack) o;
      if (this.length() != other.length()) {
         return false;
      }

      Iterator<Double> otherIterator = other.iterator();
      Iterator<Double> thisIterator = this.iterator();
      while (thisIterator.hasNext()) {
         double d1 = thisIterator.next();
         double d2 = otherIterator.next();
         if (d1 != d2) {
            return false;
         }
      }

      return true; // TODO!!! Your code here!
   }

   @Override
   public String toString() {
      StringBuffer ret = new StringBuffer();
      Node bottom = first;
      while (bottom != null) {
         Double d = bottom.val;
         ret.append(d.toString() + " ");
         bottom = bottom.getPrev();
      }
      return ret.toString(); // TODO!!! Your code here!
   }

   private static Optional<String> hasAlpha(String s) {
      String regex = "([a-zA-Z]+)";
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(s);
      while (matcher.find()) {
         String ret = matcher.group();
         Pattern pattern2 = Pattern.compile("(SWAP|ROT)");
         Matcher matcher2 = pattern2.matcher(ret);
         if(!matcher2.find()) {
            return Optional.of(ret);
         }
      } 
      return Optional.empty();
   }

   private static double parseDouble(String s) throws IllegalArgumentException {
      String pat = "[-+]?([0-9]*\\.[0-9]+|[0-9]+)";
      Pattern pattern = Pattern.compile(pat);
      Matcher matcher = pattern.matcher(s);

      if (matcher.find()) {
         return Double.parseDouble(matcher.group(0));
      } else {
         throw new IllegalArgumentException("Could not find double val in string " + s);
      }
   }

   private static boolean isOperator(String token) {
      // String patt = "^([\\*\\+\\-/])+";
      String patt = "^([\\*|\\+|\\-|\\/|SWAP|ROT])$";
      return Pattern.compile(patt).matcher(token).find();
   }

   public static double interpret(String pol) {
      String sanitized = pol.trim();
      Optional<String> str = hasAlpha(sanitized);
      if (str.isPresent()) {
         throw new RuntimeException( "interpret string " + pol + "contains invalid alphabet character '" + str.get() + "'");
      }

      String stck = sanitized;
      StringTokenizer tokenizer = new StringTokenizer(sanitized);
      DoubleStack doubleStack = null;

      doubleStack = new DoubleStack();
      while (tokenizer.hasMoreTokens()) {
         double a = 0d;
         stck = tokenizer.nextToken();
         if (isOperator(stck)) {
            doubleStack.op(stck);
         } else {
            a = parseDouble(stck);
            doubleStack.push(a);
         }
      }
      double ret = doubleStack.pop();
      if (!doubleStack.stEmpty()) {
         throw new RuntimeException("Invalid interpret string! : " + pol);
      }
      return ret; // TODO!!! Your code here!
   }

   /*** Iterator class ***/
   class DoubleStackIterator implements Iterator<Double> {
      private Node node;

      // constructor
      DoubleStackIterator(DoubleStack stack) {
         // initialize cursor
         this.node = stack.top;
      }

      // Checks if the next element exists
      public boolean hasNext() {
         return node != null;
      }

      // moves the cursor/iterator to next element
      public Double next() {
         Double val = node.val;
         node = node.getNext();
         return val;
      }

      // Used to remove an element. Implement only if needed
      public void remove() {
         // not needed.
         throw new UnsupportedOperationException();
      }

   }

   public static void main(String[] argum) {
      System.out.println("Hello world!");
   
      assert (DoubleStack.interpret( "2 5 SWAP -") == 3.0);
      assert (DoubleStack.interpret("2 5 9 ROT - +") == 12.0);
      assert (DoubleStack.interpret("2 5 9 ROT + SWAP -") == 6.0);
      assert (DoubleStack.interpret("35. 10. -3. + /") == 5.0);
   }

}