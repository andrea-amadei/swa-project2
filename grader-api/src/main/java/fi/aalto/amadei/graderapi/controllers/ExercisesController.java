package fi.aalto.amadei.graderapi.controllers;

import fi.aalto.amadei.graderapi.DatabaseConnectionInitializer;
import fi.aalto.amadei.graderapi.beans.ExerciseBean;
import fi.aalto.amadei.graderapi.beans.UserSessionBean;
import fi.aalto.amadei.graderapi.beans.responses.SuccessBean;
import fi.aalto.amadei.graderapi.daos.ExerciseDAO;
import fi.aalto.amadei.graderapi.exceptions.ElementNotFoundException;
import fi.aalto.amadei.graderapi.exceptions.InvalidParameterTypeException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class ExercisesController {

    @GetMapping("/exercises")
    public SuccessBean<List<ExerciseBean>> getAllExercises(HttpServletRequest request) {
        UserSessionBean session = UserSessionBean.getSessionFromRequest(request);

        return new SuccessBean<>(
                UserSessionBean.getSessionFromRequest(request),
                ExerciseDAO.getAllExercises(DatabaseConnectionInitializer.getConnection(), session.getSessionID())
        );
    }

    @GetMapping("/exercises/{exerciseID}")
    public SuccessBean<ExerciseBean> getExerciseByID(HttpServletRequest request,
         @PathVariable(value = "exerciseID", required = true) String exerciseID
    ) {
        int id;
        try {
            id = Integer.parseInt(exerciseID);
        } catch (NumberFormatException e) {
            throw new InvalidParameterTypeException("Parameter exerciseID must be integer");
        }

        ExerciseBean exercise = ExerciseDAO.getExerciseByID(DatabaseConnectionInitializer.getConnection(), id);

        if(exercise == null)
            throw new ElementNotFoundException("No exercise found with id: " + id);

        return new SuccessBean<>(UserSessionBean.getSessionFromRequest(request), exercise);
    }
}
