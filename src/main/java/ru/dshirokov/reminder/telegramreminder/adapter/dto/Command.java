package ru.dshirokov.reminder.telegramreminder.adapter.dto;

public enum Command {
    LIST("/list"), ADD("/add"), REMOVE("/remove"), UNDEFINED("");
    private final String literal;
    Command(String literal) {
        this.literal = literal;
    }

    public static Command from(String literal) {
        switch (literal) {
            case "/list" : return LIST;
            case "/add" : return ADD;
            case "/remove" : return REMOVE;
            default: return UNDEFINED;
        }
    }

    public String literal() {
        return literal;
    }
}
