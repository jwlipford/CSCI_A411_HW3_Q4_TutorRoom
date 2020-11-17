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

public class TutorRoom
{
    public static void main(String args[])
    {
        Dashboard board = new Dashboard();
        String[] names = {"Mary", "Emma","Jennifer","Mike","Alan","Bruce","Tom"};
        Thread[] collegestudents = new Thread[7];
        
        ReentrantLock roomLock = new ReentrantLock(); // Space for only one person (besides TA) in room
        Semaphore chairsSemaphore = new Semaphore(3); // 3 chairs in hallway
        
        Thread ta = new Thread(new TA(board, "Theresa", roomLock, chairsSemaphore));
        for(int i = 0; i < 7; i++)
            collegestudents[i] = new Thread(new Student(board, names[i], roomLock, chairsSemaphore, ta));

        ta.start();
        for (int i = 0; i < 7; i++)
            collegestudents[i].start();
    }
}

