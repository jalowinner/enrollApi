import React, { Component } from 'react'
import { init } from 'ityped'

export default class Welcome extends Component {
    constructor(props){
        super(props);
    }
  componentDidMount(){
    const myElement = document.querySelector('#welcomeElement')
    init(myElement, {  showCursor:false, strings: [this.props.username] , startDelay:666,typeSpeed:  150, disableBackTyping:true})
  }
  render(){
    return (
        <div className="content">
            <h1 className='welcometitle'>
                Welcome Home
            </h1>
            <div>
                <p className='welcome'>&lt;&nbsp;&nbsp;<span id="welcomeElement" className= 'welcome'></span><span className="ityped-cursor">_</span>&nbsp;/&gt;</p>
                
            </div>
        </div>
    )
  }
}