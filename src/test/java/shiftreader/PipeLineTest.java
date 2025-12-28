package shiftreader;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PipeLineTest {
    String pathdefault = String.valueOf(Paths.get("").toAbsolutePath());

    @Test
    public void test00() {
        String[] args = {};
        PipeLine pl = new PipeLine(args);
        assertEquals(pathdefault, pl.getFullpath());
        assertEquals("", pl.getPrefix());
        assertFalse(pl.isAppend());
        assertFalse(pl.isFull());
        assertEquals(0, pl.getFiles().size());
    }
    @Test
    public void test01() {
        String[] args = {"-s", "-a", "-p", "sample-", "in1.txt", "gradlew.bat"};
        PipeLine pl = new PipeLine(args);
        assertEquals(pathdefault, pl.getFullpath());
        assertEquals("sample-", pl.getPrefix());
        assertTrue(pl.isAppend());
        assertFalse(pl.isFull());
        assertEquals(1, pl.getFiles().size());
    }
    @Test
    public void test02() {
        String[] args = {"-f", "-a", "-o", "nopath", "gradlew.bat"};
        PipeLine pl = new PipeLine(args);
        assertEquals(pathdefault, pl.getFullpath());
        assertEquals("", pl.getPrefix());
        assertTrue(pl.isAppend());
        assertTrue(pl.isFull());
        assertEquals(1, pl.getFiles().size());
    }
    @Test
    public void test03() {
        String[] args = {"-fag", "-o", "gradlew.bat"};
        PipeLine pl = new PipeLine(args);
        assertEquals(pathdefault, pl.getFullpath());
        assertEquals("", pl.getPrefix());
        assertTrue(pl.isAppend());
        assertTrue(pl.isFull());
        assertEquals(0, pl.getFiles().size());
    }
    @Test
    public void test04() {
        String[] args = {"g*radlew.bat"};
        PipeLine pl = new PipeLine(args);
        assertEquals(pathdefault, pl.getFullpath());
        assertEquals("", pl.getPrefix());
        assertFalse(pl.isAppend());
        assertFalse(pl.isFull());
        assertEquals(0, pl.getFiles().size());
    }
    @Test
    public void testDropRightNol() throws Exception {
        Method method = PipeLine.class.getDeclaredMethod("dropRightNol", String.class);
        method.setAccessible(true);
        assertEquals(".123", method.invoke(null,".123"));
        assertEquals(".0", method.invoke(null,"."));
        assertEquals(".0123", method.invoke(null,".01230"));
        assertEquals(".123", method.invoke(null,".123000"));
        assertEquals(".1230001", method.invoke(null,".123000100"));
        assertEquals("0123", method.invoke(null,"01230"));
        assertEquals("", method.invoke(null,"0000"));
        assertEquals("", method.invoke(null,""));
    }
    @Test
    public void testDropLeftNol() throws Exception {
        Method method = PipeLine.class.getDeclaredMethod("dropLeftNol", String.class);
        method.setAccessible(true);
        assertEquals("0", method.invoke(null, "0"));
        assertEquals("", method.invoke(null,""));
        assertEquals("+1", method.invoke(null,"+1"));
        assertEquals("1", method.invoke(null,"1"));
        assertEquals("1", method.invoke(null,"01"));
        assertEquals("1", method.invoke(null,"001"));
        assertEquals("100010", method.invoke(null,"0100010"));
        assertEquals("-100010", method.invoke(null,"-0100010"));
        assertEquals("+100010", method.invoke(null,"+0100010"));
        assertEquals("-1", method.invoke(null,"-0000000000000000000000000001"));
    }
    @Test
    public void testIsFloat() throws Exception {
        Method method = PipeLine.class.getDeclaredMethod("isFloat", String.class);
        method.setAccessible(true);
        assertTrue((Boolean) method.invoke(null,"00"));
        assertTrue((Boolean) method.invoke(null,"-001."));
        assertTrue((Boolean) method.invoke(null,".001"));
        assertTrue((Boolean) method.invoke(null,".001e1"));
        assertTrue((Boolean) method.invoke(null,"0.e1"));
        assertTrue((Boolean) method.invoke(null,"+0.e1"));
        assertTrue((Boolean) method.invoke(null,"100e12"));
        assertTrue((Boolean) method.invoke(null,"001e+12"));
        assertTrue((Boolean) method.invoke(null,"020e-02"));
        assertFalse((Boolean) method.invoke(null,"e100"));
        assertFalse((Boolean) method.invoke(null,"-e100"));
        assertFalse((Boolean) method.invoke(null,".e10"));
        assertFalse((Boolean) method.invoke(null,"-.e01"));
    }
    @Test
    public void testOuts() {
        String[] args = {"-p", "_nogit_", "src/test/resources/in1.txt", "src/test/resources/in2.txt"};
        PipeLine pl = new PipeLine(args);
        assertEquals("_nogit_", pl.getPrefix());
        assertEquals(pathdefault, pl.getFullpath());
        assertFalse(pl.isAppend());
        assertFalse(pl.isFull());
        assertEquals(2, pl.getFiles().size());
        assertEquals(3, pl.strings_int().size());
        assertEquals("45", pl.strings_int().get(0));
        assertEquals("100500", pl.strings_int().get(1));
        assertEquals("1234567890123456789", pl.strings_int().get(2));
        assertEquals(3, pl.strings_flt().size());
        assertEquals("3.1415", pl.strings_flt().get(0));
        assertEquals("-0.001", pl.strings_flt().get(1));
        assertEquals("1.528535047E-25", pl.strings_flt().get(2));
        assertEquals(6, pl.strings_str().size());
        assertEquals("Lorem ipsum dolor sit amet", pl.strings_str().get(0));
        assertEquals("Пример", pl.strings_str().get(1));
        assertEquals("consectetur adipiscing", pl.strings_str().get(2));
        assertEquals("тестовое задание", pl.strings_str().get(3));
        assertEquals("Нормальная форма числа с плавающей запятой", pl.strings_str().get(4));
        assertEquals("Long", pl.strings_str().get(5));
    }
    @Test
    public void testCalcInt() {
        CalcInt testitem = new CalcInt("tester");
        testitem.list.addAll(List.of("88888888888888888888", "0"));
        assertEquals("8.888888888888889E19", testitem.sum());
        assertEquals("4.444444444444444E19", testitem.average());
        testitem.list.clear();
        testitem.list.addAll(List.of("3", "4", "5"));
        assertEquals("12", testitem.sum());
        assertEquals("4.0", testitem.average());
        testitem.list.clear();
        testitem.list.addAll(List.of("100500", "500", "-100500", "+0000000000100400", "100400",
                "999999999999999999999999999999999999999999999999999999999", "-100400", "-200"));
        testitem.sort();
        assertEquals(8, testitem.list.size());
        assertEquals("-100500", testitem.list.get(0));
        assertEquals("-100400", testitem.list.get(1));
        assertEquals("-200", testitem.list.get(2));
        assertEquals("500", testitem.list.get(3));
        assertEquals("100400", testitem.list.get(4));
        assertEquals("+0000000000100400", testitem.list.get(5));
        assertEquals("100500", testitem.list.get(6));
        assertEquals("999999999999999999999999999999999999999999999999999999999",
                testitem.list.get(7));
    }
    @Test
    public void testCalcFlt() {
        CalcFlt c = new CalcFlt("tester");
        c.list.addAll(List.of("3.333E100500", "1.111E100500"));
        assertEquals("4.444E+100500", c.sum());
        assertEquals("2.222E+100500", c.average());
        c.list.clear();
        c.list.addAll(List.of("3.333E100500", "1.111E100500", "-23423423.234234e-1"));
        c.sort();
        assertEquals("-2342342.3234234", c.list.getFirst());
        assertEquals("3.333E+100500", c.list.getLast());
    }
    @Test
    public void testStr() {
        CalcStr c = new CalcStr("tester");
        c.list.addAll(List.of("123", "sdfasdfasdfasd", ""));
        c.sort();
        assertEquals("", c.list.getFirst());
        assertEquals("sdfasdfasdfasd", c.list.getLast());
    }
    @Test
    public void testIn3() {
        String[] args = {"-s", "-o" , "src/test/resources", "-p", "_nogit_in3_", "src/test/resources/in3.txt"};
        PipeLine pl = new PipeLine(args);
        assertEquals("_nogit_in3_", pl.getPrefix());
        assertFalse(pl.isAppend());
        assertEquals(1, pl.getFiles().size());
        assertEquals(10, pl.strings_int().size());
        assertEquals("0", pl.strings_int().get(0));
        assertEquals("-0", pl.strings_int().get(1));
        assertEquals("+0", pl.strings_int().get(2));
        assertEquals("123", pl.strings_int().get(3));
        assertEquals("-456", pl.strings_int().get(4));
        assertEquals("+789", pl.strings_int().get(5));
        assertEquals("1000000000000000000", pl.strings_int().get(6));
        assertEquals("-9223372036854775808", pl.strings_int().get(7));
        assertEquals("18446744073709551615", pl.strings_int().get(8));
        assertEquals("999999999999999999999999999999999999999999999999", pl.strings_int().get(9));
        assertEquals(10, pl.strings_flt().size());
        assertEquals("0.0", pl.strings_flt().get(0));
        assertEquals("0.0", pl.strings_flt().get(1));
        assertEquals("0.0", pl.strings_flt().get(2));
        assertEquals("3.141592653589793", pl.strings_flt().get(3));
        assertEquals("-2.718281828459045", pl.strings_flt().get(4));
        assertEquals("1.602176634E-19", pl.strings_flt().get(5));
        assertEquals("-0.0000000000000000000000062607015E999999999", pl.strings_flt().get(6));
        assertEquals("299792458.0", pl.strings_flt().get(7));
        assertEquals("-273.15", pl.strings_flt().get(8));
        assertEquals("1.7976931348623157E308", pl.strings_flt().get(9));
        assertEquals(10, pl.strings_str().size());
        assertEquals("Hello World!", pl.strings_str().get(0));
        assertEquals("E123", pl.strings_str().get(1));
        assertEquals("@Test", pl.strings_str().get(2));
        assertEquals("123E", pl.strings_str().get(3));
        assertEquals("3.14.15", pl.strings_str().get(4));
        assertEquals("null", pl.strings_str().get(5));
        assertEquals("\"quoted string\"", pl.strings_str().get(6));
        assertEquals("Special chars: !@#$%^&*()", pl.strings_str().get(7));
        assertEquals("Многострочная\\nстрока", pl.strings_str().get(8));
        assertEquals("Последняя тестовая строка", pl.strings_str().get(9));
    }

}