package projeto.pkgfinal;

// @author Isabela Canelas Ett - RA00303107

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu {
    boolean runningMenu = true;
    Scanner scanner = new Scanner(System.in);
    CyclicExecutive ce = new CyclicExecutive();
    
    public void showMenu () {
        System.out.println("Bem vindo! Selecione o que deseja fazer com o executivo cíclico de tempo variável:");
        System.out.println("1 - Rodar");
        System.out.println("2 - Pausar");
        System.out.println("3 - Reotmar");
        System.out.println("4 - Parar");
        System.out.println("5 - Sair");
        
        while (runningMenu) {
            int userInput = scanner.nextInt();
            switch (userInput) {
                case 1: 
                    this.createTasks();
                    this.runTasks();
                    continue;
                case 2: 
                    ce.stopCycle();
                    this.showMenu();
                    break;
                case 3:
                    this.runTasks();
                    continue;
                case 4: 
                    ce.removeAllTasks();
                    ce.stopCycle();
                    this.showMenu();
                    break;
                case 5:
                    ce.removeAllTasks();
                    ce.stopCycle();
                    this.runningMenu = false;
                    break;
                default:
                    System.out.println("Insira uma opção válida");
            }
        }
    }
    
    private void createTasks () {
        Task task1 = new CheckBoilerStatusTask();
        Task task2 = new PrintBoilerStatusTask();
        
        List<Task> minorCycle1 = new ArrayList<>();
        minorCycle1.add(task1);
        minorCycle1.add(task2);
        
        List<Task> minorCycle2 = new ArrayList<>();
        minorCycle2.add(task1);
        
        List<Task> minorCycle3 = new ArrayList<>();
        minorCycle3.add(task1);
        
        ce.addMinorCycle(minorCycle1);
        ce.addMinorCycle(minorCycle2);
        ce.addMinorCycle(minorCycle3);
    }
    
    private void runTasks () {
       if (!ce.hasTasks()) {
            System.out.println("É necessário rodar antes de retomar");
            return;
        }
        
        new Thread(new VariableRunnable()).start();
    }
    
    private class VariableRunnable implements Runnable {
        public void run() {
            ce.run();
        }
    }
    
    private class CheckBoilerStatusTask extends Task {
        CheckBoilerStatusTask() {
            this.name = "CheckBoilerStatus";
        }

        @Override
        public void run() {
            super.setStartTime();
       
            System.out.println(this.name);

            super.setFinishTime();
            super.setDuration();
        }
    }
    
    private class PrintBoilerStatusTask extends Task {
        PrintBoilerStatusTask() {
            this.name = "PrintBoilerStatus";
        }

        @Override
        public void run() {
            super.setStartTime();
       
            System.out.println(this.name);

            super.setFinishTime();
            super.setDuration();
        }
    }
}

