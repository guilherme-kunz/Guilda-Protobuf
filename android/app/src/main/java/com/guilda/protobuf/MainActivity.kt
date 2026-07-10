package com.guilda.protobuf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.guilda.protobuf.ui.AppNavGraph
import com.guilda.protobuf.ui.theme.GuildaProtobufTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GuildaProtobufTheme {
                AppNavGraph()
            }
        }
    }
}
