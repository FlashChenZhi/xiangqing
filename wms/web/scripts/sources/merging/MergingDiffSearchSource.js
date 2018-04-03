var MergingDiffSearchSource = function (){

    var foodFlagStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data:{
            'res':[
                {'value':'1','displayValue':'食品'},
                {'value':'0','displayValue':'非食品'}
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

    var onTransferLineFlagStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data:{
            'res':[
                {'value':'1','displayValue':'上线'},
                {'value':'0','displayValue':'不上线'}
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


	this.com_foodFlag =Ext.create('Ext.form.ComboBox', {
	        fieldLabel: '是否食品',
	        name: 'foodFlag',
	        emptyText:'请选择',
	        store: foodFlagStore,
	        queryMode: 'local',
	        displayField: 'displayValue',
	        selectOnFocus : true,
	        valueField: 'value'
	});

    this.com_onTransferLineFlag =Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否上线',
        name: 'onTransferLineFlag',
        emptyText:'请选择',
        store: onTransferLineFlagStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus : true,
        valueField: 'value'
    });



};
