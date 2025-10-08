public class JavaCalc {
    //returns two strings in an array from the divider
    String[] dividers = {"*", "/", "+", "-"}
    public String[] parseLine(String input) {
        String[] parsed = {};
        if (input == null) throw new IllegalArgumentException("input is null");
        try {
            for(int x = 0; x == -1;)
            {
                x = input.indexOf(dividers[x]);
            }
            parsed = new String[] {input.substring(0,x),input.substring(x+1)};
        } catch (Exception e) {
            System.out.println("Error: Exception due to " + e);
        }
        return parsed;
    }


    
}
