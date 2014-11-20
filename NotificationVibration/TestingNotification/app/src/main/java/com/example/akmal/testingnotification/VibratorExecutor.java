package com.example.akmal.testingnotification;

/**
 * Created by Akmal on 20.11.2014.
 */

import android.os.Vibrator;
import android.content.Context;
import android.util.Log;

public class VibratorExecutor
{
    public static long[] pattern = {0, 500, 300, 300, 200,
            500, 300, 300, 200,
            500, 300, 300, 200,
            500, 300, 300, 200,
            500, 300, 300, 200};

    private Vibrator vibrator;
    private boolean turnedOff = true;

    public VibratorExecutor(Context context)
    {
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void activateVibrator()
    {
        if (turnedOff)
        {
            vibrate();
            turnedOff = false;
        }
        else
        {
            cancel();
            turnedOff = true;
        }
    }

    private void vibrate()
    {
        if (vibrator.hasVibrator())
        {
            vibrator.vibrate(pattern, 0);
            Log.v("Can Vibrate", "YES");
        }
        else
        {
            Log.v("Can Vibrate", "NO");
        }
    }

    private void cancel()
    {
        vibrator.cancel();
    }
}
