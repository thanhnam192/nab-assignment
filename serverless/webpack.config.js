const fs = require('fs');
const path = require('path');
const ZipPlugin = require('zip-webpack-plugin');
var HardSourceWebpackPlugin = require('hard-source-webpack-plugin');

module.exports = function (env) {
    const lambdaFunctionDir = path.join(__dirname, 'src', 'lambdas');
    const functionsToBuild = env && env.fxn ? env.fxn.split(",") : fs.readdirSync(lambdaFunctionDir).filter(item => fs.lstatSync(path.join(lambdaFunctionDir, item)).isDirectory() && !item.match(/^\./));
    console.log(`Building ${functionsToBuild.join(", ")}`);

    return functionsToBuild
        .map(fxn => ({
            mode: 'production',
            context: path.resolve(__dirname),
            entry: path.join(lambdaFunctionDir, fxn, 'index.js'),
            output: {
                path: path.join(__dirname, 'dist', fxn),
                filename: 'index.js',
                libraryTarget: 'commonjs2'
            },
            module: {
                rules: [
                    {
                        test: /\.js$/,
                        exclude: /node_module/,
                        use: [
                            {
                                loader: 'babel-loader',
                                options: {
                                    presets: [['@babel/env', {targets: {node: '8.10'}}]],
                                    plugins: [],
                                    compact: false,
                                    babelrc: false,
                                    cacheDirectory: true
                                }
                            }
                        ]
                    },
                    {
                        test: /\.jpe?g$|\.gif$|\.png$|\.svg$|\.woff$|\.ttf$|\.wav$|\.mp3$/,
                        use: [
                            'file-loader'
                        ]
                    }
                ]
            },
            resolve: {
                extensions: ['.ts', '.tsx', '.js']
            },
            optimization: {
                minimize: false,
                namedModules: true
            },
            plugins: [
                new ZipPlugin({
                    path: path.join(__dirname, 'dist', fxn),
                    pathPrefix: '',
                    filename: `${fxn}.zip`
                }),
                new HardSourceWebpackPlugin()
            ],
            target: 'node',
            externals: {
                // These modules are already installed on the Lambda instance.
                'aws-sdk': 'aws-sdk',
                'awslambda': 'awslambda',
                'dynamodb-doc': 'dynamodb-doc',
                'imagemagick': 'imagemagick'
            },
            node: {
                // Allow these globals.
                __filename: false,
                __dirname: false
            },
            stats: 'errors-only',
            bail: true
        }));
};