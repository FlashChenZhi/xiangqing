var EmptyLocationRetrievalPanel = function (data) {

    this.source = new AsrsSource();

    this.form = Ext.create("Ext.form.Panel", {
        title: '无库存货位出库',
        collapsible: true,
        scope: this,
        layout: 'column',
        items: [
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.source.txt_fromLocation,
                ]
            }
        ],
        buttons: [
            {
                text: '重置',
                handler: function () {
                    this.up('form').getForm().reset();
                }
            },
            {
                text: '出库',
                scope: this,
                handler: function () {
                    Ext.Msg.show({
                        title: '确认',
                        message: '确认出库？',
                        buttons: Ext.Msg.YESNO,
                        icon: Ext.Msg.QUESTION,
                        scope: this,
                        fn: function (btn) {
                            if (btn === 'yes') {
                                var locationNo = this.source.txt_fromLocation.getValue();
                                mask.show();
                                Ext.Ajax.request({
                                    url: 'wms/master/location',
                                    method: 'POST',
                                    params: {'locationNo': locationNo},
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
            tooltip: '无库存货位出库'
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
