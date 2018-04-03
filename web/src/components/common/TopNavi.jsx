import React from 'react';
import {Row, Col, Menu, Dropdown, Icon, Button, message} from 'antd';
import {redirect, reqwestError, clearLocalStorage} from './Golbal';
import reqwest from 'reqwest';
import UpdatePwdModel from './model/UpdatePwdModel';

export default class TopNavi extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            loginUserName: "",
            visibleModelName: ""
        };
    }

    componentDidMount() {
        this.loadLoginUserName();
    }

    loadLoginUserName() {
        this.setState({loginUserName: "WAP"});

        // const url = getReqUrl('users/getUserName.do');
        // reqwest({
        //     url: url,
        //     type: 'json',
        //     method: 'get',
        //     success: function (json) {
        //         if (json.success) {
        //             this.setState({loginUserName: json.msg});
        //         }
        //     }.bind(this),
        //     error: function (err) {
        //         reqwestError(err);
        //     }.bind(this)
        // })
    }

    isModelVisable(modelName) {
        if (modelName === undefined || modelName === null || modelName === '')
            return false;
        const visibleModelName = this.state.visibleModelName;
        if (visibleModelName === modelName)
            return true;
        return false;
    }

    showModel(value) {
        this.setState({visibleModelName: value});
    }

    hideModel() {
        this.setState({visibleModelName: ""});
    }

    logout() {
        clearLocalStorage();
        redirect('/login');
        // const url = getReqUrl('users/logout.do');
        // reqwest({
        //     url: url,
        //     type: 'json',
        //     method: 'post',
        //     success: function (json) {
        //         if (json.success) {
        //             clearLocalStorage();
        //             redirect('/login');
        //         } else {
        //             message.error(json.msg);
        //         }
        //     }.bind(this),
        //     error: function (err) {
        //         reqwestError(err);
        //     }.bind(this)
        // })
    }

    render() {
        let dropdownMenus;
        let menus = [];
        if (this.state.loginUserName) {
            menus.push(
                <Menu.Item key="info_updatePwd">
                    <a onClick={this.showModel.bind(this, "UpdatePwdModel")}>修改密码</a>
                </Menu.Item>
            );
        }
        menus.push(
            <Menu.Item key="info_logout">
                <a onClick={this.logout}>退出系统</a>
            </Menu.Item>
        );
        dropdownMenus = (<Menu>{menus}</Menu>);
        const updatePwdModelVisible = this.isModelVisable("UpdatePwdModel");

        return (
            <Row>
                <Col span={12}>
                    <div className="ant-layout-ceiling-demo">
                        <div className="ant-layout-ceiling">
                            <div className="ant-layout-wrapper">
                                <ul className="left">
                                    <li><h2></h2></li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </Col>
                <Col span={12}>
                    <div className="ant-layout-ceiling-demo">
                        <div className="ant-layout-ceiling">
                            <div className="ant-layout-wrapper">
                                <ul className="right">
                                    <li>
                                        <Dropdown overlay={dropdownMenus} trigger={['click']}>
                                            <a className="ant-dropdown-link">
                                                <Icon type="user"/>
                                                {this.state.loginUserName}
                                                <Icon type="down"/>
                                            </a>
                                        </Dropdown>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <UpdatePwdModel visible={updatePwdModelVisible} hideModel={this.hideModel.bind(this)}/>
                </Col>
            </Row>
        );
    }
}
