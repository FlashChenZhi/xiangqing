var Zmm55ReceivingSource = function () {

    var skuStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data:{
            'res':[
                {'value':'1','displayValue':'test1'},
                {'value':'2','displayValue':'test2'},
                {'value':'3','displayValue':'test3'}
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

    var skuUnitStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data:{
            'res':[
                {'value':'3','displayValue':'箱'}
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

    var outletStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data:{
            'res':[
                {'value':'1','displayValue':'门店1'},
                {'value':'2','displayValue':'门店2'}
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



    var printerStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data:{
            'res':[
                {'value':'1','displayValue':'打印机1'},
                {'value':'3','displayValue':'打印机2'}
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


    this.txt_number = Ext.create("Ext.form.field.Text", {
        fieldLabel: '数量',
        name: 'number'
    });


    this.com_sku =Ext.create('Ext.form.ComboBox', {
        fieldLabel: '商品代码',
        name: 'skuId',
        emptyText:'请选择',
        store: skuStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus : true,
        valueField: 'value'
    });

    this.com_lutlet =Ext.create('Ext.form.ComboBox', {
        fieldLabel: '门店',
        name: 'lutletId',
        emptyText:'请选择',
        store: outletStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus : true,
        valueField: 'value'
    });

    this.com_skuUnit =Ext.create('Ext.form.ComboBox', {
        fieldLabel: '单位',
        name: 'unit',
        emptyText:'请选择',
        store: skuUnitStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus : true,
        valueField: 'value'
    });

    this.com_printer =Ext.create('Ext.form.ComboBox', {
        fieldLabel: '打印机',
        name: 'printerId',
        emptyText:'请选择',
        store: printerStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus : true,
        valueField: 'value'
    });

    
};
