package com.git.hub;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JGITExamples {

    public static void main(String[] args) throws IOException, InterruptedException {

        String a = "    shaid hussain  123  ";
        System.out.println(a.trim());



        Path path = Paths.get("/Users/shaid/Documents/MyWorld");
        // git diff d0dfaad9ee4d44dbf085b3db24c034ac75d75e30 | grep '^[+|-][^+|-]'
        //Git.runCommand(path, "git","diff","d0dfaad9ee4d44dbf085b3db24c034ac75d75e30", "| grep \'^[+|-][^+|-]\'");
        Git.runCommand(path, "git","diff","a4eea298ed483417868131079a8f522d10724f19", "| grep \'^[+|-][^+|-]\'");

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