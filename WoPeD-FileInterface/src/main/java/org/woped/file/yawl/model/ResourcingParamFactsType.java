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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Java class for ResourcingParamFactsType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="ResourcingParamFactsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="param" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="key" type="{http://www.yawlfoundation.org/yawlschema}NameType"/>
 *                   &lt;element name="value" type="{http://www.yawlfoundation.org/yawlschema}NameType"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "ResourcingParamFactsType",
    propOrder = {"param"})
public class ResourcingParamFactsType {

  @XmlElement(required = true)
  protected List<ResourcingParamFactsType.Param> param;

  /**
   * Gets the value of the param property.
   *
   * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the param property.
   *
   * <p>For example, to add a new item, do as follows:
   *
   * <pre>
   *    getParam().add(newItem);
   * </pre>
   *
   * <p>Objects of the following type(s) are allowed in the list {@link
   * ResourcingParamFactsType.Param }
   */
  public List<ResourcingParamFactsType.Param> getParam() {
    if (param == null) {
      param = new ArrayList<ResourcingParamFactsType.Param>();
    }
    return this.param;
  }

  /**
   * Java class for anonymous complex type.
   *
   * <p>The following schema fragment specifies the expected content contained within this class.
   *
   * <pre>
   * &lt;complexType>
   *   &lt;complexContent>
   *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
   *       &lt;sequence>
   *         &lt;element name="key" type="{http://www.yawlfoundation.org/yawlschema}NameType"/>
   *         &lt;element name="value" type="{http://www.yawlfoundation.org/yawlschema}NameType"/>
   *       &lt;/sequence>
   *     &lt;/restriction>
   *   &lt;/complexContent>
   * &lt;/complexType>
   * </pre>
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(
      name = "",
      propOrder = {"key", "value"})
  public static class Param {

    @XmlElement(required = true)
    protected String key;

    @XmlElement(required = true)
    protected String value;

    /**
     * Gets the value of the key property.
     *
     * @return possible object is {@link String }
     */
    public String getKey() {
      return key;
    }

    /**
     * Sets the value of the key property.
     *
     * @param value allowed object is {@link String }
     */
    public void setKey(String value) {
      this.key = value;
    }

    /**
     * Gets the value of the value property.
     *
     * @return possible object is {@link String }
     */
    public String getValue() {
      return value;
    }

    /**
     * Sets the value of the value property.
     *
     * @param value allowed object is {@link String }
     */
    public void setValue(String value) {
      this.value = value;
    }
  }
}
