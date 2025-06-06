package com.projectstats;

import java.io.*;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class ReportGenerator {

    public static void generateReport(Map<String, Integer> projectStats, Path reportFile)
            throws IOException {
        File outputFile = reportFile.toFile();

        if (outputFile.getParentFile() != null && !outputFile.getParentFile().exists()) {
            outputFile.getParentFile().mkdirs();
        }

        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;

        try {
            fileWriter = new FileWriter(outputFile);
            bufferedWriter = new BufferedWriter(fileWriter);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            bufferedWriter.write("--- Звіт по проекту ---");
            bufferedWriter.newLine();
            bufferedWriter.write("Дата створення: " + dateFormat.format(new Date()));
            bufferedWriter.newLine();
            bufferedWriter.newLine();

            for (String key : projectStats.keySet()) {
                Integer value = projectStats.get(key);
                String line = String.format("%-12s: %d", key, value);
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }

            bufferedWriter.newLine();
            bufferedWriter.write("--- Кінець звіту ---");

        } catch (IOException ex) {
            throw new IOException("Не вдалося створити звіт: " + ex.getMessage(), ex);
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    System.err.println("Помилка при закритті BufferedWriter: " + e.getMessage());
                }
            }
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    System.err.println("Помилка при закритті FileWriter: " + e.getMessage());
                }
            }
        }
    }

    public static void printStatsToConsole(Map<String, Integer> stats) {
        System.out.println("\n--- Статистика проекту ---");
        stats.forEach((key, value) ->
                System.out.printf("%-12s: %d%n", key, value)
        );
        System.out.println("----------------------------\n");
    }
}