package com.git.hub;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

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

        StreamCallable outputGobbler = new StreamCallable(p.getInputStream(), "OUTPUT");
        List<Diff> gitChanges = outputGobbler.call();

        int exit = p.waitFor();

        if (exit != 0) {
            throw new AssertionError(String.format("runCommand returned %d", exit));
        }
        return gitChanges;
    }

    private static class StreamCallable implements Callable {

        private final InputStream is;
        private final String type;

        private StreamCallable(InputStream is, String type) {
            this.is = is;
            this.type = type;
        }

        @Override
        public List<Diff> call() {
            List<Diff> gitDiff = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is));) {
                String line;
                String parent = "";
                List<Diff> diff = new ArrayList<>();
                while ((line = br.readLine()) != null) {

                    if (line.contains(":{")) {
                        parent = line.substring(1).trim();
                    }
                    if (line.startsWith("-\t") || line.startsWith("- ") || line.startsWith("+\t") || line.startsWith("+ ")) {

                        if (!line.contains(":") && (line.contains("{") || line.contains("}") || line.contains("[") || line.contains("]")))
                            continue;

                        EVENT event;
                        if (line.startsWith("- ") || line.startsWith("-\t")) {
                            event = EVENT.DELETED;
                        } else {
                            event = EVENT.ADD;
                        }
                        String parent1 = parent.trim().replace(":{", "").replace('"', ' ').trim();
                        String key;
                        String value;
                        if (line.endsWith("},") || line.endsWith("}")) {
                            key = "NAN";
                            value = line.substring(1).trim();
                            value = value.substring(0, value.length() - 1);
                            if (value.endsWith(",")) value = value.substring(0, value.length() - 1);
                            diff.add(new Diff(event, parent1, line, key, value.trim()));
                        } else {
                            String[] split = line.split(":");
                            key = split[0].replaceAll("\"", "").replaceAll(" ", "").substring(1).trim();
                            if (split.length == 1) {
                                continue;
                            }

                            value = split[1].replace("\"", ""); // remove starting white space
                            if (value.endsWith(",")) value = value.substring(0, value.length() - 1).trim();
                            value = value.trim();
                            if (value.length() == 1 && (value.contains("[") || value.contains("]") || value.contains("{") || value.contains("}")))
                                continue;
                            diff.add(new Diff(event, parent1, line, key, value.trim()));
                        }

                    }
                }

                gitDiff = diff.stream().filter(x -> x.getEvent().equals(EVENT.ADD)).collect(Collectors.toList());

            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            return gitDiff;
        }
    }

}