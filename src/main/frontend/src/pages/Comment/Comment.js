import "./Comment.css";
import { useEffect, useState } from "react";
import api from "../../config/axiosConfig";
import { useNavigate } from "react-router-dom";

function Comment({ boardId, user }) {
  const navigate = useNavigate();
  const [errors, setErrors] = useState(null); // 에러

  const [comment, setComment] = useState(""); // 새 댓글
  const [comments, setComments] = useState([]); // 기존 모든 댓글들
  const [isEdit, setIsEdit] = useState(false); // 댓글 수정 여부
  const [isEditComment, setisEditComment] = useState(null); // 수정할 댓글 선택
  const [editComment, setEditComment] = useState(null); // 수정한 댓글

  // 게시글에 존재하는 댓글 가져오기
  const loadComments = async () => {
    try {
      const res = await api.get(`/api/comments/${boardId}`);
      setComments(res.data);
    } catch (error) {
      setErrors(error);
    }
  };
  useEffect(() => {
    loadComments();
  }, [boardId]);

  // 게시글에 댓글 작성하기 (api.post 해주고 dto에 존재하는 content의 값이 comment로 간다고 명시해줘야 함)
  const handleComment = async (e) => {
    e.preventDefault();
    setErrors(null);
    try {
      await api.post(`/api/comments/${boardId}`, { content: comment });
      alert("댓글이 등록되었습니다");
      setComment("");
      loadComments();
    } catch (error) {
      if (error.response && error.response.status === 403) {
        alert("로그인 후 작성하세요");
        navigate("/login", { replace: true });
        return;
      }
      if (error.response && error.response.status === 400) {
        setErrors(error.response.data);
        console.log("400에러", error.response.data);
      } else {
        alert("실패");
        console.log(error);
      }
    }
  };

  // 댓글 수정
  const handleCommentEdit = async (commentId) => {
    setErrors(null);
    try {
      await api.put(`/api/comments/${commentId}`, { content: editComment });
      loadComments();
    } catch (error) {
      if (error.response && error.response.status === 403) {
        setErrors(error.response.data);
        console.log("수정403에러", error.response.data);
        alert("로그인 후 수정하세요");
        navigate("/login", { replace: true });
      }
      if (error.response && error.response.status === 400) {
        setErrors(error.response.data);
        console.log("수정400에러", error.response.data);
      } else {
        alert("실패");
        console.log(error);
      }
    }
  };

  // 댓글 삭제
  const handleCommentDelete = async (commentId) => {
    if (!window.confirm("댓글을 삭제할까요?")) {
      return;
    }
    try {
      await api.delete(`/api/comments/${commentId}`);
      alert("댓글이 삭제되었습니다.");
      loadComments();
    } catch (error) {
      if (error.response.status === 403) {
        alert("삭제 할 권한이 없습니다.");
      } else {
        alert("댓글 삭제 실패");
      }
    }
  };

  // 날짜시간 포맷팅
  const formattedDate = (dateString) => {
    return new Date(dateString).toLocaleString();
  };

  return (
    <>
      <div className="comment-section">
        <h3>댓글</h3>
        <ul>
          {comments.length > 0 ? (
            comments.map((c) => (
              <li key={c.id}>
                <div className="detail-info">
                  <p style={{ color: "black" }}>작성자 : {c.writer.username}</p>
                  <p>최초 작성일 : {formattedDate(c.createdAt)}</p>
                </div>
                {/* 댓글 수정 여부에 따른 출력 폼 */}
                {isEdit === false || isEditComment !== c.id ? (
                  <p>{c.content}</p>
                ) : (
                  <>
                    <form onSubmit={() => handleCommentEdit(c.id)} className="comment-form">
                      <input
                        type="text"
                        value={editComment}
                        placeholder={c.content}
                        onChange={(e) => setEditComment(e.target.value)}
                        required
                      />
                      <button type="submit">수정</button>
                      <button
                        onClick={() => {
                          setIsEdit(false);
                        }}
                      >
                        취소
                      </button>
                    </form>
                    {/* {errors && <p className="error-message">{errors.content}</p>} */}
                  </>
                )}
                {/* <p>{c.content}</p> */}
                {user === c.writer.username && isEdit === false && (
                  <>
                    <div className="comment-btn">
                      <div
                        onClick={() => {
                          setIsEdit(true);
                          setisEditComment(c.id);
                          setEditComment(c.content);
                        }}
                        className="comment-edit-btn"
                      >
                        수정
                      </div>
                      <div onClick={() => handleCommentDelete(c.id)} className="comment-delete-btn">
                        삭제
                      </div>
                    </div>
                  </>
                )}
              </li>
            ))
          ) : (
            <p className="text-message">댓글이 존재하지 않습니다.</p>
          )}
        </ul>
        {isEdit === false && (
          <form onSubmit={handleComment} className="comment-form">
            <input
              type="text"
              placeholder="댓글을 입력하세요"
              value={comment}
              onChange={(e) => setComment(e.target.value)}
              required
            />
            <button type="submit">등록</button>
          </form>
        )}

        {errors && <p className="error-message">{errors.content}</p>}
      </div>
    </>
  );
}
export default Comment;
