var UserMasterSource = function () {

    this.statusMap = function () {
        var map = new Map();
        map.put('1', '正常');
        map.put('0', '冻结');
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


    this.txt_Id = Ext.create("Ext.form.field.Text", {
        fieldLabel: 'id',
        name: 'id'
    });

    this.txt_code = Ext.create("Ext.form.field.Text", {
        fieldLabel: '工号',
        name: 'code'
    });

    this.txt_name = Ext.create("Ext.form.field.Text", {
        fieldLabel: '姓名',
        name: 'name'
    });

    this.txt_oldPassword = Ext.create("Ext.form.field.Text", {
        inputType: 'password',
        fieldLabel: '原密码',
        name: 'oldPassword'
    });

    this.txt_newPassword = Ext.create("Ext.form.field.Text", {
        inputType: 'password',
        fieldLabel: '新密码',
        name: 'newPassword'
    });

    this.txt_confirmPassword = Ext.create("Ext.form.field.Text", {
        inputType: 'password',
        fieldLabel: '确认密码',
        name: 'confirmPassword'
    });

};
