package org.example;

import java.util.Objects;

public class SDPBuilder {

    public static String jingleToSdp(JingleMessage jingleMessage) {
        StringBuilder sdp = new StringBuilder();

        // SDP Version
        sdp.append("v=0\r\n");

        // Origin line (o=): contains session ID
        String sessionId = jingleMessage.getSid();
        sdp.append("o=- ").append(sessionId).append(" 2 IN IP4 0.0.0.0\r\n");

        // Session Name (s=)
        sdp.append("s=Jingle Session\r\n");

        // Time description (t=)
        sdp.append("t=0 0\r\n");

        // Grouping (a=group:BUNDLE)
        if (jingleMessage.getGroup() != null) {
            sdp.append("a=group:BUNDLE ");
            for (Content content : jingleMessage.getContents()) {
                sdp.append(content.getName()).append(" ");
            }
            sdp.append("\r\n");
        }

        // Iterate over contents
        for (Content content : jingleMessage.getContents()) {
            Description description = content.getDescription();
            // Media description (m=)
            if (Objects.equals(description, null))
                continue;
            String mediaType = description.getMedia();

            sdp.append("m=").append(mediaType).append(" 9 UDP/TLS/RTP/SAVPF");

            // Add payload types
            for (PayloadType payloadType : description.getPayloadTypes()) {
                sdp.append(" ").append(payloadType.getId());
            }
            sdp.append("\r\n");

            // Connection information (c=)
            sdp.append("c=IN IP4 0.0.0.0\r\n");

            // RTP map for each payload type
            for (PayloadType payloadType : description.getPayloadTypes()) {
                sdp.append("a=rtpmap:").append(payloadType.getId()).append(" ")
                        .append(payloadType.getName()).append("/").append(payloadType.getClockrate()).append("\r\n");

                // Additional payload-specific parameters (a=fmtp)
                for (Parameter parameter : payloadType.getParameters()) {
                    sdp.append("a=fmtp:").append(payloadType.getId())
                            .append(" ").append(parameter.getName()).append("=").append(parameter.getValue()).append("\r\n");
                }
            }

            // RTCP-MUX if available
            if (description.getMedia().equals("audio") || description.getMedia().equals("video")) {
                sdp.append("a=rtcp-mux\r\n");
            }

            // ICE and DTLS fingerprints
            Transport transport = content.getTransport();
            sdp.append("a=ice-ufrag:").append(transport.getUfrag()).append("\r\n");
            sdp.append("a=ice-pwd:").append(transport.getPwd()).append("\r\n");

            sdp.append("a=fingerprint:sha-256 ").append(transport.getFingerprint()).append("\r\n");
            sdp.append("a=setup:actpass\r\n");
        }

        return sdp.toString();
    }
}

