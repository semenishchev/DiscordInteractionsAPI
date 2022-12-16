package me.mrfunny.example.ticketbot.exception;

import java.io.IOException;

public class NotATicketException extends IOException {
    public NotATicketException() {
        super("Not in the ticket channel");
    }
}
