package com.changjiang.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Mojo(name = "configure-shade-plugin")
public class ConfigureShadePluginMojo extends AbstractMojo {
    @Parameter(defaultValue = "${project.build.outputDirectory}/serviceconfig.classes")
    private File serviceConfigFile;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (!serviceConfigFile.exists()) {
            getLog().warn("serviceconfig.classes file not found: " + serviceConfigFile.getAbsolutePath());
            return;
        }

        List<String> includes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(serviceConfigFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("#");
                if (parts.length == 2) {
                    includes.add(parts[0].replace('.', '/') + ".class");
                }
            }
        } catch (IOException e) {
            throw new MojoExecutionException("Failed to read serviceconfig.classes file", e);
        }

        getLog().info("Generated includes: " + includes);
    }
}
