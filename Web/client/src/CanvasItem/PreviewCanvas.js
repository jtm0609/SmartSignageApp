import React, { Fragment } from 'react'  
import * as Cfunc from '../utils/CanvasFunction.js';

class PreviewCanvas extends React.Component {


  render() {

      return ( 
        <Fragment>  
            <div className="previewcontainer" style={{
              width :"100%",
              height : "100%",
              backgroundColor : "#333",
              opacity : "0.3", 
              position : "fixed",
              left : "0",
              top : "0",
              zIndex : "10"
            }}/>

          
          </Fragment>
      );
  }
}

export default PreviewCanvas;
