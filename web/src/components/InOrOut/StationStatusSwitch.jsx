import {Button,Modal, Form, Input,Table, Pagination, InputNumber, Select, message, } from 'antd';
import React from 'react';
const FormItem = Form.Item;
import reqwest from 'reqwest';
import {reqwestError, dateFormat} from '../common/Golbal';
import PlatformSwitch from './PlatformSwitch';

const Option = Select.Option;


let StationStatusSwitch = React.createClass({
    getInitialState(){
        return {
            loading: false,
            total: 0,//表格数据总行数
            selectedRowKeys: [],
            stationNo:this.stationNo,
            pattern:this.pattern,
            defaultPageSize:8,
            current:1,
            data:'',
            platformSwitch: false,
            updateStationNo:'',
        };
    },
    componentDidMount(){
        this.findPlatformSwitch("1101");
        this.getData(1);
    },
    findPlatformSwitch(stationNo){
        reqwest({
            url: '/wcs/stationStatusChange/findStatusChange.do',
            dataType: 'json',
            method: 'post',
            data: {stationNo:stationNo},
            success: function (json) {
                if(json.success){
                    console.log(json.res);
                    this.props.form.setFieldsValue({
                        pattern:json.res,
                    });
                }
            }.bind(this),
            error: function (err) {
                message.error("初始化站台状态失败！");
                this.handleReset(err);
            }.bind(this)
        })
    },
    submit(e){
        e.preventDefault();
        this.props.form.validateFieldsAndScroll((err, values) => {
            if (!err) {
                console.log('Received values of form: ', values);
                let pattern= values.pattern;
                let stationNo= values.stationNo;
                reqwest({
                    url: '/wcs/stationStatusChange/updateStatusChange.do',
                    dataType: 'json',
                    method: 'post',
                    data: {pattern: pattern,stationNo:stationNo},
                    success: function (json) {
                        if(json.success){
                            message.success(json.msg);
                        } else {
                            message.error(json.msg);
                        }
                    }.bind(this),
                    error: function (err) {
                        message.error(err.msg);
                        this.handleReset(err);
                    }.bind(this)
                })
            }
        });
    },

    /**
     * 重置表单
     * @param e
     */
    handleReset(e) {
        console.log("进入清除！");
        this.props.form.resetFields();
    },
    onChange1(value){
        console.log(value);
        this.props.form.setFieldsValue({
            stationNo: value,
        });
        this.findPlatformSwitch(value);
    },
    onChange2(value){
        console.log('Change2'+value);
        this.setState({
            pattern:value,
        });
        this.props.form.setFieldsValue({
            pattern: value,
        });
    },
    pageChange(noop){
        this.setState({
            current:noop,
        })
        this.getData(noop);
    },
    update(){
        this.getData(this.state.current);
    },
    getData(current){
        this.setState({loading: true});
        let defaultPageSize = this.state.defaultPageSize;
        const values = this.props.form.getFieldsValue();
        console.log(values);
        values.currentPage = current;
        reqwest({
            url: '/wcs/stationStatusChange/findStationStatus.do',
            dataType: 'json',
            method: 'post',
            data: {current:current,defaultPageSize:defaultPageSize},
            success: function (json) {
                if(json.success){
                    console.log("数据："+json.res[0]);
                    this.setState({data: json.res, total: json.count, loading: false});

                }else{
                    message.error("加载数据失败！");
                }
            }.bind(this),
            error: function (err) {
                reqwestError(err);
                message.error("加载数据失败！");
            }.bind(this)
        });
    },
    updateStationMode(stationNo){
        this.setState({updateStationNo: stationNo, platformSwitch: true});
    },

    hidePlatformSwitch() {
        this.setState({updateStationNo: null, platformSwitch: false});
    },
    render() {
        const columns = [{
            title: '站台号',
            dataIndex: 'stationNo',
        },{
            title: '名称',
            dataIndex: 'name',
        },{
            title: '站台模式',
            dataIndex: 'mode',
        }, {
            title: '可入区域',
            dataIndex: 'putAwayArea',
        }, {
            title: '可出区域',
            dataIndex: 'retrievalArea',
        }, {
            title: '站台状态',
            dataIndex: 'status',
        }, {
            title: '操作',
            render: (text, record,index) => <span><a onClick={this.updateStationMode.bind(this,record.stationNo)}>修改</a></span>,
        }];
        const {getFieldProps } = this.props.form;
        const formItemLayout = {
            labelCol: {span: 5},
            wrapperCol: {span: 14},
        };

        return (
            <div>
                <Form horizontal >
                    <br/><br/>
                    <FormItem
                        {...formItemLayout}
                        label="站台：">
                        <Select id="select" size="large" defaultValue="1101" style={{ width: 200 }}
                                {...getFieldProps('stationNo', { initialValue: "1101" })} onChange={this.onChange1}>
                            <Option value="1101">1101</Option>
                            <Option value="1102">1102</Option>
                        </Select>
                    </FormItem>
                    <FormItem
                        {...formItemLayout}
                        label="状态：">
                        <Select id="select" size="large" defaultValue="1" style={{ width: 200 }}
                                {...getFieldProps('pattern')} onChange={this.onChange2} >
                            <Option value="1">启用</Option>
                            <Option value="0">禁用</Option>
                        </Select>
                    </FormItem>
                    <br/><br/>
                    <FormItem wrapperCol={{offset: 6}}>
                        <Button type="primary" onClick={this.submit}
                            //disabled={this.state.tuopanhao.length > 0 ? false : true}
                        >设定</Button>
                        <Button style={{marginLeft:"13%"}} type="primary" onClick={this.update}
                            //disabled={this.state.tuopanhao.length > 0 ? false : true}
                        >刷新</Button>
                    </FormItem><br/>
                </Form>
                <Table
                    loading={this.state.loading}
                    columns={columns}
                    rowKey={record => record.id}
                    dataSource={this.state.data}

                    pagination={{
                        onChange: this.pageChange,
                        showQuickJumper: true,
                        defaultCurrent: 1,
                        defaultPageSize:this.state.defaultPageSize,
                        total: this.state.total,
                        showTotal: total => `共 ${total} 条数据`
                    }}
                />
                <PlatformSwitch
                    code={this.state.updateStationNo}
                    visible={this.state.platformSwitch}
                    hideModel={this.hidePlatformSwitch.bind(this)}
                />
            </div>
        );
    },
});

StationStatusSwitch = Form.create()(StationStatusSwitch);
export default StationStatusSwitch;

