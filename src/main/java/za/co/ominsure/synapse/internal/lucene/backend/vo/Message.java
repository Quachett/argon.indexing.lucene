//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2022.06.01 at 10:29:54 PM CAT 
//


package za.co.ominsure.synapse.internal.lucene.backend.vo;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Message.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Message"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Index Created Successfully"/&gt;
 *     &lt;enumeration value="Failed To create index: "/&gt;
 *     &lt;enumeration value="Index Deleted Successfully"/&gt;
 *     &lt;enumeration value="Failed To delete index: "/&gt;
 *     &lt;enumeration value="Index Updated Successfully"/&gt;
 *     &lt;enumeration value="Failed To update index: "/&gt;
 *     &lt;enumeration value="Index Already Exists"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "Message")
@XmlEnum
public enum Message {

    @XmlEnumValue("Index Created Successfully")
    INDEX_CREATED_SUCCESSFULLY("Index Created Successfully"),
    @XmlEnumValue("Failed To create index: ")
    FAILED_TO_CREATE_INDEX("Failed To create index: "),
    @XmlEnumValue("Index Deleted Successfully")
    INDEX_DELETED_SUCCESSFULLY("Index Deleted Successfully"),
    @XmlEnumValue("Failed To delete index: ")
    FAILED_TO_DELETE_INDEX("Failed To delete index: "),
    @XmlEnumValue("Index Updated Successfully")
    INDEX_UPDATED_SUCCESSFULLY("Index Updated Successfully"),
    @XmlEnumValue("Failed To update index: ")
    FAILED_TO_UPDATE_INDEX("Failed To update index: "),
    @XmlEnumValue("Index Already Exists")
    INDEX_ALREADY_EXISTS("Index Already Exists");
    private final String value;

    Message(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Message fromValue(String v) {
        for (Message c: Message.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
