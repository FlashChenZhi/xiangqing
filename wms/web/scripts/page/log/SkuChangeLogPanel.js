var SkuChangeLogPanel = function (data) {

    this.cmp = new CommonSource(data);
    this.source = new SkuChangeLogSource();

    var exportExcelBtn = Ext.create("Ext.button.Button", {
            text: "导出",
            scope: this,
            handler: function () {
                var skuCode = this.cmp.com_skuCode.getValue();
                if (skuCode == null)
                    skuCode = '';
                var logType = this.source.com_changeType.getSubmitValue();
                if (logType == null)
                    logType = '';
                var type = this.source.com_type.getSubmitValue();
                if (type == null)
                    type = '';
                var recvStatus = this.source.com_recvStatus.getSubmitValue();
                if (recvStatus == null)
                    recvStatus = '';
                var beginDate = this.cmp.date_beginDate.getSubmitValue();
                if (beginDate == null)
                    beginDate = '';
                var endDate = this.cmp.date_endDate.getSubmitValue();
                if (endDate == null)
                    endDate = '';
                var exporturl = 'wms/skuChangeLog/exportExcel?skuCode=' + skuCode + '&logType=' + logType +
                    '&type=' + type + '&recvStatus=' + recvStatus + '&beginDate=' + beginDate + '&endDate=' + endDate;
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

    this.form = Ext.create("Ext.form.Panel", {
        title: 'SKU变更日志',
        collapsible: true,
        scope: this,
        layout: 'column',
        items: [
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.cmp.com_skuCode,
                    this.source.com_changeType,
                    this.cmp.date_beginDate,
                ]
            },
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.source.com_type,
                    this.source.com_recvStatus,
                    this.cmp.date_endDate
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
        fields: ['skuCode', 'splitFlag', 'skuName', 'type', 'logType', 'beforeItem', 'afterItem', 'createDate', 'recvStatus', 'rejectReason', 'createUserId'],
        pageSize: 20,
        listeners: {
            scope: this,
            beforeload: function (store) {
                store.proxy.extraParams = this.form.getValues();
            }
        },
        proxy: {
            type: "ajax",
            url: "wms/skuChangeLog/search",
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

    var typeMap = this.source.typeMap();
    var recvStatusMap = this.source.recvStatusMap();
    var changeMap = this.source.changeTypeMap();
    var splitMap = new Map();
    splitMap.put(true, "散零");
    splitMap.put(false, "整箱");
    this.grid = Ext.create("Ext.grid.Panel", {
            store: this.store,
            scope: this,
            viewConfig: {
                enableTextSelection: true
            },
            columns: [
                {text: "商品代码", dataIndex: "skuCode", width: "15%"},
                {text: "商品名称", dataIndex: "skuName", width: "17%"},
                {
                    text: "是否散零", dataIndex: "splitFlag", renderer: function (value) {
                    return splitMap.get(value);
                }, width: "10%"
                },
                {
                    text: "记录类型", dataIndex: "logType", renderer: function (value) {
                    return changeMap.get(value);
                }, width: "10%"
                },
                {
                    text: "变更类型", dataIndex: "type", renderer: function (value) {
                    return typeMap.get(value);
                }, width: "10%"
                },
                {
                    text: "下发状态", dataIndex: "recvStatus", renderer: function (value) {
                    return recvStatusMap.get(value);
                }, width: "10%"
                },
                {text: "拒绝原因", dataIndex: "rejectReason", width: "15%"},
                {text: "变更前值", dataIndex: "beforeItem", width: "15%"},
                {text: "变更后值", dataIndex: "afterItem", width: "15%"},
                {text: "创建日期", dataIndex: "createDate", width: "15%"},
                {text: "创建人", dataIndex: "createUserId", width: "15%"}
            ],
            selType: 'cellmodel',
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
            tooltip: 'SKU变更日志'
        },
        overflowY: 'auto',
        items: [
            this.form, this.grid
        ]
    });

};
