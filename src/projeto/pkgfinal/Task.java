package projeto.pkgfinal;

// @author Isabela Canelas Ett - RA00303107

public class Task {
    long startTime;
    long finishTime;
    long duration;
    String name = "Task";

    public long getDuration() {
        return this.duration;
    }

    public String getName() {
        return this.name;
    }
    
    protected void setStartTime() {
        this.startTime = System.currentTimeMillis();
    }
    
    protected void setFinishTime() {
        this.finishTime = System.currentTimeMillis();
    }
    
    protected void setDuration() {
        this.duration = this.finishTime - this.startTime;
    }

    public void run() {
        this.setStartTime();
       
        System.out.println("running task");
        
        this.setFinishTime();
        this.setDuration();
    }
}