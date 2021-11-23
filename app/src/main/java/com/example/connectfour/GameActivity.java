package com.example.connectfour;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.GridLayout;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

	private GridLayout gridLayout;

	private ConnectGame connectGame;

	public int squareSize = 0;
	public int layoutBorder = 16;

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

			if( !timerStarted ) {
				timerStarted = true;
				connectGame.startTimer( );
			}
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

		int pixelHeight = (int) (gridLayoutHeight / panelHeight - 2 * layoutBorder);
		int pixelWidth = (int) (gridLayoutWidth / panelWidth - 2 * layoutBorder);

		squareSize = Math.min( pixelWidth, pixelHeight );

		new Handler( Looper.getMainLooper( ) ).post( ( ) -> gridLayout.removeAllViews( ) );

		new Handler( Looper.getMainLooper( ) ).post( ( ) -> {
			gridLayout.setRowCount( panelHeight );
			gridLayout.setColumnCount( panelWidth );
		} );

		for( int height = 0; height < panelHeight; height++ ) {
			for( int width = 0; width < panelWidth; width++ ) {

				piecePanel[height][width] = createBasicSquare( );

				String gridPosText = height + "_" + width;
				piecePanel[height][width].setTooltipText( gridPosText );

				piecePanel[height][width].setOnClickListener( ( v ) -> {
					Log.i( "ONCLICK", gridPosText );
					gridPosClicked( v );
				} ); // gridPosClicked

				ConnectGame.setBGColor( piecePanel[height][width], Color.BLUE );

				ImageView curGridPos = piecePanel[height][width];

				new Handler( Looper.getMainLooper( ) ).post( ( ) -> gridLayout.addView( curGridPos ) );
			}
		}
	}

	public ImageView createBasicSquare( ) {

		ImageView tempImageView = new ImageView( this );

		tempImageView.setBackgroundResource( R.drawable.piece );
//		tempImageView.setBord

		// https://stackoverflow.com/questions/3416087/how-to-set-margin-of-imageview-using-code-not-xml
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT );
		layoutParams.setMargins( layoutBorder, layoutBorder, layoutBorder, layoutBorder );
		tempImageView.setLayoutParams( layoutParams );

		tempImageView.setMinimumWidth( squareSize );
		tempImageView.setMaxWidth( squareSize );

		tempImageView.setMinimumHeight( squareSize );
		tempImageView.setMaxHeight( squareSize );

		return tempImageView;
	}

	public void gridPosClicked( View view ) {

		if( !timerStarted ) {
			timerStarted = true;
			connectGame.startTimer( );
		}

		// set bottom of that column to be person's turn

		if( !connectGame.won( ) )
			if( !playPiece( getGridPos( view.getTooltipText( ).toString( ) )[0], connectGame.togglePlayerTurn( ) ) )
				Log.e( "PLAY_PIECE", "Failed to find the next available position" );

		checkSquareForWin( );

	}

	/**
	 * @param pieceXPos the x position or column in the connect game panel
	 * @param playerOne whether it is player one's turn
	 * @returns if the play was successful
	 */
	public boolean playPiece( int pieceXPos, boolean playerOne ) {

		int pieceTargetPosition = connectGame.nextAvailablePos( pieceXPos )[1];
		if( pieceTargetPosition == -1 )
			return false;

		ConstraintLayout layout = findViewById( R.id.layout );

		// transition
		ImageView hiddenImage = createBasicSquare( );
		ConnectGame.setBGColor( hiddenImage, playerOne ? Color.RED : Color.YELLOW );

		layout.addView( hiddenImage );

		hiddenImage.setX( layout.getX( ) + piecePanel[0][pieceXPos].getX( ) );
		hiddenImage.setY( 0 );

		//=================

		long waitTime = 750;

		float targetPosition = piecePanel[pieceTargetPosition][pieceXPos].getY( ) + 260 - 90 - layoutBorder;
		ObjectAnimator animation = ObjectAnimator.ofFloat( hiddenImage, "translationY", 0, targetPosition );
		animation.setDuration( waitTime );
		animation.start( );

		Log.e( "LOGGER", "top y: " + piecePanel[0][pieceXPos].getY( ) );
		Log.e( "LOGGER", "og y: " + piecePanel[pieceTargetPosition][pieceXPos].getY( ) );
		Log.e( "LOGGER", "targetPosition: " + targetPosition );

		connectGame.setState( pieceTargetPosition, pieceXPos, playerOne ? ConnectGame.PositionState.PLAYER_ONE : ConnectGame.PositionState.PLAYER_TWO );

		new Thread( ( ) -> {
			long startTime = System.currentTimeMillis( );
			while( System.currentTimeMillis( ) < startTime + waitTime /*+ 10*/ ) ;
			ConnectGame.setBGColor( piecePanel[pieceTargetPosition][pieceXPos], playerOne ? Color.RED : Color.YELLOW );
			new Handler( Looper.getMainLooper( ) ).post( ( ) -> layout.removeView( hiddenImage ) );
		} ).start( );

		return true;
	}

	public void checkSquareForWin( ) {
		ConnectGame.WinningState winningState = connectGame.checkWinner( );
		if( winningState == ConnectGame.WinningState.NONE )
			return;

		connectGame.stopTimer( );

		String winner = ("" + winningState).toLowerCase( ).replace( "_", " " ).replace( "p", "P" );

		Toast.makeText( this, winner + " wins!", Toast.LENGTH_SHORT ).show( );

	}

	/**
	 * @param gridPosToolTip the toolTipText of a grid position
	 * @return the x and y positions of a grid position where x = int[0] and y = int[1]
	 */
	private int[] getGridPos( @NonNull String gridPosToolTip ) {
		if( gridPosToolTip.contains( "_" ) )
			return new int[]{ Integer.parseInt( gridPosToolTip.substring( gridPosToolTip.indexOf( "_" ) + 1 ) ), Integer.parseInt( gridPosToolTip.substring( 0, gridPosToolTip.indexOf( "_" ) ) ) };
		return new int[]{ 0, 0 };
	}

	@Override
	protected void onResume( ) {
		initToolbar( );
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