package model;

import java.util.Objects;

/** Identifies an individual by several characteristics as seen in the constructor
 *
 */
public class Person {
    private String personID;
    private String associatedUsername;
    private String gender;
    private String fatherID;
    private String motherID;
    private String spouseID;
    private String firstName;
    private String lastName;

    /** Constructor
     *
     * @param username tied to a user if applicable
     * @param personID unique ID to access this person's details
     * @param gender self-explanatory
     * @param fatherID personID that corresponds to the person's father
     * @param motherID personID that corresponds to the person's mother
     * @param spouseID personID that corresponds to the person's spouse
     * @param firstName self-explanatory
     * @param lastName self-explanatory
     */
    public Person( String personID, String username, String gender, String fatherID, String motherID, String spouseID,
                  String firstName, String lastName) {
        this.personID = personID;
        this.associatedUsername = username;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
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

    /** Checks if two people are equal
     *
     * @param o person being compared to
     * @return boolean for whether or not they are the same individual
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return getGender().equals(person.getGender())  &&
                getAssociatedUsername().equals(person.getAssociatedUsername()) &&
                getPersonID().equals(person.getPersonID()) &&
                getFatherID().equals(person.getFatherID()) &&
                getMotherID().equals(person.getMotherID()) &&
                getSpouseID().equals(person.getSpouseID()) &&
                getFirstName().equals(person.getFirstName()) &&
                getLastName().equals(person.getLastName());
    }
}
