package com.git.hub;

import java.io.Serializable;
import java.util.Date;

public class CommitHistory implements Serializable {
    public CommitHistory() {
    }

    String commitId;
    Date date;
    String author;

    public CommitHistory(String commitId, Date date, String author) {
        this.commitId = commitId;
        this.date = date;
        this.author = author;
    }

    public String getCommitId() {
        return commitId;
    }

    public void setCommitId(String commitId) {
        this.commitId = commitId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "CommitHistory{" +
                "commitId='" + commitId + '\'' +
                ", date=" + date +
                ", author='" + author + '\'' +
                '}';
    }
}
