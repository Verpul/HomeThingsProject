import axios from "axios";

const TWITCH_API_URL = process.env.VUE_APP_GATEWAY_SERVICE_URL + '/twitch'

const state = {
  authorized: true,
  followedChannels: [],
  savedChannels: []
}

const mutations = {
  setAuthorized(state, {data}) {
    state.authorized = data;
  },
  setFollowedChannels(state, {data}) {
    state.followedChannels = data;
  },
  setSavedTwitchChannels(state, {data}) {
    state.savedChannels = data;
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
  },
  loadFollowedChannels({commit}) {
    axios.get(`${TWITCH_API_URL}/channel/follow`).then((response) => {
      commit('setFollowedChannels', {data: response.data});
    })
  },
  loadSavedTwitchChannels({commit}) {
    axios.get((`${TWITCH_API_URL}/channel`)).then((response) => {
      commit('setSavedTwitchChannels', {data: response.data})
    })
  },
  setChannelPreferences({dispatch}, data) {
    axios.post(`${TWITCH_API_URL}/channel`, data).then(() => {
      dispatch("loadSavedTwitchChannels");
    })
  }
}

const getters = {
  twitchAuthorized(state) {
    return state.authorized;
  },
  followedChannels(state) {
    return state.followedChannels;
  },
  savedChannels(state) {
    return state.savedChannels;
  }
}

export default {
  state,
  getters,
  actions,
  mutations
}
