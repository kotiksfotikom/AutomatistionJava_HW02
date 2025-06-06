package com.projectstats;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        try {
            String sourcePath = args.length > 0 ? args[0] : "src/main/java";
            String outputPath = args.length > 1 ? args[1] : "target/project-stats.txt";

            System.out.println("Аналіз проекту...");
            System.out.println("Шлях до коду: " + sourcePath);
            System.out.println("Файл звіту: " + outputPath);

            Path source = Paths.get(sourcePath);
            Map<String, Integer> stats = CodeAnalyzer.analyze(source);

            ReportGenerator.printStatsToConsole(stats);

            Path reportPath = Paths.get(outputPath);
            ReportGenerator.generateReport(stats, reportPath);

            System.out.println("Звіт успішно збережено у файл: " + outputPath);

        } catch (Exception e) {
            System.err.println("Помилка: " + e.getMessage());
            e.printStackTrace();
        }
    }
}