import { use, useEffect, useState } from "react";
import api from "../../config/axiosConfig";
import { useNavigate } from "react-router-dom";

function MovieReviews({ movie, loadMovies }) {
  const navigate = useNavigate();
  const [errors, setErrors] = useState(null);
  const [review, setReview] = useState(""); // 새로운 한줄평

  // 한줄평 등록하기
  const handleReviewSubmit = async (e, movieId) => {
    e.preventDefault();
    setErrors(null);
    try {
      await api.post(`/api/movies/${movieId}/reviews`, { content: review });
      alert("한줄평이 등록되었습니다");
      setReview("");
      loadMovies(); // 리렌더를 위함
    } catch (error) {
      if (error.response && error.response.status === 403) {
        alert("로그인 후 작성하세요");
        navigate("/login", { replace: true });
        return;
      }
      if (error.response && error.response.status === 400) {
        setErrors(error.response.data);
        alert(error.response.data.content); // 에러창 **
      } else {
        alert("한줄평 등록 실패");
        console.log(error);
      }
    }
  };

  return (
    <>
      {/* 한줄평 입력폼 */}
      <form className="comment-form" onSubmit={(e) => handleReviewSubmit(e, movie.id)}>
        <input
          type="text"
          placeholder="한줄평을 입력하세요"
          value={review}
          onChange={(e) => {
            setReview(e.target.value);
          }}
        />
        <button type="submit">등록</button>
      </form>

      {/* 한줄평 리스트 - 새로운 방법*/}
      <div className="comment-list">
        {movie.reviews.length > 0 ? (
          movie.reviews.map((review) => (
            <div key={review.id} className="comment-item">
              <span className="comment-user">{review.writer.username}</span>
              <p className="comment-content">{review.content}</p>
            </div>
          ))
        ) : (
          <p className="no-comment">아직 등록된 한줄평이 없습니다.</p>
        )}
      </div>
      {/* 한줄평 리스트 - 기존 방법 */}
      {/* {reviews
              .filter((review) => review.movieId === movie.id)
              .map((review) => (
                <p key={review.id} className="comment-item">
                  💬 {review.writerId}: {review.content}
                </p>
              ))} */}
    </>
  );
}
export default MovieReviews;
