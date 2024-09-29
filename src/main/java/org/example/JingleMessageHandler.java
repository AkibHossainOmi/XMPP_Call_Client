package org.example;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.UnparsedIQ;

public class JingleMessageHandler {
    static String handleJigleIqMessage(IQ message) throws Exception {

        JingleMessage jingleMessage = JingleIQParser.parseJingleIQ(((UnparsedIQ) message).getContent().toString());


        String action = jingleMessage.getAction();

        if ("session-initiate".equals(action) || "session-accept".equals(action)) {
            // Handle SDP exchange
            String sdp = SDPBuilder.jingleToSdp(jingleMessage);
            System.out.println("Received SDP: ");
            System.out.println(sdp);
        } else if ("transport-info".equals(action)) {
            // Handle ICE candidates
            for (Content content : jingleMessage.getContents()) {
                Transport transport = content.getTransport();
                if (transport != null && transport.hasCandidates()) {
                    handleIceCandidates(transport);
                }
                System.out.println("get ICE");
            }
        } else {
            System.out.println("Unknown Jingle action: " + action);
        }
        return action;
    }

    public static void handleIceCandidates(Transport transport) {
        for (Candidate candidate : transport.getCandidates()) {
            System.out.println("Handling ICE Candidate:");
            System.out.println("Candidate IP: " + candidate.getIp());
            System.out.println("Candidate Port: " + candidate.getPort());
            System.out.println("Candidate Protocol: " + candidate.getProtocol());
            System.out.println("Candidate Type: " + candidate.getType());
            // You can now pass the ICE candidate to the ICE agent (e.g., using WebRTC libraries or native ICE handling)
        }
    }
}
