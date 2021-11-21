package com.git.hub;

import com.git.hub.task.DiffCallable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public class Git {

    // example of usage
    private static void initAndAddFile() throws Exception {
        Path directory = Paths.get("c:\\temp\\example");
        Files.createDirectories(directory);
        gitInit(directory);
        Files.write(directory.resolve("example.txt"), new byte[0]);
        gitStage(directory);
        gitCommit(directory, "Add example.txt");
    }

    // example of usage
    private static void cloneAndAddFile() throws Exception {
        String originUrl = "https://github.com/githubshah/MyWorld.git";
        Path directory = Paths.get("c:\\temp\\TokenReplacer");
        gitClone(directory, originUrl);
        Files.write(directory.resolve("example.txt"), new byte[0]);
        gitStage(directory);
        gitCommit(directory, "Add example.txt");
        gitPush(directory);
    }

    public static void gitInit(Path directory) throws Exception {
        runCommand(directory, "git", "init");
    }

    public static void gitBranch(Path directory) throws Exception {
        runCommand(directory, "git", "branch");
    }

    public static void gitDiff(Path directory) throws Exception {
        runCommand(directory, "git", "diff", "ca793d340414f65e142966d94896e6644db78d3d");
    }

    public static void gitStage(Path directory) throws Exception {
        runCommand(directory, "git", "add", "-A");
    }

    public static void gitCommit(Path directory, String message) throws Exception {
        runCommand(directory, "git", "commit", "-m", message);
    }

    public static void gitPush(Path directory) throws Exception {
        runCommand(directory, "git", "push");
    }

    public static void gitPull(Path directory) throws Exception {
        runCommand(directory, "git", "pull");
    }

    public static void gitLog(Path directory) throws Exception {
        runCommand(directory, "git", "log");
    }

    public static void gitLogInline(Path directory) throws Exception {
        runCommand(directory, "git", "log", "--oneline");
    }

    public static void gitClone(Path directory, String originUrl) throws Exception {
        runCommand(directory.getParent(), "git", "clone", originUrl, directory.getFileName().toString());
    }

    public static void runCommand(Path directory, String... command) throws Exception {
        Objects.requireNonNull(directory, "directory");
        if (!Files.exists(directory)) {
            throw new RuntimeException("can't run command in non-existing directory '" + directory + "'");
        }
        ProcessBuilder pb = new ProcessBuilder()
                .command(command)
                .directory(directory.toFile());
        Process p = pb.start();

        StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(), "ERROR");
        StreamGobbler outputGobbler = new StreamGobbler(p.getInputStream(), "OUTPUT");
        outputGobbler.start();
        errorGobbler.start();
        int exit = p.waitFor();
        errorGobbler.join();
        outputGobbler.join();
        if (exit != 0) {
            throw new AssertionError(String.format("runCommand returned %d", exit));
        }
    }

    private static class StreamGobbler extends Thread {

        private final InputStream is;
        private final String type;

        private StreamGobbler(InputStream is, String type) {
            this.is = is;
            this.type = type;
        }

        @Override
        public void run() {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is));) {
                String line;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<Diff> getJsonChangesfromGIT(Path directory, String command) throws Exception {
        Objects.requireNonNull(directory, "directory");
        if (!Files.exists(directory)) {
            throw new RuntimeException("can't run command in non-existing directory '" + directory + "'");
        }

        Process p = Runtime.getRuntime().exec(command);

        DiffCallable outputGobbler = new DiffCallable(p.getInputStream(), "OUTPUT");
        List<Diff> gitChanges = outputGobbler.call();

        int exit = p.waitFor();

        if (exit != 0) {
            throw new AssertionError(String.format("runCommand returned %d", exit));
        }
        return gitChanges;
    }
}