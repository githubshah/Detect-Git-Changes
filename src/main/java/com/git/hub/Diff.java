package com.git.hub;

enum EVENT {
    ADD, UPDATED, DELETED
}

public class Diff {

    EVENT event;
    String parent;
    String content;
    String key;
    String value;

    public Diff(EVENT event, String parent, String content, String key, String value) {
        this.event = event;
        this.parent = parent;
        this.content = content;
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return "\n{" +
                " event='" + event + '\'' + "," +
                " parent='" + parent + '\'' + "," +
                " key='" + key + '\'' + "," +
                " value='" + value + '\'' +
                '}' + "\n";
    }
}
