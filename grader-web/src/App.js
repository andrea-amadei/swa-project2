import './App.scss'
import Header from "./components/Header";
import ExerciseBar from "./components/ExerciseBar";
import {useEffect, useState} from "react";
import SubmissionBar from "./components/SubmissionBar";
import ExerciseDisplay from "./components/ExerciseDisplay";
import SubmissionDisplay from "./components/SubmissionDisplay";
import {getExercises, getSubmissions, rawSubmissionsToStructuredSubmissions} from "./utils/fetch";

function App() {
  
  const [exerciseLoadingDone, setExerciseLoadingDone] = useState(false);
  const [submissionLoadingDone, setSubmissionLoadingDone] = useState(false);
  
  const [exercises, setExercises] = useState([{exercise_id: 0}]);
  const [rawSubmissions, setRawSubmissions] = useState(null);
  const [submissions, setSubmissions] = useState([[{ submission_id: 0, status: 'TEST'}]])
  
  useEffect(() => {
    getExercises(setExercises, setExerciseLoadingDone);
    getSubmissions(setRawSubmissions, setSubmissionLoadingDone);
  }, []);
  
  useEffect(() => {
    if(exerciseLoadingDone && submissionLoadingDone)
      setSubmissions(rawSubmissionsToStructuredSubmissions(exercises, rawSubmissions));
  }, [exerciseLoadingDone, submissionLoadingDone]);
  
  const [selectedExerciseIndex, setSelectedExerciseIndex] = useState(0);
  const [selectedSubmissionIndex, setSelectedSubmissionIndex] = useState(0);
  
  useEffect(() => setSelectedSubmissionIndex(0), [selectedExerciseIndex]);
  
  useEffect(() => {
    setInterval(() => {
      setExerciseLoadingDone(false);
      setSubmissionLoadingDone(false);
      getExercises(setExercises, setExerciseLoadingDone);
      getSubmissions(setRawSubmissions, setSubmissionLoadingDone);
    }, 5000);
  }, []);
  
  return (
    <div className="app">
      <Header token={localStorage.getItem('session_token')} />
      <ExerciseBar
        exercises={exercises}
        selectedIndex={selectedExerciseIndex}
        setSelectedIndex={setSelectedExerciseIndex}
      />
      <SubmissionBar
        submissions={submissions[selectedExerciseIndex]}
        selectedIndex={selectedSubmissionIndex}
        setSelectedIndex={setSelectedSubmissionIndex}
      />
      
      <ExerciseDisplay
        exercises={exercises}
        selectedIndex={selectedExerciseIndex}
        setSelectedIndex={setSelectedExerciseIndex}
      />
      <SubmissionDisplay
        submissions={submissions[selectedExerciseIndex]}
        selectedExerciseIndex={selectedExerciseIndex}
        selectedSubmissionIndex={selectedSubmissionIndex}
        runAfterPost={() => {
          setSubmissionLoadingDone(false);
          getSubmissions(setRawSubmissions, setSubmissionLoadingDone);
        }}
      />
    </div>
  );
}

export default App;
