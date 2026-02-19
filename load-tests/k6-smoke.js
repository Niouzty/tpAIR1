import http from "k6/http";
import { check, sleep } from "k6";

export const options = {
  vus: 5,
  duration: "20s",
  thresholds: {
    http_req_failed: ["rate<0.05"],
    http_req_duration: ["p(95)<1000"],
  },
};

const BASE_URL = __ENV.BASE_URL || "http://localhost:8080/tpAIR1/api";

export default function () {
  const res = http.get(`${BASE_URL}/helloWorld`);
  check(res, {
    "status 200": (r) => r.status === 200,
    "json body": (r) => r.headers["Content-Type"] && r.headers["Content-Type"].includes("application/json"),
  });
  sleep(1);
}
