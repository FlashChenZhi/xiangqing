var SacPrintLogSearchPanel = function (data) {

    this.cmp = new SacPrintLogSearchSource();

    this.form = Ext.create("Ext.form.Panel", {
        title: 'SAC打印日志查询',
        collapsible: true,
        scope: this,
        layout: 'column',
        items: [
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.cmp.com_sacName,
                    this.cmp.com_status,
                    this.cmp.txt_barcode,
                    this.cmp.com_pushRedisSuccess,
                    this.cmp.txt_allocateCount,
                    this.cmp.date_beginDate,
                    this.cmp.com_printSuccess
                ]
            },
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.cmp.com_targetQueueName,
                    this.cmp.txt_logisticsBarcode,
                    this.cmp.txt_shortErrorMsg,
                    this.cmp.com_updateProductDateFlag,
                    this.cmp.txt_groupNo,
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
        fields: ['id', 'sacName', 'locationId', 'targetQueueName', 'status', 'barcode', 'productDate','logisticsBarcode','printerMachine','passMachine','printData','trackSeqNo','dummy','errorMsg','createDate','groupNo','recv510Date','reply520Date','recv530Date','allocateCount','updateProductDateFlag','shortErrorMsg','pushRedisSuccess','printSuccess'],
        pageSize: 20,
        listeners: {
            scope: this,
            beforeload: function (store) {
                store.proxy.extraParams = this.form.getValues();
            }
        },
        proxy: {
            type: "ajax",
            url: "wms/sacPrintLogSearch/list",
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
    var dummpMap = this.cmp.dummpMap();
    var updateProductDateFlagMap = this.cmp.updateProductDateFlagMap();
    var pushRedisSuccessMap = this.cmp.pushRedisSuccessMap();
    var printSuccessMap = this.cmp.printSuccessMap();
    this.grid = Ext.create("Ext.grid.Panel", {
            store: this.store,
            scope: this,
            columns: [
                {text: "ID", dataIndex: "id", hidden: true},
                {text: "SAC名称", dataIndex: "sacName"},
                {text: "BCR地址", dataIndex: "locationId"},
                {text: "目标队列", dataIndex: "targetQueueName"},
                {
                    text: "打印状态", dataIndex: "status", renderer: function (value) {
                    return statusMap.get(value);
                }
                },
                {text: "条码", dataIndex: "barcode"},
                {text: "批次", dataIndex: "productDate"},
                {text: "物流码", dataIndex: "logisticsBarcode"},
                {text: "打印贴标机", dataIndex: "printerMachine"},
                {text: "PASS贴标机", dataIndex: "passMachine"},
                {text: "打印数据", dataIndex: "printData"},
                {text: "异常简称", dataIndex: "shortErrorMsg"},
                {text: "异常备注", dataIndex: "errorMsg"},
                {text: "收到510", dataIndex: "recv510Date"},
                {text: "回复520", dataIndex: "reply520Date"},
                {text: "收到530", dataIndex: "recv530Date"},
                {text: "分配次数", dataIndex: "allocateCount"},
                {
                    text: "是否更新批次", dataIndex: "updateProductDateFlag", renderer: function (value) {
                    return updateProductDateFlagMap.get(value);
                }
                },
                {
                    text: "推送REDIS", dataIndex: "pushRedisSuccess", renderer: function (value) {
                    return pushRedisSuccessMap.get(value);
                }
                },
                {
                    text: "打印结果", dataIndex: "printSuccess", renderer: function (value) {
                    return printSuccessMap.get(value);
                }
                },
                {text: "创建时间", dataIndex: "createDate"},
                {text: "打印组号", dataIndex: "groupNo"},
                {text: "轨迹序号", dataIndex: "trackSeqNo"},
                {
                    text: "是否DUMMY打印", dataIndex: "dummy", renderer: function (value) {
                    return dummpMap.get(value)
                }
                },
            ],
            listeners: {
            },
            selType: 'cellmodel',
            viewConfig: {
                enableTextSelection: true
            },
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
            tooltip: 'SAC打印日志查询'
        },
        overflowY: 'auto',
        items: [
            this.form, this.grid
        ]
    });
};
