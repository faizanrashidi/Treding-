package com.example.data

object SensitiveDataFilter {
    // Regex patterns for sensitive financial and credential info
    private val PAN_REGEX = Regex("\\b[A-Z]{5}[0-9]{4}[A-Z]\\b", RegexOption.IGNORE_CASE)
    private val OTP_REGEX = Regex("(?i)\\b(?:otp|code|pin|verification)[\\s:]*([0-9]{4,6})\\b")
    private val UPI_PIN_REGEX = Regex("(?i)\\b(?:upi[\\s_-]?pin|mpin)[\\s:]*([0-9]{4,6})\\b")
    private val ACCOUNT_NUMBER_REGEX = Regex("\\b(?:a/c|acct|acc(?:ount)?)[\\s:#]*([0-9]{9,18})\\b", RegexOption.IGNORE_CASE)
    private val GENERIC_CARD_ACC_REGEX = Regex("\\b[0-9]{4}[\\s-]?[0-9]{4}[\\s-]?[0-9]{4,6}\\b")
    private val PASSWORD_TOKEN_REGEX = Regex("(?i)\\b(?:pwd|password|secret|token|bearer)[\\s:=]+([A-Za-z0-9_\\-.~!@#\$%^&*]{4,})\\b")

    data class FilterResult(
        val sanitizedText: String,
        val maskedCount: Int,
        val detectedTypes: List<String>
    )

    fun sanitizeText(input: String): FilterResult {
        var text = input
        var count = 0
        val detected = mutableListOf<String>()

        if (PAN_REGEX.containsMatchIn(text)) {
            text = PAN_REGEX.replace(text) {
                count++
                detected.add("PAN Number")
                "[REDACTED_PAN]"
            }
        }

        if (OTP_REGEX.containsMatchIn(text)) {
            text = OTP_REGEX.replace(text) { match ->
                count++
                detected.add("OTP/Verification")
                val prefix = match.value.substring(0, match.value.length - match.groupValues[1].length)
                "${prefix}******"
            }
        }

        if (UPI_PIN_REGEX.containsMatchIn(text)) {
            text = UPI_PIN_REGEX.replace(text) { match ->
                count++
                detected.add("UPI PIN/MPIN")
                val prefix = match.value.substring(0, match.value.length - match.groupValues[1].length)
                "${prefix}****"
            }
        }

        if (ACCOUNT_NUMBER_REGEX.containsMatchIn(text)) {
            text = ACCOUNT_NUMBER_REGEX.replace(text) { match ->
                count++
                detected.add("Bank Account Number")
                val prefix = match.value.substring(0, match.value.length - match.groupValues[1].length)
                "${prefix}••••••••"
            }
        }

        if (GENERIC_CARD_ACC_REGEX.containsMatchIn(text)) {
            text = GENERIC_CARD_ACC_REGEX.replace(text) {
                count++
                detected.add("Card/Account Digits")
                "•••• •••• ••••"
            }
        }

        if (PASSWORD_TOKEN_REGEX.containsMatchIn(text)) {
            text = PASSWORD_TOKEN_REGEX.replace(text) { match ->
                count++
                detected.add("Password/Auth Token")
                val prefix = match.value.substring(0, match.value.length - match.groupValues[1].length)
                "${prefix}[PROTECTED]"
            }
        }

        return FilterResult(
            sanitizedText = text,
            maskedCount = count,
            detectedTypes = detected.distinct()
        )
    }
}
