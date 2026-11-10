package org.proven.projecte.nuria1.prova3enratlla

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun GameScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val board by viewModel.board.collectAsState()

    // 1. AÑADIMOS UN BOX PARA PONER EL FONDO DETRÁS
    Box(modifier = modifier.fillMaxSize()) {

        // 2. IMAGEN DE FONDO
        Image(
            painter = painterResource(id = R.drawable.fondo),
            contentDescription = "Fondo de pantalla",
            contentScale = ContentScale.Crop, // Crop hace que llene toda la pantalla
            modifier = Modifier.fillMaxSize()
        )

        // 3. EL JUEGO ENCIMA DEL FONDO
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (board.isGameOver) {
                Text(
                    text = "Enhorabona! JOC ACABAT!",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Button(onClick = { viewModel.restartGame() }) {
                    Text(text = "Tornar a jugar")
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(board.cards, key = { it.id }) { card ->
                    CardItemView(
                        card = card,
                        onCardClick = { viewModel.onCardClick(card.position) }
                    )
                }
            }
        }
    }
}

@Composable
fun CardItemView(
    card: CardModel,
    onCardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(140.dp)
            .clickable(enabled = !card.isMatched) { onCardClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Image(
            painter = painterResource(id = card.currentImageRes),
            contentDescription = "Carta a la posició ${card.position}",
            contentScale = ContentScale.Fit, // Ajustado para que no se corten las cartas
            modifier = Modifier.fillMaxSize()
        )
    }
}