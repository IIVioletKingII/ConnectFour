package com.example.connectfour;

public class ConnectChecker {


	private final int panelWidth;
	private final int panelHeight;

	private final int winningConnect;

	private final ConnectGame.PositionState[][] positionPanel;

	public ConnectChecker( int panelWidth, int panelHeight, int winningConnect, ConnectGame.PositionState[][] positionPanel ) {

		this.panelWidth = panelWidth;
		this.panelHeight = panelHeight;
		this.winningConnect = winningConnect;
		this.positionPanel = positionPanel;
	}

	public ConnectGame.WinningState checkWinner( ) {

		ConnectGame.WinningState winner = ConnectGame.WinningState.NONE;

		for( int row = 0; row < panelHeight; row++ ) { // the row you are checking
			for( int column = 0; column < panelWidth; column++ ) { // the column you are checking
				ConnectGame.PositionState currentState = positionPanel[row][column];
				if( currentState != ConnectGame.PositionState.EMPTY ) {

					// connects found
					boolean vertical = false, horizontal = false;
					boolean angleLeft = false, angleRight = false;

					// need three spots in said direction when checking for connects
					boolean spotsBelow = row < panelHeight - winningConnect + 1;
					boolean spotsRight = column < panelWidth - winningConnect + 1;
					boolean spotsLeft = 0 <= column - winningConnect + 1;

					// if there enough positions in the if-statement's direction, check for said connect:
					if( spotsBelow )
						vertical = findVerticalConnect( row, column );

					if( spotsRight )
						horizontal = findHorizontalConnect( row, column );

					if( spotsBelow && spotsLeft )
						angleLeft = findAngledLeftConnect( row, column );

					if( spotsBelow && spotsRight )
						angleRight = findAngledRightConnect( row, column );

					// if any of the methods found a connect, set the winner to the current position
					if( vertical || horizontal || angleRight || angleLeft )
						winner = ConnectGame.getWinningState( currentState );
				}
			}
		}

		return winner;
	}

	/**
	 * [a][b][c][d] -> a == b && a == c && a == d
	 *
	 * will throw a NullPointerLocation if the row and column provided would result in a connect of pieces that is out of bounds
	 *
	 * @param row - the horizontal position in the panel
	 * @param col - the vertical position in the panel to start at
	 * @return if there is a vertical connect starting in this space going down three more spaces
	 */
	private boolean findVerticalConnect( int row, int col ) {

		ConnectGame.PositionState start = positionPanel[row][col];
		if( start == ConnectGame.PositionState.EMPTY )
			return false;

		for( int i = 0; i < winningConnect; i++ )
			if( start != positionPanel[row + i][col] )
				return false;
		return true;
	}

	/**
	 * [a][b][c][d] -> a == b && a == c && a == d
	 *
	 * will throw a NullPointerLocation if the row and column provided would result in a connect of pieces that is out of bounds
	 *
	 * @param row - the horizontal position in the panel to start at
	 * @param col - the vertical position in the panel
	 * @return if there is a horizontal connect starting in this space going right three more spaces
	 */
	private boolean findHorizontalConnect( int row, int col ) {

		ConnectGame.PositionState start = positionPanel[row][col];
		if( start == ConnectGame.PositionState.EMPTY )
			return false;

		for( int i = 0; i < winningConnect; i++ )
			if( start != positionPanel[row][col + i] )
				return false;
		return true;
	}

	/**
	 * [a][b][c][d] -> a == b && a == c && a == d
	 *
	 * will throw a NullPointerLocation if the row and column provided would result in a connect of pieces that is out of bounds
	 *
	 * @param row - the horizontal position in the panel to start at
	 * @param col - the vertical position in the panel to start at
	 * @return if there is a connect starting in this space going down and right winningConnect - 1 more spaces
	 */
	private boolean findAngledRightConnect( int row, int col ) {

		ConnectGame.PositionState start = positionPanel[row][col];
		if( start == ConnectGame.PositionState.EMPTY )
			return false;

		for( int i = 0; i < winningConnect; i++ )
			if( start != positionPanel[row + i][col + i] )
				return false;
		return true;
	}

	/**
	 * [a][b][c][d] -> a == b && a == c && a == d
	 *
	 * will throw a NullPointerLocation if the row and column provided would result in a connect of pieces that is out of bounds
	 *
	 * @param row - the horizontal position in the panel to start at
	 * @param col - the vertical position in the panel to start at
	 * @return if there is a connect starting in this space going down and left winningConnect - 1 more spaces
	 */
	private boolean findAngledLeftConnect( int row, int col ) {

		ConnectGame.PositionState start = positionPanel[row][col];
		if( start == ConnectGame.PositionState.EMPTY )
			return false;

		for( int i = 0; i < winningConnect; i++ )
			if( start != positionPanel[row + i][col - i] )
				return false;
		return true;
	}

}
