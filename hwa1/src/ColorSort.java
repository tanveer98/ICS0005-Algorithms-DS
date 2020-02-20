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
      CountSort.sort(balls);
   }
}

class CountSort {

   public static final int NUM_OF_COLOR = 3;

   public static void sort(ColorSort.Color balls[]) {

      //array to store the frequency of each enums.
      int frequency[] = new int[NUM_OF_COLOR];
      int len = balls.length;
      for( int i = 0; i < n; i++) {
      	++frequency[ball[i].getValue()];
      }

      //Overwrite the balls array according to the frequency
      int idx = 0;
      for (int color = 0; color < NUM_OF_COLOR; ++color) {
         while (frequency[color] > 0) {
            balls[idx] = ColorSort.Color.valueOf(color);
            ++idx;
            --frequency[color];
         }
      }
      
   }

}

