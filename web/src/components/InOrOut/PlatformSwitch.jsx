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
            stationNo:this.stationNo,
            direction:this.direction,
            stationNos:[],
        };
    },
    componentDidMount(){
        this.findPlatformSwitch();
    },
    findPlatformSwitch(){
        let stationNo = this.state.stationNo;
        let direction=this.state.direction;
        console.log(stationNo);
        reqwest({
            url: '/wcs/platformSwitch/findPlatformSwitch.do',
            dataType: 'json',
            method: 'post',
            data: {stationNo:stationNo,direction:direction},
            success: function (json) {
                if(json.success){
                    if(json.stationNo=="1303"){
                        this.props.form.setFieldsValue({
                            stationNo: stationNo,
                        });
                    }
                    if(json.direction=="1"){
                        this.props.form.setFieldsValue({
                            direction: direction,
                        });
                    }else if (json.direction=="0"){
                        this.props.form.setFieldsValue({
                            direction: direction,
                        });
                    }
                }
                // else{
                //     message.error("初始化站台模式失败！");
                // }
            }.bind(this),
            error: function (err) {
                message.error("初始化站台模式失败！");
                this.handleReset(err);
            }.bind(this)
        })
    },
    submit(e){
        e.preventDefault();
        this.props.form.validateFieldsAndScroll((err, values) => {
            if (!err) {
                console.log('Received values of form: ', values);
                let direction= values.direction;
                let stationNo= values.stationNo;
                reqwest({
                    url: '/wcs/platformSwitch/updatePlatformSwitch.do',
                    dataType: 'json',
                    method: 'post',
                    data: {stationNo: stationNo,direction:direction},
                    success: function (json) {
                        if (!json.success) {
                            message.error(json.msg);
                        } else {
                            message.success(json.msg);
                        }
                        // this.handleReset(e);
                    }.bind(this),
                    error: function (err) {
                        message.error("模式切换失败！");
                        this.handleReset(err);
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
    onChange(value){
        console.log(value);
        this.setState({
            stationNo:value,
        });
        this.props.form.setFieldsValue({
            stationNo: value,
        });
    },

    render() {
        const {getFieldProps } = this.props.form;
        const formItemLayout = {
            labelCol: {span: 5},
            wrapperCol: {span: 14},
        };
        const commodityCodeListSelect =[];
        commodityCodeListSelect.push(<Option value="">---请选择---</Option>);
        this.state.stationNos.forEach((commodityCode)=>{
            commodityCodeListSelect.push(<Option value={commodityCode.skuCode}>{commodityCode.skuName}</Option>);
        });
        return (
            <div>
                <Form horizontal >
                    <br/><br/>
                    <FormItem
                        {...formItemLayout}
                        label="站台："
                    >
                        <Select id="select" size="large" defaultValue="1303" style={{ width: 200 }}
                                {...getFieldProps('stationNo', { initialValue: '1303' })} onChange={this.onChange}>
                            <Option value="1303">1301</Option>
                        </Select>
                    </FormItem>
                    <FormItem
                        {...formItemLayout}
                        label="转向："
                    >
                        <Select id="select" size="large" defaultValue="1" style={{ width: 200 }}
                                {...getFieldProps('direction', { initialValue: '1' })} >
                            <Option value="1">1201站台</Option>
                            <Option value="0">1203站台</Option>
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
