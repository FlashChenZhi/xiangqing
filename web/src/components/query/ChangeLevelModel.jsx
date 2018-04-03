import React from 'react';
import {Form, Row, Col, Cascader, InputNumber, Input, Select, Button, Icon, message, Modal, Radio} from 'antd';
import {reqwestError} from '../common/Golbal';
import reqwest from 'reqwest';

const createForm = Form.create;
const FormItem = Form.Item;

class ChangeLevelModel extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            loading: false,
            visible: false,
            blockNo: ''
        }
    }

    componentDidMount() {

    }

    componentWillReceiveProps(props) {
        if (props.visible && !this.state.visible) {
            this.showModel(props.code);
        }
    }

    onModelConfirm() {
        this.props.form.validateFields((errors, values) => {
            if (!!errors) {
                return;
            }

            reqwest({
                url: '/wcs/webService/changeLevel.do',
                method: 'POST',
                data: {blockNo: values.blockNo, level: values.level},
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

    showModel(code) {
        this.setState({visible: true, loading: false, blockNo: code});
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
        const blockNoProps = getFieldProps('blockNo', {
            initialValue: this.state.blockNo
        });

        const levelProps = getFieldProps('level', {
            rules: [
                {required: true, message: "类型不能为空"}
            ],
        });

        return (
            <Modal title="子车换层"
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
                                label="子车号"
                                {...formItemLayout}
                            >
                                <Input {...blockNoProps} readOnly={true}/>
                            </FormItem>

                            <FormItem
                                label="层"
                                {...formItemLayout}
                            >
                                <Input {...levelProps} />
                            </FormItem>
                        </Col>
                    </Row>
                </Form>
            </Modal>
        );
    }
}

ChangeLevelModel = createForm()(ChangeLevelModel);

export default ChangeLevelModel;