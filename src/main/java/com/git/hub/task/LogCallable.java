package com.git.hub.task;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;

public class LogCallable implements Callable {

    private final InputStream is;
    private final String type;

    public LogCallable(InputStream is, String type) {
        this.is = is;
        this.type = type;
    }

    @Override
    public String call() {
        String latestCommit = "get well";
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is));) {
            String temp = br.readLine();
            System.out.println("latest commit : " + temp);
            latestCommit = temp.split(" ")[0];
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }
        return latestCommit;
    }
}