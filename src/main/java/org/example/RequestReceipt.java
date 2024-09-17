package org.example;

import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.util.XmlStringBuilder;

public class RequestReceipt implements ExtensionElement {

    @Override
    public String getNamespace() {
        return "urn:xmpp:receipts";
    }

    @Override
    public String getElementName() {
        return "request";
    }

    public CharSequence toXML(String enclosingNamespace) {
        return new XmlStringBuilder()
                .halfOpenElement(getElementName())
                .xmlnsAttribute(getNamespace())
                .closeEmptyElement();
    }

    public CharSequence toXML() {
        return toXML(null);
    }
}
