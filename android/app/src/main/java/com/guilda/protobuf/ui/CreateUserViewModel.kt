package com.guilda.protobuf.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guilda.protobuf.data.model.Role
import com.guilda.protobuf.data.model.User
import com.guilda.protobuf.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CreateUserFormState(
    val name: String = "",
    val email: String = "",
    val selectedRoles: Set<Role> = setOf(Role.ROLE_VIEWER),
    val isLoading: Boolean = false,
    val error: String? = null,
    val createdUser: User? = null,
)

@HiltViewModel
class CreateUserViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _formState = MutableStateFlow(CreateUserFormState())
    val formState: StateFlow<CreateUserFormState> = _formState.asStateFlow()

    fun onNameChange(value: String) =
        _formState.update { it.copy(name = value, error = null) }

    fun onEmailChange(value: String) =
        _formState.update { it.copy(email = value, error = null) }

    fun onRoleToggle(role: Role) = _formState.update { state ->
        val roles = state.selectedRoles.toMutableSet()
        if (role in roles) roles.remove(role) else roles.add(role)
        state.copy(selectedRoles = roles, error = null)
    }

    fun submit() {
        val state = _formState.value
        if (state.name.isBlank() || state.email.isBlank()) {
            _formState.update { it.copy(error = "Nome e e-mail são obrigatórios") }
            return
        }
        viewModelScope.launch {
            _formState.update { it.copy(isLoading = true, error = null) }
            repository.createUser(
                name  = state.name.trim(),
                email = state.email.trim(),
                roles = state.selectedRoles.toList(),
            )
                .onSuccess { user -> _formState.update { it.copy(isLoading = false, createdUser = user) } }
                .onFailure { e -> _formState.update { it.copy(isLoading = false, error = e.message ?: "Erro ao criar usuário") } }
        }
    }
}
