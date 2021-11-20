package com.git.hub;

public class Diff {

    private EVENT event;
    private String parent;
    private String content;
    private String key;
    private String value;

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

    public EVENT getEvent() {
        return event;
    }

    public void setEvent(EVENT event) {
        this.event = event;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
