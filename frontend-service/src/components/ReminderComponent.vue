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
              <div v-for="(item, index) in remindersList" :key="item.id">
                <template v-if="!isParentReminderCollapsed(item)">
                  <v-list-item @click="showReminderInfo(item)"  :style="countIndent(item)">
                    <v-list-item-action-text class="text--primary">
                      <v-btn icon class="v-btn--absolute ms-n9" v-if="haveChildrenReminders(item.id)"
                             @click.stop="switchCollapse(item.id, item.parentId)">
                        <v-icon size="20px" v-if="getCollapseIcon(item.id)">
                          mdi-chevron-down
                        </v-icon>
                        <v-icon size="20px" v-else>
                          mdi-chevron-right
                        </v-icon>
                      </v-btn>
                      <v-btn icon class="ms-n2" @click.stop="handleReminderComplete(item.id)">
                        <v-icon size="20px">
                          mdi-circle-outline
                        </v-icon>
                      </v-btn>
                      <span class="text-body-2">{{ item.title }}</span>
                      <div style="right: 0; top: 0" class="v-btn--absolute" v-if="item.nestingDepth < 4 || item.nestingDepth === null">
                        <template>
                          <div class="text-center">
                            <v-menu
                                offset-y

                            >
                              <template v-slot:activator="{ on, attrs }">
                                <v-btn
                                    icon
                                    v-bind="attrs"
                                    v-on="on"
                                >
                                  <v-icon size="20px">mdi-dots-horizontal</v-icon>
                                </v-btn>
                              </template>

                              <v-list>
                                <v-list-item @click.stop="showReminderDialogForChild(item.id)" dense>
                                  <v-list-item-title class="text-body-2 font-weight-regular">Добавить подзадачу</v-list-item-title>
                                </v-list-item>
                              </v-list>
                            </v-menu>
                          </div>
                        </template>
                      </div>
                      <v-list-item-title class="text-caption col-6 pa-0 ms-6" v-if="item.comment">
                        <v-icon small>mdi-comment-outline</v-icon>
                        <span class="text-truncate ms-1" style="max-width: 10px">{{ item.comment }}</span>
                      </v-list-item-title>
                      <div class="ms-6 mt-2">
                        <v-list-item-title class="text-caption float-left me-2" v-if="item.expireDate">
                          <v-icon small>mdi-calendar</v-icon>
                          {{ item.expireDate }}
                        </v-list-item-title>
                        <v-list-item-title class="text-caption" v-if="item.remindDate">
                          <v-icon small>mdi-alarm</v-icon>
                          {{ item.remindDate + ' ' + (item.remindTime === null ? '' : item.remindTime) }}
                        </v-list-item-title>
                        <v-list-item-title class="text-caption" v-if="item.declensionedPeriod">
                          <v-icon small>mdi-sync</v-icon>
                          {{ item.declensionedPeriod }}
                        </v-list-item-title>
                      </div>
                    </v-list-item-action-text>
                  </v-list-item>
                  <v-divider :key="item.title + index" :style="countIndent(item)" class="mt-1"></v-divider>
                </template>
              </div>
            </v-list-item-group>
          </v-list>
        </v-card>
      </v-container>
    </v-main>

    <v-dialog v-model="reminderDialog" max-width="800" persistent>
      <ReminderDialogComponent @showReminderDialog="showReminderDialog"
                               :selectedReminder="selectedReminder"
                               :parentId="parentId"
      ></ReminderDialogComponent>
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
    selectedReminderCategory: null,
    collapsedReminders: [],
    parentId: null
  }),
  methods: {
    showReminderDialog() {
      this.parentId = null;
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
    showReminderDialogForChild(id) {
      this.parentId = id;
      this.reminderDialog = true;
    },
    handleReminderComplete(reminderId) {
      console.log(reminderId);
    },
    setReminderCategory(categoryId) {
      this.$store.commit("setCategoryId", categoryId);
      this.$store.dispatch("getAllReminders");
    },
    countIndent(reminder) {
      const margin = "margin-left: ";
      if (reminder.parentId === null) return margin + "0px";

      return margin + (25 * reminder.nestingDepth) + "px";
    },
    haveChildrenReminders(reminderId) {
      return this.remindersList.find(reminder => reminder.parentId === reminderId);
    },
    switchCollapse(reminderId, parentId) {
      const reminderIndex = this.collapsedReminders.indexOf(reminderId);
      const parentReminderIndex = this.collapsedReminders.indexOf(parentId);

      if (reminderIndex !== -1) {
        if (parentReminderIndex === -1) {
          this.collapsedReminders.splice(reminderIndex, 1);
        }
      } else {
        this.collapsedReminders.push(reminderId);
      }

      const childrenArr = this.$store.getters.reminders.filter(reminder => reminder.parentId === reminderId);
      childrenArr.forEach(reminder => {
        this.switchCollapse(reminder.id, reminder.parentId);
      })
    },
    getCollapseIcon(reminderId) {
      return this.collapsedReminders.indexOf(reminderId) === -1;
    }
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
    },
    isParentReminderCollapsed() {
      return function (reminder) {
        return this.collapsedReminders.find(id => id === reminder.parentId)
      }
    }
  },
  components: {
    ReminderDialogComponent, ReminderCategoryComponent
  }
}
</script>
