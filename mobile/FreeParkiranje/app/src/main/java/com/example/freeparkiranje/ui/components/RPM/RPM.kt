package com.example.freeparkiranje.ui.components.RPM

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.freeparkiranje.R
import com.example.freeparkiranje.avti.Avti
import com.example.freeparkiranje.cudez.Cudez
import com.example.freeparkiranje.parking.Parking
import com.example.freeparkiranje.parking.Polozaj
import com.example.freeparkiranje.rezervacije.Rezervacije
import com.example.freeparkiranje.ui.components.Avti.IzbrisAvta
import com.example.freeparkiranje.ui.components.GlavnaFunkcionalnost.kartica.Kartica
import com.example.freeparkiranje.ui.components.GlavnaFunkcionalnost.vsebina.Vsebina
import com.example.freeparkiranje.ui.components.ZacetnoNalaganje
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialWithDialogExample(
    onConfirm: (TimePickerState) -> Unit,
    onDismiss: () -> Unit,
) {
    val currentTime = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )

    TimePickerDialog(
        onDismiss = { onDismiss() },
        onConfirm = { onConfirm(timePickerState) }
    ) {
        TimePicker(
            state = timePickerState,
        )
    }
}
@Composable
fun RezervacijaUspela(onClick: () -> Unit) {
    AlertDialog(
        onDismissRequest = onClick,
        title = {
            Text(text = "Uspeh")
        },
        text = {
            Text(text = "Rezervacija je bila uspešna!")
        },
        confirmButton = {
            Button(onClick = onClick) {
                Text("V redu")
            }
        }
    )
}
@RequiresApi(Build.VERSION_CODES.O)
fun pridobiFormatiranDatum(millis: Long?): String {
    if (millis == null) return ""

    // Pretvorimo millisekunde v LocalDate v lokalnem časovnem pasu
    val localDate = Instant.ofEpochMilli(millis)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

    // Definiramo želeni format YYYY-MM-DD
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    return localDate.format(formatter)
}

@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Dismiss")
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm() }) {
                Text("OK")
            }
        },
        text = { content() }
    )
}

@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
fun ParkirPlac(
    zaseden: Boolean,
    izbran: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    var barva = MaterialTheme.colorScheme.primary
    if (izbran) barva = MaterialTheme.colorScheme.secondary
    if (zaseden) barva = MaterialTheme.colorScheme.error

    Card(
        modifier = modifier
            .padding(1.5.dp)
            .clickable(enabled = !zaseden, onClick = onClick),
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(containerColor = barva)
    ) {}
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VsebinaRPM() {
    Vsebina {
        var mojiAvti by remember { mutableStateOf(emptyList<String>()) }
        var nalagam by remember { mutableStateOf(true) }
        val coroutineScope = rememberCoroutineScope()
        var tss by remember { mutableStateOf(TimePickerState(0, 0, true)) }
        LaunchedEffect(Unit) {
            mojiAvti = withContext(Dispatchers.IO) {
                Avti.DobiMojeAvte().toList()
            }
            nalagam = false
        }

        if (!nalagam) {
            var zacetekDatumValue by remember { mutableStateOf<Long?>(null) }
            var aliIzbiranZacetekDatum by remember { mutableStateOf(false) }

            var zacetekCasValue by remember { mutableStateOf<TimePickerState?>(null) }
            var aliIzbiramZacetekCas by remember { mutableStateOf(false) }

            var konecDatumValue by remember { mutableStateOf<Long?>(null) }
            var aliIzbiramKonecDatum by remember { mutableStateOf(false) }

            var konecCasValue by remember { mutableStateOf<TimePickerState?>(null) }
            var aliIzbiranKoneCas by remember { mutableStateOf(false) }

            var izbranAvto by remember { mutableStateOf("") }

            val visinaAvtovNajprej = 200.dp
            val visinaAvtovPotlje = 200.dp
            var visinaAvtov by remember { mutableStateOf(visinaAvtovNajprej) }

            val coroutineScope =
                rememberCoroutineScope() // Potrebno za izvajanje IO operacij ob kliku

            var zac by remember { mutableStateOf(Cudez.zac) }
            var kon by remember { mutableStateOf(Cudez.kon) }

            var rezervacijaUspela by remember { mutableStateOf(false) }


            var parkirplaci by remember { mutableStateOf(emptyList<Polozaj>()) }
            LaunchedEffect(Unit) {
                mojiAvti = withContext(Dispatchers.IO) {
                    Avti.DobiMojeAvte().toList()
                }
                if(Cudez.zac.isEmpty()  || Cudez.kon.isEmpty()) {
                    parkirplaci = Parking.dobiParkinge().toList()
                }else
                {
                    withContext (Dispatchers.IO) {
                        try {
                            parkirplaci = Parking.dobiZasedenaParkinge(zac, kon).toList()
                        }
                        catch (e : Exception){
                            parkirplaci = Parking.dobiParkinge().toList()
                        }
                    }
                }
                nalagam = false
            }
            if(rezervacijaUspela){
                RezervacijaUspela { rezervacijaUspela=false }
            }
            if (aliIzbiranZacetekDatum) {
                DatePickerModal(
                    onDateSelected = { t ->
                        zacetekDatumValue = t
                        aliIzbiramZacetekCas = true
                        aliIzbiranZacetekDatum = false
                    },
                    onDismiss = { aliIzbiranZacetekDatum = false }
                )
            }
            if (aliIzbiramZacetekCas) {
                DialWithDialogExample(
                    onConfirm = { t ->
                        zacetekCasValue = t
                        aliIzbiramZacetekCas = false

                        val zedDatumStr = pridobiFormatiranDatum(zacetekDatumValue)

                        val zacUra =
                            zacetekCasValue?.hour?.let { String.format("%02d", it) } ?: "00"
                        val zacMin =
                            zacetekCasValue?.minute?.let { String.format("%02d", it) }
                                ?: "00"


                        zac = "$zedDatumStr $zacUra:$zacMin"
                    },
                    onDismiss = {
                        aliIzbiramZacetekCas = false
                    }
                )
            }

            if (aliIzbiramKonecDatum) {
                DatePickerModal(
                    onDateSelected = { t ->
                        konecDatumValue = t
                        aliIzbiramKonecDatum = false
                        aliIzbiranKoneCas = true


                    },
                    onDismiss = {
                        aliIzbiramKonecDatum = false


                    }
                )
            }
            if (aliIzbiranKoneCas) {
                DialWithDialogExample(
                    onConfirm = { t ->
                        konecCasValue = t
                        aliIzbiranKoneCas = false

                        val konDatumStr = pridobiFormatiranDatum(konecDatumValue)
                        val konUra =
                            konecCasValue?.hour?.let { String.format("%02d", it) } ?: "00"
                        val konMin =
                            konecCasValue?.minute?.let { String.format("%02d", it) } ?: "00"
                        kon = "$konDatumStr $konUra:$konMin"
                    },
                    onDismiss = {
                        aliIzbiranKoneCas = false


                    }
                )
            }

            Kartica("Izbira časa", 150.dp) {
                Row(
                    Modifier
                        .height(5.dp)
                        .fillMaxWidth()
                ) { }
                Row {
                    Column(
                        Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Card(
                            Modifier.clickable(onClick = { aliIzbiranZacetekDatum = true }),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
                        ) {
                            Row {
                                Text(if (zac.isEmpty()) "Čas začetka" else zac)

                                Icon(
                                    painter = painterResource(R.drawable.koledar),
                                    contentDescription = stringResource(R.string.koledar)
                                )
                            }
                        }
                    }
                    Column(
                        Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Card(
                            Modifier.clickable(onClick = { aliIzbiramKonecDatum = true }),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
                        ) {
                            Row {
                                Text(if (kon.isEmpty()) "Čas konca" else kon)
                                Icon(
                                    painter = painterResource(R.drawable.koledar),
                                    contentDescription = stringResource(R.string.koledar)
                                )
                            }
                        }
                    }
                }
                Row(
                    Modifier
                        .height(10.dp)
                        .fillMaxWidth()
                ) { 1 }
                Row {
                    Column(
                        Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(onClick = {
                            coroutineScope.launch(Dispatchers.IO) {

                                val dobljeniPlaci = Parking.dobiZasedenaParkinge(zac, kon).toList()
                                val noviSeznam = dobljeniPlaci.map { original ->
                                    Polozaj(original.x, original.y, original.smer).apply {
                                        i = original.i
                                        zaseden = original.zaseden
                                    }
                                }
                                withContext(Dispatchers.Main) {
                                    parkirplaci = emptyList()
                                    parkirplaci = noviSeznam
                                }
                            }
                        }) { Text("Potrdi izbiro") }
                    }
                }
            }

            Row(Modifier.height(15.dp)) { }

            Kartica("Izbira avta", visinaAvtov) {
                Column(Modifier.verticalScroll(rememberScrollState())) {
                    mojiAvti.forEach { avto ->
                        IzbrisAvta(
                            registerska = avto,
                            ikona = R.drawable.puscica,
                            posebn = izbranAvto,
                            naKlik = {
                                izbranAvto = avto
                                visinaAvtov = visinaAvtovPotlje
                            }
                        )
                    }
                }
            }

            Row(Modifier.height(15.dp)) { }

            var izbranPlacId by remember { mutableStateOf<Int?>(null) }

            Kartica("Parkirišče", 300.dp) {
                if (parkirplaci.isNotEmpty()) {
                    val sirinaEnot = remember(parkirplaci) {
                        parkirplaci.maxOf { it.x + if (it.smer == 0) 2 else 1 }
                    }
                    val visinaEnot = remember(parkirplaci) {
                        parkirplaci.maxOf { it.y + if (it.smer == 0) 1 else 2 }
                    }

                    BoxWithConstraints(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                    ) {
                        // Širino si shranimo v lokalno spremenljivko znotraj scope-a BoxWithConstraints
                        val localMaxWidth = maxWidth
                        val enota = localMaxWidth / sirinaEnot.toFloat()
                        val skupnaVisina = enota * visinaEnot.toFloat()

                        Box(
                            modifier = Modifier
                                .width(localMaxWidth)
                                .height(skupnaVisina)
                        ) {
                            parkirplaci.forEach { plac ->
                                val w = if (plac.smer == 0) 2 else 1
                                val h = if (plac.smer == 0) 1 else 2

                                ParkirPlac(
                                    zaseden = plac.zaseden,
                                    izbran = (izbranPlacId == plac.i),
                                    modifier = Modifier
                                        .offset(
                                            x = enota * plac.x,
                                            y = enota * plac.y
                                        )
                                        .width(enota * w)
                                        .height(enota * h),
                                    onClick = {
                                        izbranPlacId = plac.i
                                    }
                                )
                            }

                            Card(
                                modifier = Modifier
                                    .offset(
                                        x = localMaxWidth * 0.02f,
                                        y = skupnaVisina * 0.87f
                                    )
                                    .width(localMaxWidth * 0.28f)
                                    .height(skupnaVisina * 0.11f),
                                shape = RoundedCornerShape(4.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.secondary
                                )
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "▲ VHOD",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Row(Modifier.height(15.dp)) { }

            Button({
                coroutineScope.launch(Dispatchers.IO) {
                    try {
                        izbranPlacId?.let {
                            Rezervacije.Rezerviraj(zac, kon, it, izbranAvto)
                            rezervacijaUspela = true
                        }
                    } catch (e: Exception) {
                        rezervacijaUspela = false
                    }
                }
            }) { Text("Rezerviraj") }
        } else {
            ZacetnoNalaganje()
        }
    }
}