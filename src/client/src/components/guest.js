import React, { Component } from 'react'
import { init } from 'ityped'

export default class Hello extends Component {
  componentDidMount(){
    const myElement = document.querySelector('#myElement')
    init(myElement, { showCursor: false, strings: ['Welcome Guest!', 'Please login to start your journey.' ] , typeSpeed:50,disableBackTyping:true})
  }
  render(){
    return (
        <div className="content">
            <h1 className="title">XUE.app</h1>
            <div>
                <span id="myElement" className="ityped"></span>
                <span className="ityped-cursor">|</span>
            </div>
        </div>
    )
  }
}