/////////////////////////////////////////////////////////////////////////////////////////////////
//
// Queue class that holds all tasks that need to be done
//
/////////////////////////////////////////////////////////////////////////////////////////////////

import java.io.*;
import java.util.*;

public class TaskQueue {
    private ArrayDeque<Task> queue;
    private int numThreadsWorking;

    public TaskQueue() {
        queue = new ArrayDeque<Task>();
        numThreadsWorking = 0;
    }

    public synchronized void addTask(Task task) {
        System.out.println("addTask()");
        queue.add(task);
        notifyAll();
    }

    public synchronized Task getTask() {
        System.out.println("getTask()");
        while (queue.size() == 0) {
            if (numThreadsWorking == 0) return null;
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
        
        return queue.poll();
    }

    public synchronized void startWorking() {
        numThreadsWorking++;
        notifyAll();
    }

    public synchronized void stopWorking() {
        numThreadsWorking--;
    }

    public synchronized int getNumThreadsWorking() {
        return numThreadsWorking;
    }
}
