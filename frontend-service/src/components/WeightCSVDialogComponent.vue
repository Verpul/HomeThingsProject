<template>
  <v-card>
    <v-card-title>
      <span class="text-body-1">Загрузить данные из CSV файла</span>
    </v-card-title>
    <v-divider></v-divider>
    <v-container>
      <v-card-text>
        <div class="text-subtitle-2">Правила:</div>
        <div>1) Файл должен быть формата .csv</div>
        <div>2) Размер файла не должен привышать 1Мб</div>
        <div>3) Данные с одинаковой датой записи будут перезаписаны</div>
        <div>4) Данные в файле должны следовать следующим правилам:</div>
        <div>- Разделитель запятая, каждая запись на новой строке</div>
        <div>- Формат даты - дд.ММ.гг, вес - чч(.ч)</div>
        <div>- Образец:</div>
        <div class="ms-5">01.01.22,56.6</div>
        <div class="ms-5">15.01.23,60</div>
      </v-card-text>
      <v-file-input small-chips show-size dense label="CSV" accept=".csv" @change="handleFileInput" ref="fileInput"
                    :error-messages="fileUploadError" style="white-space: pre"></v-file-input>

      <v-card-actions class="justify-end">
        <v-btn outlined small @click="uploadFile" :disabled="file === null" color="success">Загрузить</v-btn>
        <v-btn outlined small @click="closeDialog" color="grey">Отмена</v-btn>
      </v-card-actions>
    </v-container>
  </v-card>
</template>

<script>
export default {
  name: "WeightCSVDialogComponent",
  data: () => ({
    file: null
  }),
  methods: {
    handleFileInput(file) {
      this.$store.commit('setFileUploadError', {error: null});
      this.file = file;
    },
    async uploadFile() {
      const formData = new FormData();
      formData.append('file', this.file);

      this.$store.commit('setFileUploadError', {error: null});

      await this.$store.dispatch("uploadFile", formData)

      if (this.fileUploadError === null) {
        this.closeDialog()
      }
    },
    clearFields() {
      this.file = null;
      this.$store.commit('setFileUploadError', {error: null});
      this.$refs.fileInput.reset();
    },
    closeDialog() {
      this.clearFields();
      this.$emit('showWeightLoadCSVDialog');
    }
  },
  computed: {
    fileUploadError() {
      return this.$store.getters.fileUploadError;
    }
  }
}
</script>
