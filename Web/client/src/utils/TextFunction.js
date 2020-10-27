//윈도우 안에 패브릭을 넣어줌
const fabric = window.fabric;
//제이쿼리를 사용하기로함                             
var $ = require('jquery');
//캔버스를 전역으로 선언
let canvas = null;


//텍스트 추가 함수 추가 -> value제거
export function addText(keyboard , _canvas) {
  canvas = _canvas;
  if($('#new_text').val() !=='') {
    var newText = new fabric.IText($('#new_text').val(), {                
      left: 50,
      top: 100,                                                         
      fontFamily: 'arial black',
      fill: '#333',
      fontSize: 50,
      uniScaleTransform: true,
      //비균일 스케일링 잠금!
      lockUniScaling: true
    });
    canvas.add(newText);
    canvas.requestRenderAll();
    //this.keyboard에 저장된 value 지움
    if (keyboard) { keyboard.clearInput(); }
    //input 박스에 저장된 value 지움
    document.getElementById('new_text').value = '';
  }
};

//폰트 종류 설정
export function fontFamily(_canvas) {
  canvas = _canvas;
  if (canvas.getActiveObject() !== undefined && canvas.getActiveObject() !== null){       
    if (canvas.getActiveObject().text) {                                                  
      canvas.getActiveObject().set({ fontFamily: $('#font_family').val() });                             
      canvas.renderAll();
    }
  }
}

//텍스트 색깔 설정
export function textColor(_canvas) {
  canvas = _canvas;
  //선택한 객체가 undefined나 null이 아닐때,
  if (canvas.getActiveObject() !== undefined && canvas.getActiveObject() !== null){  
    //선택한 객체가 text일때만 실행시키기 위해서      
    if (canvas.getActiveObject().text) {                         
      canvas.getActiveObject().set({ fill: $('#text_color').val() });                                
      canvas.renderAll();
    }
  }
}

//텍스트 배경 색 설정 
export function textBgColor(_canvas) {
  canvas = _canvas;
  if (canvas.getActiveObject() !== undefined && canvas.getActiveObject() !== null){        
    if (canvas.getActiveObject().text) {                                                   
    canvas.getActiveObject().set({ backgroundColor: $('#text_bg_color').val() });         
    canvas.renderAll();
    }
  }
}

//텍스트 테두리 설정
export function textStrokeColor(_canvas) {
  canvas = _canvas;
  if (canvas.getActiveObject() !== undefined && canvas.getActiveObject() !== null){       
    if (canvas.getActiveObject().text) {                                                  
    canvas.getActiveObject().set({ stroke: $('#text_stroke_color').val() });                    
    canvas.renderAll();
    }
  }
}

//텍스트 테두리 두께 설정
export function textStrokeWidth(_canvas) {
  canvas = _canvas;
  if (canvas.getActiveObject() !== undefined && canvas.getActiveObject() !== null){       
    if (canvas.getActiveObject().text) {         
    //val.()은 항상 문자열로 반환하기에 parseFloat을 이용해 Number로 반환                                         
    canvas.getActiveObject().set({ strokeWidth: parseFloat($('#text_stroke_width').val()) });                   
    canvas.renderAll();
    }
  }
}
