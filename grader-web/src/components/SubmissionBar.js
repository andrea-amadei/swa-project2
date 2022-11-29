import './SubmissionBar.scss'
import SubmissionCard from "./SubmissionCard";

function SubmissionBar({ submissions, selectedIndex, setSelectedIndex }) {
  return (
    <div className="submissions">
      <h3 className="title">Submissions</h3>
      {
        submissions.map((x, i) =>
          <SubmissionCard
            key={x.submission_id}
            submission={x}
            selected={i === selectedIndex}
            onClick={() => setSelectedIndex(i)}
          />)
      }
    </div>
  );
}

export default SubmissionBar;