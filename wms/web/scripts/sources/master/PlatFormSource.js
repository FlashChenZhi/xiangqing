/**
 * Created by root on 2016/4/25.
 */
var PlatFormSource = function () {

    var statusStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '0', 'displayValue': '正常'},
                {'value': '1', 'displayValue': '禁用'},
                {'value': '2', 'displayValue': '已排班'},
                {'value': '3', 'displayValue': '收获中'},

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

    this.com_status = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '站台',
        name: 'status',
        emptyText: '请选择',
        store: statusStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.txt_platFormCode = Ext.create("Ext.form.field.Text", {
        fieldLabel: '月台代码',
        name: 'platFormCode'
    });

};