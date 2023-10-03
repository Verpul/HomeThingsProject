<template>
  <v-card>
    <v-card-title>
      <span class="text-body-1">Категория напоминаний</span>
    </v-card-title>
    <v-container>
      <v-divider></v-divider>
      <v-card-actions class="mb-3 mt-3">
        <v-text-field label="Название категории"
                      v-model="title"
                      :error-messages="reminderCategoryErrors.title"
                      class="custom-text-field-font-size"
                      dense></v-text-field>
      </v-card-actions>
      <div class="text-center">
        <v-snackbar
            v-model="errorSnackbar"
            :timeout="errorSnackbarTimeout"
            outlined
            color="red"
        >
          {{ errorSnackbarText }}
        </v-snackbar>
      </div>
      <v-row>
        <v-col>
          <v-card-actions>
            <v-btn small outlined color="success" @click="saveReminderCategory()">Сохранить</v-btn>
            <v-btn small outlined color="grey" @click="closeReminderCategoryDialog()">Отмена</v-btn>
          </v-card-actions>
        </v-col>
        <v-col>
          <v-card-actions class="justify-end" v-if="id !== null">
            <v-btn small outlined color="red" @click="deleteReminderCategory()">Удалить</v-btn>
          </v-card-actions>
        </v-col>
      </v-row>
    </v-container>
  </v-card>
</template>

<script>
export default {
  name: "ReminderCategoryComponent",
  data: () => ({
    id: null,
    title: "",
    errorSnackbar: false,
    errorSnackbarTimeout: 1500,
    errorSnackbarText: null
  }),
  methods: {
    async saveReminderCategory() {
      const action = this.id === null ? "saveNewReminderCategory" : "updateReminderCategory";

      this.$store.commit("setReminderCategoryErrors", {errors: []});

      await this.$store.dispatch(action, {
        id: this.id,
        title: this.title
      })

      if (this.reminderCategoryErrors.length === 0) {
        this.clearFields();
        this.$emit('showReminderCategoryDialog');
      }
    },
    closeReminderCategoryDialog() {
      this.clearFields();
      this.$emit('showReminderCategoryDialog');
    },
    clearFields() {
      this.id = null;
      this.title = "";
      this.$store.commit("setReminderCategoryErrors", {errors: []});
    },
    handleSelectedReminderCategory() {
      if (this.selectedReminderCategory === null) {
        this.clearFields();
      } else {
        this.id = this.selectedReminderCategory.id;
        this.title = this.selectedReminderCategory.title;
      }
    },
    async deleteReminderCategory() {
      const message = await this.$store.dispatch("deleteReminderCategory", this.id);

      if (message) {
        this.errorSnackbarText = message;
        this.errorSnackbar = true;
      } else {
        this.$emit('showReminderCategoryDialog');
      }
    }
  },
  computed: {
    reminderCategoryErrors() {
      return this.$store.getters.reminderCategoryErrors;
    }
  },
  watch: {
    selectedReminderCategory() {
      this.handleSelectedReminderCategory();
    }
  },
  props: [
    "selectedReminderCategory"
  ],
  created() {
    this.handleSelectedReminderCategory();
  }
}
</script>

<style scoped>
.custom-text-field-font-size {
  font-size: 14px;
}
</style>