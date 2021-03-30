package analyzer;

import analyzer.strategies.PatternChecker;
import analyzer.strategies.Strategy;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Callable;

public class FileChecker implements Callable<Integer> {
    private final PatternChecker patternChecker = new PatternChecker();
    private final Path inputFilePath;
    private final List<String> patternsList;

    public FileChecker(Strategy strategy, Path inputFilePath, List<String> patternsList) {
        this.patternChecker.setStrategy(strategy);
        this.inputFilePath = inputFilePath;
        this.patternsList = patternsList;
    }

    @Override
    public Integer call() throws Exception {
        byte[] allBytes = Files.readAllBytes(inputFilePath);

        String fileContents;

        if (allBytes.length != 0) {
            fileContents = new String(allBytes);
        } else {
            return -2;
        }

        boolean patternMatch = false;
        int currentPattern = 0;
        while (currentPattern < patternsList.size()) {
            patternMatch = patternChecker.check(fileContents, patternsList.get(currentPattern));
            if (patternMatch) break;
            currentPattern++;
        }

        return patternMatch ? currentPattern : -1;
    }
}
