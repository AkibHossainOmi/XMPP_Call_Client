package org.example;

import java.util.List;

public class Content {
    private String name;
    private String creator;
    private Description description;
    private Transport transport;

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public Transport getTransport() {
        return transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    @Override
    public String toString() {
        return "Content{" +
                "name='" + name + '\'' +
                ", creator='" + creator + '\'' +
                ", description=" + description +
                ", transport=" + transport +
                '}';
    }
}

