package com.git.hub;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class JGITExamples {

    public static void main(String[] args) throws Exception {


        Path path = Paths.get("/Users/shaid/Documents/MyWorld");
        Git.gitLogInline(path);
        //Git.gitDiff(path);
        //extracted(path);
    }

    private static void extracted() throws Exception {
        Path path = Paths.get("/Users/shaid/Documents/MyWorld");
        /* Sync code from remote to local*/
        Git.gitPull(path);
        /* Get Json changes from git */
        String diffCMD = "git -C /Users/shaid/Documents/MyWorld/ diff b78efecf484057961a152c8cece18b598fe7c7dd";
        List<Diff> gitChanges = Git.getJsonChangesfromGIT(path, diffCMD);
        /* Get DB scripts corresponding to json changes */
        String dbScriptFile = "dbscript";
        List<String> dbScripts = new DBScript().compare(gitChanges, dbScriptFile);
        System.out.println("db scripts. to be run..");
        dbScripts.forEach(System.out::println);
    }
}