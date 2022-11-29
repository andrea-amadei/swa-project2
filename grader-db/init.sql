SET TIME ZONE 'UTC';

CREATE TABLE exercises (
    exercise_ID SERIAL NOT NULL PRIMARY KEY,
    "tag" TEXT NOT NULL,
    title TEXT NOT NULL,
    "content" TEXT NOT NULL
);

CREATE TABLE user_sessions (
    session_ID TEXT NOT NULL PRIMARY KEY,
    creation_time TIMESTAMP NOT NULL,
    expiration_time TIMESTAMP NOT NULL,
    new_session BOOLEAN NOT NULL,
    super_user BOOLEAN NOT NULL
);

CREATE TABLE submissions (
    submission_ID SERIAL NOT NULL PRIMARY KEY,
    exercise_ID INTEGER NOT NULL REFERENCES exercises(exercise_ID)
		ON UPDATE CASCADE ON DELETE CASCADE,
    session_ID TEXT NOT NULL REFERENCES user_sessions(session_ID)
		ON UPDATE CASCADE ON DELETE CASCADE,

    "content" TEXT NOT NULL DEFAULT '',
    submission_time TIMESTAMP NOT NULL DEFAULT NOW(),

    status TEXT NOT NULL DEFAULT 'SUBMITTED',
    result TEXT DEFAULT NULL,
    result_time TIMESTAMP DEFAULT NULL
);

CREATE OR REPLACE FUNCTION _renew_or_create_user_session(old_session_id TEXT, new_user_length INTEGER) RETURNS TEXT
AS $$
    DECLARE
        new_session_id TEXT;
        existing_rows user_sessions%ROWTYPE;
    BEGIN
        SELECT * FROM user_sessions INTO existing_rows WHERE session_ID = old_session_id;

        IF existing_rows IS NOT NULL AND existing_rows.expiration_time > NOW() THEN
            UPDATE user_sessions SET new_session = false WHERE session_ID = old_session_id;

            IF existing_rows.expiration_time < NOW() + INTERVAL '30 day' THEN
                UPDATE user_sessions SET expiration_time = NOW() + INTERVAL '30 day' WHERE session_ID = old_session_id;
            END IF;

            RETURN existing_rows.session_ID;
        END IF;

        LOOP
            new_session_id := LOWER(SUBSTRING(MD5(''||NOW()::TEXT||RANDOM()::TEXT) FOR new_user_length));
            BEGIN
                INSERT INTO user_sessions (session_ID, creation_time, expiration_time, new_session, super_user)
                    VALUES (new_session_id, NOW(), NOW() + INTERVAL '30 day', true, false);

                RETURN new_session_id;
            EXCEPTION WHEN unique_violation THEN

            END;
        END LOOP;

    END;
$$ LANGUAGE PLPGSQL;

CREATE OR REPLACE FUNCTION renew_user_session(old_session_id TEXT) RETURNS SETOF user_sessions
AS $$
    DECLARE
        new_session_id TEXT;
    BEGIN
        new_session_id := _renew_or_create_user_session(old_session_id, 32);
        RETURN QUERY (SELECT * FROM user_sessions WHERE session_ID = new_session_id);
    END;
$$ LANGUAGE PLPGSQL;

CREATE OR REPLACE FUNCTION add_submission(this_session_ID TEXT, this_exercise_ID INTEGER, this_content TEXT) RETURNS SETOF submissions
AS $$
    DECLARE
        new_submission_ID INTEGER;
    BEGIN
        INSERT INTO submissions (exercise_ID, session_ID, "content")
        VALUES (this_exercise_ID, this_session_ID, this_content)
        RETURNING submission_ID INTO new_submission_ID;

        RETURN QUERY (SELECT * FROM submissions WHERE submission_ID = new_submission_ID);
    END;
$$ LANGUAGE PLPGSQL;

CREATE OR REPLACE FUNCTION update_submission_status(this_submission_id INTEGER, new_status TEXT) RETURNS SETOF submissions
AS $$
    BEGIN
        UPDATE submissions SET status = new_status WHERE submission_ID = this_submission_id;

        RETURN QUERY (SELECT * FROM submissions WHERE submission_ID = this_submission_id);
    END;
$$ LANGUAGE PLPGSQL;

CREATE OR REPLACE FUNCTION set_submission_result(this_submission_id INTEGER, new_result TEXT) RETURNS SETOF submissions
AS $$
    BEGIN
        UPDATE submissions
        SET status = 'DONE', result = new_result, result_time = NOW()
        WHERE submission_ID = this_submission_id;

        RETURN QUERY (SELECT * FROM submissions WHERE submission_ID = this_submission_id);
    END;
$$ LANGUAGE PLPGSQL;

CREATE OR REPLACE PROCEDURE clean_inactive_sessions()
AS $$
    BEGIN
        DELETE FROM user_sessions WHERE new_session = true AND (creation_time + INTERVAL '5 minutes') < NOW();
    END;
$$ LANGUAGE PLPGSQL;


INSERT INTO user_sessions (session_ID, creation_time, expiration_time, new_session, super_user)
VALUES ('b105ea9e0e3ea40370e298a6a008e1f9', NOW(), '2100-01-01 12:00:00', false, true);

INSERT INTO exercises (exercise_id, tag, title, content)
VALUES  (1, '1.1', 'Sum of three values', 'Write a function int sum(int first, int second, int third) that returns the sum of the given integers. As an example, the function call sum(1, 2, 3) should return the value 6.'),
        (2, '1.2', 'Sum with formula', 'Write a function String sumWithFormula(int first, int second) that returns the written out sum of the given integers and the sum. As an example, the function call sumWithFormula(1, 2) should return the string 1+2=3 and the function call sumWithFormula(1, 1) should return the string 1+1=2. Note! Do not add spaces to the returned string.'),
        (3, '1.3', 'Budget check', 'Write a function String budgetCheck(double budget, double currentSpending) that returns information on whether a given budget is in order in light of given spending. If the value of currentSpending is larger than the value of budget, the function should return "Budget: Overspending". Otherwise, the function should return "Budget: OK".'),
        (4, '1.4', 'Mystery function', 'Write a function String mystery(int number) that returns a string based on the number. If the number is divisible by 5, the function should return the string "mys". If the number is divisible by 7, the function should return the string "tery". If the number is divisible by both 5 and 7, the function should return the string "mystery". Otherwise, the function should return a string representation of the given number, e.g. if the number is 1, the function should return "1".'),
        (5, '1.5', 'Sum of negative numbers', 'Write a function int sumOfNegatives(List<int> numbers) that returns the sum of the negative numbers in the given list. For example, if the numbers list would contain the numbers -1, 2, -4, the function should return the value -5.'),
        (6, '1.6', 'Average of positives', 'Write a function double averageOfPositives(List<int> numbers) that returns the average value of the positive numbers on the list. If there are no positive values, the function should return the value -1.'),
        (7, '2.1', 'Team', 'Create a class Team and implement the two following constructors (and necessary properties) to the class. The default constructor should have three properties: (1) the name of the team (String), (2) the home town of the team (String), and (3) the year the team was formed (int). The named constructor nameAndYear should have two properties: (1) the name of the team (String) and (2) the year the team was formed (int). In the case of the named constructor, the home town of the team must be "Unknown".
Once completed, add a toString method to the class which leads to outputs outlined by the following examples.

final hjk = Team("HJK", "Helsinki", 1907);
print(hjk);
final secret = Team.nameAndYear("Secret", 1984);
print(secret);

With the code above, the output should be as follows.

HJK (Helsinki, 1907)
Secret (Unknown, 1984)'),
        (8, '2.2', 'Video and playlist', 'Implement the classes Video and Playlist as follows. The class Video should have a name (String), a duration in seconds (int), a constructor with named arguments, and a toString method. The default name should be "Unknown" and the default length should be 0. The class should work as follows.

print(Video(name: "One second clip", duration: 1));
print(Video(name: "Hello again!", duration: 84));

With the code above, the output should be as follows.

One second clip (1 second)
Hello again! (84 seconds)

The class Playlist should contain a list of videos, provide a default (no argument) constructor, and offer the following methods: (1) void add(Video video) that adds a video to the playlist, (2) bool has(String name) that returns true if the list of videos contains a video with the given name, and (3) int duration() that returns the sum of durations of the videos in the playlist. The class should work as follows.

final playlist = Playlist();
print(playlist.has("One second clip"));
print(playlist.duration());
playlist.add(Video(name: "One second clip", duration: 1));
playlist.add(Video(name: "Hello again!", duration: 84));
print(playlist.has("One second clip"));
print(playlist.duration());

With the code above, the output should be as follows.

false
0
true
85');