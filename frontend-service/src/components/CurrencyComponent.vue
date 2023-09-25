<template>
  <v-card outlined v-if="TinkoffCurrenciesRateData !== null && CBCurrenciesRateData !== null">
    <v-card-title>
      <v-icon>mdi-currency-usd</v-icon>
      <span class="ms-2">Курсы валют
        <span class="text-body-2 text--disabled">(Обновлено: ЦБ - {{ CBCurrenciesRateData.currenciesLastUpdateTime }},
          Тинькофф - {{ TinkoffCurrenciesRateData.currenciesLastUpdateTime }})</span>
      </span>
    </v-card-title>
    <v-card-text>
      <v-simple-table dense class="text-center">
        <thead>
        <th>Валюта</th>
        <th>ЦБ</th>
        <th>Тинькофф</th>
        <th>Продажа</th>
        <th>Покупка</th>
        </thead>
        <tbody>
        <tr>
          <td>USD</td>
          <td>{{ CBCurrenciesRateData.currenciesRate.USD }}</td>
          <td>USD</td>
          <td>{{ TinkoffCurrenciesRateData.currenciesRate.USD.buy }}</td>
          <td>{{ TinkoffCurrenciesRateData.currenciesRate.USD.sell }}</td>
        </tr>
        <tr>
          <td>EUR</td>
          <td>{{ CBCurrenciesRateData.currenciesRate.EUR }}</td>
          <td>EUR</td>
          <td>{{ TinkoffCurrenciesRateData.currenciesRate.EUR.buy }}</td>
          <td>{{ TinkoffCurrenciesRateData.currenciesRate.EUR.sell }}</td>
        </tr>
        </tbody>
      </v-simple-table>
    </v-card-text>
  </v-card>
</template>

<script>
import CurrenciesRateService from "@/service/CurrenciesRateService";

export default {
  name: "CurrencyComponent",
  data() {
    return {
      CBCurrenciesRateData: null,
      TinkoffCurrenciesRateData: null
    }
  },
  methods: {
    loadCurrenciesRateData() {
      CurrenciesRateService.getCBRatesData().then((response) => {
        this.CBCurrenciesRateData = response.data;
      });

      CurrenciesRateService.getTinkoffRatesData().then((response) => {
        this.TinkoffCurrenciesRateData = response.data;
      });
    }
  },
  created() {

    this.loadCurrenciesRateData();
  }
}
</script>
