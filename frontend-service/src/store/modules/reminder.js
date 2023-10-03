import axios from "axios";

const REMINDER_API_URL = process.env.VUE_APP_LOCAL_SERVICE_URL + '/reminder'

const state = {
  reminders: [],
  reminderErrors: [],
  categoryId: null
}

const mutations = {
  setReminders(state, {data}){
    state.reminders = data;
  },
  setReminderErrors(state, {errors}) {
    state.reminderErrors = errors
  },
  setCategoryId(state, categoryId) {
    state.categoryId = categoryId;
  }
}

const actions = {
  getAllReminders({commit, state}) {
    axios.get(REMINDER_API_URL, {
      params: {
        categoryId: state.categoryId
      }
    }).then((response) => {
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
  async updateReminder({dispatch, commit}, data) {
    await axios.put(`${REMINDER_API_URL}/${data.id}`, data).then(() => {
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
  },
  currentCategoryId(state) {
    return state.categoryId;
  }
}

export default {
  state,
  getters,
  actions,
  mutations
}
