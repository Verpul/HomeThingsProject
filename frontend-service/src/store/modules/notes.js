import axios from "axios";

const NOTES_API_URL = process.env.VUE_APP_GATEWAY_SERVICE_URL + '/note/'

const state = {
  notes: [],
}

const mutations = {
  setNotes(state, {data}){
    state.notes = data;
  },
}

const actions = {
  getNotes({commit}, categoryId) {
    axios.get(`${NOTES_API_URL}/${categoryId}`).then((response) => {
      commit('setNotes', {data: response.data})
    })
  },
  createNote({dispatch}, data) {
    axios.post(NOTES_API_URL, data).then(() => {
      dispatch('getNotes', data.category.id)
    });
  },
  updateNote({dispatch}, data) {
    axios.put(`${NOTES_API_URL}/${data.id}`, data).then(() => {
      dispatch('getNotes', data.category.id);
    });
  },
  deleteNote({dispatch}, data) {
    axios.delete(`${NOTES_API_URL}/${data.noteId}`).then(() => {
      dispatch('getNotes', data.category.id)
    })
  },
}

const getters = {
  notes(state) {
    return state.notes;
  },
}

export default {
  state,
  getters,
  actions,
  mutations
}
