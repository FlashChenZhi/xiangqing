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
            updateStationNo: '',
            commodityCodeList:[],
            commodityCodeFirst:"",
        }
    }

    componentDidMount() {

    }

    componentWillReceiveProps(props) {
        if (props.visible && !this.state.visible) {
            this.showModel(props.code);
            this.getStationMode(props.code);
        }
    }

    onModelConfirm() {
        this.props.form.validateFields((errors, values) => {
            if (!!errors) {
                return;
            }

            reqwest({
                url: '/wcs/platformSwitch/updatePlatformSwitch.do',
                method: 'POST',
                data: { direction: values.direction,stationNo: values.stationNo},
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
        this.setState({visible: true, loading: false, updateStationNo: code});
    }

    getStationMode(code){
        reqwest({
            url: '/wcs/platformSwitch/findPlatformSwitch.do',
            dataType: 'json',
            method: 'post',
            data: {stationNo:code},
            success: function (json) {
                if(json.success) {
                    this.setState({

                        commodityCodeList: json.res.selectRows,
                        commodityCodeFirst: json.res.selectRowsFirst,
                    })
                }else{
                    message.error("初始化商品代码失败！");
                }
            }.bind(this),
            error: function (err) {
                message.error("初始化商品代码失败！");
            }.bind(this)
        });
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
        const blockNoProps = getFieldProps('stationNo', {
            initialValue: this.state.updateStationNo
        });

        const commodityCodeProps = getFieldProps('direction', {
            initialValue:this.state.commodityCodeFirst
        });
        const commodityCodeListSelect =[];
        this.state.commodityCodeList.forEach((commodityCode)=>{
            commodityCodeListSelect.push(<Option value={commodityCode.direction}>{commodityCode.name}</Option>);
        });

        return (
            <Modal title="负责区域切换"
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
                                label="站台号"
                                {...formItemLayout}
                            >
                                <Input {...blockNoProps} readOnly={true}/>
                            </FormItem>

                            <FormItem
                                {...formItemLayout}
                                label="转向："
                            >
                                <Select id="select" size="large" style={{ width: 200 }}
                                        {...commodityCodeProps} >
                                    {commodityCodeListSelect}
                                </Select>
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