package com.example.connectfour;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Button;
import android.widget.GridLayout;

import android.view.View;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

	private GridLayout gridLayout;

	private ConnectFourGame connectFourGame;

	public static int border = 2;

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

		new Thread( this::initSquares ).start( );

		initToolbar( );

		TextView title = findViewById( R.id.gameTitle );
		String titleText = "" + connectFourGame.getPanelHeight( ) + "*" + connectFourGame.getPanelWidth( ) + " Connect Game";
		title.setText( titleText );

		gridLayout.setOnClickListener( ( v ) -> {
			if( !connectFourGame.timerStarted( ) )
				connectFourGame.startTimer( title );
		} );

	}

	private void initSquares( ) {

		int panelHeight = connectFourGame.getPanelHeight( );
		int panelWidth = connectFourGame.getPanelWidth( );

		double gridLayoutHeight = gridLayout.getMeasuredHeight( );

		while( gridLayoutHeight <= 0 )
			gridLayoutHeight = gridLayout.getMeasuredHeight( );

		DisplayMetrics displayMetrics = new DisplayMetrics( );
		getWindowManager( ).getDefaultDisplay( ).getMetrics( displayMetrics );

		double gridLayoutWidth = displayMetrics.widthPixels;

		int pixelHeight = (int) (gridLayoutHeight / panelHeight - 2 * border);
		int pixelWidth = (int) (gridLayoutWidth / panelWidth - 2 * border);

		int squareSize = panelWidth > panelHeight ? pixelWidth : pixelHeight;

		new Handler( Looper.getMainLooper( ) ).post( ( ) -> gridLayout.removeAllViews( ) );

		new Handler( Looper.getMainLooper( ) ).post( ( ) -> {
			gridLayout.setRowCount( panelHeight );
			gridLayout.setColumnCount( panelWidth );
		} );

		for( int height = 0; height < panelHeight; height++ ) {
			for( int width = 0; width < panelWidth; width++ ) {

				Button curButton = new Button( this );
				String buttonText = "" + height + "-" + width;
				curButton.setText( buttonText );
//                curButton.setHighlightColor( Color.BLUE );
				curButton.setPadding( border, border, border, border );

				curButton.setWidth( squareSize );
				curButton.setMinimumWidth( squareSize );

				curButton.setHeight( squareSize );
				curButton.setMinimumHeight( squareSize );

				new Handler( Looper.getMainLooper( ) ).post( ( ) -> gridLayout.addView( curButton ) );
			}
		}

//		new Handler( Looper.getMainLooper( ) ).post( ( ) -> gridLayout.set );
	}

	private void initToolbar( ) {

		Toolbar toolbar = findViewById( R.id.edit_toolbar );
		toolbar.setTitle( "" );
		toolbar.setNavigationIcon( ContextCompat.getDrawable( this, R.drawable.back_arrow ) );
		toolbar.setNavigationOnClickListener( view -> finish( ) );
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