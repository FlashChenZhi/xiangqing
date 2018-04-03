var ModifyPasswordWin = function () {
    this.cmp = new UserMasterSource();

    this.cmp.txt_code.allowBlank = false;
    this.cmp.txt_name.allowBlank = false;
    this.cmp.txt_oldPassword.allowBlank = false;
    this.cmp.txt_newPassword.allowBlank = false;
    this.cmp.txt_confirmPassword.allowBlank = false;
    this.form = Ext.create("Ext.form.Panel", {
        defaults: {anchor: "100%", allowBlank: false},
        items: [
            this.cmp.txt_code,
            this.cmp.txt_name,
            this.cmp.txt_oldPassword,
            this.cmp.txt_newPassword,
            this.cmp.txt_confirmPassword
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
                text: '修改',
                scope: this,
                formBind: true,
                handler: function () {
                    Ext.Msg.show({
                        title: '确认',
                        message: '确认修改？',
                        buttons: Ext.Msg.YESNO,
                        icon: Ext.Msg.QUESTION,
                        scope: this,
                        fn: function (btn) {
                            if (btn === 'yes') {
                                var form = this.form.getForm();
                                if (form.isValid()) {
                                    var code = this.cmp.txt_code.getValue();
                                    var oldPassword = this.cmp.txt_oldPassword.getValue();
                                    var newPassword = this.cmp.txt_newPassword.getValue();
                                    var confirmpassword = this.cmp.txt_confirmPassword.getValue();
                                    if(newPassword != confirmpassword){
                                        Ext.error("两次密码不一致");
                                        return;
                                    }
                                    mask.show();
                                    Ext.Ajax.request({
                                        url: 'wms/auth/modifyPassword',
                                        method: 'POST',
                                        params: {code: code, oldPassword: oldPassword, newPassword: newPassword},
                                        scope: this,
                                        success: function (response, options) {
                                            mask.hide();
                                            var result = Ext.util.JSON.decode(response.responseText);
                                            if (result.success) {
                                                Ext.success(result.msg);
                                                this.win.hide();
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
                        }
                    });
                }
            }
        ]
    });

    this.win = Ext.create("Ext.window.Window", {
        title : '修改密码',
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

    this.init = function (user) {
        this.cmp.txt_code.disabled = true;
        this.cmp.txt_name.disabled = true;

        this.cmp.txt_code.setValue(user.code);
        this.cmp.txt_name.setValue(user.name);
    };

    var mask = new Ext.LoadMask({
        msg: '系统处理中...',
        target: this.win
    });
   
};