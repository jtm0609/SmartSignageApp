import React, { Fragment }from 'react'
import Keyboard from 'react-hangul-virtual-keyboard';
import "react-simple-keyboard/build/css/index.css";
import * as Tfunc from '../utils/TextFunction.js';
import Hangul from "hangul-js";

let canvas = null;
let buttonArray = [];
let inputText = "";

//MAKE 메뉴에 텍스트 버튼을 눌렀을 때 텍스트 관련 목록을 뿌려주는 컴포넌트
class TextItem extends React.Component {
  constructor(props) {
    super(props);
      this.state ={
        mode:"off",
        layoutName: "default",
        language: "default",
        input: ""
      }
    canvas = this.props.canvas;
  }

  //키보드 함수
  //입력인자로 ON || OFF 받아서 state 전환
  handleOnKeyborad = (mode) => { this.setState({ mode: mode }); };

  //클릭시 버튼 전환 한글 자음+모음 합치는 함수
  onKeyPress = button => {
    if (
        ![
          "{shift}",
          "{language}",
          "{enter}",
          "{bksp}",
          "{space}",
          "{tab}"
        ].includes(button)
    ) {
        buttonArray.push(button);
    }
    if (button === "{bksp}") {
        buttonArray.pop();
    }
    if (button === "{space}") {
        buttonArray.push(" ");
    }
    if (button === "{tab}") {
        buttonArray.push("  ");
    }
    if (button === "{enter}") {
      Tfunc.addText(this.keyboard , canvas); 
      this.setState({ input : "" }); 
      // inputText=""; 
      buttonArray = [];
    }

    inputText = Hangul.assemble(buttonArray);
    this.setState({ input : inputText });

    //Shift 함수
    if (button === "{shift}") this.handleShiftButton();
    if (button === "{language}") this.handleLanguageButton();
  };

  //input창에 변화가 생기면 그 값을 반영
  onChangeInput = (e) => { 
    e.preventDefault(); 
    const input = e.target.value;
    buttonArray = [];
    buttonArray.push(input);
    this.setState({ input : buttonArray });
  };

  //쉬프트버튼 함수
  handleShiftButton = (e) => {
    const layoutName = this.state.layoutName; 
    const shiftToggle = layoutName === "default" ? "shift" : "default";
    this.setState({ layoutName: shiftToggle });
  };

  //한영버튼 함수
  handleLanguageButton= (e) => {
    const language = this.state.language;
    const languageToggle = language === "default" ? "english" : "default";
    this.setState({ language: languageToggle });
  };

  //ON일때 키보드 , 입력창 , 추가버튼 출력 //ref={(t) => {text = t}}
  onKeyBoard() {
    if (this.state.mode ==='on'){
      return  <Fragment>
                <div className="keyboardcontainer" style={{
                        width :"100%",
                        height : "100%",
                        backgroundColor : "#333",
                        opacity : "0.3", 
                        position : "fixed",
                        left : "0",
                        top : "0",
                        zIndex : "10"
                }}/>
                <input
                        value={this.state.input} 
                        placeholder="Keyboard Input" type="text" 
                        id="new_text" onChange={this.onChangeInput}
                        style ={{ fontSize : "250%"}}
                />
                <Keyboard
                        stateToIgnore={this.state.input}
                        keyboardRef={r => (this.keyboard = r)}
                        onKeyPress={this.onKeyPress}
                        layoutName={this.state.layoutName}
                        language={this.state.language}
                        autoUseTouchEvents = { true }
                />
                <button id="addtext" 
                        onClick={(e) => { 
                          e.preventDefault(); 
                          Tfunc.addText(this.keyboard , canvas); 
                          this.setState({ input : "" }); 
                          buttonArray = []; 
                          }}
                        style = {{ 
                          zIndex : "15",
                          position : "fixed",
                          width : "10%",
                          height : "10%",
                          top : "65%",
                          left : "70%" 
                        }}
                >ADD TEXT
                </button><br/>
              </Fragment>
    }
  };

  //키보드 ON & OFF 버튼
  handleChangeButton() {
    if (this.state.mode === 'on'){
      return <button style = {{ 
                      zIndex : "15",
                      position : "fixed",
                      width : "10%",
                      height : "10%",
                      top : "50%",
                      left : "70%" 
                      }}
                      onClick={(e) => { 
                        e.preventDefault(); 
                        this.handleOnKeyborad('off');
                        this.setState({ input : "" }); 
                        buttonArray = []; 
                        }}
             >Keyboard Off</button>
    }
    else if (this.state.mode === 'off'){
      return <button style = {{ 
                      zIndex : "15",
                      position : "fixed",
                      width : "10%",
                      height : "10%",
                      top : "50%",
                      left : "70%" 
                      }}
                      onClick={(e) => { 
                        e.preventDefault(); 
                        this.handleOnKeyborad('on'); 
                        this.setState({ input : "" }); 
                        buttonArray = []; 
                      }}
             >Keyboard on</button>
    }
  };

  //텍스트 글꼴 옵션리스트
  fontOption() {
    return (
        <Fragment>
          <option value="arial black">Arial</option>
          <option value="helvetica" >Helvetica</option>
          <option value="comic sans ms">Comic Sans MS</option>
          <option value="impact">Impact</option>
          <option value="Times New Roman">Times New Roman</option>
          <option value="delicious">Delicious</option>
          <option value="verdana">Verdana</option>
          <option value="georgia">Georgia</option>
          <option value="courier">Courier</option>
        </Fragment>
    );
  };
  
  render() {
    return (
        <Fragment>          

          <label>Font Family</label><br/>
            <select id="font_family" onChange={(e) => { e.preventDefault(); Tfunc.fontFamily(canvas); }}>
              {this.fontOption()}
            </select><br/>

            <label>Text Color</label><br/>
              <input className="color" id="text_color" type="color" onChange={(e) => { e.preventDefault(); Tfunc.textColor(canvas); }} style ={{ width : "100%", height : 50}} /><br/>
            <label>Text Background Color</label><br/>
              <input className="color" id="text_bg_color" type="color" onChange={(e) => { e.preventDefault(); Tfunc.textBgColor(canvas); }} style ={{ width : "100%", height : 50}} /><br/>
            <label>Stroke Color</label><br/>
              <input className="color" id="text_stroke_color" type="color" onChange={(e) => { e.preventDefault(); Tfunc.textStrokeColor(canvas); }} style ={{ width : "100%", height : 50}} /><br/>
            <label>Stroke Width</label><br/>   
              <input className="range"  id="text_stroke_width" type="range" 
                      onChange={(e) => { e.preventDefault(); Tfunc.textStrokeWidth(canvas); }} step="0.5" min="0.5" max="4" defaultValue="2" /><br/> 
            
            {this.onKeyBoard()}

            {this.handleChangeButton()}

          </Fragment>
    );
  }
}

export default TextItem;
