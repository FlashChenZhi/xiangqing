import {Button, Form,Row, Col,Modal, Input,Table,  Pagination, InputNumber, Select, message, } from 'antd';
import React from 'react';
const FormItem = Form.Item;
import reqwest from 'reqwest';
import {reqwestError, dateFormat} from '../common/Golbal';

const Option = Select.Option;

const columns = [ {
    title: 'ID',
    dataIndex: 'id',
},{
    title: '货品代码',
    dataIndex: 'skuCode',
},{
    title: '货品名称',
    dataIndex: 'skuName',
}, {
    title: '出库数量',
    dataIndex: 'qty',
},{
    title: '批号',
    dataIndex: 'lotNum',
}];
let thisIn ="";
let i=0;
let PutInStorage = React.createClass({

    getInitialState(){
        return {
            data:[],
            commodityCodeList:[],//货品代码集合
            commodityCodeFirst:"",//货品代码第一个
            lotNumList:[],//批次集合
            lotNumListFirst:"",//批次第一个
            total: 0,//表格数据总行数
            selectedData: [],//点击设定提交到后台的数据
            selectedRowKeys: [],
            defaultPageSize:6,
            current:1,
            visible: false,
            formRef:"",
        };
    },

    componentDidMount(){
        this.getCommodityCode();
        this.getLotNums();
        //this.getData(1);
    },

    getCommodityCode(){
        reqwest({
            url: '/wms/master/FindOutOrInWarehouseAction/getSkuCode',
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

    getLotNums(){
        reqwest({
            url: '/wms/master/StockOutODOAction/getLotNums',
            dataType: 'json',
            method: 'post',
            data: {},
            success: function (json) {
                if(json.success){
                    console.log(json);
                    console.log(json.res);
                    this.setState({
                        lotNumList:json.res,
                        lotNumListFirst:json.res[0].lotNum,
                    })
                }else{
                    message.error("初始化批次失败！");
                }
            }.bind(this),
            error: function (err) {
                message.error("初始化批次失败！");
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
        /*reqwest({
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
        });*/
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
        /*const data = this.state.data;*/
        let data =JSON.stringify(this.state.data);
        this.props.form.validateFieldsAndScroll((err, values) => {
            if (!err) {
                console.log('Received values of form: ', values);
                let driver= encodeURI(values.driver,"utf-8");
                let createPerson= encodeURI(values.createPerson,"utf-8");
                let placeOfArrival= encodeURI(values.placeOfArrival,"utf-8");
                let car= encodeURI(values.car,"utf-8");
                let orderNo= encodeURI(values.orderNo,"utf-8");
                let zhantai= encodeURI(values.zhantai,"utf-8");
                reqwest({
                    url: '/wms/master/StockOutODOAction/addOrder',
                    dataType: 'json',
                    method: 'post',
                    data: {driver: driver,orderNo:orderNo,createPerson:createPerson,
                        placeOfArrival:placeOfArrival,car:car,zhantai:zhantai,data:data},
                    success: function (json) {
                        if (!json.success) {
                            message.error(json.msg);
                        } else {
                            message.success("设定任务成功！");
                        }
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
        const data = this.state.data;
        for(var i = 0;i<selectedRowKeys.length;i++){
             data.splice(selectedRowKeys[i]-i,1);
        }
        this.setState({
            data:data,
            selectedRowKeys:[],
        })
    },

    findNumBySkuAndLotNum(skuCode,lotNum){
        reqwest({
            url: '/wms/master/StockOutODOAction/findNumBySkuAndLotNum',
            dataType: 'json',
            method: 'post',
            data: {skuCode: skuCode,lotNum:lotNum},
            success: function (json) {
                if (!json.success) {
                    message.error("获取商品数量失败！");
                    thisIn.setFieldsValue({
                        oneTunnel: 0,
                        twoTunnel: 0,
                    });
                }else{
                    console.log(json.res)
                    for(var i=0;i<json.res.length;i++){
                        const result = json.res[i];
                        if(result.position==1){
                            thisIn.setFieldsValue({
                                oneTunnel: result.count,
                            });
                        }else if(result.position==2){
                            thisIn.setFieldsValue({
                                twoTunnel: result.count,
                            });
                        }
                    }

                }
            }.bind(this),
            error: function (err) {
                message.error("获取商品数量失败！");
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

    lotNumChange(value){
        thisIn.validateFields((err, values) => {
            if (err) {
                return;
            }
            const skuCode = values.commodityCode;
            this.findNumBySkuAndLotNum(skuCode,value);
        });
        thisIn.setFieldsValue({
            lotNum: value,
        });
    },

    commodityCodeChange(value){
        thisIn.validateFields((err, values) => {
            if (err) {
                return;
            }
            const lotNum = values.lotNum;
            this.findNumBySkuAndLotNum(value,lotNum);
        });
        thisIn.setFieldsValue({
            commodityCode: value,
        });
    },

    showModal(){
        this.setState({ visible: true });
        this.findNumBySkuAndLotNum(this.state.commodityCodeFirst.skuCode,this.state.lotNumListFirst);
    },

    handleCancel() {
        this.setState({ visible: false });
    },

    handleCreate()  {
        console.log(thisIn);
        const form = thisIn;
        form.validateFields((err, values) => {
            if (err) {
                return;
            }
            console.log('Received values of form: ', values);

            const data = this.state.data;

            const skuCode =  values.commodityCode;
            let skuName="";
            for(var j=0;j<this.state.commodityCodeList.length;j++){
                if(this.state.commodityCodeList[j].skuCode==skuCode){
                    skuName=this.state.commodityCodeList[j].skuName;
                }
            }

            let flag=1;
            for(var j=0;j<data.length;j++){
                if(data[j].skuCode==skuCode && data[j].lotNum==values.lotNum){
                    flag=2;
                    message.error("已选择同物料同批次的货物!");
                }
            }
            if(values.Num>(values.oneTunnel+values.twoTunnel)){
                flag=2;
                message.error("出库数量不能大于仓库存储数量!");
            }
            if(flag==1){
                data.push({
                    id: i,
                    skuCode: skuCode,
                    skuName:skuName,
                    qty: values.Num,
                    lotNum: values.lotNum,
                });
                i=i+1;
                this.setState({
                    data:data,
                    visible: false,
                });
                form.resetFields();
            }

        });
    },


    render() {
        const {getFieldProps } = this.props.form;
        const formItemLayout = {
            labelCol: {span: 5},
            wrapperCol: {span: 14},
        };
        const orderNoProps = getFieldProps('orderNo', {
            rules: [{ required: true, message: '请输入订单号！' }],
        });
        const driverProps = getFieldProps('driver');
        const carProps = getFieldProps('car');
        const createPersonProps = getFieldProps('createPerson');
        const placeOfArrivalProps = getFieldProps('placeOfArrival');

        const CollectionCreateForm = Form.create()(
            class extends React.Component {
                render() {
                    const { visible, onCancel, onCreate, form,thisOut } = this.props;
                    thisIn=this.props.form;
                    const {getFieldProps } = form;
                    const NumProps = getFieldProps('Num', {
                        rules: [{ required: true, message: '请输入出库托盘数量！' }],
                        initialValue:"2500",
                    });
                    const commodityCodeProps = getFieldProps('commodityCode', {
                        initialValue:thisOut.state.commodityCodeFirst.skuCode,
                    });
                    const commodityCodeListSelect =[];
                    thisOut.state.commodityCodeList.forEach((commodityCode)=>{
                        commodityCodeListSelect.push(<Option value={commodityCode.skuCode}>{commodityCode.skuName}</Option>);
                    });
                    const lotNumProps = getFieldProps('lotNum',{
                        initialValue:thisOut.state.lotNumListFirst,
                    });
                    const lotNumSelect =[];
                    thisOut.state.lotNumList.forEach((lotNum)=>{
                        lotNumSelect.push(<Option value={lotNum.lotNum}>{lotNum.lotNum}</Option>);
                    });
                    const oneTunnelProps = getFieldProps('oneTunnel',{
                        initialValue:0,
                    });
                    const twoTunnelProps = getFieldProps('twoTunnel',{
                        initialValue:0,
                    });
                    /*const oneOutStationProps = getFieldProps('oneOutStation',{
                        initialValue:0,
                    });
                    const twoOutStationProps = getFieldProps('twoOutStation',{
                        initialValue:0,
                    });*/
                    return (
                        <Modal
                            width={700}
                            visible={visible}
                            title="添加商品"
                            okText="添加"
                            onCancel={onCancel}
                            onOk={onCreate}
                        >
                            <Form >
                                <Row>
                                    <FormItem
                                        {...formItemLayout}
                                        label="商品名称："
                                    >
                                        <Select id="select" size="large" style={{ width: 200 }}
                                                {...commodityCodeProps} onChange={thisOut.commodityCodeChange}>
                                            {commodityCodeListSelect}
                                        </Select>
                                    </FormItem>

                                    <FormItem
                                        {...formItemLayout}
                                        label="商品批号："
                                    >
                                        <Select id="select" size="large" style={{ width: 200 }}
                                                {...lotNumProps} onChange={thisOut.lotNumChange}>
                                            {lotNumSelect}
                                        </Select>
                                    </FormItem>
                                    <FormItem
                                        {...formItemLayout}
                                        label="出库数量："
                                    >
                                        <InputNumber min={1} defaultValue={2}   {...NumProps}  />
                                    </FormItem>
                                </Row>
                                <Row>
                                    <Col span={12}>
                                        <FormItem
                                            {...formItemLayout}
                                            label="1巷道数量："
                                        >
                                            <InputNumber size="small" defaultValue={0} disabled={true}  {...oneTunnelProps}  />

                                        </FormItem>
                                        {/*<FormItem

                                            label="可出站台："
                                        >
                                            <span id="select"  style={{ width: 100 }}
                                                  {...oneOutStationProps} >
                                            </span>
                                        </FormItem>*/}
                                    </Col>
                                    <Col span={12}>
                                        <FormItem
                                            {...formItemLayout}
                                            label="2巷道数量："
                                        >
                                            <InputNumber size="small"  defaultValue={0}  disabled={true}  {...twoTunnelProps}  />
                                        </FormItem>
                                        {/*<FormItem

                                            label="可出站台："
                                        >
                                            <span id="select"  style={{ width: 100 }}
                                                  {...twoOutStationProps} >
                                            </span>
                                        </FormItem>*/}
                                    </Col>
                                </Row>
                            </Form>
                        </Modal>
                    );
                }
            }
        );
        return (
            <div>
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
                                <Select id="select" size="large" defaultValue="1" style={{ width: 200 }}
                                        {...getFieldProps('zhantai', { initialValue: '1' })} >
                                    <option value={"1"}>出库口一</option>
                                    <option value={"2"}>出库口二</option>
                                    <option value={"3"}>出库口三</option>
                                </Select>
                            </FormItem>
                        </Col>
                    </Row>
                    <Row gutter={16}>
                       <span style={{paddingLeft:"120px"}}></span>
                        <Button  type="primary" onClick={this.showModal}>添加商品</Button>
                    </Row>
                        <br/>
                        <Table rowSelection={{onChange: this.onChange, selectedRowKeys: this.state.selectedRowKeys,}}
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


                    <FormItem wrapperCol={{offset: 6}}>
                        <Button type="primary" onClick={this.submit}
                        >设定</Button>
                        <Button style={{marginLeft:"13%"}} type="primary" onClick={this.delete}
                        >删除</Button>
                    </FormItem>
                </Form>

                <CollectionCreateForm
                    /*wrappedComponentRef={(formRef) => this.formRef = formRef}*/
                    visible={this.state.visible}
                    onCancel={this.handleCancel}
                    onCreate={this.handleCreate}
                    thisOut={this}
                />
            </div>
        );
    },
});
PutInStorage = Form.create()(PutInStorage);
export default PutInStorage;
