import React, { useState } from "react";
import { getMovieInfoByID, getMovieImgByID } from "../../components/Movie/MovieUtil";
// import MovieComments from './MovieComments';
// import MovieLike from './MovieLike';
import "./MovieCard.css";

function MovieCard({ sortedMovies, movies, movie, reviews }) {
  // 상위 3개만 선택
  const movieTop3 = sortedMovies.slice(0, 3);
  // console.log(movieTop3);
  // console.log(movieTop3.id);

  return (
    <>
      {movieTop3.map((movie) => (
        <div key={movie.id} className="movie-card2">
          <img src={getMovieImgByID(movie.id)} alt={movie.title} />
          <h3>{movie.title}</h3>
          <p>한줄평 {movie.reviews.length}개</p>
        </div>
      ))}
      {/* <h3>{movie.title}</h3>
      <p>{getMovieInfoByID(movie.id).desc}</p> */}
      {/* <MovieLike movie={movie} />
      <MovieComments movieId={movie.id} reviews={movieReviews} /> */}
    </>
  );
}

export default MovieCard;
