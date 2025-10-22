import React, { useContext, useEffect, useState } from 'react';
import MovieCard from './MovieCard';
import api from '../../config/axiosConfig';
import { UserContext } from '../../context/UserContext';

function MovieList() {
  const user = useContext(UserContext);
  const [errors, setErrors] = useState(null);
  const [movies, setMovies] = useState([]); // 영화

  const loadMovies = async () => {
    try {
      const res = await api.get('/api/movies');
      setMovies(res.data);
      // console.log('데이터', res.data);
      // console.log('영화기본키', res.data[0].id);
      // console.log('영화제목', res.data[0].title);
      // console.log('좋아요수', res.data[0].likeCount);
    } catch (error) {
      setErrors(error);
    }
  };
  useEffect(() => {
    loadMovies();
  }, []);

  // 리뷰가 많은 순서대로 정렬된 새 배열 (내림차순)
  const sortedMovies = [...movies].sort((a, b) => b.reviews.length - a.reviews.length);

  return (
    <div className="movie-list">
      <MovieCard movies={movies} sortedMovies={sortedMovies} />
      {/* {movies.map((movie) => (
        <MovieCard key={movie.id} movie={movie} reviews={movie.reviews} sortedMovies={sortedMovies} />
      ))} */}
    </div>
  );
}

export default MovieList;
