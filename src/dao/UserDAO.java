package dao;

import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Enables access to users in the Database
 *
 */
public class UserDAO {
    private final Connection conn;
    /** Sets connection to the database
     * @param conn specified connection
     */
    public UserDAO(Connection conn) {
        this.conn = conn;
    }

    /** inserts user into Database
     * @param user user to be inserted
     */
    public void insert(User user) throws DataAccessException {
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO Users (userName, personID, gender, password, email, " +
                "firstName, lastName) VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, user.getUserName());
            stmt.setString(2, user.getPersonID());
            if (user.getGender().equals("m") || user.getGender().equals("f")) {
                stmt.setString(3, user.getGender());
            }
            else {
                String message = user.getGender() + " is not a valid gender!";
                throw new DataAccessException(message);
            }
            stmt.setString(4, user.getPassword());
            stmt.setString(5, user.getEmail());
            stmt.setString(6, user.getFirstName());
            stmt.setString(7, user.getLastName());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /** Finds and grabs a specified User from the Database
     * @param username the username that specifies the user
     * @return user
     */
    public User find(String username) throws DataAccessException {
        User user;
        ResultSet rs = null;
        String sql = "SELECT * FROM Users WHERE userName = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User(rs.getString("userName"), rs.getString("personID"),
                        rs.getString("gender"), rs.getString("password"), rs.getString("email"),
                        rs.getString("firstName"), rs.getString("lastName"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding user");
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

    public void clear() throws DataAccessException {
        String sql = "DELETE FROM Users";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error clearing users");
        }
    }
}
