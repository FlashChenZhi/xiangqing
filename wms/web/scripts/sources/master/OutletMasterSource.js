var OutletMasterSource = function (){
	
    var batchFlag = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data:{
            'res':[
                {'value':true,'displayValue':'是'},
                {'value':false,'displayValue':'否'}
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
                {'value':'0001','displayValue':'0001:门店1'},
                {'value':'0002','displayValue':'0002:门店2'}
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

    var reasonFlagStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data:{
            'res':[
                {'value':false,'displayValue':'非短保商品'},
                {'value':true,'displayValue':'短保商品'}
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

    var shippingStragegyStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data:{
            'res':[
                {'value':false,'displayValue':'后进先出'},
                {'value':true,'displayValue':'先进先出'}
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

	this.com_batchFlag =Ext.create('Ext.form.ComboBox', {
	        fieldLabel: '是否接受退品',
	        name: 'batchFlag',
	        emptyText:'请选择',
	        store: batchFlag,
	        queryMode: 'local',
	        displayField: 'displayValue',
	        selectOnFocus : true,
	        valueField: 'value'
	});

    this.com_outlet =Ext.create('Ext.form.ComboBox', {
            fieldLabel: '门店',
            name: 'outletCode',
            emptyText:'请选择',
            store: outletStore,
            queryMode: 'local',
            displayField: 'displayValue',
            selectOnFocus : true,
            valueField: 'value'
    });

    this.com_reasonFlag =Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否短保商品',
        name: 'reasonFlag',
        emptyText:'请选择',
        store: reasonFlagStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus : true,
        valueField: 'value'
    });

    this.com_shippingStragegy =Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否先进先出',
        name: 'fifoStragegy',
        emptyText:'请选择',
        store: shippingStragegyStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus : true,
        valueField: 'value'
    });

    this.txt_outletCode = Ext.create("Ext.form.field.Text", {
        fieldLabel: '客户代码',
        name: 'outletCode'
    });

    this.txt_outletName = Ext.create("Ext.form.field.Text", {
        fieldLabel: '门店名称',
        name: 'outletName'
    });
	

}
