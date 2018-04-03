import {Button, Form, Table, Pagination, Select, message, Row, Col, Input, DatePicker, Switch} from 'antd';
import React from 'react';
import reqwest from 'reqwest';
import {reqwestError, dateFormat} from '../common/Golbal';
const FormItem = Form.Item;

let TransferPanel = React.createClass({
    getInitialState() {
        return {
            data: [],
            total: 0,//表格数据总行数
            loading: false,
            autoInput:false,
        }
    },
    componentDidMount(){
        this.setState({autoInput:false});
    },
    componentWillUnmount() {
        clearInterval(this.interval);
    },

    handleSubmit(e) {
        e.preventDefault();
        const values = this.props.form.getFieldsValue();
        reqwest({
            url: '/wms/inventory/transfer.do',
            dataType: 'json',
            method: 'post',
            data: values,
            error: err => {
                message.error('网络异常,请稍后再试');
            },
            success: resp => {
                if (resp.success) {
                    message.success(resp.msg);
                } else {
                    message.error(resp.msg);
                }
            }
        });
    },

    handleReset(e) {
        e.preventDefault();
        this.props.form.resetFields();
    },
    disabledDate(current){
        return current.getTime() > Date.now();
    },

    send(id) {
        reqwest({
            url: '/wcs/webService/sendMsg.do',
            method: 'POST',
            data: {id: id},
            type: 'json',
            error: err => {
                message.error('网络异常,请稍后再试');
            },
            success: resp => {
                if (resp.success) {
                    message.success(resp.msg);
                } else {
                    message.error(resp.msg);
                }
            }
        });
    },

    render() {

        const {getFieldProps} = this.props.form;
        const barCodeProps = getFieldProps('barCode');
        const toLocationProps = getFieldProps('toLocation');
        const formItemLayout = {
            labelCol: {span: 5},
            wrapperCol: {span: 14},
        };
        return (
            <div>
                <Form horizontal>
                    <Row>
                        <Col lg={12}>
                            <FormItem
                                {...formItemLayout}
                                label="托盘号："
                            >
                                <Input {...barCodeProps}/>
                            </FormItem>
                            <FormItem
                                {...formItemLayout}
                                label="目标货位："
                            >
                                <Input {...toLocationProps}/>
                            </FormItem>
                        </Col>

                    </Row>
                    <FormItem>
                    </FormItem>
                    <FormItem wrapperCol={{offset: 10}}>
                        <Button type="primary" onClick={this.handleSubmit}>设定</Button>
                        &nbsp;&nbsp;&nbsp;
                        <Button type="ghost" onClick={this.handleReset}>重置</Button>
                    </FormItem>
                </Form>
            </div>
        );
    },
});
TransferPanel = Form.create({})(TransferPanel);
export default TransferPanel;
