package analyzer;

import analyzer.strategies.PatternChecker;
import analyzer.strategies.Strategy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

public class FileChecker implements Callable<Boolean> {
    private final PatternChecker patternChecker = new PatternChecker();
    private final Path inputFilePath;
    private final String patternString;

    public FileChecker(Strategy strategy, Path inputFilePath, String patternString) {
        this.patternChecker.setStrategy(strategy);
        this.inputFilePath = inputFilePath;
        this.patternString = patternString;
    }

    @Override
    public Boolean call() throws Exception {
        byte[] allBytes = null;

        try {
            allBytes = Files.readAllBytes(inputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }

        String fileContents = "";

        if (allBytes.length != 0) {
            fileContents = new String(allBytes);
        } else {
            System.out.println("The file is empty");
            System.exit(0);
        }

        return patternChecker.check(fileContents, patternString);
    }
}
