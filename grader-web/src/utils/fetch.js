export const BASE_ADDRESS = ''

function getToken() {
  let sessionToken = localStorage.getItem('session_token');
  
  if(sessionToken == null)
    sessionToken = 'x';
  
  return sessionToken;
}

function checkIfTokenIsDifferent(sentToken) {
  let localToken = localStorage.getItem('session_token');
  
  if(localToken !== sentToken) {
    localStorage.removeItem('session_token');
    window.location.reload();
  }
}

export const getExercises = (setExercises, setDone) => {
  
  fetch(BASE_ADDRESS + '/api/exercises', {
    method: 'GET',
    headers: {'Authorization': getToken()}
  })
  .then(data => data.json())
  .catch(error => console.log(error))
  
    .then(response => {
      checkIfTokenIsDifferent(response.session.session_token);
      localStorage.setItem('session_token', response.session.session_token);
      
      setExercises(response.response);
      
      setDone(true);
    });
}

export const getSubmissions = (setSubmissions, setDone) => {
  
  fetch(BASE_ADDRESS + '/api/submissions', {
    method: 'GET',
    headers: {'Authorization': getToken()}
  })
    .then(data => data.json())
    .catch(error => console.log(error))
    
    .then(response => {
      checkIfTokenIsDifferent(response.session.session_token);
      localStorage.setItem('session_token', response.session.session_token);
      
      setSubmissions(response.response);
  
      setDone(true);
    });
}

export const postSubmission = (submission, exerciseID) => {
  fetch(BASE_ADDRESS + '/api/submissions?exerciseID=' + exerciseID, {
    method: 'POST',
    headers: {'Authorization': getToken()},
    body: submission
  })
    .then(data => data.json())
    .catch(error => console.log(error))
    
    .then(response => {
      checkIfTokenIsDifferent(response.session.session_token);
      localStorage.setItem('session_token', response.session.session_token);
    });
}

export const rawSubmissionsToStructuredSubmissions = (exercises, rawSubmissions) => {
  const conversionMap = {};
  let submissions = [];
  
  exercises.forEach((x, i) => {
    conversionMap[x.exercise_id] = i;
    submissions.push([]);
  });
  
  rawSubmissions.forEach(x => {
    submissions[conversionMap[x.exercise_id]].push(x);
  });
  
  submissions.forEach((x, i) => {
    if(x.length === 0 || x[0].status === 'DONE')
      submissions[i].unshift({
        submission_id: -1,
        content: "Write your submission here",
        exercise_id: exercises[i].exercise_id,
        result: null,
        result_at: null,
        status: "NEW",
        submission_at: "",
      })
  });
  
  return submissions;
}