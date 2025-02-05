package com.contact_system.db;

public class DbIntegrityException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public DbIntegrityException(String message) {
        super(message);
    }
}
