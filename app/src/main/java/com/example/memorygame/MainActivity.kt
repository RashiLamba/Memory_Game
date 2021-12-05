package com.example.memorygame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.memorygame.databinding.ActivityMainBinding
import com.example.memorygame.modals.BoardSize
import com.example.memorygame.modals.MemoryCard
import com.example.memorygame.modals.MemoryGame
import com.example.memorygame.utils.DEFAULT_ICONS

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val boardSize: BoardSize = BoardSize.EASY

    companion object{
        const val TAG = "MainActivity"
    }


    override fun onCreate( savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val memoryGame = MemoryGame(boardSize)

        binding.recyclerBoard.layoutManager = GridLayoutManager(this,boardSize.getWidth())
        binding.recyclerBoard.adapter = MemoryBoardAdapter(this, boardSize, memoryGame.cards, object: MemoryBoardAdapter.CardClickListener{
            override fun onClickListener(position: Int) {
                Toast.makeText(this@MainActivity, "$TAG : On item click $position ",Toast.LENGTH_SHORT).show()
                Log.i(TAG, "Tag : On item click $position ")
            }

        })
        binding.recyclerBoard.hasFixedSize()

    }

}