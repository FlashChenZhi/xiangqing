var MenuMaster = function (data) {
    this.cmp = new MenuMasterSource();


    this.form = Ext.create("Ext.form.Panel", {
        title: '菜单主数据',
        collapsible: true,
        scope: this,
        layout: 'column',
        items: [
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.cmp.txt_code,
                    this.cmp.com_dist
                ]
            },
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.cmp.com_status
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
        fields: ['id', 'type', 'dist', 'code', 'name', 'seq', 'status'],
        listeners: {
            scope: this,
            beforeload: function (store) {
                store.proxy.extraParams = this.form.getValues();
            }
        },
        proxy: {
            type: "ajax",
            url: "wms/menu/list",
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

    var createWin = null;
    var modifyWin = null;

    this.createButton = Ext.create("Ext.button.Button", {
        text: "新建",
        scope: this,
        handler: function () {
            if (createWin == null)
                createWin = new AddMenuWin(data);
            createWin.init(this.store);
            createWin.win.show();
        }
    });

    this.exportBtn = Ext.create("Ext.button.Button", {
        text: "菜单导出",
        scope: this,
        handler: function () {
            var exporturl = 'wms/menu/exportExcel';
            window.location.href = exporturl;
        }
    });

    var typeMap = this.cmp.typeMap();
    var statusMap = this.cmp.statusMap();
    var distMap = this.cmp.distMap();
    this.grid = Ext.create("Ext.grid.Panel", {
            store: this.store,
            scope: this,
            maxHeight: 400,
            columns: [
                {text: "ID", dataIndex: "id", hidden: true},
                {
                    text: "类型", dataIndex: "type", width: "10%", scope: this, renderer: function (value) {
                    return typeMap.get(value);
                }
                },
                {
                    text: "区分", dataIndex: "dist", width: "10%", scope: this, renderer: function (value) {
                    return distMap.get(value);
                }
                },
                {text: "代码", dataIndex: "code", width: "15%"},
                {text: "名称", dataIndex: "name", width: "30%"},
                {text: "上级菜单", dataIndex: "parentCode", width: "15%"},
                {text: "顺序号", dataIndex: "seq", width: "10%"},
                {
                    text: "状态", dataIndex: "status", width: "10%", scope: this, renderer: function (value) {
                    return statusMap.get(value);
                }
                }
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
                                text: "修改",
                                scope: this,
                                handler: function () {
                                    if (modifyWin == null)
                                        modifyWin = new ModifyMenuWin(data);
                                    var rowData = record.data;
                                    modifyWin.init(rowData, this.store);
                                    modifyWin.win.show();
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
                    items: [
                        this.exportBtn, this.createButton
                    ]
                }
            ]
        }
    );


    this.panel = Ext.create("Ext.panel.Panel", {
        tabConfig: {
            tooltip: '菜单主数据'
        },
        overflowY: 'auto',
        items: [
            this.form, this.grid
        ]
    });

}