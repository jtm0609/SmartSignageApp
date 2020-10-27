import React, { Fragment } from 'react'
import ImageItem from '../CanvasItem/ImageItem.js'
import VideoItem from '../CanvasItem/VideoItem.js'
import TextItem from '../CanvasItem/TextItem.js'
import SendCanvas from '../CanvasItem/SendCanvas.js'
import './DesignCanvas.css';

import { dragAndDrop } from '../utils/DragDrop.js';
import * as Cfunc from '../utils/CanvasFunction.js';


//mainUrl = 'http://localhost:5000/api/';


//윈도우 안에 패브릭을 넣어줌
const fabric = window.fabric; 
//제이쿼리를 사용하기로함                                             
var $ = require('jquery');  
//컨버스를 전역으로 선언                                                
let canvas = null;

//MAKE 메뉴의 전체부분
class DesignCanvas extends React.Component {
  constructor(props) {
    super(props);
      this.state = { 

        menuDatas :[{
          src: null,
          type: '',
        }],

          //캔버스값을 저장을 저장할 그릇
          canvas: null,                                                    
          menu : 'IMAGE',
          send : 'off'                                   
      }
  }

  componentWillMount() {

    this.getMenuItemApi('menuDatas')
    .then(res => this.setState({ menuDatas: res }))
    .catch(err => console.log(err));
  
  }
  
  componentDidMount() {
    //캔버스를 만듬
    canvas = new fabric.Canvas(this.c , { backgroundColor : '#fff' });       

    //캔버스의 크기설정할수 있음
    canvas.setHeight($('.canvas-wrapper').height());                                                  
    canvas.setWidth($('.canvas-wrapper').width());
    this.setState({ canvas });

    //컨버스에 기본 옵션설정 테두리,모서리
    fabric.Object.prototype.set({
      borderColor: 'grey',
      cornerColor: 'black',
      cornerSize: 18,
      transparentCorners: true,
    });

    //캔버스의 크기를 전체 브라우저의 크기에 맞게 조절
    window.addEventListener('resize', function(){ Cfunc.canvasResize(canvas) }, false);

    //드래그 앤 드랍 함수를 실행 , 바로 실행시키면 서버로 부터 메뉴목록 받기전에 실행되서 드래그앤드랍이 데이터에 안먹힘 
    // dragAndDrop(canvas);
    setTimeout(() => { dragAndDrop(canvas); }, 500); 
    //선택한 객체 인덱스 맨앞으로 땡기기
    canvas.on('mouse:down', function() { Cfunc.bringFrontIndex(canvas) });   
  }

  //아이템 메뉴 목록을 가져오는 API
  getMenuItemApi = async (value) => {
    const response = await fetch(`${this.props.mainUrl}${value}`);
    const body = await response.json();
    return body;
  }

  handleMenuChange(menu){ this.setState({ menu : menu }) };

  handleSaveOnOff(send){ this.setState({ send : send }) };

  handleChangeSaveButton() {
    if (this.state.send === 'off'){
      return ( <button className="snip1535" id="toJson" onClick={(e) => { e.preventDefault(); this.handleSaveOnOff('on')}}>SAVE BILL</button> );
    } 
    else if (this.state.send === 'on'){
      return (
        <Fragment>
        <SendCanvas canvas={canvas} mainUrl={this.props.mainUrl}/>
        <button className="snip1535" id="toJson" onClick={(e) => { e.preventDefault(); this.handleSaveOnOff('off')}} style={{zIndex : "15"}}>SAVE BILL</button>
        </Fragment>
        );
    }
  };


  //IMAGE || VIDEO || TEXT 전환
  handleFurnitureChange() {
    if (this.state.menu === 'IMAGE'){ return ( 
      <div className="addimage"> 
        <ImageItem menuDatas={this.state.menuDatas} width={$('.furniture').width()} height={$('.furniture').height()}></ImageItem> 
      </div>
      ); 
    }
    else if(this.state.menu === 'VIDEO'){ return (
      <div className='addvideo'>  
        <VideoItem menuDatas={this.state.menuDatas} width={$('.furniture').width()} height={$('.furniture').height()}></VideoItem> 
      </div>
      ); 
    }
    else if (this.state.menu === 'TEXT'){ return (
      <div className="addtext">
        <TextItem canvas={canvas}></TextItem>
      </div>
      ); 
    }
  };

  render() {
    return (
      <Fragment>
        
        <div className="canvas-container">

          <div className="title">Billboard Maker</div>

          <div className="canvas-wrapper">
            <canvas ref={c => (this.c = c)} />  
          </div>                                 
        
          <div className="menu">

            <button className="snip1535u" onClick={(e) =>{ e.preventDefault(); this.handleMenuChange('IMAGE')}}>Image</button>
            <button className="snip1535u" onClick={(e) =>{ e.preventDefault(); this.handleMenuChange('VIDEO')}}>Video</button>
            <button className="snip1535u" onClick={(e) =>{ e.preventDefault(); this.handleMenuChange('TEXT')}}>Text</button>
          
            <label style ={{ color : "white" }}>Background Color</label><br/>
		      		<input className="color" id="bg_color" type="color" onChange={(e) => { e.preventDefault(); Cfunc.bgColor(canvas); }} style ={{width : "160%"}}/><br/><br/>
            <button className="snip1535" id="delete" onClick={(e) => { e.preventDefault(); Cfunc.deleteObjects(canvas); }}>DELETE</button><br/>
            <button className="snip1535" id="sendbackwards" onClick={(e) => { e.preventDefault(); Cfunc.sendBackwards(canvas); }}>SEND BACK</button><br/>
            <button className="snip1535" id="capture" onClick={(e) => { e.preventDefault(); Cfunc.captureButton(canvas); }}>CAP TURE</button><br/>
            { this.handleChangeSaveButton() }

          </div>

          <div className="furniture">
            { this.handleFurnitureChange() }
          </div>

        </div>
     
      </Fragment>
      );
  }
}

export default DesignCanvas

