package com.example.connectfour;

import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class ConnectFourGame {

	// note: when enough pieces line up to create a winning move, it is called or referred to as a 'connect' - where the default winning 'connect' is four pieces in a row

	private final int panelWidth;
	private final int panelHeight;

	private final int winningConnect;

	private final PositionState[][] panel;

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

	Timer gameTimer;
	double startTime = -1;
	int decimals = 2; // max of like 3

	/**
	 * height - height of the connect game
	 * width - width of the connect game
	 * connect - the winning connect length
	 */
	public ConnectFourGame( ) {

		this( 6, 7, 4 );
	}

	/**
	 * @param height  - height of the connect game
	 * @param width   - width of the connect game
	 * @param connect - the winning connect length
	 */
	public ConnectFourGame( int height, int width, int connect ) {

		gameTimer = new Timer( );
		panelHeight = height;
		panelWidth = width;
		winningConnect = connect;
		panel = new PositionState[panelHeight][panelWidth];
		for( int col = 0; col < panelWidth; col++ )
			for( int row = 0; row < panelHeight; row++ )
				panel[row][col] = PositionState.EMPTY;
	}

	public void startTimer( TextView headerTextView ) {

		startTime = System.currentTimeMillis( ) / 1000d;
		gameTimer.schedule( new TimerTask( ) {
			@Override
			public void run( ) {

				String headerText = "Time: " + getShortTime( true );
				new Handler( Looper.getMainLooper( ) ).post( ( ) -> headerTextView.setText( headerText ) );
			}
		}, 0, 1 );
	}

	public double getDoubleTime( ) {

		return System.currentTimeMillis( ) / 1000d - startTime;
	}

	public String getShortTime( boolean includeZeros ) {

		double time = getDoubleTime( );
//		time = 5680.342547543;
		int timeSec = (int) time; // 5680s
//		int timeMin = timeSec / 60; // 94
//		timeSec %= 60; // 40
//		int timeHour = timeMin / 60; //
//		timeMin %= 60; // 34
		String decimal = ("" + (time - (int) time)).substring( 1 ); // ".3279998302459717", or ".0" or "0.333", etc...

		// doesn't include the zeros in hours minutes and seconds i.e.: 1:3:50.25 should be 1:03:50.25
		String timeSMH = "" /*+ (timeHour != 0 ? timeHour + ":" + timeMin + ":" : (timeMin != 0 ? timeMin + ":" : ""))*/ + timeSec;

		if( includeZeros ) {

			int pow = (int) Math.pow( 10, decimals - decimal.length( ) + 1 );
			String extras = ("" + pow).substring( 1 );

			return timeSMH + (decimal + extras).substring( 0, decimals + 1 );
		}

		return timeSMH + (decimal).substring( 0, (decimal.length( ) > decimals ? decimals + 1 : decimal.length( )) );

	}

	public boolean timerStarted( ) {

		return startTime != -1d;
	}

	public int getPanelHeight( ) {

		return panel.length;
	}

	public int getPanelWidth( ) {

		return panel[0].length;
	}

	public int getWinningConnect( ) {

		return winningConnect;
	}

	public void setState( int row, int column, PositionState state ) {

		panel[row][column] = state;
	}

	public PositionState getState( int row, int column ) {

		return panel[row][column];
	}

	public WinningState checkWinner( ) {

		WinningState winner = WinningState.NONE;

		for( int column = 0; column < panelWidth; column++ ) { // width
			for( int row = 0; row < panelHeight; row++ ) { // height
				PositionState currentState = panel[row][column];
				if( currentState != PositionState.EMPTY ) {

					// connects found
					boolean vertical = false, horizontal = false;
					boolean angleRight = false, angleLeft = false;

					// need three spots in said direction when checking for connects
					boolean spotsBelow = row < panelHeight - winningConnect + 1;
					boolean spotsRight = column < panelWidth - winningConnect + 1;
					boolean spotsLeft = 0 <= column - winningConnect + 1;

					// if there enough positions in the if-statement's direction, check for said connect:
					if( spotsBelow )
						vertical = findVerticalConnect( column, row );

					if( spotsRight )
						horizontal = findHorizontalConnect( column, row );

					if( spotsBelow && spotsRight )
						angleRight = findAngledRightConnect( column, row );

					if( spotsBelow && spotsLeft )
						angleLeft = findAngledLeftConnect( column, row );

					// if any of the methods found a connect, set the winner to the current position
					if( vertical || horizontal || angleRight || angleLeft )
						winner = getWinningState( currentState );
				}
			}
		}

		return winner;
	}

	/**
	 * [a][b][c][d] -> a == b && a == c && a == d
	 *
	 * @param row - the horizontal position in the panel
	 * @param col - the vertical position in the panel to start at
	 * @return if there is a vertical connect starting in this space going down three more spaces
	 */
	private boolean findVerticalConnect( int col, int row ) {

		PositionState start = panel[row][col];
		boolean fits = panel.length > row + (winningConnect - 1);
		return start != PositionState.EMPTY &&   /* fits && */   start == panel[row + 1][col] && start == panel[row + 2][col] && start == panel[row + 3][col];
	}

	/**
	 * [a][b][c][d] -> a == b && a == c && a == d
	 *
	 * @param row - the horizontal position in the panel to start at
	 * @param col - the vertical position in the panel
	 * @return if there is a horizontal connect starting in this space going right three more spaces
	 */
	private boolean findHorizontalConnect( int col, int row ) {

		PositionState start = panel[row][col];
		boolean fits = panel[0].length > col + (winningConnect - 1);
		return start != PositionState.EMPTY &&   /* fits && */   start == panel[row][col + 1] && start == panel[row][col + 2] && start == panel[row][col + 3];
	}

	/**
	 * [a][b][c][d] -> a == b && a == c && a == d
	 *
	 * @param row - the horizontal position in the panel to start at
	 * @param col - the vertical position in the panel to start at
	 * @return if there is a horizontal connect starting in this space going down and right three more spaces
	 */
	private boolean findAngledRightConnect( int col, int row ) {

		PositionState start = panel[row][col];
		boolean fits = panel.length > row + (winningConnect - 1) && panel[0].length > col + (winningConnect - 1);
		return start != PositionState.EMPTY &&   /* fits && */   start == panel[row + 1][col + 1] && start == panel[row + 2][col + 2] && start == panel[row + 3][col + 3];
	}

	/**
	 * [a][b][c][d] -> a == b && a == c && a == d
	 *
	 * @param row - the horizontal position in the panel to start at
	 * @param col - the vertical position in the panel to start at
	 * @return if there is a horizontal connect starting in this space going down and left three more spaces
	 */
	private boolean findAngledLeftConnect( int col, int row ) {

		PositionState start = panel[row][col];
		boolean fits = panel.length > row + (winningConnect - 1) && 0 <= col - (winningConnect - 1);
		return start != PositionState.EMPTY &&   /* fits && */   start == panel[row + 1][col - 1] && start == panel[row + 2][col - 2] && start == panel[row + 3][col - 3];
	}


	private static WinningState getWinningState( PositionState state ) {

		switch( state ) {
			default:
				return WinningState.NONE;
			case PLAYER_ONE:
				return WinningState.PLAYER_ONE;
			case PLAYER_TWO:
				return WinningState.PLAYER_TWO;
		}
	}

	private static PositionState getPositionState( WinningState state ) {

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
