var SkuExceptionSource = function (){

    var typeStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data:{
            'res':[
                {'value':'1','displayValue':'SKU主数据异常'},
                {'value':'2','displayValue':'待收货SKU规则异常'},
                {'value':'3','displayValue':'未绑定货位SKU'}
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


	this.com_type =Ext.create('Ext.form.ComboBox', {
	        fieldLabel: '异常类型',
	        name: 'type',
	        emptyText:'请选择',
	        store: typeStore,
	        queryMode: 'local',
	        displayField: 'displayValue',
	        selectOnFocus : true,
	        valueField: 'value'
	});



};
