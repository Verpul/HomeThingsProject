<template>
  <v-card outlined class="mt-2" v-if="currencyAmountData !== null">
    <v-card-title>
      <v-icon>mdi mdi-piggy-bank-outline</v-icon>
      <span class="ms-2">В наличии</span>
    </v-card-title>
    <v-card-text>
      <v-simple-table dense class="text-center">
        <thead>
        <th></th>
        <th>Всего</th>
        <th>Куплено на</th>
        <th>Продано на</th>
        <th>Продажа по курсу Тинькофф</th>
        <th>Разница</th>
        </thead>
        <tbody>
        <tr>
          <td>USD</td>
          <td>{{ currencyAmountData.USD.amount }}</td>
          <td>{{ currencyAmountData.USD.rublesSpentOn }}</td>
          <td>{{ currencyAmountData.USD.rublesEarnedFrom }}</td>
          <td>{{ currencyAmountData.USD.sellPrice }}</td>
          <td>{{ currencyAmountData.USD.difference }}</td>
        </tr>
        <tr>
          <td>EUR</td>
          <td>{{ currencyAmountData.EUR.amount  }}</td>
          <td>{{ currencyAmountData.EUR.rublesSpentOn }}</td>
          <td>{{ currencyAmountData.EUR.rublesEarnedFrom }}</td>
          <td>{{ currencyAmountData.EUR.sellPrice }}</td>
          <td>{{ currencyAmountData.EUR.difference }}</td>
        </tr>
        <tr>
          <td>Всего</td>
          <td>{{ currencyAmountData.USD.amount + currencyAmountData.EUR.amount }}</td>
          <td>{{ currencyAmountData.USD.rublesSpentOn + currencyAmountData.EUR.rublesSpentOn }}</td>
          <td>{{ currencyAmountData.USD.rublesEarnedFrom + currencyAmountData.EUR.rublesEarnedFrom }}</td>
          <td>{{ currencyAmountData.USD.sellPrice + currencyAmountData.EUR.sellPrice }}</td>
          <td>{{ currencyAmountData.USD.difference + currencyAmountData.EUR.difference }}</td>
        </tr>
        </tbody>
      </v-simple-table>
    </v-card-text>
  </v-card>
</template>

<script>
export default {
  name: "CurrencyAmountComponent",
  computed: {
    currencyAmountData() {
      return this.$store.getters.currencyAmountData;
    },
  },
  created() {
    this.$store.dispatch("calculateCurrency");
  }
}
</script>
