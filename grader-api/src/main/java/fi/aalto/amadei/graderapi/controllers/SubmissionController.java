package fi.aalto.amadei.graderapi.controllers;

import fi.aalto.amadei.graderapi.DatabaseConnectionInitializer;
import fi.aalto.amadei.graderapi.beans.ExerciseBean;
import fi.aalto.amadei.graderapi.beans.SubmissionBean;
import fi.aalto.amadei.graderapi.beans.UserSessionBean;
import fi.aalto.amadei.graderapi.beans.responses.SuccessBean;
import fi.aalto.amadei.graderapi.daos.ExerciseDAO;
import fi.aalto.amadei.graderapi.daos.SubmissionDAO;
import fi.aalto.amadei.graderapi.exceptions.ElementNotFoundException;
import fi.aalto.amadei.graderapi.exceptions.InvalidParameterTypeException;
import fi.aalto.amadei.graderapi.exceptions.UnableToSubmitException;
import fi.aalto.amadei.graderapi.exceptions.UnauthorizedRequestException;
import fi.aalto.amadei.graderapi.grading.GradingScheduler;
import fi.aalto.amadei.graderapi.grading.RunnableSubmission;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class SubmissionController {

    @GetMapping("/submissions")
    public SuccessBean<List<SubmissionBean>> getAllSubmissions(HttpServletRequest request,
        @RequestParam(value = "exerciseID", required = false) String exerciseID
    ) {
        UserSessionBean session = UserSessionBean.getSessionFromRequest(request);

        if(exerciseID == null)
            return new SuccessBean<>(
                    session,
                    SubmissionDAO.getAllSubmissions(DatabaseConnectionInitializer.getConnection(), session.getSessionID())
            );

        int id;
        try {
            id = Integer.parseInt(exerciseID);
        } catch (NumberFormatException e) {
            throw new InvalidParameterTypeException("Query parameter exerciseID must be integer");
        }

        return new SuccessBean<>(
                session,
                SubmissionDAO.getSubmissionByExerciseID(
                        DatabaseConnectionInitializer.getConnection(),
                        id,
                        session.getSessionID()
                )
        );
    }

    @GetMapping("/submissions/{submissionID}")
    public SuccessBean<SubmissionBean> getSubmissionsByID(HttpServletRequest request,
        @PathVariable(value = "submissionID", required = true) String submissionID
    ) {
        UserSessionBean session = UserSessionBean.getSessionFromRequest(request);

        int id;
        try {
            id = Integer.parseInt(submissionID);
        } catch (NumberFormatException e) {
            throw new InvalidParameterTypeException("Parameter submissionID must be integer");
        }

        SubmissionBean submission = SubmissionDAO.getSubmissionByID(
                DatabaseConnectionInitializer.getConnection(),
                id
        );

        if(submission == null)
            throw new ElementNotFoundException("No submissions found with id: " + id);

        if(!submission.getSessionID().equals(session.getSessionID()) && !session.getSuperUser())
            throw new UnauthorizedRequestException("Unauthorized access to submission");

        return new SuccessBean<>(session, submission);
    }

    @PostMapping("/submissions")
    public SuccessBean<SubmissionBean> addNewSubmission(HttpServletRequest request,
        @RequestParam(value = "exerciseID", required = true) String exerciseID,
        @RequestBody(required = true) String payload
    ) {
        UserSessionBean session = UserSessionBean.getSessionFromRequest(request);

        int id;
        try {
            id = Integer.parseInt(exerciseID);
        } catch (NumberFormatException e) {
            throw new InvalidParameterTypeException("Query parameter exerciseID must be integer");
        }

        ExerciseBean exercise = ExerciseDAO.getExerciseByID(DatabaseConnectionInitializer.getConnection(), id);

        if(exercise == null)
            throw new ElementNotFoundException("No exercise found with id: " + id);

        SubmissionBean submission = SubmissionDAO.addSubmission(
                DatabaseConnectionInitializer.getConnection(), id, session.getSessionID(), payload
        );

        if(submission == null)
            throw new UnableToSubmitException("Unable to create submission");

        SubmissionBean oldSubmission = SubmissionDAO.getIdenticalSubmission(
                DatabaseConnectionInitializer.getConnection(), id, session.getSessionID(), payload
        );

        if(oldSubmission != null)
            GradingScheduler.addToQueue(submission.getSubmissionID(), oldSubmission.getResult());
        else
            GradingScheduler.addToQueue(new RunnableSubmission(submission.getSubmissionID()));

        return new SuccessBean<>(session, submission);
    }
}
