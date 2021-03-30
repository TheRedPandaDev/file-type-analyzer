
import analyzer.Main;
import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testcase.TestCase;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class MainTest extends StageTest<Clue> {
    private static final String CURRENT_DIR = System.getProperty("user.dir") + File.separator;

    private static String testFolder = "test_files";
    private static String files = testFolder + File.separator;

    private static String patternsDb = "patterns.db";
    private static final String patterns =
        "1;\"%PDF-\";\"PDF document\"\n" +
            "2;\"pmview\";\"PCP pmview config\"\n" +
            "4;\"PK\";\"Zip archive\"\n" +
            "5;\"vnd.oasis.opendocument.presentation\";\"OpenDocument presentation\"\n" +
            "6;\"W.o.r.d\";\"MS Office Word 2003\"\n" +
            "6;\"P.o.w.e.r.P.o.i\";\"MS Office PowerPoint 2003\"\n" +
            "7;\"word/_rels\";\"MS Office Word 2007+\"\n" +
            "7;\"ppt/_rels\";\"MS Office PowerPoint 2007+\"\n" +
            "7;\"xl/_rels\";\"MS Office Excel 2007+\"\n" +
            "8;\"-----BEGIN\\ CERTIFICATE-----\";\"PEM certificate\"\n" +
            "9;\"ftypjp2\";\"ISO Media JPEG 2000\"\n" +
            "9;\"ftypiso2\";\"ISO Media MP4 Base Media v2\"\n";

    @Override
    public List<TestCase<Clue>> generate() {
        return List.of(
            //Testing basic cases
            //PDF
            new TestCase<Clue>()
                .addArguments(new String[]
                    {testFolder, patternsDb})
                .addFile(patternsDb, patterns)
                .addFile(files + "doc.pdf", "PFDF%PDF-PDF")
                .addFile(files + "text.pdf", "PFPDF-PDFABC")
                .setAttach(new Clue(
                    "doc.pdf: PDF document",
                    "text.pdf: Unknown file type",
                    "Wrong answer for files with PDF documents")),

            //Zip archive
            new TestCase<Clue>()
                .addArguments(new String[] {testFolder, patternsDb})
                .addFile(patternsDb, patterns)
                .addFile(files + "doc.zip", "PCK")
                .addFile(files + "doc1.zip", "PKC")
                .setAttach(new Clue(
                    "doc.zip: Unknown file type" ,
                    "doc1.zip: Zip archive",
                    "Wrong answer for files with Zip archives")),

            //PCP pmview config
            new TestCase<Clue>()
                .addArguments(new String[]
                    {testFolder, patternsDb})
                .addFile(patternsDb, patterns)
                .addFile(files + "main", "pmview")
                .addFile(files + "main.config", "pmconfigview")
                .setAttach(new Clue(
                    "main: PCP pmview config",
                    "main.config: Unknown file type",
                    "Wrong answer for PCP pmview config files")),

            //OpenDocument presentation
            new TestCase<Clue>()
                .addArguments(new String[]
                    {testFolder, patternsDb})
                .addFile(patternsDb, patterns)
                .addFile(files + "slides1.odp", "vnd.oasis.opendocument.presentation")
                .addFile(files + "slides2.odp", "vnd.oasis.microsoft.presentation")
                .setAttach(new Clue(
                    "slides1.odp: OpenDocument presentation",
                    "slides2.odp: Unknown file type",
                    "")),

            //MS Office Word 2003
            new TestCase<Clue>()
                .addArguments(new String[]
                    {testFolder, patternsDb})
                .addFile(patternsDb, patterns)
                .addFile(files + "txt.doc", "wwwwwwwwW.o.r.d")
                .addFile(files + "doc.txt", "W.o.r.kwwwwwwww")
                .setAttach(new Clue(
                    "doc.txt: Unknown file type",
                    "txt.doc: MS Office Word 2003",
                    "Wrong answer for Word 2003 files")),

            //MS Office PowerPoint 2003
            new TestCase<Clue>()
                .addArguments(new String[]
                    {testFolder, patternsDb})
                .addFile(patternsDb, patterns)
                .addFile(files + "slides1.ptp", "P.o.w.e.r.P.o.i")
                .addFile(files + "slides2.ptp", "P.o.w.e.r.\\Sh.o.i")
                .setAttach(new Clue(
                    "slides1.ptp: MS Office PowerPoint 2003",
                    "slides2.ptp: Unknown file type",
                    "")),

            //MS Office Word 2007+
            new TestCase<Clue>()
                .addArguments(new String[]
                    {testFolder, patternsDb})
                .addFile(patternsDb, patterns)
                .addFile(files + "txt.doc", "\\word/_rels")
                .addFile(files + "doc.txt", "word/\\_rels")
                .setAttach(new Clue(
                    "doc.txt: Unknown file type",
                    "txt.doc: MS Office Word 2007+",
                    "Wrong answer for Word 2007+ files")),

            //MS Office PowerPoint 2007+
            new TestCase<Clue>()
                .addArguments(new String[]
                    {testFolder, patternsDb})
                .addFile(patternsDb, patterns)
                .addFile(files + "pres1.pptx", "afeefa%ppt/_relsasdad")
                .addFile(files + "pres2.pptx", "ppasfsfafdaet/_rels")
                .setAttach(new Clue(
                    "pres1.pptx: MS Office PowerPoint 2007+",
                    "pres2.pptx: Unknown file type",
                    "")),

            //MS Office Excel 2007+
            new TestCase<Clue>()
                .addArguments(new String[]
                    {testFolder, patternsDb})
                .addFile(patternsDb, patterns)
                .addFile(files + "sheet1.xls", "asdaxl/_rels")
                .addFile(files + "sheet2.xls", "x2/_reasdadls")
                .setAttach(new Clue(
                    "sheet1.xls: MS Office Excel 2007+",
                    "sheet2.xls: Unknown file type",
                    "Wrong answer for Excel 2007+ files")),

            //PEM certificate
            new TestCase<Clue>()
                .addArguments(new String[]
                    {testFolder, patternsDb})
                .addFile(patternsDb, patterns)
                .addFile(files + "cert.pem", "\\\\\\\\\\aasdw-----BEGIN\\ CERTIFICATE-----")
                .addFile(files + "cert_core.pem", "\\\\\\\\\\adww-----BEGIN\\CERTIFICATE-----")
                .setAttach(new Clue(
                    "cert.pem: PEM certificate",
                    "cert_core.pem: Unknown file type",
                    "")),

            //ISO Media JPEG 2000
            new TestCase<Clue>()
                .addArguments(new String[]
                    {testFolder, patternsDb})
                .addFile(patternsDb, patterns)
                .addFile(files + "1.jpg", "ftypjp2ddddddaa")
                .addFile(files + "2.jpg", "ftypdddjp2dadad")
                .setAttach(new Clue(
                    "1.jpg: ISO Media JPEG 2000",
                    "2.jpg: Unknown file type",
                    "Wrong answer for ISO Media JPEG 2000 files")),

            //ISO Media MP4 Base Media v2
            new TestCase<Clue>()
                .addArguments(new String[]
                    {testFolder, patternsDb})
                .addFile(patternsDb, patterns)
                .addFile(files + "tape.jpg", "ftypiso2mp4")
                .addFile(files + "tape.mp4", "mp4ffttypiso2")
                .setAttach(new Clue(
                    "tape.jpg: ISO Media MP4 Base Media v2",
                    "tape.mp4: Unknown file type",
                    "")),

            //Testing patterns priority
            new TestCase<Clue>()
                .addArguments(new String[]
                    {testFolder, patternsDb})
                .addFile(patternsDb, patterns)
                .addFile(files + "tape2.jpg", "PK W.o.r.d")
                .addFile(files + "tape2.mp4", "%PDF-mp4fftypiso2")
                .setAttach(new Clue(
                    "tape2.jpg: MS Office Word 2003",
                    "tape2.mp4: ISO Media MP4 Base Media v2",
                    "Wrong answer white testing priority")),

            new TestCase<Clue>()
                .addArguments(new String[]
                    {testFolder, patternsDb})
                .addFile(patternsDb, patterns)
                .addFile(files + "tape3.jpg", "-----BEGIN\\ CERTIFICATE-----pmview")
                .addFile(files + "tape3.mp4", "%PDF-ftypppfW.o.r.dftypiso")
                .setAttach(new Clue(
                    "tape3.jpg: PEM certificate",
                    "tape3.mp4: MS Office Word 2003",
                    ""))
        );
    }

    @Override
    public CheckResult check(String reply, Clue clue) {
        String actual = reply.strip();

        if (!actual.contains(clue.first)) {
            return CheckResult.wrong(
                clue.feedback + "\n\n" +
                    "Cannot find a line " + clue.first + "\nYour output:\n" + actual
            );
        }

        if (!actual.contains(clue.second)) {
            return CheckResult.wrong(
                clue.feedback + "\n\n" +
                    "Cannot find a line " + clue.second + "\nYour output:\n" + actual
            );
        }

        return CheckResult.correct();
    }

    @BeforeClass
    public static void generateTestFilesDirectory() {

        deleteTestFilesDirectory();

        try {
            Files.deleteIfExists(Paths.get(CURRENT_DIR + testFolder));
        }
        catch (IOException ex) {

        }
        try {
            Files.createDirectory(Paths.get(CURRENT_DIR + testFolder));
        }
        catch (IOException ex) {

        }
    }


    @AfterClass
    public static void deleteTestFilesDirectory() {
        try {
            Files.deleteIfExists(Paths.get(CURRENT_DIR + testFolder));
        }
        catch (IOException ex) {


        }
    }

}
