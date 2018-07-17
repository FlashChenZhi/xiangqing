import {Button,Modal,Icon , Form, Input,Radio, Table, Badge, DatePicker, Select, message, Row, Col} from 'antd';
import React from 'react';
const FormItem = Form.Item;
import reqwest from 'reqwest';
import {reqwestError, dateFormat} from '../common/Golbal';
import OrderDetailByOrderNo from './OrderDetailByOrderNo';

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
            OrderDetailByOrderNo: false,
            orderNo:"",
        };
    },
    componentDidMount(){
        this.getData(1);

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

        reqwest({
            url: '/wms/master/FindOrderDetailAction/FindOrderDetailData',
            dataType: 'json',
            method: 'post',
            data: {current:values.currentPage,defaultPageSize:defaultPageSize,orderNo:values.orderNo},
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

    findOrderDetail(orderNo) {
         this.setState({orderNo: orderNo, OrderDetailByOrderNo: true});
    },
    hideOrderDetailByOrderNo() {
        this.setState({orderNo: null, OrderDetailByOrderNo: false});
    },
    handleSubmit(e) {
        e.preventDefault();
        this.getData(1);
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
            title: '订单号',
            dataIndex: 'orderNo',
        }, {
            title: '出库人',
            dataIndex: 'coustomName',
        }, {
            title: '出库口',
            dataIndex: 'toStation',
        },{
            title: '到达地点',
            dataIndex: 'toLocation',
        }, {
            title: '车辆信息',
            dataIndex: 'carrierCar',
        }, {
            title: '订单状态',
            dataIndex: 'status',
        }, {
            title: '创建时间',
            dataIndex: 'createDate',
        },{
            title: '操作', dataIndex: 'operation', key: 'operation', fixed: 'right', width: 250,
            render: (text, record) => (
                <span>
                     <a onClick={this.findOrderDetail.bind(this, record.orderNo)}>详情</a>
                </span>
            )
        }];

        const {getFieldProps} = this.props.form;
        const orderNoProps = getFieldProps('orderNo', {
            initialValue:"",
        });
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
                                label="订单号："
                            >

                                <Input style={{width:"300"}}
                                       {...orderNoProps}   placeholder="请输入订单号" />
                            </FormItem>
                            <FormItem wrapperCol={{offset: 10}}>
                                <Button type="primary" onClick={this.handleSubmit}>查询</Button>
                                &nbsp;&nbsp;&nbsp;
                                <Button type="ghost" onClick={this.handleReset}>重置</Button>
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
                <OrderDetailByOrderNo
                    code={this.state.orderNo}
                    visible={this.state.OrderDetailByOrderNo}
                    hideModel={this.hideOrderDetailByOrderNo.bind(this)}

                />
            </div>
        );
    },
});
OutputArea = Form.create({})(OutputArea);
export default OutputArea;
