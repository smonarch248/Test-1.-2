package com.terminators.android

import android.os.Bundle
import android.graphics.BitmapFactory
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.io.File

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

                Box(Modifier.weight(1f).fillMaxWidth()) {
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
                        "Profile" -> ProfileScreen()
                    }
                }

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
fun ProfileScreen() {
    val context = LocalContext.current
    val prefs = remember {
        context.getSharedPreferences("terminators_profile", 0)
    }

    var name by remember {
        mutableStateOf(prefs.getString("name", "") ?: "")
    }
    var photoPath by remember {
        mutableStateOf(prefs.getString("photo", null))
    }
    var saved by remember { mutableStateOf(false) }
    var photoError by remember { mutableStateOf(false) }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            try {
                val destination = File(context.filesDir, "profile-photo.jpg")
                context.contentResolver.openInputStream(uri)?.use { input ->
                    destination.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                photoPath = destination.absolutePath
                prefs.edit().putString("photo", photoPath).apply()
                photoError = false
            } catch (e: Exception) {
                photoError = true
            }
        }
    }

    val bitmap = remember(photoPath) {
        photoPath?.let { BitmapFactory.decodeFile(it)?.asImageBitmap() }
    }

    Column(
        Modifier.fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Your Profile",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(8.dp))
        Text(
            "Manage how you appear in TERMINATORS.",
            color = Muted,
            fontSize = 13.sp
        )

        Spacer(Modifier.height(30.dp))

        Box(
            Modifier.size(116.dp)
                .background(Panel, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (bitmap != null) {
                Image(
                    bitmap = bitmap,
                    contentDescription = "Profile picture",
                    modifier = Modifier.fillMaxSize().clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text(
                    name.trim().firstOrNull()?.uppercase() ?: "?",
                    color = Accent,
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        OutlinedButton(
            onClick = { imagePicker.launch("image/*") },
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Accent
            )
        ) {
            Text("Choose profile picture")
        }

        if (photoError) {
            Text("Couldn't load that picture. Please try another.",
                color = Color.Red, fontSize = 12.sp)
        }

        Spacer(Modifier.height(32.dp))

        Column(Modifier.fillMaxWidth()) {
            Text(
                "DISPLAY NAME",
                color = Muted,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    saved = false
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter your name") },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Accent,
                    unfocusedBorderColor = Muted,
                    cursorColor = Accent
                )
            )
        }

        Spacer(Modifier.height(22.dp))

        Button(
            onClick = {
                prefs.edit().putString("name", name.trim()).apply()
                saved = true
            },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Accent
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Save profile", fontWeight = FontWeight.Bold)
        }

        if (saved) {
            Spacer(Modifier.height(12.dp))
            Text("Profile saved on this device.",
                color = Color(0xFF4ADE80), fontSize = 13.sp)
        }

        Spacer(Modifier.height(24.dp))

        Text(
            "Your profile is currently stored on this phone. Account synchronisation will come when native sign-in is connected.",
            color = Muted,
            fontSize = 12.sp
        )
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