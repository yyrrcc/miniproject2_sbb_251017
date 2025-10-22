import React, { useContext, useEffect, useState } from "react";
import "./MovieCard_backup.css";
import { FaHeart, FaRegHeart } from "react-icons/fa";
import { getMovieInfoByID } from "../../components/Movie/MovieUtil";
import api from "../../config/axiosConfig";
import MovieReviews from "./MovieReviews";
import { UserContext } from "../../context/UserContext";

function MovieCard() {
  // const [comments, setComments] = useState({});
  const user = useContext(UserContext);
  const [errors, setErrors] = useState(null); // 에러
  const [movies, setMovies] = useState([]); // 영화
  const [reviews, setReviews] = useState([]); // 기존 한줄평

  const [likedMovies, setLikedMovies] = useState({}); // 영화 찜하기
  const [liked, setLiked] = useState(false);

  // // 모든 한줄평 목록 불러오기
  // const loadMovieReviews = async () => {
  //   try {
  //     const res = await api.get("/api/movies/reviews");
  //     setReviews(res.data);
  //     console.log("모든 한줄평", res.data);
  //   } catch (error) {
  //     setErrors(error);
  //   }
  // };

  // 모든 영화 정보 가져오기
  const loadMovies = async () => {
    try {
      const res = await api.get("/api/movies");
      setMovies(res.data);
      console.log("영화정보", res.data);
    } catch (error) {
      setErrors(error);
    }
  };

  // // 영화 찜하기 개수 가져오기
  // const loadMoviesLike = async (movidId) => {
  //   try {
  //     const res = await api.get(`/api/movies/${movidId}/like`);
  //     setLikedMovies(res.data);
  //     console.log("찜하기 콘솔", res.data);
  //     // console.log('영화기본키', res.data[0].id);
  //     // console.log('영화제목', res.data[0].title);
  //     // console.log('좋아요수', res.data[0].likeCount);
  //   } catch (error) {
  //     setErrors(error);
  //   }
  // };

  // 찜하기 (찜하기 버튼을 눌렀을 때 -> api로 찜하기 숫자 올리기)
  const toggleLike = () => {
    setLiked(!liked);
    // loadMovies();
  };

  // 첫 마운팅 됐을 때 영화 정보, 한줄평 가져오기
  useEffect(() => {
    loadMovies();
  }, []);

  // const toggleLike = (id) => {
  //   setLikedMovies((prev) => ({
  //     ...prev,
  //     [id]: !prev[id],
  //   }));
  // };

  return (
    <div className="movie-content">
      {movies.map((movie) => (
        <div key={movie.id} className="movie-card">
          <img src={getMovieInfoByID(movie.id).img} alt={movie.title} className="movie-poster" />

          <div className="movie-info">
            <h3 className="movie-title">{movie.title}</h3>
            <p className="movie-desc">{getMovieInfoByID(movie.id).desc}</p>

            {/* 찜하기 버튼 */}
            <div className="movie-actions">
              {/* 찜하기 숫자 : {movie.likeCount}
              <button className={`like-button ${liked ? "liked" : ""}`} onClick={toggleLike}>
                {liked ? <FaHeart /> : <FaRegHeart />} 찜하기
              </button> */}
              {/* <div
                className={`like-display ${likedMovies[movie.id] ? "liked" : ""}`}
                onClick={() => toggleLike(movie.id)}
              >
                {likedMovies[movie.id] ? <FaHeart /> : <FaRegHeart />}
                <span> {likedMovies[movie.id] ? movie.likes + 1 : movie.likes}</span>
              </div> */}
            </div>

            <MovieReviews movie={movie} movieId={movie.id} loadMovies={loadMovies} />
          </div>
        </div>
      ))}
    </div>
  );
}

export default MovieCard;
