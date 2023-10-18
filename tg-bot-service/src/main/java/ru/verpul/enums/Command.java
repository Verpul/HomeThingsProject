package ru.verpul.enums;

import lombok.Getter;

@Getter
public enum Command {
    HELP("Комманды бота:\n\n \t- Дела\n \t- Курс\n \t- Погода\n");

    private final String text;

    Command(String text) {
        this.text = text;
    }
}
