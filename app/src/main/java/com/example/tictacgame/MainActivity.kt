package com.example.tictacgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tictacgame.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlin.random.Random
import kotlin.random.nextInt

class MainActivity : AppCompatActivity() {

    lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.playOfflineBtn.setOnClickListener {
            createofflinegame()
        }

        binding.createOnlineGameBtn.setOnClickListener {
            createonlinegame()
        }
        binding.joinOnlineGameBtn.setOnClickListener {
            joinonlinegame()
        }
    }

    fun createofflinegame(){
        GameData.saveGameModel(
            GameModel(
                gameStatus = GameStatus.JOINED
            )
        )
        startgame()
    }

    fun createonlinegame(){
        GameData.myID="X"
        GameData.saveGameModel(
            GameModel(
                gameStatus = GameStatus.CREATED,
                gameId= Random.nextInt(1000..9999).toString()
            )
        )
        startgame()
    }

    fun joinonlinegame(){
        var gameId = binding.gameIdInput.text.toString()
        if(gameId.isEmpty()){
            binding.gameIdInput.setError("Please enter game ID")
            return
        }
        GameData.myID="0"
        Firebase.firestore.collection("games")
            .document(gameId)
            .get()
            .addOnSuccessListener {
                val model =it?.toObject(GameModel::class.java)
                if(model==null){
                    binding.gameIdInput.setError("Please enter valid game ID")
                }else{
                    model.gameStatus=GameStatus.JOINED
                    GameData.saveGameModel(model)
                    startgame()
                }
            }
    }

    fun startgame(){
        startActivity(Intent(this,GameActivity::class.java))
    }
}