package com.projectstats;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Mojo(name = "analyze", defaultPhase = LifecyclePhase.COMPILE)
public class ProjectStatsMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    @Parameter(property = "outputFile", defaultValue = "${project.build.directory}/project-stats.txt")
    private String outputFile;

    @Parameter(property = "sourceDirectory", defaultValue = "${project.build.sourceDirectory}")
    private String sourceDirectory;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Запуск аналізу статистики проекту...");

        try {
            Path sourcePath = Paths.get(sourceDirectory);
            if (!sourcePath.toFile().exists()) {
                getLog().warn("Директорія з кодом не існує: " + sourceDirectory);
                return;
            }

            Map<String, Integer> stats = CodeAnalyzer.analyze(sourcePath);

            getLog().info("Статистика проекту:");
            stats.forEach((key, value) ->
                    getLog().info(String.format("%-12s: %d", key, value))
            );

            Path reportPath = Paths.get(outputFile);
            ReportGenerator.generateReport(stats, reportPath);

            getLog().info("Звіт збережено у файл: " + outputFile);

        } catch (Exception e) {
            throw new MojoExecutionException("Помилка при аналізі проекту", e);
        }
    }
}