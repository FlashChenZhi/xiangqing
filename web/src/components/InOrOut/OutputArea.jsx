import {Button, Form, Input, Table, Pagination, DatePicker, InputNumber, Select, message, Row, Col} from 'antd';
import React from 'react';
const FormItem = Form.Item;
import reqwest from 'reqwest';
import {reqwestError, dateFormat} from '../common/Golbal';
const Option = Select.Option;
const RangePicker = DatePicker.RangePicker;
let OutputArea = React.createClass({
    getInitialState(){
        return {
            data: [],//表格数据
            total: 0,//表格数据总行数
            selectedData: [],//点击设定提交到后台的数据
            loading: false,
            selectedRowKeys: [],
        };
    },
    componentDidMount(){
        // this.getData(1);
    },
    onChange(selectedRowKeys, selectedRows) {
        this.setState({selectedData: selectedRows, selectedRowKeys: selectedRowKeys});
    },
    handleReset(e) {
        e.preventDefault();
        this.props.form.resetFields();
        this.setState({selectedRowKeys: [], selectedData: []});
    },
    submit(e){
        e.preventDefault();
        reqwest({
            url: '/wms/inventory/outAreaSet.do',
            dataType: 'json',
            method: 'post',
            data: {outAreaData: JSON.stringify(this.state.selectedData)},
            success: function (json) {
                if (!json.success) {
                    message.error(json.msg, 10);
                } else {
                    message.success(json.msg, 10);
                    this.getData(1);
                }
            }.bind(this),
            error: function (err) {
                reqwestError(err);
            }.bind(this)
        })
    },
    reset(e){
        e.preventDefault();
        this.setState({selectedRowKeys: [], selectedData: []});
    },
    getData(current){
        this.setState({loading: true});
        const values = this.props.form.getFieldsValue();
        if (values.lotNum) {
            values.beginDate = dateFormat(values.lotNum[0]);
            values.endDate = dateFormat(values.lotNum[1]);
        } else {
            values.beginDate = null;
            values.endDate = null;
        }
        values.currentPage = current;
        delete values.lotNum;
        reqwest({
            url: '/wms/inventory/getOutAreaData.do',
            dataType: 'json',
            method: 'post',
            data: values,
            success: function (json) {
                this.setState({data: json.msg.data, total: json.msg.total, loading: false});
            }.bind(this),
            error: function (err) {
                reqwestError(err);
            }.bind(this)
        });
    },
    handleSubmit(e) {
        e.preventDefault();
        this.getData(1);
    },
    pageChange(noop){
        this.getData(noop);
    },
    disabledDate(current){
        return current.getTime() > Date.now();
    },
    render() {
        const columns = [{
            title: '商品编码',
            dataIndex: 'skuCode',
        }, {
            title: '商品名称',
            dataIndex: 'skuName',
        }, {
            title: '商品规格',
            dataIndex: 'skuSpec',
        }, {
            title: '客户货名',
            dataIndex: 'custSkuName',
        }, {
            title: '客户名称',
            dataIndex: 'custName',
        }, {
            title: '供应商名称',
            dataIndex: 'providerName',
        }, {
            title: '订单号',
            dataIndex: 'orderNo',
        }, {
            title: '批次号',
            dataIndex: 'batchNo',
        }, {
            title: '生产日期',
            dataIndex: 'lotNum',
        }, {
            title: '数量',
            dataIndex: 'qty',
        }, {
            title: '计量单位',
            dataIndex: 'skuEom',
        }];
        const {getFieldProps} = this.props.form;
        const skuCodeProps = getFieldProps('skuCode');
        const skuNameProps = getFieldProps('skuName');
        const skuSpecProps = getFieldProps('skuSpec');
        const custSkuNameProps = getFieldProps('custSkuName');
        const custNameProps = getFieldProps('custName');
        const providerNameProps = getFieldProps('providerName');
        const orderNoProps = getFieldProps('orderNo');
        const batchNoProps = getFieldProps('batchNo');
        const lotNumProps = getFieldProps('lotNum');
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
                                label="商品编号："
                            >
                                <Input {...skuCodeProps}/>
                            </FormItem>

                            <FormItem
                                {...formItemLayout}
                                label="商品名称："
                            >
                                <Input {...skuNameProps} />
                            </FormItem>
                            <FormItem
                                {...formItemLayout}
                                label="商品规格："
                            >
                                <Input {...skuSpecProps}/>
                            </FormItem>

                            <FormItem
                                {...formItemLayout}
                                label="客户货名："
                            >
                                <Input {...custSkuNameProps}/>
                            </FormItem>
                            <FormItem
                                {...formItemLayout}
                                label="客户名称："
                            >
                                <Input {...custNameProps}/>
                            </FormItem>
                        </Col>
                        <Col lg={12}>
                            <FormItem
                                {...formItemLayout}
                                label="供应商名称："
                            >
                                <Input {...providerNameProps}/>
                            </FormItem>

                            <FormItem
                                {...formItemLayout}
                                label="订单号："
                            >
                                <Input {...orderNoProps}/>
                            </FormItem>

                            <FormItem
                                {...formItemLayout}
                                label="批次号："
                            >
                                <Input {...batchNoProps}/>
                            </FormItem>

                            <FormItem
                                {...formItemLayout}
                                label="生产日期："
                            >
                                <RangePicker {...lotNumProps} format="yyyyMMdd"
                                             disabledDate={this.disabledDate}/>
                            </FormItem>

                        </Col>
                    </Row>
                    <FormItem wrapperCol={{offset: 10}}>
                        <Button type="primary" onClick={this.handleSubmit}>查询</Button>
                        &nbsp;&nbsp;&nbsp;
                        <Button type="ghost" onClick={this.handleReset}>重置</Button>
                    </FormItem>
                    <FormItem>
                        <Button type="primary" onClick={this.submit}
                                disabled={this.state.selectedData.length > 0 ? false : true}>出库</Button>
                        &nbsp;&nbsp;&nbsp;
                        <Button type="ghost" onClick={this.reset}>清除</Button>
                    </FormItem>
                </Form>
                <Table rowSelection={{onChange: this.onChange, selectedRowKeys: this.state.selectedRowKeys}}
                       loading={this.state.loading}
                       columns={columns}
                       scroll={{x: 1500}}
                       rowKey={record => record.id}
                       dataSource={this.state.data}
                       pagination={{
                           onChange: this.pageChange,
                           showQuickJumper: true,
                           defaultCurrent: 1,
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
