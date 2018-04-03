import React from 'react';

export default class LoginMain extends React.Component {
    constructor(props) {
        super(props);
        this.state = {};
    }

    render() {
        const clientHeight = document.body.clientHeight;
        return (
            <div className="login-home" style={{minHeight: +clientHeight + "px"}}>
                {this.props.children}
            </div>
        );
    }
}