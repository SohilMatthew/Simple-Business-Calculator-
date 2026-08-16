package com.example.businesscalculator.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.businesscalculator.ui.theme.LightBackground
import com.example.businesscalculator.ui.theme.PrimaryRed
import com.example.businesscalculator.ui.theme.ProfitGreen
import com.example.businesscalculator.ui.theme.ProfitGreenLight
import com.example.businesscalculator.viewmodel.BusinessViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyEntryScreen(
    viewModel: BusinessViewModel
) {
    val activeItems by viewModel.activeMenuItems.collectAsState()
    val context = LocalContext.current

    // Local input state maps item ID to quantity string
    var itemQuantities by remember { mutableStateOf(mapOf<Long, String>()) }
    var investmentText by remember { mutableStateOf("") }
    var revenueText by remember { mutableStateOf("") }
    var calculatedProfitResult by remember { mutableStateOf<Double?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Screen Header Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Daily Entry & Calculator",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Record today's itemized sales, total investment, and revenue.",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Calculated Profit Output Display (Shown after calculation in Green)
        AnimatedVisibility(
            visible = calculatedProfitResult != null,
            enter = fadeIn() + slideInVertically()
        ) {
            calculatedProfitResult?.let { profit ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = ProfitGreenLight),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Saved",
                                tint = ProfitGreen
                            )
                            Column {
                                Text(
                                    text = "Entry Saved Successfully!",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = ProfitGreen
                                )
                                Text(
                                    text = "Calculated Daily Profit",
                                    fontSize = 12.sp,
                                    color = ProfitGreen.copy(alpha = 0.8f)
                                )
                            }
                        }
                        Text(
                            text = String.format("₹%.2f", profit),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = ProfitGreen // Profit MUST be strictly Green
                        )
                    }
                }
            }
        }

        // Dynamic Menu Items Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Itemized Sales",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                if (activeItems.isEmpty()) {
                    Text(
                        text = "No active menu items. Add items in the Settings tab to track product sales.",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    activeItems.forEach { item ->
                        val qty = itemQuantities[item.id] ?: ""
                        OutlinedTextField(
                            value = qty,
                            onValueChange = { newValue ->
                                if (newValue.all { it.isDigit() }) {
                                    itemQuantities = itemQuantities.toMutableMap().apply {
                                        put(item.id, newValue)
                                    }
                                }
                            },
                            label = { Text("No of ${item.name} sold:") },
                            placeholder = { Text("0") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        // Static Inputs Card (Investment & Revenue)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Financial Summary",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                OutlinedTextField(
                    value = investmentText,
                    onValueChange = { investmentText = it },
                    label = { Text("Today's Investment (₹)") },
                    placeholder = { Text("0.00") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = revenueText,
                    onValueChange = { revenueText = it },
                    label = { Text("Today's Revenue (₹)") },
                    placeholder = { Text("0.00") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Prominent Red Action Button "Calculate & Save"
        Button(
            onClick = {
                // Map item quantities to item names for saving
                val finalItemMap = activeItems.associate { item ->
                    val qtyStr = itemQuantities[item.id] ?: "0"
                    item.name to (qtyStr.toIntOrNull() ?: 0)
                }

                viewModel.saveDailyEntry(
                    investmentStr = investmentText,
                    revenueStr = revenueText,
                    itemQuantities = finalItemMap,
                    onSuccess = { profit ->
                        calculatedProfitResult = profit
                        Toast.makeText(context, "Saved! Profit: ₹$profit", Toast.LENGTH_SHORT).show()
                    },
                    onError = { errorMsg ->
                        Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
                    }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed), // Red Button
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                text = "Calculate & Save",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
