package org.example;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.*;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;

import static org.example.JingleMessageHandler.handleJigleIqMessage;

public class Main {

    public static void main(String[] args) throws XmppStringprepException {
        // Build the XMPP configuration
//        XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
//                .setUsernameAndPassword("ps101", "ps123")  // Update credentials as needed
//                .setXmppDomain("localhost")
//                .setHost("192.168.0.31")  // Replace with your XMPP server IP
//                .setPort(5222)  // Default XMPP port
//                .setSecurityMode(XMPPTCPConnectionConfiguration.SecurityMode.disabled)  // Disable SSL for local test
//                .build();
        XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                .setUsernameAndPassword("of11", "of11@tb123")  // Update credentials as needed
                .setXmppDomain("conversations.im")
                .build();

        // Create a connection
        AbstractXMPPConnection connection = new XMPPTCPConnection(config);
        try {
            // Connect and log in
            connection.connect();
            System.out.println("Connected to XMPP server.");
            connection.login();
            System.out.println("Logged in as " + connection.getUser());

            // Set presence to available
            Presence presence = new Presence(Presence.Type.available);
            presence.setMode(Presence.Mode.available);
            presence.setStatus("Ready for calls");
            connection.sendStanza(presence);


            // Set up a listener to handle incoming messages
            connection.addAsyncStanzaListener(stanza -> {
                if (stanza instanceof Message) {
                    Message receivedMessage = (Message) stanza;
                    Message message = (Message) stanza;
                    System.out.println("Received message: " + receivedMessage.toXML());
                    StandardExtensionElement proposeElement = message.getExtension("propose", "urn:xmpp:jingle-message:0");

                    String element = determineElement(receivedMessage);
                    switch (element){
                        case "propose":
                            onRinging(connection, receivedMessage);
                            break;
                        case "presence":

                            System.out.println("got Presence");
                            break;
                        case "ringing":
                            onRinging(connection, receivedMessage);
                            break;
                        case "accept":
                            onAccept(connection, receivedMessage);
                            break;
                        default:
                            System.out.println(element);
                    }
                }
            }, stanza -> stanza instanceof Message);


            connection.addAsyncStanzaListener(stanza -> {
                if (stanza instanceof IQ) {
                    IQ receivedMessage = (IQ) stanza;
                    System.out.println("Received message: " + receivedMessage.toXML());
                    try {
                     handleJigleIqMessage(receivedMessage);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }, stanza -> stanza instanceof IQ);

            // Keep the connection alive
            Thread.sleep(Long.MAX_VALUE);

        } catch (SmackException | IOException | InterruptedException | XMPPException e) {
            e.printStackTrace();
        } finally {
            if (connection.isConnected()) {
                connection.disconnect();
                System.out.println("Disconnected from the XMPP server.");
            }
        }
    }



    private static Message getActiveMessage()
    {
        Message message = new Message();
//        message.setTo("of11@conversations.im");
//        message.setFrom("of10@conversations.im/Conversations.KmcCTM9UrV");
        message.setType(Message.Type.chat);
        message.setLanguage("en");

        // Add the <active> element
        StandardExtensionElement activeElement = StandardExtensionElement.builder("active", "http://jabber.org/protocol/chatstates")
                .build();
        message.addExtension(activeElement);

        // Add the <no-store> element
        StandardExtensionElement noStoreElement = StandardExtensionElement.builder("no-store", "urn:xmpp:hints")
                .addAttribute("xmlns:stream", "http://etherx.jabber.org/streams")
                .build();
        message.addExtension(noStoreElement);

        // Add the <no-storage> element
        StandardExtensionElement noStorageElement = StandardExtensionElement.builder("no-storage", "urn:xmpp:hints")
                .addAttribute("xmlns:stream", "http://etherx.jabber.org/streams")
                .build();
        message.addExtension(noStorageElement);

        // Print the message XML to verify
//        System.out.println(message.toXML());
        return message;
    }

    // Updated onPropose method to accept connection parameter
    private static void onPropose(AbstractXMPPConnection connection, Message message) {
        // Extract the <propose> element from the received message
        StandardExtensionElement proposeElement = message.getExtension("propose", "urn:xmpp:jingle-message:0");
        String proposeId = proposeElement.getAttributeValue("id");
        System.out.println("<propose> element is present with ID: " + proposeId);

        // Dynamically get the 'from' and 'to' values from the received message
        String fromJid = message.getFrom().asBareJid().toString();  // 'from' JID
        String toJid = message.getTo().asBareJid().toString();      // 'to' JID

        // Create the response message dynamically
        Message responseMessage = new Message();
        responseMessage.setType(Message.Type.chat);
        responseMessage.setFrom(fromJid);  // Use dynamic 'from' JID
        responseMessage.setTo(toJid);      // Use dynamic 'to' JID

        // Construct the XML string for the response dynamically
        String xmlResponse =
                "<message type=\"chat\" id=\"jm-propose-" + proposeId + "\" from=\"" + fromJid + "\" to=\"" + toJid + "\">\n" +
                        "  <propose xmlns=\"urn:xmpp:jingle-message:0\" id=\"" + proposeId + "\">\n" +
                        "    <description xmlns=\"urn:xmpp:jingle:apps:rtp:1\" media=\"audio\" />\n" +
                        "  </propose>\n" +
                        "  <request xmlns=\"urn:xmpp:receipts\" />\n" +
                        "  <store xmlns=\"urn:xmpp:hints\" />\n" +
                        "</message>\n";

        // Set the dynamically constructed XML as the body of the response message
        responseMessage.setBody(xmlResponse);

        // Send the response message
        try {
            connection.sendStanza(responseMessage);
            System.out.println("Dynamic response sent");
        } catch (SmackException.NotConnectedException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    private static void onRinging(AbstractXMPPConnection connection, Message message) {
        // Extract the <ringing> element from the received message
        StandardExtensionElement proposeElement = message.getExtension("propose", "urn:xmpp:jingle-message:0");
        if (proposeElement != null) {
            String ringingId = proposeElement.getAttributeValue("id");
            System.out.println("<ringing> element is present with ID: " + ringingId);

            // Dynamically get the 'from' and 'to' values from the received message
            String toJid = message.getFrom().toString();  // 'from' JID
            String fromJid = message.getTo().toString();      // 'to' JID

            // Construct another message with the <ringing> info


            // Create a new message for the second response and set the body
            Message secondResponseMessage = getRingingResponseMessage(ringingId, toJid);
            Message thirdResponseMessageeMessage = getThirdResponseMessageeMessage(ringingId, toJid);
            Message procedResponseMessageeMessage = new Message();
            procedResponseMessageeMessage.setType(Message.Type.chat); // Setting the message type to 'chat'
            procedResponseMessageeMessage.setTo(toJid); // Set the 'to' attribute
            procedResponseMessageeMessage.setFrom(fromJid); // Set your 'from' attribute if necessary
            procedResponseMessageeMessage.setStanzaId(ringingId); // Set the custom 'id' attribute for the message
            StandardExtensionElement device = StandardExtensionElement.builder("device", "http://gultsch.de/xmpp/drafts/omemo/dlts-srtp-verification")
                    .addAttribute("id", "1439082960")
                    .build();
            // Create the <proceed> element
            StandardExtensionElement proceed = StandardExtensionElement.builder("proceed", "urn:xmpp:jingle-message:0")
                    .addAttribute("id", ringingId)
                    .addElement(device)
                    .build();

            // Create the nested <device> element


            // Nest the <device> element inside the <proceed> element
//            proceed.addElement(device);

            // Add the <proceed> element to the message
            procedResponseMessageeMessage.addExtension(proceed);

            // Add the <store> element
            StandardExtensionElement store = StandardExtensionElement.builder("store", "urn:xmpp:hints")
                    .build();
            procedResponseMessageeMessage.addExtension(store);

            try {
                connection.sendStanza(secondResponseMessage);
                connection.sendStanza(thirdResponseMessageeMessage);
                connection.sendStanza(procedResponseMessageeMessage);
                System.out.println("Dynamic ringing message sent from: " + fromJid + " to: " + toJid);
            } catch (SmackException.NotConnectedException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("<ringing> element not found in the received message.");
        }
    }

    private static Message getThirdResponseMessageeMessage(String ringingId, String toJid) {
        Message thirdResponseMessageeMessage = new Message();
        thirdResponseMessageeMessage.setType(Message.Type.chat);
        StandardExtensionElement ringingElement = StandardExtensionElement.builder("active", "urn:xmpp:jingle-message:0")
                .addAttribute("id", ringingId)
                .build();
        thirdResponseMessageeMessage.addExtension(ringingElement);
        thirdResponseMessageeMessage.setTo(toJid);
        return thirdResponseMessageeMessage;
    }

    private static Message getRingingResponseMessage(String ringingId, String toJid) {
        Message secondResponseMessage = new Message();
        secondResponseMessage.setType(Message.Type.chat);
        StandardExtensionElement ringingElement = StandardExtensionElement.builder("ringing", "urn:xmpp:jingle-message:0")
                .addAttribute("id", ringingId)
                .build();
        secondResponseMessage.addExtension(ringingElement);
        secondResponseMessage.setTo(toJid);
        return secondResponseMessage;
    }

    private static void onAccept(AbstractXMPPConnection connection, Message message) {
        // Extract the <accept> element from the received message
        StandardExtensionElement acceptElement = message.getExtension("accept", "urn:xmpp:jingle-message:0");
        String acceptId = acceptElement.getAttributeValue("id");
        System.out.println("<accept> element is present with ID: " + acceptId);

        // Dynamically get the 'from' and 'to' values from the received message
        String fromJid = message.getFrom().asBareJid().toString();  // 'from' JID
        String toJid = message.getTo().asBareJid().toString();      // 'to' JID

        // Construct and send Message 1
        String xmlMessage1 =
                "<message type=\"chat\" from=\"of5@telcohost\" to=\"of5@telcohost/xabber-android-E8suzyJd\">\n" +
                        "  <sent xmlns=\"urn:xmpp:carbons:2\">\n" +
                        "    <forwarded xmlns=\"urn:xmpp:forward:0\">\n" +
                        "      <message type=\"chat\" from=\"of5@telcohost/Conversations.w6p2\" xmlns=\"jabber:client\">\n" +
                        "        <accept xmlns=\"urn:xmpp:jingle-message:0\" id=\"" + acceptId + "\" />\n" +
                        "        <store xmlns=\"urn:xmpp:hints\" />\n" +
                        "      </message>\n" +
                        "    </forwarded>\n" +
                        "  </sent>\n" +
                        "</message>";
        Message message1 = new Message();
        message1.setType(Message.Type.chat);
        message1.setFrom("of5@telcohost");
        message1.setTo("of5@telcohost/xabber-android-E8suzyJd");
        message1.setBody(xmlMessage1);
        try {
            connection.sendStanza(message1);
            System.out.println("Sent message 1");
        } catch (SmackException.NotConnectedException | InterruptedException e) {
            e.printStackTrace();
        }

        // Construct and send Message 2
        String xmlMessage2 =
                "<message to=\"of5@telcohost\" from=\"of5@telcohost/Conversations.w6p2\" type=\"chat\">\n" +
                        "  <accept xmlns=\"urn:xmpp:jingle-message:0\" id=\"" + acceptId + "\" />\n" +
                        "  <store xmlns=\"urn:xmpp:hints\" />\n" +
                        "</message>";
        Message message2 = new Message();
        message2.setType(Message.Type.chat);
        message2.setFrom("of5@telcohost/Conversations.w6p2");
        message2.setTo("of5@telcohost");
        message2.setBody(xmlMessage2);
        try {
            connection.sendStanza(message2);
            System.out.println("Sent message 2");
        } catch (SmackException.NotConnectedException | InterruptedException e) {
            e.printStackTrace();
        }

        // Construct and send Message 3
        String xmlMessage3 =
                "<message to=\"of5@telcohost\" from=\"of5@telcohost/Conversations.w6p2\" type=\"chat\">\n" +
                        "  <accept xmlns=\"urn:xmpp:jingle-message:0\" id=\"" + acceptId + "\" />\n" +
                        "  <store xmlns=\"urn:xmpp:hints\" />\n" +
                        "</message>";
        Message message3 = new Message();
        message3.setType(Message.Type.chat);
        message3.setFrom("of5@telcohost/Conversations.w6p2");
        message3.setTo("of5@telcohost");
        message3.setBody(xmlMessage3);
        try {
            connection.sendStanza(message3);
            System.out.println("Sent message 3");
        } catch (SmackException.NotConnectedException | InterruptedException e) {
            e.printStackTrace();
        }
    }




    private static String determineElement(Message message) {
        if (message.getExtension("propose", "urn:xmpp:jingle-message:0") != null) {
            return "propose";
        } else if (message.getExtension("ringing", "urn:xmpp:jingle-message:0") != null) {
            return "ringing";
        } else if (message.getExtension("accept", "urn:xmpp:jingle-message:0") != null) {
            return "accept";
        }
        else if (message.getExtension("chat", "urn:xmpp:jingle-message:0") != null) {
            return "accept";
        }
        else if (message.getExtension("presence", "urn:xmpp:jingle-message:0") != null) {
            return "presence";
        }else {
            return "unknown";
        }
    }


}
