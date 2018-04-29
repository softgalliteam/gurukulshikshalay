package softgalli.gurukulshikshalay.common;
/**
 * Created by Shankar on 18-12-2014.
 */

import android.widget.EditText;

import java.util.regex.Pattern;

public class Validation {

    // Regular Expression
    // you can change the expression based on your need
    // Regular Expression
    // you can change the expression based on your need
    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PHONE_REGEX = "\\d{3}\\d{7}";
    private static final String PERCENTAGE_REGEX = "\\d{1}\\d{3}";

    // Error Messages
    private static final String REQUIRED_MSG = "Required";
    private static final String EMAIL_MSG = "Invalid E-Mail";
    private static final String PHONE_MSG = "##########";
    private static final String PERCENTAGE_MSG = "###.##";

    // call this method when you need to check email validation
    public static boolean isEmailAddress(EditText editText, boolean required) {
        return isValid(editText, EMAIL_REGEX, EMAIL_MSG, required);
    }

    // call this method when you need to check phone number validation
    public static boolean isPhoneNumber(EditText editText, boolean required) {
        //return isValid(editText, PHONE_REGEX, PHONE_MSG, required);
        return isValidPhoneNumber(editText, PHONE_REGEX, PHONE_MSG, required);
    }

    // call this method when you need to check percentage number validation
    public static boolean isPercentage(EditText editText, boolean required) {
        return isValidPercentage(editText, PERCENTAGE_REGEX, PERCENTAGE_MSG, required);
    }
    //For percentage validation

    public static boolean isValidPercentage(EditText editText, String regex, String errMsg, boolean required) {

        String text = editText.getText().toString();

        // text required and editText is blank, so return false
        if (required && !hasText(editText)) return false;
        // pattern doesn't match so returning false
        if (required && !Pattern.matches(regex, text)) {
            editText.setError(errMsg);
            return false;
        }
        ;
        if (text.length() == 0) {
            editText.setError(REQUIRED_MSG);
            return false;
        }
        return true;
    }

    // return true if the input field is valid, based on the parameter passed
    public static boolean isValidPhoneNumber(EditText editText, String regex, String errMsg, boolean required) {

        String text = editText.getText().toString().trim();
        // clearing the error, if it was previously set by some other values
        editText.setError(null);

        // text required and editText is blank, so return false
        if (required && !hasText(editText)) {
            return false;
        }
        //if first no. is 0 then return false
        if (required && !validNo(text)) {
            //editText.setError(inValidMoMsg);
            return false;
        }
        // pattern doesn't match so returning false
        if (required && !Pattern.matches(regex, text) && text.length() < 10) {
            editText.setError(errMsg);
            return false;
        }
        if (text.contains(" ")) {
            //editText.setError(inValidMoMsg);
            return false;
        }
        return true;
    }

    // return true if the input field is valid, based on the parameter passed
    public static boolean isValid(EditText editText, String regex, String errMsg, boolean required) {

        String text = editText.getText().toString().trim();
        // clearing the error, if it was previously set by some other values
        editText.setError(null);

        // text required and editText is blank, so return false
        if (required && !hasText(editText)) return false;

        // pattern doesn't match so returning false
        if (required && !Pattern.matches(regex, text)) {
            //editText.setError(errMsg);
            return false;
        }
        ;

        return true;
    }


    //end of phone


    // check the input field has any text or not
    // return true if it contains text otherwise false
    public static boolean hasText(EditText editText) {

        String text = editText.getText().toString().trim();
        editText.setError(null);

        // length 0 means there is no text
        if (text.length() == 0) {
            //editText.setError(REQUIRED_MSG);
            return false;
        }

        return true;
    }

    public static boolean isAddress(EditText etAddress, boolean b) {
        // TODO Auto-generated method stub
        return hasText(etAddress);
    }

    public static boolean isMessage(EditText etMessage, boolean b) {
        // TODO Auto-generated method stub
        return hasText(etMessage);
    }


    private static boolean validNo(String number) {
        boolean isValid;
        try {
            int no = Integer.parseInt(String.valueOf(number.charAt(0)));
            if (no < 7) {
                isValid = false;
            } else {
                isValid = true;
            }
            return isValid;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

