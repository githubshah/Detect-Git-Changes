package com.git.hub;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

class DBScript {

    private List<Diff> diffs;
    private Map<String, String> valueKey;
    private List<String> values;
    private String fileName;

    public List<String> compare(List<Diff> collect, String fileName1) {
        this.diffs = collect;
        this.values = diffs.stream().map(Diff::getValue).filter(x -> !x.isEmpty()).collect(Collectors.toList());
        this.valueKey = diffs.stream().collect(Collectors.toMap(Diff::getValue, Diff::getKey, (address1, address2) -> {
            System.out.println("duplicate key found! > " + address2);
            return address2;
        }));
        this.fileName = fileName1;
        return getDBScripts(getFileAsIOStream());
    }

    private InputStream getFileAsIOStream() {
        InputStream ioStream = DBScript.class
                .getClassLoader()
                .getResourceAsStream(fileName);

        if (ioStream == null) {
            throw new IllegalArgumentException(fileName + " is not found");
        }
        return ioStream;
    }

    // print input stream
    private List<String> getDBScripts(InputStream is) {
        List<String> rows = new ArrayList<>();
        List<String> dbScripts = new ArrayList<>();
        try (InputStreamReader streamReader =
                     new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {

            String line;

            while ((line = reader.readLine()) != null) {
                rows.add(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        values.forEach(value -> {
            AtomicInteger inc = new AtomicInteger(0);
            List<String> list = new ArrayList<>();
            rows.forEach(row -> {
                if (row.contains(value)) {
                    inc.getAndIncrement();
                    list.add(row);
                }
            });
            if (inc.get() == 1) {
                dbScripts.add(list.get(0));
            } else {
                list.forEach(row -> {
                    valueKey.forEach((v, k) -> {
                        if (row.contains(k)) {
                            dbScripts.add(row);
                        }
                    });
                });
            }
        });

        return dbScripts;
    }

    // print a file
    public void printFile(File file) {

        List<String> lines;
        try {
            lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
            lines.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void print(InputStream is) {
        try (InputStreamReader streamReader =
                     new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {

            String line;

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}