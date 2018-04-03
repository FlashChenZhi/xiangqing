var AutoStorageZoneRetrievalSource = function () {


    var bayStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': '1'},
                {'value': '2', 'displayValue': '2'},
                {'value': '3', 'displayValue': '3'},
                {'value': '4', 'displayValue': '4'},
                {'value': '5', 'displayValue': '5'},
                {'value': '6', 'displayValue': '6'},
                {'value': '7', 'displayValue': '7'},
                {'value': '8', 'displayValue': '8'},
                {'value': '9', 'displayValue': '9'},
                {'value': '10', 'displayValue': '10'},
                {'value': '11', 'displayValue': '11'},
                {'value': '12', 'displayValue': '12'},
                {'value': '13', 'displayValue': '13'},
                {'value': '14', 'displayValue': '14'},
                {'value': '15', 'displayValue': '15'},
                {'value': '16', 'displayValue': '16'},
                {'value': '17', 'displayValue': '17'},
                {'value': '18', 'displayValue': '18'},
                {'value': '19', 'displayValue': '19'},
                {'value': '20', 'displayValue': '20'},
                {'value': '21', 'displayValue': '21'},
                {'value': '22', 'displayValue': '22'},
                {'value': '23', 'displayValue': '23'},
                {'value': '24', 'displayValue': '24'},
                {'value': '25', 'displayValue': '25'},
                {'value': '26', 'displayValue': '26'},
                {'value': '27', 'displayValue': '27'}

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

    var exceedLastRetrievalDateFlagStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': true, 'displayValue': '已超出'},
                {'value': false, 'displayValue': '未超出'}
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

    var fifoFlagStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': true, 'displayValue': '直接出库'},
                {'value': false, 'displayValue': '迂回出库'}
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

    var bankNoStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': '1'},
                {'value': '2', 'displayValue': '2'}
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


    this.com_bay = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '巷道号',
        name: 'bayNo',
        emptyText: '请选择',
        store: bayStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });



    this.com_exceedLastRetrievalDateFlag = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '最后出库日',
        name: 'exceedLastRetrievalDateFlag ',
        emptyText: '请选择',
        store: exceedLastRetrievalDateFlagStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_fifoFlag = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '出库类型',
        name: 'fifoFlag',
        emptyText: '请选择',
        store: fifoFlagStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_bankNo = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '排(Bank)',
        name: 'bankNo',
        emptyText: '请选择',
        store: bankNoStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

};