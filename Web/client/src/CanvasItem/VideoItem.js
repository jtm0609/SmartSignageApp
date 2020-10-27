import React, { Fragment }from 'react'
// import Fsrc from 'C:/Users/KDHyeong/Desktop/React/smartbillboard/client/src/video/test.mp4';
// import Fsrc2 from 'C:/Users/KDHyeong/Desktop/React/smartbillboard/client/src/video/test2.mp4';
// import Fsrc3 from 'C:/Users/KDHyeong/Desktop/React/smartbillboard/client/src/video/test3.mp4';
import { dragAndDrop, removeCanvasEvent } from '../utils/DragDrop.js';

//MAKE 메뉴에 비디오 버튼을 눌렀을 때 비디오 목록을 뿌려주는 컴포넌트
class VideoItem extends React.Component {
  constructor(props) {
    super(props);
      this.state = {
          src1:"http://html5demos.com/assets/dizzy.mp4",
          src2:"https://www.youtube.com/watch?v=SOE1A9p8KLw"
          
      }
    }

  //드래그 앤 드랍 리스닝
  componentDidMount(){ setTimeout(() => { dragAndDrop(null); }, 500); } 
  //메뉴 목록이 바뀌면 리스닝 해제
  componentWillUnmount() { setTimeout(() => { removeCanvasEvent(); }, 500); }

  render() {
      return (
          //서버에서 받아온 동영상 목록을 매핑해서 뿌려줌
          <Fragment>

            {/* <video draggable='true' src={Fsrc}  width ={this.props.width / 2.2} height ={this.props.height / 4.5} muted poster="" /> */}
            {
              this.props.menuDatas.map((menuDatas) => {
                if (menuDatas.zType === 'video') {
                  return (
                      <video draggable='true' src={menuDatas.src}  width ={this.props.width / 2.2} height ={this.props.height / 4.5} muted poster="http://img.youtube.com/vi/sJeZECYYaDc/0.jpg" loop/>
                  );
                }
                return false; 
              })
            }
            {/* <video draggable='true' src={Fsrc2}  width ={this.props.width / 2.2} height ={this.props.height / 4.5} muted poster="" /> */}
            {/* <video draggable='true' src={Fsrc3}  width ={this.props.width / 2.2} height ={this.props.height / 4.5} muted poster="" /> */}

          </Fragment>
          
      );
  }
}

export default VideoItem;
