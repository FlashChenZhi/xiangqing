/**
 * Created by Administrator on 2016/11/10.
 */

var DpsSource = function () {

    var dpsScanStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '9111', 'displayValue': '9111'},
                {'value': '9112', 'displayValue': '9112'},
                {'value': '9113', 'displayValue': '9113'},
                {'value': '9114', 'displayValue': '9114'},
                {'value': '9115', 'displayValue': '9115'},
                {'value': '9116', 'displayValue': '9116'},
                {'value': '9117', 'displayValue': '9117'},
                {'value': '9118', 'displayValue': '9118'},

                {'value': '9119', 'displayValue': '9119'},
                {'value': '9120', 'displayValue': '9120'},
                {'value': '9121', 'displayValue': '9121'},
                {'value': '9122', 'displayValue': '9122'},
                {'value': '9123', 'displayValue': '9123'},
                {'value': '9124', 'displayValue': '9124'},
                {'value': '9125', 'displayValue': '9125'},

                {'value': '9131', 'displayValue': '9131'},
                {'value': '9132', 'displayValue': '9132'},
                {'value': '9133', 'displayValue': '9133'},
                {'value': '9134', 'displayValue': '9134'},
                {'value': '9135', 'displayValue': '9135'},
                {'value': '9136', 'displayValue': '9136'},
                {'value': '9137', 'displayValue': '9137'},
                {'value': '9138', 'displayValue': '9138'},

                {'value': '9141', 'displayValue': '9141'},
                {'value': '9142', 'displayValue': '9142'},
                {'value': '9143', 'displayValue': '9143'},
                {'value': '9144', 'displayValue': '9144'},
                {'value': '9145', 'displayValue': '9145'},
                {'value': '9146', 'displayValue': '9146'},
                {'value': '9147', 'displayValue': '9147'},
                {'value': '9148', 'displayValue': '9148'},

                {'value': '9151', 'displayValue': '9151'},
                {'value': '9152', 'displayValue': '9152'},
                {'value': '9153', 'displayValue': '9153'},
                {'value': '9154', 'displayValue': '9154'},
                {'value': '9155', 'displayValue': '9155'},
                {'value': '9156', 'displayValue': '9156'},
                {'value': '9157', 'displayValue': '9157'},
                {'value': '9158', 'displayValue': '9158'},

                {'value': '9161', 'displayValue': '9161'},
                {'value': '9162', 'displayValue': '9162'},
                {'value': '9163', 'displayValue': '9163'},
                {'value': '9164', 'displayValue': '9164'},
                {'value': '9165', 'displayValue': '9165'},
                {'value': '9166', 'displayValue': '9166'},
                {'value': '9167', 'displayValue': '9167'},
                {'value': '9168', 'displayValue': '9168'},

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

    this.com_scaner = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '扫码枪',
        name: 'scaner',
        emptyText: '请选择',
        store: dpsScanStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

};

