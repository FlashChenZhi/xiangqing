var SortingResultPanel = function (data) {

    this.com = new CommonSource(data);
    this.cmp = new SortingResultSource();

    this.form = Ext.create("Ext.form.Panel", {
        title: '分拣实绩',
        collapsible: true,
        scope: this,
        layout: 'column',
        items: [
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.com.txt_batchNo,
                    this.cmp.com_status,
                    this.cmp.txt_beginCreateDate,
                    this.cmp.com_errCode
                ]
            },
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.cmp.txt_logictictsBarcode,
                    this.cmp.com_errStatus,
                    this.cmp.txt_endCreateDate
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
        fields: ['id','barCode','target','actualArget','status','errStatus','errCode','errMsg', 'orderNo', 'batchNo', 'createDate', 'planTarget', 'recv010Date', 'reply020Date', 'recv030Date', 'cacheErrorMsg'],
        pageSize: 20,
        listeners: {
            scope: this,
            beforeload: function (store) {
                store.proxy.extraParams = this.form.getValues();
            }
        },
        proxy: {
            type: "ajax",
            url: "wms/sortingResult/search",
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

    var statusMap = this.cmp.statusMap();
    var errStatusMap = this.cmp.errMap();
    var errorCodeMap = this.cmp.errorCodeMap();
    this.grid = Ext.create("Ext.grid.Panel", {
            store: this.store,
            scope: this,
            viewConfig: {
                enableTextSelection: true
            },
            columns: [
                {text: "ID", dataIndex: "id", hidden: true},
                {text: "物流码", width: "10%", dataIndex: "barCode"},
                {text: "WMS回复目的地",width:"10%",dataIndex:"planTarget"},
                {text: "目的地",width:"10%",dataIndex:"target"},
                {text: "实际目的地",width:"10%",dataIndex:"actualArget"},
                {text: "状态",width:"10%",dataIndex:"status" ,renderer: function (value) {
                    return statusMap.get(value)
                }},
                {text: "异常状态",width:"10%",dataIndex:"errStatus", renderer: function (value) {
                    return errStatusMap.get(value)
                }},
                {text: "异常简称",width:"12%",dataIndex:"errCode", renderer: function (value) {
                    return errorCodeMap.get(value)
                }},
                {text: "异常原因",width:"15%",dataIndex:"errMsg"},
                {text: "收到010",width:"15%",dataIndex:"recv010Date"},
                {text: "回复020",width:"15%",dataIndex:"reply020Date"},
                {text: "收到030",width:"15%",dataIndex:"recv030Date"},
                {text: "预分拣异常",width:"15%",dataIndex:"cacheErrorMsg"},
                {text: "出库单号", width: "12%", dataIndex: "orderNo"},
                {text: "波次号", width: "12%", dataIndex: "batchNo"},
                {text: "创建时间", width: "15%", dataIndex: "createDate"}
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
            tooltip: '分拣实绩'
        },
        overflowY: 'auto',
        items: [
            this.form, this.grid
        ]
    });
};
