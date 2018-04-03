import {Button, Form, Table, Pagination, Select, message, Row, Col, Input, DatePicker} from 'antd';
import React from 'react';
import reqwest from 'reqwest';
import {reqwestError, dateFormat} from '../common/Golbal';

import ModifyLocationModel from './ModifyLocationModel';

const FormItem = Form.Item;

const boolCmp = (b) => {
    if (b === true) {
        return "是";
    } else if (b === false) {
        return "否";
    } else {
        return "null";
    }
};

let ModifyLocation = React.createClass({
    getInitialState() {
        return {
            data: [],
            total: 0,//表格数据总行数
            loading: false,
            locationNo: '',
            modifyLocationModel: false

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
            url: '/wms/location/searchFirstLocation.do',
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


    hideModifyModel() {
        this.setState({locationNo: null, modifyLocationModel: false});
    },

    changeLocation(locationNo) {

        this.setState({locationNo: locationNo, modifyLocationModel: true});
    },

    render() {

        const columns = [{
            title: '货位号',
            dataIndex: 'locationNo',
        }, {
            title: 'bank',
            dataIndex: 'bank',
        }, {
            title: 'bay',
            dataIndex: 'bay',
        }, {
            title: 'level',
            dataIndex: 'level',
        }, {
            title: '是否空货位',
            dataIndex: 'emptyFlag',
            key: 'emptyFlag',
            render: (text, record) => {
                return boolCmp(record.emptyFlag);
            }
        }, {
            title: '是否预订',
            dataIndex: 'reserved',
            key: 'reserved',
            render: (text, record) => {
                return boolCmp(record.reserved);
            }
        }, {
            title: '货位类型',
            dataIndex: 'skuType'
        }
            , {
                title: '操作', dataIndex: 'operation', key: 'operation', fixed: 'right', width: 200,
                render: (text, record) => (
                    <span>
                 <a onClick={this.changeLocation.bind(this, record.locationNo)}>修改货位属性</a>
       </span>
                )
            }];

        const {getFieldProps} = this.props.form;
        const locationNoProps = getFieldProps('locationNo');
        const bayProps = getFieldProps('bay');
        const skuTypeProps = getFieldProps('skuType');
        const levelProps = getFieldProps('level');
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
                                label="货位号："
                            >
                                <Input {...locationNoProps}/>
                            </FormItem>

                            <FormItem
                                {...formItemLayout}
                                label="Level："
                            >
                                <Input {...levelProps}/>
                            </FormItem>

                        </Col>

                        <Col lg={12}>
                            <FormItem
                                {...formItemLayout}
                                label="Bay："
                            >
                                <Input {...bayProps}/>
                            </FormItem>

                            <FormItem label="商品类型" {...formItemLayout}>
                                <Select {...skuTypeProps} allowClear={true} size="default" defaultValue="0"
                                        placeholder="请选择">
                                    <Option value="C">成品</Option>
                                    <Option value="M">帽盖</Option>
                                    <Option value="L">铝片</Option>
                                </Select>
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
                       pagination={{
                           onChange: this.pageChange,
                           showQuickJumper: true,
                           defaultCurrent: 1,
                           total: this.state.total,
                           showTotal: total => `共 ${total} 条数据`
                       }}
                />

                <ModifyLocationModel
                    code={this.state.locationNo}
                    visible={this.state.modifyLocationModel}
                    hideModel={this.hideModifyModel.bind(this)}
                />

            </div>
        );
    },
});
ModifyLocation = Form.create({})(ModifyLocation);
export default ModifyLocation;
