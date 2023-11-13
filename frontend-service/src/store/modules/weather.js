import axios from "axios";

const WEATHER_API_URL = process.env.VUE_APP_GATEWAY_SERVICE_URL + '/weather'

const state = {
  weatherData: [],
}

const mutations = {
  setWeatherData(state, {data}){
    state.weatherData = data;
  },
}

const actions = {
  getWeatherData({commit}) {
    axios.get(WEATHER_API_URL).then((response) => {
      commit('setWeatherData', {data: response.data})
    })
  },
}

const getters = {
  weatherData(state) {
    return state.weatherData;
  },
}

export default {
  state,
  getters,
  actions,
  mutations
}
