package com.example.memorygame.modals

data class MemoryCard(
    val identifier: Int,
    val isFaceUp: Boolean = false,
    val isMatched: Boolean = false
)