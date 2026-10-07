package com.mobilkontrol.ui

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.mobilkontrol.data.repository.ParentalRepository
import com.mobilkontrol.security.PinHasher
import com.mobilkontrol.ui.theme.MobilKontrolTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LockActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MobilKontrolTheme {
                LockScreenContent(
                    onParentUnlock = { pin ->
                        lifecycleScope.launch {
                            val repo = ParentalRepository(applicationContext)
                            val stored = repo.settings.pinHash.first()
                            if (stored != null && PinHasher.verify(pin, stored)) {
                                repo.settings.setManualLock(false)
                                repo.settings.setFrozen(false)
                                finish()
                            }
                        }
                    }
                )
            }
        }
    }

    override fun onDestroy() {}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LockScreenContent(onParentUnlock: (String) -> Unit) {
    var showPin by remember { mutableStateOf(false) }
    var pin by remember { mutableStateOf("") }

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Bugünkü kullanım süren doldu", fontSize = 28.sp, textAlign = TextAlign.Center)
            Text("Yarın tekrar kullanabilirsin.", fontSize = 18.sp, modifier = Modifier.padding(top = 8.dp))
            if (!showPin) {
                Button(onClick = { showPin = true }, modifier = Modifier.padding(top = 24.dp)) {
                    Text("Ebeveyn Girişi")
                }
            } else {
                TextField(value = pin, onValueChange = { pin = it }, label = { Text("Ebeveyn PIN") })
                Button(onClick = { onParentUnlock(pin) }, modifier = Modifier.padding(top = 12.dp)) {
                    Text("Aç")
                }
            }
        }
    }
}
