import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.stream.IntStream;
import java.util.ArrayDeque;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.StringTokenizer;
import java.lang.Thread;
import java.time.Duration;
import java.util.Optional;
import java.nio.charset.StandardCharsets.*;

public class Answer {
	
	public static void main (String[] param) throws InterruptedException {

      // TODO!!! Solutions to small problems 
      //   that do not need an independent method!

      // conversion double -> String
		String str = Double.toString(54);
		System.out.println(str);
      // conversion String -> int
		int integer = Integer.parseInt("123"); // will throw NumberFormatException incase of invalid input containing alphabet
		System.out.println(integer);
      // "hh:mm:ss" 
		Instant instant = Clock.systemUTC().instant();
		DateTimeFormatter formatter =  DateTimeFormatter.ofPattern("HH:mm:ss") //HH instead of hh for 24 hour
		.withZone( ZoneId.systemDefault() );
		String timestamp = formatter.format(instant);
		System.out.println(timestamp);
      // cos 45 deg
		double cos45 = Math.cos(Math.toRadians(45d));
		System.out.println(cos45);
      // table of square roots
		IntStream.range(0, 99)
		.forEachOrdered(n -> {
			//System.out.println("Input: " + n + " SquareRoot: " + Math.sqrt(n));
		});

	  // reverse case
		String firstString = "JÜriöÖülestõus1Ää";
		String result = reverseCase (firstString);
		System.out.println ("\"" + firstString + "\" -> \"" + result + "\"");

      // reverse string
		System.out.println(reverseString("Abcd123"));
      // word count
		String s = "How  many	 words   here";
		int nw = countWords (s);
		System.out.println (s + "\t" + String.valueOf (nw));
		s = "Sneed's Seed and Feed 	Formerly     Chuck's";
		nw = countWords(s);
		System.out.println (s + "\t" + String.valueOf (nw));
		s = "\t\ttest \n test\t      testyyyy";
		nw = countWords(s);
		System.out.println (s + "\t" + String.valueOf (nw));

      // pause. COMMENT IT OUT BEFORE JUNIT-TESTING!
		// Instant start = Instant.now();
		// Thread.sleep(1000);
		// Instant end = Instant.now();
		// System.out.println("Time elapsed: " + Duration.between(start, end).toMillis());

	//
		final int LSIZE = 100;
		ArrayList<Integer> randList = new ArrayList<Integer> (LSIZE);
		Random generaator = new Random();
		for (int i=0; i<LSIZE; i++) {
			randList.add (Integer.valueOf (generaator.nextInt(1000)));
		}

      // minimal element

		Integer min = randList.stream().min(Integer::compareTo).get();
		System.out.println("The minimum value from the list is: " + min);
      // HashMap tasks:
      //    create
		Map<String,String> map = new HashMap<>();
		map.put("ICS0024", "Automated Testing");
		map.put("ICS005", "Algorithms and DataStructure");
		map.put("ICD0006", "JavaScript");
		map.put("ICS0019", "Advanced Python");
		map.put("ICS0026", "Cryptohraphy");
      //    print all keys
		Set<String> keySet = map.keySet();
		Iterator<String> it = keySet.iterator();
		while(it.hasNext()) {
			System.out.println("Key: " + it.next());
		}
      //    remove a key
		map.remove("ICD0006");
      //    print all pairs
		map.forEach((k,v) -> System.out.println(k + " - " + v));

		System.out.println ("Before reverse:  " + randList);
		reverseList (randList);
		System.out.println ("After reverse: " + randList);

		System.out.println ("Maximum: " + maximum (randList));
	}


/***************** MAX *****************/

   /** Finding the maximal element.
    * @param a Collection of Comparable elements
    * @return maximal element.
    * @throws NoSuchElementException if <code> a </code> is empty.
    */
   static public <T extends Object & Comparable<? super T>>
   T maximum (Collection<? extends T> a) 
   throws NoSuchElementException {
  	T max = a.stream().max((x,y) -> x.compareTo(y)).get();
      return max;
}


/***************** Word Count *****************/
   /** Counting the number of words. Any number of any kind of
    * whitespace symbols between words is allowed.
    * @param text text
    * @return number of words in the text
    */
   // public static int countWords (String text) {
   // 	StringTokenizer words = new StringTokenizer(text);
   //    return words.countTokens(); // TODO!!! Your code here
   // }


   public static int countWords (String text) {
   	char charArr[] = text.toCharArray();
   	boolean isWord = false;
   	int wc = 0;
   	int idx =0;
   	boolean isSpace = false;
   	for(char c : charArr) {
   		isSpace = Character.isWhitespace(c);
   		if(idx == 0 && !isSpace){ 
   			wc++;
   		}
   		else{
   			if(idx == 0) { 
   				idx++; 
   				continue;
   			}
   			if(Character.isWhitespace(charArr[idx -1]) && !isSpace)  {
   				wc++;
   			}
   		}
   		idx++;
   	}
   	return wc;
   }

   /***************** String Reverse *****************/

   private static Stream<Integer> reverse(IntStream intstream) {
	//uses ArrayDeque as a stack instead of Stack class
	//Stack inherits from the Vector class
	//So when its being converted to stream, it goes through with it sequentially (FIFO)
	//instead of the intended LIFO (its possible to use a linkedList instead of ArrayDeque)
   	Deque<Integer> stack = new ArrayDeque<>();
   	intstream.forEach(i -> {stack.push(i);});
   	return stack.stream();
   } 

/** String-reverse 
    * @param s String 
    * @return String (reversed: abcd123 -> 321dcba)
    */
public static String reverseString(String s) {
	StringBuilder sb = reverse(s.chars()).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append);
	return sb.toString();
}


/***************** Case Reverse *****************/
   /** Case-reverse on each character (overloaded)
    * @param c int (char expressed as int)
    * @return int (lower -> upper, upper -> lower;otherwise c)
    */
   private static int reverseCase(int c) {
   	return Character.isUpperCase(c) ? Character.toLowerCase(c) : Character.toUpperCase(c);
   }

   /** Case-reverse. Upper -> lower AND lower -> upper.
    * @param s string
    * @return processed string
    */
   public static String reverseCase (String s) {
   	//Collect:
   	//Supplier = SB.new, accumulator SB.appendCodePoint (takes int and combines it into SB)
   	//Accumulator: SB.append(SB), takes 2 SB and combines it into one (is it inefficient? a new SB in each iteration?)
   	StringBuilder sb = s.chars()
   	.map(Answer::reverseCase)
   	.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append);

      return sb.toString(); // TODO!!! Your code here
}
/***************** List reverse *****************/

   /** List reverse. Do not create a new list.
    * @param list list to reverse
    */
   public static <T extends Object> void reverseList (List<T> list)
   throws UnsupportedOperationException {
   		
      // TODO!!! Your code here
	Collections.reverse(list);
   		
   }
}
