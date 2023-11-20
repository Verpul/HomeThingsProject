import axios from "axios";

const VK_API_URL = process.env.VUE_APP_GATEWAY_SERVICE_URL + '/vk'

const state = {
  haveToken: false,
  newsfeed: [],
  newsFilter: null,
  showHidden: false
}

const mutations = {
  setHaveTokenVK(state, {data}) {
    state.haveToken = data;
  },
  setNewsfeed(state, {data}) {
    state.newsfeed = data;
  },
  setNewsFilter(state, {data}) {
    state.newsFilter = data;
  },
  switchShowHiddenPosts(state) {
    state.showHidden = !state.showHidden;
  }
}

const actions = {
  haveTokenVK({commit}) {
    axios.get(`${VK_API_URL}/token`).then((response) => {
      commit('setHaveTokenVK', {data: response.data})
    })
  },
  loadNewsfeed({commit}, data) {
    axios.get(`${VK_API_URL}/newsfeed`, {
      params: data
    }).then((response) => {
      commit('setNewsfeed', {data: response.data})
    })
  },
  async getLastNewsCheckDate() {
    return await axios.get(`${VK_API_URL}/date`);
  },
  loadVKFilter({commit}) {
     axios.get(`${VK_API_URL}/filter`).then((response) => {
      commit('setNewsFilter', {data: response.data})
    });
  },
  saveVKFilter({dispatch, state}) {
    axios.post(`${VK_API_URL}/filter`, {filter: state.newsFilter}).then(() => {
      dispatch("loadVKFilter");
    });
  },
}

const getters = {
  vkHaveToken(state) {
    return state.haveToken;
  },
  vkNewsfeed(state) {
    return state.newsfeed;
  },
  vkNewsFilter(state) {
    return state.newsFilter;
  },
  showHiddenVKPosts(state) {
    return state.showHidden;
  }
}

export default {
  state,
  getters,
  actions,
  mutations
}
