package dev.krazymanj.shopito.views.template

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.krazymanj.shopito.database.ITemplateLocalRepository
import dev.krazymanj.shopito.views.template.TemplateActions
import dev.krazymanj.shopito.views.template.TemplateUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class TemplateViewModel @Inject constructor(private val repository: ITemplateLocalRepository) : ViewModel(),
    TemplateActions {

    private val _state : MutableStateFlow<TemplateUIState> = MutableStateFlow(value = TemplateUIState())

    val templateUIState = _state.asStateFlow()
}