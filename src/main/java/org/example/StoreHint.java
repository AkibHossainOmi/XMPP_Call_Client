package org.example;

import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.util.XmlStringBuilder;

public class StoreHint implements ExtensionElement {

    @Override
    public String getNamespace() {
        return "urn:xmpp:hints";
    }

    @Override
    public String getElementName() {
        return "store";
    }

    public CharSequence toXML(String enclosingNamespace) {
        return new XmlStringBuilder()
                .halfOpenElement(getElementName())
                .xmlnsAttribute(getNamespace())
                .closeEmptyElement();
    }

    @Override
    public CharSequence toXML() {
        return toXML(null);
    }
}
