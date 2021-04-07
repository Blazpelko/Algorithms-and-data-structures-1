package Seminarska;

import java.io.*;
import java.util.*;

public class Naloga8 {
    int stVozliscVzorec;
    int stVozliscDrevo;
    Drevo vzorec;
    Drevo drevo;
    ArrayList<Vozlisce> zacetnaVozlisca;

    public Naloga8(String[] args) {
        long start = System.currentTimeMillis();
        // String vhod = args[0];
        String vhod = "Testi osma naloga/I8_10.txt";
        // String izhod = args[1];
        String izhod = "Izhod.txt";
        readFile(vhod);
        int st=run();
        writeFile(izhod,st);
        long end = System.currentTimeMillis();
        System.out.println("Cas izvajanja " + (end - start));
    }

    int run() {
        int count = 0;

        for (Vozlisce vozlisce : zacetnaVozlisca) {
            if (preveriEnakost(vozlisce, vzorec.koren)) {
                count++;
            }
        }
        return count;
    }

    boolean preveriEnakost(Vozlisce temp, Vozlisce primerjava) {
        // if (primerjava == null) {
        //     System.out.println("koec");
        //     return true;
        // }

        if(primerjava.stSinov>0){
            if(primerjava.stSinov!=temp.stSinov){
                return false;
            }
        }

        Vozlisce p = primerjava.sin;
        Vozlisce v = temp.sin;
        boolean pogoj = true;
        while (p != null&&pogoj==true) {
            if (v == null) {
                return false;
            }

            // Preverimo ce se razlikujeta
            if (p.id != v.id) {
                return false;
            }
            pogoj = preveriEnakost(v, p);
            p = p.brat;
            v = v.brat;
            //  if(p==null && v!=null){
            //      return false;
            //  }
        }
        return pogoj;

    }

    class Vozlisce {
        char id;
        Vozlisce oce;
        Vozlisce brat;
        Vozlisce sin;
        int stSinov;

        Vozlisce(char id, Vozlisce oce) {
            this.id = id;
            this.oce = oce;
            stSinov=0;
        }

        public void izpisiSinove() {
            if (this != null) {
                Vozlisce sin = this.sin;

                while (sin != null) {
                    System.out.print(sin.id + ", ");
                    sin = sin.brat;
                }

                System.out.println();
            }
        }
    }

    public class Drevo {
        Vozlisce koren;

        Drevo(char id) {
            koren = new Vozlisce(id, null);
        }

        public Vozlisce dodajVozlisce(Vozlisce oce, char sin) {

            Vozlisce s = new Vozlisce(sin, oce);
            s.brat = oce.sin;
            oce.sin = s;
            oce.stSinov++;

            return s;

        }

        private void izpis(int zamik, Vozlisce v) {
            for (int i = 0; i < zamik; i++)
                System.out.print(" |");

            System.out.println(v.id);
            Vozlisce sin = v.sin;

            while (sin != null) {
                izpis(zamik + 1, sin);
                sin = sin.brat;
            }
        }
    }

    private void readFile(String vhod) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(vhod)));
            // Preberemo vhodne parametre
            String str = br.readLine();
            String[] vrstica = str.split(" ");
            stVozliscVzorec = Integer.parseInt(vrstica[0]);

            zacetnaVozlisca = new ArrayList<Vozlisce>();

            // Kreiranje vzorca
            str = br.readLine();
            vrstica = str.split(",");
            vzorec = new Drevo(vrstica[1].charAt(0));

            Vozlisce t[] = new Vozlisce[stVozliscVzorec + 1];

            for (int i = 2; i < vrstica.length; i++) {
                t[Integer.parseInt(vrstica[i])] = vzorec.koren;
            }
            // Preberemo vozlisca in jih shranimo v graf
            for (int i = 0; i < stVozliscVzorec - 1; i++) {
                str = br.readLine();
                vrstica = str.split(",");
                if (vrstica.length <= 2) {
                    vzorec.dodajVozlisce(t[Integer.parseInt(vrstica[0])], vrstica[1].charAt(0));
                } else {
                    Vozlisce v = vzorec.dodajVozlisce(t[Integer.parseInt(vrstica[0])], vrstica[1].charAt(0));
                    for (int j = 2; j < vrstica.length; j++) {
                        t[Integer.parseInt(vrstica[j])] = v;
                    }
                }
            }
            //vzorec.izpis(0, vzorec.koren);

            str = br.readLine();
            vrstica = str.split(",");
            stVozliscDrevo = Integer.parseInt(vrstica[0]);

            // Kreiranje Drevesa
            str = br.readLine();
            vrstica = str.split(",");
            drevo = new Drevo(vrstica[1].charAt(0));
            t = new Vozlisce[stVozliscDrevo + 1];

            char temp = vzorec.koren.id;
            if (vrstica[1].charAt(0) == temp) {
                zacetnaVozlisca.add(drevo.koren);
            }

            for (int i = 2; i < vrstica.length; i++) {
                t[Integer.parseInt(vrstica[i])] = drevo.koren;
            }

            // Preberemo vozlisca in jih shranimo v graf
            for (int i = 0; i < stVozliscDrevo - 1; i++) {
                str = br.readLine();
                vrstica = str.split(",");
                Vozlisce v;
                if (vrstica.length <= 2) {
                    v = drevo.dodajVozlisce(t[Integer.parseInt(vrstica[0])], vrstica[1].charAt(0));
                } else {
                    v = drevo.dodajVozlisce(t[Integer.parseInt(vrstica[0])], vrstica[1].charAt(0));
                    for (int j = 2; j < vrstica.length; j++) {
                        t[Integer.parseInt(vrstica[j])] = v;
                    }
                }
                if (vrstica[1].charAt(0) == temp) {
                    zacetnaVozlisca.add(v);
                }
            }
            //System.out.println("---------------");
            //drevo.izpis(0, drevo.koren);

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeFile(String izhod,int st) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(izhod)));
        
                bw.write(String.valueOf(st));
                bw.newLine();

            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Naloga8(args);
    }

}
