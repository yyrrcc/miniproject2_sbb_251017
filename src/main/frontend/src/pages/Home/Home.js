import React from "react";
import "./Home.css";
import MovieList from "./../MovieDummy/MovieList";

function Home() {
  return (
    <div className="home-container">
      <section className="hero">
        <div className="hero-content">
          <h1>CineTalk 🎬</h1>
          <p>좋아하는 영화를 찜하고, 함께 이야기를 나눠보세요!</p>
          <button className="cta-btn">지금 시작하기</button>
        </div>
      </section>

      <section className="movie-preview">
        <h2>인기 영화 TOP 3</h2>
        <MovieList />
      </section>
    </div>
  );
}

export default Home;
