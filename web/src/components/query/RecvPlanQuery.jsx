import {Button, Form, Table, Pagination, Select, message, Row, Col, Input, DatePicker} from 'antd';
import React from 'react';
import reqwest from 'reqwest';
import {reqwestError} from '../common/Golbal';
const FormItem = Form.Item;
const Option = Select.Option;
const RangePicker = DatePicker.RangePicker;

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
}];

let RecvPlanQuery = React.createClass({
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
        values.currentPage = currentPage;
        reqwest({
            url: '/wms/inventory/recePlanSearch.do',
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
                    <FormItem>
                    </FormItem>
                    <FormItem wrapperCol={{offset: 10}}>
                        <Button type="primary" onClick={this.handleSubmit}>查询</Button>
                        &nbsp;&nbsp;&nbsp;
                        <Button type="ghost" onClick={this.handleReset}>取消</Button>
                    </FormItem>
                </Form>
                <Table loading={this.state.loading}
                       columns={columns}
                       rowKey={record => record.id}
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
RecvPlanQuery = Form.create({})(RecvPlanQuery);
export default RecvPlanQuery;