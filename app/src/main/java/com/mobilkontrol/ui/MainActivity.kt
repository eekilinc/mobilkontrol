package com.mobilkontrol.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mobilkontrol.service.ScreenTrackingService
import com.mobilkontrol.ui.theme.MobilKontrolTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startForegroundService(Intent(this, ScreenTrackingService::class.java))

        setContent {
            MobilKontrolTheme {
                val vm = androidx.lifecycle.viewmodel.compose.viewModel<MainViewModel>()
                val setupDone by vm.setupDone.collectAsStateWithLifecycle()
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "splash") {
                    composable("splash") {
                        SplashScreen(onContinue = {
                            navController.navigate(if (setupDone) "child" else "onboarding") {
                                popUpTo("splash") { inclusive = true }
                            }
                        })
                    }
                    composable("onboarding") {
                        OnboardingScreen(
                            onSetPin = vm::setPin,
                            onSetLimit = vm::setLimit,
                            onFinish = {
                                vm.finishSetup()
                                navController.navigate("child") { popUpTo("onboarding") { inclusive = true } }
                            },
                            onRequestUsageAccess = {
                                startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
                            }
                        )
                    }
                    composable("child") {
                        ChildHomeScreen(
                            vm = vm,
                            onParentLogin = { navController.navigate("parent_login") }
                        )
                    }
                    composable("parent_login") {
                        ParentLoginScreen(
                            onSuccess = {
                                navController.navigate("dashboard") { popUpTo("parent_login") { inclusive = true } }
                            },
                            verify = { vm.verifyPin(it) }
                        )
                    }
                    composable("dashboard") {
                        DashboardScreen(
                            vm = vm,
                            onAddTime = { navController.navigate("add_time") },
                            onLimit = { navController.navigate("limit") },
                            onStats = { navController.navigate("stats") },
                            onSettings = { navController.navigate("settings") },
                            onBack = { navController.popBackStack() }
                        )
                    }
                    composable("add_time") {
                        AddTimeScreen(vm = vm, onBack = { navController.popBackStack() })
                    }
                    composable("limit") {
                        LimitScreen(vm = vm, onBack = { navController.popBackStack() })
                    }
                    composable("stats") {
                        StatsScreen(vm = vm, onBack = { navController.popBackStack() })
                    }
                    composable("settings") {
                        SettingsScreen(vm = vm, onBack = { navController.popBackStack() })
                    }
                }
            }
        }
    }
}

@Composable
fun SplashScreen(onContinue: () -> Unit) {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("MobilKontrol", fontSize = 32.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))
        Button(onClick = onContinue) { Text("Başla") }
    }
}

@Composable
fun OnboardingScreen(
    onSetPin: (String) -> Unit,
    onSetLimit: (Int) -> Unit,
    onFinish: () -> Unit,
    onRequestUsageAccess: () -> Unit
) {
    var step by remember { mutableIntStateOf(0) }
    var pin by remember { mutableStateOf("") }
    var limit by remember { mutableIntStateOf(60) }

    Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.Center) {
        when (step) {
            0 -> {
                Text("Hoş Geldiniz", fontSize = 28.sp)
                Text("Çocuğunuzun ekran süresini kolayca yönetin.")
            }
            1 -> {
                Text("Ebeveyn PIN'i Oluşturun", fontSize = 24.sp)
                TextField(value = pin, onValueChange = { if (it.length <= 6) pin = it }, label = { Text("4-6 haneli PIN") })
            }
            2 -> {
                Text("Günlük Kullanım Süresi", fontSize = 24.sp)
                LimitChooser(selected = limit, onSelect = { limit = it })
            }
            3 -> {
                Text("İzinler", fontSize = 24.sp)
                Text("Kullanım takibi için 'Kullanım Erişimi' izni gerekir.")
                Button(onClick = onRequestUsageAccess, modifier = Modifier.padding(top = 8.dp)) { Text("İzin Ver") }
            }
        }
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = {
                when (step) {
                    1 -> { if (pin.length in 4..6) { onSetPin(pin); step++ } }
                    2 -> { onSetLimit(limit); step++ }
                    3 -> onFinish()
                    else -> step++
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (step == 3) "Kurulumu Tamamla" else "İleri")
        }
    }
}

@Composable
fun LimitChooser(selected: Int, onSelect: (Int) -> Unit) {
    val options = listOf(30, 45, 60, 90, 120, 180)
    Column {
        options.forEach { opt ->
            Button(
                onClick = { onSelect(opt) },
                colors = if (opt == selected) ButtonDefaults.buttonColors() else ButtonDefaults.outlinedButtonColors(),
                modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)
            ) { Text("$opt dakika") }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChildHomeScreen(vm: MainViewModel, onParentLogin: () -> Unit) {
    val today by vm.today.collectAsStateWithLifecycle()
    val limit by vm.dailyLimit.collectAsStateWithLifecycle()
    val used = today?.usedMinutes ?: 0
    val bonus = today?.bonusMinutes ?: 0
    val remaining = (limit + bonus - used).coerceAtLeast(0)

    Scaffold(topBar = { TopAppBar(title = { Text("Çocuk Ekranı") }) }) { padding ->
        Column(Modifier.padding(padding).padding(24.dp).fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text("Bugünkü Kalan Süren", fontSize = 18.sp)
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(220.dp).padding(16.dp)) {
                CircularProgressIndicator(
                    progress = { (1f - (used.toFloat() / (limit + bonus).coerceAtLeast(1))).coerceIn(0f, 1f) },
                    modifier = Modifier.fillMaxSize(),
                    strokeWidth = 12.dp
                )
                Text("$remaining\ndakika", fontSize = 32.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            }
            LinearProgressIndicator(progress = { (used.toFloat() / (limit + bonus).coerceAtLeast(1)).coerceAtMost(1f) }, modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp))
            Text("Bugün $used dakika kullandın.")
            Spacer(Modifier.height(24.dp))
            Button(onClick = onParentLogin) { Text("Ebeveyn Girişi") }
        }
    }
}

@Composable
fun ParentLoginScreen(onSuccess: () -> Unit, verify: suspend (String) -> Boolean) {
    var pin by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    val scope = remember { kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main) }

    Column(Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("Ebeveyn Girişi", fontSize = 26.sp)
        TextField(value = pin, onValueChange = { pin = it }, label = { Text("PIN") })
        if (error.isNotEmpty()) Text(error, color = androidx.compose.ui.graphics.Color.Red)
        Button(onClick = {
            scope.launch {
                if (verify(pin)) onSuccess() else error = "PIN hatalı"
            }
        }, modifier = Modifier.padding(top = 12.dp)) { Text("Giriş") }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(vm: MainViewModel, onAddTime: () -> Unit, onLimit: () -> Unit, onStats: () -> Unit, onSettings: () -> Unit, onBack: () -> Unit) {
    val today by vm.today.collectAsStateWithLifecycle()
    val limit by vm.dailyLimit.collectAsStateWithLifecycle()
    val frozen by vm.frozen.collectAsStateWithLifecycle()
    val manualLock by vm.manualLock.collectAsStateWithLifecycle()
    val used = today?.usedMinutes ?: 0
    val bonus = today?.bonusMinutes ?: 0
    val remaining = (limit + bonus - used).coerceAtLeast(0)

    Scaffold(topBar = { TopAppBar(title = { Text("Ebeveyn Paneli") }, navigationIcon = { TextButton(onClick = onBack) { Text("Geri") } }) }) { padding ->
        Column(Modifier.padding(padding).padding(16.dp)) {
            StatCard("Bugünkü Kullanım", "$used dakika")
            StatCard("Günlük Limit", "$limit dakika" + if (bonus > 0) " (+$bonus ek)" else "")
            StatCard("Kalan Süre", "$remaining dakika")
            LinearProgressIndicator(progress = { (used.toFloat() / (limit + bonus).coerceAtLeast(1)).coerceAtMost(1f) }, modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp))
            Button(onClick = onAddTime, modifier = Modifier.fillMaxWidth()) { Text("Süre Ekle") }
            Spacer(Modifier.height(8.dp))
            Button(onClick = onLimit, modifier = Modifier.fillMaxWidth()) { Text("Günlük Süre Ayarı") }
            Spacer(Modifier.height(8.dp))
            Button(onClick = onStats, modifier = Modifier.fillMaxWidth()) { Text("İstatistikler") }
            Spacer(Modifier.height(8.dp))
            Button(onClick = onSettings, modifier = Modifier.fillMaxWidth()) { Text("Ayarlar") }
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Süreyi Dondur", modifier = Modifier.weight(1f))
                Switch(checked = frozen, onCheckedChange = { vm.setFrozenState(it) })
            }
            Button(onClick = { vm.lockNow() }, modifier = Modifier.fillMaxWidth()) { Text("Cihazı Şimdi Kilitle") }
            if (manualLock) {
                Button(onClick = { vm.unlock() }, colors = ButtonDefaults.buttonColors(), modifier = Modifier.fillMaxWidth()) { Text("Kilidi Aç") }
            }
            Spacer(Modifier.height(16.dp))
            val context = androidx.compose.ui.platform.LocalContext.current
            val topApps = remember(today?.usedMinutes) {
                com.mobilkontrol.tracking.UsageStatsTracker(context).topAppsToday()
            }
            if (topApps.isNotEmpty()) {
                Text("En Çok Kullanılan Uygulamalar", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                topApps.forEach { (pkg, minutes) ->
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(pkg, modifier = Modifier.weight(1f))
                        Text("$minutes dk")
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(title: String, value: String) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Column(Modifier.padding(16.dp)) {
            Text(title, fontSize = 14.sp)
            Text(value, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(vm: MainViewModel, onBack: () -> Unit) {
    val w15 by vm.warn15On.collectAsStateWithLifecycle()
    val w5 by vm.warn5On.collectAsStateWithLifecycle()
    val w1 by vm.warn1On.collectAsStateWithLifecycle()
    val remote by vm.remoteOn.collectAsStateWithLifecycle()
    val context = androidx.compose.ui.platform.LocalContext.current

    Column(Modifier.padding(24.dp)) {
        Text("Ayarlar", fontSize = 26.sp, modifier = Modifier.padding(bottom = 16.dp))
        // İzin durumu taraması
        val usageGranted = remember {
            val appOps = context.getSystemService(android.content.Context.APP_OPS_SERVICE) as android.app.AppOpsManager
            appOps.unsafeCheckOpNoThrow(android.app.AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), context.packageName) == android.app.AppOpsManager.MODE_ALLOWED
        }
        val accessibilityGranted = remember {
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
                ?.contains(context.packageName) == true
        }
        if (!usageGranted) {
            Text("⚠ Kullanım Erişimi izni verilmedi — sayaç ekran-açık süresine düşer.", color = androidx.compose.ui.graphics.Color.Red)
            Button(onClick = { context.startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)) }) { Text("Kullanım İzni Ver") }
        }
        if (!accessibilityGranted) {
            Text("⚠ Erişilebilirlik izni verilmedi — uygulama engelleme çalışmaz.", color = androidx.compose.ui.graphics.Color.Red)
            Button(onClick = { context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)) }) { Text("Erişilebilirlik İzni Ver") }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("15 dk uyarısı", modifier = Modifier.weight(1f))
            Switch(checked = w15, onCheckedChange = vm::setWarn15)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("5 dk uyarısı", modifier = Modifier.weight(1f))
            Switch(checked = w5, onCheckedChange = vm::setWarn5)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("1 dk uyarısı", modifier = Modifier.weight(1f))
            Switch(checked = w1, onCheckedChange = vm::setWarn1)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Uzaktan kontrol (HTTP)", modifier = Modifier.weight(1f))
            Switch(checked = remote, onCheckedChange = vm::setRemote)
        }
        Text("Uzaktan kontrol açıkken cihaz aynı Wi-Fi ağında 8765 portunu dinler.", fontSize = 12.sp)
        Spacer(Modifier.height(16.dp))
        Button(onClick = {
            context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        }, modifier = Modifier.fillMaxWidth()) { Text("Uygulama Engelleme İzni") }

        Spacer(Modifier.height(16.dp))
        Text("PIN Değiştir", fontSize = 18.sp)
        var currentPin by remember { mutableStateOf("") }
        var newPin by remember { mutableStateOf("") }
        var pinMsg by remember { mutableStateOf("") }
        val scope = remember { kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main) }
        TextField(value = currentPin, onValueChange = { currentPin = it }, label = { Text("Mevcut PIN") })
        TextField(value = newPin, onValueChange = { newPin = it }, label = { Text("Yeni PIN (4-6 hane)") })
        if (pinMsg.isNotEmpty()) Text(pinMsg)
        Button(onClick = {
            scope.launch {
                pinMsg = if (vm.changePin(currentPin, newPin)) "PIN güncellendi" else "PIN hatalı veya geçersiz"
            }
        }) { Text("PIN'i Güncelle") }
        TextButton(onClick = onBack) { Text("Geri") }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTimeScreen(vm: MainViewModel, onBack: () -> Unit) {
    val options = listOf(5, 10, 15, 30, 60)
    Column(Modifier.padding(24.dp)) {
        Text("Ek Süre Ekle", fontSize = 26.sp, modifier = Modifier.padding(bottom = 16.dp))
        options.forEach { o ->
            Button(onClick = { vm.addBonus(o); onBack() }, modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)) { Text("+$o dakika") }
        }
        TextButton(onClick = onBack) { Text("Vazgeç") }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LimitScreen(vm: MainViewModel, onBack: () -> Unit) {
    var selected by remember { mutableIntStateOf(vm.dailyLimit.value) }
    Column(Modifier.padding(24.dp)) {
        Text("Günlük Süre Ayarı", fontSize = 26.sp, modifier = Modifier.padding(bottom = 16.dp))
        LimitChooser(selected = selected, onSelect = { selected = it })
        Spacer(Modifier.height(16.dp))
        Button(onClick = { vm.setLimit(selected); onBack() }) { Text("Kaydet") }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(vm: MainViewModel, onBack: () -> Unit) {
    val history by vm.history.collectAsStateWithLifecycle()
    val total = history.sumOf { it.totalUsageMinutes }
    val maxDay = history.maxByOrNull { it.totalUsageMinutes }
    val average = if (history.isEmpty()) 0 else total / history.size

    Column(Modifier.padding(24.dp)) {
        Text("Haftalık İstatistik", fontSize = 26.sp)
        history.forEach { h ->
            Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(h.date)
                Text("${h.totalUsageMinutes} dk / ${h.dailyLimitMinutes} dk")
            }
        }
        Spacer(Modifier.height(16.dp))
        Text("Bu haftaki toplam kullanım: $total dakika")
        Text("En fazla kullanılan gün: ${maxDay?.date ?: "-"}")
        Text("Günlük ortalama: $average dakika")
        TextButton(onClick = onBack) { Text("Geri") }
    }
}
