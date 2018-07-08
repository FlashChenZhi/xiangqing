/**
 * nodejs dev server
 * Created by xiongying on 16/8/5.
 */
'use strict';
var webpack = require('webpack');
var WebpackDevServer = require('webpack-dev-server');
var config = require('./webpack.config.dev.js');

var proxy = [{
    path: "/wcs/**",
    target: "http://localhost:8080/"
},{
    path: "/wms/**",
    target: "http://localhost:8080/"
}];

new WebpackDevServer(webpack(config), {
    watchOptions: {
        aggregateTimeout: 300,
        poll: 1000
    },
    publicPath: config.output.publicPath,
    hot: true,　　//开启热调试
    historyApiFallback: true,
    proxy: proxy
}).listen(19090, '127.0.0.1', function (err, result) {
    if (err) {
        console.log(err);
    }
    console.log('node server start');
});