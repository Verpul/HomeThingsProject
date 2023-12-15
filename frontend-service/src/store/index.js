import Vue from 'vue'
import Vuex from 'vuex'
import reminder from './modules/reminder'
import reminderCategory from "@/store/modules/reminderCategory";
import weight from "@/store/modules/weight";
import twitch from "@/store/modules/twitch";
import currencyRates from "@/store/modules/currencyRates";
import currency from "@/store/modules/currency";
import weather from "@/store/modules/weather";
import vk from "@/store/modules/vk";
import notesCategories from "@/store/modules/notesCategories";
import notes from "@/store/modules/notes";
import youtube from "@/store/modules/youtube";

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    reminder,
    reminderCategory,
    weight,
    twitch,
    currencyRates,
    currency,
    weather,
    vk,
    notesCategories,
    notes,
    youtube
  }
})