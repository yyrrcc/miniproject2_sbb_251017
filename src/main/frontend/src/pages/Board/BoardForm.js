import React, { useContext, useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import "./BoardForm.css";
import api from "../../config/axiosConfig";
import { UserContext } from "../../context/UserContext";

function BoardForm() {
  const user = useContext(UserContext);
  const navigate = useNavigate();

  const { id } = useParams();
  const isEdit = !!id; // 글쓰기인지 글수정인지
  const [errors, setErrors] = useState({});
  // const [loading, setLoading] = useState(true); // 로딩중

  // 글 작성 state 변수
  const [form, setForm] = useState({
    title: "",
    content: "",
    user,
  });

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErrors({});
    try {
      // 수정상태일 때 or 글쓰기 상태
      if (isEdit) {
        await api.put(`/api/board/${id}`, { ...form });
        alert("글 수정 완료");
        navigate(`/board/${id}`, { replace: true });
      } else {
        await api.post("/api/board/new", { ...form });
        alert("글 작성 완료");
        navigate("/board", { replace: true });
      }
    } catch (error) {
      if (error.response && error.response.status === 401) {
        // 권한
        setErrors(error.response.data);
        alert(errors);
        navigate("/login", { replace: true });
      }
      if (error.response && error.response.status === 400) {
        // 유효성
        setErrors(error.response.data);
      } else {
        setErrors(error);
        alert("실패");
        console.log(error);
      }
    }
  };

  // 글 수정 (isEdit이 참일 때만 실행 될 수 있게 useEffect 사용)
  const loadPost = async () => {
    try {
      const res = await api.get(`/api/board/${id}`);
      setForm({ ...form, title: res.data.title, content: res.data.content });
    } catch (error) {
      console.log("글 불러올 수 없음", error);
    }
  };
  useEffect(() => {
    if (isEdit) {
      loadPost();
    }
  }, [id, isEdit]); // 랜더링 .. **

  return (
    <div className="form-container">
      <h2>{isEdit ? "게시글 수정" : "새 게시글 작성"}</h2>
      <form onSubmit={handleSubmit} className="board-form">
        <input
          type="text"
          name="title"
          placeholder="제목을 입력하세요"
          value={form.title}
          onChange={handleChange}
          required
        />
        {errors.title && <p className="error-message">{errors.title}</p>}
        <textarea
          name="content"
          placeholder="내용을 입력하세요"
          rows="10"
          value={form.content}
          onChange={handleChange}
          required
        ></textarea>
        {errors.content && <p className="error-message">{errors.content}</p>}
        <button type="submit">{isEdit ? "수정 완료" : "등록"}</button>
      </form>
    </div>
  );
}

export default BoardForm;
