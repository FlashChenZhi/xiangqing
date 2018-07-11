import {Button,Modal,Icon , Form, Input,Radio, Table, Badge, DatePicker, Select, message, Row, Col} from 'antd';
import React from 'react';
const FormItem = Form.Item;
import reqwest from 'reqwest';
import {reqwestError, dateFormat} from '../common/Golbal';
const Option = Select.Option;
const confirm = Modal.confirm;
const RangePicker = DatePicker.RangePicker;
const RadioGroup = Radio.Group;
var columns2 ="";
let OutputArea = React.createClass({
    getInitialState(){
        return {
            lotNum: "",//表格数据
            isManual: "",//表格数据总行数
            commodityCodeList:[],
            commodityCodeFirst:"",

        };
    },
    componentDidMount(){
        this.getData(1);
        this.getCommodityCode();

    },
    getCommodityCode(){
        reqwest({
            url: '/wms/master/FindOutOrInWarehouseAction/getSkuCode',
            dataType: 'json',
            method: 'post',
            data: {},
            success: function (json) {
                if(json.success) {
                    this.setState({
                        commodityCodeList: json.res,
                        commodityCodeFirst: json.res[0],
                    })
                }else{
                    message.error("初始化Sku代码失败！");
                }
            }.bind(this),
            error: function (err) {
                message.error("初始化Sku代码失败！");
            }.bind(this)
        })
    },

    onChange(selectedRowKeys, selectedRows) {
        this.setState({selectedData: selectedRows, selectedRowKeys: selectedRowKeys});
    },
    handleReset(e) {
        this.props.form.resetFields();
        this.setState({selectedRowKeys: [], selectedData: []});
        this.getData(1);
    },
    reset(e){
        e.preventDefault();
        this.setState({selectedRowKeys: [], selectedData: []});
    },
    getData(skuCode){
        reqwest({
            url: '/wms/master/updateSkuLotNumAction/findSkuCodeLotNum',
            dataType: 'json',
            method: 'post',
            data: {skuCode:skuCode},
            success: function (json) {
                if(json.success){
                    console.log(json.res);
                    this.setState({
                        lotNum: json.res.lotNum,
                        isManual: json.res.isManual
                    });
                }else{
                    message.error("加载数据失败！");
                }
            }.bind(this),
            error: function (err) {
                reqwestError(err);
                message.error("加载数据失败！");
            }.bind(this)
        });
    },
    handleSubmit(e) {
        e.preventDefault();
        this.props.form.validateFieldsAndScroll((err, values) => {
            if (!err) {
                console.log('Received values of form: ', values);
                let skuCode= values.skuCode;
                let isManual= values.isManual;
                let lotNum= values.lotNum;
                reqwest({
                    url: '/wms/master/updateSkuLotNumAction/updateSkuCodeLotNum',
                    dataType: 'json',
                    method: 'post',
                    data: {skuCode: skuCode,isManual:isManual,lotNum:lotNum},
                    success: function (json) {
                        if (!json.success) {
                            message.error(json.msg);
                        } else {
                            message.success("设定成功！");
                            window.location.reload();

                        }
                        this.props.form.setFieldsValue({
                            tuopanhao:'',
                        });
                    }.bind(this),
                    error: function (err) {
                        message.error("设定任务失败！");
                        this.handleReset(e);
                    }.bind(this)
                })
            }
        });

    },

    render() {


        const {getFieldProps} = this.props.form;
        const commodityCodeProps = getFieldProps('skuCode', {
            initialValue:"1",
        });
        const lotNoProps = getFieldProps('lotNum',{ initialValue: this.state.lotNum });
        const formItemLayout = {
            labelCol: {span: 5},
            wrapperCol: {span: 14},
        };
        const radioGroupProps= getFieldProps('isManual',{initialValue:this.state.isManual});
        const commodityCodeListSelect =[];
        this.state.commodityCodeList.forEach((commodityCode)=>{
            commodityCodeListSelect.push(<Option value={commodityCode.skuCode}>{commodityCode.skuName}</Option>);
        });
        return (
            <div>
                <Form horizontal>
                    <Row>
                        <Col lg={12}>
                            <FormItem
                                {...formItemLayout}
                                label="商品名称："
                            >
                                <Select
                                    showSearch
                                    filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}
                                    id="select" size="large" style={{ width: 200 }}
                                    {...commodityCodeProps} >
                                    {commodityCodeListSelect}
                                </Select>
                            </FormItem>
                            <FormItem
                                {...formItemLayout}
                                label="批次管理"
                            >
                                <RadioGroup {...radioGroupProps}>
                                    <Radio value="0">自动</Radio>
                                    <Radio value="1">手动</Radio>
                                </RadioGroup>
                            </FormItem>
                            <FormItem
                                {...formItemLayout}
                                label="批号："
                            >

                                <Input style={{width:"300"}}
                                       {...lotNoProps}   placeholder="请输入批号" />
                            </FormItem>
                            <FormItem wrapperCol={{offset: 10}}>
                                <Button type="primary" onClick={this.handleSubmit}>提交</Button>
                            </FormItem>
                        </Col>
                    </Row>

                </Form>
            </div>
        );
    },
});
OutputArea = Form.create({})(OutputArea);
export default OutputArea;
