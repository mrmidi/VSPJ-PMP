package net.mrmidi.pmp.bank.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _accountId = MutableLiveData<Int>()
    val accountId: LiveData<Int> = _accountId

    fun setAccountId(accountId: Int) {
        _accountId.value = accountId
    }
}
