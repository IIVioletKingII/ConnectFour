package com.example.connectfour;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.gridlayout.widget.GridLayout;

import android.view.View;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    
    private GridLayout gridLayout;
    
    private ConnectFourGame connectFourGame;
    
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_game );
        
        connectFourGame = new ConnectFourGame( );
        
        int panelHeight = getIntent( ).getIntExtra( "GAME_HEIGHT", connectFourGame.getPanelHeight( ) );
        int panelWidth = getIntent( ).getIntExtra( "GAME_WIDTH", connectFourGame.getPanelWidth( ) );
        int winningConnect = getIntent( ).getIntExtra( "GAME_CONNECT", connectFourGame.getWinningConnect( ) );
        
        connectFourGame = new ConnectFourGame( panelHeight, panelWidth, winningConnect );
        
        gridLayout = findViewById( R.id.grid_layout );
        gridLayout.setRowCount( panelHeight );
        gridLayout.setColumnCount( panelWidth );
    }
    
    @Override
    protected void onResume( ) {
        super.onResume( );
    }
    
    @Override
    public void onClick( View view ) {
    
    }
    
    /*
    
    Grid gridView;
    grid.setOnClickListener( view -> {

        if( position == randInt ) {
    
            numColumns += 1;
            alpha += 0.1f;
        } else {
        
            // Negative click (When you click in the wrong spot)
            numColumns -= 1;
            alpha -= 0.1f;
        }
        
        gridView.setNumColumns( numColumns );
        gridView.setAdapter( adapter );
        adapter.notifyDataSetChanged( );
        randInt = rand.nextInt( (numColumns * numColumns) - 0 );

    } );
    
     */
    
}