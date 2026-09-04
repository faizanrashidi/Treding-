package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("TRADEAI", appName)
  }

  @Test
  fun `sensitive data filter sanitizes credentials`() {
    val input = "Order placed for PAN ABCDE1234F with OTP 987654 and account 123456789012"
    val result = com.example.data.SensitiveDataFilter.sanitizeText(input)
    assert(result.maskedCount >= 3)
    assert(!result.sanitizedText.contains("ABCDE1234F"))
    assert(!result.sanitizedText.contains("987654"))
  }
}

