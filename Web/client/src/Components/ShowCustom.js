import React, { Fragment } from 'react'; 
import './MakeCustom.css'
import ShowImage from '../ShowItem/ShowImage.js';
import ShowVideo from '../ShowItem/ShowVideo.js';
import ShowText from '../ShowItem/ShowText.js';

//HOME의 상태가 CUSTOM 일때 뿌려주는 부분
class ShowCustom extends React.Component {

  render() {
 
      return (
        //각각 이미지, 비디오, 텍스트를 매핑해서 전부 다 뿌려줌
        <Fragment>
          
          <div className='back' style={{ backgroundColor : this.props.datas[0].background }}/> 
            {
              this.props.datas.map((data) => {
                  if (data.zType === 'video') {
                    return ( <ShowVideo data={data} /> );
                  }
                  else if (data.zType === 'image'){
                    return ( <ShowImage data={data} /> );
                  }
                  else if (data.zType === 'i-text'){
                    return ( <ShowText data={data} /> )
                  }
                return false;
              })
            }

        </Fragment>

      );
    }
  } 

  
export default ShowCustom;
