package com.charan.bingediary.presentation.person

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charan.bingediary.data.repository.TmdbRepository
import com.charan.bingediary.presentation.common.mapper.toPersonUiModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class PersonViewModel(
    private val tmdbRepository: TmdbRepository,
    @InjectedParam private val personId: Long
) : ViewModel() {

    private val _state = MutableStateFlow(PersonState())
    val state: StateFlow<PersonState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<PersonEffect>()
    val effect: SharedFlow<PersonEffect> = _effect.asSharedFlow()

    init {
        loadPersonDetails(personId)
    }

    fun onEvent(event: PersonEvent) {
        when (event) {
            is PersonEvent.LoadPerson -> loadPersonDetails(event.personId)
            PersonEvent.NavigateBack -> emitEffect(PersonEffect.NavigateBack)
            is PersonEvent.OnCreditClick -> emitEffect(PersonEffect.NavigateToContentDetails(event.mediaId, event.mediaType))
        }
    }

    private fun loadPersonDetails(personId: Long) {
        _state.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            tmdbRepository.getPersonDetails(personId).fold(
                onSuccess = { dto ->
                    _state.update { 
                        it.copy(
                            isLoading = false,
                            person = dto.toPersonUiModel()
                        )
                    }
                },
                onFailure = { error ->
                    _state.update { 
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Failed to load person details"
                        )
                    }
                }
            )
        }
    }

    private fun emitEffect(effect: PersonEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }
}
