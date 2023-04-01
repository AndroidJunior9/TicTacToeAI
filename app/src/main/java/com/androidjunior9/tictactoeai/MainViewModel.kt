package com.androidjunior9.tictactoeai

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel:ViewModel() {


    var board = mutableStateListOf("", "", "", "", "", "", "", "", "")






    val winner = mutableStateOf("")

    val aiScore = mutableStateOf(0)

    val humanScore = mutableStateOf(0)

    private val isHumansTurn = mutableStateOf(true)

    val aiSymbol = mutableStateOf( Symbol.O)
    val humanSymbol = mutableStateOf(Symbol.X)


    private fun bot(){
        viewModelScope.launch {
            var move  = -1
            var besteval = -1000
            board.toList().forEachIndexed { i, s ->
                if(s==""){
                    val copyBoard = board.toMutableList()
                    copyBoard[i] = aiSymbol.value
                    val eval = minimax(copyBoard,false)
                    if(eval>besteval){
                        besteval = eval
                        move = i
                    }
                }
            }

            board[move] = aiSymbol.value
            when (getWinningPlayer(board)) {
                aiSymbol.value -> {
                    winner.value = "AI Won"
                    aiScore.value += 2
                    startNewRound()
                }
                "D" -> {
                    winner.value = "Draw"
                    aiScore.value += 1
                    humanScore.value += 1
                    startNewRound()
                }
                else -> {
                    isHumansTurn.value = true
                }
            }
        }

    }


    private fun minimax(board:List<String>, isMaximizing:Boolean):Int{
        val win = getWinningPlayer(board)
        if(win==aiSymbol.value) return 1
        if(win==humanSymbol.value) return -1
        if(win=="D") return 0
        if(isMaximizing){
            var bestScore = -1000
            var i = 0
            while(i<9){
                if(board[i] == ""){
                    val newBoard = board.toMutableList()
                    newBoard[i] = aiSymbol.value
                    val score = minimax(newBoard,false)
                    bestScore = maxOf(score,bestScore)


                }
                i++
            }
            return bestScore
        }else{
            var i = 0
            var bestScore = 1000
            while(i<9){
                if(board[i] == ""){
                    val newBoard = board.toMutableList()
                    newBoard[i] = humanSymbol.value
                    val score = minimax(newBoard,true)
                    bestScore = minOf(score,bestScore)
                }
                i++
            }
            return bestScore
        }

    }

    fun finishTurn(pos:Int){
        viewModelScope.launch {

            if (isHumansTurn.value && board[pos] == "" && winner.value.isEmpty()) {
                board[pos] = humanSymbol.value
                when (getWinningPlayer(board)) {
                    humanSymbol.value -> {
                        winner.value = "You Won"
                        humanScore.value += 2
                        startNewRound()
                    }
                    "D" -> {
                        winner.value = "Draw"
                        aiScore.value += 1
                        humanScore.value += 1
                        startNewRound()
                    }
                    else -> {
                        isHumansTurn.value = false
                        delay(200L)
                        bot()
                    }
                }
            }
        }
    }

    private suspend fun startNewRound(){
        delay(2000L)
        board = mutableStateListOf("", "", "", "", "", "", "", "", "")
        winner.value = ""
        aiSymbol.value = humanSymbol.value.also{humanSymbol.value = aiSymbol.value}
        isHumansTurn.value = humanSymbol.value == Symbol.X
        if(!isHumansTurn.value){
            board[4] = aiSymbol.value
            isHumansTurn.value = true
        }
    }




    private fun getWinningPlayer(
        board:List<String>
    ): String {

        return if (board[0] != "" && board[0] == board[1] && board[1] == board[2]) {
            board[0]
        }else if (board[3] != "" && board[3] == board[4] && board[4] == board[5]) {
            board[3]
        }else if (board[6] != "" && board[6] == board[7] && board[7] == board[8]) {
            board[6]
        }else if (board[0] != "" && board[0]== board[3] && board[3] == board[6]) {
            board[0]
        }else if (board[1] != "" && board[1] == board[4] && board[4] == board[7]) {
            board[1]
        }else if (board[2] != "" && board[2] == board[5] && board[5] == board[8]) {
            board[2]
        }else if (board[0] != "" && board[0] == board[4] && board[4] == board[8]) {
            board[0]
        }else if (board[2] != "" && board[2] == board[4] && board[4] == board[6]) {
            board[2]
        }
        else if (board.none { it == "" }){
            "D"
        }else{
            "N"
        }


    }
}