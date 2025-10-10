import java.util.Scanner;

public class InterfaceMain {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        JavaCalc calc = new JavaCalc(input);
        String versionNum = "1.0.0";
        System.out.println("Calculator V" + versionNum + "\nEnter calculator you wish to use, using the manual.\nFor help, type '0'. To exit, press enter with no input.");
        
        while (true){
            try {
                String line = input.nextLine();
                if (line.isEmpty()) {
                    System.exit(0);
                }
                int c = Integer.parseInt(line.trim());
                switch (c) {
                    case 0:
                        System.out.println("I have no instructions, read the manual inside of the repo.");
                        break;
                    case 1: 
                        System.out.println(calc.pythag());
                        break;
                    default:
                        break;
                }
                
            } catch (NumberFormatException e) {
                System.out.println("Invalid input — please enter a number or press enter to exit.");
            } 
        }
    }
}
