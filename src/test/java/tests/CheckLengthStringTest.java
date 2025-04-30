package tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import lib.BaseTestCase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CheckLengthStringTest extends BaseTestCase {

    @Epic(value = "Строки")
    @Feature(value = "Проверка длины строки")
    @Story(value = "Равенство строки")
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