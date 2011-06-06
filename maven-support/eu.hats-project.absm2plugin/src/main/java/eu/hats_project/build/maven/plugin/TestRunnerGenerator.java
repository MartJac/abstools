package eu.hats_project.build.maven.plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import abs.backend.tests.ABSTestRunnerCompiler;

public class TestRunnerGenerator {

    void generateTestRunner(
            File absfrontEnd, 
            List<String> absfiles, 
            File absTestRunnerFile) throws Exception {

        if (!absTestRunnerFile.exists() && !absTestRunnerFile.getParentFile().mkdirs()
                && !absTestRunnerFile.createNewFile()) {
            throw new MojoFailureException("Cannot write to file: " + absTestRunnerFile);
        }

        List<String> args = new ArrayList<String>();
        System.setProperty("java.class.path", absfrontEnd.getAbsolutePath());
        args.add("-o");
        args.add(absTestRunnerFile.getAbsolutePath());
        args.addAll(absfiles);

        try {
            ABSTestRunnerCompiler.main(args.toArray(new String[0]));
        } catch (Exception e) {
            throw new MojoExecutionException("Could not generate ABSUnit test runner", e);
        }

    }

}