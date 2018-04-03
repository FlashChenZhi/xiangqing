var AsrsJobLogPanel = function (data) {

    this.source = new AsrsSource();
    this.cmp = new CommonSource(data);

    this.form = Ext.create("Ext.form.Panel", {
        title: 'AS/RS作业日志查询',
        collapsible: true,
        scope: this,
        layout: 'column',
        items: [
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.source.com_type,
                    this.source.txt_fromLocation,
                    this.source.txt_fromStation,
                    this.cmp.date_beginDate,
                    this.source.txt_barcode
                ]
            },
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.source.txt_mckey,
                    this.source.txt_toLocation,
                    this.source.txt_toStation,
                    this.cmp.date_endDate
                ]
            }
        ],
        buttons: [
            {
                text: '重置',
                handler: function () {
                    this.up('form').getForm().reset();
                }
            },
            {
                text: '查询',
                scope: this,
                handler: function () {
                    this.store.load();
                }
            }
        ]
    });

    this.store = Ext.create("Ext.data.JsonStore", {
        scope: this,
        fields: ['id', 'mcKey', 'barCode', 'fromLocation', 'toLocation', 'type', 'createDate', 'status', 'fromStation', 'toStation'],
        pageSize: 20,
        listeners: {
            scope: this,
            beforeload: function (store) {
                store.proxy.extraParams = this.form.getValues();
            }
        },
        proxy: {
            type: "ajax",
            url: "wms/asrs/searchAsrsJobLog",
            actionMethods: {read: "POST"},
            reader: {
                type: "json",
                rootProperty: "res",
                totalProperty: "count"
            },
            listeners: {
                exception: function (thiz, request) {
                    extStoreLoadFail(thiz, request);

                }
            }
        }
    });


    var map = new Map();
    map.put('0', '入库');
    map.put('1', '出库');
    map.put('2', '站对站');
    map.put('3', '库对库');
    map.put('4', '空托盘');

    var statusMap = new Map();
    statusMap.put('0', '等待执行');
    statusMap.put('1', '运行');
    statusMap.put('2', '取消');
    statusMap.put('3', '完成');
    statusMap.put('4', '异常');

    var statusDetailMap = new Map();
    statusDetailMap.put('0', '等待执行');
    statusDetailMap.put('1', '到达站台');
    statusDetailMap.put('2', '已指示');
    statusDetailMap.put('3', '接受');
    statusDetailMap.put('4', '取货完成');
    statusDetailMap.put('5', '出库到达');
    statusDetailMap.put('6', '拒绝');
    statusDetailMap.put('7', '完成');
    statusDetailMap.put('8', '异常');
    statusDetailMap.put('9', '目的地不可到达');
    statusDetailMap.put('10', '目的地限制');
    statusDetailMap.put('11', '错误起始地');
    statusDetailMap.put('12', '起始地限制');
    statusDetailMap.put('13', '用户取消');
    statusDetailMap.put('14', '系统取消');

    var dictMap = new Map();
    dictMap.put('0', '通常');
    dictMap.put('1', '迂回');

    var boolMap = new Map();
    boolMap.put(true, '是');
    boolMap.put(false, '否');

    var storageMap = new Map();
    storageMap.put('1', '设定区分');
    storageMap.put('2', '载荷确认');
    this.grid = Ext.create("Ext.grid.Panel", {
            store: this.store,
            scope: this,
            viewConfig: {
                enableTextSelection: true
            },
            columns: [
                {text: "ID", dataIndex: "id"},
                {text: "任务号", dataIndex: "mcKey"},
                {text: "条码", dataIndex: "barCode"},
                {text: "源货位", dataIndex: "fromLocation"},
                {text: "目标货位", dataIndex: "toLocation"},
                {text: "作业类型", dataIndex: "type"},
                {text: "状态", dataIndex: "status"},
                {text: "起始站台", dataIndex: "fromStation"},
                {text: "目的站台", dataIndex: "toStation"},
                {text: "创建时间", dataIndex: "createDate",flex:1}
            ],
            selType: 'cellmodel',
            dockedItems: [
                {
                    xtype: "toolbar",
                    dock: "top",
                    scope: this,
                    items: []
                },
                {
                    xtype: "pagingtoolbar",
                    store: this.store,
                    dock: "bottom",
                    displayInfo: true
                }
            ]
        }
    );


    this.panel = Ext.create("Ext.panel.Panel", {
        tabConfig: {
            tooltip: 'AS/RS作业日志查询'
        },
        overflowY: 'auto',
        items: [
            this.form, this.grid
        ]
    });
};
