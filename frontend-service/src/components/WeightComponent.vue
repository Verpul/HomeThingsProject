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
                                :error-messages="weightRecordsErrors.weightRecordDate"></v-text-field>
                </td>
                <td style="vertical-align: top">
                  <v-text-field density="compact" variant="underlined" v-model="weightRecordValue"
                                placeholder=">50 и <99.9"
                                :error-messages="weightRecordsErrors.weightRecordValue"></v-text-field>
                </td>
                <td style="vertical-align: top"></td>
                <td>
                  <div class="d-inline-block" style="white-space: nowrap">
                    <v-btn density="compact" icon @click="saveWeightRecord()">
                      <v-icon color="success">{{ id ? 'mdi mdi-check-circle-outline' : 'mdi mdi-plus-box-outline' }}
                      </v-icon>
                    </v-btn>
                    <v-btn density="compact" @click="clearFields" v-if="id"
                           class="ms-3" icon>
                      <v-icon color="primary">mdi-close-circle-outline</v-icon>
                    </v-btn>
                    <v-menu bottom left offset-y v-else>
                      <template v-slot:activator="{ on, attrs }">
                        <v-btn density="compact" class="ms-3" icon
                               v-bind="attrs"
                               v-on="on">
                          <v-icon color="primary">mdi mdi-cog</v-icon>
                        </v-btn>
                      </template>
                      <v-list>
                        <v-list-item
                            link
                            @click="showWeightLoadCSVDialog"
                        >
                          <v-list-item-title class="text-body-2">Загрузить данные из CSV</v-list-item-title>
                        </v-list-item>
                      </v-list>
                    </v-menu>
                  </div>
                </td>
              </tr>
              <tr
                  v-for="(record) in weightRecords"
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

    <v-dialog v-model="weightLoadCSVDataDialog" max-width="600" persistent>
      <WeightCSVDialogComponent @showWeightLoadCSVDialog="showWeightLoadCSVDialog"></WeightCSVDialogComponent>
    </v-dialog>
  </v-app>
</template>

<script>
import WeightCSVDialogComponent from "@/components/WeightCSVDialogComponent";

export default {
  name: 'WeightComponent',
  data() {
    return {
      weightRecordDate: "",
      weightRecordValue: "",
      id: null,
      weightLoadCSVDataDialog: false
    };
  },
  methods: {
    async saveWeightRecord() {
      const action = this.id === null ? "createRecord" : "updateRecord";

      this.$store.commit("setWeightRecordsErrors", {errors: []});

      await this.$store.dispatch(action, {
        id: this.id,
        weightRecordDate: this.weightRecordDate,
        weightRecordValue: this.weightRecordValue,
      })

      if (this.weightRecordsErrors.length === 0) {
        this.clearFields();
      }
    },
    deleteRecord(id) {
      this.$store.dispatch("deleteRecord", id);
      this.clearFields();
    },
    clearFields() {
      this.id = null;
      this.weightRecordDate = "";
      this.weightRecordValue = "";
      this.$store.commit("setWeightRecordsErrors", {errors: []});
    },
    setEditMode(record) {
      this.weightRecordDate = record.weightRecordDate;
      this.weightRecordValue = record.weightRecordValue;
      this.id = record.id;
    },
    showWeightLoadCSVDialog() {
      this.weightLoadCSVDataDialog = !this.weightLoadCSVDataDialog;
    }
  },
  computed: {
    weightRecords() {
      return this.$store.getters.weightRecords;
    },
    weightRecordsErrors() {
      return this.$store.getters.weightRecordsErrors;
    }
  },
  created() {
    this.$store.dispatch("getAllRecords")
  },
  components: {
    WeightCSVDialogComponent
  }
}
</script>