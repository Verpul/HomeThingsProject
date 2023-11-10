<template>
  <v-app>
    <MainMenu/>
    <v-main>
      <v-container fluid>
        <v-card width="1100" class="mx-auto" flat>
              <CurrencyRateComponent />
              <CurrencyCountComponent />
          <v-simple-table class="mt-3">
            <thead>
              <tr>
                <th style="width: 15%">Дата обмена</th>
                <th style="width: 15%">Валюта из</th>
                <th style="width: 15%">Количество</th>
                <th style="width: 15%">Валюта в</th>
                <th style="width: 15%">Количество</th>
                <th style="width: 10%">Курс</th>
                <th style="width: 15%"></th>
              </tr>
            </thead>
            <tbody>
            <tr>
              <td>
                <v-text-field dense type="date" v-model="record.exchangeDate"
                              :error-messages="currencyRecordErrors.exchangeDate"></v-text-field>
              </td>
              <td>
                <v-select dense :items="currencies" v-model="record.currencyFrom"
                          :error-messages="currencyRecordErrors.currencyFrom">></v-select>
              </td>
              <td>
                <v-text-field dense type="number" v-model="record.currencyFromAmount"
                              :error-messages="currencyRecordErrors.currencyFromAmount">></v-text-field>
              </td>
              <td>
                <v-select dense :items="currencies" v-model="record.currencyTo"
                          :error-messages="currencyRecordErrors.currencyTo">></v-select>
              </td>
              <td>
                <v-text-field dense type="number" v-model="record.currencyToAmount"
                              :error-messages="currencyRecordErrors.currencyToAmount">></v-text-field>
              </td>
              <td>
                <v-text-field dense type="number" v-model="record.rate"
                              :error-messages="currencyRecordErrors.rate">></v-text-field>
              </td>
              <td>
                <div class="d-inline-block" style="white-space: nowrap">
                  <v-btn density="compact" icon @click="saveRecord()">
                    <v-icon color="success">{{ record.id ? 'mdi mdi-check-circle-outline' : 'mdi mdi-plus-box-outline' }}
                    </v-icon>
                  </v-btn>
                  <v-btn density="compact" @click="clearFields" v-if="record.id"
                         class="ms-3" icon>
                    <v-icon color="primary">mdi-close-circle-outline</v-icon>
                  </v-btn>
                </div>
              </td>
            </tr>
            <tr v-for="record in currencyRecords" :key="record.id">
              <td>{{ formatDate(record.exchangeDate) }}</td>
              <td>{{ record.currencyFrom }}</td>
              <td>{{ record.currencyFromAmount }}</td>
              <td>{{ record.currencyTo }}</td>
              <td>{{ record.currencyToAmount }}</td>
              <td>{{ record.rate }}</td>
              <td>
                <div class="d-inline-block" style="white-space: nowrap">
                  <v-btn density="compact" @click="setEditMode(record)" icon>
                    <v-icon color="warning">mdi-square-edit-outline</v-icon>
                  </v-btn>
                  <v-btn density="compact" @click="deleteRecord(record.id)" class="ms-3"
                         icon>
                    <v-icon color="error">mdi-delete-outline</v-icon>
                  </v-btn>
                </div>
              </td>
            </tr>
            </tbody>
          </v-simple-table>
        </v-card>
      </v-container>
    </v-main>
  </v-app>
</template>

<script>
import CurrencyRateComponent from "@/components/CurrencyRateComponent";
import CurrencyCountComponent from "@/components/CurrencyAmountComponent";

export default {
  name: "CurrencyComponent",
  data: () => ({
    currencies: ["RUB", "EUR", "USD"],
    record: {
      id: null,
      currencyFrom: null,
      currencyFromAmount: null,
      currencyTo: null,
      currencyToAmount: null,
      rate: null,
      exchangeDate: null,
    }
  }),
  methods: {
    async saveRecord() {
      const action = this.record.id === null ? "saveCurrencyRecord" : "updateCurrencyRecord";

      this.$store.commit("setCurrencyRecordErrors", {errors: []});

      await this.$store.dispatch(action, {
        id: this.record.id,
        currencyFrom: this.record.currencyFrom,
        currencyFromAmount: this.record.currencyFromAmount,
        currencyTo: this.record.currencyTo,
        currencyToAmount: this.record.currencyToAmount,
        rate: this.record.rate,
        exchangeDate: this.record.exchangeDate
      })

      if (this.currencyRecordErrors.length === 0) {
        this.clearFields();
      }
    },
    clearFields() {
      this.record.id = null;
      this.record.currencyFrom = null;
      this.record.currencyFromAmount = null;
      this.record.currencyTo = null;
      this.record.currencyToAmount = null;
      this.record.rate = null;
      this.record.exchangeDate = null;
    },
    formatDate(date) {
      if (!date) return null;

      const [year, month, day] = date.split('-')
      return `${day}.${month}.${year}`
    },
    setEditMode(record) {
      this.record.id = record.id;
      this.record.currencyFrom = record.currencyFrom;
      this.record.currencyFromAmount = record.currencyFromAmount;
      this.record.currencyTo = record.currencyTo;
      this.record.currencyToAmount = record.currencyToAmount;
      this.record.rate = record.rate;
      this.record.exchangeDate = record.exchangeDate;
    },
    deleteRecord(recordId) {
      this.$store.dispatch('deleteCurrencyRecord', recordId);
      this.clearFields();
    }
  },
  computed: {
    currencyRecords() {
      return this.$store.getters.currencyRecords;
    },
    currencyRecordErrors() {
      return this.$store.getters.currencyRecordErrors;
    }
  },
  created() {
    this.$store.dispatch("getCurrencyRecords");
  },
  components: {
    CurrencyRateComponent, CurrencyCountComponent
  }
}
</script>
