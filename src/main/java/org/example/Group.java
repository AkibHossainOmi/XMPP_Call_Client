package org.example;

public class Group {
    private String semantics;

    // Getters and Setters
    public String getSemantics() {
        return semantics;
    }

    public void setSemantics(String semantics) {
        this.semantics = semantics;
    }

    @Override
    public String toString() {
        return "Group{" +
                "semantics='" + semantics + '\'' +
                '}';
    }
}

