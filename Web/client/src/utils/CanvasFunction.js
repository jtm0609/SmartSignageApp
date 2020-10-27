//윈도우 안에 패브릭을 넣어줌
const fabric = window.fabric;
//제이쿼리를 사용하기로함                             
var $ = require('jquery');
//캔버스를 전역으로 선언
let canvas = null;

//캔버스 배경 컬러설정
export function bgColor(_canvas) {
    canvas = _canvas;
    canvas.backgroundColor = $('#bg_color').val();                                          
    canvas.renderAll();
}

//선택한 아이템 맨 뒤로 보내기
export function sendBackwards(_canvas) {
    canvas = _canvas;
    var activeObj = canvas.getActiveObject();
    if (activeObj) {
      canvas.sendToBack(activeObj);
      canvas.discardActiveObject();
    }
}
  
//선택한 아이템 맨 앞으로 가져오기
export function bringFrontIndex(_canvas) {
    canvas = _canvas;
    var activeObj = canvas.getActiveObject();  
    canvas.bringToFront(activeObj);                                                  
}
  
//선택한 아이템 삭제
export function deleteObjects(_canvas) {
    canvas = _canvas;
    var activeObject = canvas.getActiveObjects(),
        //여러개 선택하면 삭제 안되니깐 새로 패브릭으로 만들어주고 그걸 삭제                                   
        activeobjectGroup = new fabric.ActiveSelection(activeObject, {       
        canvas: canvas                                                              
        });
    if (activeobjectGroup) {
        activeobjectGroup.forEachObject(function(obj) {
        canvas.remove(obj);
        });
    }
    else { canvas.remove(activeObject); }

    canvas.discardActiveObject();
    canvas.requestRenderAll();
}
  
//캔버스 전체 캡쳐
export function captureButton(_canvas) {
    canvas = _canvas;
      var img = canvas.requestRenderAll();         
      setTimeout(() => {  
      var imgs = img.toDataURL('image/jpeg');                                              
      console.log(imgs);
      return imgs;
    }, 2000)
}

//컨버스의 크기를 브라우저에 맞게 조절하는 함수
export function canvasResize(_canvas) {
    canvas = _canvas;
    canvas.setHeight($('.canvas-wrapper').height());                                                  
    canvas.setWidth($('.canvas-wrapper').width());
}


//패브릭에 이미지태그에 비디오로 만드는놈을 새로 클래스로 정의해줌
fabric.CustomVideo = fabric.util.createClass(fabric.Image, {
    type: 'video',
    //비디오에 일부분만 짜를때 cropRect 를 활용
    cropRect: null,                                            
    
    initialize: function (video, options) {
      const defaultOpts = {
        lockRotation: false,                                    
        objectCaching: true,
        cacheProperties: ['time']
      }
      options = options || {}
        
      this.callSuper('initialize', video, 
                     Object.assign({}, defaultOpts, options))
    },
    _draw: function (video,ctx,w,h) {
      const c = this.cropRect
      const d = {
        x: -this.width/2,
        y: -this.height/2,
        w: this.width,
        h: this.height
      }
      if (c) {
        ctx.drawImage(video, d.x, d.y, d.w, d.h)
      } else {
        ctx.drawImage(video, d.x, d.y, d.w, d.h)
      }
    },
    
    _render: function (ctx) {
      // console.log('rendered', this.cropLeft)
      this._draw(this.getElement(), ctx)
    }
})
  