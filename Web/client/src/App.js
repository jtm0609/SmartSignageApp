import React, {Component, Fragment} from 'react';
import ShowAI from './Components/ShowAI';
import MakeCustom from './Components/MakeCustom';
import ShowCustom from './Components/ShowCustom';
import ShowMap from './Components/ShowMap';
import Camera from './Components/WebcamCapture';
import Button from '@material-ui/core/Button';
import './App.css';

//로컬 서버 주소/api/
const mainUrl = 'http://localhost:5000/api/';

let nowDate = null;

class App extends Component {
constructor(props) {
  super(props);

  this.state = {
    mode: 'BILLBOARD',            // 'BILLBOARD' || 'MAKE' || 'DETAIL' || 'QUESTION'               
    customTime : 'OFF',
    datas: [{               
          id: null,               // 1 ~ 10
          src: null,              
          category: 'AI',         // 'AI' || 'CUSTOM'
          }],
    datas2 : [{
          background: null,
          src: null,             
          width: null,
          height: null,
          scaleX: null,
          scaleY: null,
          top: null,
          zLeft: null,
          angle: null,             // ANGLE 
          zType: null,             // 'IMAGE' || 'VIDEO'
          zText: null,
          fontFamily: null,
          fill: null,
          stroke: null,
          strokeWidth: null,
          textBackColor: null,
          category: 'CUSTOM',      // 'AI' || 'CUSTOM'
          fromTime: 100000000,
          toTime: 1000000000
          },
          { fromTime: 10000000000, }
        ],
    }
  }

  componentDidMount() {
      //API로 AI 데이터를 가져와 그 데이터를 state에 저장
      this.getContentsApi('ai')
      .then(res => this.setState({ datas: res }))
      .catch(err => console.log(err));

      //미리 시작 시간을 확인하기 위해서 API로 CANVAS 데이터를 가져와 그 데이터를 state에 저장
      this.getContentsApi('canvas')
      .then(res => this.setState({ datas2: res }))
      .catch(err => console.log(err));
      
      //CANVAS 데이터를 계속 업데이트
      setInterval(() => {
      this.getContentsApi('canvas')
      .then(res => this.setState({ datas2: res }))
      .catch(err => console.log(err));
      }, 10000);

      //현재 시간을 계속 측정
      var timeCheck = setInterval(() => {
        nowDate = Date.now();
        console.log(nowDate);
      }, 1000);  

      //API를 바꿔 CANVAS 데이터 띄울 시간이 되면 CANVAS 데이터를 datas에 저장시켜 화면 전환
      var changeApi = setInterval(() => {
        if(this.state.mode === 'BILLBOARD'){ 
          console.log('작동중');
          if (this.state.datas2[0]) {
            if (this.state.datas2[0].fromTime > nowDate || this.state.datas2[0].toTime < nowDate ) {
              console.log('AI대기중');
              if(this.state.customTime === 'ON'){
                this.setState({ customTime : 'OFF' });
              }
              this.getContentsApi('ai')
              .then(res => this.setState({ datas: res }))
              .catch(err => console.log(err));
            }
            else if (this.state.datas2[0].fromTime < nowDate && this.state.datas2[0].toTime > nowDate && this.state.customTime === 'OFF'){
                console.log('시작')
                this.setState({ customTime : 'ON' });
                this.getContentsApi('canvas')
                .then(res => this.setState({ datas: res }))
                .catch(err => console.log(err));
              }
          }
        }
      }, 1000);
  }

  //서버주소 + value로 데이터 가져오는 API
  getContentsApi = async (value) => {
      const response = await fetch(`${mainUrl}${value}`);
      const body = await response.json();
      return body;
  }

  handleChangeMode = (mode) => {
    this.setState({ mode: mode });
  }

  //state에 따라 HOME 화면에 다른 컴포넌트를 뿌려줌
  viewChange() {
    if (this.state.mode === 'BILLBOARD'){
        if (this.state.datas[0].category === 'AI') 
          { return this.state.datas ? <ShowAI datas={this.state.datas}/> : '..LOADING '}  
        else if (this.state.datas[0].category === 'CUSTOM') 
          { return this.state.datas ? <ShowCustom datas={this.state.datas}/> : '..LOADING' }
    }
    else if (this.state.mode === 'MAKE') { return <MakeCustom mainUrl = {mainUrl}/> }
    else if (this.state.mode === 'MAP') { return <ShowMap /> }
    else if (this.state.mode === 'CAMERA') { return <Camera /> }
  }

  render(){
      return (
        <Fragment>
          <div className="noDrag">
            { this.viewChange() }
          </div>
          <ul>
            <li>  
              <Button fullWidth={true} variant="contained" color="primary" href="#contained-buttons" fontSize="x-large"
                      onClick={() => this.handleChangeMode('BILLBOARD')} name='Show' value='ShowBill'>Home</Button></li>
            <li>
              <Button fullWidth={true} variant="contained" color="primary" href="#contained-buttons" 
                      onClick={() => this.handleChangeMode('MAKE')} name='Make' value='MakeBill'>Billboard Maker</Button></li>
            <li>
              <Button fullWidth={true} variant="contained" color="primary" href="#contained-buttons" 
                      onClick={() => this.handleChangeMode('MAP')} name='Map' value='MakeBill'>Map</Button></li>
            <li>
              <Button fullWidth={true} variant="contained" color="primary" href="#contained-buttons" 
                      onClick={() => this.handleChangeMode('CAMERA')} name='Camera' value='Capture'>Camera</Button></li>
          </ul>
        
        </Fragment>
      );
  }
}

export default App;
