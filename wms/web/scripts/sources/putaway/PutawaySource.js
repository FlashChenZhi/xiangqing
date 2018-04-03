/**
 * Created by Van on 2015/6/2.
 */
var PutawaySource = function (data) {

    this.typeMap = function(){
        var map = new Map();
        map.put("0","自动库上架");
        map.put("1","收货上架");
        map.put("2","退厂上架");
        map.put("3","移库上架");
        return map;
    };

    this.com_toLogicZone = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '待上架区域',
        name: 'toLogicZone',
        emptyText: '请选择',
        store: data.logicZoneCodesStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.txt_LocationNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '待上架货位号',
        name: 'locationNo'
    });

    this.txt_toLocationNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '目标货位号',
        name: 'toLocationNo'
    });

    this.txt_containerNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '容器号',
        name: 'containerNo'
    });
};