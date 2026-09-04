package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SignalBuyBorder
import com.example.ui.theme.SignalBuyGreen
import com.example.ui.theme.SignalBuyGreenLight
import com.example.ui.theme.TextMutedDark
import com.example.ui.theme.TextSecondaryDark
import com.example.ui.theme.TradeCardSurface

@Composable
fun SensitiveDataShieldBanner(
    maskedCount: Int = 0,
    modifier: Modifier = Modifier
) {
    val cardShape = RoundedCornerShape(14.dp)
    Box(
        modifier = modifier
            .testTag("sensitive_data_shield_banner")
            .fillMaxWidth()
            .clip(cardShape)
            .background(Color(0xFF071410))
            .border(1.dp, SignalBuyBorder, cardShape)
            .padding(horizontal = 14.dp, vertical = 9.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(SignalBuyGreen.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = "Shield Active",
                    tint = SignalBuyGreenLight,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "SENSITIVE DATA SHIELD: ACTIVE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = SignalBuyGreenLight,
                        letterSpacing = 0.8.sp
                    )
                    if (maskedCount > 0) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFF0F382B))
                                .padding(horizontal = 6.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = "$maskedCount Masked",
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = SignalBuyGreenLight
                            )
                        }
                    }
                }
                Text(
                    text = "PAN, OTPs, PINs, bank accounts & credentials sanitized locally prior to analysis.",
                    fontSize = 10.5.sp,
                    color = Color(0xFF94A3B8),
                    lineHeight = 13.5.sp
                )
            }
        }
    }
}
