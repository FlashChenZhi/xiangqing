var Main = function (user, theme) {
    var data = new CommonData();

    var userCode = user.code;
    var authStore = Ext.create('Ext.data.TreeStore', {
        autoLoad: true,
        proxy: {
            type: 'ajax',
            url: 'wms/auth/getAuths?code=' + userCode,//请求
            reader: {
                type: 'json',
                root: 'res'//数据
            },
            //传参
            extraParams: {
                id: ''
            }
        }
        ,
        root: {
            text: 'root',
            id: '-1',
            expanded: true
        }

    });

    var tree = Ext.create("Ext.tree.Panel", {
        width: 200,
        store: authStore,
        useArrows: true,
        rootVisible: false,
        listeners: {
            cellclick: {
                fn: function (thiz, td, cellIndex, record, tr, rowIndex, e, eOpts) {
                    if (tab.getComponent("TAB_" + record.id) == undefined) {
                        var cmp = null;
                        switch (record.id) {
                            case "1001":
                                cmp = new userMaster(data).panel;
                                break;
                            case "1002":
                                cmp = new RoleMaster(data).panel;
                                break;
                            case "1003":
                                cmp = new MenuMaster(data).panel;
                                break;
                            case "1004":
                                cmp = new AuthMaster(data).panel;
                                break;

                            case "2001":
                                cmp = new OrderSearchPanel(data).panel;
                                break;

                            case "2002":
                                cmp = new OrderRetrievalPanel(data).panel;
                                break;
                            case "2003":
                                cmp = new DirectRetrievalPanel(data).panel;
                                break;

                            case "3001":
                                cmp = new InventorySearchPanel(data).panel;
                                break;

                            case "3002":
                                cmp = new InventoryLogSearchPanel(data).panel;
                                break;

                            case "3003":
                                cmp = new InventoryTransferPanel(data).panel;
                                break;

                            case "3004":
                                cmp = new InventoryWarningPanel(data).panel;
                                break;

                            case "3005":
                                cmp = new InventoryRelease(data).panel;
                                break;

                            case "3006":
                                cmp = new InventorySearchDailyPanel(data).panel;
                                break;
                            case "3007":
                                cmp = new InventoryBatchTransferPanel(data).panel;
                                break;

                            case "4001":
                                cmp = new AsrsJobPanel(data).panel;
                                break;
                            case "4002":
                                cmp = new AsrsJobLogPanel(data).panel;
                                break;

                            case "4003":
                                cmp = new EmptyLocationRetrievalPanel(data).panel;
                                break;

                            case "5001":
                                cmp = new SkuMasterPanel(data).panel;
                                break;
                                
                            case "5002":
                                cmp = new SkuShelfLifePanel(data).panel;
                                break;

                            case "5003":
                                cmp = new SkuStrategyPanel(data).panel;
                                break;

                            case "5004":
                                cmp = new LocationUseRate().panel;
                                break;
                            case "5005":
                                cmp = new LocationRelease(data).panel;
                                break;
                            case "5006":
                                cmp = new WelcomePanel(data).panel;
                                break;
                            case "5007":
                                cmp = new StatisticsPanel(data).panel;
                                break;
                            case "5008":
                                cmp = new InOutSettingPanel().panel;
                                break;

                            case "9001":
                                cmp = new SystemLogSearchPanel().panel;

                            //case "50":
                            //    cmp = new TransportOrderAllocatePanel(data).panel;
                            //    break;
                        }
                        if (cmp != null) {
                            cmp.id = "TAB_" + record.id;
                            cmp.closable = true;
                            cmp.title = record.data.text;
                            cmp.overflowY = 'auto';
                            tab.add(cmp);
                        }
                    }
                    tab.setActiveItem("TAB_" + record.id);
                }
            }
        }
    });
    tree.collapseAll();
    var tab = Ext.create("Ext.tab.Panel", {
        defaults: {
//            autoScroll: true,
            bodyPadding: 5
        }
    });

    var defaultPanel = Ext.create("Ext.panel.Panel", {
        // html: '<IFRAME NAME="content_frame" width=97% height=100% marginwidth=0 marginheight=0 SRC="help.pdf" ></IFRAME> '
    });
    defaultPanel.closable = tree;
    tab.add(defaultPanel);

    var header = new Header(user, theme);
    var mainPanel = Ext.create("Ext.container.Viewport", {
        id: 'mainPanel',
        autoHeight: false,
        layout: "border",
        items: [
            header.header,
           {
               region: 'south',
               bodyStyle: 'background:#DBDBDB;',
               html: '<div style="width: 100%;background:#282828;"><span style="color:#fdfdfd;margin-left: 10px;">世仓智能仓储设备(上海)股份有限公司<sup>&reg;</sup></span></div>'
           },
            {
                region: "west",
                collapsible: true,
                split: true,
                title: "功能清单",
                width: 200,
                items: tree,
                overflowY: 'auto'
            },
            {
                region: "center",
                items: tab,
                layout: 'fit',
                overflowY: 'auto'
            }
        ],
        listeners: {}
    });
};