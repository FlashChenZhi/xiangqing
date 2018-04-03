import {Button, Form, Table, Pagination, Select, message, Row, Col, Input, DatePicker} from 'antd';
import React from 'react';
import reqwest from 'reqwest';
import {reqwestError, dateFormat} from '../common/Golbal';
const FormItem = Form.Item;
const Option = Select.Option;
const RangePicker = DatePicker.RangePicker;

let OutputPerformanceQuery = React.createClass({
    getInitialState() {
        return {
            data: [],
            total: 0,//表格数据总行数
            loading: false,
        }
    },
    componentDidMount(){
        // this.getTableData(1);
    },
    getTableData(currentPage){
        this.setState({loading: true});
        const values = this.props.form.getFieldsValue();
        if (values.lotNum) {
            values.lotNumBeginDate = dateFormat(values.lotNum[0]);
            values.lotNumEndDate = dateFormat(values.lotNum[1]);
        } else {
            values.lotNumBeginDate = null;
            values.lotNumEndDate = null;
        }
        if (values.retrievalDate) {
            values.storeBeginDate = dateFormat(values.retrievalDate[0]);
            values.storeEndDate = dateFormat(values.retrievalDate[1]);
        } else {
            values.storeBeginDate = null;
            values.storeEndDate = null;
        }
        values.currentPage = currentPage;
        delete values.lotNum;
        delete values.retrievalDate;
        reqwest({
            url: '/wms/query/outputPerformance.do',
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
    pageChange(noop){
        this.getTableData(noop);
    },
    handleSubmit(e) {
        e.preventDefault();
        this.getTableData(1);
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
        const skuCodeProps = getFieldProps('skuCode');
        const skuNameProps = getFieldProps('skuName');
        const skuSpecProps = getFieldProps('skuSpec');
        const custSkuNameProps = getFieldProps('custSkuName');
        const custNameProps = getFieldProps('custName');
        const providerNameProps = getFieldProps('providerName');
        const orderNoProps = getFieldProps('orderNo');
        const batchNoProps = getFieldProps('batchNo');
        const lotNumProps = getFieldProps('lotNum');
        const locationNoProps = getFieldProps('locationNo');
        const barcodeProps = getFieldProps('palletBarcode');
        const retrievalDateProps = getFieldProps('retrievalDate');
        const formItemLayout = {
            labelCol: {span: 5},
            wrapperCol: {span: 14},
        };
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
            title: '数量',
            dataIndex: 'qty',
        }, {
            title: '计量单位',
            dataIndex: 'skuEom',
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
            title: '出库日期',
            dataIndex: 'retrievalDate',
        }, {
            title: '出库时间',
            dataIndex: 'retrievalTime',
        }, {
            title: '货位位置',
            dataIndex: 'locationNo',
        }, {
            title: '托盘号',
            dataIndex: 'palletBarcode',
        }];
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
                            <FormItem
                                {...formItemLayout}
                                label="货位位置："
                            >
                                <Input {...locationNoProps}/>
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

                            <FormItem
                                {...formItemLayout}
                                label="托盘序列号："
                            >
                                <Input {...barcodeProps}/>
                            </FormItem>
                            <FormItem
                                {...formItemLayout}
                                label="出库日期"
                            >
                                <RangePicker {...retrievalDateProps} format="yyyyMMdd"
                                             disabledDate={this.disabledDate}/>
                            </FormItem>
                        </Col>
                    </Row>
                    <FormItem>
                    </FormItem>
                    <FormItem wrapperCol={{offset: 10}}>
                        <Button type="primary" onClick={this.handleSubmit}>查询</Button>
                        &nbsp;&nbsp;&nbsp;
                        <Button type="ghost" onClick={this.handleReset}>重置</Button>
                    </FormItem>
                </Form>
                <Table loading={this.state.loading}
                       columns={columns}
                       dataSource={this.state.data}
                       scroll={{x: 1500}}
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
OutputPerformanceQuery = Form.create({})(OutputPerformanceQuery);
export default OutputPerformanceQuery;
