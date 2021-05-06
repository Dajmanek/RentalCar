package net.dajman.rentalcar.ui.utils;

import javafx.scene.control.TextField;

import java.util.Arrays;
import java.util.stream.Stream;

public class TextFields {

    public static final transient String ONLY_LETTERS_PATTERN = "[a-zA-ZęóąśłżźćńĘÓĄŚŁŻŹĆŃ]+";
    public static final transient String NUMBERS_AND_LETTERS_PATTERN = "[0-9a-zA-ZęóąśłżźćńĘÓĄŚŁŻŹĆŃ]+";
    public static final transient String SPACE_AND_NUMBERS_AND_LETTERS_PATTERN = "[\\s0-9a-zA-ZęóąśłżźćńĘÓĄŚŁŻŹĆŃ]+";

    private static final transient String INCORRECT_FIELD_CLASS = "incorrectField";

    public static void setIncorrect(final boolean incorrect, final TextField... textFields){
        final Stream<TextField> textFieldStream = Arrays.stream(textFields).parallel();
        if (incorrect){
            textFieldStream.filter(textField -> !textField.getStyleClass().contains(INCORRECT_FIELD_CLASS)).forEach(textField -> textField.getStyleClass().add(INCORRECT_FIELD_CLASS));
            return;
        }
        textFieldStream.filter(textField -> textField.getStyleClass().contains(INCORRECT_FIELD_CLASS)).forEach(textField -> textField.getStyleClass().remove(INCORRECT_FIELD_CLASS));
    }

    public static boolean checkIfBlank(final TextField... textFields){
        boolean result = false;
        for (TextField textField : textFields) {
            final boolean blank = textField.getText().isBlank();
            TextFields.setIncorrect(blank, textField);
            result |= blank;
        }
        return result;
    }

    public static boolean checkIfMatches(final String pattern, final TextField... textFields){
        boolean result = true;
        for (TextField textField : textFields) {
            final boolean matches = textField.getText().matches(pattern);
            TextFields.setIncorrect(!matches, textField);
            result &= matches;
        }
        return result;
    }
}
