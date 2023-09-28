import Vue from 'vue'
import Vuex from 'vuex'
import reminder from './modules/reminder'

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    reminder,
  }
})