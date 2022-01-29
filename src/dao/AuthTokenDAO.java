package dao;

import model.AuthToken;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Accesses the tokens in the Database
 *
 */
public class AuthTokenDAO {
    private final Connection conn;
    /** Sets connection to the database
     * @param conn specified connection
     */
    public AuthTokenDAO(Connection conn) {
        this.conn = conn;
    }

    /** inserts authToken into Database
     * @param authToken authToken to be inserted
     */
    public void insert(AuthToken authToken) throws DataAccessException {
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO AuthorizationTokens (authToken, UserName) VALUES(?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, authToken.getAuthTokenName());
            stmt.setString(2, authToken.getAssociatedUsername());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /** Finds and grabs a specified authToken from the Database
     * @param authTokenName name of the authToken
     * @return authToken
     */
    public AuthToken find(String authTokenName) throws DataAccessException {
        AuthToken authToken;
        ResultSet rs = null;
        String sql = "SELECT * FROM AuthorizationTokens WHERE authToken = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, authTokenName);
            rs = stmt.executeQuery();
            if (rs.next()) {
                authToken = new AuthToken(rs.getString("authToken"), rs.getString("userName"));
                return authToken;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding AuthorizationToken");
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
        String sql = "DELETE FROM AuthorizationTokens";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error clearing Authorization Tokens");
        }
    }
}
