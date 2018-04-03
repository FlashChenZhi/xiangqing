import {Button, Form, Input, Table, Pagination, message} from 'antd';
import React from 'react';
import reqwest from 'reqwest';
import {reqwestError} from '../common/Golbal';

let OnlineTaskQuery = React.createClass({
    getInitialState() {
        return {
            data: [],
            total: 0,//表格数据总行数
            loading: false,
        }
    },
    componentDidMount(){
        this.getTableData(1);
    },
    getTableData(currentPage){
        if (!!currentPage) {
            currentPage = 1;
        }
        this.setState({loading: true});
        reqwest({
            url: '/wms/query/onlineTask.do',
            dataType: 'json',
            method: 'post',
            data: {currentPage: currentPage},
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
    startTask(){
        reqwest({
            url: '/wms/query/startTask.do',
            type: 'json',
            method: 'post',
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
    endTask(){
        reqwest({
            url: '/wms/query/endTask.do',
            type: 'json',
            method: 'post',
            success: function (json) {
                if (json.success) {
                    message.success(json.msg);
                } else {
                    message.error(json.msg);
                }
            }.bind(this),
            error: function (err) {
                reqwestError(err);
            }.bind(this)
        })
    },

    finishForce(mcKey) {
        reqwest({
            url: '/wms/task/finishJob.do',
            method: 'POST',
            data: {mcKey: mcKey},
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

    cancelForce(mcKey) {
        reqwest({
            url: '/wms/task/cancelJob.do',
            method: 'POST',
            data: {mcKey: mcKey},
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
            title: 'MCKEY',
            dataIndex: 'mcKey',
        }, {
            title: '源货位号',
            dataIndex: 'fromLocation',
        }, {
            title: '目标货位号',
            dataIndex: 'toLocation',
        }, {
            title: '托盘号',
            dataIndex: 'barcode',
        }, {
            title: '源站台',
            dataIndex: 'fromStation',
        }, {
            title: '目标站台',
            dataIndex: 'toStation',
        }, {
            title: '作业类型',
            dataIndex: 'jobType',
        }, {
            title: '作业状态',
            dataIndex: 'jobStatus',
        }, {
            title: '操作', dataIndex: 'operation', key: 'operation', fixed: 'right', width: 163,
            render: (text, record) => (
                <span>
                 <a onClick={this.finishForce.bind(this, record.mcKey)}>强制完成</a>&nbsp;&nbsp;&nbsp;
                    ||&nbsp;&nbsp;&nbsp;
                    <a onClick={this.cancelForce.bind(this, record.mcKey)}>强制取消</a >
       </span>
            )
        }];

        return (
            <div>
                <Table loading={this.state.loading}
                       columns={columns}
                       rowKey={record => record.mcKey}
                       dataSource={this.state.data}
                       pagination={{
                           onChange: this.pageChange,
                           showQuickJumper: true,
                           defaultCurrent: 1,
                           total: this.state.total,
                           showTotal: total => `共 ${total} 条数据`
                       }}
                />
                <Button type="primary"
                        onClick={this.startTask}>任务开始</Button>
                &nbsp;&nbsp;&nbsp;
                <Button type="primary" onClick={this.endTask}>任务结束</Button>
                &nbsp;&nbsp;&nbsp;
                <Button type="primary" onClick={this.getTableData}>再显示</Button>
            </div>
        );
    },
});
export default OnlineTaskQuery;
