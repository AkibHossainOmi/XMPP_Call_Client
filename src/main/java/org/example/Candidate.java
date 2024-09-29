package org.example;

public class Candidate {
    private String id;
    private String foundation;
    private String ip;
    private int port;
    private String protocol;
    private String type;
    private int priority;
    private String component;
    private int generation;

    // Getter and setter methods
    public String getIp() { return ip; }
    public void setIp(String ip) { this.ip = ip; }

    public int getPort() { return port; }
    public void setPort(int port) { this.port = port; }

    public String getProtocol() { return protocol; }
    public void setProtocol(String protocol) { this.protocol = protocol; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }

    public String getComponent() { return component; }
    public void setComponent(String component) { this.component = component; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFoundation() { return foundation; }
    public void setFoundation(String foundation) { this.foundation = foundation; }

    public int getGeneration() { return generation; }
    public void setGeneration(int generation) { this.generation = generation; }
}
