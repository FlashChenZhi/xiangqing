var StatisticsPanel = function (data) {

    this.cmp = new CommonSource(data);

    this.cmp.date_beginDate.allowBank = false;
    this.cmp.date_endDate.allowBank = false;

    this.form = Ext.create("Ext.form.Panel", {
        title: '出入库统计',
        collapsible: true,
        scope: this,
        layout: 'column',
        items: [
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.cmp.date_beginDate,
                    this.cmp.com_jobType
                ]
            },
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
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
                formBind: true,
                disabled: true,
                handler: function () {
                    var form = this.form.getForm();
                    if (form.isValid()) {
                        if(this.cmp.date_beginDate.getSubmitValue()==null || this.cmp.date_beginDate.getSubmitValue() == ''){
                            Ext.error('请输入起始日期');
                            return;
                        }
                        if(this.cmp.date_endDate.getSubmitValue() == null || this.cmp.date_endDate.getSubmitValue() == ''){
                            Ext.error('请输入截止日期');
                            return;
                        }
                        this.store.load();
                    }
                }
            }
        ]
    });

    this.store = Ext.create("Ext.data.JsonStore", {
        scope: this,
        fields: ['type', 'qty', 'volumn'],
        pageSize: 20,
        listeners: {
            scope: this,
            beforeload: function (store) {
                store.proxy.extraParams = this.form.getValues();
            }
        },
        proxy: {
            type: "ajax",
            url: "wms/asrs/statistic",
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
            viewConfig: {
                enableTextSelection: true
            },
            columns: [
                {text: "类型", dataIndex: "type", width: "20%"},
                {text: "托盘数量", dataIndex: "qty", width: "30%"},
                {text: "体积", dataIndex: "volumn", flex: 1}
            ],
            selType: 'cellmodel',
            dockedItems: [
                {
                    xtype: "toolbar",
                    dock: "top",
                    scope: this,
                    items: []
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
            tooltip: '出入库统计'
        },
        overflowY: 'auto',
        items: [
            this.form, this.grid
        ]
    });
};
