//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference
// Implementation, v2.2.4
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2013.08.18 at 08:57:29 AM CEST
//

package org.woped.file.yawl.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 * Java class for ResourcingSetFactsType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="ResourcingSetFactsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="participant" type="{http://www.yawlfoundation.org/yawlschema}NameType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="role" type="{http://www.yawlfoundation.org/yawlschema}NameType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "ResourcingSetFactsType",
    propOrder = {"participant", "role"})
@XmlSeeAlso({
  ResourcingSecondaryFactsType.class,
  org.woped.file.yawl.model.ResourcingDistributionSetFactsType.InitialSet.class
})
public class ResourcingSetFactsType {

  protected List<String> participant;
  protected List<String> role;

  /**
   * Gets the value of the participant property.
   *
   * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the participant property.
   *
   * <p>For example, to add a new item, do as follows:
   *
   * <pre>
   *    getParticipant().add(newItem);
   * </pre>
   *
   * <p>Objects of the following type(s) are allowed in the list {@link String }
   */
  public List<String> getParticipant() {
    if (participant == null) {
      participant = new ArrayList<String>();
    }
    return this.participant;
  }

  /**
   * Gets the value of the role property.
   *
   * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the role property.
   *
   * <p>For example, to add a new item, do as follows:
   *
   * <pre>
   *    getRole().add(newItem);
   * </pre>
   *
   * <p>Objects of the following type(s) are allowed in the list {@link String }
   */
  public List<String> getRole() {
    if (role == null) {
      role = new ArrayList<String>();
    }
    return this.role;
  }
}
