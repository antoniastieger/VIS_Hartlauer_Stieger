//
// Created by Antonia Stieger on 16.01.2024.
//

package at.fhooe.sail.vis;

import jakarta.json.stream.JsonParser;
import jakarta.ws.rs.core.MediaType;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.*;
import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.eclipse.persistence.jaxb.UnmarshallerProperties;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Represents a pet with various attributes such as name, nickname, birthday, type, vaccinations, and ID.
 * Provides JAXB annotations for customized XML serialization and deserialization.
 */
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
    private PetTypes.Type mType;

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
     * Main method to demonstrate XML serialization and deserialization of a at.fhooe.sail.vis.Pet object.
     *
     * @param _args Command-line arguments (not used).
     */
    public static void main(String[] _args) {
        // Create Pet object with the specified data
        Pet tom = new Pet();
        tom.mNickname = "Tom";
        tom.mName = "Thomas";
        tom.mType = PetTypes.Type.CAT;
        tom.mID = "123456789";
        tom.mBirthday = Calendar.getInstance();
        tom.mBirthday.set(1940, Calendar.FEBRUARY, 10);
        tom.mVaccinations = new String[]{"cat flu", "feline distemper", "rabies", "leucosis"};

        // Serialize Pet object to JSON
        StringWriter sw = new StringWriter();
        try {
            // Create JAXBContext with both Pet and Pets classes
            JAXBContext context = JAXBContext.newInstance(Pet.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(MarshallerProperties.MEDIA_TYPE,
                    MediaType.APPLICATION_JSON);
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(tom, sw);

        } catch (JAXBException e) {
            e.printStackTrace();
        }

        String json = sw.toString();
        System.out.println("Serialized JSON:\n" + json);

        /*
        // Deserialize the XML back to Pet object
        try {
            // Create JAXBContext with Pet class
            JAXBContext context = JAXBContext.newInstance(Pet.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            unmarshaller.setProperty(UnmarshallerProperties.MEDIA_TYPE,
                    MediaType.APPLICATION_JSON);
            unmarshaller.setProperty(UnmarshallerProperties.JSON_INCLUDE_ROOT,
                    Boolean.FALSE);

            StringReader reader = new StringReader(json);
            Pet deserializedTom = (Pet) unmarshaller.unmarshal(reader);

            System.out.println("\nDeserialized Tom object:");
            System.out.println("Nickname: " + deserializedTom.mNickname);
            System.out.println("Name: " + deserializedTom.mName);
            System.out.println("Type: " + deserializedTom.mType);
            System.out.println("ID: " + deserializedTom.mID);
            System.out.println("Birthday: " + deserializedTom.mBirthday.getTime());
            System.out.println("Vaccinations: " + Arrays.toString(deserializedTom.mVaccinations));
            System.out.println();

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        */
    }
}