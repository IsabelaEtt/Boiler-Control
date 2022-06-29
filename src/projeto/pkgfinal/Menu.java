package projeto.pkgfinal;

/*
    @author
        Gabriel de Moraes Monteiro Zaninotti - RA00227270
        Isabela Canelas Ett - RA00303107
        João Paulo Seryung Kang - RA00231729
        Livia Rodrigues Gonsales - RA00222585
*/

import java.io.IOException;
import static java.lang.Runtime.getRuntime;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Menu {
    boolean runningMenu = true;
    Scanner scanner = new Scanner(System.in);
    CyclicExecutive ce = new CyclicExecutive();
    
    public void showMenu () {
        System.out.println("Bem vindo! Selecione o que deseja fazer com o executivo cíclico de tempo variável:");
        System.out.println("1 - Rodar");
        System.out.println("2 - Pausar");
        System.out.println("3 - Retomar");
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
    
    public void runBoiler () {
        try { Runtime.getRuntime().exec(" java -jar str-caldeira-v2.1.jar");
        } catch(IOException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        this.showMenu();
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
   
            DatagramSocket clientSocket = null;
            try { clientSocket = new DatagramSocket();
            } catch(SocketException ex) { Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex); }
            
            InetAddress IPAddress = null;
            try { IPAddress = InetAddress.getByName("localhost");
            } catch(UnknownHostException ex) { Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex); }

            byte[] sendData = new byte[1024];
            byte[] receiveData = new byte[1024];

            String sentence = "st-0";
            sendData = sentence.getBytes();
            
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 4545);
            try { clientSocket.send(sendPacket);
            } catch(IOException ex) { Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex); }

            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            try { clientSocket.receive(receivePacket);
            } catch (IOException ex) { Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex); }
            String modifiedSentence = new String(receivePacket.getData());
            
            String value = modifiedSentence.split("-")[1];
            System.out.println("Temperature: " + value);

            clientSocket.close();
            
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

