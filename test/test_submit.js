import http from 'k6/http';

export const options = {
  duration: "10s",
  vus: 5,
  summaryTrendStats: ["avg", "med", "p(95)", "p(99)"],
  summaryTimeUnit: 'us'
}

function _random_string(length) {
    let result = '';
    let characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    let charactersLength = characters.length;
    for(let i = 0; i < length; i++) {
        result += characters.charAt(Math.floor(Math.random() * charactersLength));
    }
    return result;
}

export default function () {
  const payload = _random_string(8);
  const exerciseID = Math.floor(Math.random() * 10) + 1;
  const params = {headers: {'Authorization': 'x'}};

  http.post('http://localhost/api/submissions?exerciseID=' + exerciseID, payload, params);
}