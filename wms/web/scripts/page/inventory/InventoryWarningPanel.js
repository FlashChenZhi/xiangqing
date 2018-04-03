var InventoryWarningPanel = function (data) {

    this.source = new CommonSource(data);

    var exportExcelBtn = Ext.create("Ext.button.Button", {
            text: "导出",
            scope: this,
            handler: function () {

            }
        }
    );

    var impForm = Ext.create("Ext.form.Panel", {
        layout: 'column',
        scope: this,
        items: [
            exportExcelBtn
        ]
    });


    this.locQcPanel = Ext.create("Ext.form.Panel", {
        title: '查询条件',
        layout: 'column',
        scope: this,
        collapsible: true,
        items: [
            {
                layout: "form", columnWidth: .5, defaults: {anchor: '100%'},
                items: [
                    this.source.com_warningTypeFlag
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
        fields: ['id', 'skuCode', 'skuName', 'lotNum', 'location', 'container'],
        listeners: {
            scope: this,
            beforeload: function (store) {
                store.proxy.extraParams = this.form.getValues();
            }
        },
        proxy: {
            type: "ajax",
            url: "wms/inventory/inventoryWarning",
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
                {text: "ID", dataIndex: "id", hidden: true},
                {text: "商品代码", dataIndex: "skuCode",width:"15%"},
                {text: "商品名称", dataIndex: "skuName",width:"30%"},
                {text: "批次", dataIndex: "lotNum",width:"10%"},
                {text: "货位号", dataIndex: "location",width:"10%"},
                {text: "托盘码", dataIndex: "container", flex: 1}
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

            ]
        }
    );


    this.panel = Ext.create("Ext.panel.Panel", {
        tabConfig: {
            tooltip: '库存预警'
        },
        overflowY: 'auto',
        items: [
            this.form, this.grid
        ]
    });

};