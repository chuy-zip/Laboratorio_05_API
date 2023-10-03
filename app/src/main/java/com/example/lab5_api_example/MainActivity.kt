package com.example.lab5_api_example

import ApiInterface
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://official-joke-api.appspot.com/"
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            mainScreenLayout()
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun mainScreenLayout(){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(android.graphics.Color.parseColor("#01377d"))),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ){
        var selectedItem by remember { mutableStateOf("General") }
        var isMenuExpanded by remember { mutableStateOf(false) }
        var jokeText by remember { mutableStateOf(":)") }
        var jokeID by rememberSaveable { mutableStateOf("1")}
        var isVisible by remember { mutableStateOf(false)}


        Text(
            text = "Bienvenido \nElige una categoría para generar un chiste al presionar el botón",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(20.dp)
        )

        ScrollableJoke(jokeText = jokeText)

        Column {
            Button(
                onClick = { isMenuExpanded = true},
                content = { Text(selectedItem)},
                modifier = Modifier
                    .width(220.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF69A0CC),

                    )
            )
            DropdownMenu(
                expanded = isMenuExpanded,
                onDismissRequest = { isMenuExpanded = false }
            ) {

                DropdownMenuItem(
                    text = { Text("General")},
                    onClick = {
                        selectedItem = "General"
                        isMenuExpanded = false
                        isVisible = false
                    }
                )

                DropdownMenuItem(
                    text = { Text("Programacion")},
                    onClick = {
                        selectedItem = "Programacion"
                        isMenuExpanded = false
                        isVisible = false
                    }
                )
                DropdownMenuItem(
                    text = {Text("Random")},
                    onClick = {
                        selectedItem = "Random"
                        isMenuExpanded = false
                        isVisible = false
                    })

                DropdownMenuItem(
                    text = {Text("ID")},
                    onClick = {
                        selectedItem = "ID"
                        isMenuExpanded = false
                        isVisible = true
                    })

            }

        }

        if(isVisible){
            TextField(
                value = jokeID,
                onValueChange = { jokeID  = it},

                label = { Text ("ID:")},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                shape = CircleShape,
                modifier = Modifier
                    .width(220.dp)
                )

        }

        Button(
            modifier = Modifier
                .width(220.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF69A0CC)
            ),
            onClick = {
                when (selectedItem) {
                    "General" -> {
                        jokeText = "Cargando"
                        getAPIRandomJoke(0, 1) { result ->
                            jokeText = result
                        }

                        isVisible = false

                    }
                    "Programacion" -> {
                        jokeText = "Cargando"

                        getAPIRandomJoke(0, 2) { result ->
                            jokeText = result
                        }

                        isVisible = false
                    }
                    "Random" -> {
                        jokeText = "Cargando"
                        getAPIRandomJoke(0, 3) { result ->
                            jokeText = result
                        }
                        isVisible = false
                    }
                    "ID" -> {
                        getAPIRandomJoke(jokeID.toInt(),4) { result ->
                            jokeText = result
                        }
                        jokeText = "Cargando"
                        isVisible = true
                    }
                }

            }) {
            Text(text = "Continuar")

        }

    }

}

@Composable
fun ScrollableJoke(jokeText: String) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .height(330.dp)
            .width(280.dp)
            .verticalScroll(state = scrollState)
            .background(
                Color(0xFF69A0CC),
                shape = RoundedCornerShape(20.dp)
            ) // Set the background color of the column
            .clip(shape = RoundedCornerShape(20.dp)),
                horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = jokeText,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(20.dp)
        )
    }
}

private fun getAPIRandomJoke(id: Int, jokeType: Int, onSuccess: (String) -> Unit){
    val retrofitBuilder = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()
        .create(ApiInterface::class.java)

    val retrofitData: Call<Joke> = when (jokeType) {
        1 -> retrofitBuilder.getRandomGeneralJoke()
        2 -> retrofitBuilder.getRandomProgrammingJoke()
        3 -> retrofitBuilder.getRandomJoke()
        4 -> retrofitBuilder.getJokeByID(id)
        else -> retrofitBuilder.getRandomJoke() // Default to getRandomJoke
    }

    retrofitData.enqueue(object : Callback<Joke> {
        override fun onResponse(call: Call<Joke>, response: Response<Joke>) {
            val myData = response.body()

            if (myData != null) {
                val result = myData.setup + "\n" + "\n" + myData.punchline
                onSuccess(result)
            } else {
                onSuccess("Response was null")
            }
        }

        override fun onFailure(call: Call<Joke>, t: Throwable) {
            Log.d("MainActivity", "onFailure" + t.message)
        }
    })
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    mainScreenLayout()
}