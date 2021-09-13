package com.example.connectfour;

public class TimerUtil {

	double startTime = -1;
	int decimals; // min of 0, max of like 3

	/**
	 * create a new timer to track time after a certain point
	 * -restartable
	 *
	 */
	public TimerUtil( ) {
		this( 2 );
	}

	/**
	 * create a new timer to track time after a certain point
	 * -restartable
	 *
	 * @param decimals the number of decimals of milliseconds to display
	 */
	public TimerUtil( int decimals ) {
		this.decimals = decimals;
	}

	public void start( ) {

		startTime = System.currentTimeMillis( ) / 1000d;
	}

	public double getDoubleTime( ) {

		return System.currentTimeMillis( ) / 1000d - startTime;
	}

	public String getTime( boolean includeMilliZeros ) {

		boolean includeHour = false, includeMin = true;
		int timeSec, timeMin, timeHour = 0;
		double time = getDoubleTime( );

		timeSec = (int) time;
		timeMin = timeSec / 60;
		timeSec %= 60;
		if( includeHour ) {
			timeHour = timeMin / 60;
			timeMin %= 60;
		}

		String timeSMH = "", decimal = ("" + (time - (int) time)).substring( 1 ); // ".3279998302459717", or ".0" or "0.333", etc...
		if( includeHour && timeHour > 0 )
			timeSMH += timeHour + ":"; // hours
		if( includeMin && timeMin > 0 )
			timeSMH += getZeroPrefix( timeMin, timeHour ) + timeMin + ":"; // minutes
		timeSMH += getZeroPrefix( timeSec, timeMin ) + timeSec; // seconds

		if( includeMilliZeros ) {

			int pow = (int) Math.pow( 10, decimals - decimal.length( ) + 1 );
			String extras = ("" + pow).substring( 1 );

			return timeSMH + (decimal + extras).substring( 0, decimals + 1 );
		}

		return timeSMH + (decimal).substring( 0, (decimal.length( ) > decimals ? decimals + 1 : decimal.length( )) );
	}

	/**
	 *
	 * @param currentTime the time to determine if it needs a zero prefix
	 * @param previousTime the time one step larger (if current time is in seconds, previousTime is in minutes)
	 * @return
	 */
	public static String getZeroPrefix( int currentTime, int previousTime ) {
		return Integer.toString( currentTime ).length( ) == 1 && previousTime > 0 ? "0" : "";
	}

	public boolean started( ) {
		return startTime != -1d;
	}
}
