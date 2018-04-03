var OrderDetailWin = function (id) {
    var orderId = id;
    var keyword = null;

    this.cmp = new OrderSearchSource();

    this.init = function (id) {
        orderId = id;
        this.orderDetailStore.load();
    };

    this.orderDetailStore = Ext.create("Ext.data.JsonStore", {
        fields: ['id', 'itemCode', 'batch', 'palletNo', 'qty'],
        scope: this,
        listeners: {
            scope: this,
            beforeload: function (store) {
                store.proxy.extraParams = {'orderId': orderId, 'keyword' : keyword};
            }
        },
        proxy: {
            type: "ajax",
            url: "wms/order/searchDetail",
            actionMethods: {read: "POST"},
            reader: {
                type: "json",
                rootProperty: "res"
            },
            listeners: {
                exception: function (thiz, request) {
                    extStoreLoadFail(thiz, request);
                }
            }
        }
    });

    this.orderDetailStore.load();



    var retrievalSeqMap = this.cmp.retrievalSeqMap();
    this.grid = Ext.create("Ext.grid.Panel", {
        store: this.orderDetailStore,
        scope: this,
        width: 1024,
        maxHeight: 500,
        features: [
            {
                ftype: 'summary'
            }
        ],
        tbar: [this.cmp.txt_keyword, {
            text: '搜索',
            scope: this,
            handler: function () {
                keyword = this.cmp.txt_keyword.getValue();
                this.orderDetailStore.load();
            }
        }
        ],
        columns: [
            {text: "行号", dataIndex: "id", width: "10%"},
            {text: "商品代码", dataIndex: "itemCode", width: "30%"},
            {text: "批次", dataIndex: "batch", width: "20%"},
            {text: "托盘号", dataIndex: "palletNo", width: "30%"},
            {
                text: "数量", dataIndex: "qty", scope: this, summaryType: 'sum', width: "6%",
                summaryRenderer: function (value, summaryData, dataIndex) {
                    return Ext.String.format('{0}{1}', '总数：', value);
                },
            }
        ],
        viewConfig: {
            enableTextSelection: true,
            getRowClass: function (record, rowIndex, rowParams, store) {
                if (record.data.zt == 0) {
                    return 'x-grid-record-red';
                } else {
                    return '';
                }
            }
        }
    });

    this.cmp.txt_keyword.on('specialKey', function (field, e) {
        if (e.getKey() == e.ENTER) {
            keyword = this.cmp.txt_keyword.getValue();
            this.orderDetailStore.load();
        }
    }, this);


    this.win = Ext.create("Ext.window.Window", {
        bodyPadding: 5,
        resizable: false,
        modal: true,
        closeAction: "hide",
        scope: this,
        items: this.grid,
        y:40,
        buttons: [
            {
                text: '关闭',
                scope: this,
                handler: function () {
                    this.orderDetailStore.removeAll();
                    this.win.hide();
                }
            }
        ]
    });

    function getCellStyle(planQty, currentQty) {
        if (planQty == currentQty) {
            return '<span style="color:green;">' + currentQty + '</span>';
        } else {
            return '<span style="color:red;">' + currentQty + '</span>';
        }
    }
};