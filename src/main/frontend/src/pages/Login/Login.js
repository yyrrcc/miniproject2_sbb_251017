import React, { useState } from "react";
import "./Login.css";
import { Link, useNavigate } from "react-router-dom";
import api from "../../config/axiosConfig";

function Login({ onLogin }) {
  const navigate = useNavigate();
  const [errors, setErrors] = useState({});

  const [form, setForm] = useState({ username: "", password: "" });

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      // 폼 데이터 방식이라 URLSearchParams로 전송
      await api.post("/api/auth/login", new URLSearchParams({ ...form }));
      // 현재 로그인한 사용자 정보 가져오기
      const res = await api.get("/api/auth/me");
      alert("로그인 성공");
      // 로그인한 정보를 보내줘야 하는건가? **
      onLogin(res.data.username);
      navigate("/", { replace: true });
    } catch (error) {
      setErrors(error.response.data); // 로그인 실패 시 Unauthorized
    }
  };

  return (
    <div className="login-container">
      <h2>로그인</h2>
      <form onSubmit={handleSubmit} className="login-form">
        <input
          type="text"
          name="username"
          placeholder="아이디"
          value={form.username}
          onChange={handleChange}
          required
        />
        <input
          type="password"
          name="password"
          placeholder="비밀번호"
          value={form.password}
          onChange={handleChange}
          required
        />
        {/* 에러 메시지 출력 어떻게 해야함? ** */}
        {!errors && <p className="error-message">아이디 또는 비밀번호가 틀렸습니다.</p>}
        <button type="submit">로그인</button>
      </form>
      <p className="signup-link">
        계정이 없으신가요? <Link to={"/signup"}>회원가입</Link>
      </p>
    </div>
  );
}

export default Login;
