import http from 'k6/http';

export const options = {
  duration: "10s",
  vus: 5,
  summaryTrendStats: ["avg", "med", "p(95)", "p(99)"],
  summaryTimeUnit: 'us'
}

export default function () {
  http.get('http://localhost/');
}