import React, { useEffect, useState } from 'react';
import './Signup.css';
import api from '../../config/axiosConfig';
import { useNavigate } from 'react-router-dom';

function Signup({ onLogin, user }) {
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

  // 회원 정보 수정을 위한 state 선언
  const [updatedUser, setUpdatedUser] = useState({}); // 회원정보 수정
  const [isEdit, setIsEdit] = useState(false); // 회원정보 수정

  useEffect(() => {
    if (user !== null) {
      setIsEdit(true);
      setForm({ username: user, password: '', name: user.name, email: user.email });
    } else {
      // else 부분은 꼭 해줘야 하나?
      setIsEdit(false);
      setForm({});
    }
  }, [user]);

  // input의 값들을 구조분해할당으로 값을 set하기
  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    // if (form.password !== form.confirm) {
    //   alert('비밀번호가 일치하지 않습니다!');
    //   return;
    // }
    try {
      console.log(form); // **확인용
      await api.post('/api/auth/signup', { ...form });
      alert('회원 가입 성공');
      navigate('/login', { replace: true });
    } catch (error) {
      if (error.response && error.response.statue === 400) {
        setErrors(error.response.data); // 동일 아이디 존재 시
      } else {
        alert('회원 가입 실패');
      }
    }
  };

  console.log(user);

  return (
    <div className="signup-container">
      <h2>{isEdit ? '회원정보 수정' : '회원가입'}</h2>
      로그인한 아이디 {user}/ 로그인한 이름 {user}/ 폼 아이디 {form.username}
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
        <button type="submit">회원가입</button>
      </form>
    </div>
  );
}

export default Signup;
