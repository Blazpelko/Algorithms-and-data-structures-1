package Izboljsave;

import java.io.*;

//spremeni ime MEMO
public class Naloga2 {
    private int visina;
    private int sirina;
    private char[][] polje;
    private Vozlisce najVozlisce;
    private boolean[][] prehojenaPot;

    public Naloga2(String[] args) {
        long start = System.currentTimeMillis();
        // String vhod = args[0];
        String vhod = "Testi druga naloga/Vhod10.txt";
        // String izhod = args[1];
        String izhod = "Izhod.txt";
        readFile(vhod);
        sprehod();
        writeFile(izhod);
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

    class Vozlisce {
        int i;
        int j;
        int dolzina;
        char[] pot;

        public Vozlisce(int i, int j, int dolzina, char[] pot) {
            this.i = i;
            this.j = j;
            this.dolzina = dolzina;
            this.pot = pot;
        }

        void NajkrajsaPot(int i, int j, char znak, int dolzina, char[] temp) {
            if (i < 0 || j < 0) {
                this.preveriPot(temp, dolzina);
                return;
            }

            if (i >= visina || j >= sirina) {
                this.preveriPot(temp, dolzina);
                return;
            }
            //if (polje[i][j].equals(znak) == false) {
            if (polje[i][j]!=znak) {
                this.preveriPot(temp, dolzina);
                return;
            }
            if (prehojenaPot[i][j] == true) {
                this.preveriPot(temp, dolzina);
                return;
            }

            prehojenaPot[i][j] = true;
            temp[dolzina] = 'g';
            NajkrajsaPot(i - 1, j, znak, dolzina + 1, temp);

            temp[dolzina] = 'd';
            NajkrajsaPot(i + 1, j, znak, dolzina + 1, temp);

            temp[dolzina] = 'l';
            NajkrajsaPot(i, j - 1, znak, dolzina + 1, temp);

            temp[dolzina] = 'D';
            NajkrajsaPot(i, j + 1, znak, dolzina + 1, temp);

            prehojenaPot[i][j] = false;
            temp[dolzina] = ' ';
        }
        
        void preveriPot(char[] temp, int dolzina) {
            if (dolzina > this.dolzina) {
                this.dolzina = dolzina;
                char t[] = kopirajTabelo(temp, dolzina);
                this.pot = t;
            }
        }
    }

    char[] kopirajTabelo(char[] temp, int d) {
        char[] nova = new char[d];
        for (int i = 0; i < d; i++) {
            nova[i] = temp[i];
        }
        return nova;
    }

    void sprehod() {
        Vozlisce a = new Vozlisce(0, 0, 0, null);
        najVozlisce = a;
        prehojenaPot = new boolean[visina][sirina];
        for (int i = 0; i < polje.length; i++) {
            for (int j = 0; j < polje[i].length; j++) {
                char[] temp = new char[visina * sirina];
                // System.out.print(polje[i][j]+" ");
                //   if(stSosedov(polje[i][j], i, j)!=1){
                //       continue;
                //   }
                Vozlisce b = new Vozlisce(i, j, 0, null);
                b.NajkrajsaPot(i, j, polje[i][j], 0, temp);
                if (b.dolzina > najVozlisce.dolzina) {
                    najVozlisce = b;
                   // print(najVozlisce.pot);
                }
            }
        }
    }

    int stSosedov(char znak,int x,int y){
        int stSosedov=0;
        if(y+1<polje[x].length){
            if(polje[x][y+1]==znak){
                stSosedov++;
            }
        }
        if(y-1>=0){
            if(polje[x][y-1]==znak){
                stSosedov++;
            }
        }
        if(x+1<polje.length){
            if(polje[x+1][y]==znak){
                stSosedov++;
            }
        }
        if(x-1>=0){
            if(polje[x-1][y]==znak){
                stSosedov++;
            }
        }
        return stSosedov;
    }

    void print(String[] t) {
        for (int i = 0; i < t.length; i++) {
            System.out.print(t[i] + " ");
        }
        System.out.println();
    }

    static void PrintTab(char[][] t) {
        for (int i = 0; i < t.length; i++) {
            for (int j = 0; j < t.length; j++) {
                System.out.print(t[i][j]);
            }
            System.out.println();
        }
    }

    private void readFile(String vhod) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(vhod)));
            String str = br.readLine();
            String temp[] = str.split(",");
            visina = Integer.parseInt(temp[0]);
            sirina = Integer.parseInt(temp[1]);
            polje = new char[visina][sirina];
            int indeks = 0;
            while ((str = br.readLine()) != null) {
                //String vrstica[] = str.split(",");
                for (int j = 0; j < sirina; j++) {
                    polje[indeks][j] = str.charAt(j*2);
                }
                indeks++;
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeFile(String izhod) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(izhod)));
            bw.write(String.valueOf(najVozlisce.i) + "," + String.valueOf(najVozlisce.j));
            bw.newLine();
            int pogoj = 1;
            for (int i = 0; i < najVozlisce.dolzina - 1; i++) {
                if (pogoj == 0) {
                    bw.write(",");
                }
                if(najVozlisce.pot[i]=='g'){
                    bw.write("GOR");
                }else if(najVozlisce.pot[i]=='d'){
                    bw.write("DOL");
                }else if(najVozlisce.pot[i]=='l'){
                    bw.write("LEVO");
                }else if(najVozlisce.pot[i]=='D'){
                    bw.write("DESNO");
                }
                //bw.write(najVozlisce.pot[i]);
                pogoj = 0;
            }
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Naloga2(args);
    }

}
