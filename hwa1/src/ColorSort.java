
import java.util.*;
public class ColorSort {
   // similar to {red = 0, green = 1, blue = 2} in C/C++ enums.
   enum Color {
      red(0), green(1), blue(2);

      private final int value;
      
      private Color(int val) {
         this.value = val;
      }

      public static Color valueOf(int val) {
         if(val == 0) {
            return Color.red;
         } else if (val == 1) {
            return Color.green;
         } else {
            return Color.blue;
         }
      }

      public int getValue() {
         return value;
      }
   };

   public static void main(String[] param) {
      // for debugging
   }

   public static void reorder(Color[] balls) {
      Quick3way.sort(balls);
      // Arrays.sort(balls);
   }
}

class Quick3way {

   public static final int RANGE = 3;

   // Function to efficiently sort an array with many duplicated values
   // using Counting Sort algorithm
   public static void sort(ColorSort.Color[] arr) {
      // create a new array to store counts of elements in the input array
      int[] freq = new int[RANGE];

      // using value of elements in the input array as index,
      // update their frequencies in the new array
      for (ColorSort.Color c : arr) {
         int i = c.getValue();
         freq[i]++;
      }

      // overwrite the input array with sorted order
      int k = 0;
      for (int i = 0; i < RANGE; i++) {
         while (freq[i]-- > 0) {
            arr[k++] = ColorSort.Color.valueOf(i);
         }
      }
   }

}