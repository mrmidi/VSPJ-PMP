package net.mrmdi.pmp.bmi_kotlin.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.livedata.*

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import net.mrmdi.pmp.bmi_kotlin.data.BMIRepository
import net.mrmdi.pmp.bmi_kotlin.data.BMIResult

class BMIViewModel : ViewModel() {

    private val repository = BMIRepository() // Assuming BMIRepository is not an inner class

    // Using StateFlow for Compose state management
    private val _bmiResult = MutableStateFlow<BMIResult?>(null)
    val bmiResult: StateFlow<BMIResult?> = _bmiResult

    // Function to calculate BMI and update UI states
    fun calculateBMI(height: String, weight: String) {
        viewModelScope.launch {

            val result = repository.calculateBMI(height, weight)
            _bmiResult.value = result
        }
    }

    // Helper properties for UI components
    val bmiValue: Double
        get() = bmiResult.value?.bmi ?: 0.0

    val bmiFormatted: String
        get() = String.format("%.2f", bmiValue)

    val bmiDescription: String
        get() = bmiResult.value?.category?.name ?: "Undefined"

    val bmiColor: Color
        get() = bmiResult.value?.category?.getColor() ?: Color.Black // Default color
}
