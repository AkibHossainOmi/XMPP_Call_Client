package org.example;

import java.util.ArrayList;
import java.util.List;

public class Description {
    private String media;
    private List<PayloadType> payloadTypes = new ArrayList<>();

    // Getters and Setters
    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public List<PayloadType> getPayloadTypes() {
        return payloadTypes;
    }

    public void addPayloadType(PayloadType payloadType) {
        this.payloadTypes.add(payloadType);
    }

    @Override
    public String toString() {
        return "Description{" +
                "media='" + media + '\'' +
                ", payloadTypes=" + payloadTypes +
                '}';
    }
}
