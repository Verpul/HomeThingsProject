<template>
  <v-card>
    <v-card-title>
      <span class="text-body-1">Напоминание</span>
    </v-card-title>
    <v-divider></v-divider>
    <v-container>
      <v-row>
        <v-col cols="8">
          <v-card-actions>
            <v-text-field dense label="Название напоминания" v-model="reminderData.title"></v-text-field>
          </v-card-actions>
          <v-card-actions class="mt-n6">
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
                    label="Дата события"
                    persistent-hint
                    v-bind="attrs"
                    v-on="on"
                ></v-text-field>
              </template>
              <v-date-picker
                  v-model="date"
                  no-title
                  @input="visibility.expireDateMenu = false"
              ></v-date-picker>
            </v-menu>
          </v-card-actions>
          <!--            <v-row>-->
          <!--              <v-col>-->
          <!--                <v-menu-->
          <!--                    ref="dateMenu"-->
          <!--                    v-model="visibility.dateMenu"-->
          <!--                    :close-on-content-click="true"-->
          <!--                    transition="scale-transition"-->
          <!--                    offset-y-->
          <!--                    max-width="290px"-->
          <!--                    min-width="auto"-->
          <!--                >-->
          <!--                  <template v-slot:activator="{ on, attrs }">-->
          <!--                    <v-expand-transition>-->
          <!--                      <div v-if="visibility.dateOfRemindCheckbox">-->
          <!--                        <v-text-field-->
          <!--                            v-model="dateFormatted"-->
          <!--                            label="Дата события"-->
          <!--                            persistent-hint-->
          <!--                            v-bind="attrs"-->
          <!--                            v-on="on"-->
          <!--                        ></v-text-field>-->
          <!--                      </div>-->
          <!--                    </v-expand-transition>-->
          <!--                  </template>-->
          <!--                  <v-date-picker-->
          <!--                      v-model="reminderData.date"-->
          <!--                      no-title-->
          <!--                      @input="visibility.dateMenu = false"-->
          <!--                  ></v-date-picker>-->
          <!--                </v-menu>-->
          <!--              </v-col>-->
          <!--              <v-col>-->
          <!--                <v-menu-->
          <!--                    ref="timeMenu"-->
          <!--                    v-model="visibility.timeMenu"-->
          <!--                    :close-on-content-click="false"-->
          <!--                    :nudge-right="40"-->
          <!--                    :return-value.sync="reminderData.time"-->
          <!--                    transition="scale-transition"-->
          <!--                    offset-y-->
          <!--                    max-width="290px"-->
          <!--                    min-width="290px"-->
          <!--                >-->
          <!--                  <template v-slot:activator="{ on, attrs }">-->
          <!--                    <v-expand-transition>-->
          <!--                      <div v-if="visibility.timeOfRemindCheckbox">-->
          <!--                        <v-text-field-->
          <!--                            v-model="reminderData.time"-->
          <!--                            label="Время напоминания"-->
          <!--                            readonly-->
          <!--                            v-bind="attrs"-->
          <!--                            v-on="on"-->
          <!--                        ></v-text-field>-->
          <!--                      </div>-->
          <!--                    </v-expand-transition>-->
          <!--                  </template>-->
          <!--                  <v-time-picker-->
          <!--                      v-if="visibility.timeMenu"-->
          <!--                      format="24hr"-->
          <!--                      v-model="reminderData.time"-->
          <!--                      full-width-->
          <!--                      @click:minute="$refs.timeMenu.save(reminderData.time)"-->
          <!--                  ></v-time-picker>-->
          <!--                </v-menu>-->
          <!--              </v-col>-->
          <!--            </v-row>-->
          <div class="mt-10">
            <v-card-actions class="v-btn--absolute" style="bottom: 0">
              <v-btn small outlined color="success" @click="saveReminder()">Сохранить</v-btn>
              <v-btn small outlined color="grey" @click="closeReminderDialog()">Отмена</v-btn>
            </v-card-actions>
          </div>
        </v-col>
        <v-divider vertical></v-divider>
        <v-col>
          <v-card-title>
<!--            <span class="text-subtitle-1">Опции</span>-->
          </v-card-title>
          <v-card-actions class="v-btn--absolute" style="bottom: 0">
            <v-btn small outlined color="red" @click="deleteReminder()">Удалить</v-btn>
          </v-card-actions>
          <!--          <v-checkbox-->
          <!--              dense-->
          <!--              v-model="visibility.dateOfRemindCheckbox"-->
          <!--              label="Оповещение"-->
          <!--          ></v-checkbox>-->
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
    visibility: {
      expireDateMenu: false,
      // timeMenu: false
    },
    reminderData: {
      id: null,
      title: "",
      // time: null,
      expireDateFormatted: null,
    }
  }),
  methods: {
    saveReminder() {
      const action = this.reminderData.id === null ? "saveNewReminder" : "updateReminder";

      this.$store.dispatch(action, {
        id: this.reminderData.id,
        title: this.reminderData.title,
        expireDate: this.reminderData.expireDateFormatted
      });
      this.clearFields();
      this.closeReminderDialog();
    },
    clearFields() {
      this.reminderData.id = null;
      this.reminderData.title = "";
      this.reminderData.expireDateFormatted = null;
      this.date = null;
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
    selectedReminder() {
      this.handleSelectedReminder();
    }
  },
  props: [
    "selectedReminder"
  ],
  created() {
    this.handleSelectedReminder();
  }
}
</script>

