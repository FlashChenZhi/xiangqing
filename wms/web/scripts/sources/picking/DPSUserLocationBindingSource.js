var DPSUserLocationBindingSource = function () {

    this.tipFlagMap = function () {
        var map = new Map();
        map.put(true, '提示');
        map.put(false, '非提示');
        return map;
    };

    var userStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data:{
            'res':[
                {'value':'1','displayValue':'张三'},
                {'value':'2','displayValue':'李四'}
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


    this.com_user =Ext.create('Ext.form.ComboBox', {
        fieldLabel: '用户',
        name: 'userId',
        emptyText:'请选择',
        store: userStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus : true,
        valueField: 'value'
    });


    this.txt_locationNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '扫描台',
        name: 'locationNo'
    });

};
