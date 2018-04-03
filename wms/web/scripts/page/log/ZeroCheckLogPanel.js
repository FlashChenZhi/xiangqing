var ZeroCheckLogPanel = function (data) {

    this.cmp = new CommonSource(data);

    var typeStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': true, 'displayValue': '扫描'},
                {'value': false, 'displayValue': '手动'}
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

    this.com_type = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '复核方式',
        name: 'type',
        emptyText: '请选择',
        store: typeStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.form = Ext.create("Ext.form.Panel", {
        title: '系统日志查询',
        collapsible: true,
        scope: this,
        layout: 'column',
        items: [
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.cmp.txt_containerBarcode,
                    this.cmp.date_beginDate,
                ]
            },
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.com_type,
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
                    this.store.loadPage(1);
                }
            }
        ]
    });

    this.store = Ext.create("Ext.data.JsonStore", {
        scope: this,
        fields: ['id', 'containerBarCode', 'skuCode', 'skuName', 'type', 'createDate','createUserId'],
        pageSize: 20,
        listeners: {
            scope: this,
            beforeload: function (store) {
                store.proxy.extraParams = this.form.getValues();
            }
        },
        proxy: {
            type: "ajax",
            url: "wms/zeroCheckLog/search",
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

    var typeMap = new Map();
    typeMap.put(true,"扫描");
    typeMap.put(false,"手动");

    this.grid = Ext.create("Ext.grid.Panel", {
            store: this.store,
            scope: this,
            columns: [
                {text: "ID", dataIndex: "id", hidden: true},
                {text: "周转箱代码", width: "10%", dataIndex: "containerBarCode"},
                {text: "商品代码", width: "15%", dataIndex: "skuCode"},
                {text: "商品名称", width: "15%", dataIndex: "skuName"},
                {
                    text: "复核方式", dataIndex: "type", width: "10%", renderer: function (value) {
                    return typeMap.get(value)
                }
                },
                {text: "创建时间", width: "15%", dataIndex: "createDate"},
                {text: "复核人", width: "15%", dataIndex: "createUserId"}

            ],
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
                                    var win = new LogWin();
                                    win.init(record.data);
                                    win.win.title = '<font color="red">' + record.data.name + '</font>';
                                    win.win.show();
                                }
                            }
                        ]
                    });
                    menu.showAt(e.getPoint());
                }
            },
            selType: 'cellmodel',
            viewConfig: {
                enableTextSelection: true
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
            tooltip: '零件复核日志'
        },
        overflowY: 'auto',
        items: [
            this.form, this.grid
        ]
    });
};
