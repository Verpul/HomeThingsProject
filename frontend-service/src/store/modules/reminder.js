import axios from "axios";

const REMINDER_API_URL = process.env.VUE_APP_LOCAL_SERVICE_URL + '/reminder'

const state = {
  reminders: [],
}

const mutations = {
  setReminders(state, {data}){
    state.reminders = data;
  },
}

const actions = {
  getAllReminders({commit}) {
    axios.get(REMINDER_API_URL).then((response) => {
      commit('setReminders', {data: response.data})
    })
  },
  saveNewReminder({dispatch}, data) {
    axios.post(REMINDER_API_URL, data).then(() => {
      dispatch('getAllReminders')
    })
  },
  updateReminder({dispatch}, data) {
    axios.put(`${REMINDER_API_URL}/${data.id}`, data).then(() => {
      dispatch('getAllReminders');
    })
  },
  deleteReminder({dispatch}, id) {
    axios.delete(`${REMINDER_API_URL}/${id}`).then(() => {
      dispatch('getAllReminders')
    })
  }
}

const getters = {
  reminders(state) {
    return state.reminders;
  }
}

export default {
  state,
  getters,
  actions,
  mutations
}
