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
        queue.add(task);
        notifyAll();
    }

    public synchronized Task getTask() {
        while (queue.size() == 0) {
            if (numThreadsWorking == 0) {
                notifyAll();
                return null;
            }
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
        if (queue.size() == 0) return null;

        return queue.poll();
    }

    public synchronized void startWorking() {
        numThreadsWorking++;
    }

    public synchronized void stopWorking() {
        numThreadsWorking--;
    }

    public synchronized int getNumThreadsWorking() {
        return numThreadsWorking;
    }
}
