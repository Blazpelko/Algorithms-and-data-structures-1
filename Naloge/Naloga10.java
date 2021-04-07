package Izboljsave;

import java.util.*;

import java.io.*;

public class Naloga10 {
    Vozlisce vozlisca[];
    int indeks;
    int stVozlisc;
    // ArrayList<Vozlisce>vozlisca;
    int stPovezav;
    int zacetek;
    int konec;
    // int[] previousPath;
    // int[] currPath;
    ArrayList<Integer> previousPath;
    ArrayList<Integer> currPath;

    public Naloga10(String[] args) {
        long start = System.currentTimeMillis();
        // String vhod = args[0];
        String vhod = "Testi deseta naloga/I_10.txt"; // I5_2.txt
        // String izhod = args[1];
        String izhod = "Izhod.txt";
        readFile(vhod);
        // izpisGrafa();
        izracunaj(izhod);
        // writeFile(izhod);
        long end = System.currentTimeMillis();
        System.out.println("Cas izvajanja " + (end - start));
    }

    @SuppressWarnings("unchecked")
    void izracunaj(String izhod) {
        previousPath = new ArrayList<Integer>();
        currPath = new ArrayList<Integer>();
        boolean pogoj = true;
        // while (preveriEnakost(pogoj)!=true) {

        // previousPath = (ArrayList<Integer>) currPath.clone();
        // currPath.clear();
        // pogoj=iteracija(izhod);
        // }
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(izhod)));

            while (preveriEnakost(pogoj) != true) {
                previousPath = (ArrayList<Integer>) currPath.clone();
                currPath.clear();
                pogoj = iteracija(izhod);

                Vozlisce temp = vozlisca[zacetek];
                while (temp.uporabljen != false) {
                    bw.write(String.valueOf(temp.id));
                    // Zapomnimo si pot za primerjanje
                    currPath.add(temp.id);
                    temp.uporabljen = false;
                    if (temp.naslednje == null) {
                        break;
                    }
                    bw.write(",");
                    temp = temp.naslednje;
                }
                bw.newLine();
                //System.out.println();

            }
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    boolean iteracija(String izhod) {
        boolean flag = false;
        boolean pogoj = true;
        int indeksTrenutnega = zacetek;
        Vozlisce prejsno;
        Vozlisce trenutno = vozlisca[indeksTrenutnega];
        // Nastavimo zacetno vozlisce na ze uporabljeno
        vozlisca[zacetek].uporabljen = true;

        int minVrednost = Integer.MAX_VALUE;
        int next = Integer.MAX_VALUE;
        boolean prvaIteracija = true;

        while (pogoj) {
            if (prvaIteracija == false) {
                // Nastavimo kazalec na naslednjega
                vozlisca[indeksTrenutnega].naslednje = vozlisca[next];
                vozlisca[next].uporabljen = true;

                boolean premik=false;
                //Preverimo ce se lahko premaknemo iz novega volzisca
                for (Povezava i : vozlisca[next].povezave) {
                    if(vozlisca[i.sosed].uporabljen==false){
                        premik=true;
                    }
                }
                
                
                // Ce je h manjsi od trenutne njboljse ocene jo spremenimo
                if (vozlisca[indeksTrenutnega].HverOznaka < minVrednost) {
                    vozlisca[indeksTrenutnega].HverOznaka = minVrednost;
                    flag = true;
                }

                //Ce ni premikov koncamo
                if(premik==false&&vozlisca[next].povezave.size()>0){
                    break;
                }



                // Premaknemo se v naslednje vozlisce
                indeksTrenutnega = next;

                // nastavimo vrednost nazaj na najvec
                minVrednost = Integer.MAX_VALUE;
                next = Integer.MAX_VALUE;
            }

            

            //Ce smo prisli do zadnjega zakljucimo
            if (indeksTrenutnega == konec) {
                //writeFile(izhod);
                // izpisiInReseitraj();
                break;
            }

            //Preverimo ce smo prisli do vozlisca ki nima izhodnih povezav
            if(vozlisca[indeksTrenutnega].povezave.size()==0){
                vozlisca[indeksTrenutnega].HverOznaka=Integer.MAX_VALUE;
                break;
            }

            for (Povezava i : vozlisca[indeksTrenutnega].povezave) {
                if (vozlisca[i.sosed].uporabljen == false) {
                    // naslednji stavek predstavlja funkcijoV(b)
                    vozlisca[i.sosed].ocena = i.cena + vozlisca[i.sosed].HverOznaka;

                    // Preverimo v katero vozlisce se bomo premaklnili
                    int novaVrednost = vozlisca[i.sosed].ocena;
                    if (novaVrednost < minVrednost) {
                        minVrednost = novaVrednost;
                        next = i.sosed;
                    } else if (novaVrednost == minVrednost) {
                        if (i.sosed < next) {
                            next = i.sosed;
                        }
                    }
                }
            }

            prvaIteracija = false;

        }
        return flag;
    }

    void izpisiInReseitraj() {
        Vozlisce temp = vozlisca[zacetek];
        while (temp.uporabljen != false) {
            System.out.print(temp.id + ",");
            // Zapomnimo si pot
            currPath.add(temp.id);
            temp.uporabljen = false;
            if (temp.naslednje == null) {
                break;
            }
            temp = temp.naslednje;
        }
        System.out.println();

    }

    boolean preveriEnakost(boolean flag) {
        if (flag == true) {
            return false;
        }

        return previousPath.equals(currPath);
        // return true;
    }

    public class Vozlisce {
        int id;
        int ocena;
        int HverOznaka;
        boolean uporabljen;
        Vozlisce naslednje;
        ArrayList<Povezava> povezave = new ArrayList<Povezava>();

        Vozlisce(int id) {
            this.id = id;
            ocena = 0;
            HverOznaka = 0;
            uporabljen = false;
            naslednje = null;
        }

        void izpisi() {
            System.out.print("ID " + id + ":-->");
            for (int i = 0; i < povezave.size(); i++) {
                System.out.print("cena:" + povezave.get(i).cena + ",sosed:" + povezave.get(i).sosed + " ");
            }
        }
    }

    public class Povezava {
        int sosed;
        int cena;

        Povezava(int sosed, int cena) {
            this.sosed = sosed;
            this.cena = cena;
        }
    }

    private void readFile(String vhod) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(vhod)));
            // Preberemo vhodne parametre
            String str = br.readLine();
            String[] vrstica = str.split(",");
            stPovezav = Integer.parseInt(vrstica[0]);

            vozlisca = new Vozlisce[(stPovezav * 2) + 1];
            for (int i = 0; i < vozlisca.length; i++) {
                vozlisca[i] = new Vozlisce(i);
            }

            for (int i = 0; i < stPovezav; i++) {
                str = br.readLine();
                vrstica = str.split(",");
                Povezava p = new Povezava(Integer.parseInt(vrstica[1]), Integer.parseInt(vrstica[2]));
                stVozlisc = Math.max(stVozlisc, Math.max(Integer.parseInt(vrstica[1]), Integer.parseInt(vrstica[2])));
                vozlisca[Integer.parseInt(vrstica[0])].povezave.add(p);
            }

            str = br.readLine();
            vrstica = str.split(",");
            zacetek = Integer.parseInt(vrstica[0]);
            konec = Integer.parseInt(vrstica[1]);

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void izpisGrafa() {
        for (int i = 0; i < vozlisca.length; i++) {
            if (vozlisca[i].povezave.size() == 0) {
                continue;
            }
            vozlisca[i].izpisi();
            System.out.println();
        }
    }

    private void writeFile(String izhod) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(izhod)));

            Vozlisce temp = vozlisca[zacetek];
            while (temp.uporabljen != false) {
                System.out.print(temp.id + ",");
                bw.write(temp.id);
                // Zapomnimo si pot za primerjanje
                currPath.add(temp.id);
                temp.uporabljen = false;
                if (temp.naslednje == null) {
                    break;
                }
                bw.write(",");
                temp = temp.naslednje;
            }
            bw.newLine();
            System.out.println();

            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Naloga10(args);

    }
}
