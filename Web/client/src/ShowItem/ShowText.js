import React from 'react'  

let textScale;
//텍스트를 HOME화면에 뿌려주는 컴포넌트
class ShowText extends React.Component {
  

  render() {

    textScale = this.props.data.scaleX
    const reSize = 5/3;   
      return (

          <div className="svg-wrapper" 
                style={{
                  position : 'fixed',
                  left : this.props.data.zLeft * reSize+'px',
                  top : this.props.data.top * reSize+'px',
                  transform : `rotate(${this.props.data.angle}deg)`,
                  width : this.props.data.width * this.props.data.scaleX * reSize+'px',
                  height : this.props.data.height * this.props.data.scaleY * reSize+'px',
                  backgroundColor : this.props.data.textBackColor
                  }}
          >

            <svg style={{
                  fontSize : 80 * textScale,
                  fontWeight: 'normal',
                  }}
                  width='100%' 
                  height='100%'
            >

              <text fill= {this.props.data.fill} 
                    stroke={this.props.data.stroke} 
                    strokeWidth={this.props.data.strokeWidth * 2} 
                    fontFamily={this.props.data.fontFamily} 
                    textLength='100%' 
                    y = '80%'
              >{this.props.data.zText}
              </text>
            
            </svg>
        
          </div>
       
      );
  }
}

export default ShowText;
