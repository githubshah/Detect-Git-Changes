package com.git.hub;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class JGITExamples {

    public static void main(String[] args) throws Exception {

        /* Get Json changes from git */
        Path path = Paths.get("/Users/shaid/Documents/MyWorld");
        String cmd = "git -C /Users/shaid/Documents/MyWorld/ diff b78efecf484057961a152c8cece18b598fe7c7dd";
        List<Diff> gitChanges = Git.getJsonChangesfromGIT(path, cmd);
        /* Get DB scripts corresponding to json changes */
        String dbScriptFile = "dbscript";
        List<String> dbScripts = DBScript.compare(gitChanges, dbScriptFile);
        System.out.println("db scripts. to be run..");
        dbScripts.forEach(System.out::println);

    }
}