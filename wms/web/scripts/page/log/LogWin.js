var LogWin = function () {
    this.source = new LogSearchSource();

    this.form = Ext.create("Ext.form.Panel", {
        items: [this.source.txt_summary]
    });

    this.init = function (data) {
        this.source.txt_summary.setValue(data.summary);
    };

    this.win = Ext.create("Ext.window.Window", {
        width: 500,
        bodyPadding: 5,
        resizable: false,
        scope: this,
        items: this.form
    });
};