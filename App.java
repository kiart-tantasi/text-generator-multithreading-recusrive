import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class App {

    private static String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private static int length = 2;

    public static void main(String[] args) throws Exception {
        int nThreads;

        // reset result.txt file
        resetResultFile();

        // 1 Thread
        nThreads = 1;
        long start = System.currentTimeMillis();
        generatePasswordsWithMultipleThreads(chars, "", length, nThreads);
        System.out.println(String.format("%s Thread - done in %s ms", nThreads, (System.currentTimeMillis() - start)));

        // 10 threads
        nThreads = 10;
        start = System.currentTimeMillis();
        generatePasswordsWithMultipleThreads(chars, "", length, nThreads);
        System.out.println(String.format("%s Threads - done in %s ms", nThreads, (System.currentTimeMillis() - start)));
    }

    private static void generatePasswordsWithMultipleThreads(String chars, String cur, int length, int nThreads) throws Exception {
        ExecutorService eService = Executors.newFixedThreadPool(nThreads);

        int payloadSize = Math.max(1, chars.length() / nThreads);
        int charsLength = chars.length();
        int startIndex = 0;

        while (startIndex < chars.length()) {
            int startIdx = startIndex;
            int endIdx = Math.min(charsLength, (startIndex + payloadSize));

            eService.submit((() -> {
                for (int i = startIdx; i < endIdx; i++) {
                    try {
                        generatePasswords(chars, String.valueOf(chars.charAt(i)), length);
                    } catch (Exception e) {
                        System.out.println("\n" + e.getMessage() + "\n");
                    }
                }
            }));

            startIndex += payloadSize;
        }

        eService.shutdown();
        eService.awaitTermination(20, TimeUnit.MINUTES);
    }

    private static void generatePasswords(String chars, String cur, int length) throws Exception {
        if (chars == null || chars.length() == 0) {
            throw new Exception("Empty Characters !");
        } else if (cur.length() == length) {
            appendLineToResultFile(cur);
            return;
        }

        String charsToIterate = chars;
        while (charsToIterate.length() >= 1) {
            String newCur = cur + charsToIterate.charAt(0);
            generatePasswords(chars, newCur, length);
            charsToIterate = charsToIterate.substring(1);
        }
    }

    private static void resetResultFile() {
        File file = new File("result.txt");
        if (file.exists()) {
            file.delete();
        }
    }

    // private static void writeStringLinkedListToFile(LinkedList<String> list) throws IOException {
    //     FileWriter fr = new FileWriter(new File("result.txt"), true);
    //     for (String password: list) {
    //         fr.write(password + "\n");
    //     }
    //     fr.write("\n");
    //     fr.close();
    // }

    private static void appendLineToResultFile(String line) throws IOException {
        FileWriter fr = new FileWriter(new File("result.txt"), true);
        fr.write(line + "\n");
        fr.close();
    }
}
