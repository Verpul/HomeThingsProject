import axios from "axios";

const YOUTUBE_API_URL = process.env.VUE_APP_GATEWAY_SERVICE_URL + '/youtube'

const state = {
  followedChannels: [],
  savedChannels: []
}

const mutations = {
  setFollowedYoutubeChannels(state, {data}) {
    state.followedChannels = data;
  },
  setSavedYoutubeChannels(state, {data}) {
    state.savedChannels = data;
  }
}

const actions = {
  loadYoutubeFollowedChannels({commit}) {
    axios.get(`${YOUTUBE_API_URL}/follow`).then((response) => {
      commit('setFollowedYoutubeChannels', {data: response.data});
    })
  },
  loadSavedYoutubeChannels({commit}) {
    axios.get(`${YOUTUBE_API_URL}`).then((response) => {
      commit('setSavedYoutubeChannels', {data: response.data})
    })
  },
  setYoutubeChannelPreferences({dispatch}, data) {
    axios.post(`${YOUTUBE_API_URL}`, data).then(() => {
      dispatch("loadSavedYoutubeChannels");
    })
  }
}

const getters = {
  followedYoutubeChannels(state) {
    return state.followedChannels;
  },
  savedYoutubeChannels(state) {
    return state.savedChannels;
  }
}

export default {
  state,
  getters,
  actions,
  mutations
}
