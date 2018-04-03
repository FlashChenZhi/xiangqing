import React from 'react';
import {Form, Row, Col, Cascader, InputNumber, Input, Select, Button, Icon, message, Modal} from 'antd';
import {reqwestError} from '../Golbal';
import reqwest from 'reqwest';
import md5 from 'md5';
const createForm = Form.create;
const FormItem = Form.Item;

class UpdatePwdModel extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            loading: false,
            visible: false
        };
    }

    componentWillReceiveProps(props) {
        if (props.visible && !this.state.visible) {
            this.showModel();
        }
    }

    showModel() {
        this.setState({visible: true, loading: false});
    }

    hideModel() {
        this.props.form.resetFields();
        this.setState({visible: false, loading: false});
        this.props.hideModel();
    }

    onModelConfirm() {
        this.props.form.validateFields((errors, values) => {
            if (!!errors) {
                return;
            }
            const oldPwd = md5(values.oldPwd);
            const password = md5(values.password);
            const url = getReqUrl('users/updatePassword.do');
            this.setState({loading: true});
            reqwest({
                url: url,
                type: 'json',
                method: 'post',
                data: {oldPwd, password},
                success: function (json) {
                    this.setState({loading: false});
                    if (json.success) {
                        message.success(json.msg);
                        this.hideModel();
                    } else {
                        message.error(json.msg);
                    }
                }.bind(this),
                error: function (err) {
                    this.setState({loading: false});
                    reqwestError(err);
                }.bind(this)
            })

        });
    }

    checkRePass(rule, value, callback) {
        const {getFieldValue} = this.props.form;
        if (value && value !== getFieldValue('password')) {
            callback('两次输入密码不一致！');
        } else {
            callback();
        }
    }

    render() {
        const {getFieldProps} = this.props.form;
        const formItemLayout = {
            labelCol: {span: 6},
            wrapperCol: {span: 18}
        };

        const oldPwdProps = getFieldProps('oldPwd', {
            rules: [
                {required: true, message: '请输入原密码'}
            ]
        });

        const passwordProps = getFieldProps('password', {
            rules: [
                {required: true, message: '请输入新密码'}
            ]
        });
        const confirmPwdProps = getFieldProps('confirmPwd', {
            rules: [
                {required: true, whitespace: true, message: '请输入确认密码'},
                {validator: this.checkRePass.bind(this)}
            ]
        });

        return (
            <Modal title="修改密码"
                   visible={this.state.visible}
                   onOk={this.onModelConfirm.bind(this)}
                   onCancel={this.hideModel.bind(this)}
                   confirmLoading={this.state.loading}
            >
                <Form horizontal>
                    <Row gutter={16}>
                        <Col sm={24}>
                            <FormItem
                                label="原始密码"
                                {...formItemLayout}
                                hasFeedback
                            >
                                <Input {...oldPwdProps} type="password"/>
                            </FormItem>
                            <FormItem
                                label="新密码"
                                {...formItemLayout}
                                hasFeedback
                            >
                                <Input {...passwordProps} type="password"/>
                            </FormItem>
                            <FormItem
                                label="确认密码"
                                {...formItemLayout}
                                hasFeedback
                            >
                                <Input {...confirmPwdProps} type="password"/>
                            </FormItem>
                        </Col>
                    </Row>
                </Form>
            </Modal>
        );
    }
}
UpdatePwdModel = createForm()(UpdatePwdModel);
export default UpdatePwdModel;