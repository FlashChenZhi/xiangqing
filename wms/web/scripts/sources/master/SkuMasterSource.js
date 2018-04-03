var SkuMasterSource = function (){
	var ownerStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data:{
            'res':[
                {'value':'1','displayValue':'lyf'}
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

    var skuStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data:{
            'res':[
                {'value':'1','displayValue':'70459:鸭胗'}
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

    var blockFlagStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data:{
            'res':[
                {'value':'1','displayValue':'冻结'},
                {'value':'0','displayValue':'正常'}
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

    var seasonFlagStore = Ext.create("Ext.data.JsonStore", {
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

    var splitFlagStore = Ext.create("Ext.data.JsonStore", {
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

    var barcodeErrorFlagStore = Ext.create("Ext.data.JsonStore", {
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

     var roleStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data:{
            'res':[
                {'value':'1','displayValue':'管理员'},
                {'value':'0','displayValue':'收货工'}
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

      var statusStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data:{
            'res':[
                {'value':'1','displayValue':'正常'},
                {'value':'0','displayValue':'冻结'}
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

	this.com_owner =Ext.create('Ext.form.ComboBox', {
	        fieldLabel: '货主代码',
	        name: 'owner',
	        emptyText:'请选择',
	        store: ownerStore,
	        queryMode: 'local',
	        displayField: 'displayValue',
	        selectOnFocus : true,
	        valueField: 'value'
	});

	this.com_sku =Ext.create('Ext.form.ComboBox', {
	        fieldLabel: '商品代码',
	        name: 'sku',
	        emptyText:'请选择',
	        store: skuStore,
	        queryMode: 'local',
	        displayField: 'displayValue',
	        selectOnFocus : true,
	        valueField: 'value'
	});

	this.com_blockFlag =Ext.create('Ext.form.ComboBox', {
	        fieldLabel: '是否冻结',
	        name: 'blockFlag',
	        emptyText:'请选择',
	        store: blockFlagStore,
	        queryMode: 'local',
	        displayField: 'displayValue',
	        selectOnFocus : true,
	        valueField: 'value'
	});

	this.com_seasonFlag =Ext.create('Ext.form.ComboBox', {
	        fieldLabel: '是否短保',
	        name: 'seasonFlag',
	        emptyText:'请选择',
	        store: seasonFlagStore,
	        queryMode: 'local',
	        displayField: 'displayValue',
	        selectOnFocus : true,
	        valueField: 'value'
	});

    this.com_foodFlag = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否食品',
        name: 'foodFlag',
        emptyText:'请选择',
        store: seasonFlagStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus : true,
        valueField: 'value'
    });

    this.com_status =Ext.create('Ext.form.ComboBox', {
            fieldLabel: '状态',
            name: 'status',
            emptyText:'请选择',
            store: statusStore,
            queryMode: 'local',
            displayField: 'displayValue',
            selectOnFocus : true,
            valueField: 'value'
    });

    this.com_isOnline =Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否投线',
        name: 'onLine',
        emptyText:'请选择',
        store: seasonFlagStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus : true,
        valueField: 'value'
    });

    this.com_exemptionFlag =Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否免检',
        name: 'exemptionFlag',
        emptyText:'请选择',
        store: seasonFlagStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus : true,
        valueField: 'value'
    });

    this.com_splitFlag =Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否拆零',
        name: 'splitFlag',
        emptyText:'请选择',
        store: splitFlagStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus : true,
        valueField: 'value'
    });

    this.com_packageMater =Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否包材',
        name: 'packageMater',
        emptyText:'请选择',
        store: splitFlagStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus : true,
        valueField: 'value'
    });

    this.com_barcodeErrorFlag =Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否码质异常',
        name: 'barcodeErrorFlag',
        emptyText:'请选择',
        store: barcodeErrorFlagStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus : true,
        valueField: 'value'
    });

    this.txt_purcharseBarcode = Ext.create("Ext.form.field.Text", {
        fieldLabel: '箱条码',
        name: 'purcharseBarcode'
    });

    this.txt_shippingBarcode = Ext.create("Ext.form.field.Text", {
        fieldLabel: '发货条码',
        name: 'shippingBarcode'
    });

    this.txt_basicBarcode = Ext.create("Ext.form.field.Text", {
        fieldLabel: '基本条码',
        name: 'basicBarcode'
    });

	this.txt_skuCode = Ext.create("Ext.form.field.Text", {
        fieldLabel: '商品代码',
        name: 'skuCode'
    });

    this.txt_skuName = Ext.create("Ext.form.field.Text", {
        fieldLabel: '商品名称',
        name: 'skuName'
    });

    this.txt_skuEom = Ext.create("Ext.form.field.Text", {
        fieldLabel: '商品单位',
        name: 'eom'
    });

    this.txt_expirDate = Ext.create("Ext.form.field.Number", {
        fieldLabel: '保质期',
        name: 'expirDate',
        minValue: 0
    });

    this.txt_allowDate = Ext.create("Ext.form.field.Number", {
        fieldLabel: '允收期',
        name: 'allowDate',
        minValue: 0
    });

   
    this.txt_userName = Ext.create("Ext.form.field.Text", {
        fieldLabel: '员工姓名',
        name: 'userName'
    });
    
    this.txt_nikeName = Ext.create("Ext.form.field.Text", {
        fieldLabel: '用户名',
        name: 'nikeName'
    });

    this.txt_userPwd = Ext.create("Ext.form.field.Text", {
        fieldLabel: '密码',
        name: 'userPwd'
    });

    this.txt_level = Ext.create("Ext.form.field.Number", {
        fieldLabel: '层',
        name: 'level',
        minValue: 0
    });

    this.txt_front = Ext.create("Ext.form.field.Number", {
        fieldLabel: '面',
        name: 'front',
        minValue: 0
    });

    this.txt_shippingEomVolume = Ext.create("Ext.form.field.Number", {
        fieldLabel: '发货单位体积',
        name: 'shippingEomVolume',
        minValue: 0
    });

    this.txt_purcharseEomVolume = Ext.create("Ext.form.field.Number", {
        fieldLabel: '采购单位体积',
        name: 'purcharseEomVolume',
        minValue: 0
    });

};
