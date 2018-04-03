import {Button, Form, Input, Tag, Modal, message, Switch} from 'antd';
import React from 'react';
import reqwest from 'reqwest';
import {reqwestError} from '../common/Golbal';
const FormItem = Form.Item;
var autoInput = false;
let NewInputArea = React.createClass({

        getInitialState() {
            return {}
        },
        handleSubmit(e) {
            e.preventDefault();
            this.props.form.validateFields((errors, values) => {
                if (!!errors) {
                    return;
                }
                reqwest({
                    url: '/wms/inventory/inputAreaSet.do',
                    type: 'json',
                    method: 'post',
                    data: values,
                    success: function (json) {
                        if (json.success) {
                            this.props.form.resetFields();
                            message.success(json.msg, 10);
                        } else {
                            message.error(json.msg, 10);
                        }
                    }.bind(this),
                    error: function (err) {
                        reqwestError(err);
                    }.bind(this)
                })
            });
        },
        handleReset(e){
            e.preventDefault();
            this.props.form.resetFields();
        },
        componentDidMount(){
            autoInput = false;
        },

        closeRecv(){
            const batchNo = this.props.form.getFieldValue('batchNo');
            if (!batchNo) {
                return;
            }
            reqwest({
                url: '/wms/inventory/closeRecvPlan.do',
                type: 'json',
                method: 'post',
                data: {batchNo: batchNo},
                success: function (json) {
                    if (json.success) {
                        message.success(json.msg, 10);
                    } else {
                        message.error(json.msg, 10);
                    }
                }.bind(this),
                error: function (err) {
                    reqwestError(err);
                }.bind(this)
            })
        },

        onChange(checked)
        {
            autoInput = checked;
            if (autoInput) {
                message.success("切换到自动入库设定模式,扫完批次码，自动入库", 10);
            } else {
                message.success("切换到手动入库设定模式，扫完批次码，手动设定入库", 10);
            }
        },

        batchNoOnBlur(event)
        {
            const batchNo = this.props.form.getFieldValue('batchNo');

            if (event.keyCode == 13) {

                if (!batchNo) {
                    return;
                }

                reqwest({
                    url: '/wms/inventory/inputAreaQuery.do',
                    type: 'json',
                    method: 'post',
                    data: {batchNo},
                    success: function (json) {
                        if (json.success) {
                            this.props.form.setFieldsValue(
                                {
                                    'skuCode': json.msg.skuCode,
                                    'skuName': json.msg.skuName,
                                    'skuSpec': json.msg.skuSpec,
                                    'custSkuName': json.msg.custSkuName,
                                    'qty': json.msg.qty,
                                    'custName': json.msg.custName,
                                    'providerName': json.msg.providerName,
                                    'orderNo': json.msg.orderNo,
                                    'lotNum': json.msg.lotNum,
                                    'skuEom': json.msg.skuEom,
                                    'recvQty': json.msg.recvQty
                                }
                            );

                            if (autoInput) {
                                event.preventDefault();
                                this.props.form.validateFields((errors, values) => {
                                    if (!!errors) {
                                        return;
                                    }
                                    reqwest({
                                        url: '/wms/inventory/inputAreaSet.do',
                                        type: 'json',
                                        method: 'post',
                                        data: values,
                                        success: function (json) {
                                            if (json.success) {
                                                this.props.form.resetFields();
                                                message.success(json.msg, 10);
                                            } else {
                                                message.error(json.msg, 10);
                                            }
                                        }.bind(this),
                                        error: function (err) {
                                            reqwestError(err);
                                        }.bind(this)
                                    })
                                });
                            }

                        } else {
                            this.props.form.resetFields();
                            this.props.form.setFieldsValue(
                                {'batchNo': batchNo});
                            message.error(json.msg, 10);
                        }
                    }.bind(this),
                    error: function (err) {
                        reqwestError(err);
                    }.bind(this)
                })
            }

        }
        ,

        render()
        {
            const {getFieldProps} = this.props.form;
            const batchNoProps = getFieldProps('batchNo', {
                rules: [
                    {required: true, message: '请输入批次号'}
                ],
            });
            const skuCodeProps = getFieldProps('skuCode');
            const skuNameProps = getFieldProps('skuName');
            const skuSpecProps = getFieldProps('skuSpec');
            const custSkuNameProps = getFieldProps('custSkuName');
            const qtyProps = getFieldProps('qty');
            const recvQtyProps = getFieldProps('recvQty');

            const skuEomProps = getFieldProps('skuEom');
            const custNameProps = getFieldProps('custName');
            const providerNameProps = getFieldProps('providerName');
            const orderNoProps = getFieldProps('orderNo');
            const lotNumProps = getFieldProps('lotNum');
            const formItemLayout = {
                labelCol: {span: 7},
                wrapperCol: {span: 12},
            };
            return (
                <Form horizontal>
                    <FormItem
                        {...formItemLayout}
                        label="自动设定"
                    >
                        <Switch checkedChildren={'开'} unCheckedChildren={'关'} defaultChecked={false}
                                onChange={this.onChange}/>

                    </FormItem>
                    <FormItem
                        {...formItemLayout}
                        label="批次号："
                    >
                        <Input {...batchNoProps} onKeyDown={this.batchNoOnBlur}/>
                    </FormItem>

                    <FormItem
                        {...formItemLayout}
                        label="商品编号："
                    >
                        <Input {...skuCodeProps} readOnly="true"/>
                    </FormItem>

                    <FormItem
                        {...formItemLayout}
                        label="商品名称："
                    >
                        <Input {...skuNameProps} readOnly="true"/>
                    </FormItem>

                    <FormItem
                        {...formItemLayout}
                        label="商品规格："
                    >
                        <Input {...skuSpecProps} readOnly="true"/>
                    </FormItem>

                    <FormItem
                        {...formItemLayout}
                        label="客户货名："
                    >
                        <Input {...custSkuNameProps} readOnly="true"/>
                    </FormItem>

                    <FormItem
                        {...formItemLayout}
                        label="数量："
                    >
                        <Input {...qtyProps} addonAfter={this.props.form.getFieldValue('skuEom')}/>
                    </FormItem>

                    <FormItem
                        {...formItemLayout}
                        label="已收数量："
                    >
                        <Input {...recvQtyProps} readOnly="true"/>
                    </FormItem>

                    <FormItem
                        {...formItemLayout}
                        label="客户名称："
                    >
                        <Input {...custNameProps} readOnly="true"/>
                    </FormItem>

                    <FormItem
                        {...formItemLayout}
                        label="供应商名称："
                    >
                        <Input {...providerNameProps} readOnly="true"/>
                    </FormItem>

                    <FormItem
                        {...formItemLayout}
                        label="订单号："
                    >
                        <Input {...orderNoProps} readOnly="true"/>
                    </FormItem>

                    <FormItem
                        {...formItemLayout}
                        label="生产日期："
                    >
                        <Input {...lotNumProps} readOnly="true"/>
                    </FormItem>

                    <FormItem wrapperCol={{offset: 11}}>
                        <Button type="primary" onClick={this.handleSubmit}>新规入库</Button>
                        &nbsp;&nbsp;&nbsp;
                        <Button type="ghost" onClick={this.handleReset}>重置</Button>
                        &nbsp;&nbsp;&nbsp;
                        <Button type="ghost" onClick={this.closeRecv}>关闭收货单</Button>

                    </FormItem>
                </Form>
            );
        }
        ,
    })
    ;
NewInputArea = Form.create({})(NewInputArea);
export default NewInputArea;
