var OwnerMasterSource = function (){

    this.txt_id = Ext.create("Ext.form.field.Text", {
        fieldLabel: '货主ID',
        name: 'id'
    });

	this.txt_code = Ext.create("Ext.form.field.Text", {
        fieldLabel: '货主代码',
        name: 'code'
    });

    this.txt_name = Ext.create("Ext.form.field.Text", {
        fieldLabel: '货主名称',
        name: 'name'
    });

    this.txt_expressCnorName = Ext.create("Ext.form.field.Text", {
        fieldLabel: '发货人名称',
        name: 'expressCnorName'
    });

    this.txt_expressCnorPhone = Ext.create("Ext.form.field.Text", {
        fieldLabel: '发货人电话',
        name: 'expressCnorPhone'
    });

    this.txt_expressCnorAddress = Ext.create("Ext.form.field.Text", {
        fieldLabel: '发货人地址',
        name: 'expressCnorAddress'
    });

}
