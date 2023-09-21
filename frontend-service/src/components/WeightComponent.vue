<template>
  <v-app>
    <MainMenu/>
    <v-main>
      <v-container fluid>
        <v-card width="600px" class="mx-auto"
                outlined>
          <v-form ref="form">
            <v-simple-table class="ma-5">
              <thead>
              <tr>
                <th class="text-left" style="width: 30%">
                  Дата
                </th>
                <th class="text-left" style="width: 30%">
                  Вес
                </th>
                <th class="text-left" style="width: 30%">
                  Изменение
                </th>
                <th class="text-left" style="width: 10%"></th>
              </tr>
              </thead>
              <tbody>
              <tr>

                <td style="vertical-align: top">
                  <v-text-field type="date" density="compact" variant="underlined" v-model="weightRecordDate"
                                :error-messages="errorMessages.weightRecordDate"></v-text-field>
                </td>
                <td style="vertical-align: top">
                  <v-text-field density="compact" variant="underlined" v-model="weightRecordValue"
                                placeholder=">50 и <99.9"
                                :error-messages="errorMessages.weightRecordValue"></v-text-field>
                </td>
                <td style="vertical-align: top"></td>
                <td>
                  <div class="d-inline-block" style="white-space: nowrap">
                    <v-btn density="compact" icon @click="submitRecord">
                      <v-icon color="success">{{ id ? 'mdi mdi-check-circle-outline' : 'mdi mdi-plus-box-outline' }}
                      </v-icon>
                    </v-btn>
                    <v-btn density="compact" @click="clearFields" v-if="id"
                           class="ms-3" icon>
                      <v-icon color="primary">mdi-close-circle-outline</v-icon>
                    </v-btn>
                  </div>
                </td>

              </tr>
              <tr
                  v-for="(record) in records"
                  :key="record.id"
              >
                <td>{{ record.formattedWeightRecordDate }}</td>
                <td>{{ record.weightRecordValue }}</td>
                <td :class="record.differencePreviousValue > 0 ? 'green--text': 'red--text'">{{
                    record.differencePreviousValue
                  }}
                </td>
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
          </v-form>
        </v-card>
      </v-container>
    </v-main>
  </v-app>
</template>

<script>
import WeightService from "@/service/WeightService";

export default {
  name: 'WeightComponent',
  data() {
    return {
      records: [],
      weightRecordDate: "",
      weightRecordValue: "",
      id: "",
      errorMessages: {}
    };
  },
  methods: {
    loadRecords() {
      WeightService.getAllRecords().then((res) => {
        this.records = res.data;
      });
    },
    submitRecord() {
      if (this.id === "") {
        WeightService.createRecord({
          weightRecordDate: this.weightRecordDate,
          weightRecordValue: this.weightRecordValue,
        }).then(() => {
          this.clearFields();
          this.loadRecords();
        }).catch((error) => {
          this.errorMessages = error.response.data;
        })
      } else {
        WeightService.updateRecord(this.id, {
          id: this.id,
          weightRecordDate: this.weightRecordDate,
          weightRecordValue: this.weightRecordValue,
        }).then(() => {
          this.clearFields();
          this.loadRecords();
        }).catch((error) => {
          this.errorMessages = error.response.data;
        })
      }
    },
    setEditMode(record) {
      this.weightRecordDate = record.weightRecordDate;
      this.weightRecordValue = record.weightRecordValue;
      this.id = record.id;
    },
    deleteRecord(id) {
      WeightService.deleteRecord(id).then(() => {
        this.loadRecords();
      });
    },
    clearFields() {
      this.errorMessages = {};
      this.id = "";
      this.$refs.form.reset();
    }
  },
  created() {
    this.loadRecords();
  },
}
</script>