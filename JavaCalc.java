public class JavaCalc {
    // Parses an expression like "12.5 + 3" or "-2.3e-1*4". The method
    // automatically finds the operator (+ - * / or x/X) inside the input string,
    // taking care not to confuse a leading sign or exponent sign with the operator.
    // It then parses the left and right operands and returns the computed result.
    public double parseLine(String input, boolean verbose) {
        if (input == null) throw new IllegalArgumentException("input is null");

        // find operator position: first occurrence of + - * / x X that is
        // not a leading sign of the first number and not part of an exponent (e/E).
        int pos = -1;
        char op = 0;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == '+' || c == '-' || c == '*' || c == '/' || c == 'x' || c == 'X') {
                // If this is a plus/minus, it might be a sign for the first number
                // (i == 0) or a sign in an exponent (previous char 'e' or 'E').
                if ((c == '+' || c == '-') ) {
                    if (i == 0) {
                        continue; // leading sign of first number
                    }
                    char prev = input.charAt(i - 1);
                    if (prev == 'e' || prev == 'E') {
                        continue; // sign of exponent
                    }
                }
                // Otherwise treat as the operator
                pos = i;
                op = c;
                break;
            }
        }

        if (pos < 0) {
            throw new IllegalArgumentException("operator not found in input");
        }

        String leftStr = input.substring(0, pos).trim();
        String rightStr = input.substring(pos + 1).trim();

        double left, right;
        try {
            left = Double.parseDouble(leftStr);
            right = Double.parseDouble(rightStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("unable to parse operands: " + leftStr + ", " + rightStr, e);
        }

        if (verbose) {
            System.out.println("Left: " + left + ", Right: " + right + ", Op: " + op);
        }

        double result;
        switch (op) {
            case '+':
                result = left + right;
                break;
            case '-':
                result = left - right;
                break;
            case '*':
            case 'x': // treat 'x' as multiply
            case 'X':
                result = left * right;
                break;
            case '/':
                // optional: throw on integer-style division by zero; here using double semantics
                if (right == 0.0) {
                    // choose behavior: return Infinity/NaN (double semantics) or throw
                    // throw new ArithmeticException("division by zero");
                    result = left / right; // yields Infinity or NaN per IEEE-754
                } else {
                    result = left / right;
                }
                break;
            default:
                throw new IllegalArgumentException("unsupported operator: " + op);
        }

        return result;
    }

    
}
