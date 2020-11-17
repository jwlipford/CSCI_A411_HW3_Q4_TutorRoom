/*
 A university computer science department has a teaching assistant (TA) who
 helps undergraduate students with their programming assignments during
 regular office hours. The TA's office is rather small and has room for only
 one desk with a chair and computer. There are three chairs in the hallway
 outside the office where students can sit and wait if the TA is currently
 helping another student. When there are no students who need help during
 office hours, the TA sits at the desk and takes a nap. If a student arrives
 during office hours and finds the TA sleeping, the student must awaken the TA
 to ask for help. If a student arrives and finds the TA currently helping
 another student, the student sits on one of the chairs in the hallway and
 waits. If no chairs are available, the student will come back at a later time.
 When the TA finishes helping a student, the TA must check to see if there are
 students waiting for help in the hallway. If so, the TA must help each of
 these students in turn. If no students are present, the TA may return to
 napping.
 
 https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/locks/Lock.html
 https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/Semaphore.html
*/

import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TA implements Runnable
{
	private Dashboard board;
	private String name;
    
    private ReentrantLock roomLock;
    private Semaphore chairsSemaphore;

	public TA(Dashboard board, String name, ReentrantLock roomLock, Semaphore chairsSemaphore)
    {
		this.board = board;
        this.name = name;
        this.roomLock = roomLock;
		this.chairsSemaphore = chairsSemaphore;
	}

	public void run()
    {
        while (true)
        {
			// I am not sure if this is the best way to do things.
            // I would like to use wait() instead of Thread.sleep(9999), where 9999 is an arbitrary
            // value, but am not sure how to do that.
            // Also, I think that the thread will often lock the room, unlock it, go back to sleep,
            // and then immediately be interrupted back awake again. I would like to make this less
            // likely.
            
            board.officeMessage("TA "+name +" is sleeping");
            try{ Thread.sleep(9999); } // Nap until awakened
            catch(InterruptedException ie){};
            board.officeMessage("TA " + name + " is working with students");
            while(!roomLock.tryLock())
            {
                try{ Thread.sleep(9999); }
                catch(InterruptedException ie){};
            }
            roomLock.unlock();
		}
	}
}
