var WelcomePanel = function (data) {

    this.source = new CommonSource(data);

    this.form = Ext.create("Ext.form.Panel", {
        title: '设置欢迎语',
        collapsible: true,
        scope: this,
        layout: 'column',
        items: [
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.source.txt_welcome
                ]
            }
        ],
        buttons: [
            {
                text: '查询',
                scope: this,
                handler: function () {
                    Ext.Ajax.request({
                        url: 'wms/master/config/searchWelcome',
                        method: 'POST',
                        scope: this,
                        success: function (response, options) {
                            mask.hide();
                            var result = Ext.util.JSON.decode(response.responseText);
                            if (result.success) {
                                this.source.txt_welcome.setValue(result.res)
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
            },
            {
                text: '修改',
                scope: this,
                handler: function () {
                    Ext.Msg.show({
                        title: '确认',
                        message: '确认修改？',
                        buttons: Ext.Msg.YESNO,
                        icon: Ext.Msg.QUESTION,
                        scope: this,
                        fn: function (btn) {
                            if (btn === 'yes') {
                                var welcome = this.source.txt_welcome.getValue();
                                mask.show();
                                Ext.Ajax.request({
                                    url: 'wms/master/config/settingWelcome',
                                    method: 'POST',
                                    params: {'welcome': welcome},
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
            }
        ]
    });

    this.panel = Ext.create("Ext.panel.Panel", {
        tabConfig: {
            tooltip: '设置欢迎语'
        },
        overflowY: 'auto',
        items: [
            this.form
        ]
    });


    var mask = new Ext.LoadMask({
        msg: '系统处理中...',
        target: this.panel
    });
};
