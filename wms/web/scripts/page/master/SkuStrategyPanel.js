var SkuStrategyPanel = function (data) {

    this.cmp = new CommonSource(data);

    var exportUserLocBtn = Ext.create("Ext.button.Button", {
            text: "商品区域关系导出",
            scope: this,
            handler: function () {
                var exporturl = 'wms/master/sku/exportExcelTemplate';
                window.location.href = exporturl;
            }
        }
    );


    var file_excel = Ext.create("Ext.form.field.File", {
        name: 'file',
        labelWidth: 50,
        msgTarget: 'side',
        allowBlank: false,
        margin: '0 5 0 0',
        buttonText: '打开'
    });

    var importRuleBtn = Ext.create("Ext.button.Button", {
        text: "规则导入",
        formBind: true,
        scope: this,
        handler: function () {
            if (impForm.isValid()) {
                impForm.submit({
                    timeout: 300,
                    method: 'POST',
                    headers: [
                        {"Request-By": "Ext"}
                    ],
                    url: 'wms/master/sku/importExcel',
                    scope: this,
                    success: function (form, action) {
                        Ext.MessageBox.hide();
                        window.clearInterval(window.intervalId);
                        Ext.success(action.result.msg);
                    },
                    failure: function (form, action) {
                        Ext.MessageBox.hide();
                        window.clearInterval(window.intervalId);
                        switch (action.failureType) {
                            case Ext.form.action.Action.CLIENT_INVALID:
                                Ext.error('表单验证未通过');
                                break;
                            case Ext.form.action.Action.CONNECT_FAILURE:
                                Ext.error('Ajax调用时连接失败');
                                break;
                            case Ext.form.action.Action.SERVER_INVALID:
                                Ext.error(action.result.msg);
                        }
                    }
                });
                var path = file_excel.getValue();
                var index = path.lastIndexOf("\\") + 1;
                var fileName = path.substring(index);
                Ext.MessageBox.show({
                    title: '数据导入',
                    msg: '导入' + fileName,
                    progressText: '正在处理...',
                    progress: true,
                    closable: false
                });
                window.intervalId = setInterval("getProcess()", 1000);
            }
        }
    });

    window.getProcess = function () {
        Ext.Ajax.request({
            url: 'wms/master/sku/getImpProcess',
            headers: {'Content-Type': "application/json; charset=utf-8"},
            method: 'POST',
            success: function (response, options) {
                var result = Ext.util.JSON.decode(response.responseText).res;
                if (result['total'] == -1) {
                    return;
                }
                var percent = result['done'] / result['total'];
                Ext.MessageBox.updateProgress(percent, "正在处理..." + result['done'] + "/" + result['total']);
                if (percent == 1) {
                    clearInterval(window.intervalId);
                }
            },
            failure: function (response, options) {
                //do nothing
            }
        });
    };

    var impForm = Ext.create("Ext.form.Panel", {
        layout: 'column',
        scope: this,
        items: [
            exportUserLocBtn, file_excel, importRuleBtn
        ]
    });

    this.form = Ext.create("Ext.form.Panel", {
        title: 'SKU存储策略',
        collapsible: true,
        scope: this,
        layout: 'column',
        items: [
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.cmp.txt_skuCode,
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
        fields: ['id', 'skuCode', 'skuName', 'position', 'area'],
        pageSize: 20,
        listeners: {
            scope: this,
            beforeload: function (store) {
                store.proxy.extraParams = this.form.getValues();
            }
        },
        proxy: {
            type: "ajax",
            url: "wms/master/sku/getInStrategy",
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
            selModel: Ext.create('Ext.selection.CheckboxModel', {mode: "SIMPLE"}),
            columns: [
                {text: "id", dataIndex: "id", width: "10%", hidden: true},
                {text: "商品代码", dataIndex: "skuCode", width: "20%"},
                {text: "商品名称", dataIndex: "skuName", width: "30%"},
                {text: "位置", dataIndex: "position", width: "20%"},
                {text: "区域", dataIndex: "area", flex: 1},
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
                                text: "删除",
                                handler: function () {
                                    var id = record.data.id;
                                    Ext.Msg.show({
                                        title: '确认',
                                        message: '确认删除？',
                                        buttons: Ext.Msg.YESNO,
                                        icon: Ext.Msg.QUESTION,
                                        scope: this,
                                        fn: function (btn) {
                                            if (btn === 'yes') {
                                                mask.show();
                                                Ext.Ajax.request({
                                                    url: 'wms/master/sku/deleteInStrategy',
                                                    method: 'POST',
                                                    params: {'id': id},
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
                    items: [
                        impForm
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
            tooltip: 'SKU主数据'
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
