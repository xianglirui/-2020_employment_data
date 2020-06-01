<<<<<<< HEAD
const path = require('path');

function resolve(dir) {
  return path.join(__dirname, dir);
}
module.exports = {
  lintOnSave: true,
  chainWebpack: (config) => {
    config.resolve.alias
      .set('@', resolve('src'))
      .set('assets',resolve('src/assets'))
      .set('components',resolve('src/components'))
    config.module
      .rule('md')
      .test(/\.md$/)
      // .use('html-loader')
      // .loader('html-loader')
      // .end()
      // .use('markdown-loader')
      // .loader('markdown-loader')
      // .end()
        .use('vue-loader')
        .loader('vue-loader')
        .end()
        .use('vue-markdown-loader')
        .loader('vue-markdown-loader/lib/markdown-compiler')
        .options({
          raw: true
        })
    // config.module
    //     .rule('css')
    //     .test(/\.css$/)
    //     .use('vue-loader')
    //     .loader('vue-loader')
    //     .end()
    //     .use('css-loader')
    //     .loader('css-loader')
    //     .end()
  },
  // configureWebpack: config => {
  //   config.module.rules.push({
  //         test: /\.md$/,
  //         use: [
  //           {
  //             loader: 'vue-loader',
  //           },
  //           // {loader:'html-loader'},
  //           // {loader:'markdown-loader'},
  //           {
  //             loader: require.resolve('./src/assets/js/markdown-loader'),
  //           },
  //         ],
  //       },
  //   );
  // },

};
=======
const path = require('path');

function resolve(dir) {
  return path.join(__dirname, dir);
}
module.exports = {
  lintOnSave: true,
  chainWebpack: (config) => {
    config.resolve.alias
      .set('@', resolve('src'))
      .set('assets',resolve('src/assets'))
      .set('components',resolve('src/components'))
    config.module
      .rule('md')
      .test(/\.md$/)
      // .use('html-loader')
      // .loader('html-loader')
      // .end()
      // .use('markdown-loader')
      // .loader('markdown-loader')
      // .end()
        .use('vue-loader')
        .loader('vue-loader')
        .end()
        .use('vue-markdown-loader')
        .loader('vue-markdown-loader/lib/markdown-compiler')
        .options({
          raw: true
        })
    // config.module
    //     .rule('css')
    //     .test(/\.css$/)
    //     .use('vue-loader')
    //     .loader('vue-loader')
    //     .end()
    //     .use('css-loader')
    //     .loader('css-loader')
    //     .end()
  },
  // configureWebpack: config => {
  //   config.module.rules.push({
  //         test: /\.md$/,
  //         use: [
  //           {
  //             loader: 'vue-loader',
  //           },
  //           // {loader:'html-loader'},
  //           // {loader:'markdown-loader'},
  //           {
  //             loader: require.resolve('./src/assets/js/markdown-loader'),
  //           },
  //         ],
  //       },
  //   );
  // },

};
>>>>>>> 3c581657509a8b54954ba628dd5f310367a51d19
