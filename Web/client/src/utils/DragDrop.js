//윈도우 안에 패브릭을 넣어줌
const fabric = window.fabric;
//제이쿼리를 사용하기로함                             
var $ = require('jquery');
//캔버스를 전역으로 선언
let canvas = null;

//이미지와 비디오의 좌표를 얻기위해 선언
var imageOffsetX, imageOffsetY;                                     
var videoOffsetX, videoOffsetY;
//필요한 쿼리 재료들을 전부 전역으로 빼줌
let canvas_container = null;
let canvasObject = null;
let canvasContainer = null;

//태그달고있는 전체 NodeList로 반환

let images = null;       
let videos = null;       

//드래그 앤 드랍을 각 이미지 동영상 별로 실행 시키기 위해 전역으로 export
export function dragAndDrop(_canvas) {       
    if (_canvas) { canvas = _canvas; }

    // 태그달고있는 전체 NodeList로 반환
    images = document.querySelectorAll(".addimage img");          
    videos = document.querySelectorAll(".addvideo video");
    // console.log(images);
    // console.log(videos);    
    console.log($(".canvas-container").width());
    console.log($(".canvas-container").height());
    //$(".canvas-container") 가아니라 $(.'canvas-wrapper') 이거에 대해 리스닝해야 컨버스에 올라갔을때만 드랍이 실행 **
    canvas_container = document.getElementsByClassName('canvas-wrapper')[0];

    //console.log(document.getElementsByClassName('canvas-container')[0]);
    canvasObject = $('canvas',canvas_container)[0];
    //console.log(canvasObject);
    canvasContainer = $(canvas_container)[0];
    //addimage img 태그 단 아이템 전부 에 대해 각각 start, end 리스너 실행
    [].forEach.call(images, function(img) {
        img.addEventListener("dragstart", handleDragStart, false);
        img.addEventListener("dragend", handleDragEnd, false);
    });
    //addvideo vid 태그 단 아이템 전부 에 대해 start,end 리스너 실행
    [].forEach.call(videos, function(vid) {
        vid.addEventListener("dragstart", handleDragStart, false);
        vid.addEventListener("dragend", handleDragEnd, false);
    });
    //컨버스 내에서 enter , over , leave , drop 리스너 실행
    if(canvasContainer) {
        canvasContainer.addEventListener("dragenter", handleDragEnter, false);
        canvasContainer.addEventListener("dragover", handleDragOver, false);
        canvasContainer.addEventListener("dragleave", handleDragLeave, false);
        canvasContainer.addEventListener("drop", handleDrop, false);
    }
}

//넣을 이미지를 클릭하고 옮기는 딱 start시점에 발생
function handleDragStart(e) {                                       
    console.log('DragStart');
    //모든 img중에 classList 에서 dragging 남아있는거 지움
    [].forEach.call(images, function(img) {
      img.classList.remove("img_dragging");                            
    });
    //태그이름 따려고 따는놈 반환값= VIDEO OR IMG
    if ($(this).prop('tagName') === 'IMG'){
    //태그이름이 이미지일때만 실행해서 classList에 img_dragging 넣어줌
      this.classList.add("img_dragging");                            
     //내가 놓은 위치에 이미지가 그 위치에 안착하게 값 계산  
    var imageOffset = $(this).offset();
        imageOffsetX = e.clientX - imageOffset.left;                 
        imageOffsetY = e.clientY - imageOffset.top;
    }
  
   
    [].forEach.call(videos, function(vid) {
        vid.classList.remove("vid_dragging");                         //이미지랑 마찬가지
      });
      
    if ($(this).prop('tagName') === 'VIDEO'){
      this.classList.add("vid_dragging");
  
      var videoOffset = $(this).offset();
      videoOffsetX = e.clientX - videoOffset.left;             
      videoOffsetY = e.clientY - videoOffset.top;
    }
}

//넣을 이미지가 canvas위에서 자리이동할때 불리는함수 (디폴트로 페이지 변경 방지)
function handleDragOver(e) {                                       
    //console.log('drag over');
    if (e.preventDefault) {
      e.preventDefault();
    }
    e.dataTransfer.dropEffect = "copy";
    return false;
}

//넣을 이미지가 canvas안에 들어가는 시점에서 발생되는 함수 (사실 쓰는일 없음)
function handleDragEnter(e) {                                      
    //console.log('drag enter');
    canvas_container.classList.add("over");
}

//넣을 이미지가 canvas에서 나오는 시점에서 발생되는함수 (사실 안쓰임)
function handleDragLeave(e) {                                       
    //console.log('drag leave');
    //컨버스내 모든 아이템의 classList에서 지워줌
    canvas_container.classList.remove("over");
}

//넣을 이미지가 canvas위에 드랍됬을때 호출되는함수 (이미지 , 비디오 추가)
function handleDrop(e) {                                     
    // console.log('drag drop');
    e = e || window.event;
    if (e.preventDefault) {
        e.preventDefault();
    }
    if (e.stopPropagation) {
        e.stopPropagation();
    }
    //드래그 중일때만 classList에 _dragging 있으니 그 태그 선택
    var img = document.querySelector(".addimage img.img_dragging");                   
    var vid = document.querySelector(".addvideo video.vid_dragging");

    // console.log("event: ", e);

    var offset = $(canvasObject).offset();
    
     //이미지 or 동영상 추가될 때 드랍되는 좌표값
    var iy = e.clientY - (offset.top + imageOffsetY);    
    var ix = e.clientX - (offset.left + imageOffsetX);                                 

    var vy = e.clientY - (offset.top + videoOffsetY);    
    var vx = e.clientX - (offset.left + videoOffsetX);
    //태그이름이 이미지 일때만 실행해서 추가
    if ($(img).prop('tagName') === 'IMG') {
        //이미지 패브릭 만듬
        var newImage = new fabric.Image(img, { left: ix, top: iy });    
        canvas.add(newImage);
        //HTML 태그에서 명시한 width height을 scale화 해서 드랍되는 아이템에 사이즈 수정
        newImage.scaleToWidth(img.width);                                             
        newImage.scaleToHeight(img.height);
    }

    //태그이름이 비디오 일때만 실행해서 추가
    else if ($(vid).prop('tagName') === 'VIDEO') {
        //비디오 패브릭 만듬                                    
        var newVideo = new fabric.CustomVideo(vid, { left: vx, top: vy });
        //cropRect: {x: 200, y: 50, w: 200, h: 200}
        canvas.add(newVideo); 
        newVideo.scaleToWidth(vid.width);                                             
        newVideo.scaleToHeight(vid.height);    
        
        //비디오의 장면을 현재시간으로 계속 셋팅해서 플레이시킴
        function playTrigger() {
        var vid = $(".addvideo video.vid_dragging");
        if(vid.paused || vid.ended) return false;                                  
            newVideo.set('time', vid.currentTime);
            // console.log('current time:', v.currentTime)
            canvas.renderAll();
            setTimeout(playTrigger,20)
        }   

        //만들어진 패브릭에 대해서 플레이트리거를 리스너해서 똑같이 적용되게 만듬
        newVideo.getElement().addEventListener('play', playTrigger(), false);           
        newVideo.getElement().play();
    }
    return false;
}

//마우스에서 손이 때졌을때 실행
function handleDragEnd(e) {
    //console.log('Dragend');
    images = document.querySelectorAll(".addimage img");            
    videos = document.querySelectorAll(".addvideo video");
    //선택한 태그의 모든 아이템에 대해 dragging을 지움 
    [].forEach.call(images, function(img) {
        img.classList.remove("img_dragging");
    });

    [].forEach.call(videos, function(vid) {
        vid.classList.remove("vid_dragging");
    });
}

//드랍이 완료될때마다 이벤트 리스너를 지워주는 함수
export function removeCanvasEvent() {

    [].forEach.call(images, function(img) {
        img.removeEventListener("dragstart", handleDragStart, false);
        img.removeEventListener("dragend", handleDragEnd, false);
    });

    [].forEach.call(videos, function(vid) {
        vid.removeEventListener("dragstart", handleDragStart, false);
        vid.removeEventListener("dragend", handleDragEnd, false);
    });

    if(canvasContainer) {
        canvasContainer.removeEventListener("dragenter", handleDragEnter, false);
        canvasContainer.removeEventListener("dragover", handleDragOver, false);
        canvasContainer.removeEventListener("dragleave", handleDragLeave, false);
        canvasContainer.removeEventListener("drop", handleDrop, false);
    }
}

