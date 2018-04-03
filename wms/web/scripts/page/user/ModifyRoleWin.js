var ModifyRoleWin = function () {
    this.cmp = new RoleMasterSource();
    this.parentStore = null;

    this.cmp.txt_id.allowBlank = false;
    this.cmp.txt_name.allowBlank = false;
    this.cmp.com_status.allowBlank = false;

    this.form = Ext.create("Ext.form.Panel", {
        defaults: {anchor: "100%", allowBlank: false},
        items: [
            this.cmp.txt_id,
            this.cmp.txt_name,
            this.cmp.com_status
        ],
        scope: this
    });

    this.win = Ext.create("Ext.window.Window", {
        title: '修改角色',
        width: 800,
        maxHeight: 500,
        bodyPadding: 5,
        resizable: false,
        modal: true,
        closeAction: "hide",
        scope: this,
        items: [this.form],
        listeners: {
            scope: this,
            'beforehide': function () {
                this.form.getForm().reset();
            }
        },
        buttons: [
            {
                text: '取消',
                scope: this,
                handler: function () {
                    this.form.getForm().reset();
                    this.win.hide();
                }
            },
            {
                text: '修改',
                scope: this,
                formBind: true,
                handler: function () {
                    var form = this.form.getForm();
                    if (form.isValid()) {
                        Ext.Msg.show({
                            title: '确认',
                            message: '确认修改？',
                            buttons: Ext.Msg.YESNO,
                            icon: Ext.Msg.QUESTION,
                            scope: this,
                            fn: function (btn) {
                                if (btn === 'yes') {
                                    var id = this.cmp.txt_id.getValue();
                                    var name = this.cmp.txt_name.getValue();
                                    var status = this.cmp.com_status.getValue();
                                    mask.show();
                                    Ext.Ajax.request({
                                        url: encodeURI(encodeURI('wms/role/update?id=' + id + '&name=' + name + '&status=' + status)),
                                        headers: {'Content-Type': "application/json; charset=utf-8"},
                                        method: 'POST',
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

    this.init = function (rowData, parentStore) {
        this.parentStore = parentStore;

        this.cmp.txt_id.disabled = true;
        this.cmp.txt_id.hide();

        this.cmp.txt_id.setValue(rowData.id);
        this.cmp.txt_name.setValue(rowData.name);
        this.cmp.com_status.setValue(rowData.status);

        this.cmp.txt_name.fieldLabel = '<font style="color: red;">*</font>' + this.cmp.txt_name.fieldLabel;
        this.cmp.com_status.fieldLabel = '<font style="color: red;">*</font>' + this.cmp.com_status.fieldLabel;
    };

    var mask = new Ext.LoadMask({
        msg: '系统处理中...',
        target: this.win
    });

};