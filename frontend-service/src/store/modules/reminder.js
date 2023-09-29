import axios from "axios";

const REMINDER_API_URL = process.env.VUE_APP_LOCAL_SERVICE_URL + '/reminder'

const state = {
  reminders: [],
  reminderErrors: [],
}

const mutations = {
  setReminders(state, {data}){
    state.reminders = data;
  },
  setReminderErrors(state, {errors}) {
    state.reminderErrors = errors
  }
}

const actions = {
  getAllReminders({commit}) {
    axios.get(REMINDER_API_URL).then((response) => {
      commit('setReminders', {data: response.data})
    })
  },
  async saveNewReminder({dispatch, commit}, data) {
    await axios.post(REMINDER_API_URL, data).then(() => {
      dispatch('getAllReminders')
    }).catch((errors) => {
      commit('setReminderErrors', {errors: errors.response.data})
    })
  },
  updateReminder({dispatch, commit}, data) {
    axios.put(`${REMINDER_API_URL}/${data.id}`, data).then(() => {
      dispatch('getAllReminders');
    }).catch((errors) => {
      commit('setReminderErrors', {errors: errors.response.data})
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
  },
  reminderErrors(state) {
    return state.reminderErrors;
  }
}

export default {
  state,
  getters,
  actions,
  mutations
}
