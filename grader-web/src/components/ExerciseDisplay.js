import './ExerciseDisplay.scss'

function ExerciseDisplay({ exercises, selectedIndex, setSelectedIndex }) {
  
  let exercise = exercises[selectedIndex];
  
  if(exercise === undefined) {
    setSelectedIndex(0);
    exercise = exercises[0];
  }
  
  return (
    <div className="exercise-display" >
      <h3 className="title"><b>{exercise.tag}</b> {exercise.title}</h3>
      <div className="content">
        {exercise.content}
      </div>
    </div>
  );
}

export default ExerciseDisplay;