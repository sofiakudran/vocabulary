package com.example.lab3__;

public class Word {
    //--------------------------------Анотація------------------------------------
    //Цей клас потрібний, щоб заповнювати таблицю зі словами.
    //
    //При зчитуванні даних з файлу "words", потрібні поля, запихуєм в конструктор, шоб
    //створити об'єкт класу "word" і вже потім ці об'єкти виводити в таблиці.
    //----------------------------------------------------------------------------


    //далі перераховано поля, потрібні для опису класу "слово", а саме: мова, автор, назва та тип книги
    private String title;
    private String translation;
    private String type;
    private String note;
    //далі реалізовано гетери-сетери, потрібні роботи з раніше описаними полями

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Word(String title, String translation, String type, String note) {
        this.title = title;
        this.translation = translation;
        this.type = type;
        this.note = note;
    }
}

