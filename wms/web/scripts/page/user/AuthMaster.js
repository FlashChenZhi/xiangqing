var AuthMaster = function (data) {

    this.cmp = new RoleMasterSource();
    this.source = new CommonSource(data);

    this.form = Ext.create("Ext.form.Panel", {
        title: '权限设定',
        collapsible: true,
        scope: this,
        layout: 'column',
        items: [
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.source.com_roleId
                ]
            }
        ]
    });

    this.store = Ext.create("Ext.data.JsonStore", {
        fields: ['id', 'dist', 'type', 'code', 'name'],
        scope: this,
        listeners: {
            scope: this,
            beforeload: function (store) {
                store.proxy.extraParams = {status: '1'};
            }
        },
        proxy: {
            type: "ajax",
            url: "wms/menu/list",
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

    this.flushButton = Ext.create("Ext.button.Button", {
        text: "刷新",
        scope: this,
        handler: function () {
            this.store.load();
            this.resetGrid();
        }
    });

    this.settingButton = Ext.create("Ext.button.Button", {
        text: "设定权限",
        scope: this,
        handler: function () {
            Ext.Msg.show({
                title: '确认',
                message: '确认设定权限？',
                buttons: Ext.Msg.YESNO,
                icon: Ext.Msg.QUESTION,
                scope: this,
                fn: function (btn) {
                    if (btn === 'yes') {
                        var roleId = this.source.com_roleId.getValue();
                        if (roleId == null || roleId == '') {
                            Ext.warn('请选择角色');
                            return;
                        }

                        var results = [];
                        var sels = this.grid.getSelection();
                        for (var res in sels) {
                            results.push({menuId: sels[res].data.id});
                        }
                        if (sels.length == 0) {
                            Ext.warn('请至少选择一条数据');
                            return;
                        }

                        mask.show();
                        Ext.Ajax.request({
                            url: 'wms/role/authSetting?id=' + roleId,
                            headers: {'Content-Type': "application/json; charset=utf-8"},
                            method: 'POST',
                            params: Ext.util.JSON.encode(results),
                            scope: this,
                            success: function (response, options) {
                                mask.hide();
                                var result = Ext.util.JSON.decode(response.responseText);
                                if (result.success) {
                                    Ext.success(result.msg);
                                    this.resetGrid();
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
                }
            });
        }
    });

    this.sm = Ext.create("Ext.selection.CheckboxModel", {
        checkOnly: true
    });

    var typeMap = this.cmp.typeMap();
    var distMap = this.cmp.distMap();
    this.grid = Ext.create("Ext.grid.Panel", {
        selModel: this.sm,
        store: this.store,
        scope: this,
        columns: [
            {text: "id", dataIndex: "id", hidden: true},
            {
                text: "类型", dataIndex: "type", width: "20%", scope: this, renderer: function (value) {
                return typeMap.get(value);
            }
            },
            {
                text: "区分", dataIndex: "dist", width: "20%", scope: this, renderer: function (value) {
                return distMap.get(value);
            }
            },
            {text: "代码", dataIndex: "code", width: "20%"},
            {text: "名称", dataIndex: "name", width: "40%"}
        ],
        selType: 'cellmodel',
        viewConfig: {
            enableTextSelection: true
        },
        dockedItems: [
            {
                xtype: "toolbar",
                dock: "top",
                scope: this,
                items: [
                    this.flushButton, this.settingButton
                ]
            }
        ]
    });

    this.source.com_roleId.on('select', function (thiz, value) {
        var selectValue = value.data.value;

        Ext.Ajax.request({
            url: 'wms/role/getMenus',
            method: 'POST',
            params: {roleId: selectValue},
            scope: this,
            success: function (response, options) {
                this.resetGrid();
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.success) {
                    var res = result.res;
                    for (var r in res) {
                        var id = res[r].menuId;
                        this.sm.select(this.store.findExact('id', id), true);
                        console.log("menuId="+ id);
                    }
                } else {
                    Ext.error(result.msg);
                }
            },
            failure: function (response, options) {
                var result = Ext.util.JSON.decode(response.responseText);
                Ext.error(result.msg);
            }
        });

    }, this);

    this.store.load();

    this.panel = Ext.create("Ext.panel.Panel", {
        tabConfig: {
            tooltip: '权限设定'
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


    this.resetGrid = function () {
        console.log('reset grid...');
        this.sm.deselectAll();
    }

};