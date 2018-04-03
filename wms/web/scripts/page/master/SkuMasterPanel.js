var SkuMasterPanel = function (data) {

    this.cmp = new CommonSource(data);


    this.form = Ext.create("Ext.form.Panel", {
        title: 'SKU主数据',
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
        fields: ['skuCode', 'skuName'],
        pageSize: 20,
        listeners: {
            scope: this,
            beforeload: function (store) {
                store.proxy.extraParams = this.form.getValues();
            }
        },
        proxy: {
            type: "ajax",
            url: "wms/master/sku/getSkus",
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

    var map = new Map();
    map.put(true, '是');
    map.put(false, '否');

    this.grid = Ext.create("Ext.grid.Panel", {
            store: this.store,
            scope: this,
            viewConfig: {
                enableTextSelection: true
            },
            selModel: Ext.create('Ext.selection.CheckboxModel', {mode: "SIMPLE"}),
            columns: [
                {text: "商品代码", dataIndex: "skuCode",width:"50%"},
                {text: "商品名称", dataIndex: "skuName",width:"50%"},
            ],
            selType: 'cellmodel',
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
            tooltip: 'SKU主数据'
        },
        overflowY: 'auto',
        items: [
            this.form, this.grid
        ]
    });

};
