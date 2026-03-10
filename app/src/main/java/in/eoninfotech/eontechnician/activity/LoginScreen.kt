//package `in`.eoninfotech.eontechnician.activity
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.offset
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Email
//import androidx.compose.material.icons.filled.Lock
//import androidx.compose.material.icons.filled.Person
//import androidx.compose.material.icons.filled.Phone
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.Icon
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//
//@Composable
//fun LoginScreen(
//    onLoginClick: (String, String) -> Unit,
//    passwordPref: String,
//    usernamePref: String,
//    version: String,
//    appName: String,
//    secondName: String,
//    onCallClick: () -> Unit,
//    onEmailClick: () -> Unit
//) {
//
//    var username by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.White)
//    ) {
//
//        Column(
//            modifier = Modifier
//                .verticalScroll(rememberScrollState())
//                .fillMaxSize()
//        ) {
//
//            // Blue header
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(350.dp)
//                    .background(Color(0xFF0B5ED7)),
//                contentAlignment = Alignment.Center
//            ) {
//
//                Column(horizontalAlignment = Alignment.CenterHorizontally) {
//
////                    Image(
////                        painter = painterResource(id = R.drawable.eon_logo),
////                        contentDescription = null,
////                        modifier = Modifier.size(120.dp)
////                    )
//
//                    Spacer(modifier = Modifier.height(10.dp))
//
//                    Text(
//                        text = "EON FTAPP",
//                        color = Color.White,
//                        fontWeight = FontWeight.Bold,
//                        fontSize = 20.sp
//                    )
//
//                    Text(
//                        text = "Field Activity Logger",
//                        color = Color.White,
//                        fontWeight = FontWeight.Bold,
//                        fontSize = 20.sp
//                    )
//                }
//            }
//
//            Card(
//                modifier = Modifier
//                    .padding(20.dp)
//                    .offset(y = (-120).dp)
//                    .fillMaxWidth(),
//                elevation = CardDefaults.cardElevation(8.dp)
//            ) {
//
//                Column(
//                    modifier = Modifier.padding(20.dp)
//                ) {
//
//                    OutlinedTextField(
//                        value = username,
//                        onValueChange = { username = it },
//                        label = { Text("Username") },
//                        leadingIcon = {
//                            Icon(Icons.Default.Person, null)
//                        },
//                        modifier = Modifier.fillMaxWidth()
//                    )
//
//                    Spacer(modifier = Modifier.height(16.dp))
//
//                    OutlinedTextField(
//                        value = password,
//                        onValueChange = { password = it },
//                        label = { Text("Password") },
//                        leadingIcon = {
//                            Icon(Icons.Default.Lock, null)
//                        },
//                        visualTransformation = PasswordVisualTransformation(),
//                        modifier = Modifier.fillMaxWidth()
//                    )
//                }
//            }
//
//            Button(
//                onClick = {
//                    onLoginClick(username, password)
//                },
//                modifier = Modifier
//                    .align(Alignment.CenterHorizontally)
//                    .width(250.dp)
//                    .height(50.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color(0xFF0B5ED7)
//                )
//            ) {
//                Text("Login")
//            }
//
//            Spacer(modifier = Modifier.height(20.dp))
//
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//
//                Text(
//                    text = "Version 7.2",
//                    color = Color(0xFF0B5ED7)
//                )
//
//                Text(
//                    text = "EON Infotech Limited",
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 20.sp,
//                    color = Color(0xFF0B5ED7)
//                )
//
//                Row(
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Icon(Icons.Default.Phone, null)
//                    Text(" 0123456789")
//                }
//
//                Row(
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Icon(Icons.Default.Email, null)
//                    Text(" support@eon.com")
//                }
//            }
//        }
//    }
//}