package fi.aalto.amadei.graderapi.grading;

import fi.aalto.amadei.graderapi.DatabaseConnectionInitializer;
import fi.aalto.amadei.graderapi.daos.SubmissionDAO;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class GradingScheduler {
    private static final Queue<RunnableSubmission> queue = new ArrayDeque<>();

    private static final int MAX_EXECUTING = 5;
    private static final int MAX_DURATION_SECONDS = 60;
    private static final Map<Integer, ExecutingSubmission> executing = new HashMap<>(MAX_EXECUTING);

    private static final ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(MAX_EXECUTING);

    private static void update() {
        // Force close stuck instances
        Instant now = Instant.now();
        for(int id : executing.keySet()) {
            long secondsRunning = ChronoUnit.SECONDS.between(now, executing.get(id).getStartTime());

            if (secondsRunning > MAX_DURATION_SECONDS) {
                Future<?> finished = executing.get(id).getFuture();

                finished.cancel(true);
                executing.remove(id);
                SubmissionDAO.setResult(DatabaseConnectionInitializer.getConnection(), id, "ERROR");
            }
        }

        // If all machines are occupied quit
        if(executing.size() >= MAX_EXECUTING)
            return;

        // Start executing all instances that can fit in free slots
        for(int i = 0; i < MAX_EXECUTING - executing.size(); i++) {
            if(queue.isEmpty())
                return;

            RunnableSubmission toRun = queue.poll();
            SubmissionDAO.setStatus(DatabaseConnectionInitializer.getConnection(), toRun.getId(), "RUNNING");
            executing.put(toRun.getId(), new ExecutingSubmission(pool.submit(toRun)));
        }
    }

    public synchronized static void addToQueue(RunnableSubmission submission) {
        queue.add(submission);
        SubmissionDAO.setStatus(DatabaseConnectionInitializer.getConnection(), submission.getId(), "QUEUED");

        update();
    }

    public synchronized static void addToQueue(int newSubmissionID, String oldSubmissionResult) {
        SubmissionDAO.setResult(DatabaseConnectionInitializer.getConnection(), newSubmissionID, oldSubmissionResult);
    }

    public synchronized static void finishedExecuting(int id, String result) {
        Future<?> finished = executing.get(id).getFuture();

        finished.cancel(false);
        executing.remove(id);
        SubmissionDAO.setResult(DatabaseConnectionInitializer.getConnection(), id, result);

        update();
    }
}
