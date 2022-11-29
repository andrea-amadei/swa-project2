import './ExerciseBar.scss'
import ExerciseCard from "./ExerciseCard";

function ExerciseBar({ exercises, selectedIndex, setSelectedIndex }) {
  return (
    <div className="exercises">
      <h3 className="title">Exercises</h3>
      {
        exercises.filter(x => x.visible).map((x, i) =>
          <ExerciseCard
            key={x.exercise_id}
            exercise={x}
            selected={i === selectedIndex}
            completed={x.completed}
            onClick={() => setSelectedIndex(i)}
          />)
      }
    </div>
  );
}

export default ExerciseBar;