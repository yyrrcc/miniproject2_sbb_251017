import frozen from "../../images/frozen.jpg";
import interstellar from "../../images/interstellar.jpg";
import lalaland from "../../images/lalaland.jpg";
import substance from "../../images/substance.jpg";
import thelma from "../../images/thelma.jpg";

// DB : 기본키, 제목, 좋아요 수 + 한줄평
// 프론트 : 기본키, 이미지

// id 값을 이용해서 영화 정보를 가져오기!
export const getMovieInfoByID = (movieId) => {
  const targetMovieId = String(movieId);
  switch (targetMovieId) {
    case "1":
      return { img: frozen, desc: "겨울이 찾아 왔어요" };
    case "2":
      return { img: interstellar, desc: "우주여행을 떠나요" };
    case "3":
      return { img: lalaland, desc: "신나는 노래" };
    case "4":
      return { img: substance, desc: "무조건 봐야 하는 영화" };
    case "5":
      return { img: thelma, desc: "클래식은 영원해" };
    default:
      return null;
  }
};

export const getMovieImgByID = (movieId) => {
  const targetMovieId = String(movieId);
  switch (targetMovieId) {
    case "1":
      return frozen;
    case "2":
      return interstellar;
    case "3":
      return lalaland;
    case "4":
      return substance;
    case "5":
      return thelma;
    default:
      return null;
  }
};

// 이미지 랜더링하기 (js에서 사용할 수 있는 데이터 형태로)
export const getMovieInfo = [
  {
    id: 1,
    desc: "겨울이 찾아왔어요",
    img: getMovieImgByID(1),
  },
  {
    id: 2,
    desc: "우주여행을 떠나요",
    img: getMovieImgByID(2),
  },
  {
    id: 3,
    desc: "신나는 노래",
    img: getMovieImgByID(3),
  },
  {
    id: 4,
    desc: "미친 영화",
    img: getMovieImgByID(4),
  },
  {
    id: 5,
    desc: "하늘을 날자",
    img: getMovieImgByID(5),
  },
];
