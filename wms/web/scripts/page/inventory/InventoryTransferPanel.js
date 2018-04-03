var InventoryTransferPanel = function (data) {

    this.source = new CommonSource(data);

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
        fields: ['whCode', 'itemCode', 'qty', 'locationNo', 'palletNo', 'lotNum', 'itemName'],
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

    this.grid = Ext.create("Ext.grid.Panel", {
            store: this.store,
            scope: this,
            selModel: this.sm,
            viewConfig: {
                enableTextSelection: true
            },
            columns: [
                {text: "仓库", dataIndex: "whCode", width: "5%"},
                {text: "货位号", dataIndex: "locationNo", width: "20%"},
                {text: "托盘码", dataIndex: "palletNo", width: "15%"},
                {text: "商品代码", dataIndex: "itemCode", width: "15%"},
                {text: "商品名称", dataIndex: "itemName", width: "20%"},
                {text: "数量", dataIndex: "qty", width: "10%"},
                {text: "批次", dataIndex: "lotNum", flex: 1},
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
                                text: "自动调整库存批次",
                                handler: function () {
                                    Ext.Msg.show({
                                        title: '确认',
                                        message: '根据批次移动托盘？',
                                        buttons: Ext.Msg.YESNO,
                                        icon: Ext.Msg.QUESTION,
                                        scope: this,
                                        fn: function (btn) {
                                            if (btn === 'yes') {
                                                var palletNo = record.data.palletNo;
                                                mask.show();
                                                Ext.Ajax.request({
                                                    url: 'wms/inventory/transfer',
                                                    method: 'POST',
                                                    params: {'palletNo': palletNo},
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
                        ]
                    });
                    menu.showAt(e.getPoint());
                }
            },
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
            tooltip: '库内移动'
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