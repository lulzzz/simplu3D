//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB)
// Reference Implementation, v2.1.3-b01-fcs
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source
// schema.
// Generated on: 2009.10.26 at 12:24:15 PM CET
//

package fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.regles;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for DistanceFHauteur complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="DistanceFHauteur">
 *   &lt;complexContent>
 *     &lt;extension base="{}Distance">
 *       &lt;sequence>
 *         &lt;element name="coefficient" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DistanceFHauteur", propOrder = { "coefficient",
    "hauteurOrigine" })
public class DistanceFHauteur extends Distance {
  protected double hauteurOrigine;

  public double getHauteurOrigine() {
    return this.hauteurOrigine;
  }

  public void setHauteurOrigine(double hauteurOrigine) {
    this.hauteurOrigine = hauteurOrigine;
  }

  protected double coefficient;

  /**
   * Gets the value of the coefficient property.
   */
  public double getCoefficient() {
    return this.coefficient;
  }

  /**
   * Sets the value of the coefficient property.
   */
  public void setCoefficient(double value) {
    this.coefficient = value;
  }

  public DistanceFHauteur() {
  }

  public DistanceFHauteur(double coef, double hauteurOrigine) {

    this.coefficient = coef;
    this.hauteurOrigine = hauteurOrigine;
  }

  @Override
  public String toString() {

    return this.coefficient + " * hauteur du batiment + " + this.hauteurOrigine;
  }
}
