import axios from "axios";

const CURRENCY_API_URL = process.env.VUE_APP_GATEWAY_SERVICE_URL + '/currency';

const state = {
  currencyRecords: null,
  currencyAmountData: null,
  currencyRecordErrors: []
}

const mutations = {
  setCurrencyRecords(state, {data}) {
    state.currencyRecords = data;
  },
  setCurrencyAmountData(state, {data}) {
    state.currencyAmountData = data;
  },
  setCurrencyRecordErrors(state, {errors}) {
    state.currencyRecordErrors = errors;
  }
}

const actions = {
  getCurrencyRecords({commit}) {
    axios.get(`${CURRENCY_API_URL}`).then((response) => {
      commit('setCurrencyRecords', {data: response.data});
    });
  },
  async saveCurrencyRecord({dispatch, commit}, data) {
    await axios.post(`${CURRENCY_API_URL}`, data).then(() => {
      dispatch('getCurrencyRecords');
      dispatch('calculateCurrency');
    }).catch((errors) => {
      commit('setCurrencyRecordErrors', {errors: errors.response.data})
    })
  },
  async updateCurrencyRecord({dispatch, commit}, data) {
    await axios.put(`${CURRENCY_API_URL}/${data.id}`, data).then(() => {
      dispatch('getCurrencyRecords');
      dispatch('calculateCurrency');
    }).catch((errors) => {
      commit('setCurrencyRecordErrors', {errors: errors.response.data})
    })
  },
  deleteCurrencyRecord({dispatch}, id) {
    axios.delete(`${CURRENCY_API_URL}/${id}`).then(() => {
      dispatch('getCurrencyRecords');
      dispatch('calculateCurrency');
    })
  },
  calculateCurrency({commit}) {
    axios.get(`${CURRENCY_API_URL}/calculate`).then((response) => {
      commit('setCurrencyAmountData', {data: response.data})
    })
  }
}

const getters = {
  currencyRecords(state) {
    return state.currencyRecords;
  },
  currencyAmountData(state) {
    return state.currencyAmountData;
  },
  currencyRecordErrors(state) {
    return state.currencyRecordErrors;
  }
}

export default {
  state,
  getters,
  actions,
  mutations
}