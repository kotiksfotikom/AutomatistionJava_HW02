package com.projectstats;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class CodeAnalyzer {
    public static Map<String, Integer> analyze(Path sourcePath) throws IOException {
        Map<String, Integer> stats = new LinkedHashMap<>();

        List<Path> javaFiles = findJavaFiles(sourcePath);

        int totalClasses = 0;
        int totalMethods = 0;
        int totalLines = 0;

        for (Path javaFile : javaFiles) {
            try {
                CompilationUnit compilationUnit = StaticJavaParser.parse(javaFile);
                List<String> fileLines = Files.readAllLines(javaFile);

                totalClasses += compilationUnit.getTypes().size();

                List<MethodDeclaration> methods = compilationUnit.findAll(MethodDeclaration.class);

                totalMethods += methods.size();
                totalLines += fileLines.size();

            } catch (Exception e) {
                System.err.println("Помилка при обробці файлу: " + javaFile + " - " + e.getMessage());
            }
        }

        stats.put("Classes", totalClasses);
        stats.put("Methods", totalMethods);
        stats.put("Lines", totalLines);

        return stats;
    }

    private static List<Path> findJavaFiles(Path rootPath) throws IOException {
        return Files.walk(rootPath)
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".java"))
                .collect(Collectors.toList());
    }
}