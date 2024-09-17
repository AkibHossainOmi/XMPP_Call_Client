package org.example;

import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.util.XmlStringBuilder;

public class ProposeElement implements ExtensionElement {
    public static final String ELEMENT = "propose";
    public static final String NAMESPACE = "urn:xmpp:jingle-message:0";

    private final String id;
    private final String media;
    private final String type; // Add a type field

    public ProposeElement(String id, String media, String type) {
        this.id = id;
        this.media = media;
        this.type = type;
    }

    @Override
    public String getNamespace() {
        return NAMESPACE;
    }

    @Override
    public String getElementName() {
        return ELEMENT;
    }

    public String getId() {
        return id;
    }

    public String getMedia() {
        return media;
    }

    public String getType() {
        return type;
    }

    public CharSequence toXML(String enclosingNamespace) {
        XmlStringBuilder xml = new XmlStringBuilder();
        xml.halfOpenElement(getElementName())
                .xmlnsAttribute(getNamespace())
                .attribute("id", id)
                .attribute("type", type) // Add type attribute
                .rightAngleBracket();
        xml.halfOpenElement("description")
                .xmlnsAttribute("urn:xmpp:jingle:apps:rtp:1")
                .attribute("media", media)
                .closeEmptyElement();
        xml.closeElement(getElementName());
        return xml;
    }

    @Override
    public CharSequence toXML() {
        return toXML(null);
    }
}
