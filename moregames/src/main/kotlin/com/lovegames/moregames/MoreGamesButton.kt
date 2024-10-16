package com.lovegames.moregames

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@Composable
fun MoreGamesButton(onClick: () -> Unit, showAd: () -> Unit) {
    Button(
        onClick = {
            onClick()
            showAd()
        }
    ) {
        Text(text = stringResource(R.string.more_games))
    }
}
