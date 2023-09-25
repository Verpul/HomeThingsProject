import axios from "axios"

const CURRENCIES_RATE_API_URL = process.env.VUE_APP_API_SERVICE_URL + '/rates'

class CurrenciesRateService {

  getCBRatesData() {
    return axios.get(CURRENCIES_RATE_API_URL + "/cb");
  }

  getTinkoffRatesData() {
    return axios.get(CURRENCIES_RATE_API_URL + "/tinkoff");
  }
}

export default new CurrenciesRateService()
