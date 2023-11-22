import axios from "axios";

const WEIGHT_API_URL = process.env.VUE_APP_GATEWAY_SERVICE_URL + '/weight'

const state = {
  weightRecords: [],
  weightRecordsErrors: [],
  fileUploadError: null
}

const mutations = {
  setWeightRecords(state, {data}){
    state.weightRecords = data;
  },
  setWeightRecordsErrors(state, {errors}) {
    state.weightRecordsErrors = errors
  },
  setFileUploadError(state, {error}) {
    state.fileUploadError = error;
  }
}

const actions = {
  getAllRecords({commit}) {
    axios.get(WEIGHT_API_URL).then((response) => {
      commit('setWeightRecords', {data: response.data})
    })
  },
  async createRecord({dispatch, commit}, data) {
    await axios.post(WEIGHT_API_URL, data).then(() => {
      dispatch('getAllRecords')
    }).catch((errors) => {
      commit('setWeightRecordsErrors', {errors: errors.response.data})
    })
  },
  async updateRecord({dispatch, commit}, data) {
    await axios.put(`${WEIGHT_API_URL}/${data.id}`, data).then(() => {
      dispatch('getAllRecords');
    }).catch((errors) => {
      commit('setWeightRecordsErrors', {errors: errors.response.data})
    })
  },
  deleteRecord({dispatch}, id) {
    axios.delete(`${WEIGHT_API_URL}/${id}`).then(() => {
      dispatch('getAllRecords')
    })
  },
  async uploadFile({dispatch, commit}, data) {
    await axios.post(`${WEIGHT_API_URL}/upload`, data).then(() => {
      dispatch('getAllRecords');
    }).catch((error) => {
      commit('setFileUploadError', {error: error.response.data})
    })
  },
  downloadWeightRecordsData(context, type) {
    axios.get(`${WEIGHT_API_URL}/download/${type}`, {
      responseType: 'blob'
    }).then((response) => {
      const link = document.createElement('a');
      link.href = window.URL.createObjectURL(
          new Blob([response.data])
      );

      link.setAttribute('download', 'WeightRecords.' + type);
      document.body.appendChild(link);
      link.click();
    });
  }
}

const getters = {
  weightRecords(state) {
    return state.weightRecords;
  },
  weightRecordsErrors(state) {
    return state.weightRecordsErrors;
  },
  fileUploadError(state) {
    return state.fileUploadError;
  }
}

export default {
  state,
  getters,
  actions,
  mutations
}
