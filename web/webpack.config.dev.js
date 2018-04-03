/**
 * webpack开发环境配置
 * Created by xiongying on 16/8/5.
 */
var path = require('path');
var webpack = require('webpack');
module.exports = {
    entry: [
        'webpack-dev-server/client?http://127.0.0.1:9091',
        'webpack/hot/dev-server',
        './src/entry/index.js'
    ],
    output: {
        path: path.join(__dirname, 'www'),
        filename: 'dist/bundle.js',
        publicPath: '/www/'
    },
    resolve: {
        extensions: ['', '.js', '.jsx', '.css', '.less']
    },
    module: {
        loaders: [
            {test: /\.js$/, loader: 'babel?presets[]=react,presets[]=es2015'},
            {test: /\.jsx?$/, loader: 'babel?presets[]=react,presets[]=es2015'},
            {test: /\.css$/, loader: 'style!css'},
            {test: /\.(png|jpe?g|eot|svg|ttf|woff2?)$/, loader: "file?name=images/[name].[ext]"}
        ]
    },
    babel: {
        //使用antd默认样式
        plugins: [["antd", {style: "css"}]]
    },
    plugins: [
        new webpack.NoErrorsPlugin(),               //允许错误不打断程序
        new webpack.HotModuleReplacementPlugin()　　//webpack热替换插件
    ]
};