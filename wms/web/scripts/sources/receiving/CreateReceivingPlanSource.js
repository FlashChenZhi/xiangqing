/**
 * Created by Van on 2015/9/2.
 */
var CreateReceivingPlanSource = function () {

    var skuCode = "";

    this.initSkuEom = function (code) {
        skuCode = code;
        this.skuEomStore.load();
    };

    var typeStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '11', 'displayValue': 'zm555'},
                {'value': '12', 'displayValue': '老翻新'},
                {'value': '16', 'displayValue': '例外入库'}
            ]
        },
        proxy: {
            type: 'memory',
            reader: {
                type: 'json',
                rootProperty: 'res'
            }
        }
    });
    this.com_type = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '入库类型',
        name: 'type',
        emptyText: '请选择',
        store: typeStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.skuEomStore = Ext.create("Ext.data.JsonStore", {
        fields: ['value', 'displayValue'],
        scope: this,
        listeners: {
            scope: this,
            beforeload: function (store) {
                store.proxy.extraParams = {'skuCode': skuCode};
            }
        },
        proxy: {
            type: "ajax",
            url: "wms/createReceivingPlan/getSkuEom",
            actionMethods: {read: "POST"},
            reader: {
                type: "json",
                rootProperty: "res"
            },
            listeners: {
                exception: function (thiz, response) {
                    var result = Ext.util.JSON.decode(response.responseText);
                    Ext.Msg.alert('失败', result.msg);
                    console.log(result);
                }
            }
        }
    });

    this.com_skuEom = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '单位',
        name: 'eom',
        emptyText: '请选择',
        store: this.skuEomStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });
};
