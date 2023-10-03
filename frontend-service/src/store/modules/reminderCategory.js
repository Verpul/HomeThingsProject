import axios from "axios";

const REMINDER_CATEGORY_API_URL = process.env.VUE_APP_LOCAL_SERVICE_URL + '/reminder/category'

const state = {
  categories: [],
  reminderCategoryErrors: [],
}

const mutations = {
  setReminderCategories(state, {data}){
    state.categories = data;
  },
  setReminderCategoryErrors(state, {errors}) {
    state.reminderCategoryErrors = errors
  },
}

const actions = {
  getAllReminderCategories({commit}) {
    axios.get(REMINDER_CATEGORY_API_URL).then((response) => {
      commit('setReminderCategories', {data: response.data})
    })
  },
  async saveNewReminderCategory({dispatch, commit}, data) {
    await axios.post(REMINDER_CATEGORY_API_URL, data).then(() => {
      dispatch('getAllReminderCategories')
    }).catch((errors) => {
      commit('setReminderCategoryErrors', {errors: errors.response.data})
    })
  },
  async updateReminderCategory({dispatch, commit}, data) {
    await axios.put(`${REMINDER_CATEGORY_API_URL}/${data.id}`, data).then(() => {
      dispatch('getAllReminderCategories');
    }).catch((errors) => {
      commit('setReminderCategoryErrors', {errors: errors.response.data})
    })
  },
  async deleteReminderCategory({dispatch}, id) {
    return await axios.delete(`${REMINDER_CATEGORY_API_URL}/${id}`).then(() => {
      dispatch('getAllReminderCategories')
    }).catch((errors) => {
      return errors.response.data;
    })
  }
}

const getters = {
  reminderCategories(state) {
    return state.categories;
  },
  reminderCategoryErrors(state) {
    return state.reminderCategoryErrors;
  }
}

export default {
  state,
  getters,
  actions,
  mutations
}
