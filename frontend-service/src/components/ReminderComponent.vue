<template>
  <v-app>
    <MainMenu/>
    <v-main>
      <v-container fluid>
        <v-card max-width="800" class="mx-auto"
                flat>
          <div class="text-end">
            <v-btn
                small
                outlined
                color="primary"
                @click="showReminderDialog()"
            >
              Добавить напоминание
            </v-btn>
          </div>
          <v-list flat>
            <v-list-item-group>
              <template v-for="(item, index) in remindersList">
                <v-list-item :key="item.id" @click="showReminderInfo(item)">
                  <v-list-item-action>
                    <v-btn icon color="primary">
                      <v-icon>
                        mdi-check
                      </v-icon>
                    </v-btn>
                  </v-list-item-action>
                  <v-list-item-content>
                    <v-list-item-title class="text-body-2 font-weight-medium">{{ item.title }}</v-list-item-title>
                    <div class="mt-2">
                      <v-list-item-title class="text-caption" v-if="item.expireDate">
                        <v-icon small>mdi-calendar</v-icon>
                        {{ item.expireDate }}
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
  </v-app>
</template>

<script>
import ReminderDialogComponent from "@/components/ReminderDialogComponent";

export default {
  name: "ReminderComponent",
  data: () => ({
    reminderDialog: false,
    selectedReminder: null
  }),
  methods: {
    showReminderDialog() {
      this.selectedReminder = null;
      this.reminderDialog = !this.reminderDialog;
    },
    showReminderInfo(reminder) {
      this.selectedReminder = reminder;
      this.reminderDialog = true;
    }
  },
  created() {
    this.$store.dispatch("getAllReminders");
  },
  computed: {
    remindersList() {
      return this.$store.getters.reminders;
    }
  },
  components: {
    ReminderDialogComponent
  }
}
</script>
