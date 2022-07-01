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
    Boiler boiler = new Boiler();
    DatagramSocket clientSocket = null;
    InetAddress IPAddress = null;
    
    public void showMenu () {
        System.out.println("Bem vindo! Selecione o que deseja fazer com o executivo cíclico de tempo variável:");
        System.out.println("1 - Rodar");
        System.out.println("2 - Pausar");
        System.out.println("3 - Retomar");
        System.out.println("4 - Sair");
        
        while (runningMenu) {
            int userInput = scanner.nextInt();
            switch (userInput) {
                case 1:
                    this.runBoiler();
                    this.createTasks();
                    this.openConnectionWithBoiler();
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
                    this.closeConnectionWithBoiler();
                    this.runningMenu = false;
                    break;
                    
                default:
                    System.out.println("Insira uma opção válida");
            }
        }
    }
    
    private void createTasks () {
        if (ce.hasTasks()) { return; } //Was already initiated
        
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
    
    private void runBoiler () {
        if (ce.hasTasks()) { return; } //Was already initiated
        
        try { Runtime.getRuntime().exec(" java -jar str-caldeira-v2.1.jar");
        } catch(IOException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try { new Thread().sleep(1000); // Wait boiler open
        } catch(InterruptedException ex) { Logger.getLogger(CyclicExecutive.class.getName()).log(Level.SEVERE, null, ex);}
    }
    
    private void openConnectionWithBoiler () {
        try { clientSocket = new DatagramSocket();
        } catch(SocketException ex) { Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex); }

        try { IPAddress = InetAddress.getByName("localhost");
        } catch(UnknownHostException ex) { Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex); }
    }
    
    private void closeConnectionWithBoiler () {
        this.clientSocket.close();
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
   
            double ti = socketConnection("sti");
            double no = socketConnection("sno");
            double ta = socketConnection("sta");
            double t = socketConnection("st-");
            double h = socketConnection("sh-");
            
            boiler.setTi(ti);
            boiler.setNo(no);
            boiler.setTa(ta);
            boiler.setT(t);
            boiler.setH(h);

            super.setFinishTime();
            super.setDuration();
        }
        
        private double socketConnection (String varName) {
            String sentence = varName + "0";
            
            byte[] sendData = new byte[1024];
            byte[] receiveData = new byte[1024];

            sendData = sentence.getBytes();
            
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 4545);
            try { clientSocket.send(sendPacket);
            } catch(IOException ex) { Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex); }

            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            try { clientSocket.receive(receivePacket);
            } catch (IOException ex) { Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex); }
            String modifiedSentence = new String(receivePacket.getData());
            
            String value = modifiedSentence.split(varName)[1];
            return Double.parseDouble(value);
        }
    }
    
    private class PrintBoilerStatusTask extends Task {
        PrintBoilerStatusTask() {
            this.name = "PrintBoilerStatus";
        }

        @Override
        public void run() {
            super.setStartTime();
       
            boiler.printData();

            super.setFinishTime();
            super.setDuration();
        }
    }
}

