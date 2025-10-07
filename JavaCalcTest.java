import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JavaCalcTest {

    @Test
    void testAddition() {
        JavaCalc calc = new JavaCalc();
        assertEquals(15.5, calc.parseLine("12.5 + 3", false), 1e-10);
        assertEquals(-1.0, calc.parseLine("-2 + 1", false), 1e-10);
    }

    @Test
    void testSubtraction() {
        JavaCalc calc = new JavaCalc();
        assertEquals(9.5, calc.parseLine("12.5 - 3", false), 1e-10);
        assertEquals(-3.0, calc.parseLine("-2 - 1", false), 1e-10);
    }

    @Test
    void testMultiplication() {
        JavaCalc calc = new JavaCalc();
        assertEquals(37.5, calc.parseLine("12.5 * 3", false), 1e-10);
        assertEquals(37.5, calc.parseLine("12.5 x 3", false), 1e-10);
        assertEquals(37.5, calc.parseLine("12.5 X 3", false), 1e-10);
        assertEquals(-2.0, calc.parseLine("-2 * 1", false), 1e-10);
        assertEquals(-0.92, calc.parseLine("-2.3e-1*4", false), 1e-10);
    }

    @Test
    void testDivision() {
        JavaCalc calc = new JavaCalc();
        assertEquals(4.166666666666667, calc.parseLine("12.5 / 3", false), 1e-10);
        assertEquals(-2.0, calc.parseLine("-2 / 1", false), 1e-10);
        assertEquals(Double.POSITIVE_INFINITY, calc.parseLine("1 / 0", false));
        assertTrue(Double.isNaN(calc.parseLine("0 / 0", false)));
    }

    @Test
    void testExponentSignHandling() {
        JavaCalc calc = new JavaCalc();
        assertEquals(1.0, calc.parseLine("1e+0+0", false), 1e-10);
        assertEquals(0.0, calc.parseLine("1e-1-0.1", false), 1e-10);
    }

    @Test
    void testWhitespaceHandling() {
        JavaCalc calc = new JavaCalc();
        assertEquals(5.0, calc.parseLine(" 2 + 3 ", false), 1e-10);
        assertEquals(6.0, calc.parseLine(" 2*3 ", false), 1e-10);
    }

    @Test
    void testInvalidInput() {
        JavaCalc calc = new JavaCalc();
        assertThrows(IllegalArgumentException.class, () -> calc.parseLine("12.5 3", false));
        assertThrows(IllegalArgumentException.class, () -> calc.parseLine("abc + 3", false));
        assertThrows(IllegalArgumentException.class, () -> calc.parseLine("12.5 + xyz", false));
        assertThrows(IllegalArgumentException.class, () -> calc.parseLine(null, false));
        assertThrows(IllegalArgumentException.class, () -> calc.parseLine("12.5", false));
    }
}