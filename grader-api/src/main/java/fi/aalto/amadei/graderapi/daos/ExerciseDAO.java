package fi.aalto.amadei.graderapi.daos;

import fi.aalto.amadei.graderapi.beans.ExerciseBean;
import fi.aalto.amadei.graderapi.exceptions.InvalidDatabaseInteractionException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ExerciseDAO {

    public static final int MAX_VISIBLE_EXERCISES = 3;

    public static List<ExerciseBean> getAllExercises(Connection connection, String sessionID) {
        Objects.requireNonNull(connection);
        Objects.requireNonNull(sessionID);

        String query =
                "SELECT exercise_ID, \"tag\", title, \"content\"," +
                "       (SELECT COUNT(*)" +
                "        FROM submissions as s" +
                "        WHERE session_ID = ? AND result = 'PASS' AND s.exercise_ID = e.exercise_ID" +
                "        ) > 0 AS completed " +
                "FROM exercises as e " +
                "ORDER BY exercise_ID;";

        List<ExerciseBean> list = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, sessionID);

            try (ResultSet result = preparedStatement.executeQuery()) {
                while(result.next()) {
                    ExerciseBean exercise = new ExerciseBean();
                    exercise.setExerciseId(result.getInt("exercise_id"));
                    exercise.setTag(result.getString("tag"));
                    exercise.setTitle(result.getString("title"));
                    exercise.setContent(result.getString("content"));
                    exercise.setCompleted(result.getBoolean("completed"));

                    list.add(exercise);
                }
            }
        } catch (SQLException e) {
            throw new InvalidDatabaseInteractionException(e);
        }

        int visible = 0;

        for(ExerciseBean i : list) {
            if(i.getCompleted())
                i.setVisible(true);
            else if (!i.getCompleted() && visible < MAX_VISIBLE_EXERCISES) {
                i.setVisible(true);
                visible++;
            }
            else
                i.setVisible(false);
        }

        return list;
    }

    public static ExerciseBean getExerciseByID(Connection connection, int exerciseID) {
        Objects.requireNonNull(connection);

        String query = "SELECT * FROM exercises WHERE exercise_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, exerciseID);

            try (ResultSet result = preparedStatement.executeQuery()) {
                if(!result.isBeforeFirst())
                    return null;

                result.next();

                ExerciseBean exercise = new ExerciseBean();
                exercise.setExerciseId(result.getInt("exercise_id"));
                exercise.setTag(result.getString("tag"));
                exercise.setTitle(result.getString("title"));
                exercise.setContent(result.getString("content"));

                return exercise;
            }
        } catch (SQLException e) {
            throw new InvalidDatabaseInteractionException(e);
        }
    }
}
