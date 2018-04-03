var LogSearchPanel = function (data) {

    this.cmp = new LogSearchSource();

    this.form = Ext.create("Ext.form.Panel", {
        title: '系统日志查询',
        collapsible: true,
        scope: this,
        layout: 'column',
        items: [
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.cmp.com_systemCode,
                    this.cmp.com_code,
                    this.cmp.date_beginDate,
                    this.cmp.txt_simpleSummary
                ]
            },
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.cmp.com_level,
                    this.cmp.txt_name,
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
        fields: ['id', 'systemCode', 'level', 'code', 'name', 'summary', 'createDate'],
        pageSize: 20,
        listeners: {
            scope: this,
            beforeload: function (store) {
                store.proxy.extraParams = this.form.getValues();
            }
        },
        proxy: {
            type: "ajax",
            timeout: 900000,
            url: "wms/systemLogSearch/list",
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


    var systemCodeMap = this.cmp.systemCodeMap();
    var levelMap = this.cmp.levelMap();
    this.grid = Ext.create("Ext.grid.Panel", {
            store: this.store,
            scope: this,
            columns: [
                {text: "ID", dataIndex: "id", hidden: true},
                {text: "系统代码", dataIndex: "systemCode", width : "10%", renderer: function (value) {
                    return systemCodeMap.get(value)
                }},
                {
                    text: "日志级别", dataIndex: "level",width : "10%", renderer: function (value) {
                    return levelMap.get(value)
                }
                },
                {text: "日志代码", width : "10%",dataIndex: "code"},
                {text: "日志名称",width : "15%", dataIndex: "name"},
                {text: "创建时间",width : "15%", dataIndex: "createDate"},
                {text: "备注",width : "40%", dataIndex: "summary", scope: this, renderer: function (value) {
                    if (value == null)
                        return "";
                    var brIndex = value.indexOf("<br/>");
                    if (brIndex == -1) {
                        return value;
                    } else {
                        var shortRemarkStr = value.substring(0, brIndex);
                        return shortRemarkStr;
                    }
                }}
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
                    items: [

                    ]
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
            tooltip: '系统日志查询'
        },
        overflowY: 'auto',
        items: [
            this.form, this.grid
        ]
    });
};
