package com.git.hub;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class JGITExamples {

    public static void main(String[] args) throws Exception {

        /* Get Json changes from git */
        Path path = Paths.get("/Users/shaid/Documents/MyWorld");
        String cmd = "git -C /Users/shaid/Documents/MyWorld/ diff d3b2ca47f10eb97116c0ca02247dd885f72ae071";
        List<Diff> gitChanges = Git.getGitChanges(path, cmd);

        /* Get DB scripts corresponding to json changes */
        DBScript dbScript = new DBScript(gitChanges);
        String fileName = "dbscript";
        List<String> dbScripts = dbScript.getDBScripts(dbScript.getFileAsIOStream(fileName));
        System.out.println("db scripts. to be run.." + dbScripts);

    }
}