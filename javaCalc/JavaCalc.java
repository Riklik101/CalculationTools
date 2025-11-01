package javaCalc;
public class JavaCalc {
    private final java.util.Scanner input;
    public JavaCalc(java.util.Scanner input) {
        this.input = input;
    }
    //------UNUSED CODE BELOW------
    //returns two strings in an array from the divider
    //String[] dividers = {"*", "/", "+", "-"};
    /*public double parseLine(String input) {
        if (input == null) throw new IllegalArgumentException("input is null");
        try {
            int y = 0;
            for(int x = 0; x == -1; x++)
            {
                y = input.indexOf(dividers[x]);
            }
            parsed = new String[] {input.substring(0,y),input.substring(y+1)};
        } catch (Exception e) {
            System.out.println("Error: Exception due to " + e);
        }
        return paresed;
    }*/

    public double pythag()
    {
        System.out.print("Enter side A --> ");
        double a = Double.parseDouble(input.nextLine());
        System.out.print("Enter side B --> ");
        double b = Double.parseDouble(input.nextLine());
        return Math.sqrt((a*a)+(b*b));
    }
    public double bhtriangle()
    {
        System.out.print("Enter base --> ");
        double base = Double.parseDouble(input.nextLine());
        System.out.print("Enter height --> ");
        double height = Double.parseDouble(input.nextLine());
        return 0.5*base*height;
    }
    
}
