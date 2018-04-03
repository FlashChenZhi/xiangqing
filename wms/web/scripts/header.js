var Header = function (user, theme, news) {
    console.log("username:" + user.name + ", theme:" + theme);

    var otherTheme;
    var otherThemeName;
    if ('crisp' == theme) {
        otherTheme = 'gray';
        otherThemeName = '黑色主题';
    } else {
        otherTheme = 'crisp';
        otherThemeName = '蓝色主题';
    }

    Ext.define("Rubicware.opt", function () {
        return {
            extend: "Ext.Container",
            xtype: "opt",
            margin: "0 5 0 0",
            layout: "hbox",
            initComponent: function () {
                var a = Ext.create("Ext.menu.Menu", {
                    items: [
                        {
                            text: otherThemeName,
                            scope: this,
                            handler: function () {
                                Ext.Ajax.request({
                                    url: 'wms/user/updateTheme',
                                    method: 'POST',
                                    params: {'userCode': user.code, 'theme': otherTheme},
                                    scope: this,
                                    success: function (response, options) {
                                        var result = Ext.util.JSON.decode(response.responseText);
                                        if (result.success) {
                                            window.location.href = 'index.html?theme=' + otherTheme;
                                        } else {
                                            console.log(result.msg);
                                        }
                                    },
                                    failure: function (response, options) {
                                    }
                                });


                            }
                        },
                        {
                            text: '修改密码',
                            scope: this,
                            handler: function () {
                                var win = new ModifyPasswordWin();
                                win.init(user);
                                win.win.show();
                            }
                        },
                        {
                            text: '退出系统',
                            scope: this,
                            handler: function () {
                                Ext.Ajax.request({
                                    url: 'wms/auth/loginOut',
                                    method: 'POST',
                                    scope: this,
                                    success: function (response, options) {
                                        var result = Ext.util.JSON.decode(response.responseText);
                                        if (result.success) {
                                            window.location.href = "login.html";
                                        } else {
                                            console.log(result.msg);
                                        }
                                    },
                                    failure: function (response, options) {
                                    }
                                });
                            }
                        }
                    ]
                });
                this.items = [
                    {
                        xtype: "component",
                        id: "theme-switcher",
                        cls: "hi-icon hi-icon-user",
                        listeners: {
                            scope: this,
                            click: function (j) {
                                a.showBy(this)
                            },
                            element: "el"
                        }
                    }
                ];
                this.callParent();
            }
        }
    });

    var headBackgroud = '#282828';
    if ('crisp' == theme) {
        headBackgroud = '#157fcc';
    }

    this.header = Ext.create("Ext.Container", {
        id: "rubicware-header",
        title: "Rubicware",
        region: 'north',
        style: {background: headBackgroud},
        height: 45,
        layout: {
            type: "hbox",
            align: "middle"
        },
        items: [
            //{
            //    xtype: "component",
            //    id: "rubicware-header-logo",
            //    html:'<img style="width:38px;height:38px;" src="images/yunlif.png">'
            //},
            {
                xtype: "component",
                id: "rubicware-header-title",
                html: '<div style="font: 30px 微软雅黑,arial,sans-serif;color:white;float: left">WMS</div>' +
                '<div  style="color: red;margin-left: 100px;font: 25px 微软雅黑,arial"><marquee scrollamount="5" scrolldelay="5" direction="left"><div id="news"></div></marquee></div>',
                flex: 1
            },
            {
                xtype: "component",
                id: 'rubicware-header-user',
                html: user.name,
                style: 'color:white;font:14px/40px 微软雅黑,arial,sans-serif;'
            },
            {
                xtype: "opt",
                width: 40,
                listeners: {
                    scope: this,
                    click: function (thiz) {
                        menu.showBy(thiz);
                    }
                }
            }
        ]
    });
    setInterval(function () {

        Ext.Ajax.request({
            url: 'wms/auth/searchNews',
            method: 'POST',
            success: function (response, options) {
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.success) {
                    Ext.get("news").dom.innerHTML = result.msg;
                }
            }
        });

    }, 1000 * 60);
};