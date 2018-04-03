var ReceivingPlanRegistrationSource = function () {

    this.txt_purchaseid=Ext.create('Ext.form.field.Text',{
        fieldLabel:'采购单号',
        name:'purchase_ID'
    });

    var finishFlagStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': '待排班'},
                {'value': '2', 'displayValue': '已到厂登记'},
                {'value': '3', 'displayValue': '收货中'},
                {'value': '4', 'displayValue': '已完成'},
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

    this.com_finishFlag = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '排班状态',
        name: 'finishFlag',
        store: finishFlagStore,
        queryMode: 'local',
        displayField: 'displayValue',
        editable: false,
        triggerAction: "all",
        value: '',
        valueField: 'value'
    });

};