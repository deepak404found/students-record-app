@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.myapplication

import android.os.Bundle
import android.widget.TableLayout
import androidx.activity.ComponentActivity
import androidx.activity.R
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme

data class User(var name: String, var age: Int, var section: String = "1A")

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                MainApp()
            }
        }
    }
}

// Main App
@Preview(showBackground = true)
@Composable
fun MainApp() {
    MyApplicationTheme {
        var usersData: MutableList<User> = remember { mutableStateListOf() }

        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

//                    title
            Text(
                text = "Students Section",
                style = TextStyle(
                    fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                ),
            )

            UserForm(onAdd = { name, age, section ->
                usersData.add(User(name, age))
            })

//            students list
            StudentsList(users = usersData, onDel = { index ->
                usersData.removeAt(index)
            })
        }
    }
}

// create a user form
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserForm(
    onAdd: (
        name: String, age: Int, section: String
    ) -> Unit
) {
    var input: MutableMap<String, Any> =
        remember { mutableStateMapOf("name" to "", "age" to 0, "section" to "") }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
//        title
        Text(
            text = "Add New Student",
            style = TextStyle(
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            ),
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = input["name"].toString(),
            onValueChange = { value -> input["name"] = value },
            label = {
                Text(text = "Name")
            })
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.5f),
                value = input["age"].toString(),
                onValueChange = {
                    if (it.toIntOrNull() != null) {
                        input["age"] = it.toInt()
                    }
                },
                label = {
                    Text(text = "Age")
                })
            OutlinedTextField(
                value = input["section"].toString(),
                onValueChange = { value -> input["section"] = value },
                label = {
                    Text(text = "Section")
                })
        }

//        add button
        Button(
            onClick = {
                onAdd(
                    input["name"].toString(),
                    input["age"].toString().toInt(),
                    input["section"].toString()
                )
            },
            modifier = Modifier.padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onTertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiary
            )

        ) {
            Text(text = "Add")
        }
    }
}

// students list
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentsList(users: MutableList<User>, onDel: (index: Int) -> Unit = {}) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Students List",
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                )

                TextButton(onClick = {
                    users.clear()
                }) {
                    Text(text = "Delete All")
                }
            }
        }
        items(users) { user ->
            ListItem(
                modifier = Modifier.fillMaxWidth(),
                headlineText = {
                    Text(text = "Name: ${user.name}")
                },
                supportingText = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(text = "Age: ${user.age}")
                        Text(text = "Class: ${user.section}")
                    }
                },
                trailingContent = {
                    IconButton(
                        onClick = { onDel(users.indexOf(user)) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )

            Divider()
        }
    }
}