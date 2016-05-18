public class Timer {

    long currentTime, lastTime, nextTime, frameTime, prefCountFreq;
    double timeElapsed, timeScale, fps;

    public Timer(double fps)
    {
        timeElapsed = 0;
        lastTime = 0;
        prefCountFreq = 0;
        this.fps = fps;
    }

    public void start()
    {

    }

    public boolean readyForNextFrame()
    {
        return true;
    }

    double getTimeElapsed()
    {
        return timeElapsed;
    }

    double timeElapsed()
    {
        return 1;
    }
}
