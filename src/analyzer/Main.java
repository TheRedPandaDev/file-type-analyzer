package analyzer;

import analyzer.strategies.KMPStrategy;
import analyzer.strategies.NaiveStrategy;
import analyzer.strategies.PatternChecker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;

public class Main {
    private static final PatternChecker patternChecker = new PatternChecker();

    public static void main(String[] args) {
        if (args.length != 4 || Arrays.stream(args).anyMatch(Objects::isNull)) {
            System.out.println("Please provide checking strategy, relative file path, the pattern and type");
            System.exit(0);
        }

        String checkingStrategy = args[0];
        String inputFilePath = args[1];
        String patternString = args[2];
        String resultString = args[3];


        switch (checkingStrategy) {
            case "--naive":
                patternChecker.setStrategy(new NaiveStrategy());
                break;
            case "--KMP":
                patternChecker.setStrategy(new KMPStrategy());
                break;
            default:
                System.out.println("Invalid checking strategy");
                System.exit(0);
        }

        byte[] allBytes = null;

        try {
            allBytes = Files.readAllBytes(Paths.get(inputFilePath));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }

        String fileContents = null;

        if (allBytes.length != 0) {
            fileContents = new String(allBytes);
        } else {
            System.out.println("The file is empty");
            System.exit(0);
        }

        long startTime = System.nanoTime();

        if (patternChecker.check(fileContents, patternString)) {
            System.out.println(resultString);
        } else {
            System.out.println("Unknown file type");
        }

        double elapsedTimeSeconds = (System.nanoTime() - startTime) / 1_000_000_000.0;

        System.out.println("It took " + String.format("%.3f", elapsedTimeSeconds) + " seconds");
    }
}
