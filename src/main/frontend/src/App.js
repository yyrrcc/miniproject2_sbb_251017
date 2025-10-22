import "./App.css";
import { Route, Routes, useNavigate } from "react-router-dom";
import Navbar from "./components/Navbar/Navbar";
import Footer from "./components/Footer/Footer";
import Home from "./pages/Home/Home";
import Login from "./pages/Login/Login";
import Signup from "./pages/Signup/Signup";
import MyPage from "./pages/MyPage/MyPage";
import BoardList from "./pages/Board/BoardList";
import { useEffect, useState } from "react";
import api from "./config/axiosConfig";
import BoardForm from "./pages/Board/BoardForm";
import BoardDetail from "./pages/Board/BoardDetail";
import MovieList from "./pages/MovieDummy/MovieList";
import MovieCardBackup from "./pages/Movie/MovieCard_backup";
import { UserContext } from "./context/UserContext";
import KakaoMap from "./components/Map/KakaoMap";

function App() {
  const navigate = useNavigate();
  const [user, setUser] = useState(null); // 현재 로그인한 유저의 아이디(username)
  const onLogin = async () => {
    try {
      const res = await api.get("/api/auth/me");
      // 응답이 성공이고 실제 사용자명이 있을 때만 로그인 처리
      if (res.data && res.data.username && res.data.username !== "anonymousUser") {
        setUser(res.data.username);
      } else {
        setUser(null);
      }
    } catch (error) {
      setUser(null);
      console.log("로그인 에러", error); // 로그인 에러가 자꾸 걸림 **
    }
  };
  useEffect(() => {
    onLogin();
  }, []);

  // 로그아웃 (새로고침하면 왜 anonymousyUser로 뜰까? **)
  const logout = async () => {
    await api.post("/api/auth/logout");
    setUser(null);
    alert("성공적으로 로그아웃 되었습니다.");
    navigate("/", { replace: true }); // 난 왜 navigate를 써줘야 할까? **
  };

  return (
    // PrivateRoute Outlet 사용해서 권한 설정하기 **
    // UserContext 이용해서 모든 컴포넌트에서 user 사용할 수 있게 해주기
    <UserContext.Provider value={user}>
      <div className="app-container">
        <Navbar logout={logout} />
        <main className="content">
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/login" element={<Login onLogin={onLogin} />} />
            <Route path="/signup" element={<Signup />} />
            <Route path="/mypage" element={<MyPage />} />
            <Route path="/board" element={<BoardList />} />
            <Route path="/board/new" element={<BoardForm />} />
            <Route path="/board/edit/:id" element={<BoardForm />} />
            <Route path="/board/:id" element={<BoardDetail />} />
            <Route path="/movies" element={<MovieCardBackup />} />

            <Route path="/moviecard" element={<MovieList />} />

            <Route path="/map" element={<KakaoMap />} />
          </Routes>
        </main>
        <Footer />
      </div>
    </UserContext.Provider>
  );
}

export default App;
