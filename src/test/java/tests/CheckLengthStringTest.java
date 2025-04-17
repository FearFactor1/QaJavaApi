package tests;

import lib.BaseTestCase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CheckLengthStringTest extends BaseTestCase {

    @Test
    public void testCheckLengthString() {
        String hello = "Hello, world";
        int stringLength = getLengthString(hello);

        System.out.println("Длина строки - ".concat(hello).concat(" равна = ")
                .concat(String.valueOf(stringLength)));
        assertTrue(stringLength > 15, "Длина строки - ".concat(hello).concat(" равна = ")
                .concat(String.valueOf(stringLength)));
    }
}