package net.mrmdi.pmp.bmi_kotlin.data

import androidx.compose.ui.graphics.Color

class BMIRepository {
    fun calculateBMI(height: String, weight: String): BMIResult? {
        if (height.isEmpty() || weight.isEmpty()) {
            return null
        } else if (height.toDouble() == 0.0 || weight.toDouble() == 0.0) {
            return BMIResult(0.0, BMICategory.UNDEFINED) // Return undefined if height or weight is 0
        }
        val heightInMeters = height.toDouble() / 100
        val bmi = weight.toDouble() / (heightInMeters * heightInMeters)
        val category = getCategory(bmi)

        return BMIResult(bmi, category)
    }


        private fun getCategory(bmi: Double): BMICategory {
            return when {
                bmi == 0.0 -> BMICategory.UNDEFINED
                bmi < 18.5 -> BMICategory.UNDERWEIGHT
                bmi < 24.9 -> BMICategory.NORMAL
                bmi < 29.9 -> BMICategory.OVERWEIGHT
                bmi < 34.9 -> BMICategory.OBESITY
                else -> BMICategory.CLASS_3_OBESITY
            }
        }
    }