package swu.xl.trafficsystem.util

import java.util.regex.Matcher
import java.util.regex.Pattern

object PhoneUtil {
    fun isPhoneNumberValid(phoneNumber: String): Boolean {
        var isValid = false

        //valid phone number format;
        val expression1 = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{5})$"

        /**
         * valid phone number format;
         */
        val expression2 = "^\\(?(\\d{3})\\)?[- ]?(\\d{4})[- ]?(\\d{4})$"
        val inputStr: CharSequence = phoneNumber
        val pattern1: Pattern = Pattern.compile(expression1)
        val matcher1: Matcher = pattern1.matcher(inputStr)
        val pattern2: Pattern = Pattern.compile(expression2)
        val matcher2: Matcher = pattern2.matcher(inputStr)
        if (matcher1.matches() || matcher2.matches()) {
            isValid = true
        }
        return isValid
    }
}