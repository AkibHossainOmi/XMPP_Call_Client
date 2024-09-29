package org.example;

import java.util.ArrayList;
import java.util.List;

public class PayloadType {
    private String name;
    private String clockrate;
    private String id;
    private String channels;
    private List<Parameter> parameters = new ArrayList<>();

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClockrate() {
        return clockrate;
    }

    public void setClockrate(String clockrate) {
        this.clockrate = clockrate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void addParameter(Parameter parameter) {
        this.parameters.add(parameter);
    }


    public String getChannels() {
        return channels;
    }

    public void setChannels(String channels) {
        this.channels = channels;
    }
    @Override
    public String toString() {
        return "PayloadType{id='" + id + "', name='" + name + "', clockrate='" + clockrate + "', channels='" + channels + "'}";
    }
}
