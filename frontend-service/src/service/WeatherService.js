import axios from "axios"

const WEIGHT_API_URL = 'http://localhost:8082/api/weather'

class WeatherService {

  getWeatherData() {
    return axios.get(WEIGHT_API_URL);
  }
}

export default new WeatherService()
