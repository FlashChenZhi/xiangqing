var AsrsReplenishSource = function () {
    
    this.statusMap = function () {
        var map = new Map();
        map.put('0','等待执行');
        map.put('1','执行成功');
        map.put('2','执行失败');
        return map;
    };

    var statusStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '0', 'displayValue': '等待执行'},
                {'value': '1', 'displayValue': '执行成功'},
                {'value': '2', 'displayValue': '执行失败'}
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


    this.com_status = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '执行状态',
        name: 'status',
        emptyText: '请选择',
        store: statusStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.date_beginDate = Ext.create("Ext.form.field.Date", {
        fieldLabel: '创建时间(开始)',
        name: 'beginDate',
        emptyText: "请选择日期",
        selectOnFocus: true,
        format: "Y-m-d",
        altFormats: "Y/m/d|Ymd"
    });

    this.date_endDate = Ext.create("Ext.form.field.Date", {
        fieldLabel: '创建时间(结束)',
        name: 'endDate',
        emptyText: "请选择日期",
        selectOnFocus: true,
        format: "Y-m-d",
        altFormats: "Y/m/d|Ymd"
    });

};
