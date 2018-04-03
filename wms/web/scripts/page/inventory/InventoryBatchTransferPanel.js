var InventoryBatchTransferPanel = function (data) {

    this.source = new CommonSource(data);

    this.locQcPanel = Ext.create("Ext.form.Panel", {
        title: '库存地点条件',
        layout: 'column',
        scope: this,
        collapsible: true,
        items: [
            {
                layout: "form", columnWidth: .5, defaults: {anchor: '100%'},
                items: [
                    this.source.txt_position,


                ]
            }
        ]
    });

    this.tabs = Ext.create('Ext.tab.Panel', {
        columnWidth: 1,
        items: [this.locQcPanel]
    });

    this.form = Ext.create("Ext.form.Panel", {
        title: '库存查询',
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
        fields: ['id', 'position', 'area', 'bay', 'level'],
        listeners: {
            scope: this,
            beforeload: function (store) {
                store.proxy.extraParams = this.form.getValues();
            }
        },
        proxy: {
            type: "ajax",
            url: "wms/inventory/searchTransferSuggest",
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


    this.sm = Ext.create("Ext.selection.CheckboxModel", {
        checkOnly: true
    });

    this.grid = Ext.create("Ext.grid.Panel", {
            store: this.store,
            scope: this,
            selModel: this.sm,
            viewConfig: {
                enableTextSelection: true
            },
            columns: [
                {text: "ID", dataIndex: "id", width: "5%"},
                {text: "区域", dataIndex: "position", width: "20%"},
                {text: "层", dataIndex: "level", width: "10%"},
                {text: "列", dataIndex: "bay", width: "15%"},
                {text: "具体边", dataIndex: "area", flex: 1},
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
                                text: "调整库存批次",
                                handler: function () {
                                    Ext.Msg.show({
                                        title: '确认',
                                        message: '根据批次移动托盘？',
                                        buttons: Ext.Msg.YESNO,
                                        icon: Ext.Msg.QUESTION,
                                        scope: this,
                                        fn: function (btn) {
                                            if (btn === 'yes') {
                                                var position = record.data.position;
                                                var level = record.data.level;
                                                var bay = record.data.bay;
                                                var area = record.data.area;
                                                mask.show();
                                                Ext.Ajax.request({
                                                    url: 'wms/inventory/batchTransfer',
                                                    method: 'POST',
                                                    params: {
                                                        'position': position,
                                                        'level': level,
                                                        'bay': bay,
                                                        'area': area
                                                    },
                                                    scope: this,
                                                    success: function (response, options) {
                                                        mask.hide();
                                                        var result = Ext.util.JSON.decode(response.responseText);
                                                        if (result.success) {
                                                            Ext.success(result.msg);
                                                            this.store.loadPage(1);
                                                        } else {
                                                            Ext.error(result.msg);
                                                        }
                                                    },
                                                    failure: function (response, options) {
                                                        mask.hide();
                                                        extAjaxFail(response, options);
                                                    }
                                                });
                                            }
                                            else {
                                                //todo cancel
                                            }
                                        }
                                    });
                                }
                            },
                        ]
                    });
                    menu.showAt(e.getPoint());
                }
            },
            selType: 'cellmodel',
            viewConfig: {
                enableTextSelection: true
            },
            features: [
                {
                    ftype: 'summary'
                }
            ],

        }
    );


    this.panel = Ext.create("Ext.panel.Panel", {
        tabConfig: {
            tooltip: '批量库内移动'
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