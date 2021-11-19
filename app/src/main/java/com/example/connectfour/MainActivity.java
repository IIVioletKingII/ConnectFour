package com.example.connectfour;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.connectfour.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

	private ActivityMainBinding binding;

	private ConnectGame connectGame;

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
		int newHeight = getViewInt( editBoardHeight );
		int newWidth = getViewInt( editBoardWidth );
		int newConnect = getViewInt( editWinningConnect );
		if( newHeight > newConnect && newWidth > newConnect && newConnect > 2 ) {
			connectGame = new ConnectGame( newHeight, newWidth, newConnect );
			// open the activity for the game
			openGame( );
		} else {
			Toast.makeText( this, "Invalid Dimensions", Toast.LENGTH_SHORT ).show( );
		}
	}

	public static int getViewInt( EditText text ) {

		return Integer.parseInt( text.getText( ).toString( ) );
	}

	public void openGame( ) {

		Intent intent = new Intent( this, GameActivity.class );
		intent.putExtra( "GAME_HEIGHT", connectGame.getPanelHeight( ) )
				.putExtra( "GAME_WIDTH", connectGame.getPanelWidth( ) )
				.putExtra( "GAME_CONNECT", connectGame.getWinningConnect( ) );
		startActivity( intent );

	}

}