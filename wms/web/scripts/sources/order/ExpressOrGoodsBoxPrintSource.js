var ExpressOrGoodsBoxPrintSource = function (){

    var printerTypeStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data:{
            'res':[
                {'value':'1','displayValue':'面单'},
                {'value':'2','displayValue':'装箱单'}
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

    this.text_expressNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '运单号',
        name: 'expressNo'
    });


    this.com_printerType =Ext.create('Ext.form.ComboBox', {
        fieldLabel: '打印类型',
        name: 'printerType',
        emptyText:'请选择',
        store: printerTypeStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus : true,
        valueField: 'value'
    });


};