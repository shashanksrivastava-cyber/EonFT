//package `in`.eoninfotech.eontechnician.compose
//
//import android.graphics.Bitmap
//import androidx.compose.foundation.*
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.DateRange
//import androidx.compose.material.icons.filled.Close
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.RectangleShape
//import androidx.compose.ui.graphics.asImageBitmap
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.ComposeView
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontFamily
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import `in`.eoninfotech.eontechnician.R
//
//fun setBillIntimationUI(
//    composeView: ComposeView,
//    fromDate: String,
//    toDate: String,
//    capturedBitmap: Bitmap?,
//    onFromDateClick: () -> Unit,
//    onToDateClick: () -> Unit,
//    onPhotoClick: () -> Unit,
//    onRemovePhoto: () -> Unit,
//    onSubmit: (String, String) -> Unit
//) {
//    composeView.setContent {
//        // Fix: All arguments must be specified by name here
//        BillIntimationScreen(
//            fromDate = fromDate,
//            toDate = toDate,
//            capturedBitmap = capturedBitmap,
//            onFromDateClick = onFromDateClick,
//            onToDateClick = onToDateClick,
//            onPhotoClick = onPhotoClick,
//            onRemovePhoto = onRemovePhoto,
//            onSubmit = { amount, remarks ->
//                onSubmit(amount, remarks)
//            },
//            composeView = TODO()
//        )
//    }
//}
//@Composable
//fun BillIntimationScreen(
//    composeView: ComposeView,
//    fromDate: String,
//    toDate: String,
//    capturedBitmap: Bitmap?,
//    onFromDateClick: () -> Unit,
//    onToDateClick: () -> Unit,
//    onPhotoClick: () -> Unit,
//    onRemovePhoto: () -> Unit,
//    onSubmit: (String, String) -> Unit,
//) {
//
//    // These hold the data (replaces your EditText.getText() logic)
////    var fromDate by remember { mutableStateOf("") }
////    var toDate by remember { mutableStateOf("") }
////    var amount by remember { mutableStateOf("") }
////    var remarks by remember { mutableStateOf("") }
////
////    val scrollState = rememberScrollState()
////
////    // 1. Box replaces FrameLayout (allows us to put Submit button at the bottom)
////    Box(modifier = Modifier.fillMaxSize()) {
////
////        // 2. Column replaces the vertical LinearLayout
////        Column(
////            modifier = Modifier
////                .fillMaxSize()
////                .padding(bottom = 60.dp) // Leave space for the bottom button
////                .verticalScroll(scrollState),
////            horizontalAlignment = Alignment.CenterHorizontally
////        ) {
////
////            // Header
////            Text(
////                text = "Bill Details",
////                fontSize = 20.sp,
////                fontFamily = FontFamily.SansSerif,
////                modifier = Modifier.padding(top = 10.dp)
////            )
////
////            Text(
////                text = "Bill Period",
////                fontSize = 16.sp,
////                modifier = Modifier
////                    .fillMaxWidth()
////                    .padding(start = 15.dp, top = 10.dp)
////            )
////
////            // 3. Row replaces the RelativeLayout for From/To Date
////            Row(
////                modifier = Modifier
////                    .fillMaxWidth()
////                    .padding(15.dp),
////                horizontalArrangement = Arrangement.spacedBy(10.dp)
////            ) {
////                OutlinedTextField(
////                    value = fromDate,
////                    onValueChange = { fromDate = it },
////                    label = { Text("From Date") },
////                    modifier = Modifier.weight(1f),
////                    leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) }
////                )
////                OutlinedTextField(
////                    value = toDate,
////                    onValueChange = { toDate = it },
////                    label = { Text("To Date") },
////                    modifier = Modifier.weight(1f),
////                    leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) }
////                )
////            }
////
////            // Amount Field
////            OutlinedTextField(
////                value = amount,
////                onValueChange = { amount = it },
////                label = { Text("Amount") },
////                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
////                modifier = Modifier
////                    .fillMaxWidth()
////                    .padding(horizontal = 15.dp, vertical = 7.dp),
////                leadingIcon = { Text("₹ ", fontWeight = FontWeight.Bold) }
////            )
////
////            // Remarks Field
////            OutlinedTextField(
////                value = remarks,
////                onValueChange = { remarks = it },
////                label = { Text("Remarks") },
////                modifier = Modifier
////                    .fillMaxWidth()
////                    .padding(horizontal = 15.dp, vertical = 10.dp)
////                    .height(100.dp)
////            )
////
////            // Image Picker Section (The dashed green button)
////            Card(
////                modifier = Modifier
////                    .fillMaxWidth()
////                    .padding(15.dp)
////                    .height(60.dp)
////                    .clickable { /* Handle Image Pick */ },
////                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
////                border = BorderStroke(2.dp, Color(0xFF4CAF50)) // Green Border
////            ) {
////                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
////                    Text("Click here to add photo", fontWeight = FontWeight.Bold, color = Color.Black)
////                }
////            }
////
////            // Image Preview (The 200dp square)
////            Box(
////                modifier = Modifier
////                    .size(200.dp)
////                    .padding(10.dp),
////                contentAlignment = Alignment.TopEnd
////            ) {
////                Image(
////                    painter = painterResource(id = R.drawable.noimage),
////                    contentDescription = null,
////                    modifier = Modifier.fillMaxSize(),
////                    contentScale = ContentScale.Crop
////                )
////                // Cancel Icon (X)
////                IconButton(onClick = { /* Clear image */ }) {
////                    Icon(Icons.Default.Close, contentDescription = null, tint = Color.Black)
////                }
////            }
////        }
////
////        // 4. Submit Button pinned to bottom (The blue bar)
////        Button(
////            onClick = { /* Submit logic */ },
////            modifier = Modifier
////                .fillMaxWidth()
////                .height(60.dp)
////                .align(Alignment.BottomCenter),
////            shape = RectangleShape,
////            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1)) // EonBlue
////        ) {
////            Text("Submit", color = Color.White, fontSize = 20.sp)
////        }
////    }
//
//    var amount by remember { mutableStateOf("") }
//    var remarks by remember { mutableStateOf("") }
//
//    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
//        Column(
//            modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text("Bill Details", fontSize = 20.sp, fontWeight = FontWeight.Light)
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Date Selection Row
//            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
//                OutlinedTextField(
//                    value = fromDate, onValueChange = {}, readOnly = true,
//                    label = { Text("From Date") },
//                    modifier = Modifier.weight(1f).clickable { onFromDateClick() },
//                    leadingIcon = { Icon(Icons.Default.DateRange, null) },
//                    enabled = false, // Makes the whole box clickable via Modifier
//                    colors = OutlinedTextFieldDefaults.colors(disabledTextColor = Color.Black, disabledBorderColor = Color.Gray, disabledLabelColor = Color.Black)
//                )
//                OutlinedTextField(
//                    value = toDate, onValueChange = {}, readOnly = true,
//                    label = { Text("To Date") },
//                    modifier = Modifier.weight(1f).clickable { onToDateClick() },
//                    leadingIcon = { Icon(Icons.Default.DateRange, null) },
//                    enabled = false,
//                    colors = OutlinedTextFieldDefaults.colors(disabledTextColor = Color.Black, disabledBorderColor = Color.Gray, disabledLabelColor = Color.Black)
//                )
//            }
//
//            // Input Fields
//            OutlinedTextField(
//                value = amount, onValueChange = { amount = it },
//                label = { Text("Amount") },
//                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
//            )
//
//            OutlinedTextField(
//                value = remarks, onValueChange = { remarks = it },
//                label = { Text("Remarks") },
//                modifier = Modifier.fillMaxWidth().padding(top = 8.dp).height(100.dp)
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Image Section
//            if (capturedBitmap == null) {
//                Button(
//                    onClick = onPhotoClick,
//                    modifier = Modifier.fillMaxWidth().height(60.dp),
//                    shape = RoundedCornerShape(8.dp),
//                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE8F5E9), contentColor = Color.Black)
//                ) {
//                    Text("Click here to add photo", fontWeight = FontWeight.Bold)
//                }
//            } else {
//                Box(modifier = Modifier.size(200.dp)) {
//                    Image(
//                        bitmap = capturedBitmap.asImageBitmap(),
//                        contentDescription = null,
//                        modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(8.dp)),
//                        contentScale = ContentScale.Crop
//                    )
//                    IconButton(onClick = onRemovePhoto, modifier = Modifier.align(Alignment.TopEnd)) {
//                        Icon(Icons.Default.Close, null, tint = Color.Red)
//                    }
//                }
//            }
//        }
//
//        // Submit Button
//        Button(
//            onClick = { onSubmit(amount, remarks) },
//            modifier = Modifier.fillMaxWidth().height(60.dp).align(Alignment.BottomCenter),
//            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1))
//        ) {
//            Text("SUBMIT", color = Color.White, fontSize = 18.sp)
//        }
//    }
//}