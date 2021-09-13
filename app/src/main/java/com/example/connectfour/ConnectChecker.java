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

		for( int column = 0; column < panelWidth; column++ ) { // width
			for( int row = 0; row < panelHeight; row++ ) { // height
				ConnectGame.PositionState currentState = positionPanel[row][column];
				if( currentState != ConnectGame.PositionState.EMPTY ) {

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
						winner = ConnectGame.getWinningState( currentState );
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

		ConnectGame.PositionState start = positionPanel[row][col];
		boolean fits = positionPanel.length > row + (winningConnect - 1);
		return start != ConnectGame.PositionState.EMPTY &&   /* fits && */   start == positionPanel[row + 1][col] && start == positionPanel[row + 2][col] && start == positionPanel[row + 3][col];
	}

	/**
	 * [a][b][c][d] -> a == b && a == c && a == d
	 *
	 * @param row - the horizontal position in the panel to start at
	 * @param col - the vertical position in the panel
	 * @return if there is a horizontal connect starting in this space going right three more spaces
	 */
	private boolean findHorizontalConnect( int col, int row ) {

		ConnectGame.PositionState start = positionPanel[row][col];
		boolean fits = positionPanel[0].length > col + (winningConnect - 1);
		return start != ConnectGame.PositionState.EMPTY &&   /* fits && */   start == positionPanel[row][col + 1] && start == positionPanel[row][col + 2] && start == positionPanel[row][col + 3];
	}

	/**
	 * [a][b][c][d] -> a == b && a == c && a == d
	 *
	 * @param row - the horizontal position in the panel to start at
	 * @param col - the vertical position in the panel to start at
	 * @return if there is a horizontal connect starting in this space going down and right three more spaces
	 */
	private boolean findAngledRightConnect( int col, int row ) {

		ConnectGame.PositionState start = positionPanel[row][col];
		boolean fits = positionPanel.length > row + (winningConnect - 1) && positionPanel[0].length > col + (winningConnect - 1);
		return start != ConnectGame.PositionState.EMPTY &&   /* fits && */   start == positionPanel[row + 1][col + 1] && start == positionPanel[row + 2][col + 2] && start == positionPanel[row + 3][col + 3];
	}

	/**
	 * [a][b][c][d] -> a == b && a == c && a == d
	 *
	 * @param row - the horizontal position in the panel to start at
	 * @param col - the vertical position in the panel to start at
	 * @return if there is a horizontal connect starting in this space going down and left three more spaces
	 */
	private boolean findAngledLeftConnect( int col, int row ) {

		ConnectGame.PositionState start = positionPanel[row][col];
		boolean fits = positionPanel.length > row + (winningConnect - 1) && 0 <= col - (winningConnect - 1);
		return start != ConnectGame.PositionState.EMPTY &&   /* fits && */   start == positionPanel[row + 1][col - 1] && start == positionPanel[row + 2][col - 2] && start == positionPanel[row + 3][col - 3];
	}

}
