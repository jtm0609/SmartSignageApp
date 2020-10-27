import React, { Fragment } from 'react';
//이게 있어야 util 사용가능
import 'fabric';  
import DesignCanvas from './DesignCanvas';
import './MakeCustom.css';

//MAKE목록을 눌렀을 때 메인서버주소를 props로 넘겨주면서 DesignCanvas를 뿌려줌
class MakeCustom extends React.Component {
    
  render() {

      return (

        <Fragment>

          <DesignCanvas mainUrl= {this.props.mainUrl} />
        
        </Fragment>

      );   
  }
}
  
export default MakeCustom;