package ph.stacktrek.novare.game.tuliaomiranda.snakeandladder

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import ph.stacktrek.novare.game.tuliaomiranda.snakeandladder.adapters.PlayerAdapter
import ph.stacktrek.novare.game.tuliaomiranda.snakeandladder.adapters.SwipeCallback
import ph.stacktrek.novare.game.tuliaomiranda.snakeandladder.dao.PlayerDAO
import ph.stacktrek.novare.game.tuliaomiranda.snakeandladder.dao.PlayerDAOSQLLiteImplementation

import ph.stacktrek.novare.game.tuliaomiranda.snakeandladder.databinding.ActivityPlayerBinding
import ph.stacktrek.novare.game.tuliaomiranda.snakeandladder.databinding.PlayerCreateBinding
import ph.stacktrek.novare.game.tuliaomiranda.snakeandladder.model.Player

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private lateinit var playerCreateBinding: PlayerCreateBinding

    private lateinit var playerDAO: PlayerDAO
    private lateinit var playerAdapter: PlayerAdapter
    private lateinit var itemTouchHelper: ItemTouchHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        playerCreateBinding = PlayerCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadPlayers()

        binding.addPlayerButton.setOnClickListener {
            val player = Player("")
            Log.d("This is player size", playerDAO.getPlayers().size.toString())
            player.nickname = binding.nicknameEditText.text.toString()

            val playerDAO = PlayerDAOSQLLiteImplementation(applicationContext)
            playerDAO.addPlayer(player)
            playerAdapter.addPlayer(player)

            binding.addPlayerButton.isEnabled = playerDAO.getPlayers().size != 4

            if ( playerDAO.getPlayers().size in 2 .. 4) {
                binding.startButton.isEnabled = true

            } else {
                binding.startButton.isEnabled = false
                Snackbar.make(binding.root,
                    "Players should in range of 2-4",
                    Snackbar.LENGTH_SHORT).show()
            }
        }

        //Start Button
        binding.startButton.setOnClickListener {
            val goToGame = Intent(
                applicationContext,
                GameActivity::class.java
            )
            startActivity(goToGame)
            finish()
        }

    }

    private fun loadPlayers(){
        playerDAO = PlayerDAOSQLLiteImplementation(applicationContext)
        playerAdapter = PlayerAdapter(applicationContext, playerDAO.getPlayers())
        with(binding.playerRecyclerView){
            layoutManager = LinearLayoutManager(applicationContext,
                LinearLayoutManager.VERTICAL,false)

            adapter = playerAdapter
        }

    }
}


