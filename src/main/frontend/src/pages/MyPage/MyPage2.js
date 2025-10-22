import React from 'react';
import './MyPage.css';

function MyPage({ user, likedMovies }) {
  return (
    <div className="mypage-container">
      <h2>마이페이지</h2>
      <div className="user-info">
        <h3>회원 정보</h3>
        <p>
          <strong>이름:</strong> {user.name}
        </p>
        <p>
          <strong>이메일:</strong> {user.email}
        </p>
      </div>

      <div className="liked-movies">
        <h3>내가 찜한 영화 🎬</h3>
        <div className="movie-grid">
          {likedMovies && likedMovies.length > 0 ? (
            likedMovies.map((movie) => (
              <div key={movie.id} className="liked-movie-card">
                <img src={movie.posterUrl} alt={movie.title} />
                <p>{movie.title}</p>
              </div>
            ))
          ) : (
            <p className="no-movies">아직 찜한 영화가 없습니다 😢</p>
          )}
        </div>
      </div>
    </div>
  );
}

export default MyPage;
