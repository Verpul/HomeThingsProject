const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
  transpileDependencies: [
    'vuetify', '@peepi/vuetify-tiptap'
  ],
  devServer: {
    port: 8080
  },
})
