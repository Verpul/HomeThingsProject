import axios from "axios";

const REMINDER_API_URL = process.env.VUE_APP_GATEWAY_SERVICE_URL + '/reminder'

const state = {
  reminders: [],
  reminderErrors: [],
  categoryId: null,
  showCompeted: false
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
  },
  changeShowCompleted(state) {
    state.showCompeted = !state.showCompeted;
  }
}

const actions = {
  getAllReminders({commit, state}) {
    axios.get(REMINDER_API_URL, {
      params: {
        categoryId: state.categoryId,
        showCompleted: state.showCompeted
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
  },
  changeCompletedStatus({dispatch}, id) {
    axios.patch(`${REMINDER_API_URL}/${id}`).then(() => {
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
  },
  showCompleted(state) {
    return state.showCompeted;
  }
}

export default {
  state,
  getters,
  actions,
  mutations
}
