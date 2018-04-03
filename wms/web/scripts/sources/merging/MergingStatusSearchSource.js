var MergingStatusSearchSource = function (){

    var checkFlagStore = Ext.create("Ext.data.JsonStore", {
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

    var onTransferLineFlagStore = Ext.create("Ext.data.JsonStore", {
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


	this.com_checkFlag =Ext.create('Ext.form.ComboBox', {
	        fieldLabel: '是否复核完成',
	        name: 'merginFinishFlag',
	        emptyText:'请选择',
	        store: checkFlagStore,
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

    this.date_beginCheckDate = Ext.create("Ext.form.field.Date", {
        fieldLabel: '复核时间(开始)',
        name: 'beginCheckDate',
        emptyText: "请选择日期",
        selectOnFocus: true,
        format: "Y-m-d",
        altFormats: "Y/m/d|Ymd"
    });

    this.date_endCheckDate = Ext.create("Ext.form.field.Date", {
        fieldLabel: '复核时间(结束)',
        name: 'endCheckDate',
        emptyText: "请选择日期",
        selectOnFocus: true,
        format: "Y-m-d",
        altFormats: "Y/m/d|Ymd"
    });


};
