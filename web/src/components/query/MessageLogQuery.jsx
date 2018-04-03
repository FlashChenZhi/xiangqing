import {Button, Form, Table, Pagination, Select, message, Row, Col, Input, DatePicker} from 'antd';
import React from 'react';
import reqwest from 'reqwest';
import {reqwestError, dateFormat} from '../common/Golbal';

const FormItem = Form.Item;
const RangePicker = DatePicker.RangePicker;

let MessageLogQuery = React.createClass({
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

        if (values.createDate) {
            values.beginDate = values.createDate[0].format('yyyy-MM-dd HH:mm:ss');
            values.endDate = values.createDate[1].format('yyyy-MM-dd HH:mm:ss');
        } else {
            values.beginDate = null;
            values.endDate = null;
        }

        values.currentPage = currentPage;
        delete values.createDate;
        reqwest({
            url: '/wcs/webService/searchMessageLog.do',
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

    render() {

        const columns = [{
            title: 'id',
            dataIndex: 'id',
            key: 'id',
        }, {
            title: '类型',
            dataIndex: 'type'
        }, {
            title: '消息',
            dataIndex: 'msg'
        }, {
            title: '创建时间',
            dataIndex: 'createDate',
        }];

        const {getFieldProps} = this.props.form;
        const createDateProps = getFieldProps('createDate');
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
                                label="时间"
                            >
                                <RangePicker showTime {...createDateProps} style={{ width: 400 }}
                                             format="yyyy-MM-dd HH:mm:ss"
                                />
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
MessageLogQuery = Form.create({})(MessageLogQuery);
export default MessageLogQuery;
