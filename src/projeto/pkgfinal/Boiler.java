package projeto.pkgfinal;

/*
    @author
        Gabriel de Moraes Monteiro Zaninotti - RA00227270
        Isabela Canelas Ett - RA00303107
        João Paulo Seryung Kang - RA00231729
        Livia Rodrigues Gonsales - RA00222585
*/

public class Boiler {
    private double ti = 0;  // temperatura água de entrada (C)
    private double no = 0;  // fluxo água de saída (Kg/s)
    private double ta = 0;  // temperatura ar ambiente (C)
    private double t = 0;   // temperatura água interior (C)
    private double h = 0;   // altura coluna água interior (m)

    public void setTi (double ti) {
        this.ti = ti;
    }
    
    public void setNo(double no) {
        this.no = no;
    }

    public void setTa(double ta) {
        this.ta = ta;
    }

    public void setT(double t) {
        this.t = t;
    }

    public void setH(double h) {
        this.h = h;
    }
    
    public void printData () {
        System.out.println("\n-------- Status boiler --------");
        
        System.out.println("Temperatura água de entrada: " + this.ti + " C");
        System.out.println("Temperatura ar ambiente: " + this.ta + " C");
        System.out.println("Temperatura água interior: " + this.t + " C");
        System.out.println("Fluxo água de saída: " + this.no + " Kg/s");
        System.out.println("Altura coluna água interior: " + this.h + " m");
        
        System.out.println("\n-------------------------------");
    }
}
