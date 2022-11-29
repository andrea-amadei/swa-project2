package fi.aalto.amadei.graderapi.daos;

import fi.aalto.amadei.graderapi.beans.SubmissionBean;
import fi.aalto.amadei.graderapi.exceptions.InvalidDatabaseInteractionException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class SubmissionDAO {

    public static List<SubmissionBean> getAllSubmissions(Connection connection, String sessionID) {
        Objects.requireNonNull(connection);
        Objects.requireNonNull(sessionID);

        String query =
                "SELECT * " +
                "FROM submissions " +
                "WHERE session_id = ? " +
                "ORDER BY submission_time DESC";

        List<SubmissionBean> list = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, sessionID);

            try (ResultSet result = preparedStatement.executeQuery()) {
                while(result.next()) {
                    SubmissionBean submission = new SubmissionBean();

                    submission.setSubmissionID(result.getInt("submission_ID"));
                    submission.setExerciseID(result.getInt("exercise_ID"));
                    submission.setSessionID(result.getString("session_ID"));

                    submission.setContent(result.getString("content"));
                    submission.setSubmissionTime(result.getTimestamp("submission_time").toInstant());

                    submission.setStatus(result.getString("status"));
                    submission.setResult(result.getString("result"));

                    submission.setResultTime(
                            Optional.ofNullable(result.getTimestamp("result_time"))
                                    .map(Timestamp::toInstant)
                                    .orElse(null)
                    );

                    list.add(submission);
                }
            }
        } catch (SQLException e) {
            throw new InvalidDatabaseInteractionException(e);
        }

        return list;
    }

    public static SubmissionBean getSubmissionByID(Connection connection, int submissionID) {
        Objects.requireNonNull(connection);

        String query = "SELECT * FROM submissions WHERE submission_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, submissionID);

            try (ResultSet result = preparedStatement.executeQuery()) {
                if(!result.isBeforeFirst())
                    return null;

                result.next();

                SubmissionBean submission = new SubmissionBean();

                submission.setSubmissionID(result.getInt("submission_ID"));
                submission.setExerciseID(result.getInt("exercise_ID"));
                submission.setSessionID(result.getString("session_ID"));

                submission.setContent(result.getString("content"));
                submission.setSubmissionTime(result.getTimestamp("submission_time").toInstant());

                submission.setStatus(result.getString("status"));
                submission.setResult(result.getString("result"));

                submission.setResultTime(
                        Optional.ofNullable(result.getTimestamp("result_time"))
                                .map(Timestamp::toInstant)
                                .orElse(null)
                );

                return submission;
            }
        } catch (SQLException e) {
            throw new InvalidDatabaseInteractionException(e);
        }
    }

    public static List<SubmissionBean> getSubmissionByExerciseID(Connection connection, int exerciseID, String sessionID) {
        Objects.requireNonNull(connection);
        Objects.requireNonNull(sessionID);

        String query =
                "SELECT * " +
                "FROM submissions " +
                "WHERE exercise_id = ? AND session_id = ? " +
                "ORDER BY submission_time DESC";

        List<SubmissionBean> list = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, exerciseID);
            preparedStatement.setString(2, sessionID);

            try (ResultSet result = preparedStatement.executeQuery()) {
                while(result.next()) {
                    SubmissionBean submission = new SubmissionBean();

                    submission.setSubmissionID(result.getInt("submission_ID"));
                    submission.setExerciseID(result.getInt("exercise_ID"));
                    submission.setSessionID(result.getString("session_ID"));

                    submission.setContent(result.getString("content"));
                    submission.setSubmissionTime(result.getTimestamp("submission_time").toInstant());

                    submission.setStatus(result.getString("status"));
                    submission.setResult(result.getString("result"));

                    submission.setResultTime(
                            Optional.ofNullable(result.getTimestamp("result_time"))
                                    .map(Timestamp::toInstant)
                                    .orElse(null)
                    );

                    list.add(submission);
                }
            }
        } catch (SQLException e) {
            throw new InvalidDatabaseInteractionException(e);
        }

        return list;
    }

    public static SubmissionBean addSubmission(Connection connection, int exerciseID, String sessionID, String content) {
        Objects.requireNonNull(connection);
        Objects.requireNonNull(sessionID);

        String query = "SELECT * FROM add_submission(?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, sessionID);
            preparedStatement.setInt(2, exerciseID);
            preparedStatement.setString(3, content);

            try (ResultSet result = preparedStatement.executeQuery()) {
                if(!result.isBeforeFirst())
                    return null;

                result.next();

                SubmissionBean submission = new SubmissionBean();

                submission.setSubmissionID(result.getInt("submission_ID"));
                submission.setExerciseID(result.getInt("exercise_ID"));
                submission.setSessionID(result.getString("session_ID"));

                submission.setContent(result.getString("content"));
                submission.setSubmissionTime(result.getTimestamp("submission_time").toInstant());

                submission.setStatus(result.getString("status"));
                submission.setResult(result.getString("result"));

                submission.setResultTime(
                        Optional.ofNullable(result.getTimestamp("result_time"))
                                .map(Timestamp::toInstant)
                                .orElse(null)
                );

                return submission;
            }
        } catch (SQLException e) {
            throw new InvalidDatabaseInteractionException(e);
        }
    }

    public static SubmissionBean setStatus(Connection connection, int submissionID, String status) {
        Objects.requireNonNull(connection);
        Objects.requireNonNull(status);

        String query = "SELECT * FROM update_submission_status(?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, submissionID);
            preparedStatement.setString(2, status);

            try (ResultSet result = preparedStatement.executeQuery()) {
                if(!result.isBeforeFirst())
                    return null;

                result.next();

                SubmissionBean submission = new SubmissionBean();

                submission.setSubmissionID(result.getInt("submission_ID"));
                submission.setExerciseID(result.getInt("exercise_ID"));
                submission.setSessionID(result.getString("session_ID"));

                submission.setContent(result.getString("content"));
                submission.setSubmissionTime(result.getTimestamp("submission_time").toInstant());

                submission.setStatus(result.getString("status"));
                submission.setResult(result.getString("result"));

                submission.setResultTime(
                        Optional.ofNullable(result.getTimestamp("result_time"))
                                .map(Timestamp::toInstant)
                                .orElse(null)
                );

                return submission;
            }
        } catch (SQLException e) {
            throw new InvalidDatabaseInteractionException(e);
        }
    }

    public static SubmissionBean setResult(Connection connection, int submissionID, String new_result) {
        Objects.requireNonNull(connection);
        Objects.requireNonNull(new_result);

        String query = "SELECT * FROM set_submission_result(?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, submissionID);
            preparedStatement.setString(2, new_result);

            try (ResultSet result = preparedStatement.executeQuery()) {
                if(!result.isBeforeFirst())
                    return null;

                result.next();

                SubmissionBean submission = new SubmissionBean();

                submission.setSubmissionID(result.getInt("submission_ID"));
                submission.setExerciseID(result.getInt("exercise_ID"));
                submission.setSessionID(result.getString("session_ID"));

                submission.setContent(result.getString("content"));
                submission.setSubmissionTime(result.getTimestamp("submission_time").toInstant());

                submission.setStatus(result.getString("status"));
                submission.setResult(result.getString("result"));

                submission.setResultTime(
                        Optional.ofNullable(result.getTimestamp("result_time"))
                                .map(Timestamp::toInstant)
                                .orElse(null)
                );

                return submission;
            }
        } catch (SQLException e) {
            throw new InvalidDatabaseInteractionException(e);
        }
    }

    public static SubmissionBean getIdenticalSubmission(Connection connection, int exerciseID, String sessionID, String content) {
        Objects.requireNonNull(connection);
        Objects.requireNonNull(sessionID);

        String query =
                "SELECT * " +
                "FROM submissions " +
                "WHERE exercise_id = ? AND session_id = ? AND \"content\" = ? AND status = 'DONE'" +
                "ORDER BY submission_time DESC";

        List<SubmissionBean> list = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, exerciseID);
            preparedStatement.setString(2, sessionID);
            preparedStatement.setString(3, content);

            try (ResultSet result = preparedStatement.executeQuery()) {
                while(result.next()) {
                    SubmissionBean submission = new SubmissionBean();

                    submission.setSubmissionID(result.getInt("submission_ID"));
                    submission.setExerciseID(result.getInt("exercise_ID"));
                    submission.setSessionID(result.getString("session_ID"));

                    submission.setContent(result.getString("content"));
                    submission.setSubmissionTime(result.getTimestamp("submission_time").toInstant());

                    submission.setStatus(result.getString("status"));
                    submission.setResult(result.getString("result"));

                    submission.setResultTime(
                            Optional.ofNullable(result.getTimestamp("result_time"))
                                    .map(Timestamp::toInstant)
                                    .orElse(null)
                    );

                    list.add(submission);
                }
            }
        } catch (SQLException e) {
            throw new InvalidDatabaseInteractionException(e);
        }

        if(list.size() == 0)
            return null;

        return list.get(0);
    }
}
