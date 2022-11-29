import './SubmissionCard.scss'

function SubmissionCard({ submission, selected, onClick }) {
  
  const datetime = new Date(submission.submission_at)
  let date = datetime.toLocaleDateString(undefined,{dateStyle: 'short'});
  let time = datetime.toLocaleTimeString();
  
  let status = submission.status;
  if(status === 'DONE')
    status = submission.result;
  
  if(status === 'NEW') {
    date = 'Now'
    time = ''
  }
  
  return (
    <div className={"submission" + (selected ? " selected" : "")} onClick={onClick} >
      <div className="date">
        <div className="date-row">{date}</div>
        <div className="date-row">{time}</div>
      </div>
      <div className={"score " + status.toLowerCase()}>{status}</div>
    </div>
  );
}

export default SubmissionCard;