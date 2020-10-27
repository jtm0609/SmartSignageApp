import React from 'react';
//웹서버 링크, 로컬 둘다 플레이 가능 
import ReactPlayer from 'react-player';      

//HOME의 상태가 AI일때 보여주는 비디오
class ShowAI extends React.Component {
  
    render () {
        return (
          <div className="Player">
            
            <ReactPlayer padding='0' 
                         margin='0' 
                         width='100%' 
                         height='100%'
                         url={this.props.datas[0].src} 
                         loop playing
            />

          </div>
        );
    }
} 

  
export default ShowAI;

