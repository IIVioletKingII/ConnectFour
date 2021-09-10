package com.example.connectfour;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.connectfour.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

	private ActivityMainBinding binding;

	private ConnectFourGame connectFourGame;

	EditText editBoardHeight;
	EditText editBoardWidth;
	EditText editWinningConnect;

	@Override
	protected void onCreate( Bundle savedInstanceState ) {

		super.onCreate( savedInstanceState );
		binding = ActivityMainBinding.inflate( getLayoutInflater( ) );
		setContentView( binding.getRoot( ) );

		editBoardHeight = binding.editBoardHeight;
		editBoardWidth = binding.editBoardWidth;
		editWinningConnect = binding.editWinningConnect;

		editBoardHeight.setText( "6" );
		editBoardWidth.setText( "7" );
		editWinningConnect.setText( "4" );

	}

	public void playGame( View view ) {

		playGame( );
	}

	public void playGame( ) {

		// create game based on entered sizes
		connectFourGame = new ConnectFourGame( getViewInt( editBoardHeight ), getViewInt( editBoardWidth ), getViewInt( editWinningConnect ) );

		// open the activity for the game
		openGame( );
	}

	public static int getViewInt( EditText text ) {

		return Integer.parseInt( text.getText( ).toString( ) );
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