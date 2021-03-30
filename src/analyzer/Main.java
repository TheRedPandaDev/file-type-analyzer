package analyzer;

import analyzer.strategies.KMPStrategy;
import analyzer.strategies.RPStrategy;
import analyzer.strategies.Strategy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        if (args.length != 2) {
            System.out.println("Please provide the patterns file and the directory path");
            System.exit(0);
        }

        String inputFilesPath = args[0];
        String patternsFile = args[1];

        List<String> lines = new ArrayList<>();

        List<String> patterns = new ArrayList<>();
        List<String> fileTypes = new ArrayList<>();

        try (Stream<String> stream = Files.lines(Paths.get(patternsFile))) {
            stream.forEach(lines::add);
        } catch (IOException e) {
            e.printStackTrace();
        }

        lines.sort(Comparator.comparingInt(l -> Character.getNumericValue(l.charAt(0))));

        Collections.reverse(lines);

        lines.forEach(s -> {
            String[] line = s.split(";");
            patterns.add(line[1].substring(1, line[1].length() - 1));
            fileTypes.add(line[2].substring(1, line[2].length() - 1));
        });

        Strategy strategyToUse = new KMPStrategy();

        List<Path> filePaths = null;

        try (Stream<Path> walk = Files.walk(Paths.get(inputFilesPath))) {
            filePaths = walk
                    .filter(Files::isRegularFile)
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }

        List<Callable<Integer>> checkerList = new ArrayList<>();


        for (Path fileToCheck : filePaths) {
            checkerList.add(new FileChecker(strategyToUse, fileToCheck, patterns));
        }

        List<Future<Integer>> checkerResults = new ArrayList<>();

        for (Callable<Integer> fileChecker : checkerList) {
            checkerResults.add(executorService.submit(fileChecker));
        }

        for (int i = 0; i < filePaths.size(); i++) {
            System.out.print(filePaths.get(i).getFileName() + ": ");
            try {
                int patternIndex = checkerResults.get(i).get();
                if (patternIndex == -2) {
                    System.out.println("File is empty");
                } else if (patternIndex == -1) {
                    System.out.println("Unknown file type");
                } else {
                    System.out.println(fileTypes.get(patternIndex));
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                System.exit(0);
            }
        }

        executorService.shutdown();
    }
}
