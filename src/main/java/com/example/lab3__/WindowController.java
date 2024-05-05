package com.example.lab3__;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class WindowController {
    //-----------------------------------Анотація---------------------------------------------------------------
    //AnchorPane використано замість Scene того, шо я не знаю, як працювати з Scene і я обрав спосіб який мені здався легшим
    //Типу... в нас є 2 панельки, в яких міститься різна інформація, і коли нам треба одна інформація
    //(Наприклад нам треба реєстрація, замість того, шоб викликати нове вікно, ми просто відобржаємо панельку, яка містить
    //потрібні поля для вікна реєстрації, а вже після того, як ми авторизувались, ми приховуєм панель реєстрації і
    //показуєм панель вже з функіоналом основної програми)
    //
    //
    //Також в цьому класі реалізовано весь функціонал програми (я намагався максимально все спростити, і не робити як я)
    //-----------------------------------------------------------------------------------------------------------



    //Називаємо елементи, з якими далі будем працювати (конопочки там всякі, текстові поля, стовпці таблиці тощо)
    @FXML
    private AnchorPane Program;
    @FXML
    private AnchorPane SignUp;
    private boolean isAdmin = false;
    @FXML
    private Label label;
    @FXML
    private TextField loginField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField searchTitleField;
    @FXML
    private TextField searchTranslationField;
    @FXML
    private CheckBox eWordCheckBox;
    @FXML
    private CheckBox wordCheckBox;
    @FXML
    private TextField searchNoteField;
    @FXML
    private Button signupButton;
    @FXML
    private Button changeButton;
    @FXML
    private Button findWordButton;
    @FXML
    private Button deleteWordButton;
    @FXML
    private Button sortWord;
    @FXML
    private Button loginButton;
    private ObservableList<Word> wordList;
    private String loggedInUser;  // Змінна для збереження логіну авторизованого користувача
    private boolean isHistory = false; // Змінна для функціоналу кнопки "показати історію"
    @FXML
    private TableView<Word> wordTableView;
    @FXML
    private TableColumn<Word, String> title;
    @FXML
    private TableColumn<Word, String> translation;
    @FXML
    private TableColumn<Word, String> type;
    @FXML
    private TableColumn<Word, String> note;

    public void logIn(){
        String login = loginField.getText();
        String password = passwordField.getText();
        try {
            File file = new File("users.txt");
            Scanner scanner = new Scanner(file);

            // Читаємо файл рядок за рядком
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                // Порівнюємо зчитані значення зі значеннями з текстових полів
                if (line.equals(login + ";" + password)) {
                    // Якщо знайдено співпадіння, міняємо AnchorPane (тобто з режиму реєстрації заходим в програму)
                    SignUp.setVisible(false);
                    SignUp.setDisable(true);
                    Program.setVisible(true);
                    Program.setDisable(false);
                    loggedInUser = login;  // Зберігаємо логін користувача

                    setTable();

                    if (line.equals("admin;admin")){
                        isAdmin = true;
                        findWordButton.setVisible(true);
                        deleteWordButton.setVisible(true);
                        changeButton.setVisible(true);
                        System.out.println("Режим адміна");
                    }
                }
            }
            // Закриваємо Scanner після завершення читання файлу
            scanner.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }
    public void signUp() {
        String login = loginField.getText();
        String password = passwordField.getText();

        // Перевіряємо, чи існує користувач з таким логіном в файлі
        if (userExists(login)) {
            label.setText("Користувач з таким логіном вже існує!");
            return;
        }

        // Додаємо нового користувача в файл
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt", true));
            writer.write(login + ";" + password);
            writer.newLine();
            writer.close();
            label.setText("Вітаю! Реєстрація успішна! \nНатисніть кнопку 'Увійти', щоб продовжити роботу");
            signupButton.setVisible(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private boolean userExists(String login) {
        //функція, яка перевіряє, чи є користувач із заданим логіном
        try {
            File file = new File("users.txt");
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(";");

                if (parts.length == 2) {
                    String existingLogin = parts[0];
                    if (existingLogin.equals(login)) {
                        scanner.close();
                        return true;
                    }
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
    public void setTable() {
        try {
            File file = new File("words.txt");
            Scanner scanner = new Scanner(file);

            List<Word> words = new ArrayList<>();

            // Читаємо файл рядок за рядком
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                // Розбиваємо рядок на частини по роздільнику ";"
                String[] parts = line.split(";");

                // Перевірка на коректність рядка
                if (parts.length == 4) {
                    String title = parts[0];
                    String translation = parts[1];
                    String type = parts[2];
                    String note = parts[3];

                    Word word = new Word(title, translation, type, note);
                    words.add(word);
                }
            }

            // Закриваємо Scanner після завершення читання файлу
            scanner.close();

            title.setCellValueFactory(new PropertyValueFactory<>("title"));
            translation.setCellValueFactory(new PropertyValueFactory<>("translation"));
            type.setCellValueFactory(new PropertyValueFactory<>("type"));
            note.setCellValueFactory(new PropertyValueFactory<>("note"));
            this.wordList = FXCollections.observableArrayList(words);

            // Встановлення даних в таблицю
            wordTableView.setItems(wordList);

            // Додайте слухача подій для таблиці, щоб заповнювати поля при виборі слова
            wordTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    Word selectedBook = wordTableView.getSelectionModel().getSelectedItem();
                    fillFields(selectedBook);
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void searchWords() {
        String title = searchTitleField.getText();
        String translation = searchTranslationField.getText();
        boolean searchEWord = eWordCheckBox.isSelected();
        boolean searchWord = wordCheckBox.isSelected();
        String note = searchNoteField.getText();

        try {
            File file = new File("words.txt");
            Scanner scanner = new Scanner(file);

            List<Word> words = new ArrayList<>();

            // Читаємо файл рядок за рядком
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(";");

                // Перевіряємо, чи рядок задовольняє введені параметри пошуку
                if (parts.length == 4) {
                    String wordTitle = parts[0];
                    String wordTranslation = parts[1];
                    String wordType = parts[2];
                    String wordNote = parts[3];

                    boolean matchTitle = title.isEmpty() || wordTitle.contains(title);
                    boolean matchTranslation = translation.isEmpty() || wordTranslation.contains(translation);
                    boolean matchType = (!searchEWord || wordType.equals("Вивчено")) &&
                            (!searchWord || wordType.equals("Не вивчено"));
                    boolean matchNote = note.isEmpty() || wordNote.contains(note);

                    // Додавання слова до списку, якщо всі параметри відповідають критеріям пошуку
                    if (matchTitle && matchTranslation && matchType && matchNote) {
                        Word word = new Word(wordTitle, wordTranslation, wordType, wordNote);
                        words.add(word);
                    }
                }
            }

            scanner.close();

            // Створення ObservableList зі списку слів
            ObservableList<Word> wordList = FXCollections.observableArrayList(words);

            // Встановлення даних в таблицю
            wordTableView.setItems(wordList);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void fillFields(Word word) {
        searchTitleField.setText(word.getTitle());
        searchTranslationField.setText(word.getTranslation());
        String type = word.getType();
        eWordCheckBox.setSelected(type.equals("Вивчено"));
        wordCheckBox.setSelected(type.equals("Не вивчено"));
        searchNoteField.setText(word.getNote());
    }


    public void deleteWord() {
        if (isAdmin) {
            Word selectedWord = wordTableView.getSelectionModel().getSelectedItem();

            if (selectedWord != null) {
                // Видалення з таблиці
                wordList.remove(selectedWord);
               // wordTableView.refresh();
                setWordTable(wordList);
                // Видалення з файлу
                removeWordFromFile(selectedWord);

                // Очищення полів
               // clearFields();
            }
        }
    }

    private void clearFields() {
        searchTitleField.clear();
        searchTranslationField.clear();
        eWordCheckBox.setSelected(false);
        wordCheckBox.setSelected(false);
        searchNoteField.clear();
    }
    private void removeWordFromFile(Word word) {
        try {
            File inputFile = new File("words.txt");
            File tempFile = new File("temp.txt");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String line;
            while ((line = reader.readLine()) != null) {
                // Розбиваємо рядок на частини по роздільнику ";"
                String[] parts = line.split(";");

                // Перевіряємо, чи рядок містить дані про видаляєму книгу
                if (parts.length == 4) {
                    String title = parts[0];
                    String translation = parts[1];
                    String type = parts[2];
                    String note = parts[3];

                    if (!title.equals(word.getTitle()) || !translation.equals(word.getTranslation()) || (type.equals(word.getType()) ||
                            !note.equals(word.getNote()))) {
                        // Записуємо рядок у тимчасовий файл, якщо це не видаляєма книга
                        writer.write(line);
                        writer.newLine();
                    }
                }
            }

            writer.close();
            reader.close();

            // Замінюємо початковий файл тимчасовим файлом
            if (inputFile.delete()) {
                tempFile.renameTo(inputFile);
            } else {
                System.out.println("Помилка при видаленні слова з файлу.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void addWord() {
        String title = searchTitleField.getText();
        String translation = searchTranslationField.getText();
        String type = eWordCheckBox.isSelected() ? "Вивчено" : "Не вивчено";
        String note = searchNoteField.getText();

        if (!title.isEmpty() && !translation.isEmpty() && !note.isEmpty()) {
            // Створення нового об'єкта книги
            Word newWord = new Word(title, translation, type, note);

            // Додавання слова до таблиці
            wordList.add(newWord);
            setWordTable(wordList);
            // Додавання книги до файлу
            addWordToFile(newWord);

            // Очищення полів для введення
            searchTitleField.clear();
            searchTranslationField.clear();
            eWordCheckBox.setSelected(false);
            wordCheckBox.setSelected(false);
            searchNoteField.clear();
        }
    }

    private void addWordToFile(Word word) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("words.txt", true));
            writer.write(word.getTitle() + ";" + word.getTranslation() + ";" +
                    word.getType() + ";" + word.getNote());
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void wordHistory() {
        isHistory = !isHistory;
        if (isHistory) {
            try {
                File file = new File("history.txt");
                Scanner scanner = new Scanner(file);

                List<Word> words = new ArrayList<>();

                // Читаємо файл рядок за рядком
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();

                    // Розбиваємо рядок на частини по роздільнику ";"
                    String[] parts = line.split(";");

                    // Перевірка на коректність рядка
                    if (parts.length == 5) {
                        String login = parts[0];
                        String title = parts[1];
                        String translation = parts[2];
                        String type = parts[3];
                        String note = parts[4];
                        if (login.equals(loggedInUser)) {
                            Word word = new Word(title, translation, type, note);
                            words.add(word);
                        }
                    }
                }

                // Закриваємо Scanner після завершення читання файлу
                scanner.close();

                // Налаштування відображення даних в таблиці
                title.setCellValueFactory(new PropertyValueFactory<>("title"));
                translation.setCellValueFactory(new PropertyValueFactory<>("translation"));
                type.setCellValueFactory(new PropertyValueFactory<>("type"));
                note.setCellValueFactory(new PropertyValueFactory<>("note"));
                this.wordList = FXCollections.observableArrayList(words);


                // Встановлення даних в таблицю
                wordTableView.setItems(wordList);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }else {
            setTable();
        }
    }
    public void changeWord() {
        if (isAdmin) {
            Word selectedWord = wordTableView.getSelectionModel().getSelectedItem();

            if (selectedWord != null) {
                // Зміна значень вибраної книги з полів вводу
                selectedWord.setTitle(searchTitleField.getText());
                selectedWord.setTranslation(searchTranslationField.getText());
                selectedWord.setType(eWordCheckBox.isSelected() ? "Вивчено" : "Не вивчено");
                selectedWord.setNote(searchNoteField.getText());

                // Оновлення таблиці
                wordTableView.refresh();

                // Збереження змін у файлі
                saveWordListToFile();
            }
        }
    }
    private void saveWordListToFile() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("words.txt"));
            for (Word word : wordList) {
                String line = word.getTitle() + ";" + word.getTranslation() + ";" + ";" +
                        word.getType() + ";" + word.getNote() + ";";
                writer.write(line);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   public void setWordTable(List<Word> words) {
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        translation.setCellValueFactory(new PropertyValueFactory<>("translation"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        note.setCellValueFactory(new PropertyValueFactory<>("note"));

        ObservableList<Word> wordList = FXCollections.observableArrayList(words);

        wordList.sort(Comparator.comparing(Word::getTitle));
        wordTableView.setItems(wordList);
    }

}