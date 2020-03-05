import java.util.*;

public class ColorSort {
   private static final int NUM_OF_COLOR = 3;
   private static int[] frequency = null;
   enum Color {
      red(0), green(1), blue(2);

      private final int value;

      private Color(int val) {
         this.value = val;
      }

      public static Color valueOf(int val) {
         switch(val) {
            case 0:
              return Color.red;
            case 1:
            return Color.green;
            default:
               return Color.blue;
          }
      }

      public int getValue() {
         return value;
      }
   };

   public static void main(String[] param) {
   }

   public static void reorder(Color[] balls) {
      frequency = new int[NUM_OF_COLOR];
      int len = balls.length;
      for (int i = 0; i < len; i++) {
         ++frequency[balls[i].getValue()];
      }
      int totalFilled = 0;
      for (int i = 0; i < NUM_OF_COLOR; ++i) {
         Arrays.fill(balls, totalFilled, (totalFilled += frequency[i]), ColorSort.Color.valueOf(i));
      }
   }
} 