import React, { useState, useEffect } from "react";
import { useParams, Link, useNavigate } from "react-router-dom";
import "./BoardDetail.css";
import api from "../../config/axiosConfig";

function BoardDetail({ user }) {
  const { id } = useParams(); // url 존재하는 id 값 받아오기
  const navigate = useNavigate();
  const [post, setPost] = useState(null);
  const [comment, setComment] = useState(""); // 댓글 관련
  const [comments, setComments] = useState([]); // 댓글 관련

  // 글 상세보기
  const loadPost = async () => {
    try {
      const res = await api.get(`/api/board/${id}`);
      setPost(res.data);
    } catch (error) {
      console.log("에러", error);
    }
  };
  useEffect(() => {
    loadPost();
  }, [id]);

  // 글 삭제하기
  const handleDelete = async () => {
    if (!window.confirm("정말 삭제할까요?")) {
      return;
    }
    try {
      await api.delete(`/api/board/${id}`);
      navigate("/board", { replace: true });
    } catch (error) {
      if (error.response.status === 403) {
        alert("삭제 할 권한이 없습니다.");
      } else {
        alert("글 삭제 실패");
      }
    }
  };

  // 댓글 관련
  const handleComment = (e) => {
    e.preventDefault();
    if (!comment.trim()) return;
    setComments([...comments, comment]);
    setComment("");
  };

  // 날짜시간 포맷팅
  const formattedDate = (dateString) => {
    return new Date(dateString).toLocaleString();
  };

  if (!post) return <p className="error-message">로딩 중...</p>;

  return (
    <div className="detail-container">
      <div className="detail-box">
        <h2>{post.title}</h2>
        <div className="detail-info">
          <span>작성자: {post.writer.username}</span>
          {/* 작성일과 수정일을 어떻게 알 수 있지? */}
          <span>작성일: {formattedDate(post.createdAt)}</span>
          <span>수정일: {formattedDate(post.updatedAt)}</span>
        </div>
        <p className="detail-content">{post.content}</p>

        <div className="detail-buttons">
          <Link to="/board" className="back-btn">
            목록
          </Link>
          {/* 수정 시 링크를 edit 넣는 게 좋을지 아닐지 모르겠음 */}
          {user === post.writer.username && (
            <>
              <Link to={`/board/edit/${id}`} className="edit-btn">
                수정
              </Link>
              <div onClick={handleDelete} className="edit-btn">
                삭제
              </div>
            </>
          )}
        </div>
      </div>

      <div className="comment-section">
        <h3>댓글</h3>
        <ul>
          {comments.map((c, idx) => (
            <li key={idx}>{c}</li>
          ))}
        </ul>

        <form onSubmit={handleComment} className="comment-form">
          <input
            type="text"
            placeholder="댓글을 입력하세요"
            value={comment}
            onChange={(e) => setComment(e.target.value)}
          />
          <button type="submit">등록</button>
        </form>
      </div>
    </div>
  );
}

export default BoardDetail;
