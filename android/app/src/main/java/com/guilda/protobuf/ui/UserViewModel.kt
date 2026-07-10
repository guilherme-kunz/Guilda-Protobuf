package com.guilda.protobuf.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guilda.protobuf.data.model.User
import com.guilda.protobuf.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface UiState {
    data object Loading : UiState
    data class Success(val users: List<User>) : UiState
    data class Error(val message: String) : UiState
}

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun loadUsers() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            repository.getUsers()
                .onSuccess { users -> _uiState.value = UiState.Success(users) }
                .onFailure { e  -> _uiState.value = UiState.Error(e.message ?: "Erro desconhecido") }
        }
    }

    fun deleteUser(id: String) {
        viewModelScope.launch {
            repository.deleteUser(id)
                .onSuccess { loadUsers() }
                .onFailure { e -> _uiState.value = UiState.Error(e.message ?: "Erro ao deletar usuário") }
        }
    }
}
