package ru.dshirokov.reminder.telegramreminder.adapter.dto;

public enum Command {
    UNDEFINED(""),
    LIST("/list"),
    ADD("/add"),
    REMOVE("/remove"),
    HELP("/help"),
    SET_TIMEZONE("/setzone"),
    GET_TIMEZONE("/getzone");
    private final String literal;
    Command(String literal) {
        this.literal = literal;
    }

    public static Command from(String literal) {
        switch (literal) {
            case "/list" : return LIST;
            case "/add" : return ADD;
            case "/remove" : return REMOVE;
            case "/help" : return HELP;
            case "/setzone": return SET_TIMEZONE;
            case "/getzone": return GET_TIMEZONE;
            default: return UNDEFINED;
        }
    }

    public String literal() {
        return literal;
    }
}
