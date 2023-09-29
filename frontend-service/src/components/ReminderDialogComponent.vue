<template>
  <v-card>
    <v-card-title>
      <span class="text-body-1">Напоминание</span>
    </v-card-title>
    <v-divider></v-divider>
    <v-container>
      <v-row>
        <v-col cols="8">
          <v-card-text class="pa-0 mb-2">
            <v-btn icon color="primary">
              <v-icon>
                mdi-check
              </v-icon>
            </v-btn>
            <span>Основной</span>
          </v-card-text>
          <v-card-actions>
            <v-text-field dense v-model="reminderData.title"
                          class="custom-text-field-font-size">
              <template v-slot:label>
                    <span class="custom-text-field-font-size">
                      Название напоминания
                    </span>
              </template>
            </v-text-field>
          </v-card-actions>
          <v-card-actions class="mt-n3">
            <v-menu
                ref="dateMenu"
                v-model="visibility.expireDateMenu"
                :close-on-content-click="true"
                transition="scale-transition"
                offset-y
                max-width="290px"
                min-width="auto"
            >
              <template v-slot:activator="{ on, attrs }">
                <v-text-field
                    v-model="reminderData.expireDateFormatted"
                    persistent-hint
                    v-bind="attrs"
                    v-on="on"
                    dense
                    class="custom-text-field-font-size"
                >
                  <template v-slot:label>
                    <span class="custom-text-field-font-size">
                      Дата события
                    </span>
                  </template>
                </v-text-field>
              </template>
              <v-date-picker
                  v-model="date"
                  no-title
                  @input="visibility.expireDateMenu = false"
                  locale="ru-ru"
              ></v-date-picker>
            </v-menu>
          </v-card-actions>
          <v-card-actions>
            <v-row>
              <v-card-text class="pa-0 ms-3 mb-2 mt-1" v-if="visibility.remindCheckbox">
                <v-icon small>mdi-calendar</v-icon>
                <span class="ms-2">Оповещения</span>
              </v-card-text>
              <v-col>
                <v-menu
                    ref="visibility.remindDateMenu"
                    v-model="visibility.remindDateMenu"
                    :close-on-content-click="true"
                    transition="scale-transition"
                    offset-y
                    max-width="290px"
                    min-width="auto"
                >
                  <template v-slot:activator="{ on, attrs }">
                    <v-expand-transition>
                      <div v-if="visibility.remindCheckbox">
                        <v-text-field
                            v-model="reminderData.remindDateFormatted"
                            persistent-hint
                            v-bind="attrs"
                            v-on="on"
                            dense
                            class="custom-text-field-font-size"
                        >
                          <template v-slot:label>
                            <span class="custom-text-field-font-size">
                              Дата напоминания
                            </span>
                          </template>
                        </v-text-field>
                      </div>
                    </v-expand-transition>
                  </template>
                  <v-date-picker
                      v-model="remindDate"
                      no-title
                      @input="visibility.remindDateMenu = false"
                      locale="ru-ru"
                  ></v-date-picker>
                </v-menu>
              </v-col>
              <v-col>
                <v-menu
                    ref="timeMenu"
                    v-model="visibility.remindTimeMenu"
                    :close-on-content-click="false"
                    :nudge-right="40"
                    :return-value.sync="reminderData.remindTime"
                    transition="scale-transition"
                    offset-y
                    max-width="290px"
                    min-width="290px"
                >
                  <template v-slot:activator="{ on, attrs }">
                    <v-expand-transition>
                      <div v-if="visibility.remindCheckbox">
                        <v-text-field
                            v-model="reminderData.remindTime"
                            readonly
                            v-bind="attrs"
                            v-on="on"
                            dense
                            class="custom-text-field-font-size"
                        >
                          <template v-slot:label>
                            <span class="custom-text-field-font-size">
                              Время напоминания
                            </span>
                          </template>
                        </v-text-field>
                      </div>
                    </v-expand-transition>
                  </template>
                  <v-time-picker
                      v-if="visibility.remindTimeMenu"
                      format="24hr"
                      v-model="reminderData.remindTime"
                      full-width
                      @click:minute="$refs.timeMenu.save(reminderData.remindTime)"
                  ></v-time-picker>
                </v-menu>
              </v-col>
            </v-row>
          </v-card-actions>
          <div class="mt-10">
            <v-card-actions class="v-btn--absolute mb-2" style="bottom: 0">
              <v-btn small outlined color="success" @click="saveReminder()">Сохранить</v-btn>
              <v-btn small outlined color="grey" @click="closeReminderDialog()">Отмена</v-btn>
            </v-card-actions>
          </div>
        </v-col>
        <v-divider vertical></v-divider>
        <v-col>
          <v-checkbox v-model="visibility.remindCheckbox" dense>
            <template v-slot:label>
              <span class="text-body-2">
                Оповещения
              </span>
            </template>
          </v-checkbox>
          <v-card-actions class="v-btn--absolute mb-2" style="bottom: 0">
            <v-btn small outlined color="red" @click="deleteReminder()">Удалить</v-btn>
          </v-card-actions>
        </v-col>
      </v-row>
    </v-container>
  </v-card>
</template>

<script>

export default {
  name: "ReminderDialogComponent",
  data: () => ({
    date: null,
    remindDate: null,
    visibility: {
      expireDateMenu: false,
      remindCheckbox: false,
      remindTimeMenu: false,
      remindDateMenu: false
    },
    reminderData: {
      id: null,
      title: "",
      expireDateFormatted: null,
      remindTime: null,
      remindDateFormatted: null
    }
  }),
  methods: {
    saveReminder() {
      const action = this.reminderData.id === null ? "saveNewReminder" : "updateReminder";

      this.$store.dispatch(action, {
        id: this.reminderData.id,
        title: this.reminderData.title,
        expireDate: this.reminderData.expireDateFormatted,
        remindDate: this.reminderData.remindDateFormatted,
        remindTime: this.reminderData.remindTime
      });
      this.clearFields();
      this.closeReminderDialog();
    },
    clearFields() {
      this.reminderData.id = null;
      this.reminderData.title = "";
      this.reminderData.expireDateFormatted = null;
      this.date = null;
      this.reminderData.remindDateFormatted = null;
      this.reminderData.remindTime = null;
      this.remindDate = null;
    },
    closeReminderDialog() {
      this.$emit('showReminderDialog');
    },
    formatDate(date) {
      if (!date) return null

      const [year, month, day] = date.split('-')
      return `${day}.${month}.${year}`
    },
    handleSelectedReminder() {
      if (this.selectedReminder === null) {
        this.clearFields()
      } else {
        this.reminderData.id = this.selectedReminder.id;
        this.reminderData.title = this.selectedReminder.title;
        this.reminderData.expireDateFormatted = this.selectedReminder.expireDate;
        this.reminderData.remindDateFormatted = this.selectedReminder.remindDate;
        this.reminderData.remindTime = this.selectedReminder.remindTime;

        this.visibility.remindCheckbox = this.selectedReminder.remindDate !== null;
      }
    },
    deleteReminder() {
      this.$store.dispatch("deleteReminder", this.reminderData.id);
      this.closeReminderDialog();
      this.clearFields();
    }
  },
  watch: {
    date() {
      this.reminderData.expireDateFormatted = this.formatDate(this.date)
    },
    remindDate() {
      this.reminderData.remindDateFormatted = this.formatDate(this.remindDate);
    },
    selectedReminder() {
      this.handleSelectedReminder();
    },
  },
  props: [
    "selectedReminder"
  ],
  created() {
    this.handleSelectedReminder();
  }
}
</script>

<style scoped>
.custom-text-field-font-size {
  font-size: 14px;
}
</style>

