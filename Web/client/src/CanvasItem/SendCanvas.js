import React, { Fragment } from 'react'  
import axios from 'axios';


class SendCanvas extends React.Component {

  //캔버스에서 만든 내용물을 ToJSON형식으로 서버로 보냄
  addCanvasDataApi = (canvasDatas) => {
    const canvasDataUrl = this.props.mainUrl + 'canvasDatas';
    const url = `${canvasDataUrl}`;
    axios.post( url , { canvasDatas })
    .then( response => { console.log(response) } )
    .catch( response => { console.log(response) } );
  }

  dateFunction() {
    var dateTime = new Date();
    //사용자가 입력한 date 를 valueAsNumber로 가져옴
    var getFromDateTime = document.getElementById('fromdatetime').valueAsNumber;
    var getToDateTime = document.getElementById('todatetime').valueAsNumber;
    console.log(getFromDateTime);
    console.log(getToDateTime);
    //9시간 시간차가 있어서 밀리초단위로 9시간을 빼줌
    getFromDateTime = getFromDateTime - (60 * 60 * 9 * 1000);
    getToDateTime = getToDateTime - (60 * 60 * 9 * 1000);
    console.log(getFromDateTime);
    console.log(getToDateTime);
    dateTime.setTime(getFromDateTime);
    console.log(dateTime);

    const sendData = this.props.canvas.toJSON();
    sendData.fromdatetime = `${getFromDateTime}`;
    sendData.todatetime = `${getToDateTime}`;
    console.log(sendData);
    
    return sendData;
  }

  render() {

      return ( 
        <Fragment>  
            <div className="sendcontainer" style={{
              width :"100%",
              height : "100%",
              backgroundColor : "#333",
              opacity : "0.3", 
              position : "fixed",
              left : "0",
              top : "0",
              zIndex : "10"
            }}/>
           
            
            <div className="send" style={{ zIndex : "15" }}>
                <div style ={{color : "white"}}>만든 광고를 보여줄 날짜와 시간을 정해주세요.</div>
                <input type="datetime-local" id="fromdatetime" className="fromdatetime" style={{
                    width : '350px',
                }} /><br/><br/>
                <div style ={{color : "white"}}>만든 광고를 종료할 날짜와 시간을 정해주세요.</div>
                <input type="datetime-local" id="todatetime" className="todatetime" style={{
                    width : '350px',
                }} /><br/>
                <button className="sendbutton" id="sendbutton" onClick={(e) =>{ e.preventDefault(); this.addCanvasDataApi(this.props.canvas.toJSON()) }}>테스트</button>
                <button className="sendbutton2" id="sendbutton2" onClick={(e) =>{ e.preventDefault(); this.addCanvasDataApi(this.dateFunction()) }}>저장하기</button>
            </div>
          </Fragment>
      );
  }
}

export default SendCanvas;
