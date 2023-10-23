import axios from "axios"

const WEIGHT_API_URL = process.env.VUE_APP_GATEWAY_SERVICE_URL + '/weight'

class WeightService {

  getAllRecords() {
    return axios.get(WEIGHT_API_URL);
  }

  deleteRecord(id) {
    return axios.delete(WEIGHT_API_URL + '/' + id);
  }

  updateRecord(id, record) {
    return axios.put(`${WEIGHT_API_URL}/${id}`, record);
  }

  createRecord(record) {
    return axios.post(`${WEIGHT_API_URL}`, record);
  }
}

export default new WeightService()
