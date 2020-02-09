import static org.junit.Assert.*;
import org.junit.Test;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Arrays;
public class ColorSortTest {

   /** Number of milliseconds allowed to spend for sorting 1 million elements */
   public static int threshold = 25;

   /** Test data */
   static int balls[] = null;
   static final int ColorSort_Color_red = 0;
   static final int ColorSort_Color_green = 1;
   static final int ColorSort_Color_blue = 2;
   static int rCount = 0;
   static int gCount = 0;
   static int bCount = 0;
   
   /** Correctness check for the result */
   static boolean check (int balls[], int r, int g, int b) {
      if (balls == null)
         return false;
      int balls_length = balls.length;
      if (r<0 || g<0 || b<0 || balls_length != r+g+b)
         return false; 
      if (balls_length == 0)
         return true;
      for (int i=0; i < r; i++)
         if (balls[i] != ColorSort_Color_red)
            return false;
      for (int i=r; i < r+g; i++)
         if (balls[i] != ColorSort_Color_green)
            return false;
      for (int i=r+g; i < balls_length; i++)
         if (balls[i] != ColorSort_Color_blue)
            return false;
      return true;
   } // check

    @Test (timeout=1000)
    public void testFunctionality() {
      balls = new int[100000];
      rCount = 0;
      gCount = 0;
      bCount = 0;
      for (int i=0; i < balls.length; i++) {
         double rnd = Math.random();
         if (rnd < 1./3.) {
            balls[i] = ColorSort_Color_red;
            rCount++;
         } else  if (rnd > 2./3.) {
            balls[i] =  ColorSort_Color_blue;
            bCount++;
         } else {
            balls[i] = ColorSort_Color_green;
            gCount++;
         }  
      }
      ColorSort.reorder (balls);
      assertTrue ("Result incorrect", check (balls, rCount, gCount, bCount));
    }

    @Test (timeout=1000)
    public void testShort() {
      balls = new int[1];
      rCount = 0;
      gCount = 0;
      bCount = 0;
      double rnd = Math.random();
      if (rnd < 1./3.) {
         balls[0] = ColorSort_Color_red;
         rCount++;
      } else  if (rnd > 2./3.) {
         balls[0] =  ColorSort_Color_blue;
         bCount++;
      } else {
         balls[0] = ColorSort_Color_green;
         gCount++;
      }
      ColorSort.reorder (balls);
      assertTrue ("One element array not working", 
         check (balls, rCount, gCount, bCount));
      balls = new int[0];
      rCount = 0;
      gCount = 0;
      bCount = 0;
      ColorSort.reorder (balls);
      assertTrue ("Zero element array not working", 
         check (balls, rCount, gCount, bCount));
      balls = new int[100000];
      rCount = 0;
      gCount = 0;
      bCount = 0;
      for (int i=0; i < balls.length; i++) {
         rnd = Math.random();
         if (rnd < 1./3.) {
            balls[i] = ColorSort_Color_red;
            rCount++;
         } else  if (rnd > 2./3.) {
            balls[i] =  ColorSort_Color_blue;
            bCount++;
         } else {
            balls[i] = ColorSort_Color_green;
            gCount++;
         }
      }
      ColorSort.reorder (balls);
      assertTrue ("Result incorrect", check (balls, rCount, gCount, bCount));
    }

    @Test (timeout=1000)
    public void testAllRed() {
      balls = new int[100000];
      int len = balls.length;
      rCount = len;
      gCount = 0;
      bCount = 0;
      for (int i=0; i < len; i++) {
            balls[i] = ColorSort_Color_red;
      }
      ColorSort.reorder (balls);
      assertTrue ("Result incorrect for all red", 
         check (balls, rCount, gCount, bCount));
      balls = new int[100000];
      rCount = 0;
      gCount = 0;
      bCount = 0;
      for (int i=0; i < balls.length; i++) {
         double rnd = Math.random();
         if (rnd < 1./3.) {
            balls[i] = ColorSort_Color_red;
            rCount++;
         } else  if (rnd > 2./3.) {
            balls[i] =  ColorSort_Color_blue;
            bCount++;
         } else {
            balls[i] = ColorSort_Color_green;
            gCount++;
         }
      }
      ColorSort.reorder (balls);
      assertTrue ("Result incorrect", check (balls, rCount, gCount, bCount));
    }

    @Test (timeout=1000)
    public void testAllGreen() {
      balls = new int[100000];
      int len = balls.length;
      rCount = 0;
      gCount = len;
      bCount = 0;
      for (int i=0; i < len; i++) {
            balls[i] = ColorSort_Color_green;
      }
      ColorSort.reorder (balls);
      assertTrue ("Result incorrect for all green", 
         check (balls, rCount, gCount, bCount));
      balls = new int[100000];
      rCount = 0;
      gCount = 0;
      bCount = 0;
      for (int i=0; i < balls.length; i++) {
         double rnd = Math.random();
         if (rnd < 1./3.) {
            balls[i] = ColorSort_Color_red;
            rCount++;
         } else  if (rnd > 2./3.) {
            balls[i] =  ColorSort_Color_blue;
            bCount++;
         } else {
            balls[i] = ColorSort_Color_green;
            gCount++;
         }
      }
      ColorSort.reorder (balls);
      assertTrue ("Result incorrect", check (balls, rCount, gCount, bCount));
    }

    @Test (timeout=1000)
    public void testAllBlue() {
      balls = new int[100000];
      int len = balls.length;
      rCount = 0;
      gCount = 0;
      bCount = len;
      for (int i=0; i < len; i++) {
            balls[i] =  ColorSort_Color_blue;
      }
      ColorSort.reorder (balls);
      assertTrue ("Result incorrect for all blue",
         check (balls, rCount, gCount, bCount));
      balls = new int[100000];
      rCount = 0;
      gCount = 0;
      bCount = 0;
      for (int i=0; i < balls.length; i++) {
         double rnd = Math.random();
         if (rnd < 1./3.) {
            balls[i] = ColorSort_Color_red;
            rCount++;
         } else  if (rnd > 2./3.) {
            balls[i] =  ColorSort_Color_blue;
            bCount++;
         } else {
            balls[i] = ColorSort_Color_green;
            gCount++;
         }
      }
      ColorSort.reorder (balls);
      assertTrue ("Result incorrect", check (balls, rCount, gCount, bCount));
    }

    @Test (timeout=1000)
    public void testSpeed() {
      balls = new int[1000000];
      rCount = 0;
      gCount = 0;
      bCount = 0;
      for (int i=0; i < balls.length; i++) {
         double rnd = Math.random();
         if (rnd < 1./3.) {
            balls[i] = ColorSort_Color_red;
            rCount++;
         } else  if (rnd > 2./3.) {
            balls[i] =  ColorSort_Color_blue;
            bCount++;
         } else {
            balls[i] = ColorSort_Color_green;
            gCount++;
         }
      }
      System.out.println("R G B: " + rCount + " " + gCount + " " +bCount);
      
      writeArrayToFile(balls);

      long t0 = System.currentTimeMillis();
      ColorSort.reorder (balls);
      long t1 = System.currentTimeMillis();
      int delta = (int)(t1-t0);
      System.out.println ("Time spent: " + delta + " ms");
      assertTrue ("Result incorrect", check (balls, rCount, gCount, bCount));
      assertTrue ("Too slow: "+ delta, delta < threshold);
      System.out.println ("Time spent: " + delta + " ms");
      writeArrayToFile(balls);
    }

    static void writeArrayToFile(int[] arr) {
      try {
         FileWriter file = new FileWriter("sort.txt", true);
         BufferedWriter outputWriter = new BufferedWriter(file);
         String s = Arrays.toString(arr);
         outputWriter.write(s);
         outputWriter.write("\n\n\n\n\n\n\n\n SORTED: ");
         outputWriter.close();

      } catch (Exception e) {
         e.printStackTrace();
      }
    }

/*
   @Test (expected=RuntimeException.class)
   public void testNullArray() {
      ColorSort.reorder (null);
   }
*/
}

