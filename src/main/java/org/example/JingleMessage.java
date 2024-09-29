package org.example;

import java.util.ArrayList;
import java.util.List;

public class JingleMessage {
    private String sid;
    private String action;
    private List<Content> contents = new ArrayList<>();
    private Group group;

    // Getters and Setters
    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public List<Content> getContents() {
        return contents;
    }

    public void addContent(Content content) {
        this.contents.add(content);
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "JingleMessage{" +
                "sid='" + sid + '\'' +
                ", action='" + action + '\'' +
                ", contents=" + contents +
                ", group=" + group +
                '}';
    }
}
