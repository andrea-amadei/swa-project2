import './ExerciseCard.scss'

function ExerciseCard({ exercise, selected, completed, onClick }) {
  return (
    <div className={"exercise" + (selected ? " selected" : "")} onClick={onClick} >
      <div className="title">
        <span className={"tag" + (completed ? " completed" : "")}>{exercise.tag}</span>
        <span> {exercise.title}</span>
      </div>
    </div>
  );
}

export default ExerciseCard;