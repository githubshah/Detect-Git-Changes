package com.git.hub;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

public class JGITExamples {

    public static void main(String[] args) throws Exception {


        Path path = Paths.get("/Users/shaid/Documents/MyWorld");

        String diffCMD = "git -C /Users/shaid/Documents/MyWorld/ log --oneline";
        String logCallable = Git.getLogCallable(path, diffCMD);
        System.out.println("Remote commit: " + logCallable);

        CommitHistory shaid = new CommitHistory(logCallable, new Date(), "shaid");
        new CommitToFile().updateCommit(shaid);

        CommitHistory savedCommit = new CommitToFile().getSavedCommit();
        System.out.println("Saved Commit : " + savedCommit.commitId);

        String cId = savedCommit.commitId;

        //Git.gitDiff(path);
        extracted(path, cId);
    }

    private static void extracted(Path path, String cId) throws Exception {
        /* Sync code from remote to local*/
        Git.gitPull(path);
        /* Get Json changes from git */
        String diffCMD = "git -C /Users/shaid/Documents/MyWorld/ diff " + cId;
        List<Diff> gitChanges = Git.getJsonChangesfromGIT(path, diffCMD);
        /* Get DB scripts corresponding to json changes */
        String dbScriptFile = "dbscript";
        List<String> dbScripts = new DBScript().compare(gitChanges, dbScriptFile);
        System.out.println("db scripts. to be run..");
        dbScripts.forEach(System.out::println);
    }
}