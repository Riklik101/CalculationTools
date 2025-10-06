import java.util.Scanner; // Import the Scanner class to read input
//math is a built in library
// java.lang is imported by default
/* this is the structure for a multiline comment
 * this is the second line of the comment
 */

public class hello {
    public static void main(String[] args) {


        System.out.println("Hello, World!");
        System.out.println("This is going to be a simple recap of what I am learning in compsci 1 rn");

        //---------------start of learning-----------------
        double flo = 2.445;
        int x = 3;
        System.out.println(flo / 0);
        // Dividing a float by 0 gives Infinity, not an error in Java.
        System.out.println("5/2 (int division): " + (5 / 2)); // 2
        System.out.println("Math.round(5/2): " + Math.round(5 / 2)); // 2
        System.out.println("5.0/2 (double division): " + (5.0 / 2)); // 2.5
        // Strings are objects in Java. Use Scanner for input.
        System.out.println("Enter your name: ");
        Scanner input = new Scanner(System.in);
        String name = input.nextLine(); // Read user input
        System.out.println("Hello, " + name); // Output user input
        for(int i=0;i<5;i++){
            System.out.println(i);
        }
        //This differs from python as python uses "in" for loops
        //java is much more strict with syntax
        //for example
        try {
            int[] arr = {1, 2, 3};
            System.out.println(arr[5]); // This will throw an ArrayIndexOutOfBoundsException
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
        try
        {
            int a = 5 / 0; // This will throw an ArithmeticException
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }


    }
}
