import java.util.Scanner;

public class Interface {
    public static void main(String[] args) {
        JavaCalc calc = new JavaCalc();
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the expression to evaluate:");
        String s = input.nextLine();
        System.out.println("Result: " + calc.parseLine(s, ));
        input.close();

    }
}
