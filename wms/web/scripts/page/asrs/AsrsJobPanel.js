var AsrsJobPanel = function (data) {

    this.source = new AsrsSource();
    this.cmp = new CommonSource(data);

    this.form = Ext.create("Ext.form.Panel", {
        title: 'AS/RS作业查询',
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
                    this.source.txt_barcode
                ]
            },
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.source.txt_mckey,
                    this.source.txt_toLocation,
                    this.source.txt_toStation,
                    this.cmp.txt_retrievalOrderNo

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
            url: "wms/asrs/list",
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
                {text: "创建时间", dataIndex: "createDate", flex: 1}
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
                                text: "强制完成",
                                handler: function () {
                                    Ext.Msg.show({
                                        title: '确认',
                                        message: '强制完成？',
                                        buttons: Ext.Msg.YESNO,
                                        icon: Ext.Msg.QUESTION,
                                        scope: this,
                                        fn: function (btn) {
                                            if (btn === 'yes') {
                                                var mcKey = record.data.mcKey;
                                                mask.show();
                                                Ext.Ajax.request({
                                                    url: 'wms/asrs/finish',
                                                    method: 'POST',
                                                    params: {'mcKey': mcKey},
                                                    scope: this,
                                                    success: function (response, options) {
                                                        mask.hide();
                                                        var result = Ext.util.JSON.decode(response.responseText);
                                                        if (result.success) {
                                                            Ext.success(result.msg);
                                                            this.store.loadPage(1);
                                                        } else {
                                                            Ext.error(result.msg);
                                                        }
                                                    },
                                                    failure: function (response, options) {
                                                        mask.hide();
                                                        extAjaxFail(response, options);
                                                    }
                                                });
                                            }
                                            else {
                                                //todo cancel
                                            }
                                        }
                                    });
                                }
                            },
                            {
                                text: "WCS无任务删除",
                                handler: function () {
                                    Ext.Msg.show({
                                        title: '确认',
                                        message: '确认删除？',
                                        buttons: Ext.Msg.YESNO,
                                        icon: Ext.Msg.QUESTION,
                                        scope: this,
                                        fn: function (btn) {
                                            if (btn === 'yes') {
                                                var mcKey = record.data.mcKey;
                                                mask.show();
                                                Ext.Ajax.request({
                                                    url: 'wms/asrs/delete',
                                                    method: 'POST',
                                                    params: {'mcKey': mcKey},
                                                    scope: this,
                                                    success: function (response, options) {
                                                        mask.hide();
                                                        var result = Ext.util.JSON.decode(response.responseText);
                                                        if (result.success) {
                                                            Ext.success(result.msg);
                                                            this.store.loadPage(1);
                                                        } else {
                                                            Ext.error(result.msg);
                                                        }
                                                    },
                                                    failure: function (response, options) {
                                                        mask.hide();
                                                        extAjaxFail(response, options);
                                                    }
                                                });
                                            }
                                            else {
                                                //todo cancel
                                            }
                                        }
                                    });
                                }
                            },
                            {
                                text: "重复存放",
                                handler: function () {
                                    Ext.Msg.show({
                                        title: '确认',
                                        message: '强制解决？',
                                        buttons: Ext.Msg.YESNO,
                                        icon: Ext.Msg.QUESTION,
                                        scope: this,
                                        fn: function (btn) {
                                            if (btn === 'yes') {
                                                var mcKey = record.data.mcKey;
                                                mask.show();
                                                Ext.Ajax.request({
                                                    url: 'wms/asrs/duplicatedStorage',
                                                    method: 'POST',
                                                    params: {'mcKey': mcKey},
                                                    scope: this,
                                                    success: function (response, options) {
                                                        mask.hide();
                                                        var result = Ext.util.JSON.decode(response.responseText);
                                                        if (result.success) {
                                                            Ext.success(result.msg);
                                                            this.store.loadPage(1);
                                                        } else {
                                                            Ext.error(result.msg);
                                                        }
                                                    },
                                                    failure: function (response, options) {
                                                        mask.hide();
                                                        extAjaxFail(response, options);
                                                    }
                                                });
                                            }
                                            else {
                                                //todo cancel
                                            }
                                        }
                                    });
                                }
                            }, {
                                text: "空出库",
                                handler: function () {
                                    Ext.Msg.show({
                                        title: '确认',
                                        message: '强制解决？',
                                        buttons: Ext.Msg.YESNO,
                                        icon: Ext.Msg.QUESTION,
                                        scope: this,
                                        fn: function (btn) {
                                            if (btn === 'yes') {
                                                var mcKey = record.data.mcKey;
                                                mask.show();
                                                Ext.Ajax.request({
                                                    url: 'wms/asrs/finish',
                                                    method: 'POST',
                                                    params: {'mcKey': mcKey},
                                                    scope: this,
                                                    success: function (response, options) {
                                                        mask.hide();
                                                        var result = Ext.util.JSON.decode(response.responseText);
                                                        if (result.success) {
                                                            Ext.success(result.msg);
                                                            this.store.loadPage(1);
                                                        } else {
                                                            Ext.error(result.msg);
                                                        }
                                                    },
                                                    failure: function (response, options) {
                                                        mask.hide();
                                                        extAjaxFail(response, options);
                                                    }
                                                });
                                            }
                                            else {
                                                //todo cancel
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
