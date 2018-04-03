var OutletLablePrintSource = function () {

    this.allowPrintMap = function () {
        var map = new Map();
        map.put(true, '可打印');
        map.put(false, '不可打印');
        return map;
    };
};
