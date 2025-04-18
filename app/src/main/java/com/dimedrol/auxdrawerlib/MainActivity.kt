package com.dimedrol.auxdrawerlib

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dimedrol.auxdrawerlib.ui.theme.AuxDrawerLibTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AuxDrawerLibTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MyScreen()
                }
            }
        }
    }
}

@Composable
fun MyScreen() {
    AuxDrawer(
        mainContentBackgroundColor = Color.Blue,
        drawerElevation = 0.dp,
        cornerRadius = 50.dp,
        drawerBackgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
        drawerContent = { scope, closeDrawer ->
            // Твой контент для Drawer
            Text("Drawer Content", modifier = Modifier.clickable { closeDrawer() })
        },
        mainContent = { onMenuClick, isDrawerOpen, closeDrawer ->
            // Твой контент для Main
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Main Content")
                Button(onClick = { onMenuClick() }) {
                    Text("Open Drawer")
                }
            }
        }
    )
}
