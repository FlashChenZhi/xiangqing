var KpiSumSearchSource = function (){

    this.operateType = function () {
        var map = new Map();
        map.put('1', '收货确认');
        map.put('2', '上架');
        map.put('2-201', '密集架上架');
        map.put('2-202', '横梁架上架');
        map.put('2-203', '平库隔板式上架');
        map.put('2-204', '平库流利式dps上架');
        map.put('2-205', '平库流利式上架');
        map.put('2-206', '退品整理格斗货架区上架');
        map.put('2-207', '其他上架');
        map.put('3', '拣选');
        map.put('3-301', '整托拣选');
        map.put('3-302', '箱拣选');
        map.put('3-303', '散零拣选');
        map.put('3-304', '密集架DPS拣选');
        map.put('3-305', '流利架DPS拣选');
        map.put('3-306', '密集架站台拣选');
        map.put('3-307', '其他拣选');
        map.put('4', '打包');
        map.put('6', '集货');
        map.put('7', '发货复核');
        map.put('8', '发货确认');
        map.put('10', '补货');
        map.put('10-1001', '一级补货');
        map.put('10-1002', '二级补货');
        map.put('11', '移库');
        map.put('12', '码垛');
        map.put('13', '上架转移');
        map.put('20', '盘亏');
        map.put('21', '盘盈');
        map.put('22', '例外入库');
        map.put('23', '质量变更');
        map.put('24', '盘亏(WMS)');
        map.put('25', '盘盈(WMS)');
        map.put('26', '例外入库(WMS)');
        map.put('27','强制移库');

        return map;
    };

    var operateTypeStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': '收货确认'},
                {'value': '2-201', 'displayValue': '密集架上架'},
                {'value': '2-202', 'displayValue': '横梁架上架'},
                {'value': '2-203', 'displayValue': '平库隔板式上架'},
                {'value': '2-204', 'displayValue': '平库流利式dps上架'},
                {'value': '2-205', 'displayValue': '平库流利式上架'},
                {'value': '2-206', 'displayValue': '退品整理格斗货架区上架'},
                {'value': '2-207', 'displayValue': '其他上架'},
                {'value': '3-301', 'displayValue': '整托拣选'},
                {'value': '3-302', 'displayValue': '箱拣选'},
                {'value': '3-303', 'displayValue': '散零拣选'},
                {'value': '3-304', 'displayValue': '密集架DPS拣选'},
                {'value': '3-305', 'displayValue': '流利架DPS拣选'},
                {'value': '3-306', 'displayValue': '密集架站台拣选'},
                {'value': '3-307', 'displayValue': '其他拣选'},
                {'value': '4', 'displayValue': '打包'},
                {'value': '6', 'displayValue': '集货'},
                {'value': '7', 'displayValue': '发货复核'},
                {'value': '8', 'displayValue': '发货确认'},
                {'value': '10-1001', 'displayValue': '一级补货'},
                {'value': '10-1002', 'displayValue': '二级补货'},
                {'value': '11', 'displayValue': '移库'},
                {'value': '12', 'displayValue': '码垛'},
                {'value': '13', 'displayValue': '上架转移'},
                {'value': '20', 'displayValue': '盘亏'},
                {'value': '21', 'displayValue': '盘盈'},
                {'value': '22', 'displayValue': '例外入库'},
                {'value': '23', 'displayValue': '质量变更'},
                {'value': '24', 'displayValue': '盘亏(WMS)'},
                {'value': '25', 'displayValue': '盘盈(WMS)'},
                {'value': '26', 'displayValue': '例外入库(WMS)'},
                {'value': '27', 'displayValue': '强制移库'}
            ]
        },
        proxy: {
            type: 'memory',
            reader: {
                type: 'json',
                rootProperty: 'res'
            }
        }
    });



	this.com_operateType =Ext.create('Ext.form.ComboBox', {
	        fieldLabel: '操作类型',
	        name: 'operateType',
	        emptyText:'请选择',
	        store: operateTypeStore,
	        queryMode: 'local',
	        displayField: 'displayValue',
	        selectOnFocus : true,
	        valueField: 'value'
	});


    this.date_beginDate = Ext.create("Ext.form.field.Date", {
        fieldLabel: '完成时间(开始)',
        name: 'beginDate',
        emptyText: "请选择日期",
        selectOnFocus: true,
        format: "Y-m-d",
        altFormats: "Y/m/d|Ymd"
    });

    this.date_endDate = Ext.create("Ext.form.field.Date", {
        fieldLabel: '完成时间(结束)',
        name: 'endDate',
        emptyText: "请选择日期",
        selectOnFocus: true,
        format: "Y-m-d",
        altFormats: "Y/m/d|Ymd"
    });


    this.date_beginDate_month = Ext.create("Ext.form.field.Date", {
        fieldLabel: '月份(开始)',
        name: 'beginDate',
        emptyText: "请选择日期",
        selectOnFocus: true,
        format: "Y-m",
        altFormats: "Y/m|Ym"
    });

    this.date_endDate__month = Ext.create("Ext.form.field.Date", {
        fieldLabel: '月份(结束)',
        name: 'endDate',
        emptyText: "请选择日期",
        selectOnFocus: true,
        format: "Y-m",
        altFormats: "Y/m|Ym"
    });

};
