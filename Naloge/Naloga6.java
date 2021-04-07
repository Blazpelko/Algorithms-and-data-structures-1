package Seminarska;

import java.util.*;
import java.io.*;

public class Naloga6 {
    Vozlisce []volzisca;
    int stMest,stPovezav,stSkupin,zacetno,koncno;

    public Naloga6(String[]args){
        long start = System.currentTimeMillis();
        // String vhod = args[0];
        String vhod = "Testi sesta naloga/Vhod.txt"; // I5_2.txt
        // String izhod = args[1];
        String izhod = "Izhod.txt";
        readFile(vhod);
        izpisGrafa();
        // writeFile(izhod);
        long end = System.currentTimeMillis();
        System.out.println("Cas izvajanja " + (end - start));
    }

    void izracunajPot(int vozlisce,int dolzina){
        
    }


    private void readFile(String vhod) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(vhod)));
            //Preberemo vhodne parametre
            String str = br.readLine();
            String[] vrstica = str.split(" ");
            stMest=Integer.parseInt(vrstica[0]);
            stPovezav=Integer.parseInt(vrstica[1]);
            stSkupin=Integer.parseInt(vrstica[2]);

            //Ustvarimo novo tabelo vozlisc
            volzisca=new Vozlisce[stMest];
            for (int i = 0; i < volzisca.length; i++) {
                volzisca[i]=new Vozlisce(i);
            }

            //preberemo zacetno in koncno vozlisce
            str = br.readLine();
            vrstica = str.split(" ");
            zacetno=Integer.parseInt(vrstica[0]);
            koncno=Integer.parseInt(vrstica[1]);

            for (int i = 0; i <stPovezav; i++) {
                str = br.readLine();
                vrstica = str.split(" ");
                volzisca[Integer.parseInt(vrstica[0])].povezave.add(new Povezava(Integer.parseInt(vrstica[2]), Integer.parseInt(vrstica[1])));
                volzisca[Integer.parseInt(vrstica[1])].povezave.add(new Povezava(Integer.parseInt(vrstica[2]), Integer.parseInt(vrstica[0])));
            }
            for (int i = 0; i <stSkupin; i++) {
                str = br.readLine();
                vrstica = str.split(" ");
                for (int j = 1; j < Integer.parseInt(vrstica[0])+1; j++) {
                    volzisca[Integer.parseInt(vrstica[j])].skupina=i;
                }
            }

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void izpisGrafa(){
        for (int i = 0; i < volzisca.length; i++) {
            volzisca[i].izpisi();
        }
    }

    public class Vozlisce{
        int id;
        int skupina;
        int najCenejsa;
        ArrayList<Povezava>povezave=new ArrayList<Povezava>();

        Vozlisce(int id){
            this.id=id;
            skupina=-1;
            najCenejsa=1000000;
        }

        void izpisi(){
            System.out.println("id "+(id+1)+" skupina "+(skupina+1));
            for (int i = 0; i < povezave.size(); i++) {
                System.out.println((povezave.get(i).doVozlisca+1)+" "+povezave.get(i).price);
            }
        }
    }

    public class Povezava{
        int price;
        int doVozlisca;

        Povezava(int price,int doVozlisca){
            this.price=price;
            this.doVozlisca=doVozlisca;
        }
    }

    public static void main(String[]args){
        new Naloga6(args); 

    }
}
