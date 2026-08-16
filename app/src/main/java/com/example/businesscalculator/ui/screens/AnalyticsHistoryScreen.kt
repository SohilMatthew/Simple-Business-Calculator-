package com.example.businesscalculator.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.businesscalculator.data.entity.DailyRecord
import com.example.businesscalculator.ui.theme.LightBackground
import com.example.businesscalculator.ui.theme.LossRed
import com.example.businesscalculator.ui.theme.ProfitGreen
import com.example.businesscalculator.viewmodel.BusinessViewModel

@Composable
fun AnalyticsHistoryScreen(
    viewModel: BusinessViewModel
) {
    val analyticsState by viewModel.analyticsState.collectAsState()
    val dailyRecords by viewModel.dailyRecords.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // TOP SECTION: ANALYTICS CARDS
        item {
            Text(
                text = "Business Analytics",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }

        // Card 1: Estimated Monthly Profit
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Estimated Monthly Profit",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = String.format("₹%.2f", analyticsState.estimatedMonthlyProfit),
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = ProfitGreen // Strictly Green profit indicator
                    )
                    Text(
                        text = "Based on 30-day average daily profit projection",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }
        }

        // Card 2: Revenue Comparison Badges (vs Yesterday & vs 7 Days Ago)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Revenue vs Yesterday
                ComparisonCard(
                    modifier = Modifier.weight(1f),
                    title = "vs Yesterday",
                    diffAmount = analyticsState.revenueDiffVsYesterday
                )

                // Revenue vs 7 Days Ago
                ComparisonCard(
                    modifier = Modifier.weight(1f),
                    title = "vs 7 Days Ago",
                    diffAmount = analyticsState.revenueDiffVs7DaysAgo
                )
            }
        }

        // Card 3: 7-Day Revenue Trend Chart
        item {
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
                        text = "7-Day Revenue Trend",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    if (analyticsState.last7DaysTrend.isEmpty()) {
                        Text(
                            text = "No revenue trend data available yet.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        val maxRevenue = analyticsState.last7DaysTrend.maxOfOrNull { it.second }?.takeIf { it > 0 } ?: 1.0

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            analyticsState.last7DaysTrend.forEach { (dateStr, rev) ->
                                val barHeightFraction = (rev / maxRevenue).toFloat().coerceIn(0.1f, 1.0f)

                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Bottom,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = String.format("₹%.0f", rev),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ProfitGreen
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Box(
                                        modifier = Modifier
                                            .width(20.dp)
                                            .fillMaxHeight(barHeightFraction)
                                            .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                            .background(ProfitGreen)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = dateStr,
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // BOTTOM SECTION: HISTORY LIST HEADER
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Daily History Log",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        if (dailyRecords.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No saved entries found. Add your first record in Tab 1!",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(dailyRecords, key = { it.id }) { record ->
                HistoryRowItem(
                    record = record,
                    viewModel = viewModel
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ComparisonCard(
    modifier: Modifier = Modifier,
    title: String,
    diffAmount: Double?
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (diffAmount == null) {
                Text(
                    text = "N/A",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                val isPositive = diffAmount >= 0
                val icon = if (isPositive) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward
                val color = if (isPositive) ProfitGreen else LossRed

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = if (isPositive) "Increase" else "Decrease",
                        tint = color,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = String.format("%s₹%.2f", if (isPositive) "+" else "-", kotlin.math.abs(diffAmount)),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = color
                    )
                }
            }
        }
    }
}

@Composable
fun HistoryRowItem(
    record: DailyRecord,
    viewModel: BusinessViewModel
) {
    val salesMap = remember(record.itemizedSalesJson) {
        viewModel.parseItemizedSales(record.itemizedSalesJson)
    }

    val itemizedText = if (salesMap.isEmpty()) {
        "No items recorded"
    } else {
        salesMap.entries.joinToString(" | ") { "${it.key} sold: ${it.value}" }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = record.date,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(
                    onClick = { viewModel.deleteDailyRecord(record) },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Record",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                }
            }

            Divider(color = Color(0xFFF0F0F0))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "Revenue", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = String.format("₹%.2f", record.revenue), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }
                Column {
                    Text(text = "Investment", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = String.format("₹%.2f", record.investment), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }
                Column {
                    Text(text = "Profit", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        text = String.format("₹%.2f", record.profit),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = ProfitGreen // Profit MUST be strictly Green
                    )
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            Surface(
                color = Color(0xFFF8F9FA),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = itemizedText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}
