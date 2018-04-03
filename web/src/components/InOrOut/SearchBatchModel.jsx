import React from 'react';
import {Form, Row, Col, Cascader, InputNumber, Input, Select, Button, Icon, message, Modal, Radio, Table} from 'antd';
import {reqwestError} from '../common/Golbal';
import reqwest from 'reqwest';

const createForm = Form.create;
const FormItem = Form.Item;

class SearchBatchModel extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            loading: false,
            visible: false,
            orderNo: '',
            itemCode: ''
        }
    }

    componentDidMount() {
    }

    componentWillReceiveProps(props) {
        if (props.visible && !this.state.visible) {
            this.showModel(props.orderNo, props.itemCode);
        }
    }

    deleteBatch(orderNo,itemCode,batchNo) {
        this.props.form.validateFields((errors, values) => {

            if (!!errors) {
                return;
            }

            reqwest({
                url: '/wms/retrieval/deleteBatch.do',
                method: 'POST',
                data: {orderNo: orderNo, itemCode: itemCode, batchNo: batchNo},
                type: 'json',
                error: err => {
                    message.error('网络异常,请稍后再试');
                },
                success: resp => {
                    if (resp.success) {
                        message.success(resp.msg);
                        this.setState({visible: false, loading: false});
                    } else {
                        message.error(resp.msg);
                    }
                }
            });
        });
    }

    showModel(orderNo, itemCode) {
        this.setState({visible: true, loading: false, orderNo: orderNo, itemCode: itemCode});
        this.loadData(orderNo,itemCode);
    }

    loadData(orderNo,itemCode){
        this.setState({loading: true});
        reqwest({
            url: '/wms/retrieval/searchBatch.do',
            dataType: 'json',
            method: 'post',
            data: {orderNo:orderNo,itemCode,itemCode},
            success: function (json) {
                this.setState({data: json.msg.data, loading: false});
            }.bind(this),
            error: function (err) {
                reqwestError(err);
            }.bind(this)
        });

    }

    hideModel() {
        this.props.form.resetFields();
        this.setState({visible: false, loading: false});
        this.props.hideModel();
    }

    render() {

        const columns = [{
            title: '订单号',
            dataIndex: 'orderNo',
        }, {
            title: '商品代码',
            dataIndex: 'itemCode',
        }, {
            title: '批次',
            dataIndex: 'batchNo',
        }, {
            title: '操作', dataIndex: 'operation', key: 'operation', fixed: 'right', width: 100,
            render: (text, record) => (
                <span>
                 <a onClick={this.deleteBatch.bind(this, record.orderNo, record.itemCode, record.batchNo)}>删除</a>
       </span>
            )
        }];
        return (
            <Modal title="查询波次"
                   visible={this.state.visible}
                   onCancel={this.hideModel.bind(this)}
                   onOk={this.hideModel.bind(this)}
                   confirmLoading={this.state.loading}
                   width="800px"
            >

                <div>
                    <Table loading={this.state.loading}
                           columns={columns}
                           dataSource={this.state.data}
                           scroll={{x: 500}}
                    />
                </div>
            </Modal>
        );
    }

}

SearchBatchModel = createForm()(SearchBatchModel);

export default SearchBatchModel;