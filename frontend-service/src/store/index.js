import Vue from 'vue'
import Vuex from 'vuex'
import reminder from './modules/reminder'
import reminderCategory from "@/store/modules/reminderCategory";
import weight from "@/store/modules/weight";

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    reminder,
    reminderCategory,
    weight
  }
})