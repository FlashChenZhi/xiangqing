var DpsLogSource = function (){

    var typeStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': '拣选'},
                {'value': '2', 'displayValue': '点灯'},
                {'value': '3', 'displayValue': '分组'}
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

    this.dpsTypeMap = function (){
        var map = new Map();
        map.put("1","拣选");
        map.put("2","点灯");
        map.put("3","分组");
        return map;
    };

    this.com_type = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '类型',
        name: 'type',
        emptyText: '请选择',
        store: typeStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });


    this.date_beginDate = Ext.create("Ext.form.field.Date", {
        fieldLabel: '开始时间',
        name: 'beginDate',
        emptyText: "请选择日期",
        selectOnFocus: true,
        format: "Y-m-d",
        altFormats: "Y/m/d|Ymd"
    });

    this.date_endDate = Ext.create("Ext.form.field.Date", {
        fieldLabel: '结束时间',
        name: 'endDate',
        emptyText: "请选择日期",
        selectOnFocus: true,
        format: "Y-m-d",
        altFormats: "Y/m/d|Ymd"
    });
};
