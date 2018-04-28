import {Button, Form, Input, Pagination, InputNumber, Select, message, } from 'antd';
import React from 'react';
const FormItem = Form.Item;
import reqwest from 'reqwest';
import {reqwestError, dateFormat} from '../common/Golbal';

const Option = Select.Option;


let PlatformSwitch = React.createClass({
    getInitialState(){
        return {
            loading: false,
            selectedRowKeys: [],
            zhantai:"1301",
        };
    },
    componentDidMount(){
        this.findPlatformSwitch();
    },
    findPlatformSwitch(){
        let zhantai = this.state.zhantai;
        console.log(zhantai);
        reqwest({
            url: '/wcs/platformSwitch/findPlatformSwitch.do',
            dataType: 'json',
            method: 'post',
            data: {zhantai:zhantai},
            success: function (json) {
                if(json.success){

                    if(json.res=="03"){
                        this.props.form.setFieldsValue({
                            pattern: "03",
                        });
                    }else if (json.res=="01"){
                        this.props.form.setFieldsValue({
                            pattern: "01",
                        });
                    }
                }else{
                    message.error("初始化站台模式失败！");
                }
            }.bind(this),
            error: function (err) {
                message.error("初始化站台模式失败！");
                this.handleReset(e);
            }.bind(this)
        })
    },
    submit(e){
        e.preventDefault();
        this.props.form.validateFieldsAndScroll((err, values) => {
            if (!err) {
                console.log('Received values of form: ', values);
                let pattern= values.pattern;
                let zhantai= values.zhantai;
                reqwest({
                    url: '/wcs/platformSwitch/updatePlatformSwitch.do',
                    dataType: 'json',
                    method: 'post',
                    data: {pattern: pattern,zhantai:zhantai},
                    success: function (json) {
                        if (!json.success) {
                            message.error(json.msg);
                        } else {
                            message.success("模式切换成功！");
                        }
                        // this.handleReset(e);
                    }.bind(this),
                    error: function (err) {
                        message.error("模式切换失败！");
                        this.handleReset(e);
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
    onChange(value){
        console.log(value);
        this.setState({
            zhantai:value,
        });
        this.props.form.setFieldsValue({
            zhantai: value,
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
                        label="站台："
                    >
                        <Select id="select" size="large" defaultValue="1301" style={{ width: 200 }}
                                {...getFieldProps('zhantai', { initialValue: '1301' })} onChange={this.onChange}>
                            <Option value="1301">1301</Option>
                        </Select>
                    </FormItem>
                    <FormItem
                        {...formItemLayout}
                        label="模式："
                    >
                        <Select id="select" size="large" defaultValue="01" style={{ width: 200 }}
                                {...getFieldProps('pattern', { initialValue: '01' })} >
                            <Option value="01">入库</Option>
                            <Option value="03">出库</Option>
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
PlatformSwitch = Form.create()(PlatformSwitch);
export default PlatformSwitch;
