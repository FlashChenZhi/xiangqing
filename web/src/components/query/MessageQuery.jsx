import {Button, Form, Table, Pagination, Select, message, Row, Col, Input, DatePicker, Switch} from 'antd';
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

const sendCmp = (s) => {
    if (s === '03') {
        return "发送";
    } else {
        return "已接收";
    }
};

const typeCmp = (s) => {
    if (s === '02') {
        return "取货";
    } else if (s === '03') {
        return '卸货';
    } else if (s === '04') {
        return '移动';
    } else if (s == '05') {
        return '接车';
    } else if (s === '06') {
        return '卸车';
    } else if (s === '07') {
        return '移栽取货';
    } else if (s === '08') {
        return '移栽卸货';
    } else if (s === '09') {
        return '上车';
    } else if (s === '10') {
        return '下车';
    } else if (s === '11') {
        return "载货移动";
    } else if (s === '12') {
        return "载货上车";
    } else if (s == '13') {
        return '载货下车';
    } else if (s == '14') {
        return '充电开始';
    } else if (s == '15') {
        return '充电结束';
    }
};

let MessageQuery = React.createClass({
    getInitialState() {
        return {
            data: [],
            total: 0,//表格数据总行数
            loading: false,
            autoInput:false,
        }
    },
    componentDidMount(){
        this.setState({autoInput:false});
        this.getTableData(1);
        this.interval = setInterval(() => this.tick(), 3000);

    },
    componentWillUnmount() {
        clearInterval(this.interval);
    },

    tick(){
        if (this.state.autoInput) {
            this.getTableData(1);
        } else {
        }
    },
    getTableData(currentPage){
        this.setState({loading: true});
        const values = this.props.form.getFieldsValue();
        values.currentPage = currentPage;
        reqwest({
            url: '/wcs/webService/searchMessage.do',
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

    send(id) {
        reqwest({
            url: '/wcs/webService/sendMsg.do',
            method: 'POST',
            data: {id: id},
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

    onChange(checked)
    {
        this.setState({autoInput : checked});
        if (checked) {
            message.success("自动更新消息", 5);
        } else {
            message.success("手动更新消息", 5);
        }
    },


    getMsg(id) {
        reqwest({
            url: '/wcs/webService/getMsg.do',
            method: 'POST',
            data: {id: id},
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
            title: 'id',
            width: 100,
            dataIndex: 'id',
            key: 'id',fixed: true,
        },{
            title: '机器号', width: 100,fixed: true,
            dataIndex: 'machineNo',
        },  {
            title: '类型',
            dataIndex: 'type', width: 100,
            key: 'type',fixed: true,
            render: (text, record) => {
                return sendCmp(record.type);
            }

        }, {
            title: '类型',
            dataIndex: 'cycleOrder', width: 100,
            key: 'cycleOrder',fixed: true,
            render: (text, record) => {
                return typeCmp(record.cycleOrder);
            }
        }, {
            title: '机器号', width: 100,fixed: true,
            dataIndex: 'machineNo',
        }, {
            title: 'mcKey',
            dataIndex: 'mcKey',
        }, {
            title: '下一个目的',
            dataIndex: 'station',
        }, {
            title: '序号',
            dataIndex: 'dock',
        }, {
            title: '排',
            dataIndex: 'bank',
        }, {
            title: '列',
            dataIndex: 'bay',
        }, {
            title: '层',
            dataIndex: 'level',
        }, {
            title: '是否回复',
            dataIndex: 'received',
            key: 'received',
            render: (text, record) => {
                return boolCmp(record.received);
            }
        }, {
            title: '发送时间',
            dataIndex: 'sendDate',
        }, {
            title: '操作', dataIndex: 'operation', key: 'operation', fixed: 'right', width: 163,
            render: function (text, record, index) {
                let operateLabs = null;
                if (record.type === '03') {
                    operateLabs = (
                        <span>
                         <a onClick={this.send.bind(this, record.id)}>重发</a>
                            &nbsp;&nbsp;||&nbsp;&nbsp;
                            <a onClick={this.getMsg.bind(this, record.id)}>手动回复</a>
                    </span>

                    );
                }
                return operateLabs;
            }.bind(this)
        }];

        const {getFieldProps} = this.props.form;
        const mcKeyProps = getFieldProps('mcKey');
        const machineNoProps = getFieldProps('machineNo');
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
                                label="Mckey："
                            >
                                <Input {...mcKeyProps}/>
                            </FormItem>
                            <FormItem
                                {...formItemLayout}
                                label="机器号："
                            >
                                <Input {...machineNoProps}/>
                            </FormItem>
                            <FormItem
                                {...formItemLayout}
                                label="自动更新"
                            >
                                <Switch checkedChildren={'开'} unCheckedChildren={'关'} defaultChecked={false}
                                        onChange={this.onChange}/>

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
MessageQuery = Form.create({})(MessageQuery);
export default MessageQuery;
