package net.mrmdi.pmp.bmi_kotlin.data

import androidx.compose.ui.graphics.Color
import net.mrmdi.pmp.bmi_kotlin.ui.theme.*

data class BMIInput(
    val height: Double,
    val weight: Double
)

data class BMIResult(
    val bmi: Double,
    val category: BMICategory
)

enum class BMICategory() {
    UNDEFINED,
    UNDERWEIGHT,
    NORMAL,
    OVERWEIGHT,
    OBESITY,
    CLASS_3_OBESITY;

    fun getColor(): Color {
        return when (this) {
            // from Color.kt
            UNDEFINED ->ColorUndefined
            UNDERWEIGHT -> ColorUnderweight
            NORMAL -> ColorNormal
            OVERWEIGHT -> ColorOverweight
            OBESITY -> ColorObesity
            CLASS_3_OBESITY -> ColorClass3Obesity
        }
    }
}