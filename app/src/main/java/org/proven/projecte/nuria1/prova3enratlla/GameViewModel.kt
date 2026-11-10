package org.proven.projecte.nuria1.prova3enratlla

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {
    private val _board = MutableStateFlow(BoardModel.createInitialBoard())
    val board: StateFlow<BoardModel> = _board.asStateFlow()

    fun onCardClick(position: Int) {
        val currentBoard = _board.value
        val flippedUnmatched = currentBoard.cards.filter { it.isFlipped && !it.isMatched }

        if (flippedUnmatched.size >= 2) return

        val updatedBoard = currentBoard.onCardClick(position)
        _board.value = updatedBoard

        val newlyFlipped = updatedBoard.cards.filter { it.isFlipped && !it.isMatched }
        if (newlyFlipped.size == 2) {
            viewModelScope.launch {
                delay(1000L)
                _board.value = _board.value.checkMatches()
            }
        }
    }

    fun restartGame() {
        _board.value = BoardModel.createInitialBoard()
    }
}