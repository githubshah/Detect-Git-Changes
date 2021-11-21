package com.git.hub.task;

import com.git.hub.Diff;
import com.git.hub.EVENT;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class DiffCallable implements Callable {

    private final InputStream is;
    private final String type;

    public DiffCallable(InputStream is, String type) {
        this.is = is;
        this.type = type;
    }

    @Override
    public List<Diff> call() {
        List<Diff> gitDiff = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is));) {
            String line;
            String parent = "";
            List<Diff> diff = new ArrayList<>();
            while ((line = br.readLine()) != null) {

                if (line.contains(":{")) {
                    parent = line.substring(1).trim();
                }
                if (line.startsWith("-\t") || line.startsWith("- ") || line.startsWith("+\t") || line.startsWith("+ ")) {

                    if (!line.contains(":") && (line.contains("{") || line.contains("}") || line.contains("[") || line.contains("]")))
                        continue;

                    EVENT event;
                    if (line.startsWith("- ") || line.startsWith("-\t")) {
                        event = EVENT.DELETED;
                    } else {
                        event = EVENT.ADD;
                    }
                    String parent1 = parent.trim().replace(":{", "").replace('"', ' ').trim();
                    String key;
                    String value;
                    if (line.endsWith("},") || line.endsWith("}")) {
                        key = "NAN";
                        value = line.substring(1).trim();
                        value = value.substring(0, value.length() - 1);
                        if (value.endsWith(",")) value = value.substring(0, value.length() - 1);
                        diff.add(new Diff(event, parent1, line, key, value.trim()));
                    } else {
                        String[] split = line.split(":");
                        key = split[0].replaceAll("\"", "").replaceAll(" ", "").substring(1).trim();
                        if (split.length == 1) {
                            continue;
                        }

                        value = split[1].replace("\"", ""); // remove starting white space
                        if (value.endsWith(",")) value = value.substring(0, value.length() - 1).trim();
                        value = value.trim();
                        if (value.length() == 1 && (value.contains("[") || value.contains("]") || value.contains("{") || value.contains("}")))
                            continue;
                        diff.add(new Diff(event, parent1, line, key, value.trim()));
                    }

                }
            }

            gitDiff = diff.stream().filter(x -> x.getEvent().equals(EVENT.ADD)).collect(Collectors.toList());

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return gitDiff;
    }
}