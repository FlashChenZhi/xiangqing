import {Button, Form, Table, Pagination, Select, message, Row, Col, Input, DatePicker} from 'antd';
import React from 'react';
import reqwest from 'reqwest';
import {reqwestError, dateFormat} from '../common/Golbal';
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
    title: '箱装数量',
    dataIndex: 'packageQty',
}, {
    title: '托盘数量',
    dataIndex: 'palletLoadQTy',
}, {
    title: '计量单位',
    dataIndex: 'skuEom',
}, {
    title: 'SKU类型',
    dataIndex: 'skuType',
}];
let SkuQuery = React.createClass({
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
            url: '/wms/query/searchSku.do',
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
SkuQuery = Form.create({})(SkuQuery);
export default SkuQuery;
