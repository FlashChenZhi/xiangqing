var DirectRetrievalPanel = function (data) {

    this.source = new CommonSource(data);

    var retrievanBtn = Ext.create("Ext.button.Button", {
            text: "出库",
            scope: this,
            handler: function () {

                var form = this.form.getForm();

                if (form.isValid()) {
                    Ext.Msg.show({
                        title: '确认',
                        message: '确认出库？',
                        buttons: Ext.Msg.YESNO,
                        icon: Ext.Msg.QUESTION,
                        scope: this,
                        fn: function (btn) {
                            if (btn === 'yes') {
                                var results = [];
                                var sels = this.grid.getSelection();

                                for (var res in sels) {
                                    results.push({
                                        barCode: sels[res].data.palletNo,
                                    });
                                }
                                if (sels.length == 0) {
                                    Ext.warn('请至少选择一条数据');
                                    return;
                                }

                                mask.show();
                                Ext.Ajax.request({
                                    url: 'wms/order/directRetrieval',
                                    headers: {'Content-Type': "application/json; charset=utf-8"},
                                    method: 'POST',
                                    params: Ext.util.JSON.encode(results),
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
        }
    );

    var impForm = Ext.create("Ext.form.Panel", {
        layout: 'column',
        scope: this,
        items: [
            retrievanBtn
        ]
    });


    this.locQcPanel = Ext.create("Ext.form.Panel", {
        title: '库存地点条件',
        layout: 'column',
        scope: this,
        collapsible: true,
        items: [
            {
                layout: "form", columnWidth: .5, defaults: {anchor: '100%'},
                items: [
                    this.source.txt_locationNo,
                    this.source.txt_containerBarcode,
                    this.source.txt_skuCode,
                    this.source.txt_bayNo

                ]
            },
            {
                layout: 'form', columnWidth: .5,
                items: [
                    this.source.txt_whCode,
                    this.source.txt_position,
                    this.source.txt_levelNo
                ]
            }
        ]
    });

    this.tabs = Ext.create('Ext.tab.Panel', {
        columnWidth: 1,
        items: [this.locQcPanel]
    });

    this.form = Ext.create("Ext.form.Panel", {
        title: '库存查询',
        collapsible: true,
        scope: this,
        layout: 'column',
        items: this.tabs,
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
        fields: ['whCode', 'itemCode','status', 'qty', 'locationNo', 'bank', 'bay', 'lev', 'palletNo', 'lotNum', 'itemName'],
        pageSize: 20,
        listeners: {
            scope: this,
            beforeload: function (store) {
                store.proxy.extraParams = this.form.getValues();
            }
        },
        proxy: {
            type: "ajax",
            url: "wms/order/invList",
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


    this.sm = Ext.create("Ext.selection.CheckboxModel", {
        checkOnly: true
    });

    var statusMap = new Map();
    statusMap.put("0","正常");
    statusMap.put("1","冻结");


    this.grid = Ext.create("Ext.grid.Panel", {
            store: this.store,
            scope: this,
            selModel: this.sm,
            viewConfig: {
                enableTextSelection: true
            },
            columns: [
                {text: "仓库", dataIndex: "whCode", width: "5%"},
                {text: "货位号", dataIndex: "locationNo", width: "10%"},
                {text: "排", dataIndex: "bank", width: "8%"},
                {text: "列", dataIndex: "bay", width: "8%"},
                {text: "层", dataIndex: "lev", width: "8%"},
                {text: "托盘码", dataIndex: "palletNo", width: "10%"},
                {text: "商品代码", dataIndex: "itemCode", width: "10%"},
                {text: "商品名称", dataIndex: "itemName", width: "20%"},
                {text: "数量", dataIndex: "qty", width: "10%"},
                {text: "状态", dataIndex: "status", width: "10%", scope: this, renderer: function (value) {
                    return statusMap.get(value);
                }},
                {text: "批次", dataIndex: "lotNum", flex: 1},
            ],
            listeners: {},
            selType: 'cellmodel',
            viewConfig: {
                enableTextSelection: true
            },
            features: [
                {
                    ftype: 'summary'
                }
            ],
            dockedItems: [
                {
                    xtype: "toolbar",
                    dock: "top",
                    scope: this,
                    items: [impForm]
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
            tooltip: '指定库存出库'
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