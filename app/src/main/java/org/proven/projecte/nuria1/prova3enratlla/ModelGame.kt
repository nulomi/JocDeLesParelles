package org.proven.projecte.nuria1.prova3enratlla


// --- MODEL DE LA CARTA ---
data class CardModel(
    val id: Int,
    val position: Int,
    val frontImageRes: Int,
    val isFlipped: Boolean = false,
    val isMatched: Boolean = false
) {
    val currentImageRes: Int
        get() = if (isFlipped || isMatched) frontImageRes else R.drawable.cartagirada

    fun flip(): CardModel = copy(isFlipped = !isFlipped)

    fun isSameCard(other: CardModel): Boolean = frontImageRes == other.frontImageRes

    fun markAsMatched(): CardModel = copy(isMatched = true, isFlipped = true)
}

// --- MODEL DEL TAULER ---
data class BoardModel(
    val cards: List<CardModel> = emptyList(),
    val isGameOver: Boolean = false
) {
    companion object {
        fun createInitialBoard(): BoardModel {
            // Utilitzem les teves imatges del drawable
            val images = listOf(
                R.drawable.lobo,
                R.drawable.mano,
                R.drawable.perro
            )

            val initialCards = (images + images)
                .shuffled()
                .mapIndexed { index, imageRes ->
                    CardModel(
                        id = index,
                        position = index,
                        frontImageRes = imageRes
                    )
                }

            return BoardModel(cards = initialCards)
        }
    }

    fun onCardClick(position: Int): BoardModel {
        val updatedCards = cards.map { card ->
            if (card.position == position && !card.isMatched) {
                card.flip()
            } else {
                card
            }
        }
        return this.copy(cards = updatedCards)
    }

    fun checkMatches(): BoardModel {
        val flippedCards = cards.filter { it.isFlipped && !it.isMatched }

        if (flippedCards.size == 2) {
            val (card1, card2) = flippedCards
            val isMatch = card1.isSameCard(card2)

            val updatedCards = cards.map { card ->
                when {
                    isMatch && (card.id == card1.id || card.id == card2.id) -> card.markAsMatched()
                    !isMatch && (card.id == card1.id || card.id == card2.id) -> card.copy(isFlipped = false)
                    else -> card
                }
            }

            val allMatched = updatedCards.all { it.isMatched }
            return this.copy(cards = updatedCards, isGameOver = allMatched)
        }

        return this
    }
}