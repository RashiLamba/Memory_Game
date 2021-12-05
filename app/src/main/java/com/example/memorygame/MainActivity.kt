package com.example.memorygame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.memorygame.databinding.ActivityMainBinding
import com.example.memorygame.modals.BoardSize
import com.example.memorygame.utils.DEFAULT_ICONS

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val boardSize: BoardSize = BoardSize.EASY
    private lateinit var chosenImages: List<Int>
    private lateinit var randomizedImages: List<Int>


    override fun onCreate( savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chosenImages = DEFAULT_ICONS.shuffled().take(boardSize.getNumPairs())
        randomizedImages = (chosenImages + chosenImages).shuffled()

        binding.recyclerBoard.layoutManager = GridLayoutManager(this,boardSize.getWidth())
        binding.recyclerBoard.adapter = MemoryBoardAdapter(this,boardSize, randomizedImages )
        binding.recyclerBoard.hasFixedSize()

    }

}