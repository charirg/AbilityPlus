package com.charirodriguez.abilityplus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.charirodriguez.abilityplus.ui.theme.AbilityPlusTheme

import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.charirodriguez.abilityplus.data.local.db.AbilityPlusDatabase
import com.charirodriguez.abilityplus.data.repository.PersonaRepository
import com.charirodriguez.abilityplus.ui.persona.PersonaScreen
import com.charirodriguez.abilityplus.ui.persona.PersonaViewModel
import com.charirodriguez.abilityplus.ui.persona.PersonaViewModelFactory
import androidx.compose.runtime.remember

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            setContent {
                val context = LocalContext.current

                val db = remember { AbilityPlusDatabase.getInstance(context) }
                val repo = remember { PersonaRepository(db.personaDao()) }
                val factory = remember { PersonaViewModelFactory(repo) }

                val personaViewModel: PersonaViewModel = viewModel(factory = factory)

                AbilityPlusTheme {
                    PersonaScreen(viewModel = personaViewModel)
                }
            }



            AbilityPlusTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AbilityPlusTheme {
        Greeting("Android")
    }
}