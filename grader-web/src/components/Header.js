import './Header.scss'

function Header({ token }) {
  return (
    <div className="header">
      <h2>Grader</h2>
      <div><b>Session token:</b> {token}</div>
    </div>
  );
}

export default Header;