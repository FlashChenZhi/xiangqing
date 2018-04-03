import React from 'react';
import {Link} from 'react-router';
import {Row, Menu, Breadcrumb, Button, Form, Input, Checkbox, Icon, message, Tooltip} from 'antd';
import {redirect, reqwestError, clearLocalStorage} from '../common/Golbal';
import reqwest from 'reqwest';
import md5 from 'md5';
const FormItem = Form.Item;

class LoginForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            loading: false
        };
    }

    componentDidMount() {
        clearLocalStorage();
    }

    handleSubmit(e) {
        e.preventDefault();
        this.props.form.validateFields((errors, values) => {
            if (!!errors) {
                return;
            }
            let userName = values.userName;
            let pwd = values.password;
            if (userName == 'wap' && pwd == 'wap') {
                redirect('/home');
            } else {
                message.error("用户名密码不正确");
            }
            // let pwd = md5(values.password);
            // let url = getReqUrl('users/login.do?userName=' + userName + '&pwd=' + pwd);
            // this.setState({loading: true});
            // reqwest({
            //     url: url,
            //     type: 'json',
            //     method: 'post',
            //     success: function (json) {
            //         this.setState({loading: false});
            //         if (json.success) {
            //             redirect('/home');
            //         } else {
            //             message.error(json.msg);
            //         }
            //     }.bind(this),
            //     error: function (err) {
            //         this.setState({loading: false});
            //         reqwestError(err);
            //     }.bind(this)
            // })
        });
    }

    render() {
        const {getFieldProps} = this.props.form;
        const userAddon = (<Icon type="user"/>);
        const pwdAddon = (<Icon type="lock"/>);
        const nameProps = getFieldProps('userName', {
            rules: [
                {required: true, message: '请输入用户名'}
            ]
        });
        const passwdProps = getFieldProps('password', {
            rules: [
                {required: true, whitespace: true, message: '请输入密码'}
            ]
        });
        const loading = this.state.loading;
        const loadIcon = loading ? 'loading' : '';
        return (
            <div className="login-body">
                <div className="login-main">
                    <div className="login-title">
                        <h1 className="login-title-color">
                            WMS
                        </h1>
                    </div>
                    <div className="login-cnt">
                        <Form >
                            <Input.Group className="login-from-group">
                                <FormItem>
                                    <Input {...nameProps} addonBefore={userAddon} autoComplete="off" size="large"
                                           placeholder="用户名"/>
                                </FormItem>
                                <FormItem>
                                    <Input  {...passwdProps} addonBefore={pwdAddon} size="large" type="password"
                                            placeholder="密码"/>
                                </FormItem>
                                <Button size="large" type="primary" onClick={this.handleSubmit.bind(this)}
                                        icon={loadIcon}> {loading ? '登录中...' : '登录'} </Button>
                            </Input.Group>
                        </Form>
                    </div>
                </div>
            </div>
        );
    }
}

LoginForm = Form.create({})(LoginForm);

export default LoginForm;