import axios from "axios";

const CURRENCIES_RATE_API_URL = process.env.VUE_APP_GATEWAY_SERVICE_URL + '/rates';

const state = {
  cbRates: null,
  tinkoffRates: null
}

const mutations = {
  setCBRates(state, {data}) {
    state.cbRates = data;
  },
  setTinkoffRates(state, {data}) {
    state.tinkoffRates = data;
  },
}

const actions = {
  getRates({commit}) {
    axios.get(`${CURRENCIES_RATE_API_URL}/cb`).then((response) => {
      commit('setCBRates', {data: response.data})
    });
    axios.get(`${CURRENCIES_RATE_API_URL}/tinkoff`).then((response) => {
      commit('setTinkoffRates', {data: response.data})
    });
  },
}

const getters = {
  cbRates(state) {
    return state.cbRates;
  },
  tinkoffRates(state) {
    return state.tinkoffRates;
  },
}

export default {
  state,
  getters,
  actions,
  mutations
}
