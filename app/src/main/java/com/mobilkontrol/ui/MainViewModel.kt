package com.mobilkontrol.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mobilkontrol.data.local.DailyUsageEntity
import com.mobilkontrol.data.local.UsageHistoryEntity
import com.mobilkontrol.data.repository.ParentalRepository
import com.mobilkontrol.security.PinHasher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = ParentalRepository(app)

    val pinHash = repo.settings.pinHash.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    val dailyLimit = repo.settings.dailyLimit.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 60)
    val setupDone = repo.settings.setupDone.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
    val frozen = repo.settings.frozen.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
    val manualLock = repo.settings.manualLock.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    private val _today = MutableStateFlow<DailyUsageEntity?>(null)
    val today: StateFlow<DailyUsageEntity?> = _today

    private val _history = MutableStateFlow<List<UsageHistoryEntity>>(emptyList())
    val history: StateFlow<List<UsageHistoryEntity>> = _history

    init {
        viewModelScope.launch {
            repo.ensureToday()
            repo.observeToday().collect { _today.value = it }
        }
        viewModelScope.launch {
            repo.observeHistoryLast7().collect { _history.value = it }
        }
    }

    fun refreshToday() {
        viewModelScope.launch { repo.ensureToday() }
    }

    fun setPin(pin: String) {
        viewModelScope.launch { repo.settings.setPinHash(PinHasher.hash(pin)) }
    }

    fun setLimit(minutes: Int) {
        viewModelScope.launch { repo.settings.setDailyLimit(minutes) }
    }

    fun finishSetup() {
        viewModelScope.launch { repo.settings.setSetupDone(true) }
    }

    suspend fun verifyPin(pin: String): Boolean {
        val stored = repo.settings.pinHash.first() ?: return false
        return PinHasher.verify(pin, stored)
    }

    fun addBonus(minutes: Int) {
        viewModelScope.launch { repo.addBonusMinutes(minutes) }
    }

    fun setFrozenState(value: Boolean) {
        viewModelScope.launch { repo.settings.setFrozen(value) }
    }

    fun lockNow() {
        viewModelScope.launch { repo.setManualLock(true) }
    }

    fun unlock() {
        viewModelScope.launch {
            repo.setManualLock(false)
            repo.settings.setFrozen(false)
        }
    }

    val warn15On = repo.settings.warn15On.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)
    val warn5On = repo.settings.warn5On.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)
    val warn1On = repo.settings.warn1On.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)
    val remoteOn = repo.settings.remoteOn.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun setWarn15(v: Boolean) = viewModelScope.launch { repo.settings.setWarn15On(v) }
    fun setWarn5(v: Boolean) = viewModelScope.launch { repo.settings.setWarn5On(v) }
    fun setWarn1(v: Boolean) = viewModelScope.launch { repo.settings.setWarn1On(v) }
    fun setRemote(v: Boolean) = viewModelScope.launch { repo.settings.setRemoteOn(v) }

    suspend fun changePin(current: String, new: String): Boolean {
        if (!verifyPin(current) || new.length !in 4..6) return false
        setPin(new)
        return true
    }

    fun remainingMinutes(): Int {
        val usage = _today.value ?: return dailyLimit.value
        return com.mobilkontrol.data.TimeMath.remainingMinutes(dailyLimit.value, usage.bonusMinutes, usage.usedMinutes)
    }
}
