package com.projectstats;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Mojo(name = "analyze")
public class ProjectStatsMojo extends AbstractMojo {

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Запуск аналізу статистики проекту...");

        try {
            Path sourcePath = Paths.get("src/main/java");
            Path reportPath = Paths.get("target/project-stats.txt");

            if (!sourcePath.toFile().exists()) {
                getLog().warn("Директорія з кодом не існує: " + sourcePath.toAbsolutePath());
                return;
            }

            Map<String, Integer> stats = CodeAnalyzer.analyze(sourcePath);

            getLog().info("Статистика проекту:");
            stats.forEach((key, value) ->
                    getLog().info(String.format("%-12s: %d", key, value))
            );

            ReportGenerator.generateReport(stats, reportPath);

            getLog().info("Звіт збережено у файл: " + reportPath.toAbsolutePath());

        } catch (Exception e) {
            throw new MojoExecutionException("Помилка при аналізі проекту", e);
        }
    }
}