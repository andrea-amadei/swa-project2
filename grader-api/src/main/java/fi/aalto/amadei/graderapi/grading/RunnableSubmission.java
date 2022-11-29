package fi.aalto.amadei.graderapi.grading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class RunnableSubmission implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RunnableSubmission.class);

    private final int id;

    public RunnableSubmission(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public void run() {
        logger.info("Started executing submission {}", id);

        String[] COMMANDS = {
                "docker", "run",
                "--name", String.format("grader-instance-%d", id),
                "--rm",
                "-e", String.format("SUBMISSION_ID=%d", id),
                "grader-instance"
        };

        List<String> POSSIBLE_RESULTS = List.of("PASS", "FAIL", "ERROR");

        Process process;
        String result;
        try {
            process = Runtime.getRuntime().exec(COMMANDS);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder str = new StringBuilder();

            String s;
            while ((s = stdInput.readLine()) != null) {
                str.append(s);
            }

            result = str.toString().strip();

        } catch (Exception e) {
            GradingScheduler.finishedExecuting(id, "FAIL");
            return;
        }

        if(!POSSIBLE_RESULTS.contains(result))
            result = "ERROR";

        logger.info("Finished executing submission {} with result {}", id, result);
        GradingScheduler.finishedExecuting(id, result);
    }
}
