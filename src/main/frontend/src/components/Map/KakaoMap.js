import { useEffect, useState } from "react";
import { Map, MapMarker } from "react-kakao-maps-sdk";
import "./KakaoMap.css";

const KakaoMap = () => {
  const [isLoaded, setIsLoaded] = useState(false);

  useEffect(() => {
    const script = document.createElement("script");
    script.src = `https://dapi.kakao.com/v2/maps/sdk.js?appkey=${process.env.REACT_APP_KAKAO_API_KEY}&autoload=false`;
    script.async = true;
    script.onload = () => {
      window.kakao.maps.load(() => {
        setIsLoaded(true);
      });
    };
    document.head.appendChild(script);
  }, []);

  if (!isLoaded) return <div>지도 불러오는 중...</div>;

  return (
    <Map
      center={{ lat: 37.529796410505774, lng: 126.96418158883404 }}
      style={{ width: "90%", height: "400px" }}
      level={3}
    >
      <MapMarker position={{ lat: 37.529796410505774, lng: 126.96418158883404 }}>
        <div>영화 이야기는 여기서!</div>
      </MapMarker>
    </Map>
  );
};

export default KakaoMap;
