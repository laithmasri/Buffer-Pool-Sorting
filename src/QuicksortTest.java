import student.TestCase;

/**
 * 
 * @author Laith Al-Masri
 * @version Oct 16th
 * 
 */
public class QuicksortTest extends TestCase {
    private CheckFile fileChecker;

    /**
     * Sets up the tests that follow. In general, used for initialization.
     */
    public void setUp() {
        fileChecker = new CheckFile();
    }


    /**
     * This method is a demonstration of the file generator and file checker
     * functionality. It calles generateFile to create a small "ascii" file.
     * It then calls the file checker to see if it is sorted (presumably not
     * since we don't call a sort method in this test, so we assertFalse).
     * 
     * @throws Exception
     *             either a IOException or FileNotFoundException
     */
    public void testFileGenerator() throws Exception {
        String[] args = new String[3];
        args[0] = "input.txt";
        args[1] = "1";
        args[2] = "stats.txt";
        Quicksort.generateFile("input.txt", "1", 'a');
        // In a real test we would call the sort
        Quicksort.main(args);
        // In a real test, the following would be assertTrue()
        assertTrue(fileChecker.checkFile("input.txt"));
    }


    /**
     * 
     * Get code coverage of the class declaration.
     * 
     */
    public void testQInit() {
        Quicksort tree = new Quicksort();
        assertNotNull(tree);
        // Quicksort.main(null);
    }


    /**
     * This test case checks the 4th reference test.
     * 
     * @throws Exception
     */
    public void test4() throws Exception {
        String[] args = new String[3];
        args[0] = "test4.txt";
        args[1] = "10";
        args[2] = "stats.txt";
        Quicksort.generateFile("test4.txt", "10", 'a');
        Quicksort.main(args);
        assertTrue(fileChecker.checkFile("test4.txt"));
    }


    /**
     * This test case checks the 5th reference test.
     * 
     * @throws Exception
     */
    public void test5() throws Exception {
        String[] args = new String[3];
        args[0] = "test5.txt";
        args[1] = "4";
        args[2] = "stats.txt";
        Quicksort.generateFile("test5.txt", "10", 'a');
        Quicksort.main(args);
        assertTrue(fileChecker.checkFile("test5.txt"));
    }


    /**
     * This test case checks the 6th reference test.
     * 
     * @throws Exception
     */
    public void test6() throws Exception {
        String[] args = new String[3];
        args[0] = "test6.txt";
        args[1] = "1";
        args[2] = "stats.txt";
        Quicksort.generateFile("test6.txt", "10", 'b');
        Quicksort.main(args);
        assertTrue(fileChecker.checkFile("test6.txt"));
    }


    /**
     * This test case checks the 7th reference test.
     * 
     * @throws Exception
     */
    public void test7() throws Exception {
        String[] args = new String[3];
        args[0] = "test7.txt";
        args[1] = "10";
        args[2] = "stats.txt";
        Quicksort.generateFile("test7.txt", "100", 'b');
        Quicksort.main(args);
        assertTrue(fileChecker.checkFile("test7.txt"));
    }


    /**
     * This test case checks the 8th reference test.
     * 
     * @throws Exception
     */
    public void test8() throws Exception {
        String[] args = new String[3];
        args[0] = "test8.txt";
        args[1] = "10";
        args[2] = "stats.txt";
        Quicksort.generateFile("test8.txt", "100", 'b');
        Quicksort.main(args);
        assertTrue(fileChecker.checkFile("test8.txt"));
    }


    /**
     * This test case checks the 9th reference test.
     * 
     * @throws Exception
     */
    public void test9() throws Exception {
        String[] args = new String[3];
        args[0] = "test9.txt";
        args[1] = "10";
        args[2] = "stats.txt";
        Quicksort.generateFile("test9.txt", "1000", 'b');
        Quicksort.main(args);
        assertTrue(fileChecker.checkFile("test9.txt"));
    }


    /**
     * This test case checks the 10th reference test.
     * 
     * @throws Exception
     */
    public void test10() throws Exception {
        String[] args = new String[3];
        args[0] = "test10.txt";
        args[1] = "10";
        args[2] = "stats.txt";
        Quicksort.generateFile("test10.txt", "1000", 'b');
        Quicksort.main(args);
        assertTrue(fileChecker.checkFile("test10.txt"));
    }


    /**
     * This test case checks the 11th reference test.
     * 
     * @throws Exception
     */
    public void test11() throws Exception {
        String[] args = new String[3];
        args[0] = "test11.txt";
        args[1] = "10";
        args[2] = "stats.txt";
        Quicksort.generateFile("test11.txt", "1000", 'b');
        Quicksort.main(args);
        assertTrue(fileChecker.checkFile("test11.txt"));
    }


    /**
     * This test case checks the 12th reference test.
     * 
     * @throws Exception
     */
    public void test12() throws Exception {
        String[] args = new String[3];
        args[0] = "test12.txt";
        args[1] = "10";
        args[2] = "stats.txt";
        Quicksort.generateFile("test12.txt", "1000", 'a');
        Quicksort.main(args);
        assertTrue(fileChecker.checkFile("test12.txt"));
    }

}
