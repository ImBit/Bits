package xyz.bitsquidd.bits.lib.command.exceptions;

public class SelectorParseException extends ArgumentParseException {

    public SelectorParseException(String message) {
        super(message);
    }

    public static SelectorParseException noPlayersFound(String selector) {
        return new SelectorParseException("No players found matching selector: " + selector);
    }

    public static SelectorParseException noEntitiesFound(String selector) {
        return new SelectorParseException("No entities found matching selector: " + selector);
    }

    public static SelectorParseException invalidSyntax(String selector, String details) {
        return new SelectorParseException("Invalid selector syntax in '" + selector + "': " + details);
    }
}