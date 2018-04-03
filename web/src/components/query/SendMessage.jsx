import {Button, Form, Table, Pagination, Select, message, Row, Col, Input, DatePicker} from 'antd';
import React from 'react';
import reqwest from 'reqwest';
import {reqwestError, dateFormat} from '../common/Golbal';
const FormItem = Form.Item;


let SendMessage = React.createClass({
    getInitialState() {
        return {
            data: [],
            total: 0,//表格数据总行数
            loading: false,
        }
    },
    componentDidMount(){

    },
    sendMessage(){
        this.setState({loading: true});
        const values = this.props.form.getFieldsValue();
        reqwest({
            url: '/wcs/webService/sendMessageHand.do',
            method: 'POST',
            data: {message: values.message},
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

    pageChange(noop){
        this.getTableData(noop);
    },
    handleSubmit(e) {
        e.preventDefault();
        this.sendMessage();
    },
    handleReset(e) {
        e.preventDefault();
        this.props.form.resetFields();
    },
    disabledDate(current){
        return current.getTime() > Date.now();
    },

    render() {

        const {getFieldProps} = this.props.form;
        const messageProps = getFieldProps('message');
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
                                label="消息："
                            >
                                <Input {...messageProps} style={{width: 400}}/>
                            </FormItem>
                        </Col>

                    </Row>
                    <FormItem>
                    </FormItem>
                    <FormItem wrapperCol={{offset: 10}}>
                        <Button type="primary" onClick={this.handleSubmit}>发送</Button>
                    </FormItem>
                </Form>
                <div>
                    <span>
                        <h3>Cycle命令</h3>
                        <li>01	返回原点 </li>
                        <li>02	取货</li>
                        <li>03	卸货</li>
                        <li>04	移动</li>
                        <li>05	接子车</li>
                        <li>06	卸子车</li>
                        <li>07	移载取货</li>
                        <li>08	移载卸货</li>
                        <li>09	上母车</li>
                        <li>10	下母车</li>
                        <li>11	载货移动</li>
                        <li>12	载货上车</li>
                        <li>13	载货下车</li>
                    </span>
                    <span>
                        <h3>作业区分</h3>
                        <li>01	入库</li>
                        <li>02	直行</li>
                        <li>03	整出库</li>
                        <li>04	拣选出库</li>
                        <li>05	补充出库</li>
                        <li>06	回库</li>
                        <li>07	充电  *4 新增</li>
                        <li>08	换层  *4 新增</li>
                        <li>09	充电完成 *4 新增</li>
                    </span>
                    <span>
                        <h3> 消息格式：</h3>  Mckey[4] + 机器号【4】+Cycle命令【2】+ 作业区分【2】+货形(高度)【1】+  货形(宽度)【1】+ 货位排列层【6】+ 站台【4】+ 码头【4】
                    </span>

                </div>
            </div>
        );
    },
});
SendMessage = Form.create({})(SendMessage);
export default SendMessage;
