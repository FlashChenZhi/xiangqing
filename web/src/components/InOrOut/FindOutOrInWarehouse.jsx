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
            data: [],//表格数据
            total: 0,//表格数据总行数
            selectedData: [],//点击设定提交到后台的数据
            loading: false,
            selectedRowKeys: [],
            defaultPageSize:10,
            commodityCodeList:[],
            commodityCodeFirst:"",
            shipperIdList:[],
            shipperIdFirst:"",
            current:1,

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
    getData(current){
        this.setState({loading: true});
        let defaultPageSize = this.state.defaultPageSize;
        const values = this.props.form.getFieldsValue();
        values.currentPage = current;
        if (values.createDate) {
            values.beginDate = values.createDate[0].format('yyyy-MM-dd HH:mm:ss');
            values.endDate = values.createDate[1].format('yyyy-MM-dd HH:mm:ss');
        } else {
            values.beginDate = null;
            values.endDate = null;
        }
        reqwest({
            url: '/wms/master/FindOutOrInWarehouseAction/findOutOrInWarehouse',
            dataType: 'json',
            method: 'post',
            data: {current:values.currentPage,defaultPageSize:defaultPageSize,
                productId:values.productId,beginDate:values.beginDate,
                endDate:values.endDate,type:values.type},
            success: function (json) {
                if(json.success){
                    console.log(json.res);
                    for(var i =0;i<json.res.length;i++){
                        json.res[i].rowIndex = i+1;
                        json.res[i].dateTime=new Date(json.res[i].dateTime).format("yyyy-MM-dd HH:mm:ss");
                    }
                    this.setState({data: json.res, total: json.count, loading: false});
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
        this.getData(1);
    },
    handleSubmitstatement(e) {
        const values = this.props.form.getFieldsValue();
        if (values.createDate) {
            values.beginDate = values.createDate[0].format('yyyy-MM-dd HH:mm:ss');
            values.endDate = values.createDate[1].format('yyyy-MM-dd HH:mm:ss');
        } else {
            values.beginDate = "";
            values.endDate = "";
        }
        window.open('/wms/master/FindOutOrInWarehouseAction/exportReport?beginDate='+values.beginDate+'&endDate='+values.endDate);
        //location.href='/wms/master/FindOutOrInWarehouseAction/exportReport?beginDate='+values.beginDate+'&endDate='+values.endDate;
        /*reqwest({
            url: '/wms/master/FindOutOrInWarehouseAction/exportReport',
            dataType: 'text',
            method: 'post',
            data: {beginDate:values.beginDate,endDate:},

            success: function (json) {
                console.log("11"+json.msg);
                if(json.success){
                    message.success("导出报表成功！");

                }else{
                    message.error("导出报表失败！");
                }
            }.bind(this),
            error: function (err) {
                reqwestError(err);
                message.error("系统错误！");
            }.bind(this)
        });*/
    },
    pageChange(noop){
        this.setState({
            current:noop,
        })
        this.getData(noop);
    },
    disabledDate(current){
        return current.getTime() > Date.now();
    },
    render() {
        const columns = [{
            title: '货品代码',
            dataIndex: 'skuCode',
        }, {
            title: '货品名称',
            dataIndex: 'skuName',
        }, {
            title: '批次',
            dataIndex: 'lotNum',
        },{
            title: '托盘数量',
            dataIndex: 'num',
        }, {
            title: '货品数量',
            dataIndex: 'qty',
        }, {
            title: '类型',
            dataIndex: 'type',
        }, {
            title: '最近操作时间',
            dataIndex: 'dateTime',
        }];

        const {getFieldProps} = this.props.form;
        const commodityCodeProps = getFieldProps('productId', {
            initialValue:"",
        });
        const containerNoProps = getFieldProps('containerNo',{ initialValue: '' });
        const locationNoProps = getFieldProps('locationNo',{ initialValue: '' });
        const lotNoProps = getFieldProps('lotNo',{ initialValue: '' });
        const formItemLayout = {
            labelCol: {span: 5},
            wrapperCol: {span: 14},
        };
        const createDateProps = getFieldProps('createDate');
        const radioGroupProps= getFieldProps('type',{initialValue:'00'});
        const commodityCodeListSelect =[];
        commodityCodeListSelect.push(<Option value="">---请选择---</Option>);
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
                                label="时间"
                            >
                                <RangePicker showTime {...createDateProps} style={{ width: 400 }}
                                             format="yyyy-MM-dd HH:mm:ss"
                                />
                            </FormItem>
                            <FormItem
                                {...formItemLayout}
                                label="查询类型"
                            >
                                <RadioGroup {...radioGroupProps}>
                                    <Radio value="00">全部</Radio>
                                    <Radio value="01">入库</Radio>
                                    <Radio value="03">出库</Radio>
                                </RadioGroup>
                            </FormItem>

                            <FormItem wrapperCol={{offset: 10}}>
                                <Button type="primary" onClick={this.handleSubmit}>查询</Button>
                                &nbsp;&nbsp;&nbsp;
                                <Button type="ghost" onClick={this.handleReset}>重置</Button>
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <a onClick={this.handleSubmitstatement}>  </a>
                                <Button type="ghost"  onClick={this.handleSubmitstatement}><Icon type="download" />导出报表</Button>
                            </FormItem>
                        </Col>
                    </Row>

                </Form>
                <Table
                    loading={this.state.loading}
                    columns={columns}
                    rowKey={record => record.id}
                    dataSource={this.state.data}
                    pagination={{
                        onChange: this.pageChange,
                        current:this.state.current,
                        showQuickJumper: true,
                        defaultCurrent: 1,
                        defaultPageSize:this.state.defaultPageSize,
                        total: this.state.total,
                        showTotal: total => `共 ${total} 条数据`
                    }}
                />
            </div>
        );
    },
});
OutputArea = Form.create({})(OutputArea);
export default OutputArea;
