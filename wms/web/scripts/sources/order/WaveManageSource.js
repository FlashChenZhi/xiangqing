/**
 * Created by Van on 2015/6/2.
 */
var WaveManageSource = function () {

    var type = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': 'RDC'},
                {'value': '2', 'displayValue': '委外出库'},
                {'value': '2', 'displayValue': '加盟商'}
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
        fieldLabel: '待上架货位',
        name: 'type',
        emptyText: '请选择',
        store: type,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });


    var statusStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': '开始'},
                {'value': '2', 'displayValue': '拣选中'},
                {'value': '3', 'displayValue': '已集货'},
                {'value': '4', 'displayValue': '完成'}
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
        fieldLabel: '状态',
        name: 'status',
        emptyText: '请选择',
        store: statusStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.txt_orderNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '波次号',
        name: 'waveOrderNo'
    });
}