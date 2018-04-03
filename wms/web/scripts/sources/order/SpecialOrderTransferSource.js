var SpecialOrderTransferSource = function () {

    var transferTypeStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': 'A001-->A009'},
                {'value': '2', 'displayValue': 'A001-->A011'},
                {'value': '3', 'displayValue': 'A011-->A009'}
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

    this.com_transferType = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '转储类型',
        name: 'transferType',
        emptyText: '请选择',
        store: transferTypeStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });


};
