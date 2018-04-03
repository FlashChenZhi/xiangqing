var InventoryLogSearchPanel = function (data) {

    this.cmp = new InventoryLogSource(data);
    this.com = new CommonSource(data);

    this.invLocPanel = Ext.create("Ext.form.Panel", {
        title: '库存日志查询',
        scope: this,
        collapsible: true,
        items: [
            {
                layout: "column", defaults: {columnWidth: 0.5, layout: "form"},
                items: [
                    {items: [this.cmp.txt_fromLocation]},
                    {items: [this.cmp.txt_toLocation]}
                ]
            },
            {
                layout: "column", defaults: {columnWidth: 0.5, layout: "form"},
                items: [
                    {items: [this.com.txt_skuCode]},
                    {items: [this.com.com_jobType]}
                ]
            }, {
                layout: "column", defaults: {columnWidth: 0.5, layout: "form"},
                items: [
                    {items: [this.cmp.txt_beginCreateDate]},
                    {items: [this.cmp.txt_endCreateDate]}
                ]
            }, {
                layout: "column", defaults: {columnWidth: 0.5, layout: "form"},
                items: [
                    {items: [this.com.txt_containerBarcode]},
                    {items: [this.com.txt_retrievalOrderNo]}
                ]
            }
        ]
    });


    var exportExcelBtn = Ext.create("Ext.button.Button", {
            text: "库存日志导出",
            scope: this,
            handler: function () {

                var fromLocation = this.cmp.txt_fromLocation.getValue();
                if (fromLocation == null) {
                    fromLocation = '';
                }

                var toLocation = this.cmp.txt_toLocation.getValue();
                if (toLocation == null) {
                    toLocation = '';
                }

                var skuCode = this.com.txt_skuCode.getValue();
                if (skuCode == null) {
                    skuCode = '';
                }
                var jobType = this.com.com_jobType.getValue();
                if (jobType == null) {
                    jobType = '';
                }

                var beginCreateDate = this.cmp.txt_beginCreateDate.getSubmitValue();
                if (beginCreateDate == null) {
                    beginCreateDate = '';
                }

                var endCreateDate = this.cmp.txt_endCreateDate.getSubmitValue();
                if (endCreateDate == null) {
                    endCreateDate = '';
                }

                var containerBarcode = this.com.txt_containerBarcode.getValue();
                if (containerBarcode == null) {
                    containerBarcode = '';
                }

                var orderNo = this.com.txt_retrievalOrderNo.getValue();
                if (orderNo == null) {
                    orderNo = '';
                }

                var exporturl = 'wms/inventory/exportInventoLog?fromLocation=' + fromLocation + "&toLocation="
                    + toLocation + '&skuCode=' + skuCode + "&jobType=" + jobType + "&beginCreateDate=" + beginCreateDate
                    + '&endCreateDate=' + endCreateDate + '&containerBarcode=' + containerBarcode + '&orderNo=' + orderNo;

                window.location.href = exporturl;

            }

        }
    );

    this.tabs = Ext.create('Ext.tab.Panel', {
        columnWidth: 1,
        items: [this.invLocPanel]
    });

    this.form = Ext.create("Ext.form.Panel", {
        title: '库存日志查询',
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
        fields: ['id', 'skuCode','jobType', 'skuName', 'qty', 'lotNum', 'whCode', 'fromLocation', 'toLocation', 'container', 'orderNo','createDate'],
        pageSize: 20,
        listeners: {
            scope: this,
            beforeload: function (store) {
                store.proxy.extraParams = this.form.getValues();
            }
        },
        proxy: {
            type: "ajax",
            url: "wms/inventory/searchLog",
            timeout: 900000,
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

    var typeMap = new Map();
    typeMap.put("01","入库");
    typeMap.put("02","出库");
    typeMap.put("05","冻结");
    typeMap.put("06","解除冻结");

    this.grid = Ext.create("Ext.grid.Panel", {
            store: this.store,
            scope: this,
            viewConfig: {
                enableTextSelection: true
            },
            columns: [
                {text: "ID", dataIndex: "id", hidden: true},
                {text: "商品代码", width: "10%", dataIndex: "skuCode"},
                {text: "商品名称", width: "15%", dataIndex: "skuName"},
                {text: "数量", width: "10%", dataIndex: "qty"},
                {text: "类型", width: "10%", dataIndex: "jobType", scope: this, renderer: function (value) {
                    return typeMap.get(value);
                }},
                {text: "批次", width: "10%", dataIndex: "lotNum"},
                {text: "仓库", width: "10%", dataIndex: "whCode"},
                {text: "源货位", width: "10%", dataIndex: "fromLocation"},
                {text: "目的货位", width: "10%", dataIndex: "toLocation"},
                {text: "托盘号", width: "10%", dataIndex: "container"},
                {text: "出库单号", width: "10%", dataIndex: "orderNo"},
                {text: "托盘号", width: "20%", dataIndex: "createDate"}

            ],
            selType: 'cellmodel',
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
                    items: [exportExcelBtn]
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
            tooltip: '库存日志查询'
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
