package support;

import java.text.NumberFormat;
import java.util.Locale;

public final class TimeTool
{
	public static int SECOND_MILLIS = 1000;
	public static int MINUTE_MILLIS = 60000;
	public static int HOUR_MILLIS = 3600000;
	private static String NEG_ARG_EXCEP_TEXT = "The argument 'milliseconds' must have a positive value.";
	private final NumberFormat format;

	public TimeTool()
	{
		format = NumberFormat.getInstance(Locale.US);
		format.setMinimumIntegerDigits(2);
	}

	public long getFullSeconds(long milliseconds)
	{
		if (milliseconds < 0)
		{
			throw new IllegalArgumentException(NEG_ARG_EXCEP_TEXT);
		}
		return milliseconds / SECOND_MILLIS;
	}

	public long getFullMinutes(long milliseconds)
	{
		if (milliseconds < 0)
		{
			throw new IllegalArgumentException(NEG_ARG_EXCEP_TEXT);
		}
		return milliseconds / MINUTE_MILLIS;
	}

	public long getFullHours(long milliseconds)
	{
		if (milliseconds < 0)
		{
			throw new IllegalArgumentException(NEG_ARG_EXCEP_TEXT);
		}
		return milliseconds / HOUR_MILLIS;
	}

	public String getDisplayText(long milliseconds)
	{
		if (milliseconds < 0)
		{
			throw new IllegalArgumentException(NEG_ARG_EXCEP_TEXT);
		}
		StringBuilder res = new StringBuilder();
		long hours = milliseconds / HOUR_MILLIS;
		milliseconds -= HOUR_MILLIS * hours;
		long minutes = milliseconds / MINUTE_MILLIS;
		milliseconds -= MINUTE_MILLIS * minutes;
		long seconds = milliseconds / SECOND_MILLIS;
		res.append(format.format(hours));
		res.append(":");
		res.append(format.format(minutes));
		res.append(":");
		res.append(format.format(seconds));
		return res.toString();
	}
}
