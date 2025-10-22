import React, { useContext, useEffect, useState } from "react";
import { Link, useNavigate, useSearchParams } from "react-router-dom";
import "./BoardList.css";
import api from "../../config/axiosConfig";
import { UserContext } from "../../context/UserContext";

function BoardList() {
  const user = useContext(UserContext);
  const navigate = useNavigate();
  const [loading, setLoading] = useState(true); // 로딩중 **
  const [errors, setErrors] = useState(null); // 에러 **

  const pageGroupSize = 10; // 1 페이지 당 보여지는 글의 개수
  const [posts, setPosts] = useState([]); // 모든 글 목록
  const [totalItems, setTotalItems] = useState(0); // 총 글 수
  const [totalPages, setTotalPages] = useState(0); // 총 페이지 수

  // const [currentPage, setCurrentPage] = useState(validatedAndGetPage()); // 현재 페이지 번호
  const [searchParams, setSearchParams] = useSearchParams(); // url에 입력된 값 받기 (쿼리 스트링)
  // const currentPage = parseInt(searchParams.get('page') || '0'); // 현재 페이지를 0 또는 url에서 받아온 값으로 저장하기

  // 페이지 유효성 검증 함수 (파라미터 없으면 0, 숫자 아니면 0, 음수면 0)
  const validatedAndGetPage = () => {
    const pageParam = parseInt(searchParams.get("page"));
    if (!pageParam) return 0;
    if (isNaN(pageParam)) {
      setSearchParams({ page: "0" }); // url 주소도 변경해줌
      return 0;
    }
    if (pageParam < 0) {
      setSearchParams({ page: "0" });
      return 0;
    }
    return pageParam;
  };
  const currentPage = validatedAndGetPage();

  // 글 목록 가져오기
  const loadPosts = async (page = 0) => {
    try {
      setLoading(true); // loading : 첫 마운트 때는 true가 적용되지만 리렌더가 될 때는 false가 되기 때문에 다시 세팅해줘야 함
      setErrors(null); // errors : 마찬가지로 초기화 시켜줘야 함
      const res = await api.get(`/api/board?page=${page}&size=10`);
      setPosts(res.data.posts); // 모든 글 목록
      setTotalItems(res.data.totalItems); // 총 글 수
      setTotalPages(res.data.totalPages); // 총 페이지 수
      // setCurrentPage(res.data.currentPage); // 현재 페이지 번호

      // 만약 param으로 넘어온 page의 값이 총 페이지 수보다 큰 경우에는 처음으로 돌려줌
      if (page > 0 && res.data.totalPages > 0 && page >= res.data.totalPages) {
        setSearchParams({ page: "0" });
      }
    } catch (error) {
      console.log("글 불러오기 에러 : ", error);
      setErrors("게시글을 불러올 수 없습니다.");
      setPosts([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadPosts(currentPage);
  }, [currentPage]); // 현재 페이지 번호가 바뀔 때마다 실행

  // 페이지 변경 함수 (url 변경) *******************
  const handlePageChange = (page) => {
    setSearchParams({ page: page.toString() });
  };

  // 페이지네이션 pagination (startPage, endPage, pages 구하기)
  const getPageNumbers = () => {
    const startPage = Math.floor(currentPage / pageGroupSize) * pageGroupSize;
    const endPage = startPage + pageGroupSize < totalPages ? startPage + pageGroupSize : totalPages;
    const pages = [];
    for (let i = startPage; i < endPage; i++) {
      pages.push(i);
    }
    return pages;
  };

  // 글쓰기 권한
  const handleWriteClick = (e) => {
    if (user === null) {
      e.preventDefault();
      alert("로그인이 필요한 서비스입니다.");
      navigate("/login", { replace: true });
    }
  };

  // 날짜 포맷팅
  const formattedDate = (dateString) => {
    return new Date(dateString).toLocaleDateString();
  };

  // 확인용 **
  // console.log("총 글 수: ", totalItems, "총 페이지 수: ", totalPages, "현재 페이지: ", currentPage);

  return (
    <div className="board-container">
      <div className="board-header">
        <h2>자유게시판</h2>
        <Link to="/board/new" onClick={handleWriteClick} className="write-btn">
          글쓰기 ✍️
        </Link>
      </div>
      {loading && <p className="error-message">글 목록 로딩 중...</p>}
      {errors && <p className="error-message">게시글이 존재하지 않습니다.</p>}
      <table className="board-table">
        <thead>
          <tr>
            <th>번호</th>
            <th>제목</th>
            <th>작성자</th>
            <th>조회수</th>
            <th>작성일</th>
          </tr>
        </thead>
        <tbody>
          {posts.map((p, index) => (
            <tr key={p.id}>
              <td>{totalItems - index - pageGroupSize * currentPage}</td>
              <td>
                <Link to={`/board/${p.id}`}>{p.title}</Link>
                <span className="board-comment">({p.comments.length})</span>
              </td>
              <td>{p.writer.username}</td>
              <td>{p.views}</td>
              <td>{formattedDate(p.createdAt)}</td>
            </tr>
          ))}
        </tbody>
      </table>
      {/* 페이지네이션 */}
      <div className="pagination">
        {/* 첫 페이지로 이동 */}
        <button onClick={() => handlePageChange(0)} disabled={currentPage === 0}>
          ◀◀
        </button>
        {/* 이전 페이지로 이동 */}
        <button onClick={() => handlePageChange(currentPage - 1)} disabled={currentPage === 0}>
          ◀
        </button>
        {/* 페이지 그룹 */}
        {getPageNumbers().map((p) => (
          <button key={p} onClick={() => handlePageChange(p)} className={p === currentPage ? "active" : ""}>
            {p + 1}
          </button>
        ))}
        {/* 다음 페이지로 이동 */}
        <button onClick={() => handlePageChange(currentPage + 1)} disabled={currentPage === totalPages - 1}>
          ▶
        </button>
        {/* 마지막 페이지로 이동 */}
        <button onClick={() => handlePageChange(totalPages - 1)} disabled={currentPage === totalPages - 1}>
          ▶▶
        </button>
      </div>
    </div>
  );
}

export default BoardList;
