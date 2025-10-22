import React, { useState } from "react";
import "./MovieCard.css";
import { FaHeart, FaRegHeart } from "react-icons/fa";
import { getMovieImg } from "../../components/Movie/MovieUtil";

function MovieCard({ user, movie, onLike, onReviewSubmit }) {
  const [review, setReview] = useState("");
  const [isLiked, setIsLiked] = useState(false);

  const handleLike = () => {
    setIsLiked(!isLiked);
    onLike(movie.id);
  };

  const handleReviewSubmit = (e) => {
    e.preventDefault();
    if (!review.trim()) return;
    onReviewSubmit(movie.id, review);
    setReview("");
  };

  return (
    // <MovieCard key={movie.id} movie={movie} reviews={reviews} />
    <div className="movie-card">
      {getMovieImg.map((movie) => (
        <>
          <img src={movie.img} alt={movie.title} className="movie-poster" />

          <div className="movie-info">
            <h3 className="movie-title">{movie.title}</h3>
            <p className="movie-desc">영화설명</p>

            <div className="like-section" onClick={handleLike}>
              {isLiked ? <FaHeart className="heart liked" /> : <FaRegHeart className="heart" />}
              {/* <span>{movie.likeCount + (isLiked ? 1 : 0)}</span> */}
              <span>좋아요수</span>
            </div>
          </div>
        </>
      ))}

      <div className="review-section">
        <form onSubmit={handleReviewSubmit} className="review-form">
          <input
            type="text"
            placeholder="한줄평을 남겨보세요 ✏️"
            value={review}
            onChange={(e) => setReview(e.target.value)}
            className="review-input"
          />
          <button type="submit" className="review-btn">
            등록
          </button>
        </form>

        {/* <ul className="review-list">
          {movie.reviews?.map((r, i) => (
            <li key={i} className="review-item">
              <strong>{r.writer?.nickname || '익명'}</strong> : {r.content}
            </li>
          ))}
        </ul> */}
      </div>
    </div>
  );
}

export default MovieCard;
