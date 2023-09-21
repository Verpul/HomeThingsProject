<template>
  <div>
    <v-row class="justify-center">
      <v-col class="col-5">
        <v-card outlined>
          <v-card-title>
            <v-icon cmall>mdi-clock-outline</v-icon>
            <span class="ms-2">Погода сейчас <span class="text-body-2 text--disabled">(Обновлено {{ weatherData.updatedDateTime }})</span></span>

          </v-card-title>
          <v-card-text v-if="weatherNow">
            <v-img :src="require('@/resources/images/weather/' + weatherNow.icon + '.png')"
                   alt="Image"
                   max-width="70" max-height="70" class="float-left"></v-img>
            <v-icon class="ms-3 mb-3">mdi-thermometer</v-icon>
            <span :class="getWeatherColor(weatherNow.temperature)"
                  class="text-h3 ms-1">{{ weatherNow.temperature + '°' }}</span>
            <span>Чувствуется как<span class="ms-1"
                                       :class="getWeatherColor(weatherNow.temperature)">{{
                weatherNow.feelsLike + '°'
              }}</span>
              <v-icon class="ms-3 mb-3">mdi-umbrella</v-icon>
              <span class="text-h3 blue--text text--darken-2 ms-1">{{ weatherNow.precipitationAverage }}</span>
              <span class="ms-1 blue--text text--darken-2">мм</span>
              <v-icon class="ms-3 mb-3">mdi-weather-windy</v-icon>
              <span class="text-h3 ms-1">{{ weatherNow.windAverage }}</span>
              <span class="ms-1">{{ '(' + weatherNow.windGusts + ') м/с' }}</span>
            </span>
          </v-card-text>
        </v-card>
      </v-col>
      <v-col class="col-5"></v-col>
    </v-row>

    <v-row class="justify-center">
      <v-simple-table class="col-10">
        <thead>
        <tr>
          <th class="col-2">
          </th>
          <th class="col-1">
            Ночь
          </th>
          <th class="col-1">
            Утро
          </th>
          <th class="col-1">
            День
          </th>
          <th class="col-1">
            Вечер
          </th>
          <th class="col-1">
            Макс/мин t°
          </th>
          <th class="col-1">
            Осадки
          </th>
          <th class="col-1">
            Ветер
          </th>
          <th class="col-2"></th>
        </tr>
        </thead>
        <tbody>
        <tr :style="{cursor : haveHourlyInfo(interval.start) ? 'pointer' : 'default'}"
            @click="showHourlyInfo(interval.start)"
            v-for="(interval, index) in weatherData.dayIntervals"
            :key="index"
        >
          <td>{{ interval.start }}</td>
          <td v-for="(symbol, index) in interval.sixHoursSymbols"
              :key="index"
          >
            <v-img v-if="symbol !== 'null'" :src="require('@/resources/images/weather/' + symbol + '.png')"
                   max-height="40" max-width="40"></v-img>
          </td>
          <td>
            <span
                :class="getWeatherColor(interval.temperatureMax)">{{
                interval.temperatureMax + '°/'
              }}</span>
            <span
                :class="getWeatherColor(interval.temperatureMin)">{{
                interval.temperatureMin + '°'
              }}</span>
          </td>
          <td><span v-if="interval.precipitation !== '0.0'"
                    class="blue--text text--darken-2">{{ interval.precipitation + ' мм' }}</span></td>
          <td>{{ interval.wind + ' м/с' }}</td>
          <td><span v-if="haveHourlyInfo(interval.start)">
            Подробный прогноз<v-icon x-large>mdi-chevron-right</v-icon>
          </span>
          </td>
        </tr>
        </tbody>
      </v-simple-table>
    </v-row>

    <v-dialog v-model="hourlyInfoModal" width="500">
      <v-card>
        <v-card-title>
          <span class="text-h5">{{ hourlyInfoDate }}</span>
        </v-card-title>
        <v-card-text>
          <v-simple-table class="col-3">
            <thead>
            <tr>
              <th>Время</th>
              <th>Погода</th>
              <th>t°</th>
              <th>Осадки мм</th>
              <th>Ветер(порывы)<br>м/с</th>
            </tr>
            </thead>
            <tbody>
            <tr
                v-for="(interval, index) in hourlyInfoData"
                :key="index"
            >
              <td>{{ interval.time }}</td>
              <td>
                <v-img :src="require('@/resources/images/weather/' + interval.icon + '.png')"
                       max-height="30" max-width="30"></v-img>
              </td>
              <td :class="getWeatherColor(interval.temperature)">
                {{ interval.temperature + '°' }}
              </td>
              <td class="blue--text text--darken-2">
            <span v-if="interval.precipitationMax > 0">
            {{ interval.precipitationMin + '-' + interval.precipitationMax }}
              </span>
              </td>
              <td>{{ interval.windAverage + ' (' + interval.windGusts + ')' }}</td>
            </tr>
            </tbody>
          </v-simple-table>
        </v-card-text>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import WeatherService from "@/service/WeatherService";

export default {
  name: "WeatherComponent",
  data() {
    return {
      weatherData: {},
      hourlyInfoModal: false,
      hourlyInfoDate: "",
      hourlyInfoData: [],
      weatherNow: null,
    }
  },
  methods: {
    loadData() {
      WeatherService.getWeatherData().then((res) => {
        this.weatherData = res.data;
        this.weatherNow = this.weatherData.shortIntervals[0];
      });
    },
    showHourlyInfo(date) {
      if (this.haveHourlyInfo(date)) {
        this.hourlyInfoDate = date;
        this.hourlyInfoModal = true;
        this.hourlyInfoData = this.weatherData.shortIntervals.filter(interval => interval.start === date);
      }
    },
    haveHourlyInfo(date) {
      return this.weatherData.shortIntervals.find(interval => interval.start === date);
    },
    getWeatherColor(temperature) {
      return temperature > 0 ? 'red--text text--darken-2' : 'blue--text text--darken-3';
    }
  },
  created() {
    this.loadData()
  }
}
</script>
