package fi.aalto.amadei.graderapi.daos;

import fi.aalto.amadei.graderapi.beans.UserSessionBean;
import fi.aalto.amadei.graderapi.exceptions.InvalidDatabaseInteractionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class AuthenticationDAO {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationDAO.class);

    public static UserSessionBean createOrUpdateSession(Connection connection, @Nullable String oldSessionId) {
        Objects.requireNonNull(connection);

        if(oldSessionId == null)
            oldSessionId = "";

        String query = "SELECT * FROM renew_user_session(?);";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, oldSessionId);

            try (ResultSet result = preparedStatement.executeQuery()) {
                if(!result.isBeforeFirst())
                    return null;

                result.next();

                UserSessionBean session = new UserSessionBean();
                session.setSessionID(result.getString("session_ID"));
                session.setCreationTime(result.getTimestamp("creation_time").toInstant());
                session.setExpirationTime(result.getTimestamp("expiration_time").toInstant());
                session.setNewSession(result.getBoolean("new_session"));
                session.setSuperUser(result.getBoolean("super_user"));

                return session;
            }
        } catch (SQLException e) {
            throw new InvalidDatabaseInteractionException(e);
        }
    }

    public static void cleanupInactiveSessions(Connection connection) {
        Objects.requireNonNull(connection);

        String query = "CALL clean_inactive_sessions();";

        try (PreparedStatement preparedStatement = connection.prepareCall(query)) {
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new InvalidDatabaseInteractionException(e);
        }
    }
}
