//
// Created by Antonia Stieger on 16.01.2024.
//

package at.fhooe.sail.vis;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Represents a collection of pets with JAXB annotations for XML serialization and deserialization.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Pets {

    /**
     * An array of Pet objects representing the pets in the collection.
     */
    @XmlElement(name = "pet")
    protected Pet[] pets = null;

    /**
     * Default constructor for JAXB marshalling and unmarshalling.
     */
    public Pets() {}

    /**
     * Constructor that initializes the Pets object with an array of Pet objects.
     *
     * @param _p An array of Pet objects.
     */
    public Pets(Pet[] _p) {
        pets = _p;
    }
}

