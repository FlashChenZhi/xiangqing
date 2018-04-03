var ExceptionPickingLogPanel = function (data) {

    this.source = new CommonSource(data);

    var exportUserLocBtn = Ext.create("Ext.button.Button", {
            text: '导出日志结果',
            scope: this,
            formBind: true,
            handler: function () {
                var batchNo = this.source.txt_batchNo.getValue();
                var locationNo = this.source.txt_locationNo.getValue();
                var retrievalOrderNo = this.source.txt_retrievalOrderNo.getValue();
                var beginDate = this.source.date_beginDate.getSubmitValue();
                var endDate = this.source.date_endDate.getSubmitValue();
                var skuCode = this.source.com_skuCode.getSubmitValue();
                if (skuCode == "null")
                    skuCode = "";

                var exporturl = 'wms/exceptionPickingLog/exportExcel?batchNo=' + batchNo + '&retrievalOrderNo=' + retrievalOrderNo
                    + '&beginDate=' + beginDate + '&locationNo=' + locationNo + '&endDate=' + endDate + '&skuCode=' + skuCode;
                window.location.href = exporturl;
            }
        }
    );

    var impForm = Ext.create("Ext.form.Panel", {
        layout: 'column',
        scope: this,
        items: [
            exportUserLocBtn
        ]
    });

    this.form = Ext.create("Ext.form.Panel", {
        title: '缺货拣选查询',
        collapsible: true,
        scope: this,
        layout: 'column',
        items: [
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.source.txt_batchNo,
                    this.source.txt_locationNo,
                    this.source.date_beginDate
                ]
            },
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.source.txt_retrievalOrderNo,
                    this.source.com_skuCode,
                    this.source.date_endDate
                ]
            }
        ],
        buttons: [
            {
                text: '重置',
                scope: this,
                handler: function () {
                    this.up('form').getForm().reset();
                }
            },
            {
                text: '查询',
                scope: this,
                formBind: true,
                handler: function () {
                    var form = this.form.getForm();
                    if (form.isValid()) {
                        this.store.loadPage(1);
                    }
                }
            }
        ]
    });

    this.store = Ext.create("Ext.data.JsonStore", {
        scope: this,
        fields: ['id', 'warehouseCode', 'locationNo', 'batchNo', 'retrievalOrderNo', 'skuCode', 'skuName', 'needQty', 'pickedQty', 'eom', 'exceptionQty', 'jobUserId', 'createDate'],
        pageSize: 20,
        listeners: {
            scope: this,
            beforeload: function (store) {
                store.proxy.extraParams = this.form.getValues();
            }
        },
        proxy: {
            type: "ajax",
            url: "wms/exceptionPickingLog/search",
            timeout: 120000,
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
            columns: [
                {text: "ID", dataIndex: "id", hidden: true},
                {text: "仓库", dataIndex: "warehouseCode", width: "10%"},
                {text: "货位号", dataIndex: "locationNo", width: "10%"},
                {text: "波次", dataIndex: "batchNo", width: "10%"},
                {text: "出库单", dataIndex: "retrievalOrderNo", width: "12%"},
                {text: "商品代码", dataIndex: "skuCode", width: "10%"},
                {text: "商品名称", dataIndex: "skuName", width: "10%"},
                {text: "订单数量", dataIndex: "needQty", width: "8%"},
                {text: "单位", dataIndex: "eom", width: "5%"},
                {text: "拣选数量", dataIndex: "pickedQty", width: "8%"},
                {text: "缺货拣选数量", dataIndex: "exceptionQty", width: "10%"},
                {text: "作业人", dataIndex: "jobUserId", width: "10%"},
                {text: "创建时间", dataIndex: "createDate", width: "12%"}
            ],
            selType: 'cellmodel',
            viewConfig: {
                enableTextSelection: true
            },
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
            tooltip: '缺货拣选查询'
        },
        overflowY: 'auto',
        items: [
            this.form, this.grid
        ]
    });


};