package com.example.memorygame

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.example.memorygame.databinding.ActivityMainBinding
import com.example.memorygame.modals.BoardSize
import com.example.memorygame.modals.MemoryCard
import com.example.memorygame.modals.MemoryGame
import com.example.memorygame.utils.DEFAULT_ICONS
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val boardSize: BoardSize = BoardSize.EASY
    private lateinit var memoryBoardAdapter: MemoryBoardAdapter
    private lateinit var memoryGame: MemoryGame
    private lateinit var clRoot: ConstraintLayout

    companion object{
        const val TAG = "MainActivity"
    }


    override fun onCreate( savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        memoryGame = MemoryGame(boardSize)

        binding.recyclerBoard.layoutManager = GridLayoutManager(this,boardSize.getWidth())
        memoryBoardAdapter = MemoryBoardAdapter(this, boardSize, memoryGame.cards,
            object: MemoryBoardAdapter.CardClickListener{
            override fun onClickListener(position: Int) {
                updateGameWithFlip(position)
            }
        })
        binding.recyclerBoard.adapter = memoryBoardAdapter
        binding.recyclerBoard.hasFixedSize()
    }

    private fun updateGameWithFlip(position: Int) {
        //Error Checking
        if(memoryGame.haveWonGame()){
            //Alert the user of an invalid move
                Snackbar.make(binding.clRoot, "You already Won! ", Snackbar.LENGTH_LONG).show()
            return
        }
        if(memoryGame.isCardFaceUp(position)){
            //Alert the user of an invalid move
            Snackbar.make(binding.clRoot, "Invalid Move! ", Snackbar.LENGTH_LONG).show()
            return
        }
        //Actually flip over the card
        if (memoryGame.flipCard(position)){
            Log.i(TAG, "Found a match! Num Pairs found: ${memoryGame.numPairsFound}")
            binding.tvNumPairs.text = "Pairs:  ${memoryGame.numPairsFound} / ${boardSize.getNumPairs()}"
            if (memoryGame.haveWonGame()){
                Snackbar.make(binding.clRoot, "You Won! Congratulations.", Snackbar.LENGTH_LONG).show()
            }
        }
        binding.tvNumMoves.text = "Moves : ${memoryGame.getNumMoves()}"
        memoryBoardAdapter.notifyDataSetChanged()
    }

}