package com.terminators.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Bg = Color(0xFF090B10)
private val Panel = Color(0xFF151923)
private val Accent = Color(0xFF8B5CF6)
private val Muted = Color(0xFF9299AA)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { TerminatorsApp() }
    }
}

@Composable
fun TerminatorsApp() {
    var tab by remember { mutableStateOf("Community") }

    MaterialTheme(colorScheme = darkColorScheme(primary = Accent)) {
        Surface(Modifier.fillMaxSize(), color = Bg) {
            Column(Modifier.fillMaxSize()) {
                Row(
                    Modifier.fillMaxWidth().padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(
                            "TERMINATORS",
                            color = Color.White,
                            fontSize = 23.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            "YOUR COMMUNITY. YOUR RULES.",
                            color = Muted,
                            fontSize = 9.sp,
                            letterSpacing = 1.5.sp
                        )
                    }
                    Box(
                        Modifier.size(42.dp).background(Panel, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("S", color = Accent, fontWeight = FontWeight.Bold)
                    }
                }

                when (tab) {
                    "Community" -> Community()
                    "Members" -> Section(
                        "Members",
                        "Your approved community members will appear here."
                    )
                    "Admin" -> Section(
                        "Administration",
                        "Community management and chat controls will appear here."
                    )
                    "Profile" -> Section(
                        "Profile",
                        "Your name and profile picture will appear here."
                    )
                }

                Spacer(Modifier.weight(1f))

                NavigationBar(containerColor = Panel) {
                    listOf("Community", "Members", "Admin", "Profile").forEach {
                        NavigationBarItem(
                            selected = tab == it,
                            onClick = { tab = it },
                            icon = {
                                Text(
                                    when (it) {
                                        "Community" -> "⌂"
                                        "Members" -> "◎"
                                        "Admin" -> "⚙"
                                        else -> "●"
                                    }
                                )
                            },
                            label = { Text(it, fontSize = 10.sp) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Community() {
    Column(Modifier.fillMaxWidth().padding(horizontal = 18.dp)) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Panel),
            shape = RoundedCornerShape(18.dp)
        ) {
            Column(Modifier.fillMaxWidth().padding(20.dp)) {
                Text("THE COMMUNITY", color = Muted, fontSize = 10.sp)
                Spacer(Modifier.height(7.dp))
                Text(
                    "Shared group",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    "One space for everyone in TERMINATORS.",
                    color = Muted,
                    fontSize = 13.sp
                )
            }
        }

        Spacer(Modifier.height(24.dp))
        Text(
            "Community feed",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(12.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = Panel),
            shape = RoundedCornerShape(18.dp)
        ) {
            Column(
                Modifier.fillMaxWidth().padding(25.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("✦", color = Accent, fontSize = 30.sp)
                Spacer(Modifier.height(12.dp))
                Text(
                    "Your shared space starts here",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "Messages will appear here after we connect the app to its server.",
                    color = Muted,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun Section(title: String, description: String) {
    Column(Modifier.fillMaxWidth().padding(20.dp)) {
        Text(
            title,
            color = Color.White,
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(15.dp))
        Card(colors = CardDefaults.cardColors(containerColor = Panel)) {
            Text(
                description,
                Modifier.padding(20.dp),
                color = Muted,
                fontSize = 14.sp
            )
        }
    }
}
