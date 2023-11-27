<template>
  <v-app>
    <MainMenu/>
    <v-main>
      <v-container fluid>
        <v-row>
          <v-col cols="2">
            <v-list outlined dense shaped>
              <v-list-item-group v-model="selectedCategoryIndex" color="primary">
                <v-hover v-slot="{hover}">
                  <v-row>
                    <v-col>
                      <v-subheader>Категории</v-subheader>
                    </v-col>
                    <v-col class="text-end">
                      <v-btn icon color="success" x-small class="mt-2 me-5" plain v-if="hover"
                             @click.stop="openCategoryDialog(null)">
                        <v-icon>mdi mdi-plus-box-outline</v-icon>
                      </v-btn>
                    </v-col>
                  </v-row>
                </v-hover>
                <v-divider></v-divider>
                <template v-for="(category, index) in categories">
                  <v-hover :key="category.title" v-slot="{hover}">
                    <v-list-item link @click="getNotes(category.id)">
                      <v-list-item-content>
                        <v-list-item-title v-text="category.title"></v-list-item-title>
                      </v-list-item-content>
                      <v-list-item-icon v-if="hover">
                        <v-btn icon color="warning" x-small plain @click.stop="openCategoryDialog(category.id)">
                          <v-icon>mdi-square-edit-outline</v-icon>
                        </v-btn>
                      </v-list-item-icon>
                    </v-list-item>
                  </v-hover>
                  <v-divider :key="index + category.title" v-if="index < categories.length - 1"></v-divider>
                </template>
              </v-list-item-group>
            </v-list>
          </v-col>
          <v-col cols="2">
            <v-list v-if="selectedCategory !== null" dense outlined>
              <v-hover v-slot="{hover}">
                <v-row>
                  <v-col cols="8">
                    <div class="col-12 text-truncate">
                      {{ selectedCategory.title }}
                    </div>
                  </v-col>
                  <v-col class="text-end">
                    <v-btn icon color="success" x-small class="mt-3 me-5" plain v-if="hover" @click.stop="createNote">
                      <v-icon>mdi mdi-plus-box-outline</v-icon>
                    </v-btn>
                  </v-col>
                </v-row>
              </v-hover>
              <v-divider v-if="notes.length !== 0"></v-divider>
              <template v-for="(note, index) in notes">
                <v-hover :key="note.id" v-slot="{hover}">
                  <v-list-item link @click="selectNote(note)">
                    <v-list-item-content>
                      <v-list-item-title v-text="removeTags(note.text)"></v-list-item-title>
                    </v-list-item-content>
                    <v-list-item-icon v-if="hover">
                      <v-btn icon color="error" x-small plain @click.stop="deleteNote(note.id)">
                        <v-icon>mdi-delete-outline</v-icon>
                      </v-btn>
                    </v-list-item-icon>
                  </v-list-item>
                </v-hover>
                <v-divider :key="index * notes.length * notes.length" v-if="index < notes.length - 1"></v-divider>
              </template>
            </v-list>
          </v-col>
          <v-col>
            <v-tiptap v-model="noteValue" v-if="noteValue !== null"
                      :toolbar="['bold', 'italic', 'underline', 'strike', 'color', '|', 'headings', '|', 'left', 'center', 'right', 'justify', '|', 'bulletList', 'orderedList', '|', 'link', 'image', 'video', 'emoji', '|', 'clear', '#updateNote']">
              <template #updateNote>
                <v-btn icon small title="Update note" @click="updateNote" color="success">
                  <v-icon>mdi mdi-content-save-outline</v-icon>
                </v-btn>
              </template>
            </v-tiptap>
          </v-col>
        </v-row>
      </v-container>
    </v-main>

    <v-dialog v-model="categoryDialog" persistent max-width="600px">
      <v-card>
        <v-card-title>
          <span class="text-body-1">Категория заметок</span>
        </v-card-title>
        <v-container>
          <v-divider></v-divider>
          <v-card-actions class="mb-3 mt-3">
            <v-text-field label="Название категории"
                          v-model="categoryData.title"
                          class="custom-text-field-font-size"
                          :error-messages="notesCategoriesErrors.title"
                          dense
                          counter
            ></v-text-field>
          </v-card-actions>
          <v-row>
            <v-col>
              <v-card-actions>
                <v-btn small outlined color="success" @click="saveNoteCategory()">Сохранить</v-btn>
                <v-btn small outlined color="grey" @click="clearCategoryFields">Отмена</v-btn>
              </v-card-actions>
            </v-col>
            <v-col>
              <v-card-actions class="justify-end" v-if="categoryData.id !== null">
                <v-btn small outlined color="red" @click="deleteNoteCategory()">Удалить</v-btn>
              </v-card-actions>
            </v-col>
          </v-row>
        </v-container>
      </v-card>
    </v-dialog>
  </v-app>
</template>

<script>
export default {
  name: "NotesComponent",
  data: () => ({
    noteValue: null,
    selectedCategoryIndex: null,
    selectedCategory: null,
    selectedNoteId: null,
    categoryDialog: false,
    categoryData: {
      id: null,
      title: null
    }
  }),
  methods: {
    getNotes(categoryId) {
      this.$store.dispatch('getNotes', categoryId);
      this.selectedCategory = this.categories.find(category => category.id === categoryId);
    },
    selectNote(note) {
      this.selectedNoteId = note.id;
      this.noteValue = note.text;
    },
    openCategoryDialog(categoryId) {
      if (categoryId) {
        const category = this.categories.find(category => category.id === categoryId);
        this.categoryData.id = category.id;
        this.categoryData.title = category.title;
      }

      this.categoryDialog = true;
    },
    async saveNoteCategory() {
      const action = this.categoryData.id === null ? "createNoteCategory" : "updateNoteCategory";

      this.$store.commit("setNotesCategoriesErrors", {errors: []});

      await this.$store.dispatch(action, {
        id: this.categoryData.id,
        title: this.categoryData.title,
      })

      if (this.notesCategoriesErrors.length === 0) {
        this.clearCategoryFields();
      }
    },
    deleteNoteCategory() {
      this.$store.dispatch("deleteNoteCategory", this.categoryData.id);
      this.clearCategoryFields();
    },
    clearCategoryFields() {
      this.categoryDialog = false;
      this.categoryData.id = null;
      this.categoryData.title = null;
    },
    createNote() {
      this.$store.dispatch("createNote", {
        text: "",
        category: this.selectedCategory
      })
    },
    updateNote() {
      this.$store.dispatch("updateNote", {
        id: this.selectedNoteId,
        text: this.noteValue,
        category: this.selectedCategory
      })
    },
    deleteNote(noteId) {
      this.$store.dispatch("deleteNote", {
        noteId: noteId,
        category: this.selectedCategory
      });

      if (noteId === this.selectedNoteId) this.clearNoteFields();
    },
    clearNoteFields() {
      this.noteValue = null;
    },
    removeTags(text) {
      return text.replace(/(<([^>]+)>)/ig, '');
    }
  },
  computed: {
    categories() {
      return this.$store.getters.notesCategories;
    },
    notes() {
      return this.$store.getters.notes;
    },
    notesCategoriesErrors() {
      return this.$store.getters.notesCategoriesErrors;
    }
  },
  created() {
    this.$store.dispatch('getNotesCategories');
  }
}
</script>

