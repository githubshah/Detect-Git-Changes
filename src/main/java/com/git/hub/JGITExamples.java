package com.git.hub;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JGITExamples {

    public static void main(String[] args) throws IOException, InterruptedException {


        Path path = Paths.get("/Users/shaid/Documents/MyWorld");
        // git diff d0dfaad9ee4d44dbf085b3db24c034ac75d75e30 | grep '^[+|-][^+|-]'
        //Git.runCommand(path, "git","diff","d0dfaad9ee4d44dbf085b3db24c034ac75d75e30", "| grep \'^[+|-][^+|-]\'");
        Git.runCommand(path, "git","diff","d3b2ca47f10eb97116c0ca02247dd885f72ae071", "| grep \'^[+|-][^+|-]\'");

//        Process exec = Runtime.getRuntime().exec("git branch");
//        InputStream is = exec.getInputStream();
//        try (BufferedReader br = new BufferedReader(new InputStreamReader(is));) {
//            String line;
//            while ((line = br.readLine()) != null) {
//                //System.out.println(type + ">> " + line);
//                System.out.println(line);
//            }
//        } catch (IOException ioe) {
//            ioe.printStackTrace();
//        }


    }
}