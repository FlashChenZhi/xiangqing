/**
 * webpack生产环境配置
 * Created by xiongying on 16/8/5.
 */
var path = require('path');
var webpack = require('webpack');
var ExtractTextPlugin = require("extract-text-webpack-plugin");
module.exports = {
    entry: {
        index: './src/entry/index.js'
    },
    output: {
        path: path.join(__dirname, 'www'),
        filename: 'dist/bundle.js'
    },
    resolve: {
        extensions: ['', '.js', '.jsx', '.css', '.less']
    },
    module: {
        loaders: [
            {test: /\.js$/, loader: 'babel?presets[]=react,presets[]=es2015'},
            {test: /\.jsx?$/, loader: 'babel?presets[]=react,presets[]=es2015'},
            {test: /\.css$/, loader: 'style!css'},
            {test: /\.less$/, loader: ExtractTextPlugin.extract("style-loader", "css-loader!less-loader")},
            {test: /\.(png|jpe?g|eot|svg|ttf|woff2?)$/, loader: "file?name=images/[name].[ext]"}
        ]
    },
    babel: {
        //使用antd默认样式
        plugins: [["antd", {style: "css"}]]
    },
    plugins: [
        new ExtractTextPlugin("[name].css"),
        //生产环境再使用,压缩js
        // new webpack.optimize.UglifyJsPlugin({
        //     compress: {
        //         warnings: false
        //     }
        // })
    ]
};