package dao;

import model.Person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/** Enables access to people in the Database
 *
 */
public class PersonDAO {
    private final Connection conn;
    /** Sets connection to the database
     * @param conn specified connection
     */
    public PersonDAO(Connection conn)
    {
        this.conn = conn;
    }

    /** inserts person into Database
     * @param person person to be inserted
     * @throws  DataAccessException if something goes wrong in the Database
     */
    public void insert(Person person) throws DataAccessException {
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO Persons ( personID, userName, gender, fatherID, motherID, " +
                    "spouseID, firstName, lastName) VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, person.getPersonID());
            stmt.setString(2, person.getAssociatedUsername());
            stmt.setString(3, person.getGender());
            stmt.setString(4, person.getFatherID());
            stmt.setString(5, person.getMotherID());
            stmt.setString(6, person.getSpouseID());
            stmt.setString(7, person.getFirstName());
            stmt.setString(8, person.getLastName());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /** Finds and grabs a specified person from the Database
     * @param personID the ID that specifies the person
     * @throws DataAccessException if something goes wrong
     * @return Person
     */
    public Person find(String personID) throws DataAccessException {
        Person person;
        ResultSet rs = null;
        String sql = "SELECT * FROM Persons WHERE personID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                person = new Person( rs.getString("personID"), rs.getString("userName"),
                        rs.getString("gender"), rs.getString("fatherID"), rs.getString("motherID"),
                        rs.getString("spouseID"), rs.getString("firstName"), rs.getString("lastName"));
                return person;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding person");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }

    /** Finds and grabs a specified person from the Database
     * @param userName identifier that marks the group of people to be found
     * @throws DataAccessException if something goes wrong
     * @return Person
     */
    public ArrayList<Person> findAllAssociated(String userName) throws DataAccessException {
        ArrayList<Person> people = new ArrayList<>();
        ResultSet rs = null;
        String sql = "SELECT * FROM Persons WHERE userName = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Person person = new Person( rs.getString("personID"), rs.getString("userName"),
                        rs.getString("gender"), rs.getString("fatherID"), rs.getString("motherID"),
                        rs.getString("spouseID"), rs.getString("firstName"), rs.getString("lastName"));
                people.add(person);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding people associated with userName");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        return people;
    }

    public void clear() throws DataAccessException {
        String sql = "DELETE FROM Persons";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error clearing Persons");
        }
    }

    /** Clears all people associated with a username except for the person corresponding to the user
     *
     * @param userName
     * @param userPersonID
     * @throws DataAccessException
     */
    public void clear(String userName, String userPersonID) throws DataAccessException {
        String sql = "DELETE FROM Persons WHERE userName = ? AND personID != ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            stmt.setString(2, userPersonID);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error clearing Persons");
        }
    }
}
