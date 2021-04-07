package Izboljsave;

import java.util.*;

import java.io.*;

public class Naloga7 {
    Krizisce krizisca[];
    int stKrizisc;
    int stPovezav;
    int zacetek, konec;
    ArrayList<Povezava> pot;
    //Daj raje hash map zaradi duplikatov
    HashSet<Povezava> mostovi;

    public Naloga7(String[] args) {
        long start = System.currentTimeMillis();
        // String vhod = args[0];
        String vhod = "Testi sedma naloga/I7_6.txt"; 
        // String izhod = args[1];
        String izhod = "Izhod.txt";
        readFile(vhod);
        breadthFirstSearch();
        najdiMostove();
        primerjaj();
        writeFile(izhod);
        long end = System.currentTimeMillis();
        System.out.println("Cas izvajanja " + (end - start));
    }

    void breadthFirstSearch() {
        boolean obiskani[] = new boolean[stKrizisc];
        Integer cenePoti[] = new Integer[stKrizisc];

        // Sortiramo vrsto po najnizji ceni in tabele cen
        PriorityQueue<Integer> vrsta = new PriorityQueue<Integer>(new Comparator<Integer>() {
            public int compare(Integer prvi, Integer drugi) {
                return cenePoti[prvi].compareTo(cenePoti[drugi]);
            }
        });
        int trenutno;

        obiskani[zacetek] = true;
        cenePoti[zacetek] = 0;
        vrsta.add(zacetek);
        // najPot=new ArrayList<>();

        pot = new ArrayList<Povezava>();

        // Uporabimo algoritem za iskanje poti ko so vse povezave enako dolge, ki smo ga
        // obravnavali na predavanjih
        while (!vrsta.isEmpty()) {
            // Vzamemo prvi element iz vrste in ga odstranimo
            trenutno = vrsta.poll();
            for (int i : krizisca[trenutno].povezave) {
                if (!obiskani[i]) {
                    obiskani[i] = true;
                    cenePoti[i] = cenePoti[trenutno] + 1;
                    vrsta.add(i);
                    // Dodamo soseda povezemo z trenutnim elementom
                    krizisca[i].parent = krizisca[trenutno];
                }
            }
        }

        Krizisce temp = krizisca[konec];
        int prejsni = temp.id;
        temp = temp.parent;
        while (temp != null) {
            Povezava a = new Povezava(Math.min(prejsni, temp.id), Math.max(prejsni, temp.id));
            pot.add(a);
            prejsni = temp.id;
            temp = temp.parent;
        }

        /*
         * System.out.print("[ "); for (Povezava i : pot) { System.out.print(i.from +
         * "-" + i.to + " "); } System.out.println("]");
         */
        // System.out.println("Naj pot "+pot);
    }

    void primerjaj() {
        //Sprehodimo se cez vse na poti in preverimo ce je most
        Iterator<Povezava> i = pot.iterator();
        while (i.hasNext()) {
            Povezava s = i.next();
            if (!mostovi.contains(s)) {
                i.remove();
            }
        }
        Collections.sort(pot);
        for (Povezava j : pot) {
            System.out.println(j.from + " " + j.to);
        }
    }

    boolean[] obiskani;
    int[] vhodniCas;
    // Najnizji cas je minimum od vhodni cas
    int[] najCas;
    int cas;

    // Algoritem za iskanje mostov v neusmerjenem grafu
    void najdiMostove() {
        cas = 0;
        obiskani = new boolean[stKrizisc];
        vhodniCas = new int[stKrizisc];
        najCas = new int[stKrizisc];
        // mostovi=new ArrayList<Povezava>();
        mostovi = new HashSet<Povezava>();

        for (int i = 0; i < krizisca.length; i++) {
            vhodniCas[i] = -1;
            najCas[i] = -1;
        }
        
        for (int i = 0; i < obiskani.length; i++) {
            if (!obiskani[i]) {
                depthFirstSearch(i, -1);
            }
        }
        /*
         * Iterator<Povezava> it = mostovi.iterator(); while(it.hasNext()){ Povezava
         * a=it.next(); System.out.println(a.from+" "+a.to); }
         */
    }

    void depthFirstSearch(int indeks, int p) {
        obiskani[indeks] = true;
        vhodniCas[indeks] = cas;
        najCas[indeks] = cas;
        cas++;

        for (int i : krizisca[indeks].povezave) {
            // Ce je i vzolisce iz katerega smo prisli v vozlisce indeks nadaljuj
            if (i == p) {
                continue;
            }
            if (obiskani[i] == true) {
                // Ce smo ze bili v sosednem vozliscu smo nasli pot nazaj do enega od
                // predhodnikov volzisca indeks zato povezava indeks-i ni most
                najCas[indeks] = Math.min(najCas[indeks], vhodniCas[i]);
            } else {
                // Ce izpoljnjujemo vse pogoje naprej gradimo dfs drevo
                depthFirstSearch(i, indeks);
                najCas[indeks] = Math.min(najCas[indeks], najCas[i]);
                if (najCas[i] > vhodniCas[indeks]) {
                    mostovi.add(new Povezava(Math.min(indeks, i), Math.max(indeks, i)));
                    // System.out.println(Math.min(indeks, i) + " " + Math.max(indeks, i) + " je
                    // most");
                }
            }
        }
    }

    void izpisiTabelo(Integer t[]) {
        for (int i = 0; i < t.length; i++) {
            System.out.print(t[i] + " ");
        }
        System.out.println();
    }

    private void readFile(String vhod) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(vhod)));
            // Preberemo vhodne parametre
            String str = br.readLine();
            String[] vrstica = str.split(" ");
            stKrizisc = Integer.parseInt(vrstica[0]);
            stPovezav = Integer.parseInt(vrstica[1]);

            str = br.readLine();
            vrstica = str.split(" ");
            zacetek = Integer.parseInt(vrstica[0]);
            konec = Integer.parseInt(vrstica[1]);

            // Ustvarimo tabelo v katermo bomo skranili povezave
            krizisca = new Krizisce[stKrizisc];
            for (int i = 0; i < stKrizisc; i++) {
                krizisca[i] = new Krizisce();
                krizisca[i].id = i;
            }

            for (int i = 0; i < stPovezav; i++) {
                str = br.readLine();
                vrstica = str.split(" ");
                // Ker so povezave dvo smerne dodamo povezavo za vsako vozlisce
                krizisca[Integer.parseInt(vrstica[0])].povezave.add(Integer.parseInt(vrstica[1]));
                krizisca[Integer.parseInt(vrstica[1])].povezave.add(Integer.parseInt(vrstica[0]));
            }

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void izpis() {
        for (int i = 0; i < krizisca.length; i++) {
            System.out.println(krizisca[i].povezave);
        }
    }

    public class Krizisce {
        int id;
        ArrayList<Integer> povezave;
        Krizisce parent;

        Krizisce() {
            povezave = new ArrayList<Integer>();
            parent = null;
        }
    }

    public class Povezava implements Comparable<Povezava> {
        int from;
        int to;

        Povezava(int from, int to) {
            this.from = from;
            this.to = to;
        }

        public boolean equals(Object object) {
            if (object == null) {
                return false;
            }
            if (object instanceof Povezava) {
                Povezava drugo = (Povezava) object;
                if (this.from == drugo.from && this.to == drugo.to) {
                    return true;
                }
            }
            return false;
        }

        public int hashCode() {
            return Objects.hash(from, to);
        }

        public int compareTo(Povezava povezava) {
            if (this.from != povezava.from) {
                return this.from - povezava.from;
            } else {
                return this.to - povezava.to;
            }
        }
    }

    private void writeFile(String izhod) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(izhod)));
            for (Povezava j : pot) {
                bw.write(j.from + " " + j.to);
                bw.newLine();
            }
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Naloga7(args);

    }
}
