import {Button, Form, Input, Select, Modal, message, DatePicker} from 'antd';
import React from 'react';
import reqwest from 'reqwest';
import {reqwestError} from '../common/Golbal';
const FormItem = Form.Item;
const Option = Select.Option;
let CreateReceivingPlan = React.createClass({
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
                url: '/wms/inventory/createReceivingPlan.do',
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
    },

    skuCodeOnBlur(){
        const skuCode = this.props.form.getFieldValue('skuCode');
        if (!skuCode) {
            return;
        }
        reqwest({
            url: '/wms/inventory/checkSku.do',
            type: 'json',
            method: 'post',
            data: {'skuCode': skuCode},
            success: function (json) {
                if (json.success) {
                    this.props.form.setFieldsValue(
                        {
                            'skuName': json.msg.skuName,
                            'skuSpec': json.msg.skuSpec,
                            'custSkuName': json.msg.custSkuName,
                            'custName': json.msg.custName,
                            'providerName': json.msg.providerName,
                            'skuEom': json.msg.skuEom
                        }
                    );
                } else {
                    this.props.form.resetFields();
                    this.props.form.setFieldsValue(
                        {'skuCode': skuCode});
                    message.error(json.msg, 10);
                }
            }.bind(this),
            error: function (err) {
                reqwestError(err);
            }.bind(this)
        })
    },

    render() {
        const {getFieldProps} = this.props.form;
        const batchNoProps = getFieldProps('batchNo', {
            rules: [
                {required: true, message: '请输入批次号'}
            ],
        });
        const skuCodeProps = getFieldProps('skuCode', {
            rules: [
                {required: true, message: '请输入商品代码'}
            ],
        });
        const skuNameProps = getFieldProps('skuName');
        const skuSpecProps = getFieldProps('skuSpec');
        const custSkuNameProps = getFieldProps('custSkuName');
        const qtyProps = getFieldProps('qty', {
            rules: [
                {required: true, message: '请输入数量'}
            ]
        });
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
                    label="批次号："
                >
                    <Input {...batchNoProps}/>
                </FormItem>

                <FormItem
                    {...formItemLayout}
                    label="商品编号："
                >
                    <Input {...skuCodeProps} onBlur={this.skuCodeOnBlur}/>
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
                    <Input {...orderNoProps}/>
                </FormItem>

                <FormItem
                    {...formItemLayout}
                    label="生产日期："
                >
                    <Input {...lotNumProps}/>
                </FormItem>

                <FormItem wrapperCol={{offset: 11}}>
                    <Button type="primary" onClick={this.handleSubmit}>新建入库单</Button>
                    &nbsp;&nbsp;&nbsp;
                    <Button type="ghost" onClick={this.handleReset}>重置</Button>

                </FormItem>
            </Form>
        );
    },
});
CreateReceivingPlan = Form.create({})(CreateReceivingPlan);
export default CreateReceivingPlan;
