import './SubmissionDisplay.scss'
import {useEffect, useState} from "react";
import {postSubmission} from "../utils/fetch";

function SubmissionDisplay({ submissions, selectedExerciseIndex, selectedSubmissionIndex, runAfterPost }) {
  
  let submission = submissions[selectedSubmissionIndex];
  
  if(submission === undefined) {
    submission = submissions[0];
  }
  
  const [code, setCode] = useState(submission.content);
  
  const handleChange = (e) => {
    setCode(e.target.value);
  }
  
  useEffect(() => {
    if(submission.status !== 'NEW')
      setCode(submission.content);
  }, [submission])
  
  useEffect(() => {
    setCode(submission.content);
  }, [selectedExerciseIndex, selectedSubmissionIndex])
  
  return (
    <div className="submission-display">
      {
        (submission.status !== 'NEW' ? <></> :
            <button className="submit-button" onClick={
              () => {
                postSubmission(code, submission.exercise_id);
                setTimeout(() => runAfterPost(), 300);
              }
            }>
              Submit
            </button>
        )
      }
      <textarea value={code} onChange={handleChange} readOnly={submission.status !== 'NEW'} />
    </div>
  );
}

export default SubmissionDisplay;