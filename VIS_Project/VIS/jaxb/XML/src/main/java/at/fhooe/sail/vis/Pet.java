//
// Created by Antonia Stieger on 16.01.2024.
//

package at.fhooe.sail.vis;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.*;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Calendar;

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
        // Create Pet objects with the specified data
        Pet tom = new Pet();
        tom.mNickname = "Tom";
        tom.mName = "Thomas";
        tom.mType = PetTypes.Type.CAT;
        tom.mID = "123456789";
        tom.mBirthday = Calendar.getInstance();
        tom.mBirthday.set(1940, Calendar.FEBRUARY, 10);
        tom.mVaccinations = new String[]{"cat flu", "feline distemper", "rabies", "leucosis"};

        Pet mimi = new Pet();
        mimi.mNickname = "Mimi";
        mimi.mName = "Miriam";
        mimi.mType = PetTypes.Type.BIRD;
        mimi.mID = "378291025";
        mimi.mBirthday = Calendar.getInstance();
        mimi.mBirthday.set(2003, Calendar.OCTOBER, 25);
        mimi.mVaccinations = new String[]{"bird hunger", "syphilis", "ear infection", "urinary tract infection"};

        Pet paul = new Pet();
        paul.mNickname = "Pauli";
        paul.mName = "Paul";
        paul.mType = PetTypes.Type.DOG;
        paul.mID = "879500463";
        paul.mBirthday = Calendar.getInstance();
        paul.mBirthday.set(2016, Calendar.JUNE, 7);
        paul.mVaccinations = new String[]{"gonorrhea", "skin infection", "lung cancer"};

        Pets pets = new Pets(new Pet[]{tom, mimi, paul});

        // Serialize Pet objects to XML
        StringWriter sw = new StringWriter();
        try {
            // Create JAXBContext with both Pet and Pets classes
            JAXBContext context = JAXBContext.newInstance(Pet.class, Pets.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(pets, sw);

        } catch (JAXBException e) {
            e.printStackTrace();
        }

        String xml = sw.toString();
        System.out.println("Serialized XMLs:\n" + xml);

        // Deserialize the XML back to Pet objects
        try {
            // Create JAXBContext with both Pet and Pets classes
            JAXBContext context = JAXBContext.newInstance(Pet.class, Pets.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            StringReader reader = new StringReader(xml);
            Pets deserializedPets = (Pets) unmarshaller.unmarshal(reader);

            System.out.println("\nDeserialized Pet objects:");
            for (Pet deserializedPet : deserializedPets.pets) {
                System.out.println("Nickname: " + deserializedPet.mNickname);
                System.out.println("Name: " + deserializedPet.mName);
                System.out.println("Type: " + deserializedPet.mType);
                System.out.println("ID: " + deserializedPet.mID);
                System.out.println("Birthday: " + deserializedPet.mBirthday.getTime());
                System.out.println("Vaccinations: " + Arrays.toString(deserializedPet.mVaccinations));
                System.out.println();
            }

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
