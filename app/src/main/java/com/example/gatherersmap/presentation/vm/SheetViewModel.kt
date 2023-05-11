package com.example.gatherersmap.presentation.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gatherersmap.navigation.BottomSheetScreenState

class SheetViewModel : ViewModel() {
    private val _sheetState = MutableLiveData<BottomSheetScreenState>(BottomSheetScreenState.Start)
    val sheetState: LiveData<BottomSheetScreenState> = _sheetState


}