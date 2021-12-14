package com.example.memorygame

import android.animation.ArgbEvaluator
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.LayoutDirection
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.DialogTitle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.memorygame.databinding.ActivityMainBinding
import com.example.memorygame.modals.BoardSize
import com.example.memorygame.modals.MemoryCard
import com.example.memorygame.modals.MemoryGame
import com.example.memorygame.utils.DEFAULT_ICONS
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var boardSize: BoardSize = BoardSize.EASY
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

        setUpBoard()
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
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
            val color = ArgbEvaluator().evaluate(
                memoryGame.numPairsFound.toFloat() / boardSize.getNumPairs(),
                ContextCompat.getColor(this, R.color.red),
                ContextCompat.getColor(this, R.color.green)
             ) as Int
            binding.tvNumPairs.setTextColor(color)
            binding.tvNumPairs.text = "Pairs:  ${memoryGame.numPairsFound} / ${boardSize.getNumPairs()}"
            if (memoryGame.haveWonGame()){
                Snackbar.make(binding.clRoot, "You Won! Congratulations.", Snackbar.LENGTH_LONG).show()
            }
        }
        binding.tvNumMoves.text = "Moves : ${memoryGame.getNumMoves()}"
        memoryBoardAdapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.refresh_item -> {
                if (memoryGame.getNumMoves() > 0 && !memoryGame.haveWonGame()){
                    showAlertDialog("Quit your current game?", null) {
                        setUpBoard()
                    }
                }else{
                    setUpBoard()
                }
                return true
            }
            R.id.choose_new_size -> {
                showNewSizeDialog()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showNewSizeDialog() {
        val boardViewSize = LayoutInflater.from(this).inflate(R.layout.dialog_board_size,null)
        val radioGroupSize = boardViewSize.findViewById<RadioGroup>(R.id.radio_group)
        when(this.boardSize){
            BoardSize.EASY -> radioGroupSize.check(R.id.radio_button_easy)
            BoardSize.MEDIUM -> radioGroupSize.check(R.id.radio_button_medium)
            BoardSize.HARD -> radioGroupSize.check(R.id.radio_button_hard)
        }

        showAlertDialog("Choose new size", boardViewSize) {
            boardSize = when (radioGroupSize.checkedRadioButtonId) {
                R.id.radio_button_easy -> BoardSize.EASY
                R.id.radio_button_medium -> BoardSize.MEDIUM
                else -> BoardSize.HARD
            }
            setUpBoard()
        }
    }

    private fun showAlertDialog(title: String, view: View?, positiveClickListener: View.OnClickListener) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(view)
            .setNegativeButton("CANCEL", null)
            .setPositiveButton("OK"){ _, _ ->
                positiveClickListener.onClick(null)
            }.show()
    }

    private fun setUpBoard() {
        updateMovesAndPairText()
        binding.tvNumPairs.setTextColor(ContextCompat.getColor(this, R.color.red))
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

    private fun updateMovesAndPairText() {
        when(boardSize){
            BoardSize.EASY -> {
                binding.tvNumMoves.text = "EASY : 4 x 2"
                binding.tvNumPairs.text = "Pairs : 0 / 4"
            }
            BoardSize.MEDIUM -> {
                binding.tvNumMoves.text = "MEDIUM : 6 x 3"
                binding.tvNumPairs.text = "Pairs : 0 / 9"
            }
            BoardSize.HARD -> {
                binding.tvNumMoves.text = "HARD : 7 x 4"
                binding.tvNumPairs.text = "Pairs : 0 / 12"
            }
        }
    }
}
