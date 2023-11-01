import axios from "axios";

const TWITCH_API_URL = process.env.VUE_APP_GATEWAY_SERVICE_URL + '/twitch'

const state = {
  authorized: false
}

const mutations = {
  setAuthorized(state, {data}) {
    state.authorized = data;
  }
}

const actions = {
  isAuthorized({commit}) {
    axios.get(`${TWITCH_API_URL}/token/authorized`).then((response) => {
      commit('setAuthorized', {data: response.data})
    })
  },
  getNewToken({dispatch}, code) {
    axios.post(`${TWITCH_API_URL}/token/${code}`).then(() => {
      dispatch('isAuthorized');
    })
  }
}

const getters = {
  twitchAuthorized(state) {
    return state.authorized;
  }
}

export default {
  state,
  getters,
  actions,
  mutations
}
