import axios from "axios"

const WEATHER_API_URL = process.env.VUE_APP_GATEWAY_SERVICE_URL + '/weather'

class WeatherService {

  getWeatherData() {
    return axios.get(WEATHER_API_URL);
  }
}

export default new WeatherService()
