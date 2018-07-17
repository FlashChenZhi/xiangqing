import React from 'react';
import {Button, Form, Row, Col,Table, Cascader, InputNumber, Input, Select,  Icon, message, Modal, Radio} from 'antd';
import {reqwestError} from '../common/Golbal';
import reqwest from 'reqwest';

const createForm = Form.create;
const FormItem = Form.Item;
const columns = [{
    title: '货品名称',
    dataIndex: 'skuName',
}, {
    title: '出库数量',
    dataIndex: 'qty',
},{
    title: '批号',
    dataIndex: 'batch',
}];
class ChangeLevelModel extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            loading: false,
            visible: false,
            orderNo: '',
            createDate:"",
            toLocation:"",
            carrierName:"",
            carrierCar:"",
            toStation:"",
            coustomName:"",
            data:[],

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

        /*if (!!window.ActiveXObject || "ActiveXObject" in window) { //是否ie
            this.remove_ie_header_and_footer();
        }*/
        document.body.innerHTML='<h3>亲亲山水订单详情</h3><br/>'+document.getElementById('div').innerHTML;
        window.print();
        //window.print();
    }
    remove_ie_header_and_footer() {
        var hkey_path;
        hkey_path = "HKEY_CURRENT_USER\\Software\\Microsoft\\Internet Explorer\\PageSetup\\";
        try {
            var RegWsh = new ActiveXObject("WScript.Shell");
            RegWsh.RegWrite(hkey_path + "header", "");
            RegWsh.RegWrite(hkey_path + "footer", "");
        } catch (e) {
        }
    }

    showModel(code) {
        let orderNo= encodeURI(code,"utf-8");
        this.setState({visible: true, loading: false, orderNo: orderNo});
        reqwest({
            url: '/wms/master/FindOrderDetailAction/FindOrderDetail',
            method: 'POST',
            data: {orderNo: orderNo},
            type: 'json',
            error: err => {
                message.error('网络异常,请稍后再试');
            },
            success: resp => {
                if (resp.success) {
                    this.setState({
                        orderNo: resp.res.orderNo,
                        createDate: resp.res.createDate,
                        toLocation: resp.res.toLocation,
                        carrierName: resp.res.carrierName,
                        carrierCar: resp.res.carrierCar,
                        toStation: resp.res.toStation,
                        coustomName: resp.res.coustomName,
                        data: resp.res.data,
                    })
                } else {
                    message.error(resp.msg);
                }
            }
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
            labelCol: {span: 5},
            wrapperCol: {span: 16}
        };
        const orderNoProps = getFieldProps('orderNo', {
            initialValue:this.state.orderNo,
        });
        const driverProps = getFieldProps('carrierName', {
            initialValue:this.state.carrierName,
        });
        const carProps = getFieldProps('carrierCar', {
            initialValue:this.state.carrierCar,
        });
        const createPersonProps = getFieldProps('coustomName', {
            initialValue:this.state.coustomName,
        });
        const placeOfArrivalProps = getFieldProps('toLocation', {
            initialValue:this.state.toLocation,
        });
        const toStationProps = getFieldProps('toStation', {
            initialValue:this.state.toStation,
        });

        return (
                <Modal title="亲亲山水订单详情"
                       visible={this.state.visible}
                       okText="打印"
                       onOk={this.onModelConfirm.bind(this)}
                       onCancel={this.hideModel.bind(this)}
                       confirmLoading={this.state.loading}
                       width="800px"
                >
                <div id={"div"}>
                    <Form horizontal >
                        <Row gutter={16}>
                            <Col span={12}>
                                <FormItem
                                    {...formItemLayout}
                                    label="订单号："
                                >
                                    <Input style={{width:"300"}}
                                           {...orderNoProps}   placeholder="请输入订单号" />
                                </FormItem>
                                <FormItem
                                    {...formItemLayout}
                                    label="驾驶员信息："
                                >
                                    <Input style={{width:"300"}}
                                           {...driverProps}   placeholder="请输入驾驶员信息" />
                                </FormItem>
                                <FormItem
                                    {...formItemLayout}
                                    label="车辆信息："
                                >
                                    <Input style={{width:"300"}}
                                           {...carProps}   placeholder="请输入车辆信息" />
                                </FormItem>
                            </Col>
                            <Col span={12}>
                                <FormItem
                                    {...formItemLayout}
                                    label="出库人："
                                >

                                    <Input style={{width:"300"}}
                                           {...createPersonProps}   placeholder="请输入出库人" />
                                </FormItem>
                                <FormItem
                                    {...formItemLayout}
                                    label="到货地点："
                                >
                                    <Input style={{width:"300"}}
                                           {...placeOfArrivalProps}   placeholder="请输入到货地点" />
                                </FormItem>
                                <FormItem
                                    {...formItemLayout}
                                    label="出库站台："
                                >
                                    <Input style={{width:"300"}}
                                           {...toStationProps}   placeholder="请输入出库站台" />
                                </FormItem>
                            </Col>
                        </Row>
                        {/*<Row gutter={16}>
                        <span style={{paddingLeft:"120px"}}></span>
                        <Button  type="primary" onClick={this.showModal}>添加商品</Button>
                    </Row>*/}
                        <br/>
                        <Table
                            /*loading={this.state.loading}*/
                            columns={columns}
                            /*rowKey={record => record.index}*/
                            dataSource={this.state.data}

                            /*pagination={{
                                onChange: this.pageChange,
                                showQuickJumper: true,
                                defaultCurrent: 1,
                                defaultPageSize:this.state.defaultPageSize,
                                total: this.state.total,
                                showTotal: total => `共 ${total} 条数据`
                            }}*/
                        />
                        <br/>
                        <Row gutter={16}>
                            <Col span={8}>
                                <span>收货人签字：</span>
                            </Col>
                            <Col span={8}>
                                <span>收货时间：</span>
                            </Col>
                            <Col span={8}>
                                <span>收货单位盖章：</span>
                            </Col>
                        </Row>
                    </Form>
                </div>
               </Modal>

        );
    }
}

ChangeLevelModel = createForm()(ChangeLevelModel);

export default ChangeLevelModel;