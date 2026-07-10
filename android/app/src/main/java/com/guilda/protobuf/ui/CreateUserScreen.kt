package com.guilda.protobuf.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.guilda.protobuf.data.model.Role

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CreateUserScreen(
    onNavigateBack: () -> Unit,
    viewModel: CreateUserViewModel = hiltViewModel(),
) {
    val state by viewModel.formState.collectAsStateWithLifecycle()

    LaunchedEffect(state.createdUser) {
        if (state.createdUser != null) onNavigateBack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Novo Usuário") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value         = state.name,
                onValueChange = viewModel::onNameChange,
                label         = { Text("Nome") },
                modifier      = Modifier.fillMaxWidth(),
                singleLine    = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                isError       = state.error != null && state.name.isBlank(),
            )

            OutlinedTextField(
                value         = state.email,
                onValueChange = viewModel::onEmailChange,
                label         = { Text("E-mail") },
                modifier      = Modifier.fillMaxWidth(),
                singleLine    = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction    = ImeAction.Done,
                ),
                isError = state.error != null && state.email.isBlank(),
            )

            Text("Permissões", style = MaterialTheme.typography.labelLarge)

            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AVAILABLE_ROLES.forEach { (role, label) ->
                    FilterChip(
                        selected = role in state.selectedRoles,
                        onClick  = { viewModel.onRoleToggle(role) },
                        label    = { Text(label) },
                    )
                }
            }

            if (state.error != null) {
                Text(
                    text  = state.error!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick  = viewModel::submit,
                modifier = Modifier.fillMaxWidth(),
                enabled  = !state.isLoading,
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier    = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color       = MaterialTheme.colorScheme.onPrimary,
                    )
                } else {
                    Text("Criar Usuário")
                }
            }
        }
    }
}

private val AVAILABLE_ROLES = listOf(
    Role.ROLE_ADMIN  to "Admin",
    Role.ROLE_EDITOR to "Editor",
    Role.ROLE_VIEWER to "Viewer",
)
