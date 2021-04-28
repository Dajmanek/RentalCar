package net.dajman.rentalcar.ui.utils;

import javafx.scene.control.TextField;

import java.util.Arrays;
import java.util.stream.Stream;

public class TextFields {

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
            final boolean isBlank = textField.getText().isBlank();
            TextFields.setIncorrect(isBlank, textField);
            result = isBlank || result;
        }
        return result;
    }
}
