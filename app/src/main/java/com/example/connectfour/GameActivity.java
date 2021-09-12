package com.example.connectfour;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.GridLayout;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

	private GridLayout gridLayout;

	private ConnectFourGame connectFourGame;

	public static int border = 2;

	public int squareSize = 0;

	private ImageView[][] pieceGrid;

	private boolean playerOneTurn;

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
		String titleText = "" + panelHeight + "*" + panelWidth + " Connect Game";
		title.setText( titleText );

		pieceGrid = new ImageView[ /*rows*/ panelHeight][ /*columns*/ panelWidth];

		AtomicInteger toastShowTimes = new AtomicInteger( );

		AtomicReference<Toast> toastMessage = new AtomicReference<>( Toast.makeText( this, "initiated toast", Toast.LENGTH_SHORT ) );

		gridLayout.setOnClickListener( ( v ) -> {
			toastMessage.get( ).cancel( );
			toastMessage.set( Toast.makeText( this, "Display " + (toastShowTimes.incrementAndGet( )), Toast.LENGTH_SHORT ) );
			toastMessage.get( ).show( );

			if( !connectFourGame.timerStarted( ) )
				connectFourGame.startTimer( title );
		} );

		new Thread( ( ) -> {

			while( pieceGrid[4][3] == null ) ;

			ImageView examplePos = pieceGrid[1][3];
			Drawable buttonDrawable = examplePos.getBackground( );
			buttonDrawable = DrawableCompat.wrap( buttonDrawable );
			// the color is a direct color int and not a color resource
			DrawableCompat.setTint( buttonDrawable, Color.RED );
			examplePos.setBackground( buttonDrawable );
		} )/*.start( )*/;

	}

	@RequiresApi(api = Build.VERSION_CODES.O)
	public void gridPosClicked( View view ) {

		String toolTipText = view.getTooltipText( ).toString( );

		// set bottom of that column to be person's turn

		ImageView currentPosition = (ImageView) view;

		if( currentPosition != null ) {

			int x = Integer.parseInt( toolTipText.substring( 0, 1 ) );
			int y = Integer.parseInt( toolTipText.substring( 2, 3 ) );

//			runTransitionToPos( x, y, playerOneTurn );

			setColor( currentPosition, /*red's turn ? */Color.RED /* : Color.YELLOW*/ );
//			new Thread( () -> setColor( currentPosition, /*red's turn ? */Color.RED /* : Color.YELLOW*/ ) ).start( );
			Toast.makeText( this, "current: " + toolTipText, Toast.LENGTH_SHORT ).show( );
		}
	}

	public static void setColor( View view, int color ) {

		Drawable buttonDrawable = view.getBackground( );
		buttonDrawable = DrawableCompat.wrap( buttonDrawable );
		// the color is a direct color int and not a color resource
		DrawableCompat.setTint( buttonDrawable, color );
		view.setBackground( buttonDrawable );
	}

	private int[] getGridPos( int x, int y ) {
		return new int[]{ 0, 0 };
	}

	private void runTransitionToPos( int x, int y, boolean playerOne ) {
		ImageView hiddenImage = pieceGrid[x][y];


	}

	@RequiresApi(api = Build.VERSION_CODES.O)
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

		squareSize = panelWidth > panelHeight ? pixelWidth : pixelHeight;

		new Handler( Looper.getMainLooper( ) ).post( ( ) -> gridLayout.removeAllViews( ) );

		new Handler( Looper.getMainLooper( ) ).post( ( ) -> {
			gridLayout.setRowCount( panelHeight );
			gridLayout.setColumnCount( panelWidth );
		} );

		for( int height = 0; height < panelHeight; height++ ) {
			for( int width = 0; width < panelWidth; width++ ) {

				ImageView curGridPos = new ImageView( this );
				String gridPosText = height + "_" + width;
				curGridPos.setTooltipText( gridPosText );

				curGridPos.setOnClickListener( ( v ) -> {
					Log.e( "ONCLICK", gridPosText + ":clicked: " + curGridPos.getTooltipText( ) );
					gridPosClicked( v );
				} ); // gridPosClicked

				curGridPos.setBackgroundResource( R.drawable.piece );
				Drawable buttonDrawable = curGridPos.getBackground( );
				buttonDrawable = DrawableCompat.wrap( buttonDrawable );
				// the color is a direct color int and not a color resource
				DrawableCompat.setTint( buttonDrawable, Color.BLUE );
				curGridPos.setBackground( buttonDrawable );

				curGridPos.setMinimumWidth( squareSize );
				curGridPos.setMaxWidth( squareSize );

				curGridPos.setMinimumHeight( squareSize );
				curGridPos.setMaxHeight( squareSize );

				pieceGrid[height][width] = curGridPos;

				new Handler( Looper.getMainLooper( ) ).post( ( ) -> gridLayout.addView( curGridPos ) );
			}
		}
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