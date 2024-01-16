//
// Created by Antonia Stieger on 16.01.2024.
//

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.*;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * Represents a pet with various attributes such as name, nickname, birthday, type, vaccinations, and ID.
 * Provides JAXB annotations for customized XML serialization and deserialization.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Pet {

    /**
     * The nickname of the pet (mapped to XML attribute).
     */
    @XmlAttribute(name = "nickname")
    private String mNickname;

    /**
     * The name of the pet.
     */
    @XmlElement(name = "name")
    private String mName;

    /**
     * The type of the pet (e.g., CAT, DOG).
     */
    @XmlElement(name = "type")
    private Type mType;

    /**
     * The ID of the pet.
     */
    @XmlElement(name = "ID")
    private String mID;

    /**
     * The birthday of the pet.
     */
    @XmlElement(name = "birthday")
    @XmlSchemaType(name = "date")
    private Calendar mBirthday;

    /**
     * The vaccinations received by the pet, grouped under the "vaccinations" tag.
     */
    @XmlElementWrapper(name = "vaccinations")
    @XmlElement(name = "vaccination")
    private String[] mVaccinations;

    /**
     * Enumeration representing different types of pets.
     * Each constant corresponds to a specific type such as CAT, DOG, MOUSE, or BIRD.
     */
    public enum Type {
        CAT, DOG, MOUSE, BIRD
    }


    /**
     * Main method to demonstrate XML serialization and deserialization of a Pet object.
     *
     * @param _args Command-line arguments (not used).
     */
    public static void main(String[] _args) {
        // Create a Pet object with the specified data
        Pet pet = new Pet();
        pet.mNickname = "Tom";
        pet.mName = "Thomas";
        pet.mType = Type.CAT;
        pet.mID = "123456789";
        pet.mBirthday = Calendar.getInstance();
        pet.mVaccinations = new String[]{"cat flu", "feline distemper", "rabies", "leucosis"};


        // Serialize the Pet object to XML
        StringWriter sw = new StringWriter();
        try {
            JAXBContext context = JAXBContext.newInstance(Pet.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(pet, sw);

        } catch (JAXBException e) {
            e.printStackTrace();
        }

        String xml = sw.toString();
        System.out.println("Serialized XML:\n" + xml);

        // Deserialize the XML back to a Pet object
        try {
            JAXBContext context = JAXBContext.newInstance(Pet.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            StringReader reader = new StringReader(xml);
            Pet deserializedPet = (Pet) unmarshaller.unmarshal(reader);

            // Check if the deserialized Pet object is not null
            if (deserializedPet != null) {
                System.out.println("\nDeserialized Pet object:");
                System.out.println("Nickname: " + deserializedPet.mNickname);
                System.out.println("Name: " + deserializedPet.mName);
                System.out.println("Type: " + deserializedPet.mType);
                System.out.println("ID: " + deserializedPet.mID);
                System.out.println("Birthday: " + deserializedPet.mBirthday);
                System.out.println("Vaccinations: " + Arrays.toString(deserializedPet.mVaccinations));

            } else {
                System.out.println("Deserialization failed. The deserializedPet is null.");
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }
}
