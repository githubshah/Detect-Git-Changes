package com.git.hub;

import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class JGITExamples {

    public static void main(String[] args) throws Exception {

        Path path = Paths.get("/Users/shaid/Documents/MyWorld");
        String cmd = "git -C /Users/shaid/Documents/MyWorld/ diff d3b2ca47f10eb97116c0ca02247dd885f72ae071";

        List<Diff> gitChanges = Git.getGitChanges(path, cmd);

        Apple apple = new Apple(gitChanges);
        String fileName = "dbscript";

        InputStream in = apple.getFileAsIOStream(fileName);
        apple.printInputStream(in);

    }
}