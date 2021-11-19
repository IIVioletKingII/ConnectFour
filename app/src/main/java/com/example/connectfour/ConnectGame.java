package com.example.connectfour;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;

import java.util.Timer;
import java.util.TimerTask;

public class ConnectGame {

	// note: when enough pieces line up to create a winning move, it is called or referred to as a 'connect' - where the default winning 'connect' is four pieces in a row

	private final int panelWidth;
	private final int panelHeight;

	private final int winningConnect;

	private boolean won = false;

	private final PositionState[][] positionPanel;

	private ImageView[][] piecePanel;

	/* panel example
	
	[0,0][0,1][0,2][0,3][0,4][0,5][0,6]
	[1,0][1,1][1,2][1,3][1,4][1,5][1,6]
	[2,0][2,1][2,2][2,3][2,4][2,5][2,6]
	[3,0][3,1][3,2][3,3][3,4][3,5][3,6]
	[4,0][4,1][4,2][4,3][4,4][4,5][4,6]
	[5,0][5,1][5,2][5,3][5,4][5,5][5,6]
	
	 */

	enum PositionState {
		EMPTY,
		PLAYER_ONE,
		PLAYER_TWO
	}

	enum WinningState {
		NONE,
		PLAYER_ONE,
		PLAYER_TWO
	}

	public boolean playerOneTurn = true;
	TextView titleView;

	private Timer gameTimer;
	private TimerUtil timeKeeper;
	private int decimals = 2; // min of 0, max of like 3

	/**
	 * height - height of the connect game
	 * width - width of the connect game
	 * connect - the winning connect length
	 */
	public ConnectGame( ) {

		this( 6, 7, 4 );
	}

	/**
	 * @param height  - height of the connect game
	 * @param width   - width of the connect game
	 * @param connect - the winning connect length
	 */
	public ConnectGame( int height, int width, int connect ) {

		gameTimer = new Timer( );
		panelHeight = height;
		panelWidth = width;
		winningConnect = connect;
		positionPanel = new PositionState[panelHeight][panelWidth];
		for( int col = 0; col < panelWidth; col++ )
			for( int row = 0; row < panelHeight; row++ )
				positionPanel[row][col] = PositionState.EMPTY;
	}

	public void setTitleView( TextView titleView ) {
		this.titleView = titleView;
	}

	/**
	 * x = int[0] and y = int[1]
	 * @param x the x position or column to search through for:
	 * @return the next available/empty position
	 */
	public int[] nextAvailablePos( int x ) {
		for( int height = panelHeight-1; height >= 0; height-- )
			if( positionPanel[height][x] == PositionState.EMPTY )
				return new int[] { x, height };
		return new int[] { x, -1 };
	}

	/**
	 *
	 * @returns the current player's turn then toggles it
	 */
	public boolean togglePlayerTurn( ) {
		playerOneTurn = !playerOneTurn;
		return !playerOneTurn;
	}

	public void startTimer( ) {

		timeKeeper = new TimerUtil( decimals );
		timeKeeper.start( );

		gameTimer.schedule( new TimerTask( ) {
			@Override
			public void run( ) {

				String headerText = "Time: " + timeKeeper.getTime( true );
				new Handler( Looper.getMainLooper( ) ).post( ( ) -> titleView.setText( headerText ) );
			}
		}, 0, 1 );
	}

	public void stopTimer( ) {
		gameTimer.cancel( );
	}

	/**
	 * @param gridPosToolTip the toolTipText of a grid position
	 * @return the x and y positions of a grid position where x = int[0] and y = int[1]
	 */
	public static int[] getGridPos( String gridPosToolTip ) {

		return new int[]{ Integer.parseInt( gridPosToolTip.substring( 2, 3 ) ), Integer.parseInt( gridPosToolTip.substring( 0, 1 ) ) };
	}

	public boolean timerStarted( ) {
		return timeKeeper.started( );
	}

	public int getPanelHeight( ) {
		return positionPanel.length;
	}

	public int getPanelWidth( ) {
		return positionPanel[0].length;
	}

	public int getWinningConnect( ) {
		return winningConnect;
	}

	public void setState( int row, int column, PositionState state ) {
		positionPanel[row][column] = state;
	}

	public PositionState getState( int row, int column ) {
		return positionPanel[row][column];
	}

	public boolean won( ) {
		return won;
	}

	public WinningState checkWinner( ) {
		WinningState winner = new ConnectChecker( panelWidth, panelHeight, winningConnect, positionPanel ).checkWinner( );
		if( winner != null && winner != WinningState.NONE )
			won = true;
		return winner;
	}

	public static void setBGColor( View view, int color ) {

		view.setBackgroundTintList( ColorStateList.valueOf( color ) );
//		ViewCompat
	}

	public static Color getBGColor( View view ) {
		int colorInt = view.getBackgroundTintList( ).getDefaultColor( );
		return Color.valueOf( Color.red( colorInt ), Color.green( colorInt ), Color.blue( colorInt ) );
	}

	public static WinningState getWinningState( PositionState state ) {

		switch( state ) {
			default:
				return WinningState.NONE;
			case PLAYER_ONE:
				return WinningState.PLAYER_ONE;
			case PLAYER_TWO:
				return WinningState.PLAYER_TWO;
		}
	}

	public static PositionState getPositionState( WinningState state ) {

		switch( state ) {
			default:
				return PositionState.EMPTY;
			case PLAYER_ONE:
				return PositionState.PLAYER_ONE;
			case PLAYER_TWO:
				return PositionState.PLAYER_TWO;
		}
	}
}
// do we even need a piecePanel if we base everything off of the positionPanel
