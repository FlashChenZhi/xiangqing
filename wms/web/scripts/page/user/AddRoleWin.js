var AddRoleWin = function () {
    this.cmp = new RoleMasterSource();
    this.parentStore = null;

    this.cmp.txt_name.allowBlank = false;
    this.cmp.com_status.allowBlank = false;

    this.form = Ext.create("Ext.form.Panel", {
        defaults: {anchor: "100%", allowBlank: false},
        items: [
            this.cmp.txt_name,
            this.cmp.com_status
        ],
        scope: this
    });

    this.menuStore = Ext.create("Ext.data.JsonStore", {
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

    this.sm = Ext.create("Ext.selection.CheckboxModel", {
        checkOnly: true
    });

    var typeMap = this.cmp.typeMap();
    var distMap = this.cmp.distMap();
    this.grid = Ext.create("Ext.grid.Panel", {
        selModel: this.sm,
        store: this.menuStore,
        scope: this,
        width: 800,
        maxHeight: 300,
        columns: [
            {text: "ID", dataIndex: "id", hidden: true},
            {text: "类型", dataIndex: "type", width: "20%", scope: this, renderer: function (value) {
                return typeMap.get(value);
            }},
            {
                text: "区分", dataIndex: "dist", width: "20%", scope: this, renderer: function (value) {
                return distMap.get(value);
            }
            },
            {text: "代码", dataIndex: "code", width: "20%"},
            {text: "名称", dataIndex: "name", width: "40%"}
        ],
        viewConfig: {
            enableTextSelection: true
        }
    });


    this.win = Ext.create("Ext.window.Window", {
        title: '添加角色',
        width: 800,
        maxHeight: 500,
        bodyPadding: 5,
        resizable: false,
        modal: true,
        closeAction: "hide",
        scope: this,
        items: [this.form, this.grid],
        listeners: {
            scope: this,
            'beforehide': function () {
                this.menuStore.removeAll();
                this.form.getForm().reset();
            }
        },
        buttons: [
            {
                text: '取消',
                scope: this,
                handler: function () {
                    this.menuStore.removeAll();
                    this.form.getForm().reset();
                    this.win.hide();
                }
            },
            {
                text: '创建',
                scope: this,
                formBind: true,
                handler: function () {
                    var form = this.form.getForm();
                    if (form.isValid()) {
                        Ext.Msg.show({
                            title: '确认',
                            message: '确认创建？',
                            buttons: Ext.Msg.YESNO,
                            icon: Ext.Msg.QUESTION,
                            scope: this,
                            fn: function (btn) {
                                if (btn === 'yes') {

                                    var name = this.cmp.txt_name.getValue();
                                    var status = this.cmp.com_status.getValue();

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
                                        url: encodeURI(encodeURI('wms/role/create?name=' + name + '&status=' + status)),
                                        headers: {'Content-Type': "application/json; charset=utf-8"},
                                        method: 'POST',
                                        params: Ext.util.JSON.encode(results),
                                        scope: this,
                                        success: function (response, options) {
                                            mask.hide();
                                            var result = Ext.util.JSON.decode(response.responseText);
                                            if (result.success) {
                                                Ext.success(result.msg);
                                                this.win.hide();
                                                if (this.parentStore != null)
                                                    this.parentStore.loadPage(1);
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
                }
            }
        ]
    });

    this.init = function (parentStore) {
        this.parentStore = parentStore;
        this.menuStore.load();

        this.cmp.txt_name.fieldLabel = '<font style="color: red;">*</font>' + this.cmp.txt_name.fieldLabel;
        this.cmp.com_status.fieldLabel = '<font style="color: red;">*</font>' + this.cmp.com_status.fieldLabel;
    };

    var mask = new Ext.LoadMask({
        msg: '系统处理中...',
        target: this.win
    });
};