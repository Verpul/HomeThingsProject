<template>
  <v-app>
    <MainMenu/>
    <v-main>
      <v-container fluid>
        <v-card max-width="800" class="mx-auto"
                flat>
          <v-row>
            <v-col class="text-start">
              <template>
                <div>
                  <v-menu offset-y>
                    <template v-slot:activator="{ on, attrs }">
                      <v-btn
                          small
                          outlined
                          color="purple"
                          v-bind="attrs"
                          v-on="on"
                      >
                        <span v-text="'Категория:'"></span>
                        <span class="red--text text--lighten-2 ms-1"
                              v-text="currentCategory === null ? 'Все' : currentCategory.title">Test</span>
                      </v-btn>
                      <v-btn
                          icon
                          color="red"
                          small
                          v-show="currentCategory !== null"
                          @click="setReminderCategory(null)"
                          class="ms-1"
                      >
                        <v-icon>mdi-close</v-icon>
                      </v-btn>
                    </template>
                    <v-list>
                      <v-list-item
                          link
                          @click="reminderCategoryDialog = true"
                      >
                        <v-list-item-title class="text-body-2">Добавить категорию...</v-list-item-title>
                      </v-list-item>
                      <v-divider></v-divider>
                      <v-list-item
                          v-for="item in categoriesList"
                          :key="item.id"
                          link
                          @click="setReminderCategory(item.id)"
                      >
                        <v-list-item-title class="text-body-2">{{ item.title }}</v-list-item-title>
                        <v-list-item-icon>
                          <v-btn icon small @click.stop="showReminderCategoryInfo(item)">
                            <v-icon small>mdi-pencil-outline</v-icon>
                          </v-btn>
                        </v-list-item-icon>
                      </v-list-item>
                    </v-list>
                  </v-menu>
                </div>
              </template>
            </v-col>
            <v-col class="text-end">
              <v-btn
                  small
                  outlined
                  color="primary"
                  @click="showReminderDialog()"
              >
                Добавить напоминание
              </v-btn>
            </v-col>
          </v-row>
          <v-list flat>
            <v-list-item-group>
              <template v-for="(item, index) in remindersList">
                <v-list-item :key="item.id" @click="showReminderInfo(item)">
                  <v-list-item-action @click.stop="handleReminderComplete(item.id)">
                    <v-btn icon color="primary">
                      <v-icon>
                        mdi-check
                      </v-icon>
                    </v-btn>
                  </v-list-item-action>
                  <v-list-item-content>
                    <v-list-item-title class="text-body-2 font-weight-medium">{{ item.title }}</v-list-item-title>
                    <v-list-item-title class="text-caption mt-2 col-6 pa-0" v-if="item.comment">
                      <v-icon small>mdi-comment-outline</v-icon>
                      <span class="text-truncate ms-1" style="max-width: 10px">{{ item.comment }}</span>
                    </v-list-item-title>
                    <div class="mt-2">
                      <v-list-item-title class="text-caption float-left me-2" v-if="item.expireDate">
                        <v-icon small>mdi-calendar</v-icon>
                        {{ item.expireDate }}
                      </v-list-item-title>
                      <v-list-item-title class="text-caption" v-if="item.remindDate">
                        <v-icon small>mdi-alarm</v-icon>
                        {{ item.remindDate + ' ' + (item.remindTime === null ? '' : item.remindTime) }}
                      </v-list-item-title>
                    </div>
                  </v-list-item-content>
                </v-list-item>
                <v-divider :key="item.title + index"></v-divider>
              </template>
            </v-list-item-group>
          </v-list>
        </v-card>
      </v-container>
    </v-main>

    <v-dialog v-model="reminderDialog" max-width="800" persistent>
      <ReminderDialogComponent @showReminderDialog="showReminderDialog"
                               :selectedReminder="selectedReminder"></ReminderDialogComponent>
    </v-dialog>

    <v-dialog v-model="reminderCategoryDialog" max-width="600" persistent>
      <ReminderCategoryComponent @showReminderCategoryDialog="showReminderCategoryDialog"
                                 :selectedReminderCategory="selectedReminderCategory"
      ></ReminderCategoryComponent>
    </v-dialog>
  </v-app>
</template>

<script>
import ReminderDialogComponent from "@/components/ReminderDialogComponent";
import ReminderCategoryComponent from "@/components/ReminderCategoryComponent";

export default {
  name: "ReminderComponent",
  data: () => ({
    reminderDialog: false,
    reminderCategoryDialog: false,
    selectedReminder: null,
    selectedReminderCategory: null
  }),
  methods: {
    showReminderDialog() {
      this.selectedReminder = null;
      this.reminderDialog = !this.reminderDialog;
    },
    showReminderCategoryDialog() {
      this.selectedReminderCategory = null;
      this.reminderCategoryDialog = !this.reminderCategoryDialog;
    },
    showReminderInfo(reminder) {
      this.selectedReminder = reminder;
      this.reminderDialog = true;
    },
    showReminderCategoryInfo(category) {
      this.selectedReminderCategory = category;
      this.reminderCategoryDialog = true;
    },
    handleReminderComplete(reminderId) {
      console.log(reminderId);
    },
    setReminderCategory(categoryId) {
      this.$store.commit("setCategoryId", categoryId);
      this.$store.dispatch("getAllReminders");
    },
  },
  created() {
    this.$store.dispatch("getAllReminders");
    this.$store.dispatch("getAllReminderCategories")
  },
  computed: {
    remindersList() {
      return this.$store.getters.reminders;
    },
    categoriesList() {
      return this.$store.getters.reminderCategories;
    },
    currentCategory() {
      let categoryId = this.$store.getters.currentCategoryId;

      return categoryId === null ?
          null : this.$store.getters.reminderCategories.find(category => category.id === categoryId);
    }
  },
  components: {
    ReminderDialogComponent, ReminderCategoryComponent
  }
}
</script>
