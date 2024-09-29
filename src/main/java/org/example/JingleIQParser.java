package org.example;

import org.w3c.dom.*;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import java.io.StringReader;
import java.util.Iterator;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.xml.sax.InputSource;

public class JingleIQParser {

    public static JingleMessage  parseJingleIQ(String xmlContent) throws Exception {
        JingleMessage jingleMessage = new JingleMessage();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(xmlContent)));

        Element jingleElement = doc.getDocumentElement();
        jingleMessage.setSid(jingleElement.getAttribute("sid"));
        jingleMessage.setAction(jingleElement.getAttribute("action"));

        NodeList contentList = jingleElement.getElementsByTagName("content");

        for (int i = 0; i < contentList.getLength(); i++) {
            Element contentElement = (Element) contentList.item(i);
            Content content = new Content();
            content.setName(contentElement.getAttribute("name"));
            content.setCreator(contentElement.getAttribute("creator"));

            // Parsing description element
            NodeList descriptionList = contentElement.getElementsByTagNameNS("urn:xmpp:jingle:apps:rtp:1", "description");
            if (descriptionList.getLength() > 0) {
                Element descriptionElement = (Element) descriptionList.item(0);
                Description description = new Description();
                description.setMedia(descriptionElement.getAttribute("media"));

                // Parsing payload types
                NodeList payloadTypeList = descriptionElement.getElementsByTagName("payload-type");
                for (int j = 0; j < payloadTypeList.getLength(); j++) {
                    Element payloadElement = (Element) payloadTypeList.item(j);
                    PayloadType payloadType = new PayloadType();
                    payloadType.setName(payloadElement.getAttribute("name"));
                    payloadType.setClockrate(payloadElement.getAttribute("clockrate"));
                    payloadType.setId(payloadElement.getAttribute("id"));

                    // Parsing parameters within the payload-type
                    NodeList parameterList = payloadElement.getElementsByTagName("parameter");
                    for (int k = 0; k < parameterList.getLength(); k++) {
                        Element parameterElement = (Element) parameterList.item(k);
                        Parameter parameter = new Parameter(
                                parameterElement.getAttribute("name"),
                                parameterElement.getAttribute("value")
                        );
                        payloadType.addParameter(parameter);
                    }

                    description.addPayloadType(payloadType);
                }
                content.setDescription(description);
            }

            // Parsing transport element
            NodeList transportList = contentElement.getElementsByTagNameNS("urn:xmpp:jingle:transports:ice-udp:1", "transport");
            if (transportList.getLength() > 0) {
                Element transportElement = (Element) transportList.item(0);
                Transport transport = new Transport();
                transport.setUfrag(transportElement.getAttribute("ufrag"));
                transport.setPwd(transportElement.getAttribute("pwd"));

                NodeList fingerprintList = transportElement.getElementsByTagNameNS("urn:xmpp:jingle:apps:dtls:0", "fingerprint");
                if (fingerprintList.getLength() > 0) {
                    Element fingerprintElement = (Element) fingerprintList.item(0);
                    transport.setFingerprint(fingerprintElement.getTextContent());
                }

                content.setTransport(transport);
            }

            jingleMessage.addContent(content);
        }

        // Parsing group element
        NodeList groupList = jingleElement.getElementsByTagNameNS("urn:xmpp:jingle:apps:grouping:0", "group");
        if (groupList.getLength() > 0) {
            Element groupElement = (Element) groupList.item(0);
            Group group = new Group();
            group.setSemantics(groupElement.getAttribute("semantics"));
            jingleMessage.setGroup(group);
        }

        return jingleMessage;
    }

    private static Transport getTransport(Element transportElement) {
        Transport transport = new Transport();
        transport.setUfrag(transportElement.getAttribute("ufrag"));
        transport.setPwd(transportElement.getAttribute("pwd"));

        // Parse ICE candidates
        NodeList candidateNodes = transportElement.getElementsByTagNameNS("urn:xmpp:jingle:transports:ice-udp:1", "candidate");
        for (int j = 0; j < candidateNodes.getLength(); j++) {
            Element candidateElement = (Element) candidateNodes.item(j);
            Candidate candidate = new Candidate();
            candidate.setId(candidateElement.getAttribute("id"));
            candidate.setFoundation(candidateElement.getAttribute("foundation"));
            candidate.setIp(candidateElement.getAttribute("ip"));
            candidate.setPort(Integer.parseInt(candidateElement.getAttribute("port")));
            candidate.setProtocol(candidateElement.getAttribute("protocol"));
            candidate.setType(candidateElement.getAttribute("type"));
            candidate.setPriority(Integer.parseInt(candidateElement.getAttribute("priority")));
            candidate.setComponent(candidateElement.getAttribute("component"));
            candidate.setGeneration(Integer.parseInt(candidateElement.getAttribute("generation")));

            transport.addCandidate(candidate);
        }
        return transport;
    }
}