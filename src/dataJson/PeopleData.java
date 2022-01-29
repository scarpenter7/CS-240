package dataJson;

import com.google.gson.Gson;
import dao.DataAccessException;
import dao.Database;
import model.Event;
import model.Person;
import model.User;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class PeopleData {
    private LocationData locationData;
    private MaleFirstNames maleFirstNames;
    private FemaleFirstNames femaleFirstNames;
    private LastNames lastNames;

    public PeopleData() {
        initializeLocationData();
        initializeMaleNames();
        initializeFemaleNames();
        initializeLastNames();
    }

    private void initializeLocationData() {
        try {
            Reader reader = new FileReader("json/locations.json");
            Gson gson = new Gson();
            locationData = gson.fromJson(reader, LocationData.class);
            reader.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeMaleNames() {
        try {
            Reader reader = new FileReader("json/mnames.json");
            Gson gson = new Gson();
            maleFirstNames = gson.fromJson(reader, MaleFirstNames.class);
            reader.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeFemaleNames() {
        try {
            Reader reader = new FileReader("json/fnames.json");
            Gson gson = new Gson();
            femaleFirstNames = gson.fromJson(reader, FemaleFirstNames.class);
            reader.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeLastNames() {
        try {
            Reader reader = new FileReader("json/snames.json");
            Gson gson = new Gson();
            lastNames = gson.fromJson(reader, LastNames.class);
            reader.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    public Person generateUserPerson(Database db, User user) throws Exception {
        try {
            String personID = user.getPersonID();
            String username = user.getUserName();
            String gender = user.getGender();
            String fatherID = db.generateUniquePersonID();
            String motherID = db.generateUniquePersonID();
            String spouseID = db.generateUniquePersonID();
            String firstName = user.getFirstName();
            String lastName = user.getLastName();
            Person newPerson = new Person(personID, username, gender, fatherID, motherID,
                                            spouseID, firstName, lastName);
            db.getPersonDAO().insert(newPerson);
            generateUserBirth(db, newPerson);
            return newPerson;
        }
        catch (DataAccessException e) {
            e.printStackTrace();
        }
        throw new Exception("something wrong with generateUserPerson");
    }

    public Person generateRandomParent(Database db, Person child, String parentGender, String userName,
                                       boolean isLastGeneration) throws Exception {
        try {
            if (!(parentGender.equals("m") || parentGender.equals("f"))) {
                throw new Exception("invalid gender in generateRandomParent method");
            }

            Person newPerson = createPerson(db, child, parentGender, userName);

            if (isLastGeneration) {
                newPerson.setMotherID("");
                newPerson.setFatherID("");
            }
            db.getPersonDAO().insert(newPerson);
            generateFundamentalEvents(db, child, newPerson);
            return newPerson;
        }
        catch (DataAccessException e) {
            e.printStackTrace();
        }
        throw new Exception("something wrong with generateRandomParent");
    }

    private Person createPerson(Database db, Person child, String parentGender, String userName) throws Exception {
        if (!(parentGender.equals("m") || parentGender.equals("f"))) {
            throw new Exception("invalid gender in generateRandomParent method");
        }
        String firstName = generateFirstName(parentGender);
        String lastName = generateLastName(child, parentGender);
        String parentID = getParentID(child, parentGender);
        String parentFatherID = db.generateUniquePersonID();
        String parentMotherID = db.generateUniquePersonID();
        String parentSpouseID = getSpouseID(child, parentGender);

        return new Person(parentID, userName, parentGender, parentFatherID,
                parentMotherID, parentSpouseID, firstName, lastName);
    }

    private String getParentID(Person child, String parentGender) {
        if (parentGender.equals("m")) {
            return child.getFatherID();
        }
        else { //it's female
            return child.getMotherID();
        }
    }


    private String getSpouseID(Person child, String parentGender) {
        if (parentGender.equals("m")) {
            return child.getMotherID();
        }
        else { //it's female
            return child.getFatherID();
        }
    }

    private String generateFirstName(String parentGender) {
        if (parentGender.equals("m")) {
            String[] firstNames = maleFirstNames.getFirstNames();
            int index = (int) (firstNames.length * Math.random());
            return firstNames[index];
        }
        else { //it's female
            String[] firstNames = femaleFirstNames.getFirstNames();
            int firstNameIndex = (int) (firstNames.length * Math.random());
            return firstNames[firstNameIndex];
        }
    }

    private String generateLastName(Person child, String parentGender) {
        if (parentGender.equals("m")) {
            return child.getLastName();
        }
        else { //it's female
            String[] lastNames = this.lastNames.getLastNames();
            int lastNameIndex = (int) (lastNames.length * Math.random());
            return lastNames[lastNameIndex];
        }
    }

    private void generateFundamentalEvents(Database db, Person child, Person currentPerson) {
        try {
           generateEvent(db, child, currentPerson, "birth");
           generateEvent(db, child, currentPerson, "marriage");
           generateEvent(db, child, currentPerson, "death");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generateUserBirth(Database db, Person userPerson) throws Exception {
        try {
            Event event = createUserBirth(db, userPerson);
            db.getEventDAO().insert(event);
            return;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        throw new Exception("Something wrong in generateEvent");
    }

    public void generateEvent(Database db, Person child, Person currentPerson, String eventType) throws Exception {
        try {
            if (eventType.equals("marriage")) {
                if (linkMarriageToSpouse(db, currentPerson)) {
                    return;
                } //else the currentPerson's spouse hasn't been created yet,
                    // therefore their marriage event doesn't exist
            }
            Event event = createEvent(db, child, currentPerson, eventType);
            db.getEventDAO().insert(event);
            return;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        throw new Exception("Something wrong in generateEvent");
    }

    private Event createEvent(Database db, Person child, Person currentPerson, String eventType) throws Exception {
        String eventID = db.generateUniqueEventID();
        String username = currentPerson.getAssociatedUsername();
        String personID = currentPerson.getPersonID();
        Location location = generateRandomLocation();
        float latitude = location.getLatitude();
        float longitude = location.getLongitude();
        String country = location.getCountry();
        String city = location.getCity();
        int year = generatePseudoRandomYear(db, child, currentPerson, eventType);

        return new Event(eventID, username, personID, latitude, longitude,
                country, city, eventType, year);
    }

    private Event createUserBirth(Database db, Person currentPerson) throws DataAccessException {
        String eventID = db.generateUniqueEventID();
        String username = currentPerson.getAssociatedUsername();
        String personID = currentPerson.getPersonID();
        Location location = generateRandomLocation();
        float latitude = location.getLatitude();
        float longitude = location.getLongitude();
        String country = location.getCountry();
        String city = location.getCity();
        int year = 2000;
        return new Event(eventID, username, personID, latitude, longitude, country, city, "birth", year);
    }

    private boolean linkMarriageToSpouse(Database db, Person currentPerson) {
        try {
            Person spouse = db.getPersonDAO().find(currentPerson.getSpouseID());
            if (spouse != null) {
                Event marriage = db.getEventDAO().find(spouse.getPersonID(), "marriage");
                String eventID = db.generateUniqueEventID();
                marriage.setPersonID(currentPerson.getPersonID());
                marriage.setEventID(eventID);
                db.getEventDAO().insert(marriage);
                return true;
            }
        }
        catch (DataAccessException e) {
            e.printStackTrace();
        }
        return false; //spouse has not been created yet so marriage event doesn't exist yet
    }

    private Location generateRandomLocation() {
        Location[] locations = this.locationData.getLocations();
        int locationIndex = (int) (locations.length * Math.random());
        return locations[locationIndex];
    }

    private int generatePseudoRandomYear(Database db, Person child, Person currentPerson,
                                         String eventType) throws Exception {
        try {
            if (eventType.equals("birth")) {
                return generateBirthYear(db, child);
            }
            else if (eventType.equals("marriage")) {
                return generateMarriageYear(db, currentPerson);
            }
            else if (eventType.equals("death")) {
                return generateDeathYear(db, currentPerson);
            }
            else { //other event type
                return generateEventYear(db, currentPerson);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        throw new Exception("Something wrong in generatePseudoRandomYear");
    }

    private int generateBirthYear(Database db, Person child) throws Exception {
        try {
            Event childBirth = db.getEventDAO().find(child.getPersonID(), "birth");
            int childBirthYear;
            if (childBirth != null) {
                childBirthYear = childBirth.getYear();
            }
            else {
                childBirthYear = 2000;
            }
            int maxYear = childBirthYear - 13;
            int minYear = childBirthYear - 50;
            return (int) (Math.random() * (maxYear - minYear + 1)  + minYear);
        }
        catch (DataAccessException e) {
            e.printStackTrace();
        }
        throw new Exception("Something wrong with generate birth year");
    }

    private int generateMarriageYear(Database db, Person currentPerson) throws Exception {
        try {
            Event birth = db.getEventDAO().find(currentPerson.getPersonID(), "birth");
            assert birth != null;
            int birthYear = birth.getYear();
            int maxYear = birthYear + 50;
            int minYear = birthYear + 14;
            int marriageYear = (int)(Math.random() * (maxYear - minYear + 1)  + minYear);
            return marriageYear;
        }
        catch (DataAccessException e) {
            e.printStackTrace();
        }
        throw new Exception("Something wrong with generate marriage year");
    }

    private int generateDeathYear(Database db, Person currentPerson) throws Exception {
        try {
            Event birth = db.getEventDAO().find(currentPerson.getPersonID(), "birth");
            assert birth != null;
            int birthYear = birth.getYear();

            Event marriage = db.getEventDAO().find(currentPerson.getPersonID(), "marriage");
            assert marriage != null;
            int marriageYear = marriage.getYear();

            int maxYear = birthYear + 120;
            int minYear = marriageYear;
            return (int) (Math.random() * (maxYear - minYear + 1)  + minYear);
        }
        catch (DataAccessException e) {
            e.printStackTrace();
        }
        throw new Exception("Something wrong with generate death year");
    }

    private int generateEventYear(Database db, Person currentPerson) throws Exception {
        try {
            Event birth = db.getEventDAO().find(currentPerson.getPersonID(), "birth");
            int birthYear = birth.getYear();

            Event death = db.getEventDAO().find(currentPerson.getPersonID(), "death");
            int deathYear = death.getYear();

            int maxYear = deathYear;
            int minYear = birthYear;
            return (int) (Math.random() * (maxYear - minYear + 1)  + minYear);
        }
        catch (DataAccessException e) {
            e.printStackTrace();
        }
        throw new Exception("Something wrong with generate event year");
    }
}
