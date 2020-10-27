import React from 'react'  

//이미지를 HOME화면에 뿌려주는 컴포넌트
class ShowImage extends React.Component {

  render() {
    const reSize = 5/3;
      return (
        
          <img style={{
                position : 'fixed',
                left : this.props.data.zLeft * reSize +'px',
                top : this.props.data.top * reSize +'px',
                transform : `rotate(${this.props.data.angle}deg)`,
                }} 
                src ={this.props.data.src}
                width = {this.props.data.width * this.props.data.scaleX * reSize}
                height = {this.props.data.height * this.props.data.scaleY * reSize}
                draggable='false'
                alt = ""
          />

      );
  }
}

export default ShowImage;
