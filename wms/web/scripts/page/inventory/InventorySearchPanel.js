var InventorySearchPanel = function (data) {

    this.source = new CommonSource(data);

    var exportExcelBtn = Ext.create("Ext.button.Button", {
            text: "导出",
            scope: this,
            handler: function () {

                var locationNo = this.source.txt_locationNo.getValue();
                if (locationNo == null) {
                    locationNo = '';
                }

                var barCode = this.source.txt_containerBarcode.getValue();
                if (barCode == null) {
                    barCode = '';
                }

                var skuCode = this.source.txt_skuCode.getValue();
                if (skuCode == null) {
                    skuCode = '';
                }

                var whCode = this.source.txt_whCode.getValue();
                if (whCode == null) {
                    whCode = '';
                }

                var cartonCode = this.source.txt_barcodes.getValue();
                if (cartonCode == null) {
                    cartonCode = '';
                }

                var batchNo = this.source.txt_batchNo.getValue();
                if (batchNo == null) {
                    batchNo = '';
                }

                var exporturl = 'wms/inventory/exportExcel?locationNo=' + locationNo + "&containerBarcode="
                    + barCode + '&skuCode=' + skuCode + "&whCode=" + whCode + "&barcodes=" + cartonCode + "&batchNo=" + batchNo;

                window.location.href = exporturl;

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
                    this.source.com_status
                ]
            },
            {
                layout: 'form', columnWidth: .5,
                items: [
                    this.source.txt_whCode,
                    this.source.txt_barcodes,
                    this.source.txt_batchNo
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
        fields: ['id', 'whCode', 'itemCode', 'qty', 'locationNo', 'caseBarCode', 'palletNo', 'lotNum', 'itemName', 'status'],
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
    statusMap.put("0", "正常");
    statusMap.put("1", "冻结");

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
                {
                    text: "状态", dataIndex: "status", width: "10%", scope: this, renderer: function (value) {
                    return statusMap.get(value);
                }
                },
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
            tooltip: '库存查询'
        },
        overflowY: 'auto',
        items: [
            this.form, this.grid
        ]
    });

};