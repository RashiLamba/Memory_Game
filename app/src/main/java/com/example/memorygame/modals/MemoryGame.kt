package com.example.memorygame.modals

import com.example.memorygame.utils.DEFAULT_ICONS

class MemoryGame(private val boardSize: BoardSize) {

    val cards: List<MemoryCard>
    var numPairsFound = 0
    private var numCardFlips  = 0
    private var indexOfSingleSelectedCard: Int? = null


    init {
    val chosenImages = DEFAULT_ICONS.shuffled().take(boardSize.getNumPairs())
    val randomizedImages = (chosenImages + chosenImages).shuffled()
    cards = randomizedImages.map { MemoryCard(it) }
    }

    fun flipCard(position: Int): Boolean {
        numCardFlips++
        val card = cards[position]
        var matchFound = false
        //three cases:
        /**
         * 1. 0 cards previously flipped : restore cards + flip over the selected cards
         * 2. 1 cards previously flipped : flip over the selected cards + check if the cards matches
         * 3. 2 cards previously flipped : restore cards + flip over the selected cards
         * */
        if (indexOfSingleSelectedCard == null){
            //0 or 2 cards are previously flipped card
            restoreCards()
            indexOfSingleSelectedCard = position
        }else{
            //exactly 1 card is previously flipped over
            matchFound = checkForMatch(indexOfSingleSelectedCard!!, position)
            indexOfSingleSelectedCard = null
        }
          card.isFaceUp = !card.isFaceUp
        return  matchFound
    }

    private fun checkForMatch(position1: Int, position2: Int):Boolean {
        return when {
            cards[position1].identifier != cards[position2].identifier -> {
                false
            }
            else -> {
                cards[position1].isMatched = true
                cards[position2].isMatched = true
                numPairsFound++
                true
            }
        }
    }

    private fun restoreCards() {
        for (card in cards){
            if (!card.isMatched){
                card.isFaceUp = false
            }
        }
    }

    fun haveWonGame(): Boolean {
        return numPairsFound == boardSize.getNumPairs()
    }

    fun isCardFaceUp(position: Int): Boolean {
        return cards[position].isFaceUp
    }

    fun getNumMoves(): Int {
        return numCardFlips / 2
    }
}