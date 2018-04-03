var LocationUseRate = function () {

    this.form = Ext.create("Ext.form.Panel", {
        title: '货位使用率',
        collapsible: true,
        scope: this,
        layout: 'column',
        items: this.tabs,
        buttons: [
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
        fields: ['position', 'total', 'used', 'volumn', 'rate'],
        listeners: {
            scope: this,
            beforeload: function (store) {
                store.proxy.extraParams = this.form.getValues();
            }
        },
        proxy: {
            type: "ajax",
            url: "wms/master/location/searchFullRate",
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
                {text: "区域", dataIndex: "position", width: "20%"},
                {text: "总数", dataIndex: "total", width: "20%"},
                {text: "使用数", dataIndex: "used", width: "20%"},
                {text: "体积", dataIndex: "volumn", width: "20%"},
                {text: "使用比例", dataIndex: "rate", flex: 1},
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
            ]
        }
    );


    this.panel = Ext.create("Ext.panel.Panel", {
        tabConfig: {
            tooltip: '货位使用率'
        },
        overflowY: 'auto',
        items: [
            this.form, this.grid
        ]
    });

};