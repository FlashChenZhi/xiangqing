var ModifyMenuWin = function (data) {
    this.source = new CommonSource(data);
    this.cmp = new MenuMasterSource();
    this.parentStore = null;

    this.cmp.txt_id.allowBlank = false;
    this.cmp.com_dist.allowBlank = false;
    this.cmp.com_type.allowBlank = false;
    this.cmp.txt_name.allowBlank = false;
    this.cmp.txt_seq.allowBlank = false;
    this.cmp.com_status.allowBlank = false;

    this.form = Ext.create("Ext.form.Panel", {
        defaults: {anchor: "100%", allowBlank: false},
        items: [
            this.cmp.txt_id,
            this.cmp.com_dist,
            this.cmp.com_type,
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
                text: '修改',
                scope: this,
                formBind: true,
                disabled: true,
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
                                    var type = this.cmp.com_type.getValue();
                                    var dist = this.cmp.com_dist.getValue();
                                    var name = this.cmp.txt_name.getValue();
                                    var parentId = this.source.com_firstMenuId.getValue();
                                    var seq = this.cmp.txt_seq.getValue();
                                    var status = this.cmp.com_status.getValue();

                                    mask.show();
                                    Ext.Ajax.request({
                                        url: 'wms/menu/update',
                                        method: 'POST',
                                        params: {
                                            id: id,
                                            type: type,
                                            dist: dist,
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
                                                //if (this.parentStore != null)
                                                //    this.parentStore.load();
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
        this.cmp.com_type.setValue(rowData.type);
        this.cmp.com_dist.setValue(rowData.dist);
        this.cmp.txt_name.setValue(rowData.name);
        this.cmp.txt_seq.setValue(rowData.seq);
        this.cmp.com_status.setValue(rowData.status);

        if (rowData.parentId != null) {
            this.source.com_firstMenuId.setValue(rowData.parentId);
            this.source.com_firstMenuId.disabled = false;
        } else {
            this.source.com_firstMenuId.disabled = true;
        }

        this.cmp.com_dist.fieldLabel = '<font style="color: red;">*</font>' + this.cmp.com_dist.fieldLabel;
        this.cmp.com_type.fieldLabel = '<font style="color: red;">*</font>' + this.cmp.com_type.fieldLabel;
        this.cmp.txt_code.fieldLabel = '<font style="color: red;">*</font>' + this.cmp.txt_code.fieldLabel;
        this.cmp.txt_name.fieldLabel = '<font style="color: red;">*</font>' + this.cmp.txt_name.fieldLabel;
        this.cmp.txt_seq.fieldLabel = '<font style="color: red;">*</font>' + this.cmp.txt_seq.fieldLabel;
        this.cmp.com_status.fieldLabel = '<font style="color: red;">*</font>' + this.cmp.com_status.fieldLabel;

    };

    this.cmp.com_type.on('select', function (thiz, value) {
        var selectValue = value.data.value;
        if ('1' == selectValue) {
            this.source.com_firstMenuId.clearValue();
            this.source.com_firstMenuId.disabled = true;
            this.source.com_firstMenuId.allowBlank = true;

        } else {
            this.source.com_firstMenuId.disabled = false;
            this.source.com_firstMenuId.allowBlank = false;

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

    var mask = new Ext.LoadMask({
        msg: '系统处理中...',
        target: this.win
    });


};