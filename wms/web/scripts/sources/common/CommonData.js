var CommonData = function () {
    this.ownerCodeStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"]
    });

    this.skuCodeStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"]
    });

    this.turnDateStoreWeek = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"]
    });

    this.turnDateStoreMonth = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"]
    });

    this.lazerPrinterStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"]
    });

    this.labelPrinterStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"]
    });

    this.receiptPrinterStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"]
    });
    this.tempZonesStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"]
    });

    this.outletIdsStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"]
    });

    this.zoneStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"]
    });

    this.carrierStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"]
    });

    this.dsCarrierStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"]
    });

    this.logicZoneCodesStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"]
    });

    this.tempLocationNosStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"]
    });

    this.wareHouseIdsStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"]
    });

    this.providersStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"]
    });

    this.roleIdsStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"]
    });

    this.firstMenuIdsStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"]
    });

    this.usersStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"]
    });

    this.aisleStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"]
    });

    this.stationStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"]
    });

    this.storgZoneStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"]
    });


    Ext.Ajax.request({
        url: 'wms/common/getData',
        method: 'POST',
        timeout: 120000,
        scope: this,
        success: function (response, options) {
            var result = Ext.util.JSON.decode(response.responseText);
            this.roleIdsStore.loadRawData(result['roleIds'].res);
            this.firstMenuIdsStore.loadRawData(result['firstMenuIds'].res);
            this.usersStore.loadRawData(result['users'].res);

            console.log("CommonData getData success");
        },
        failure: function (response, options) {
            var result = Ext.util.JSON.decode(response.responseText);
            Ext.MessageBox.alert('失败', result.msg);
            console.log("CommonData getData fail", response);
        }
    });
};