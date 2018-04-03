var DsReturnSkuReceivingSource = function () {

    this.returnSkuTypeMap = function() {
        var map = new Map();
        map.put('2','客退');
        map.put('5','再销售');
        return map;
    };

    var returnSkuTypeStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data:{
            'res':[
                {'value':'2','displayValue':'客退'},
                {'value':'5','displayValue':'再销售'}
            ]
        },
        proxy:{
            type:'memory',
            reader:{
                type:'json',
                rootProperty:'res'
            }
        }
    });

    this.com_returnSkuType =Ext.create('Ext.form.ComboBox', {
        fieldLabel: '退货类型',
        name: 'returnSkuType',
        store: returnSkuTypeStore,
        queryMode: 'local',
        displayField: 'displayValue',
        editable: false,
        triggerAction: "all",
        value: '',
        valueField: 'value'
    });


};
