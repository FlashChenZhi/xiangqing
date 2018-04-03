var SortingResultCountPanel = function (data) {

    this.com = new CommonSource(data);
    this.cmp = new SortingResultSource();

    this.form = Ext.create("Ext.form.Panel", {
        title: '分拣实绩汇总',
        collapsible: true,
        scope: this,
        items: [

            {
                layout: "column", defaults: {columnWidth: 0.5, layout: "form"},
                items: [
                    {items: [this.com.txt_batchNo]},
                    {items: [this.cmp.com_status]}
                ]
            },
            {
                layout: "column", defaults: {columnWidth: 0.5, layout: "form"},
                items: [
                    {items:[this.cmp.com_errStatus]},
                    {
                        layout: "column", defaults: {columnWidth: 0.5, layout: "form"}, scope: this,
                        items: [
                            {
                                items: [
                                    this.cmp.txt_beginCreateDate
                                ]
                            },
                            {
                                items: [
                                    this.cmp.txt_endCreateDate
                                ]
                            }
                        ]
                    }
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
        fields: ['id', 'target', 'actualArget', 'status', 'errStatus', 'errorCode', 'batchNo', 'qty'],
        pageSize: 20,
        listeners: {
            scope: this,
            beforeload: function (store) {
                store.proxy.extraParams = this.form.getValues();
            }
        },
        proxy: {
            type: "ajax",
            url: "wms/sortingResult/searchCount",
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
                {text: "目的地", width: "10%", dataIndex: "target"},
                {text: "实际目的地", width: "10%", dataIndex: "actualArget"},
                {
                    text: "状态", width: "10%", dataIndex: "status", renderer: function (value) {
                    return statusMap.get(value)
                }
                },
                {
                    text: "异常状态", width: "10%", dataIndex: "errStatus", renderer: function (value) {
                    return errStatusMap.get(value)
                }
                },
                {
                    text: "异常原因", width: "10%", dataIndex: "errorCode", renderer: function (value) {
                    return errorCodeMap.get(value)
                }
                },
                {text: "波次号", width: "10%", dataIndex: "batchNo"},
                {text: "数量", width: "10%", dataIndex: "qty"}
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
            tooltip: '分拣实绩汇总'
        },
        overflowY: 'auto',
        items: [
            this.form, this.grid
        ]
    });
};
