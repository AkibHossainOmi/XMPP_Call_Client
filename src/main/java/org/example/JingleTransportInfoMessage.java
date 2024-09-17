//package org.example;
//import org.jivesoftware.smack.packet.IQ;
//import org.jivesoftware.smack.packet.Message;
//import org.jivesoftware.smack.packet.Packet;
//import org.jivesoftware.smackx.packet.Jingle;
//import org.jivesoftware.smackx.packet.JingleContent;
//import org.jivesoftware.smackx.packet.JingleTransport;
//
//public class JingleTransportInfoMessage {
//
//    public static IQ createTransportInfoIQ(String id, String to, String from, String sid, String ufrag, String pwd, String foundation, int port, String candidateId, int generation, int priority, String ip, String protocol, String type, int component) {
//        // Create the Jingle transport
//        JingleTransport transport = new JingleTransport();
//        transport.setUfrag(ufrag);
//        transport.setPwd(pwd);
//
//        // Create the Jingle candidate
//        JingleTransport.JingleTransportCandidate candidate = new JingleTransport.JingleTransportCandidate(foundation, port, candidateId, generation, priority, ip, protocol, type, component);
//
//        // Create the Jingle content
//        JingleContent content = new JingleContent("0", JingleContent.Creator.initiator);
//        content.addTransport(transport); // Use appropriate method to add transport
//        content.addCandidate(candidate);  // Use appropriate method to add candidate
//
//        // Create the Jingle packet
//        Jingle jingle = new Jingle("transport-info", sid); // Use appropriate constructor
//        jingle.addContent(content);
//
//        // Create the IQ packet
//        return new IQ("iq", "urn:xmpp:jingle:1") {
//            @Override
//            protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
//                xml.rightAngleBracket();
//                xml.append(jingle.toXML());
//                return xml;
//            }
//        };
//    }
//}