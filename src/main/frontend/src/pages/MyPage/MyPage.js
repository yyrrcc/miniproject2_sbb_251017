import React, { useContext, useEffect, useState } from 'react';
import './MyPage.css';
import { UserContext } from '../../context/UserContext';
import api from '../../config/axiosConfig';
// import MovieCard from '../../components/MovieCard';

function MyPage() {
  const user = useContext(UserContext); // 로그인한 회원의 아이디
  const [errors, setErrors] = useState(null);
  const [userInfo, setUserInfo] = useState({}); // 기존 정보
  const [updatedUser, setUpdatedUser] = useState({
    username: user,
    name: '',
    password: '',
    email: '',
  }); // 수정할 정보

  // 기존 회원 정보 불러오기
  const loadUser = async () => {
    try {
      const res = await api.get('/api/auth/mypage');
      setUserInfo(res.data);
    } catch (error) {
      setErrors(error);
      alert('정보를 불러올 수 없음');
      console.log(error);
    }
  };
  useEffect(() => {
    loadUser();
  }, [updatedUser]);

  const handleChange = (e) => {
    setUpdatedUser({ ...updatedUser, [e.target.name]: e.target.value });
  };

  // 회원 정보 수정
  const handleSubmit = async (e) => {
    e.preventDefault();
    setErrors(null);
    try {
      await api.post('/api/auth/mypage', { ...updatedUser });
      // input을 빈칸으로 만들고, user는 그대로 유지 시켜야 하고, 리렌더 시켜야 함
      setUpdatedUser({ username: user, name: '', password: '', email: '' });
      alert('회원정보가 수정되었습니다.');
    } catch (error) {
      if (error.response && error.response.status === 400) {
        setErrors(error.response.data);
        console.log(error);
      } else {
        alert('수정 실패');
        console.log(error);
      }
    }
  };

  return (
    <div className="mypage-container">
      <h2>My Page</h2>
      {errors && <p className="error-message">에러가 있어요.</p>}
      <form className="profile-form" onSubmit={handleSubmit}>
        <label>아이디</label>
        <input type="text" name="username" value={updatedUser.username} disabled />

        <label>변경 할 비밀번호</label>
        <input type="text" name="password" value={updatedUser.password} onChange={handleChange} required />

        <label>변경 할 이름</label>
        <input
          type="text"
          name="name"
          value={updatedUser.name}
          onChange={handleChange}
          placeholder={userInfo.name}
          required
        />

        <label>변경 할 이메일</label>
        <input
          type="email"
          name="email"
          value={updatedUser.email}
          onChange={handleChange}
          placeholder={userInfo.email}
          required
        />

        <button type="submit">정보 수정</button>
      </form>

      <h3>내가 찜한 영화</h3>
      {/* <div className="mypage-movies">
        {movies.map((movie) => (
          <MovieCard key={movie.id} movie={movie} />
        ))}
      </div> */}
    </div>
  );
}

export default MyPage;
