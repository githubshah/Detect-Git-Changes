package com.git.hub;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

public class JGITExamples {

    public static void main(String[] args) throws Exception {


        Path path = Paths.get("/Users/shaid/Documents/MyWorld");

        /* Sync code from remote to local*/
        //Git.gitPull(path);

        //CommitHistory savedCommit = new CommitToFile().getSavedCommit();

        CommitHistory savedCommit = new CommitHistory("ed6ffd3876ab90a265f963c016cae11a34b7d25b", new Date(), "shaid");
        extracted(path, savedCommit);

//        String diffCMD = "git -C /Users/shaid/Documents/MyWorld/ log --oneline";
//        String logCallable = Git.getLogCallable(path, diffCMD);
//        System.out.println("Remote commit: " + logCallable);
//
//        CommitHistory shaid = new CommitHistory(logCallable, new Date(), "shaid");
//        new CommitToFile().updateCommit(shaid);
    }

    private static void extracted(Path path, CommitHistory commitHistory) throws Exception {
        /* Get Json changes from git */
        System.out.println("Saved Commit Id: " + commitHistory);
        String diffCMD = "";
        if (commitHistory == null) {
            diffCMD = "git -C /Users/shaid/Documents/MyWorld/ diff";
        } else {
            diffCMD = "git -C /Users/shaid/Documents/MyWorld/ diff " + commitHistory.commitId;
        }
        System.out.println("cmd to be run :" + diffCMD);
        List<Diff> gitChanges = Git.getJsonChangesfromGIT(path, diffCMD);
        /* Get DB scripts corresponding to json changes */
        String dbScriptFile = "dbscript";
        List<String> dbScripts = new DBScript().compare(gitChanges, dbScriptFile);
        System.out.println("db scripts. to be run..");
        dbScripts.forEach(System.out::println);
    }
}