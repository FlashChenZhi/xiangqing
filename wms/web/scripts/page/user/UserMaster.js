var userMaster = function (data) {

    this.cmp = new UserMasterSource();

    this.form = Ext.create("Ext.form.Panel", {
        title: '用户主数据',
        collapsible: true,
        scope: this,
        layout: 'column',
        items: [
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.cmp.txt_code

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
                    this.store.loadPage(1);
                }
            }
        ]
    });

    this.store = Ext.create("Ext.data.JsonStore", {
        scope: this,
        fields: ['id', 'code', 'name', 'roleNames', 'status', 'roleId'],
        pageSize: 20,
        listeners: {
            scope: this,
            beforeload: function (store) {
                store.proxy.extraParams = this.form.getValues();
            }
        },
        proxy: {
            type: "ajax",
            url: "wms/user/list",
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
                createWin = new AddUserWin(data);
            createWin.init(this.store);
            createWin.win.show();
        }
    });

    var statusMap = this.cmp.statusMap();
    this.grid = Ext.create("Ext.grid.Panel", {
            store: this.store,
            scope: this,
            columns: [
                {text: "ID", dataIndex: "id", hidden: true},
                {text: "工号", dataIndex: "code", width: "20%"},
                {text: "名称", dataIndex: "name", width: "20%"},
                {text: "所属角色", dataIndex: "roleNames", width: "50%"},
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
                                        modifyWin = new ModifyUserWin(data);
                                    var rowData = record.data;
                                    modifyWin.init(rowData, this.store);
                                    modifyWin.win.show();
                                }
                            }, {
                                text: "重置密码",
                                scope: this,
                                handler: function () {
                                    mask.show();
                                    var userCode = record.data.code;
                                    Ext.Ajax.request({
                                        url: 'wms/user/initUserPwd',
                                        method: 'POST',
                                        params: {userCode: userCode},
                                        scope: this,
                                        success: function (response, options) {
                                            mask.hide();
                                            var result = Ext.util.JSON.decode(response.responseText);
                                            if (result.success) {
                                                Ext.success(result.msg);
                                                this.win.hide();
                                                if (this.parentStore != null)
                                                    this.parentStore.loadPage(1);
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
                    xtype: "pagingtoolbar",
                    store: this.store,
                    dock: "bottom",
                    displayInfo: true
                },
                {
                    xtype: "toolbar",
                    dock: "top",
                    scope: this,
                    items: [
                        this.createButton
                    ]
                }
            ]
        }
    );


    this.panel = Ext.create("Ext.panel.Panel", {
        tabConfig: {
            tooltip: '用户主数据'
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