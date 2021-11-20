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

    List<Diff> diffs;
    Map<String, String> valueKey;
    List<String> values;

    public DBScript(List<Diff> collect) {
        diffs = collect;

        valueKey = diffs.stream().collect(Collectors.toMap(Diff::getValue, Diff::getKey));
        values = diffs.stream().map(x -> x.value).collect(Collectors.toList());
        values.remove("");

    }

    public InputStream getFileAsIOStream(final String fileName) {
        InputStream ioStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream(fileName);

        if (ioStream == null) {
            throw new IllegalArgumentException(fileName + " is not found");
        }
        return ioStream;
    }

    // print input stream
    public List<String> getDBScripts(InputStream is) {
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
}

public class ReadFromFile {

    public static void main(String[] args) throws IOException {

        String fileName = "dbscript";

        DBScript apple = new DBScript(new ArrayList<>());
        InputStream in = apple.getFileAsIOStream(fileName);
        apple.getDBScripts(in);
    }


}