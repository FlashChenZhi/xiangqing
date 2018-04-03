/**
 * Created by meak on 14-10-28.
 */
var PackingConfirmSource = function () {
    this.txt_expressNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '运单号',
        name: 'expressNo'
    });

    this.txt_containerBarcode =  Ext.create("Ext.form.field.Text", {
        fieldLabel: '容器条码',
        name: 'barcode'
    });

    this.txt_productCode = Ext.create("Ext.form.field.Text", {
        fieldLabel: '商品条码',
        name: 'productCode'
    });

    this.txt_packageType = Ext.create("Ext.form.field.Text", {
        fieldLabel: '包材型号',
        name: 'packageType'
    });

    this.txt_tipsPackageType = Ext.create("Ext.form.field.Text", {
        fieldLabel: '提示包材型号',
        name: 'TipsPackageType'
    });

    this.lbl_msgLabel = Ext.create("Ext.form.Label",{
        //id:'msgLabel'
    });

    this.lbl_tipsPackageTypeLabel = Ext.create("Ext.form.Label",{
        //id:'msgLabel'
    });

    this.lbl_errorMsgLabel = Ext.create("Ext.form.Label",{
        //id:'msgLabel'
    });

    var labelPrintStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        scope: this,
        proxy: {
            type: "ajax",
            url: "rubicware/common/getAvailablePrinter",
            actionMethods: {read: "POST"},
            reader: {
                type: "json",
                rootProperty: "res"
            },
            listeners: {
                exception: function (thiz, request) {
                    Ext.Msg.alert('系统错误', '查询时发生错误');
                }
            }
        }
    });

    this.com_labelPrinter = Ext.create('Ext.form.ComboBox', {
        name: 'labelPrinter',
        emptyText: '请选择',
        store: labelPrintStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        forceSelection: true,
        valueField: 'value'
    });

    this.number_weight = Ext.create('Ext.form.field.Number',{
        name:'weight',
        fieldLabel:'称重重量',
        decimalPrecision:3,
        allowNegative:false,
        minValue:0
    });
    this.number_totalWeight = Ext.create('Ext.form.field.Number',{
        name:'weight',
        fieldLabel:'计算重量',
        decimalPrecision:3,
        readOnly:true,
        allowNegative:false,
        minValue:0
    });
    this.btn_weight = Ext.create('Ext.button.Button',{
        text:'称重',
        listeners:{
            scope:this,
            click:function(){
                var number_weight = this.number_weight;
                var txt_packageType = this.txt_packageType;
                this.getWeight(function(result){
                    if(result.success) {
                        number_weight.setValue(result.weight);
                        txt_packageType.enable();
                        txt_packageType.focus();
                    }
                });
            }
        }
    });
    this.getWeight = function(fun){
        var result = {success:false,weight:0};
        Ext.data.JsonP.request({
            url: 'http://localhost:8080/serial',
            success: function (response, options) {
                if (response.success) {
                    result.success = true;
                    result.weight = response.res.weight;
                    fun(result);
                }
            },
            failure: function (response, options) {
                Ext.error('称重失败,请检查称重程序是否启动');
                fun(result);

            }
        });
    }

    this.lab_BindingContainerQty = Ext.create("Ext.form.field.Display", {
        fieldLabel: '绑定容器数',
        name: 'bindingContainerQty',
        value:'0'
    });

    this.number_packQty = Ext.create('Ext.form.field.Number',{
        name:'packQty',
        fieldLabel:'数量',
        minValue:1,
        value:1
    });

    this.number_pacMetkQty = Ext.create('Ext.form.field.Number',{
        name:'packMetQty',
        fieldLabel:'包材数量',
        minValue:1,
        value:1
    });


};