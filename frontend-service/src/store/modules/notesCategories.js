import axios from "axios";

const NOTES_CATEGORIES_API_URL = process.env.VUE_APP_GATEWAY_SERVICE_URL + '/note/category'

const state = {
  notesCategories: [],
  notesCategoriesErrors: []
}

const mutations = {
  setNotesCategories(state, {data}){
    state.notesCategories = data;
  },
  setNotesCategoriesErrors(state, {errors}) {
    state.notesCategoriesErrors = errors;
  }
}

const actions = {
  getNotesCategories({commit}) {
    axios.get(NOTES_CATEGORIES_API_URL).then((response) => {
      commit('setNotesCategories', {data: response.data})
    })
  },
  async createNoteCategory({dispatch, commit}, data) {
    await axios.post(NOTES_CATEGORIES_API_URL, data).then(() => {
      dispatch('getNotesCategories')
    }).catch((errors) => {
      commit('setNotesCategoriesErrors', {errors: errors.response.data})
    })
  },
  async updateNoteCategory({dispatch, commit}, data) {
    await axios.put(`${NOTES_CATEGORIES_API_URL}/${data.id}`, data).then(() => {
      dispatch('getNotesCategories');
    }).catch((errors) => {
      commit('setNotesCategoriesErrors', {errors: errors.response.data})
    })
  },
  deleteNoteCategory({dispatch}, id) {
    axios.delete(`${NOTES_CATEGORIES_API_URL}/${id}`).then(() => {
      dispatch('getNotesCategories')
    })
  },
}

const getters = {
  notesCategories(state) {
    return state.notesCategories;
  },
  notesCategoriesErrors(state) {
    return state.notesCategoriesErrors;
  }
}

export default {
  state,
  getters,
  actions,
  mutations
}
