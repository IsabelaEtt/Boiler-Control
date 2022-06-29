package projeto.pkgfinal;

// @author Isabela Canelas Ett - RA00303107

import java.util.ArrayList;
import java.util.List;

public class CyclicExecutive {
    private final long minorCycleTime = 30;
    private long timeUsedInMinorCycle = 0;
    private List<List<Task>> minorCycles = new ArrayList<>();
    private boolean running = true;
    
    public CyclicExecutive () {}
    
    public void run () {
        this.running = true;
        
        while (running) {
            for (List<Task> minorCycleTasks: minorCycles) {
                this.waitNextMinorCycle();
                
                for (Task task: minorCycleTasks) {
                    task.run();

                    this.timeUsedInMinorCycle += task.getDuration();
                }
            }
        }
    }
    
    synchronized private void waitNextMinorCycle () {
        System.out.println("\nUsed " + this.timeUsedInMinorCycle + " ms, minor cycle should have " + this.minorCycleTime + " ms");
        
        long timeToWait = this.minorCycleTime - this.timeUsedInMinorCycle;
        this.timeUsedInMinorCycle = 0;
        
        if (timeToWait <= 0) {
            this.timeUsedInMinorCycle = timeToWait * -1;
            System.out.println("Doesn't need to wait... Overrun..." + this.timeUsedInMinorCycle);
            return;
        }
        
        System.out.println("Will wait " + timeToWait + " ms...");
        
        try { new Thread().sleep(timeToWait);
        } catch(InterruptedException e) {
            System.out.println("Something went wrong while waiting next minor cycle..." + e);
        }
    }
    
    public void addMinorCycle (List<Task> newMinorCycle) {
        this.minorCycles.add(newMinorCycle);
    }
    
    public void stopCycle () {
        this.running = false;
    }
    
    public boolean hasTasks () {
        return this.minorCycles.size() > 0;
    }
    
    public void removeAllTasks() {
        this.minorCycles = new ArrayList<>();
    }
}