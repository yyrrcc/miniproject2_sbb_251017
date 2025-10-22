import axios from "axios";

// 배포할 때는 배포 ip를 넣어줘야 함 (http://로컬아이피:8888/api) **
const api = axios.create({
  baseURL: "http://localhost:8888", // 스프링부트 백엔드 url
  withCredentials: true, // 세션 쿠키 전달
});

export default api;
