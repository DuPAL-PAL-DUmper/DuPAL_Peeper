package info.hkzlab.dupal.peeper.exceptions;

import java.io.IOException;

public class PeepholeException extends IOException {
    private static final long serialVersionUID = 1L;

    public PeepholeException(String message) {
        super(message);
    }
}