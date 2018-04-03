/**
 * Created by xingf on 2015/5/5.
 */

var BigOrderAllocateSource = function(){

    this.txt_allocateQty = Ext.create("Ext.form.field.Number", {
        fieldLabel: '分配数',
        name: 'allocateQty',
        minValue: 0
    });


}