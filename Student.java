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

public class Student implements Runnable
{
	private Dashboard board;
	private String name;
    
    private ReentrantLock roomLock;
    private Semaphore chairsSemaphore;
    private Thread ta;

	public Student(Dashboard board, String name, ReentrantLock roomLock, Semaphore chairsSemaphore, Thread ta)
    {
		this.board = board;
        this.name = name;
        this.roomLock = roomLock;
		this.chairsSemaphore = chairsSemaphore;
        this.ta = ta;
	}

	public void run()
    {
		final int PROGRAMMING_TIME = 25;
        // Maximum amount of time a student spends programming before needing help
        
        final int HELP_TIME = 3;
        // Maximum amount of time a student needs help
        
        while (true)
        {
			SleepUtilities.nap(PROGRAMMING_TIME); // Program for a while
            
			board.postMessage(name + " needs help"); // Student needs help
            
            boolean inRoom = roomLock.tryLock(); // Enter room if possible right now
            if(inRoom)
            {
                board.enterRoom(name);
                ta.interrupt();
                SleepUtilities.nap(HELP_TIME); // Get help from the TA for a while
                board.leaveRoom(name);
                roomLock.unlock();
                ta.interrupt();
            }
            else
            {
                boolean inAChair = chairsSemaphore.tryAcquire(); // Sit in a chair if possible right now
                if(!inAChair)
                {
                    // Restart loop. Program some more and come back later.
                    board.postMessage(name + " needs help but will return later");
                    continue;
                }
                else
                {
                    board.waitHallway(name);
                    roomLock.lock(); // Wait until able to enter room, then do so
                    
                    chairsSemaphore.release();
                    board.leaveHallway(name);
                    board.enterRoom(name);
                    ta.interrupt();
                    SleepUtilities.nap(HELP_TIME);
                    board.leaveRoom(name);
                    roomLock.unlock();
                    ta.interrupt();
                }
            }
		}
	}
}
