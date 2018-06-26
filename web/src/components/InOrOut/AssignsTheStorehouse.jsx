import {Button,Modal, Popover,Tabs ,Form, Radio, Table, Badge, DatePicker, Select, message, Row, Col} from 'antd';
import React from 'react';
const FormItem = Form.Item;
import reqwest from 'reqwest';
import './css/jquery.seat-charts.css';
import './js/jquery-1.9.1';
import './js/jquery.seat-charts';


const TabPane = Tabs.TabPane;
const Option = Select.Option;
const confirm = Modal.confirm;
const RangePicker = DatePicker.RangePicker;
const RadioGroup = Radio.Group;

var sc ="";
var content = (
    <div>
        <p>Content</p>
        <p>Content</p>
    </div>
);

let AssignsTheStorehouse = React.createClass({
    getInitialState(){
        return {
            commodityCodeList:[],
            commodityCodeFirst:"",
            map:[],
            availableList:[],
            reservedOutList:[],
            reservedInList:[],
            emptyList:[],
            tabKey:1,
            PopoverModelVisible:false,
            focusLocation:"",
            msg:"",
            skuName:"",
            skuCode:"",
            lotNum:"",
            barcode:"",
            qty:"",
            commodityStationNoList:[],
            commodityStationFirst:"",
            stationNo:"",
            name:"",
            selectLocation:[],
            cancelLocation:[],
        };
    },
    componentDidMount(){
        this.getCommodityCode();
        this.getCommodityStattionNo();
        this.getStorageLocationData(1);
        this.initChart();
    },
    getCommodityStattionNo(){
        // let stationNo =JSON.stringify(this.state.commodityStationNoList);
        reqwest({
            url:'/wms/master/AssignsTheStorehouseAction/getStationNo',
            dataType:'json',
            method:'post',
            data:{},
            success:function(json){
                if(json.success){
                    this.setState({
                        commodityStationNoList:json.res,
                        commodityStationFirst:json.res[0],
                    })
                }else{
                    message.error("初始化站台号失败！");
                }
            }.bind(this),
            error:function () {
                message.error("获取站台号失败");
            }.bind(this),
            }
        )
    },
    getCommodityCode(){
        reqwest({
            url: '/wms/master/FindOutOrInWarehouseAction/getSkuCode',
            dataType: 'json',
            method: 'post',
            data: {},
            success: function (json) {
                if(json.success) {
                    this.setState({
                        commodityCodeList: json.res,
                        commodityCodeFirst: json.res[0],
                    })
                }else{
                    message.error("初始化Sku代码失败！");
                }
            }.bind(this),
            error: function (err) {
                message.error("初始化Sku代码失败！");
            }.bind(this)
        })
    },
    getStorageLocationData(level){
        const values = this.props.form.getFieldsValue();
        console.log(values);
        reqwest({
            url: '/wms/master/AssignsTheStorehouseAction/getStorageLocationData',
            dataType: 'json',
            method: 'post',
            data: {productId:values.productId,tier:level},
            success: function (json) {
                if(json.success) {
                    console.log(json.res);
                    this.setState({
                        map: json.res.map,
                        availableList: json.res.availableList,
                        emptyList: json.res.emptyList,
                        reservedOutList:json.res.reservedOutList,
                        reservedInList:json.res.reservedInList,
                        selectLocation:[],
                    })
                    console.log(this.state.map);
                    $(".legend").hide();
                    if(level==1){
                        $("#legend1").show();
                        this.initChart(this.state.map,"#seat-map1","#legend1");
                    }else if(level==2){
                        $("#legend2").show();
                        this.initChart(this.state.map,"#seat-map2","#legend2");
                    }else if(level==3){
                        $("#legend3").show();
                        this.initChart(this.state.map,"#seat-map3","#legend3");
                    }else if(level==4){
                        $("#legend4").show();
                        this.initChart(this.state.map,"#seat-map4","#legend4");
                    }
                    sc.get(json.res.unavailableList).status('unavailable');
                    sc.get(json.res.availableList).status('available');
                    sc.get(json.res.reservedOutList).status('reservedOut');
                    sc.get(json.res.emptyList).status('empty');
                    sc.get(json.res.reservedInList).status('reservedIn');
                }else{
                    message.error("初始化库位代码失败！");
                }
            }.bind(this),
            error: function (err) {
                message.error("初始化库位代码失败！");
            }.bind(this)
        })
    },

    initChart(map,divId,legendId) {
        var thisOut = this;
        var $cart = $('#selected-seats'), //库位
            $counter = $('#counter'), //票数
            $total = $('#total'); //总计金额
        sc = $(divId).seatCharts({
            map: map,
            naming:{
                top    : true,
                left   : true,
                getId  : function(character, row, column) {
                    return column + '_' + row;
                },
                getLabel : function (character, row, column) {
                    return column;
                },
                getData:function(dataList){
                    console.log(dataList)
                    return dataList;
                },
                data: map,
            },
            legend : { //定义图例
                node : $(legendId),
                items : [
                    [ 'a', 'available', '可选货位' ],
                    [ 'a', 'unavailable', '不可选货位'],
                    [ 'a', 'reservedOut', '已有出库任务'],
                    [ 'a', 'reservedIn', '已有入库任务'],
                    [ 'a', 'empty', '空货位'],
                    [ 'a', 'selected', '已选货位']
                ]
            },
            //点击事件
            click: function () {
                if (this.status() == 'available') {
                    $('<li>'+(this.settings.column+1)+'排'+this.settings.label+'座</li>')
                        .attr('id', 'cart-item-'+this.settings.id)
                        .data('seatId', this.settings.id)
                        .appendTo($cart);
                    $counter.text(sc.find('selected').length+1);
                    thisOut.outClick(this.settings);
                    return 'selected';
                } else if (this.status() == 'selected') {
                    thisOut.outClick(this.settings);
                    $counter.text(sc.find('selected').length-1);
                    //删除已预订座位
                    $('#cart-item-'+this.settings.id).remove();
                    //可选座
                    return 'available';
                } else if (this.status() == 'unavailable') {
                    return 'unavailable';
                } else {
                    return this.style();
                }
            },
            //获取焦点事件
            focus  : function() {
                thisOut.outFocus(this.settings);

                if (this.status() == 'available') {
                    return 'focused';
                } else  {
                    return this.style();
                }
            },
            //失去焦点事件
            blur   : function() {
                thisOut.outBlur(this.settings);

                return this.status();
            },
        });
        console.log(sc.data);
        //已售出的座位

    },
    hideChangeLevelModel() {
        this.setState({blockNo: null, changeLevelModel: false});
    },
    outClick(settings){
        let bank = settings.column+1;
        let bay = settings.row+1;
        let level = this.state.tabKey;
        // let stationNo=this.state.stationNo;
        if(settings.status=='available'){
            reqwest({
                url: '/wms/master/AssignsTheStorehouseAction/getNextAvailableLocation',
                dataType: 'json',
                method: 'post',
                data: {bank:bank,bay:bay,level:level},
                success: function (json) {
                    if(json.success) {
                        if(json.res.status){
                            sc.get(json.res.location).status('available');
                        }
                        let locationNo = this.PrefixInteger(bank,3)+this.PrefixInteger(bay,3)+this.PrefixInteger(level,3);
                        this.state.selectLocation.push(locationNo);
                        // this.state.commodityStationNoList.push(stationNo);
                        console.log(this.state.selectLocation);
                    }else{
                        message.error("获取下一位库位代码失败！");
                    }
                }.bind(this),
                error: function (err) {
                    message.error("获取下一位库位代码失败！");
                }.bind(this)
            })
        }else if(settings.status=='selected'){
            reqwest({
                url: '/wms/master/AssignsTheStorehouseAction/getAgoUnavailableLocation',
                dataType: 'json',
                method: 'post',
                data: {bank:bank,bay:bay,level:level},
                success: function (json) {
                    if(json.success) {
                        if(json.res.status){
                            sc.get(json.res.location).status('unavailable');
                        }
                        this.setState({
                            cancelLocation:json.res.location,
                        })

                        this.state.cancelLocation.forEach((s)=>{
                            let locationNo = this.PrefixInteger(s.split("_")[0],3)+this.PrefixInteger(s.split("_")[1],3)+this.PrefixInteger(level,3);
                            let index = this.state.selectLocation.indexOf(locationNo);
                            if(index >= 0){
                                this.state.selectLocation.splice(index,1);
                            }
                        })
                        let locationNo = this.PrefixInteger(bank,3)+this.PrefixInteger(bay,3)+this.PrefixInteger(level,3);
                        let index = this.state.selectLocation.indexOf(locationNo);
                        if(index >= 0){
                            this.state.selectLocation.splice(index,1);
                        }
                        console.log( this.state.selectLocation);
                    }else{
                        message.error("初始化库位代码失败！");
                    }
                }.bind(this),
                error: function (err) {
                    message.error("初始化库位代码失败！");
                }.bind(this)
            })
        }

    },
    outFocus(settings){
        let bank = settings.column+1;
        let bay = settings.row+1;
        let level = this.state.tabKey;
        this.setState({PopoverModelVisible: true});
        reqwest({
            url: '/wms/master/AssignsTheStorehouseAction/getLocationInfo',
            dataType: 'json',
            method: 'post',
            data: {bank:bank,bay:bay,level:level},
            success: function (json) {
                if(json.success) {
                    this.setState({
                        skuName:json.res.skuName,
                        skuCode:json.res.skuCode,
                        lotNum:json.res.lotNum,
                        qty:json.res.qty,
                        msg:json.res.msg,
                        barcode:json.res.barcode,
                    });
                    if(json.res.bank!=null){
                        this.setState({
                            locationInfo:json.res.bank+'排'+json.res.bay+'列'+json.res.level+'层',
                        })
                    }else{
                        this.setState({
                            locationInfo:"",
                        })
                    }
                }else{
                    message.error("初始化库位代码失败！");
                }
            }.bind(this),
            error: function (err) {
                message.error("初始化库位代码失败！");
            }.bind(this)
        })
    },
    outBlur(settings){

        this.setState({focusLocation:"",PopoverModelVisible: false});
    },
    PrefixInteger(num, length) {
        return ( "000" + num ).substr( -length );
    },
    handleSubmit2(e) {
        e.preventDefault();
        let locationList =JSON.stringify(this.state.selectLocation);
        const values = this.props.form.getFieldsValue();
        reqwest({
            url: '/wms/master/AssignsTheStorehouseAction/assignsTheStorehouse',
            dataType: 'json',
            method: 'post',
            data: { selectLocation:locationList,stationNo:values.stationNo},
            success: function (json) {
                if(json.success) {
                    message.success(json.msg);
                    let level = this.state.tabKey;
                    this.getStorageLocationData(level);
                }else{
                    message.error(json.msg);
                }
            }.bind(this),
            error: function (err) {
                message.error("初始化库位代码失败！");
            }.bind(this)
        })
    },
    handleSubmit(e) {
        e.preventDefault();
        let level = this.state.tabKey;
        this.getStorageLocationData(level);
     },
    handleReset(e) {
        this.props.form.resetFields();
        for(let i =1;i<5;i++){
            this.getStorageLocationData(i);
        }
    },
    tabCallback(key){
        console.log(key);
        this.setState({
            tabKey:key,
            selectLocation:[],
        });
        this.getStorageLocationData(key);
    },


    render() {
        const {getFieldProps} = this.props.form;
        const formItemLayout = {
            labelCol: {span: 5},
            wrapperCol: {span: 14},
        };
        const formItemLayout2 = {
            labelCol: {span: 5},
            wrapperCol: {span: 10},
        };
        const commodityCodeProps = getFieldProps('productId', {
            initialValue:"",
        });
        const tierProps = getFieldProps('tier', {
            initialValue:"1",
        });

        const commodityStationNoProps = getFieldProps('stationNo', {
            initialValue:"",
        });
        const commodityCodeListSelect =[];
        commodityCodeListSelect.push(<Option value="">---请选择---</Option>);
        this.state.commodityCodeList.forEach((commodityCode)=>{
            commodityCodeListSelect.push(<Option value={commodityCode.skuCode}>{commodityCode.skuName}</Option>);
        });

        const commodityStationNoSelect=[];
        commodityStationNoSelect.push(<Option value="">---请选择---</Option>);
        this.state.commodityStationNoList.forEach((commodityCode)=>{
            commodityStationNoSelect.push(<Option value={commodityCode.stationNo}>{commodityCode.stationNo}</Option>);
        });
        return (
            <div style={{overflow:"auto",width:"1800px"}}>
                <Form horizontal>
                    <Row>
                        <Col lg={9}>
                            <FormItem
                                {...formItemLayout}
                                label="商品名称："
                            >
                                <Select
                                    showSearch
                                    filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}
                                    id="select" size="large" style={{ width: 200 }}
                                    {...commodityCodeProps} >
                                    {commodityCodeListSelect}
                                </Select>
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <Button type="primary" onClick={this.handleSubmit}>查询</Button>
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <Button type="ghost" onClick={this.handleReset}>重置</Button>
                            </FormItem><br/>
                            <FormItem  {...formItemLayout}  label="出库站台：" >
                                <Select
                                    showSearch
                                    filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}
                                    id="select" size="large" style={{ width: 200 }}
                                    {...commodityStationNoProps} >
                                    {commodityStationNoSelect}
                                </Select>
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <Button type="primary" onClick={this.handleSubmit2}>提交</Button>
                            </FormItem>
                            {/*<FormItem wrapperCol={{offset: 10}}>*/}
                            {/*</FormItem>*/}
                        </Col>

                        <Col lg={15}>
                            <br/><br/><br/>
                            <div id="legend1" className={'legend'}></div>
                            <div id="legend2" className={'legend'}></div>
                            <div id="legend3" className={'legend'}></div>
                            <div id="legend4" className={'legend'}></div>

                        </Col>
                        <Col lg={3}>
                            <div id="Info1" style={{paddingLeft:"50px",fontWeight: "bold"}}>
                                <span >货位状态：{this.state.msg}</span><br/>
                                <span >位置信息：{this.state.locationInfo}</span><br/>
                                <span >商品数量：{this.state.qty}</span>

                            </div>
                        </Col>
                        <Col lg={10}>
                            <div id="Info1" style={{paddingLeft:"50px",fontWeight: "bold"}}>
                                <span >商品名称：{this.state.skuName}</span><br/>
                                <span >托盘号码：{this.state.barcode}</span><br/>
                                <span >商品批次：{this.state.lotNum}</span>
                            </div>
                        </Col>
                    </Row>
                </Form>
                <Row>
                    <Tabs defaultActiveKey="1" onChange={this.tabCallback}>
                        <TabPane tab="第一层" key="1">
                            <div id="seat-map1"  >
                                <div className={'front'}>第一层库位</div>
                            </div>
                        </TabPane>
                        <TabPane tab="第二层" key="2">
                            <div id="seat-map2"  >
                                <div className={'front'}>第二层库位</div>
                            </div>
                        </TabPane>
                        <TabPane tab="第三层" key="3">
                            <div id="seat-map3"  >
                                <div className={'front'}>第三层库位</div>
                            </div>
                        </TabPane>
                        <TabPane tab="第四层" key="4">
                            <div id="seat-map4"  >
                                <div className={'front'}>第四层库位</div>
                            </div>
                        </TabPane>
                    </Tabs>
                </Row>
                {/*/>*/}
            </div>
        );
    },
});
AssignsTheStorehouse = Form.create({})(AssignsTheStorehouse);
export default AssignsTheStorehouse;
