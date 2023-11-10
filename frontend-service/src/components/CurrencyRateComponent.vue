<template>
  <v-card outlined v-if="tinkoffRates !== null && cbRates !== null">
    <v-card-title>
      <v-icon>mdi-currency-usd</v-icon>
      <span class="ms-2">Курсы валют
        <span class="text-body-2 text--disabled">(Обновлено: ЦБ - {{ cbRates.currenciesLastUpdateTime }},
          Тинькофф - {{ tinkoffRates.currenciesLastUpdateTime }})</span>
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
          <td>{{ cbRates.currenciesRate.USD }}</td>
          <td>USD</td>
          <td>{{ tinkoffRates.currenciesRate.USD.buy }}</td>
          <td>{{ tinkoffRates.currenciesRate.USD.sell }}</td>
        </tr>
        <tr>
          <td>EUR</td>
          <td>{{ cbRates.currenciesRate.EUR }}</td>
          <td>EUR</td>
          <td>{{ tinkoffRates.currenciesRate.EUR.buy }}</td>
          <td>{{ tinkoffRates.currenciesRate.EUR.sell }}</td>
        </tr>
        </tbody>
      </v-simple-table>
    </v-card-text>
  </v-card>
</template>

<script>
export default {
  name: "CurrencyComponent",
  computed: {
    cbRates() {
      return this.$store.getters.cbRates;
    },
    tinkoffRates() {
      return this.$store.getters.tinkoffRates;
    }
  },
  created() {
    this.$store.dispatch('getRates');
  }
}
</script>
