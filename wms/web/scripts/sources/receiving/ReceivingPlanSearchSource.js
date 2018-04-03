var ReceivingPlanSearchSource = function () {

    this.receivingType = function(){
        var map = new Map();
        map.put('01','供应商采购入库');
        map.put('02','委外采购入库');
        map.put('03','常规门店退货入库');
        map.put('04','加盟退货入库');
        map.put('05','团购退货入库');
        map.put('06','特渠退品退货入库');
        map.put('07','RDC调拨入库');
        map.put('08','RDC退货入库');
        map.put('09','转仓入库');
        map.put('10','转仓入库（311）');
        map.put('11','zm55');
        map.put('12','老翻新');
        map.put('13','电商B类客户退货入库');
        map.put('14','调拨入库');
        map.put('15','电商转仓入库');
        map.put('16','例外入库');
        map.put('17','Hibris退货');
        map.put('18','电商退货入库');
        return map;
    };

    this.sapReceivingType = function(){
        var map = new Map();
        map.put('01','供应商采购入库');
        map.put('02','委外采购入库');
        map.put('03','常规门店退货入库');
        map.put('04','加盟退货入库');
        map.put('05','团购退货入库');
        map.put('06','RDC调拨入库');
        map.put('07','RDC退货入库');
        map.put('08','转仓入库');
        map.put('09','转仓入库（311）');
        map.put('10','DIY礼盒入库');
        map.put('11','调拨入库');
        map.put('12','电商转仓入库');
        map.put('13','电商B类客户退货入库');
        map.put('14','电商退货入库');
        return map;
    };

    this.receivingStatus = function(){
        var map = new Map();
        map.put('1','开始');
        map.put('2','收货中');
        map.put('3','完成');
        map.put('9','取消');
        return map;
    };


    var receivingTypeStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data:{
            'res':[
                {'value':'01','displayValue':'供应商采购入库'},
                {'value':'02','displayValue':'委外采购入库'},
                {'value':'03','displayValue':'常规门店退货入库'},
                {'value':'04','displayValue':'加盟退货入库'},
                {'value':'05','displayValue':'团购退货入库'},
                {'value':'06','displayValue':'特渠退品退货入库'},
                {'value':'07','displayValue':'RDC调拨入库'},
                {'value':'08','displayValue':'RDC退货入库'},
                {'value':'09','displayValue':'转仓入库'},
                {'value':'10','displayValue':'转仓入库（311）'},
                {'value':'11','displayValue':'zm55'},
                {'value':'12','displayValue':'老翻新'},
                {'value':'13','displayValue':'电商B类客户退货入库'},
                {'value':'14','displayValue':'调拨入库'},
                {'value':'15','displayValue':'电商转仓入库'},
                {'value':'16','displayValue':'例外入库'},
                {'value':'17','displayValue':'Hibris退货'},
                {'value':'18','displayValue':'电商退货入库'}
            ]
        },
        proxy:{
            type:'memory',
            reader:{
                type:'json',
                rootProperty:'res'
            }
        }
    });

    var receivingStatusStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data:{
            'res':[
                {'value':'1','displayValue':'开始'},
                {'value':'2','displayValue':'收货中'},
                {'value':'3','displayValue':'完成'},
                {'value':'9','displayValue':'取消'}
            ]
        },
        proxy:{
            type:'memory',
            reader:{
                type:'json',
                rootProperty:'res'
            }
        }
    });


    this.txt_cnorDocNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: 'SAP单据号',
        name: 'cnorDocNo'
    });

    this.com_receivingType =Ext.create('Ext.form.ComboBox', {
        fieldLabel: '入库类型',
        name: 'receivingType',
        emptyText:'请选择',
        store: receivingTypeStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus : true,
        valueField: 'value'
    });

    this.com_receivingStatus=Ext.create('Ext.form.ComboBox', {
        fieldLabel: '收货状态',
        name: 'receivingStatus',
        emptyText:'请选择',
        store: receivingStatusStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus : true,
        valueField: 'value'
    });


    this.date_receivingStartDateFrom = Ext.create("Ext.form.field.Date", {
        fieldLabel: '收货开始时间',
        name: 'receivingStartDate',
        emptyText: "请选择日期",
        selectOnFocus:true,
        format: "Y-m-d",
        altFormats: "Y/m/d|Ymd"
    });

    this.date_receivingStartDateTo = Ext.create("Ext.form.field.Date", {
        name: 'receivingEndDate',
        emptyText: "请选择日期",
        selectOnFocus:true,
        format: "Y-m-d",
        altFormats: "Y/m/d|Ymd"
    });

    this.date_receivingEndDateFrom = Ext.create("Ext.form.field.Date", {
        fieldLabel: '收货结束时间',
        name: 'receivingStartDate',
        emptyText: "请选择日期",
        selectOnFocus:true,
        format: "Y-m-d",
        altFormats: "Y/m/d|Ymd"
    });
    
    this.date_receivingEndDateTo = Ext.create("Ext.form.field.Date", {
        name: 'receivingEndDate',
        emptyText: "请选择日期",
        selectOnFocus:true,
        format: "Y-m-d",
        altFormats: "Y/m/d|Ymd"
    });


    this.txt_keyword = Ext.create("Ext.form.field.Text", {
        fieldLabel: '关键字',
        name: 'keyword'
    });

    this.txt_shipingDocNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '发货方单据号',
        name: 'shipingDocNo'
    });
    
};
