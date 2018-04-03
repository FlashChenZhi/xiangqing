var InventoryRelease = function (data) {

    this.source = new CommonSource(data);

    this.form = Ext.create("Ext.form.Panel", {
        title: '库存管理',
        collapsible: true,
        scope: this,
        layout: 'column',
        items: [
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.source.txt_containerBarcode,
                    this.source.txt_batchNo
                ]
            }, {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.source.txt_skuCode,
                    this.source.com_status
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
            },
            {
                text: '冻结',
                scope: this,
                handler: function () {
                    Ext.Msg.show({
                        title: '确认',
                        message: '确认冻结？',
                        buttons: Ext.Msg.YESNO,
                        icon: Ext.Msg.QUESTION,
                        scope: this,
                        fn: function (btn) {
                            if (btn === 'yes') {
                                var barCode = this.source.txt_containerBarcode.getValue();
                                var batchNo = this.source.txt_batchNo.getValue();
                                var skuCode = this.source.txt_skuCode.getValue();
                                mask.show();
                                Ext.Ajax.request({
                                    url: 'wms/inventory/frozen',
                                    method: 'POST',
                                    params: {'barcode': barCode, 'batchNo': batchNo, 'skuCode': skuCode},
                                    scope: this,
                                    timeout: 9000000,
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
                text: '解除冻结',
                scope: this,
                handler: function () {
                    Ext.Msg.show({
                        title: '确认',
                        message: '确认解除冻结？',
                        buttons: Ext.Msg.YESNO,
                        icon: Ext.Msg.QUESTION,
                        scope: this,
                        fn: function (btn) {
                            if (btn === 'yes') {
                                var barCode = this.source.txt_containerBarcode.getValue();
                                var batchNo = this.source.txt_batchNo.getValue();
                                var skuCode = this.source.txt_skuCode.getValue();

                                mask.show();
                                Ext.Ajax.request({
                                    url: 'wms/inventory/realease',
                                    method: 'POST',
                                    params: {'barcode': barCode, 'batchNo': batchNo, 'skuCode': skuCode},
                                    scope: this,
                                    timeout: 9000000,

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

    this.store = Ext.create("Ext.data.JsonStore", {
        scope: this,
        fields: ['id', 'whCode', 'itemCode', 'qty', 'locationNo', 'caseBarCode', 'palletNo', 'lotNum', 'itemName','status'],
        pageSize: 20,
        listeners: {
            scope: this,
            beforeload: function (store) {
                store.proxy.extraParams = this.form.getValues();
            }
        },
        proxy: {
            type: "ajax",
            url: "wms/inventory/list",
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

    var statusMap = new Map();
    statusMap.put("0","正常");
    statusMap.put("1","冻结");

    this.grid = Ext.create("Ext.grid.Panel", {
            store: this.store,
            scope: this,
            viewConfig: {
                enableTextSelection: true
            },
            columns: [
                {text: "ID", dataIndex: "id", hidden: true},
                {text: "仓库", dataIndex: "whCode", width: "5%"},
                {text: "商品代码", dataIndex: "itemCode", width: "15%"},
                {text: "商品名称", dataIndex: "itemName", width: "20%"},
                {text: "数量", dataIndex: "qty", width: "10%"},
                {text: "批次", dataIndex: "lotNum", width: "10%"},
                {text: "状态", dataIndex: "status", width: "10%", scope: this, renderer: function (value) {
                    return statusMap.get(value);
                }},
                {text: "货位号", dataIndex: "locationNo", width: "10%"},
                {text: "箱码", dataIndex: "caseBarCode", width: "10%"},
                {text: "托盘码", dataIndex: "palletNo", flex: 1},
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
            tooltip: '库存管理'
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
