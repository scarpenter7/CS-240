package result;

import model.Person;

import java.util.ArrayList;

/** Output of a Person Service
 *
 */
public class PersonsResult {
    private String associatedUsername;
    private String personID;
    private String firstName;
    private String lastName;
    private String gender;
    private String fatherID;
    private String motherID;
    private String spouseID;
    private String message;
    private ArrayList<Person> data;
    private boolean success;

    /** Constructor
     *
     * @param person containing all their attributes
     */
    public PersonsResult(Person person) {
        associatedUsername = person.getAssociatedUsername();
        personID = person.getPersonID();
        firstName = person.getFirstName();
        lastName = person.getLastName();
        gender = person.getGender();
        fatherID = person.getFatherID();
        motherID = person.getMotherID();
        spouseID = person.getSpouseID();
        success = true;
    }

    public PersonsResult(ArrayList<Person> data) {
        this.data = data;
        success = true;
    }

    public PersonsResult(String message) {
        this.message = message;
        success = false;
    }

    public String getUsername() {
        return associatedUsername;
    }

    public void setUsername(String username) {
        this.associatedUsername = username;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFatherID() {
        return fatherID;
    }

    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    public String getMotherID() {
        return motherID;
    }

    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    public String getSpouseID() {
        return spouseID;
    }

    public void setSpouseID(String spouseID) {
        this.spouseID = spouseID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public ArrayList<Person> getData() {
        return data;
    }
}
