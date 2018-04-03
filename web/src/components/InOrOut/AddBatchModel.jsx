import React from 'react';
import {Form, Row, Col, Cascader, InputNumber, Input, Select, Button, Icon, message, Modal, Radio} from 'antd';
import {reqwestError} from '../common/Golbal';
import reqwest from 'reqwest';

const createForm = Form.create;
const FormItem = Form.Item;

class AddBatchModel extends React.Component {
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

    onModelConfirm() {
        this.props.form.validateFields((errors, values) => {
            if (!!errors) {
                return;
            }

            reqwest({
                url: '/wms/retrieval/addBatch.do',
                method: 'POST',
                data: {orderNo: values.orderNo, itemCode: values.itemCode, batchNo: values.batchNo},
                type: 'json',
                error: err => {
                    message.error('网络异常,请稍后再试');
                },
                success: resp => {
                    if (resp.success) {
                        message.success(resp.msg);
                    } else {
                        message.error(resp.msg);
                    }
                }
            });
        });
    }

    showModel(orderNo, itemCode) {
        this.setState({visible: true, loading: false, orderNo: orderNo, itemCode: itemCode});
    }

    hideModel() {
        this.props.form.resetFields();
        this.setState({visible: false, loading: false});
        this.props.hideModel();
    }

    render() {
        const {getFieldProps} = this.props.form;

        const formItemLayout = {
            labelCol: {span: 8},
            wrapperCol: {span: 16}
        };
        const orderNoProps = getFieldProps('orderNo', {
            initialValue: this.state.orderNo
        });

        const itemProps = getFieldProps('itemCode', {
            initialValue: this.state.itemCode
        });

        const batchProps = getFieldProps('batchNo', {
            rules: [
                {required: true, message: "批次不能为空"}
            ],
        });

        return (
            <Modal title="增加波次"
                   visible={this.state.visible}
                   onOk={this.onModelConfirm.bind(this)}
                   onCancel={this.hideModel.bind(this)}
                   confirmLoading={this.state.loading}
                   width="800px"
            >
                <Form horizontal>
                    <Row gutter={8}>
                        <Col span={24}>
                            <FormItem
                                label="订单号"
                                {...formItemLayout}
                            >
                                <Input {...orderNoProps} readOnly={true}/>
                            </FormItem>

                            <FormItem
                                label="商品代码"
                                {...formItemLayout}
                            >
                                <Input {...itemProps} readOnly={true}/>
                            </FormItem>

                            <FormItem
                                label="批次"
                                {...formItemLayout}
                            >
                                <Input {...batchProps} />
                            </FormItem>

                        </Col>
                    </Row>
                </Form>
            </Modal>
        );
    }
}

AddBatchModel = createForm()(AddBatchModel);

export default AddBatchModel;