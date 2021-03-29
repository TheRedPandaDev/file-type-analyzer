
import analyzer.Main;
import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testcase.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;

public class MainTest extends StageTest<Clue> {
    private static final String CURRENT_DIR = System.getProperty("user.dir") + File.separator;
    private static int startedThreadCount = 0;

    @Override
    public List<TestCase<Clue>> generate() {
        return List.of(
                //Testing basic cases
                new TestCase<Clue>()
                        .addArguments(new String[]
                                {"test_files", "%PDF-", "PDF document"})
                        .addFile("test_files" + File.separator + "doc.pdf", "PFDF%PDF-PDF")
                        .addFile("test_files" + File.separator + "text.txt", "PF%PDF-PDFABC")
                        .setAttach(new Clue("doc.pdf: PDF document\n" +
                                "text.txt: PDF document", 2, "The files had following content: " + "\n" +
                                "\"PFDF%PDF-PDF\", \"PF%PDF-PDFABC\" and were analyzed for pattern \"%PDF-\"")),

                new TestCase<Clue>()
                        .addArguments(new String[]
                                {"test_files", "-----BEGIN\\CERTIFICATE-----", "PEM certificate"})
                        .addFile("test_files" + File.separator + "file.pem", "PF-----BEGIN\\CERTIFICATE-----DF%PDF-PDF")
                        .addFile("test_files" + File.separator + "file.pdf", "PF%P-----BEGIN\\CERTIFICATE-----DF-PDF")
                        .setAttach(new Clue("file.pdf: PEM certificate\n" +
                                "file.pem: PEM certificate", 2, "")),

                //Testing with only one file in the directory
                new TestCase<Clue>()
                        .addArguments(new String[]
                                {"test_files", "-----BEGIN\\CERTIFICATE-----", "PEM certificate"})
                        .addFile("test_files" + File.separator + "the_only_file.pem", "PF-----BEGIN\\CERTIFICATE-----DF%PDF-PDF")
                        .setAttach(new Clue("the_only_file.pem: PEM certificate", 1, "")),

                //Testing with 10 files in the directory
                new TestCase<Clue>()
                        .addArguments(new String[]
                                {"test_files", "%txt-", "TXT document"})
                        .addFile("test_files" + File.separator + "doc0.pdf", "PFDF%PDF%txt-PDF")
                        .addFile("test_files" + File.separator + "doc1.pdf", "PF%PDF%txt-PDF")
                        .addFile("test_files" + File.separator + "doc2.pdf", "PFDF%PDF%txt-PF")
                        .addFile("test_files" + File.separator + "doc3.pdf", "PF%PF%txt-PDF")
                        .addFile("test_files" + File.separator + "doc4.pdf", "PFDF%PDF%txt-PDF")
                        .addFile("test_files" + File.separator + "doc5.pdf", "PF%PDF%txt-PDF")
                        .addFile("test_files" + File.separator + "doc6.pdf", "PFF%PDF%txt-PDF")
                        .addFile("test_files" + File.separator + "doc7.pdf", "PF%DF%txt-PDF")
                        .addFile("test_files" + File.separator + "doc8.pdf", "PFDF%PDFPDF")
                        .addFile("test_files" + File.separator + "doc9.txt", "PF%PDF%PDF")
                        .setAttach(new Clue("doc0.pdf: TXT document\n" +
                                "doc1.pdf: TXT document\n" +
                                "doc2.pdf: TXT document\n" +
                                "doc3.pdf: TXT document\n" +
                                "doc4.pdf: TXT document\n" +
                                "doc5.pdf: TXT document\n" +
                                "doc6.pdf: TXT document\n" +
                                "doc7.pdf: TXT document\n" +
                                "doc8.pdf: Unknown file type\n" +
                                "doc9.txt: Unknown file type", 10, ""))
        );
    }

    @Override
    public CheckResult check(String reply, Clue clue) {
        long currentThreads = ManagementFactory.getThreadMXBean().getTotalStartedThreadCount();
        long startedThisTestThread = currentThreads - startedThreadCount;

        long neededThreads = startedThreadCount + clue.threadsStarted;
        long startedThisTestNeededThreads = neededThreads - startedThreadCount;

        Function<Long, String> threadName = count ->
                count + " thread" + (count == 1 ? "" : "s");

        if (startedThisTestThread < startedThisTestNeededThreads) {
            return CheckResult.wrong(
                    "There were not enough Threads started, " +
                            "you should process each file in a different Thread. " +
                            "\nYou started " + threadName.apply(startedThisTestThread) + ", " +
                            "but you need to start " + threadName.apply(startedThisTestNeededThreads) + ". " +
                            "\nThe executor.invokeAll() method might help. Also, don't forget to call .shutdown()."
            );
        }

        startedThreadCount = (int) currentThreads;

        String[] expectedLines = clue.output.split("\n");
        String actual = reply.strip();

        if (actual.isEmpty()) {
            return CheckResult.wrong("No output (check if you exited " +
                    "from main too early, you should wait until all threads are finishes)");
        }

        int expectedLinesLength = expectedLines.length;

        if (actual.trim().split("\n").length != expectedLines.length) {
            return CheckResult.wrong(String.format("There is should be %d lines in the output when you check %d files!\nMake sure you don't print empty lines.", expectedLinesLength, expectedLinesLength));
        }

        for (String expectedLine : expectedLines) {
            if (!actual.toLowerCase().contains(expectedLine.toLowerCase())) {
                return new CheckResult(false, "Can't find the line '"
                        + expectedLine + "' in the output!");
            }
        }

        return CheckResult.correct();
    }

    @BeforeClass
    public static void generateTestFilesDirectory() {
        try {
            Files.deleteIfExists(Paths.get(CURRENT_DIR + "test_files"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            Files.createDirectory(Paths.get(CURRENT_DIR + "test_files"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @BeforeClass
    public static void countThreadsBefore() {
        startedThreadCount += ManagementFactory.getThreadMXBean().getTotalStartedThreadCount();
    }

    @AfterClass
    public static void deleteTestFilesDirectory() {
        try {
            Files.deleteIfExists(Paths.get(CURRENT_DIR + "test_files"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
