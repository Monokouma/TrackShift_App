package com.despaircorp.feature_home.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.despaircorp.feature_home.view_model.HomeViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel()
) {

}