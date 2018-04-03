var AddUserWin = function (data) {
    this.source = new CommonSource(data);
    this.cmp = new UserMasterSource();
    this.parentStore = null;

    this.cmp.txt_code.allowBlank = false;
    this.cmp.txt_name.allowBlank = false;
    this.source.com_roleId.allowBlank = false;
    this.cmp.com_status.allowBlank = false;


    this.form = Ext.create("Ext.form.Panel", {
        defaults: {anchor: "100%", allowBlank: false},
        items: [
            this.cmp.txt_code,
            this.cmp.txt_name,
            this.source.com_roleId,
            this.cmp.com_status
        ],
        scope: this,
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
                                    var code = this.cmp.txt_code.getValue();
                                    var name = this.cmp.txt_name.getValue();
                                    var roleId = this.source.com_roleId.getValue();
                                    var status = this.cmp.com_status.getValue();

                                    mask.show();
                                    Ext.Ajax.request({
                                        url: 'wms/user/create',
                                        method: 'POST',
                                        params: {code: code, name: name, roleId: roleId, status: status},
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

    this.win = Ext.create("Ext.window.Window", {
        title: '新增用户',
        width: 500,
        bodyPadding: 5,
        resizable: false,
        modal: true,
        closeAction: "hide",
        scope: this,
        items: this.form,
        listeners: {
            scope: this,
            'beforehide': function () {
                this.form.getForm().reset();
            }
        }
    });

    this.init = function (parentStore) {
        this.parentStore = parentStore;

        this.cmp.txt_code.fieldLabel = '<font style="color: red;">*</font>' + this.cmp.txt_code.fieldLabel;
        this.cmp.txt_name.fieldLabel = '<font style="color: red;">*</font>' + this.cmp.txt_name.fieldLabel;
        this.source.com_roleId.fieldLabel = '<font style="color: red;">*</font>' + this.source.com_roleId.fieldLabel;
        this.cmp.com_status.fieldLabel = '<font style="color: red;">*</font>' + this.cmp.com_status.fieldLabel;
    };

    var mask = new Ext.LoadMask({
        msg: '系统处理中...',
        target: this.win
    });

};