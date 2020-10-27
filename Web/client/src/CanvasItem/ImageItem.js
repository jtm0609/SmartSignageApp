import React, { Fragment }from 'react'  
import { dragAndDrop, removeCanvasEvent } from '../utils/DragDrop.js';
import Iu from '../iu.png'
import dl from '../dl.png'
import aas from '../aas.png'

//MAKE 메뉴에 이미지 버튼을 눌렀을 때 이미지 목록을 뿌려주는 컴포넌트
class ImageItem extends React.Component {

  //드래그 앤 드랍을 실행
  componentDidMount(){ setTimeout(() => { dragAndDrop(null); }, 500); } 
  //메뉴가 바뀔때 드래그앤 드랍을 제거
  componentWillUnmount() { setTimeout(() => { removeCanvasEvent(); }, 500); }

  render() {

      return (
           
          <Fragment>
                 <img draggable='true' src={Iu} width ={this.props.width / 2.2} height ={this.props.height / 4.5} alt="" />
                 <img draggable='true' src={dl} width ={this.props.width / 2.2} height ={this.props.height / 4.5} alt="" />
                 {/* <img draggable='true' src={aas} width ={this.props.width / 2.2} height ={this.props.height / 4.5} alt="" /> */}
            {
              this.props.menuDatas.map((menuDatas) => {
                if (menuDatas.zType === 'image') {
                  return ( <img draggable='true' src={menuDatas.src} width ={this.props.width / 2.2} height ={this.props.height / 4.5} alt="" /> );
                }
              return false; 
              })
            }

          </Fragment>
      );
  }
}

export default ImageItem;
