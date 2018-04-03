var InOutSettingPanel = function () {

    this.form = Ext.create("Ext.form.Panel", {
        title: '出入库策略',
        collapsible: true,
        scope: this,
        layout: 'column',
        buttons: [
            {
                text: '查询',
                scope: this,
                handler: function () {
                    Ext.Ajax.request({
                        url: 'wms/master/config/searchInOutSetting',
                        method: 'POST',
                        scope: this,
                        success: function (response, options) {
                            mask.hide();
                            var result = Ext.util.JSON.decode(response.responseText);
                            if (result.success) {
                                Ext.success(result.res);
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
                text: '入库优先',
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
                                mask.show();
                                Ext.Ajax.request({
                                    url: 'wms/master/config/inFirst',
                                    method: 'POST',
                                    scope: this,
                                    success: function (response, options) {
                                        mask.hide();
                                        var result = Ext.util.JSON.decode(response.responseText);
                                        if (result.success) {
                                            Ext.success(result.msg);
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
            {
                text: '出库优先',
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
                                mask.show();
                                Ext.Ajax.request({
                                    url: 'wms/master/config/outFirst',
                                    method: 'POST',
                                    scope: this,
                                    success: function (response, options) {
                                        mask.hide();
                                        var result = Ext.util.JSON.decode(response.responseText);
                                        if (result.success) {
                                            Ext.success(result.msg);
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
            tooltip: '出入库策略'
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
