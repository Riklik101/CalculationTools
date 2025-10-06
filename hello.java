public class hello {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
        System.out.println("This is going to be a simple recap of what I am learning in compsci 1 rn");

        double flo = 2.445;
        int x = 3;
        try {
            System.out.println(flo / 0);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        // Interestingly, this does not throw an error
        // This is because in java, dividing a floating point number by 0 results in Infinity, not an error

        // Now, some things that you remember from python that do NOT apply to java
        // You must declare one variable as a double if you want it to divide as a double with another integer or other type
        // Importantly, java does NOT round, it truncates
        // So, if you want to round, you must use Math.round()
        System.out.println("For example, 5/2 = " + (5 / 2)); // integer division, result is 2
        System.out.println("But if you want to round, you must do Math.round(5/2) = " + Math.round(5 / 2)); // still 2, since 5/2 is int
        // However, if you want to get a double result, you must declare one of the numbers as a double
        System.out.println("So, 5.0/2 = " + (5.0 / 2)); // double division, result is 2.5
        // Or, you can cast one of the numbers as a double

        // Anyways, next up is strings. In java, strings are objects, not primitive types
        // Input is not simple, however, you can use the Scanner class to get input from the user
        // For example, you can do the following:

    }
}
