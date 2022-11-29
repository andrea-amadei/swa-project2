package fi.aalto.amadei.graderapi.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.Instant;

@JsonPropertyOrder({"submission_id", "exercise_id", "content", "submission_at", "status", "result", "result_at" })
public class SubmissionBean {
    @JsonProperty("submission_id")
    private int submissionID;
    @JsonProperty("exercise_id")
    private int exerciseID;
    @JsonIgnore
    private String sessionID;

    private String content;
    @JsonProperty("submission_at")
    private Instant submissionTime;

    private String status;
    private String result;
    @JsonProperty("result_at")
    private Instant resultTime;

    public int getSubmissionID() {
        return submissionID;
    }

    public void setSubmissionID(int submissionID) {
        this.submissionID = submissionID;
    }

    public int getExerciseID() {
        return exerciseID;
    }

    public void setExerciseID(int exerciseID) {
        this.exerciseID = exerciseID;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getSubmissionTime() {
        return submissionTime;
    }

    public void setSubmissionTime(Instant submissionTime) {
        this.submissionTime = submissionTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Instant getResultTime() {
        return resultTime;
    }

    public void setResultTime(Instant resultTime) {
        this.resultTime = resultTime;
    }
}
