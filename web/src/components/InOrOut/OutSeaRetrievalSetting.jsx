import {Button, Form, Table, Pagination, Select, message, Row, Col, Input, DatePicker} from 'antd';
import React from 'react';
import reqwest from 'reqwest';
import {reqwestError, dateFormat} from '../common/Golbal';

import AddBatchModel from './AddBatchModel';
import  SearchBatchModel from './SearchBatchModel';

const FormItem = Form.Item;

let OutSeaRetrievalSetting = React.createClass({
    getInitialState() {
        return {
            data: [],
            total: 0,//表格数据总行数
            loading: false,
            addBatchModel: false,
            searchBatchModel: false,
            orderNo: '',
            itemCode: ''
        }
    },
    componentDidMount(){
        this.getTableData(1);
    },
    getTableData(currentPage){
        this.setState({loading: true});
        const values = this.props.form.getFieldsValue();
        values.currentPage = currentPage;
        reqwest({
            url: '/wms/retrieval/searchOutSeaRetrieval.do',
            dataType: 'json',
            method: 'post',
            data: values,
            success: function (json) {
                this.setState({data: json.msg.data, loading: false});
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

    hideModifyModel() {
        this.setState({locationNo: null, addBatchModel: false});
    },

    hideSearchModel(){
        this.setState({locationNo: null, searchBatchModel: false});
    },

    addBatch(orderNo, itemCode) {
        this.setState({orderNo: orderNo, itemCode: itemCode, addBatchModel: true, searchBatchModel: false});
    },

    searchBatch(orderNo, itemCode) {
        this.setState({orderNo: orderNo, itemCode: itemCode, searchBatchModel: true, addBatch: false});
    },

    render() {

        const columns = [{
            title: '订单号',
            dataIndex: 'orderNo',
        }, {
            title: '商品代码',
            dataIndex: 'itemCode',
        }, {
            title: '数量',
            dataIndex: 'qty',
        }, {
            title: '操作', dataIndex: 'operation', key: 'operation', fixed: 'right', width: 200,
            render: (text, record) => (
                <span>
                 <a onClick={this.addBatch.bind(this, record.orderNo, record.itemCode)}>增加批次</a>
                    &nbsp;&nbsp;||&nbsp;&nbsp;
                    <a onClick={this.searchBatch.bind(this, record.orderNo, record.itemCode)}>查询批次</a>
       </span>
            )
        }];

        const {getFieldProps} = this.props.form;
        const orderNoProps = getFieldProps('orderNo');
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
                                <Input {...orderNoProps}/>
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
                />

                <AddBatchModel
                    orderNo={this.state.orderNo}
                    itemCode={this.state.itemCode}
                    visible={this.state.addBatchModel}
                    hideModel={this.hideModifyModel.bind(this)}
                />

                <SearchBatchModel
                    orderNo={this.state.orderNo}
                    itemCode={this.state.itemCode}
                    visible={this.state.searchBatchModel}
                    hideModel={this.hideSearchModel.bind(this)}
                />

            </div>
        );
    },
});
OutSeaRetrievalSetting = Form.create({})(OutSeaRetrievalSetting);
export default OutSeaRetrievalSetting;
