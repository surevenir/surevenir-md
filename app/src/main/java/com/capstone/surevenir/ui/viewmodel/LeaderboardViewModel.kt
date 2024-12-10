package com.capstone.surevenir.ui.viewmodel

import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.surevenir.data.network.response.LeaderboardUser
import com.capstone.surevenir.data.repository.LeaderboardRepository
import com.capstone.surevenir.helper.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val repository: LeaderboardRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _leaderboardState = MutableStateFlow<List<LeaderboardUser>>(emptyList())
    val leaderboardState: StateFlow<List<LeaderboardUser>> = _leaderboardState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _currentUserPoints = MutableStateFlow(0)
    val currentUserPoints: StateFlow<Int> = _currentUserPoints.asStateFlow()

    fun fetchLeaderboard(token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.getLeaderboard(token).onSuccess { response ->
                    _leaderboardState.value = response.data
                    val userEmail = userPreferences.userEmail.first()
                    val currentUser = response.data.find { it.email == userEmail }
                    _currentUserPoints.value = currentUser?.points ?: 0
                }.onFailure { exception ->
                    _error.value = exception.message
                }
            } finally {
                _isLoading.value = false
            }
        }
    }
}