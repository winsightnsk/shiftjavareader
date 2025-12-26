package shiftreader;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

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
        assertEquals(pathdefault, pl.getFullpath());
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
        assertEquals(pathdefault, pl.getFullpath());
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
        assertEquals(pathdefault, pl.getFullpath());
        assertFalse(pl.isAppend());
        assertFalse(pl.isFull());
        assertEquals(0, pl.getFiles().size());
    }

}