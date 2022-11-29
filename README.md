# Grader
The aim of the project is to develop a full web application (frontend and backend) to submit and grade automatically programming exercises. 
The whole application features:
- Single-page frontend application to browse exercise, submit solutions and explore older solutions, including their grades.
- Real-time updates to submission status and grades.
- Queueing system to avoid congestions.
- Multi-threaded containerized graders able to execute up to 5 (configurable) gradings at the same time.
- Session-based authentication to keep track of progress.
- Caching system that replicates the grading for already acknowledged submissions.
- Smart algorithm that only shows up to 3 (configurable) exercises at a time and progresses them based on previous exercise completion.

The project is composed of:
- PostgreSQL
  
  Database to store exercises, submissions (including their content and results) and user sessions.

- Spring Boot (Java)
  
  REST backend to handle web requests on dynamic content and database interactions. Also handles grading.

- Nginx
  
  Static pages server that distributes the frontend and pass all other non-static requests to the backend.

- Grader instance (Bash)

  Replicable docker instance that grades submissions and can be started, stopped and replicated by the backend.


## Running the application
To run the applicartion run in the root project folder (where the docker-compose.yml file is located)
```sh
docker compose up
```

The application will be running by default on http://localhost/

## Running the benchmarks
Once the application is rurnning, tests can be performed with k6 by running in the *test* folder:
```sh
k6 run <testfile.js>
```

Tests refer to:
- Get homepage: *test_homepage.js*:     
  GET request to http://\<host\>/
- Add new submission: *test_submit.js*  
  POST request to http://\<host\>/api/submissions?exerciseID=\<exerciseID\> with the submission content as payload

|           | Homepage |        |           |           | Submit    |           |           |           |
|-----------|----------|--------|-----------|-----------|-----------|-----------|-----------|-----------|
|           | Average  | Median | 95th perc | 99th perc | Average   | Median    | 95th perc | 99th perc |
| request   | 755,97   | 532,60 | 1.070,40  | 1.569,90  | 13.778,46 | 14.235,60 | 21.173,14 | 25.791,62 |
| sending   | 8,34     | 0,00   | 0,00      | 523,38    | 13,18     | 0,00      | 0,00      | 524,70    |
| waiting   | 716,06   | 530,30 | 1.066,40  | 1.345,10  | 13.715,46 | 14.215,80 | 21.123,58 | 25.656,49 |
| receiving | 31,57    | 0,00   | 518,60    | 531,20    | 49,82     | 0,00      | 523,40    | 532,10    |

## Lighthouse
Google Lighthouse's Chrome extension shows the following results for the main page:

| Performance | Accessibility | Best Practices | SEO |
|-------------|---------------|----------------|-----|
| 100         | 77            | 100            | 78  |

Average score is 89.

## Conclusions and future improvements
As shown by the data, Nginx is serving static files at incredibly high speeds, mostly thanks to the Single-page application that allows caching. 
On the other hand, exercises submissions are far slower, mostly because the requests need to be redirected from Nginx to Spring, and then authenticated before being submitted.

The frontend uses a polling system to refresh submission data and exercises, however this could be vastly improved thanks to web-sockets and more granular requests to certain elements instead of refreshing every element on the page.
