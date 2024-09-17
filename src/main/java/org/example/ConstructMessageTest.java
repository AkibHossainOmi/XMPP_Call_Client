package org.example;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.StandardExtensionElement;

public class ConstructMessageTest {

    public static void main(String[] args) {
        // Call the method to construct the message
        Message message = ringingMessage("of4@telcohost/Conversations.qxbZ", "tx51W3rRMJjXy-Ial-4l5A");

        // Output the constructed message
        System.out.println("Constructed message: " + message.toXML());
    }

    // Method to construct the <ringing> message
    public static Message ringingMessage(String toJid, String id) {
        // Create the base message
        Message message = new Message();
        message.setType(Message.Type.chat);
        message.setTo(toJid);

        // Create the <ringing> element
        StandardExtensionElement ringingElement = StandardExtensionElement.builder("ringing", "urn:xmpp:jingle-message:0")
                .addAttribute("id", id)
                .build();

        // Add the <ringing> element to the message
        message.addExtension(ringingElement);

        // Create the <store> element
        StandardExtensionElement storeElement = StandardExtensionElement.builder("store", "urn:xmpp:hints")
                .build();

        // Add the <store> element to the message
        message.addExtension(storeElement);

        return message;
    }

    public static Message proposeMessage(String toJid, String id) {
        // Create the base message
        Message message = new Message();
        message.setType(Message.Type.chat);
        message.setTo(toJid);
        message.setStanzaId("jm-propose-" + id);

        // Create the <propose> element
        StandardExtensionElement.Builder proposeBuilder = StandardExtensionElement.builder("propose", "urn:xmpp:jingle-message:0")
                .addAttribute("id", id);

        // Create the <description> element and add it to <propose>
        StandardExtensionElement descriptionElement = StandardExtensionElement.builder("description", "urn:xmpp:jingle:apps:rtp:1")
                .addAttribute("media", "audio")
                .build();

        // Add the <description> element to the <propose> element using addElement()
        proposeBuilder.addElement(descriptionElement);

        // Build the <propose> element
        StandardExtensionElement proposeElement = proposeBuilder.build();

        // Add the <propose> element to the message
        message.addExtension(proposeElement);

        // Create and add the <request> element
        StandardExtensionElement requestElement = StandardExtensionElement.builder("request", "urn:xmpp:receipts")
                .build();
        message.addExtension(requestElement);

        // Create and add the <store> element
        StandardExtensionElement storeElement = StandardExtensionElement.builder("store", "urn:xmpp:hints")
                .build();
        message.addExtension(storeElement);

        return message;
    }
}

