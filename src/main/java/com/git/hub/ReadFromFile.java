package com.git.hub;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

class Apple {

    List<Diff> diffs;
    Map<String, String> valueKey;
    List<String> values;

    public Apple(List<Diff> collect) {
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
    public void printInputStream(InputStream is) {
        List<String> rows = new ArrayList<>();
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
                System.out.println(list);
            } else {
                list.forEach(row -> {
                    valueKey.forEach((v, k) -> {
                        if (row.contains(k)) {
                            System.out.println(row);
                        }
                    });
                });
            }
        });


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

        Apple apple = new Apple(new ArrayList<>());
        InputStream in = apple.getFileAsIOStream(fileName);
        apple.printInputStream(in);
    }


}