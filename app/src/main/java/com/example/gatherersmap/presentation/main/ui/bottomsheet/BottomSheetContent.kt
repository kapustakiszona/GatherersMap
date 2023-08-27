//@file:OptIn(
//    ExperimentalMaterial3Api::class
//)
//
//package com.example.gatherersmap.presentation.main.ui.bottomsheet
//
//import androidx.compose.material3.BottomSheetScaffoldState
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.runtime.Composable
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.gatherersmap.navigation.BottomSheetScreenState
//import com.example.gatherersmap.presentation.main.ui.map.MapEvent
//import com.example.gatherersmap.presentation.main.vm.MapViewModel
//import kotlinx.coroutines.CoroutineScope
//
//@Composable
//fun BottomSheetContent(
//    currentSheetState: BottomSheetScreenState,
//    scaffoldState: BottomSheetScaffoldState,
//    coroutineScope: CoroutineScope,
//    viewModel: MapViewModel = viewModel(),
//) {
//    when (currentSheetState) {
//        is BottomSheetScreenState.Add -> {
//            viewModel.setVisibilities(BottomSheetVisibility.SHOW)
//            EditDetailsSheetContent(
//                itemSpot = currentSheetState.itemSpot,
//                onCancelClicked = {
//                    viewModel.onEvent(MapEvent.Initial)
//                    viewModel.setVisibilities(BottomSheetVisibility.HIDE)
//                },
//                onSaveClicked = { itemSpot ->
//                    viewModel.insertItemSpot(itemSpot)
//                    viewModel.setVisibilities(BottomSheetVisibility.HIDE)
//                }
//            )
//        }
//
//        is BottomSheetScreenState.Details -> {
//            viewModel.setVisibilities(BottomSheetVisibility.SHOW)
//            DetailsSheetContent(
//                itemSpot = currentSheetState.itemSpot,
//                onEditClickListener = { itemSpot ->
//                    viewModel.setVisibilities(BottomSheetVisibility.HIDE)
//                    viewModel.onEvent(MapEvent.OnEditItemClick(itemSpot))
//                },
//                onDeleteClickListener = { itemSpot ->
//                    viewModel.onEvent(MapEvent.OnDeleteItemClick(itemSpot))
//                }
//            )
//        }
//
//        is BottomSheetScreenState.Edit -> {
//            viewModel.setVisibilities(BottomSheetVisibility.SHOW)
//            EditDetailsSheetContent(
//                itemSpot = currentSheetState.itemSpot,
//                onCancelClicked = { itemSpot ->
//                    viewModel.onEvent(MapEvent.OnDetailsItemClick(itemSpot))
//                    viewModel.setVisibilities(BottomSheetVisibility.HIDE)
//                },
//                onSaveClicked = { itemSpot ->
//                    viewModel.updateItemSpot(itemSpot)
//                    viewModel.setVisibilities(BottomSheetVisibility.HIDE)
//                    viewModel.onEvent(MapEvent.OnDetailsItemClick(itemSpot))
//                }
//            )
//        }
//
//        BottomSheetScreenState.Initial -> {
//        }
//
//    }
//}
