import Vue from 'vue'
import App from './App.vue'
import router from './router'
import vuetify from './plugins/vuetify'
import store from "./store"
import Keycloak from "keycloak-js";
import setHeader from "@/service/HeadService";
import '@/globalComponents'

Vue.config.productionTip = false

let initOptions = {
  url: process.env.VUE_APP_KEYCLOAK_URL,
  realm: process.env.VUE_APP_KEYCLOAK_REALM,
  clientId: process.env.VUE_APP_CLIENT_ID,
  onLoad: process.env.VUE_APP_ON_LOAD
}

let keycloak = new Keycloak(initOptions);

keycloak.init({onLoad: initOptions.onLoad}).then((auth) => {
  if (!auth) {
    window.location.reload();
  } else {
    setHeader(keycloak.token);

    new Vue({
      router,
      vuetify,
      store,
      render: h => h(App)
    }).$mount('#app');

    //For logout
    Vue.prototype.$keycloak = keycloak
  }

  //Token refresh
  setInterval(() => {
    keycloak.updateToken(1200).then((refreshed) => {
      if (refreshed) setHeader(keycloak.token);
    }).catch(() => {
      console.log('Failed to refresh token');
    });
  }, 60000)

}).catch(() => {
  console.log("Authenticated Failed");
});

// new Vue({
//   router,
//   vuetify,
//   store,
//   render: h => h(App)
// }).$mount('#app')
