import {Button, Form, Input,Table,  Pagination, InputNumber, Select, message, } from 'antd';
import React from 'react';
const FormItem = Form.Item;
import reqwest from 'reqwest';
import {reqwestError, dateFormat} from '../common/Golbal';

const Option = Select.Option;

const columns = [{
    title: 'ID',
    dataIndex: 'id',
},{
    title: 'McKey',
    dataIndex: 'mcKey',
}, {
    title: '托盘号',
    dataIndex: 'containerId',
}, {
    title: '货品名称',
    dataIndex: 'skuName',
}, {
    title: '货品数量',
    dataIndex: 'qty',
},{
    title: '批号',
    dataIndex: 'lotNo',
}, {
    title: '类型',
    dataIndex: 'type',
}, {
    title: '起始位置',
    dataIndex: 'fromStation',
}, {
    title: '结束位置',
    dataIndex: 'toStation',
},{
    title: '作业状态',
    dataIndex: 'status',
},{
    title: '创建时间',
    dataIndex: 'createDate',
    render:(text,record)=>{
        let date = new Date(text).toLocaleString();
        console.log(date)
        return(
            date
        )
    },
}];
let PutInStorage = React.createClass({
    getInitialState(){
        return {
            tuopanhao: "",//托盘号
            loading: false,
            commodityCodeList:[],//货品代码集合
            commodityCodeFirst:"",//货品代码第一个
            total: 0,//表格数据总行数
            loading: false,
            selectedData: [],//点击设定提交到后台的数据
            selectedRowKeys: [],
            defaultPageSize:6,
            current:1,
        };
    },
    componentDidMount(){
        this.getCommodityCode();
        this.getData(1);
    },

    getCommodityCode(){
        reqwest({
            url: '/wms/master/putInStorage/getCommodityCode',
            dataType: 'json',
            method: 'post',
            data: {},
            success: function (json) {
                if(json.success){
                    console.log(json);
                    console.log(json.res);
                    this.setState({
                        commodityCodeList:json.res,
                        commodityCodeFirst:json.res[0],
                    })
                }else{
                    message.error("初始化商品代码失败！");
                }
            }.bind(this),
            error: function (err) {
                message.error("初始化商品代码失败！");
            }.bind(this)
        })
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
            url: '/wms/master/putInStorage/findPutInStorageOrder',
            dataType: 'json',
            method: 'post',
            data: {current:current,defaultPageSize:defaultPageSize},
            success: function (json) {
                if(json.success){
                    console.log("数据："+json.res);
                    this.setState({data: json.res, total: json.count, loading: false});
                    this.stateReset();
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
    pageChange(noop){
        this.setState({
            selectedRowKeys:[],
            current:noop,
        })
        this.getData(noop);
    },
    submit(e){
        e.preventDefault();
        const current = this.state.current;
        this.props.form.validateFieldsAndScroll((err, values) => {
            if (!err) {
                console.log('Received values of form: ', values);
                let tuopanhao= encodeURI(values.tuopanhao,"utf-8");
                let zhantai= values.zhantai;
                let commodityCode= values.commodityCode;
                let num= values.Num;
                let lotNo = values.lotNo;
                reqwest({
                    url: '/wms/master/putInStorage/addTask',
                    dataType: 'json',
                    method: 'post',
                    data: {tuopanhao: tuopanhao,zhantai:zhantai,commodityCode:commodityCode,lotNo:lotNo,num:num},
                    success: function (json) {
                        if (!json.success) {
                            message.error(json.msg);
                        } else {
                            message.success("设定任务成功！");
                            this.getData(current);

                        }
                        this.props.form.setFieldsValue({
                            tuopanhao:'',
                        });
                    }.bind(this),
                    error: function (err) {
                        message.error("设定任务失败！");
                        this.handleReset(e);
                    }.bind(this)
                })
            }
        });

    },
    delete(e){
        e.preventDefault();
        const selectedRowKeys = this.state.selectedRowKeys;
        var selectedRowKeysString='';
        for(let i = 0;i<selectedRowKeys.length;i++){
            if(i!=selectedRowKeys.length-1){
                selectedRowKeysString =selectedRowKeysString+ selectedRowKeys[i]+",";
            }else{
                selectedRowKeysString =selectedRowKeysString+ selectedRowKeys[i];
            }
        }
        const current = this.state.current;
        reqwest({
            url: '/wms/master/putInStorage/deleteTask',
            dataType: 'json',
            method: 'post',
            data: {selectedRowKeysString: selectedRowKeysString},
            success: function (json) {
                if (!json.success) {
                    message.error(json.msg);
                } else {
                    message.success("删除任务成功！")
                    this.getData(current);
                }
                this.props.form.setFieldsValue({
                    tuopanhao:'',
                });
            }.bind(this),
            error: function (err) {
                message.error("删除任务失败！");
                this.handleReset(e);
            }.bind(this)
        })

    },
    /**
     * 重置表单
     * @param e
     */
    handleReset(e) {
        console.log("进入清除！");
        this.props.form.resetFields();
    },
    stateReset(){
        this.setState({
            selectedRowKeys:[],
            selectedData:[],
        })
    },
    onChange(selectedRowKeys, selectedRows) {
        console.log(selectedRowKeys);
        this.setState({selectedData: selectedRows, selectedRowKeys: selectedRowKeys});
    },

    render() {
        const {getFieldProps } = this.props.form;
        const formItemLayout = {
            labelCol: {span: 5},
            wrapperCol: {span: 14},
        };

        const tuopanhaoProps = getFieldProps('tuopanhao', {
            rules: [{ required: true, message: '请扫描托盘号！' }]
        });
        const NumProps = getFieldProps('Num', {
            rules: [{ required: true, message: '请输入数量！' }],
            initialValue:"1",
        });
        const commodityCodeProps = getFieldProps('commodityCode', {
            initialValue:this.state.commodityCodeFirst.id,
        });

        const lotNoProps = getFieldProps('lotNo');

        const commodityCodeListSelect =[];
        this.state.commodityCodeList.forEach((commodityCode)=>{
            commodityCodeListSelect.push(<Option value={commodityCode.id}>{commodityCode.name}</Option>);
        });
        return (
            <div>
                <Form horizontal >
                    <FormItem
                        {...formItemLayout}
                        label="托盘号："
                    >

                        <Input style={{width:"300"}}
                            {...tuopanhaoProps}   placeholder="请扫描托盘号" />
                    </FormItem>
                    <FormItem
                        {...formItemLayout}
                        label="商品名称："
                    >
                        <Select id="select" size="large" style={{ width: 200 }}
                                {...commodityCodeProps} >
                            {commodityCodeListSelect}
                        </Select>
                    </FormItem>
                    <FormItem
                        {...formItemLayout}
                        label="批号："
                    >

                        <Input style={{width:"300"}}
                               {...lotNoProps}   placeholder="请输入批号" />
                    </FormItem>
                    <FormItem
                        {...formItemLayout}
                        label="数量："
                    >

                        {/*<Input style={{width:"300"}}*/}
                               {/*{...NumProps}   placeholder="请输入数量" />*/}
                        <InputNumber min={1} defaultValue={2}   {...NumProps}  />
                    </FormItem>
                    <FormItem
                        {...formItemLayout}
                        label="站台："
                    >
                        <Select id="select" size="large" defaultValue="1101" style={{ width: 200 }}
                        {...getFieldProps('zhantai', { initialValue: '1101' })} >
                            <Option value="1101">1101</Option>
                            <Option value="1301">1301</Option>
                        </Select>
                    </FormItem>
                    <FormItem wrapperCol={{offset: 6}}>
                        <Button type="primary" onClick={this.submit}
                                //disabled={this.state.tuopanhao.length > 0 ? false : true}
                        >设定</Button>
                        <Button style={{marginLeft:"13%"}} type="primary" onClick={this.update}
                            //disabled={this.state.tuopanhao.length > 0 ? false : true}
                        >刷新</Button>
                        <Button style={{marginLeft:"13%"}} type="primary" onClick={this.delete}
                            //disabled={this.state.tuopanhao.length > 0 ? false : true}
                        >删除</Button>
                    </FormItem>
                </Form><br/>
                <Table rowSelection={{onChange: this.onChange, selectedRowKeys: this.state.selectedRowKeys,}}
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
            </div>
        );
    },
});
PutInStorage = Form.create()(PutInStorage);
export default PutInStorage;
