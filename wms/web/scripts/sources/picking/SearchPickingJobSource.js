/**
 * Created by Van on 2015/8/10.
 */
var SearchPickingJobSource = function () {

    var booleanStore = Ext.create("Ext.data.JsonStore", {
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

    this.txt_fromLocationNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '源货位号',
        name: 'fromLocationNo'
    });

    this.txt_toLocationNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '目标货位号',
        name: 'toLocationNo'
    });

    this.txt_dpsAddress = Ext.create("Ext.form.field.Text", {
        fieldLabel: 'DPS地址',
        name: 'dpsAddress'
    });

    this.txt_jobId = Ext.create("Ext.form.field.Text", {
        fieldLabel: '作业ID',
        name: 'jobId'
    });

    this.txt_groupNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '组号',
        name: 'groupNo'
    });

    this.com_recvFlag =Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否领取',
        name: 'recvFlag',
        emptyText:'请选择',
        store: booleanStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus : true,
        valueField: 'value'
    });

    this.com_allowRecvFlag =Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否允许领取',
        name: 'allowRecvFlag',
        emptyText:'请选择',
        store: booleanStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus : true,
        valueField: 'value'
    });

    this.com_detailFlag =Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否明细拣选',
        name: 'detailFlag',
        emptyText:'请选择',
        store: booleanStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus : true,
        valueField: 'value'
    });


};