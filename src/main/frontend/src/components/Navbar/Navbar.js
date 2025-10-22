import React, { useContext } from "react";
import { Link } from "react-router-dom";
import "./Navbar.css";
import { UserContext } from "../../context/UserContext";

function Navbar({ logout }) {
  const user = useContext(UserContext);

  return (
    <nav className="navbar">
      <div className="nav-logo">
        <Link to="/">🎬 CineTalk</Link>
      </div>
      <ul className="nav-links">
        {/* 비회원 : Home 게시판 로그인 회원가입 */}
        {/* 회원 : Home 게시판 마이페이지 로그아웃  */}
        <li>
          <Link to="/">Home</Link>
        </li>
        <li>
          <Link to="/movies">영화</Link>
        </li>
        <li>
          <Link to="/board">게시판</Link>
        </li>
        <li>
          <Link to="/map">오시는 길</Link>
        </li>
        {user && (
          <>
            <li>
              <Link to="/mypage">{user}님의 마이페이지</Link>
            </li>
            <li onClick={logout} className="logout-btn">
              로그아웃
            </li>
          </>
        )}
        {!user && (
          <>
            <li>
              <Link to="/login">로그인</Link>
            </li>
            <li>
              <Link to="/signup" className="signup-btn">
                회원가입
              </Link>
            </li>
          </>
        )}
      </ul>
    </nav>
  );
}

export default Navbar;
