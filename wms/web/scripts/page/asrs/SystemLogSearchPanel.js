var SystemLogSearchPanel = function () {

    this.source = new LogSearchSource();

    this.form = Ext.create("Ext.form.Panel", {
        title: 'AS/RS作业查询',
        collapsible: true,
        scope: this,
        layout: 'column',
        items: [
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.source.txt_simpleSummary
                ]
            },
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
        fields: ['id','message','type', 'createDate'],
        pageSize: 20,
        listeners: {
            scope: this,
            beforeload: function (store) {
                store.proxy.extraParams = this.form.getValues();
            }
        },
        proxy: {
            type: "ajax",
            url: "wms/asrs/searchSystemLog",
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

    this.grid = Ext.create("Ext.grid.Panel", {
            store: this.store,
            scope: this,
            viewConfig: {
                enableTextSelection: true
            },
            columns: [
                {text: "ID", dataIndex: "id",width:"10%"},
                {text: "类型", dataIndex: "type",width:"10%"},
                {text: "信息", dataIndex: "message",width:"60%"},
                {text: "创建时间", dataIndex: "createDate",flex:1}
            ],
            selType: 'cellmodel',
            listeners: {
                scope: this,
                rowdblclick: function (thiz, record, tr, rowIndex, e, eOpts) {
                },
                rowcontextmenu: function (thiz, record, tr, rowIndex, e, eOpts) {
                    e.preventDefault();
                    if (rowIndex < 0) {
                        return;
                    }
                    var menu = Ext.create("Ext.menu.Menu", {
                        items: [
                            {
                                text: "明细",
                                scope: this,
                                handler: function () {
                                    var rowData = record.data;
                                    var win;
                                    if (rowData.type === '0')
                                        win = new SearchPutawayJobWin(rowData.jobid);
                                    else if (rowData.type === '4')
                                        Ext.alert("无明细");
                                    else
                                        win = new SearchRetrievalJobWin(rowData.jobid);
                                    win.win.show();
                                }
                            },
                            {
                                text: "删除",
                                scope: this,
                                handler: function () {
                                    Ext.Msg.show({
                                        title: '确认',
                                        message: '确认删除该条作业？',
                                        buttons: Ext.Msg.YESNO,
                                        icon: Ext.Msg.QUESTION,
                                        scope: this,
                                        fn: function (btn) {
                                            var rowData = record.data;
                                            var id = rowData.id;
                                            if (btn === 'yes') {
                                                mask.show();
                                                Ext.Ajax.request({
                                                    url: 'wms/asrs/delete',
                                                    method: 'POST',
                                                    params: {'id': id},
                                                    scope: this,
                                                    success: function (response, options) {
                                                        mask.hide();
                                                        var result = Ext.util.JSON.decode(response.responseText);
                                                        if (result.success) {
                                                            Ext.success(result.msg);
                                                            this.store.load();
                                                        } else {
                                                            Ext.error(result.msg);
                                                        }
                                                    },
                                                    failure: function (response, options) {
                                                        mask.hide();
                                                        if (response.responseText == null || response.responseText == '') {
                                                            Ext.error('服务器连接中断,请联系管理员');
                                                        } else {
                                                            var result = Ext.util.JSON.decode(response.responseText);
                                                            Ext.error(result.msg);
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            },
                            {
                                text: "强制完成",
                                scope: this,
                                handler: function () {
                                    Ext.Msg.show({
                                        title: '确认',
                                        message: '确认强制完成该条作业？',
                                        buttons: Ext.Msg.YESNO,
                                        icon: Ext.Msg.QUESTION,
                                        scope: this,
                                        fn: function (btn) {
                                            var rowData = record.data;
                                            var id = rowData.id;
                                            if (btn === 'yes') {
                                                mask.show();
                                                Ext.Ajax.request({
                                                    url: 'wms/asrs/forceDone',
                                                    method: 'POST',
                                                    params: {'id': id},
                                                    scope: this,
                                                    success: function (response, options) {
                                                        mask.hide();
                                                        var result = Ext.util.JSON.decode(response.responseText);
                                                        if (result.success) {
                                                            Ext.success(result.msg);
                                                            this.store.load();
                                                        } else {
                                                            Ext.error(result.msg);
                                                        }
                                                    },
                                                    failure: function (response, options) {
                                                        mask.hide();
                                                        if (response.responseText == null || response.responseText == '') {
                                                            Ext.error('服务器连接中断,请联系管理员');
                                                        } else {
                                                            var result = Ext.util.JSON.decode(response.responseText);
                                                            Ext.error(result.msg);
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            },
                            {
                                text: "AGC无数据强制删除",
                                scope: this,
                                handler: function () {
                                    Ext.Msg.show({
                                        title: '确认',
                                        message: '确认AGC无该作业搬送数据,强制删除WMS搬送作业？',
                                        buttons: Ext.Msg.YESNO,
                                        icon: Ext.Msg.QUESTION,
                                        scope: this,
                                        fn: function (btn) {
                                            var rowData = record.data;
                                            var id = rowData.id;
                                            if (btn === 'yes') {
                                                mask.show();
                                                Ext.Ajax.request({
                                                    url: 'wms/asrs/forceDelete',
                                                    method: 'POST',
                                                    params: {'id': id},
                                                    scope: this,
                                                    success: function (response, options) {
                                                        mask.hide();
                                                        var result = Ext.util.JSON.decode(response.responseText);
                                                        if (result.success) {
                                                            Ext.success(result.msg);
                                                            this.store.load();
                                                        } else {
                                                            Ext.error(result.msg);
                                                        }
                                                    },
                                                    failure: function (response, options) {
                                                        mask.hide();
                                                        if (response.responseText == null || response.responseText == '') {
                                                            Ext.error('服务器连接中断,请联系管理员');
                                                        } else {
                                                            var result = Ext.util.JSON.decode(response.responseText);
                                                            Ext.error(result.msg);
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            }
                        ]
                    });
                    menu.showAt(e.getPoint());
                }
            },
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
            tooltip: 'AS/RS作业查询'
        },
        overflowY: 'auto',
        items: [
            this.form, this.grid
        ]
    });


    var mask = new Ext.LoadMask({
        msg: '系统处理中...',
        target: this.panel
    });
};
