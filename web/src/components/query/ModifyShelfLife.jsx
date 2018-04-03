import React from 'react';
import {Form, Row, Col, Cascader, InputNumber, Input, Select, Button, Icon, message, Modal, Radio} from 'antd';
import {reqwestError} from '../common/Golbal';
import reqwest from 'reqwest';

const createForm = Form.create;
const FormItem = Form.Item;

class ModifyShelfLife extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            loading: false,
            visible: false,
            itemCode: '',
            shelfLife: 0
        }
    }

    componentDidMount() {

    }

    componentWillReceiveProps(props) {
        if (props.visible && !this.state.visible) {
            this.showModel(props.itemCode, props.shelfLife);
        }
    }

    onModelConfirm() {
        this.props.form.validateFields((errors, values) => {
            if (!!errors) {
                return;
            }

            reqwest({
                url: '/wms/query/modifySkuShelfLife.do',
                method: 'POST',
                data: {skuCode: values.itemCode, shelfLife: values.shelfLife},
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

    showModel(itemCode, shelfLife) {
        this.setState({visible: true, loading: false, shelfLife: shelfLife, itemCode: itemCode});
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

        const itemProps = getFieldProps('itemCode', {
            initialValue: this.state.itemCode
        });

        const batchProps = getFieldProps('shelfLife', {
            rules: [
                {required: true, message: "存储周期，不能为空"}
            ],
        });

        return (
            <Modal title="修改存储周期"
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
                                label="商品代码"
                                {...formItemLayout}
                            >
                                <Input {...itemProps} readOnly={true}/>
                            </FormItem>

                            <FormItem
                                label="存储周期"
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

ModifyShelfLife = createForm()(ModifyShelfLife);

export default ModifyShelfLife;