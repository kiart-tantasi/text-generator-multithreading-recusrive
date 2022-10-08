import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
// import java.util.concurrent.ExecutorService;
// import java.util.concurrent.Executors;

class App {
    
    // private static ExecutorService executorService = Executors.newFixedThreadPool(4);
    
    private static int passwordLength = 3;

    private static String chars = "0123456789";

    public static void main(String[] args) throws Exception {
        // record start time
        long start = System.currentTimeMillis();

        // generate
        generatePassword(chars, "", passwordLength);

        // record end time
        long end = System.currentTimeMillis();

        // print used time
        System.out.println("Done in " + (end - start) + " ms.");
    }

    // private static void generatePasswordCallables(String chars, String cur, int length) throws Exception {
    // }

    private static void generatePassword(String chars, String cur, int length) throws Exception {
        if (chars == null || chars.length() == 0) {
            throw new Exception("Empty characters");
        } else if (cur.length() == length) {
            appendLineToFile(cur);
            return;
        }

        String charsToIterate = chars;
        // "abcde"
        while (charsToIterate.length() > 0) {
            char newChar = charsToIterate.charAt(0);

            if (charsToIterate.length() >= 2) {
                charsToIterate = charsToIterate.substring(1);
            } else {
                charsToIterate = "";
            }

            String newCur = cur + newChar;
            generatePassword(chars, newCur, length);
        }
    }


    private static void appendLineToFile(String password) throws IOException {
        FileWriter fr = new FileWriter(new File("result.txt"), true);

        fr.write(password + "\n");
        fr.close();
    }
}
