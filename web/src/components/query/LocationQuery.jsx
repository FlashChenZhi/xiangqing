import {Button, Form, Table, Pagination, Select, message, Row, Col, Input, DatePicker} from 'antd';
import React from 'react';
import reqwest from 'reqwest';
import {reqwestError, dateFormat} from '../common/Golbal';
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


let LocationQuery = React.createClass({
    getInitialState() {
        return {
            data: [],
            total: 0,//表格数据总行数
            loading: false,
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
            url: '/wms/location/searchLocation.do',
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

    frozenLocation(locationNo) {
        reqwest({
            url: '/wms/location/frozenLocation.do',
            method: 'POST',
            data: {locationNo: locationNo},
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
    },

    unFrozenLocation(locationNo) {
        reqwest({
            url: '/wms/location/unFrozenLocation.do',
            method: 'POST',
            data: {locationNo: locationNo},
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
    },

    changeLocation(locationNo) {
        reqwest({
            url: '/wms/location/changeLocation.do',
            method: 'POST',
            data: {locationNo: locationNo},
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
            render: (text, record)=> {
                return boolCmp(record.emptyFlag);
            }
        }, {
            title: '是否预订',
            dataIndex: 'reserved',
            key: 'reserved',
            render: (text, record)=> {
                return boolCmp(record.reserved);
            }
        }, {
            title: '操作', dataIndex: 'operation', key: 'operation', fixed: 'right', width: 200,
            render: (text, record) => (
                <span>
                 <a onClick={this.frozenLocation.bind(this, record.locationNo)}>冻结</a>&nbsp;&nbsp;&nbsp;||&nbsp;&nbsp;&nbsp;
                    <a onClick={this.unFrozenLocation.bind(this, record.locationNo)}>解冻</a >
                    &nbsp;&nbsp;&nbsp;||&nbsp;&nbsp;&nbsp;
                    <a onClick={this.changeLocation.bind(this, record.locationNo)}>货位变更</a >
       </span>
            )
        }];

        const {getFieldProps} = this.props.form;
        const locationNoProps = getFieldProps('locationNo');
        const bankProps = getFieldProps('bank');
        const bayProps = getFieldProps('bay');
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
                                label="Bank："
                            >
                                <Input {...bankProps}/>
                            </FormItem>

                        </Col>

                        <Col lg={12}>
                            <FormItem
                                {...formItemLayout}
                                label="Bay："
                            >
                                <Input {...bayProps}/>
                            </FormItem>

                            <FormItem
                                {...formItemLayout}
                                label="Level："
                            >
                                <Input {...levelProps}/>
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
            </div>
        );
    },
});
LocationQuery = Form.create({})(LocationQuery);
export default LocationQuery;
