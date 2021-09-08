package com.example.connectfour;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.connectfour.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    
    private ActivityMainBinding binding;
    
    private ConnectFourGame connectFourGame;
    
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        
        super.onCreate( savedInstanceState );
        binding = ActivityMainBinding.inflate( getLayoutInflater( ) );
        setContentView( binding.getRoot( ) );

        Button startGame = binding.button;
    
    }
    
    public void playGame( View view ) {
        playGame( );
    }
    
    public void playGame( ) {
        
        // create pop-up menu to ask for dimensions
//        connectFourGame = new ConnectFourGame( height, width, connect );
        connectFourGame = new ConnectFourGame( );
        
        // open the activity for the game
        openGame( );
    }
    
    public void storeHeight( View view ) {
        storeHeight( );
    }
    
    public void storeHeight( ) {
    
    }
    
    public void storeWidth( View view ) {
        storeWidth( );
    }
    
    public void storeWidth( ) {
    
    }
    
    public void storeConnectLength( View view ) {
        storeConnectLength( );
    }
    
    public void storeConnectLength( ) {
    
    }
    
    public void openGame( ) {
        
        Intent intent = new Intent( this, GameActivity.class );
        intent.putExtra( "GAME_HEIGHT", connectFourGame.getPanelHeight( ) )
                .putExtra( "GAME_WIDTH", connectFourGame.getPanelWidth( ) )
                .putExtra( "GAME_CONNECT", connectFourGame.getWinningConnect( ) );
        startActivity( intent );
        
    }
    
}