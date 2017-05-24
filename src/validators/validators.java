package validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.input.KeyEvent;

public class validators {

    public void numOnly(KeyEvent e) {
        String c = e.getCharacter();
        if ("0123456789".contains(c)) {
        } else {
            e.consume();
        }
    }

    public void numDot(KeyEvent e) {
        String c = e.getCharacter();
        if ("0123456789.".contains(c)) {
        } else {
            e.consume();
        }
    }

    public void numPh(KeyEvent e) {
        String c = e.getCharacter();
        if ("0123456789+-".contains(c)) {
        } else {
            e.consume();
        }
    }

    public void others(KeyEvent e) {
        String c = e.getCharacter();
        if ("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz.\'- /".contains(c)) {
        } else {
            e.consume();
        }
    }

    public static boolean email_val(String email) {

        boolean status = false;

        String EMAIL_PATTERN = "^[a-zA-Z0-9]{1,20}@[a-zA-Z0-9]{1,20}.[a-zA-Z]{2,3}$";
        String EMAIL_PATTERN2 = "^[a-zA-Z0-9]{1,20}.[a-zA-Z0-9]{1,20}@[a-zA-Z0-9]{1,20}.[a-zA-Z]{2,3}$";

        String EMAIL_PATTERNa = "^[a-zA-Z0-9]{1,20}@[a-zA-Z0-9]{1,20}.[a-zA-Z]{2,3}.[a-zA-Z]{2,3}$";
        String EMAIL_PATTERN2a = "^[a-zA-Z0-9]{1,20}.[a-zA-Z0-9]{1,20}@[a-zA-Z0-9]{1,20}.[a-zA-Z]{2,3}.[a-zA-Z]{2,3}$";

        String EMAIL_PATTERNb0 = "^[a-zA-Z0-9]{1,20}_[a-zA-Z0-9]{1,20}@[a-zA-Z0-9]{1,20}.[a-zA-Z]{2,3}$";
        String EMAIL_PATTERNb1 = "^[a-zA-Z0-9]{1,20}_[a-zA-Z0-9]{1,20}@[a-zA-Z0-9]{1,20}.[a-zA-Z]{2,3}.[a-zA-Z]{2,3}$";
        String EMAIL_PATTERNb2 = "^[a-zA-Z0-9]{1,20}_[a-zA-Z0-9]{1,20}.[a-zA-Z0-9]{1,20}@[a-zA-Z0-9]{1,20}.[a-zA-Z]{2,3}.[a-zA-Z]{2,3}$";
        String EMAIL_PATTERNb3 = "^[a-zA-Z0-9]{1,20}_[a-zA-Z0-9]{1,20}.[a-zA-Z0-9]{1,20}@[a-zA-Z0-9]{1,20}.[a-zA-Z]{2,3}$";
        String EMAIL_PATTERNb4 = "^[a-zA-Z0-9]{1,20}.[a-zA-Z0-9]{1,20}_[a-zA-Z0-9]{1,20}@[a-zA-Z0-9]{1,20}.[a-zA-Z]{2,3}.[a-zA-Z]{2,3}$";
        String EMAIL_PATTERNb5 = "^[a-zA-Z0-9]{1,20}.[a-zA-Z0-9]{1,20}_[a-zA-Z0-9]{1,20}@[a-zA-Z0-9]{1,20}.[a-zA-Z]{2,3}$";

        Matcher mat1 = Pattern.compile(EMAIL_PATTERN).matcher(email);
        Matcher mat2 = Pattern.compile(EMAIL_PATTERN2).matcher(email);
        Matcher mat3 = Pattern.compile(EMAIL_PATTERNa).matcher(email);
        Matcher mat4 = Pattern.compile(EMAIL_PATTERN2a).matcher(email);
        Matcher mat5 = Pattern.compile(EMAIL_PATTERNb0).matcher(email);
        Matcher mat6 = Pattern.compile(EMAIL_PATTERNb1).matcher(email);
        Matcher mat7 = Pattern.compile(EMAIL_PATTERNb2).matcher(email);
        Matcher mat8 = Pattern.compile(EMAIL_PATTERNb3).matcher(email);
        Matcher mat9 = Pattern.compile(EMAIL_PATTERNb4).matcher(email);
        Matcher mat10 = Pattern.compile(EMAIL_PATTERNb5).matcher(email);

        status = mat1.matches() || mat2.matches() || mat3.matches() || mat4.matches() || mat5.matches()
                || mat6.matches() || mat7.matches() || mat8.matches() || mat9.matches() || mat10.matches();
        return status;
    }

    public static boolean web_val(String web) {

        boolean status = false;

        String WEB_PATTERN1 = "^[a-zA-Z0-9]{1,20}.[a-zA-Z]{2,3}$";
        String WEB_PATTERN2 = "^[a-zA-Z0-9]{1,3}.[a-zA-Z]{2,3}.[a-zA-Z]{2,3}$";
        String WEB_PATTERN3 = "^[a-zA-Z0-9]{1,20}.[a-zA-Z]{2,3}.[a-zA-Z]{2,3}$";
        String WEB_PATTERN4 = "^[a-zA-Z0-9]{1,3}.[a-zA-Z0-9]{1,20}.[a-zA-Z]{2,3}.[a-zA-Z]{2,3}$";

        Matcher mat1 = Pattern.compile(WEB_PATTERN1).matcher(web);
        Matcher mat2 = Pattern.compile(WEB_PATTERN2).matcher(web);
        Matcher mat3 = Pattern.compile(WEB_PATTERN3).matcher(web);
        Matcher mat4 = Pattern.compile(WEB_PATTERN4).matcher(web);

        status = mat1.matches() || mat2.matches() || mat3.matches() || mat4.matches();
        return status;
    }

}
