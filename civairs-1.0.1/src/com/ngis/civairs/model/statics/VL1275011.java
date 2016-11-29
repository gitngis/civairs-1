//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.8-b130911.1802 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2016.10.04 à 11:04:45 AM GMT 
//


package com.ngis.civairs.model.statics;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour VL127_5_0_1_1.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * <p>
 * <pre>
 * &lt;simpleType name="VL127_5_0_1_1">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="VMC"/>
 *     &lt;enumeration value="IMC"/>
 *     &lt;enumeration value="Unknown"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "VL127_5_0_1_1")
@XmlEnum
public enum VL1275011 {


    /**
     * The general weather conditions in the area of the occurrence were visual meteorological conditions (VMC)
     * VMC: Meteorological conditions expressed in terms of visibility, distance from cloud, and ceiling, equal to or better than specified minima. 
     * 
     */
    VMC("VMC"),

    /**
     * The general weather conditions in the area of the occurrence were instrument meteorological conditions (IMC).
     * IMC: Meteorological conditions expressed in terms of visibility, distance from cloud, and ceiling, less than the minima specified for visual meteorological conditions.
     *    Note 1.- The specified minima for visual meteorological conditions are contained in Chapter 4 of Annex 2.
     *    Note 2.- In a control zone, a VFR flight may proceed under instrument meteorological conditions if and as authorized by air traffic control.
     * 
     */
    IMC("IMC"),

    /**
     * The general weather conditions in the area of the occurrence were not established.
     * 
     */
    @XmlEnumValue("Unknown")
    UNKNOWN("Unknown");
    private final String value;

    VL1275011(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static VL1275011 fromValue(String v) {
        for (VL1275011 c: VL1275011 .values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
