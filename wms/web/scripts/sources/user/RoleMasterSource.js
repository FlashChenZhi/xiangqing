var RoleMasterSource = function () {

    this.typeMap = function() {
        var map = new Map();
        map.put('1','一级菜单');
        map.put('2','二级菜单');
        return map;
    };

    this.distMap = function() {
        var map = new Map();
        map.put('1','WEB');
        map.put('2','RF');
        map.put('3','CZ');

        return map;
    };

    this.statusMap = function() {
        var map = new Map();
        map.put('1','正常');
        map.put('0','冻结');
        return map;
    };

    var statusStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': '正常'},
                {'value': '0', 'displayValue': '冻结'}
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
        fieldLabel: '状态',
        name: 'status',
        emptyText: '请选择',
        store: statusStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.txt_name = Ext.create("Ext.form.field.Text", {
        fieldLabel: '名称',
        name: 'name'
    });

    this.txt_id = Ext.create("Ext.form.field.Text", {
        fieldLabel: 'id',
        name: 'id'
    });

};
