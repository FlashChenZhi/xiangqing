var AddMenuWin = function (data) {
    this.source = new CommonSource(data);
    this.cmp = new MenuMasterSource();
    this.parentStore = null;

    this.cmp.com_dist.allowBlank = false;
    this.cmp.com_type.allowBlank = false;
    this.cmp.txt_code.allowBlank = false;
    this.cmp.txt_name.allowBlank = false;
    this.cmp.txt_seq.allowBlank = false;
    this.cmp.com_status.allowBlank = false;

    this.form = Ext.create("Ext.form.Panel", {
        defaults: {anchor: "100%", allowBlank: false},
        items: [
            this.cmp.com_dist,
            this.cmp.com_type,
            this.cmp.txt_code,
            this.cmp.txt_name,
            this.source.com_firstMenuId,
            this.cmp.txt_seq,
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

                                    var dist = this.cmp.com_dist.getValue();
                                    var type = this.cmp.com_type.getValue();
                                    var code = this.cmp.txt_code.getValue();
                                    var name = this.cmp.txt_name.getValue();
                                    var parentId = this.source.com_firstMenuId.getValue();
                                    var seq = this.cmp.txt_seq.getValue();
                                    var status = this.cmp.com_status.getValue();

                                    mask.show();
                                    Ext.Ajax.request({
                                        url: 'wms/menu/create',
                                        method: 'POST',
                                        params: {
                                            dist: dist,
                                            type: type,
                                            code: code,
                                            name: name,
                                            parentId: parentId,
                                            seq: seq,
                                            status: status
                                        },
                                        scope: this,
                                        success: function (response, options) {
                                            mask.hide();
                                            var result = Ext.util.JSON.decode(response.responseText);
                                            if (result.success) {
                                                Ext.success(result.msg);
                                                this.win.hide();
                                                if (this.parentStore != null)
                                                    this.parentStore.load();
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

    this.cmp.com_type.on('select', function (thiz, value) {
        var selectValue = value.data.value;
        console.log(selectValue);
        if ('1' == selectValue) {
            this.source.com_firstMenuId.clearValue();
            this.source.com_firstMenuId.disabled = true;
            this.source.com_firstMenuId.allowBlank = true;
            console.log('一级菜单');
        } else {
            this.source.com_firstMenuId.disabled = false;
            this.source.com_firstMenuId.allowBlank = false;
            console.log('二级菜单');
        }
    }, this);

    this.win = Ext.create("Ext.window.Window", {
        title: '添加菜单',
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

        this.cmp.com_dist.fieldLabel = '<font style="color: red;">*</font>' + this.cmp.com_dist.fieldLabel;
        this.cmp.com_type.fieldLabel = '<font style="color: red;">*</font>' + this.cmp.com_type.fieldLabel;
        this.cmp.txt_code.fieldLabel = '<font style="color: red;">*</font>' + this.cmp.txt_code.fieldLabel;
        this.cmp.txt_name.fieldLabel = '<font style="color: red;">*</font>' + this.cmp.txt_name.fieldLabel;
        this.cmp.txt_seq.fieldLabel = '<font style="color: red;">*</font>' + this.cmp.txt_seq.fieldLabel;
        this.cmp.com_status.fieldLabel = '<font style="color: red;">*</font>' + this.cmp.com_status.fieldLabel;

    };

    var mask = new Ext.LoadMask({
        msg: '系统处理中...',
        target: this.win
    });

};