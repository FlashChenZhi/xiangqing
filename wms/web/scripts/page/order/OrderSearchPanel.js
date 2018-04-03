var OrderSearchPanel = function (data) {

    this.source = new CommonSource(data);

    var impForm = Ext.create("Ext.form.Panel", {
        layout: 'column',
        scope: this,
        items: [
            // exportExcelBtn
        ]
    });


    this.orderPanel = Ext.create("Ext.form.Panel", {
        title: '按订单内容',
        layout: 'column',
        scope: this,
        collapsible: true,
        items: [
            {
                layout: "form", columnWidth: .5, defaults: {anchor: '100%'},
                items: [
                    this.source.txt_retrievalOrderNo,
                    this.source.date_beginDate,
                    this.source.com_orderstatus
                ]
            },
            {
                layout: 'form', columnWidth: .5,
                items: [
                    this.source.txt_whCode,
                    this.source.date_endDate
                ]
            }
        ]
    });


    this.tabs = Ext.create('Ext.tab.Panel', {
        columnWidth: 1,
        items: [this.orderPanel]
    });

    this.form = Ext.create("Ext.form.Panel", {
        title: '出库订单查询',
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
        fields: ['id', 'whCode', 'orderNo', 'jobType', 'status', 'boxQty', 'coustomName', 'carrierName', 'toLocation', 'area', 'desc'],
        pageSize: 20,
        listeners: {
            scope: this,
            beforeload: function (store) {
                store.proxy.extraParams = this.form.getValues();
            }
        },
        proxy: {
            type: "ajax",
            url: "wms/order/list",
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

    this.orderDetailWin = null;

    var statusMap = new Map();
    statusMap.put('0', '未开始');
    statusMap.put('1', '已开始');
    statusMap.put('9', '已取消');

    this.grid = Ext.create("Ext.grid.Panel", {
            store: this.store,
            scope: this,
            columns: [
                {text: "ID", dataIndex: "id", hidden: true},
                {text: "仓库编码", dataIndex: "whCode", width: "10%"},
                {text: "订单号", dataIndex: "orderNo", width: "15%"},
                {text: "状态", dataIndex: "status", width: "10%", scope: this, renderer: function (value) {
                    return statusMap.get(value);
                }},
                {text: "箱数", dataIndex: "boxQty", width: "10%"},
                {text: "客户名称", dataIndex: "coustomName", width: "15%"},
                {text: "承运商", dataIndex: "carrierName", width: "10%"},
                {text: "目标货位", dataIndex: "toLocation", width: "10%"},
                {text: "区域", dataIndex: "area", width: "10%"},
                {text: "描述", dataIndex: "desc", width: "10%", flex: 1}
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
                                text: "订单明细", handler: function () {
                                var orderId = record.data.id;
                                var orderNo = record.data.orderNo;
                                if (this.orderDetailWin == null) {
                                    this.orderDetailWin = new OrderDetailWin(orderId);
                                } else {
                                    this.orderDetailWin.init(orderId);
                                }
                                this.orderDetailWin.win.title = "订单:" + orderNo + "明细一览";
                                this.orderDetailWin.win.show();
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
            tooltip: '出库订单查询'
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