<template>
  <v-card>
    <v-card-title>
      <span class="text-body-1">Напоминание</span>
    </v-card-title>
    <v-card-actions class="v-btn--absolute" style="top: 5px; right: 0">
      <v-btn small outlined color="red" @click="deleteReminder()">Удалить</v-btn>
    </v-card-actions>
    <v-divider></v-divider>
    <v-container>
      <v-row>
        <v-col cols="8">
          <v-card outlined v-if="reminderData.parentId !== null">
            <v-card-subtitle class="pa-0 mt-1">
              <v-icon small class="ms-1 mt-n1">mdi-arrow-up-left</v-icon>
              <span class="ms-1" style="font-size: 12px">{{ parentReminderTitle }}</span>
            </v-card-subtitle>
          </v-card>
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
                          :error-messages="reminderErrors.title"
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
          <v-card-actions class="py-0">
            <v-row>
              <v-card-text class="pa-0 ms-3 mb-2 mt-1" v-if="visibility.remindCheckbox">
                <v-icon small>mdi-alarm</v-icon>
                <span class="ms-2">Настройка оповещения</span>
              </v-card-text>
              <v-col class="pt-0">
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
              <v-col class="pt-0">
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
          <v-card-actions class="py-0">
            <v-row>
              <v-card-text class="pa-0 ms-3 mb-2 mt-3" v-if="reminderData.periodic">
                <v-icon small>mdi-sync</v-icon>
                <span class="ms-2">Настройка периодизации</span>
              </v-card-text>
              <v-col class="pt-0">
                <v-expand-transition>
                  <div v-if="reminderData.periodic">
                    <v-select :items="periods"
                              v-model="reminderData.period"
                              class="custom-text-field-font-size"
                              clearable
                              dense
                    >
                      <template v-slot:label>
                            <span class="custom-text-field-font-size">
                              Период
                            </span>
                      </template>
                    </v-select>
                  </div>
                </v-expand-transition>
              </v-col>
              <v-col class="pt-0">
                <v-expand-transition>
                  <div v-if="reminderData.periodic">
                    <v-text-field type="number" dense v-model="reminderData.periodicity"
                                  class="custom-text-field-font-size">
                      <template v-slot:label>
                    <span class="custom-text-field-font-size">
                      Периодичность
                    </span>
                      </template>
                    </v-text-field>
                  </div>
                </v-expand-transition>
              </v-col>
            </v-row>
          </v-card-actions>
          <v-card-actions class="py-0">
            <v-row>
              <v-col>
                <v-expand-transition>
                  <div v-if="visibility.commentCheckbox" class="mt-3">
                    <v-textarea dense
                                outlined
                                auto-grow
                                counter="500"
                                rows="3"
                                v-model="reminderData.comment"
                                class="custom-text-field-font-size"
                    >
                      <template v-slot:label>
                            <span class="custom-text-field-font-size">
                              Комментарий
                            </span>
                      </template>
                    </v-textarea>
                  </div>
                </v-expand-transition>
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
          <v-card-text class="pa-0 mt-3 mb-5">Опции</v-card-text>
          <v-select :items="reminderCategories"
                    item-text="title"
                    item-value="id"
                    v-model="reminderData.reminderCategory"
                    class="custom-text-field-font-size"
                    clearable
                    dense
                    :disabled="reminderData.parentId !== null"
          >
            <template v-slot:label>
                            <span class="custom-text-field-font-size">
                              Категория
                            </span>
            </template>
          </v-select>
          <v-checkbox v-model="visibility.remindCheckbox" dense>
            <template v-slot:label>
              <span class="text-body-2">
                Оповещение
              </span>
            </template>
          </v-checkbox>
          <v-checkbox v-model="reminderData.periodic" dense class="mt-n4">
            <template v-slot:label>
              <span class="text-body-2">
                Периодическая
              </span>
            </template>
          </v-checkbox>
          <v-checkbox v-model="visibility.commentCheckbox" dense class="mt-n4">
            <template v-slot:label>
              <span class="text-body-2">
                Комментарий
              </span>
            </template>
          </v-checkbox>
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
    parentReminderTitle: null,
    visibility: {
      expireDateMenu: false,
      remindCheckbox: false,
      remindTimeMenu: false,
      remindDateMenu: false,
      commentCheckbox: false,
    },
    reminderData: {
      id: null,
      title: "",
      expireDateFormatted: null,
      remindTime: null,
      remindDateFormatted: null,
      reminderCategory: null,
      comment: null,
      parentId: null,
      period: null,
      periodicity: null,
      periodic: false
    },
    periods: ['Минута', 'Час', 'День', 'Неделя', 'Месяц', 'Год']
  }),
  methods: {
    async saveReminder() {
      const action = this.reminderData.id === null ? "saveNewReminder" : "updateReminder";

      this.$store.commit("setReminderErrors", {errors: []});

      await this.$store.dispatch(action, {
        id: this.reminderData.id,
        title: this.reminderData.title,
        expireDate: this.reminderData.expireDateFormatted,
        remindDate: this.reminderData.remindDateFormatted,
        remindTime: this.reminderData.remindTime,
        categoryId: this.reminderData.reminderCategory,
        comment: this.reminderData.comment,
        parentId: this.reminderData.parentId,
        periodic: this.reminderData.periodic,
        period: this.reminderData.period,
        periodicity: this.reminderData.periodicity
      })

      if (this.reminderErrors.length === 0) {
        this.clearFields();
        this.closeReminderDialog();
      }
    },
    clearFields() {
      this.$store.commit("setReminderErrors", {errors: []});
      this.reminderData.id = null;
      this.reminderData.title = "";
      this.reminderData.expireDateFormatted = null;
      this.date = null;
      this.reminderData.remindDateFormatted = null;
      this.reminderData.remindTime = null;
      this.remindDate = null;
      this.reminderData.reminderCategory = null;
      this.visibility.remindCheckbox = false;
      this.reminderData.comment = null;
      this.visibility.commentCheckbox = false;
      this.reminderData.parentId = null;
      this.parentReminderTitle = null;
      this.reminderData.periodic = false;
      this.reminderData.period = null;
      this.reminderData.periodicity = null;
    },
    closeReminderDialog() {
      this.clearFields();
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
        this.reminderData.reminderCategory = this.selectedReminder.categoryId;
        this.reminderData.comment = this.selectedReminder.comment;
        this.reminderData.periodic = this.selectedReminder.periodic;
        this.reminderData.period = this.selectedReminder.period;
        this.reminderData.periodicity = this.selectedReminder.periodicity;

        if (this.selectedReminder.parentId !== null) {
          this.setParentIdTitle(this.selectedReminder.parentId);
        }

        this.visibility.remindCheckbox = this.selectedReminder.remindDate !== null;
        this.visibility.commentCheckbox = this.selectedReminder.comment !== null;
      }
    },
    deleteReminder() {
      this.$store.dispatch("deleteReminder", this.reminderData.id);
      this.closeReminderDialog();
      this.clearFields();
    },
    setParentIdTitle(id) {
      const foundReminder = this.$store.getters.reminders.find(reminder => reminder.id === id);

      if (foundReminder) {
        this.parentReminderTitle = foundReminder.title;
        this.reminderData.parentId = id;
        this.reminderData.reminderCategory = foundReminder.categoryId;
      }
    }
  },
  computed: {
    reminderErrors() {
      return this.$store.getters.reminderErrors;
    },
    reminderCategories() {
      return this.$store.getters.reminderCategories;
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
    parentId(id) {
      this.setParentIdTitle(id);
    }
  },
  props: [
    "selectedReminder", "parentId"
  ],
  created() {
    this.handleSelectedReminder();

    if (this.parentId !== null) {
      this.setParentIdTitle(this.parentId);
    }
  },
}
</script>

<style scoped>
.custom-text-field-font-size {
  font-size: 14px;
}
</style>

