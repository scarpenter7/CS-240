package model;

/** A person that has a registered account with the application
 *
 */
public class User {
    private String userName;
    private String personID;
    private String gender;
    private String password;
    private String email;
    private String firstName;
    private String lastName;

    /** Constructor
     *
     * @param username unique account ID
     * @param personID unique personID associated with the user them self
     * @param gender self-explanatory
     * @param password self-explanatory
     * @param email self-explanatory
     * @param firstName self-explanatory
     * @param lastName self-explanatory
     */
    public User(String username, String personID, String gender, String password, String email, String firstName, String lastName) {
        this.userName = username;
        this.personID = personID;
        this.gender = gender;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    /** Check if two users are the same
     *
     * @param o user being compared to
     * @return boolean for whether or not they are the same user
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return getGender().equals(user.getGender()) &&
                getUserName().equals(user.getUserName()) &&
                getPersonID().equals(user.getPersonID()) &&
                getPassword().equals(user.getPassword()) &&
                getEmail().equals(user.getEmail()) &&
                getFirstName().equals(user.getFirstName()) &&
                getLastName().equals(user.getLastName());
    }
}
