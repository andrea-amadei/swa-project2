package fi.aalto.amadei.graderapi.controllers;

import fi.aalto.amadei.graderapi.DatabaseConnectionInitializer;
import fi.aalto.amadei.graderapi.beans.SubmissionBean;
import fi.aalto.amadei.graderapi.beans.UserSessionBean;
import fi.aalto.amadei.graderapi.daos.SubmissionDAO;
import fi.aalto.amadei.graderapi.exceptions.ElementNotFoundException;
import fi.aalto.amadei.graderapi.exceptions.InvalidParameterTypeException;
import fi.aalto.amadei.graderapi.exceptions.UnauthorizedRequestException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class TextOnlyController {

    @GetMapping(value = "/textonly", produces = "plain/text")
    public String getTextSubmission(HttpServletRequest request,
        @RequestParam(value = "submissionID", required = true) String submissionID
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

        return submission.getContent();
    }
}
