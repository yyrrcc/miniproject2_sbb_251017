import React, { useContext, useState } from 'react';
import './Signup.css';
import api from '../../config/axiosConfig';
import { useNavigate } from 'react-router-dom';

function Signup() {
  const navigate = useNavigate();
  const [errors, setErrors] = useState({});

  // 회원 정보를 배열 (구조분해할당)으로 받기
  const [form, setForm] = useState({
    username: '',
    password: '',
    name: '',
    email: '',
    // confirm: '',
  });

  // input의 값들을 구조분해할당으로 값을 set하기
  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErrors({});
    // if (form.password !== form.confirm) {
    //   alert('비밀번호가 일치하지 않습니다!');
    //   return;
    // }
    try {
      // console.log(form); // **확인용
      await api.post('/api/auth/signup', { ...form });
      alert('회원 가입 성공');
      navigate('/login', { replace: true });
    } catch (error) {
      if (error.response && error.response.status === 400) {
        setErrors(error.response.data); // 유효성 검증 실패 시
      } else {
        alert('회원 가입 실패');
        console.log(error);
      }
    }
  };

  return (
    <div className="signup-container">
      <h2>회원가입</h2>
      <form onSubmit={handleSubmit} className="signup-form">
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
        <input type="text" name="name" placeholder="이름" value={form.name} onChange={handleChange} required />
        <input type="email" name="email" placeholder="이메일" value={form.email} onChange={handleChange} required />
        {/* <input
          type="password"
          name="confirm"
          placeholder="비밀번호 확인"
          value={form.confirm}
          onChange={handleChange}
          required
        /> */}
        {errors.idError && <p className="error-message">{errors.idError}</p>}
        {errors.username && <p className="error-message">{errors.username}</p>}
        {errors.password && <p className="error-message">{errors.password}</p>}
        {errors.name && <p className="error-message">{errors.name}</p>}
        {/* 이메일 중복 검사하고 싶음 */}
        {errors.email && <p className="error-message">{errors.email}</p>}
        <button type="submit">회원가입</button>
      </form>
    </div>
  );
}

export default Signup;
