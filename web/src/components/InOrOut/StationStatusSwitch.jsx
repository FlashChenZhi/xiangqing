import {Button, Form, Input, Pagination, InputNumber, Select, message, } from 'antd';
import React from 'react';
const FormItem = Form.Item;
import reqwest from 'reqwest';
import {reqwestError, dateFormat} from '../common/Golbal';

const Option = Select.Option;


let StationStatusSwitch = React.createClass({
    getInitialState(){
        return {
            loading: false,
            selectedRowKeys: [],
            stationNo:this.stationNo,
            pattern:this.pattern,
        };
    },
    componentDidMount(){
        this.findPlatformSwitch("1101");
    },
    findPlatformSwitch(stationNo){
        reqwest({
            url: '/wcs/stationStatusChange/findStatusChange.do',
            dataType: 'json',
            method: 'post',
            data: {stationNo:stationNo},
            success: function (json) {
                if(json.success){
                    console.log(json.res);
                    this.props.form.setFieldsValue({
                        pattern:json.res,
                    });
                }
            }.bind(this),
            error: function (err) {
                message.error("初始化站台状态失败！");
                this.handleReset(err);
            }.bind(this)
        })
    },
    submit(e){
        e.preventDefault();
        this.props.form.validateFieldsAndScroll((err, values) => {
            if (!err) {
                console.log('Received values of form: ', values);
                let pattern= values.pattern;
                let stationNo= values.stationNo;
                reqwest({
                    url: '/wcs/stationStatusChange/updateStatusChange.do',
                    dataType: 'json',
                    method: 'post',
                    data: {pattern: pattern,stationNo:stationNo},
                    success: function (json) {
                        if(json.success){
                            message.success(json.msg);
                        } else {
                            message.error(json.msg);
                        }
                    }.bind(this),
                    error: function (err) {
                        message.error(err.msg);
                        this.handleReset(err);
                    }.bind(this)
                })
            }
        });
    },

    /**
     * 重置表单
     * @param e
     */
    handleReset(e) {
        console.log("进入清除！");
        this.props.form.resetFields();
    },
    onChange1(value){
        console.log(value);
        this.props.form.setFieldsValue({
            stationNo: value,
        });
        this.findPlatformSwitch(value);
    },
    onChange2(value){
        console.log('Change2'+value);
        this.setState({
            pattern:value,
        });
        this.props.form.setFieldsValue({
            pattern: value,
        });
    },

    render() {
        const {getFieldProps } = this.props.form;
        const formItemLayout = {
            labelCol: {span: 5},
            wrapperCol: {span: 14},
        };

        return (
            <div>
                <Form horizontal >
                    <br/><br/>
                    <FormItem
                        {...formItemLayout}
                        label="站台：">
                        <Select id="select" size="large" defaultValue="1101" style={{ width: 200 }}
                                {...getFieldProps('stationNo', { initialValue: "1101" })} onChange={this.onChange1}>
                            <Option value="1101">1101</Option>
                            <Option value="1102">1102</Option>
                        </Select>
                    </FormItem>
                    <FormItem
                        {...formItemLayout}
                        label="状态：">
                        <Select id="select" size="large" defaultValue="1" style={{ width: 200 }}
                                {...getFieldProps('pattern')} onChange={this.onChange2} >
                            <Option value="1">启用</Option>
                            <Option value="0">禁用</Option>
                        </Select>
                    </FormItem>
                    <br/><br/>
                    <FormItem wrapperCol={{offset: 6}}>
                        <Button type="primary" onClick={this.submit}
                            //disabled={this.state.tuopanhao.length > 0 ? false : true}
                        >设定</Button>
                    </FormItem>
                </Form>
            </div>
        );
    },
});

StationStatusSwitch = Form.create()(StationStatusSwitch);
export default StationStatusSwitch;

