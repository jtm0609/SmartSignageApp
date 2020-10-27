import React, { Fragment } from 'react'; 
import './ShowMap.css';

//HOME의 상태가 CUSTOM 일때 뿌려주는 부분
class ShowMap extends React.Component {
    
    componentDidMount() {
        const mapContainer = document.getElementById('map'); // 지도를 표시할 div
        const mapOption = {
            center: new window.kakao.maps.LatLng(33.450701, 126.570667), // 지도의 중심좌표
            level: 4 // 지도의 확대 레벨
        };
        // 지도를 표시할 div와  지도 옵션으로  지도를 생성합니다
        const map = new window.kakao.maps.Map(mapContainer, mapOption);

        mapAround(map);
        window.addEventListener('resize', function() { mapAround(map); }, false);
    }
    componentWillUnmount() {
        window.removeEventListener('resize', mapAround, false);
    }
    render() {
    
        return (
            
            <Fragment>
                <div className="title">Map</div>
                <div className='Nav__map__board'>
                    <div id='map'></div>
                </div>
            </Fragment>

        );
    }
} 

  
export default ShowMap;

function mapAround(map) {
    if (navigator.geolocation) { // GPS를 지원하면
        navigator.geolocation.getCurrentPosition(function(position) {
        const coords = new window.kakao.maps.LatLng(position.coords.latitude, position.coords.longitude);

        // 결과값으로 받은 위치를 마커로 표시합니다
        const marker = new window.kakao.maps.Marker({
            map: map,
            position: coords,
        });
        // 인포윈도우로 장소에 대한 설명을 표시합니다
        const infowindow = new window.kakao.maps.InfoWindow({
            content: '<div style="width:150px;text-align:center;padding:6px 0;">현재 위치</div>'
        });
        infowindow.open(map, marker);
        // 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
        map.setCenter(coords);
        }, function(error) {
        console.error(error);
        }, {
        enableHighAccuracy: true,
        maximumAge: 0,
        timeout: Infinity
        });
    } else {
        alert('GPS를 지원하지 않습니다');
    }
}
