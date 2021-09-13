package com.example.connectfour;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.GridLayout;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

	private GridLayout gridLayout;

	private ConnectGame connectGame;

	public static int border = 2;

	public int squareSize = 0;

	private ImageView[][] piecePanel;

	TextView titleView;

	boolean timerStarted = false;

	@Override
	protected void onCreate( Bundle savedInstanceState ) {

		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_game );

		connectGame = new ConnectGame( );

		int panelHeight = getIntent( ).getIntExtra( "GAME_HEIGHT", connectGame.getPanelHeight( ) );
		int panelWidth = getIntent( ).getIntExtra( "GAME_WIDTH", connectGame.getPanelWidth( ) );
		int winningConnect = getIntent( ).getIntExtra( "GAME_CONNECT", connectGame.getWinningConnect( ) );

		connectGame = new ConnectGame( panelHeight, panelWidth, winningConnect );

		gridLayout = findViewById( R.id.grid_layout );

		initToolbar( );

		piecePanel = new ImageView[ /*rows*/ panelHeight][ /*columns*/ panelWidth];

		new Thread( this::initSquares ).start( );

		titleView = findViewById( R.id.gameTitle );
		String titleText = "" + panelHeight + "*" + panelWidth + " Connect Game";
		titleView.setText( titleText );

		connectGame.setTitleView( titleView );

		gridLayout.setOnClickListener( ( v ) -> {

			if( !timerStarted )
				timerStarted = connectGame.startTimer( );
		} );
	}

	private void initToolbar( ) {

		Toolbar toolbar = findViewById( R.id.edit_toolbar );
		toolbar.setTitle( "" );
		toolbar.setNavigationIcon( ContextCompat.getDrawable( this, R.drawable.back_arrow ) );
		toolbar.setNavigationOnClickListener( view -> finish( ) );
	}

	private void initSquares( ) {

		int panelHeight = connectGame.getPanelHeight( );
		int panelWidth = connectGame.getPanelWidth( );

		double gridLayoutHeight = gridLayout.getMeasuredHeight( );

		while( gridLayoutHeight <= 0 )
			gridLayoutHeight = gridLayout.getMeasuredHeight( );

		DisplayMetrics displayMetrics = new DisplayMetrics( );
		getWindowManager( ).getDefaultDisplay( ).getMetrics( displayMetrics );

		double gridLayoutWidth = displayMetrics.widthPixels;

		int pixelHeight = (int) (gridLayoutHeight / panelHeight - 2 * border);
		int pixelWidth = (int) (gridLayoutWidth / panelWidth - 2 * border);

		squareSize = panelWidth > panelHeight ? pixelWidth : pixelHeight;

		new Handler( Looper.getMainLooper( ) ).post( ( ) -> gridLayout.removeAllViews( ) );

		new Handler( Looper.getMainLooper( ) ).post( ( ) -> {
			gridLayout.setRowCount( panelHeight );
			gridLayout.setColumnCount( panelWidth );
		} );

		for( int height = 0; height < panelHeight; height++ ) {
			for( int width = 0; width < panelWidth; width++ ) {

				piecePanel[height][width] = new ImageView( this );
				String gridPosText = height + "_" + width;
				piecePanel[height][width].setTooltipText( gridPosText );

				piecePanel[height][width].setOnClickListener( ( v ) -> {
					Log.e( "ONCLICK", gridPosText );
					gridPosClicked( v );
				} ); // gridPosClicked

				piecePanel[height][width].setBackgroundResource( R.drawable.piece );

				ConnectGame.setBGColor( piecePanel[height][width], Color.BLUE );

				piecePanel[height][width].setMinimumWidth( squareSize );
				piecePanel[height][width].setMaxWidth( squareSize );

				piecePanel[height][width].setMinimumHeight( squareSize );
				piecePanel[height][width].setMaxHeight( squareSize );

				ImageView curGridPos = piecePanel[height][width];

				new Handler( Looper.getMainLooper( ) ).post( ( ) -> gridLayout.addView( curGridPos ) );
			}
		}
	}

	public void gridPosClicked( View view ) {

		if( !timerStarted )
			timerStarted = connectGame.startTimer( );

		String toolTipText = view.getTooltipText( ).toString( );

		// set bottom of that column to be person's turn

		ImageView currentPosition = (ImageView) view;

		if( currentPosition != null ) {

			int[] pos = getGridPos( toolTipText );

			playPiece( pos[0], pos[1], connectGame.togglePlayerTurn( ) );
		}
	}

	/**
	 * @param pieceXPos the x position or column in the connect game panel
	 * @param pieceYPos the y position or row in the connect game panel
	 * @param playerOne whether it is player one's turn
	 * @returns if the play was successful
	 */
	public boolean playPiece( int pieceXPos, int pieceYPos, boolean playerOne ) {

		int pieceTargetPosition = connectGame.nextAvailablePos( pieceXPos )[1];
		if( pieceTargetPosition == -1 )
			return false;


			/*
			// transition
			ImageView hiddenImage = new ImageView( this );
			float x = piecePanel[0][pieceXPos].getX( );
			hiddenImage.setX( x );

			ConnectGame.setBGColor( hiddenImage, playerOne ? Color.RED : Color.YELLOW );
*/
		//

		ConnectGame.setBGColor( piecePanel[pieceTargetPosition][pieceXPos], playerOne ? Color.RED : Color.YELLOW );
		connectGame.setState( pieceTargetPosition, pieceXPos, playerOne ? ConnectGame.PositionState.PLAYER_ONE : ConnectGame.PositionState.PLAYER_TWO );


		return true;
	}

	/**
	 * @param gridPosToolTip the toolTipText of a grid position
	 * @return the x and y positions of a grid position where x = int[0] and y = int[1]
	 */
	private int[] getGridPos( @NonNull String gridPosToolTip ) {

		return new int[]{ Integer.parseInt( gridPosToolTip.substring( 2, 3 ) ), Integer.parseInt( gridPosToolTip.substring( 0, 1 ) ) };
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