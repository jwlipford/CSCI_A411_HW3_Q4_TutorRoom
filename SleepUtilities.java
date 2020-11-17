
// Heavily modified by Jeffrey

public class SleepUtilities
{
	// Nap between 1 and duration seconds (inclusive)
	public static void nap(int duration)
    {
        int sleeptime = 1 + (int)(duration * Math.random() );
        try { Thread.sleep(1000 * sleeptime); }
        catch (InterruptedException e) {}
        // We should be handling interrupted exceptions but choose not to do so for code clarity.
	}
}
