package Seminarska;

import java.io.*;

public class Naloga3 {
    int stVrstic;
    int zadnjaVrstica;
    int stPremikov;
    int premikDoNoveVrstice;
    int steviloElementov;
    Seznam[] tabela;
    char[] prevajalnik = { 'A', 'B', 'C', 'Č', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'R',
            'S', 'Š', 'T', 'U', 'V', 'Z', 'Ž', 'a', 'b', 'c', 'č', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'r', 's', 'š', 't', 'u', 'v', 'z', 'ž', ' ' };

    /*
    public Naloga3(int stVrstic, int zadnjaVrstica, int stPremikov, int premikDoNoveVrstice) {
        this.stVrstic = stVrstic;
        this.zadnjaVrstica = zadnjaVrstica;
        this.stPremikov = stPremikov;
        this.premikDoNoveVrstice = premikDoNoveVrstice;
        tabela = new Seznam[stVrstic];
    }
    */
    public class Seznam {
        protected SeznamElement prvi;
        protected SeznamElement zadnji;

        public Seznam() {
            makeNull();
        }

        public void makeNull() {
            prvi = new SeznamElement(null, null);
            zadnji = null;
        }

        public void addLast(Object obj) {
            SeznamElement temp = new SeznamElement(obj, null);
            if (zadnji == null) {
                prvi.naslednji = temp;
                zadnji = prvi;
            } else {
                zadnji.naslednji.naslednji = temp;
                zadnji = zadnji.naslednji;
            }
            /*
             * if(zadnji==null){ //seznam prazen prvi.naslednji=temp; zadnji=prvi; return; }
             * //seznam ni prazen zadnji.naslednji.naslednji=temp; zadnji=zadnji.naslednji;
             */
        }

        public void addFirst(Object obj) {
            SeznamElement temp = new SeznamElement(obj, null);
            temp.naslednji = prvi.naslednji;
            prvi.naslednji = temp;
            if (zadnji == null) {
                zadnji = prvi;
            } else if (zadnji == prvi) {
                zadnji = temp;
            }

            /*
             * if(zadnji==null){ //seznam prazen prvi.naslednji=temp; zadnji=prvi; return; }
             * temp.naslednji=prvi.naslednji; prvi.naslednji=temp;
             */
        }

        public void izpisi() {
            SeznamElement temp = prvi.naslednji;
            if (temp == null && zadnji == null) {
                System.out.println("Prazen seznam");
                return;
            }
            int i = 1;
            while (temp != null) {
                if (i != 1) {
                    System.out.print(",");
                }
                System.out.print(temp.element);
                temp = temp.naslednji;
                i = 0;
            }
            System.out.println();
            // System.out.println("prvi"+prvi.element+ " kazalec "+prvi.naslednji.element);
            // System.out.println("prvi"+zadnji.element+ " kazalec
            // "+zadnji.naslednji.element);
        }

        public SeznamElement last() {
            if (zadnji != null) {
                return zadnji.naslednji;
            }
            return null;

        }

        public SeznamElement first() {
            return prvi.naslednji;
        }

        // Mogoce raje dodaj prejsni element v Seznam element
        public void deletelast() {
            // SeznamElement temp=prvi.naslednji;
            SeznamElement temp = prvi;
            if (prvi == zadnji) {
                // ce samo 1 element v seznamu
                zadnji = null;
                prvi.naslednji = null;
                return;
            }

            if (zadnji == null) {
                // seznam prazen;
                return;
            }

            // if(temp==zadnji){
            // //dva elementa v seznamu
            // temp.naslednji=null;
            // zadnji=prvi;
            // return;
            // }

            while (temp.naslednji != zadnji) {
                // System.out.println(temp.element);
                temp = temp.naslednji;
            }
            zadnji = temp;
            zadnji.naslednji.naslednji = null;
        }

        public void removeFirst() {
            if (zadnji == null) {
                // seznam prazen;
                return;
            }
            if (prvi == zadnji) {
                // samo 1 element v seznamu
                zadnji = null;
                prvi.naslednji = null;
                return;
            }
            if (prvi.naslednji == zadnji) {
                prvi.naslednji = zadnji;
                zadnji = prvi;
            }
            prvi.naslednji = prvi.naslednji.naslednji;

            /*
             * if (prvi.naslednji == null) { //Prazen seznam return; } SeznamElement temp =
             * prvi.naslednji; if (temp.naslednji == null) { //Le se en element v seznamu
             * prvi.naslednji = null; return; } prvi.naslednji = temp.naslednji; if (zadnji
             * == temp) { zadnji = prvi; }
             */
        }
    }

    public class SeznamElement {
        Object element;
        SeznamElement naslednji;

        public SeznamElement(Object element, SeznamElement naslednji) {
            this.element = element;
            this.naslednji = naslednji;
        }
    }

    public int vrsticaL(int znak) {
        int rez = 0;
        // k =(l+element[i])%n
        // l-vrstica iz katere vzamemo element[l]
        // k-trenutna vrstica
        // neznano=vrstica iz katere smo element vzeli

        for (int i = 0; i < stVrstic; i++) {
            if ((i + znak) % stVrstic == zadnjaVrstica) {
                rez = i;
                break;
            }
        }
        return rez;
    }

    public int vrsticaK(int l) {
        int rez = 0;
        // l=(k+P)%N
        // iscemo k
        for (int i = 0; i < stVrstic; i++) {
            int temp = i + premikDoNoveVrstice;
            if (temp < 0) {
                temp = stVrstic + (temp % stVrstic);
            }
            if ((temp) % stVrstic == l) {
                rez = i;
                break;
            }
        }
        return rez;
    }

    public void decode() {
        // Dekodiramo v nasprotno smer
        for (int i = 1; i <= stPremikov; i++) {
            SeznamElement znak = tabela[zadnjaVrstica].last(); // Vzamemo zadnji element podane zadnje vrstice
            String elemet = (String) znak.element;
            int l = vrsticaL(Integer.parseInt(elemet));
            // System.out.println("l: " + l + " k: " + zadnjaVrstica);
            tabela[zadnjaVrstica].deletelast(); // Izbrisemo zadnji element trenutne vrstice
            tabela[l].addFirst(elemet); // Dodamo element v izracunano vrstico

            // for (int j = 0; j < stVrstic; j++) {
            // tabela[j].izpisi();
            // }

            // Izracunamo nasljednjo vrstico

            if ((i % 2) == 0) {
                int nas = l - premikDoNoveVrstice;

                if (nas >= stVrstic) {
                    // System.out.println(nas);
                    // nas = stVrstic - nas;
                    nas = nas % stVrstic;
                }

                if (nas < 0) {
                    nas = stVrstic + (nas % stVrstic);
                }
                zadnjaVrstica = nas;
            } else {
                zadnjaVrstica = vrsticaK(l);
            }
            // System.out.println("l: "+l+" k: "+zadnjaVrstica);
        }
    }

    public void zakodiraj() {
        for (int i = 1; i <= stPremikov; i++) {

            SeznamElement znak = tabela[zadnjaVrstica].first();
            String elemet = (String) znak.element;
            tabela[zadnjaVrstica].removeFirst();
            int k = (zadnjaVrstica + Integer.parseInt(elemet)) % stVrstic;
            // System.out.println(Integer.parseInt(elemet));
            tabela[k].addLast(elemet);

            int l = (k + premikDoNoveVrstice) % stVrstic;
            if (l >= stVrstic) {
                l = stVrstic - l;

            }
            if (l < 0) {
                l = stVrstic + l;
            }
            // System.out.println("k: " + k + " l: " + l);
            // for (int j = 0; j < stVrstic; j++) {
            // tabela[j].izpisi();
            // }
            zadnjaVrstica = l;

            if ((i % 2) == 0) {
                int nas = k + premikDoNoveVrstice;
                if (nas >= stVrstic) {
                    nas = stVrstic - nas;
                }
                if (nas < 0) {
                    nas = stVrstic + nas;
                }
                zadnjaVrstica = nas;
            }
        }
    }

    private void readFile(String vhod) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(vhod)));
            String str = br.readLine();
            String temp[] = str.split(",");
            stVrstic = Integer.parseInt(temp[0]);
            zadnjaVrstica = Integer.parseInt(temp[1]);
            stPremikov = Integer.parseInt(temp[2]);
            premikDoNoveVrstice = Integer.parseInt(temp[3]);
            tabela = new Seznam[stVrstic];
            int indeks = 0;
            int stElementov = 0;
            while ((str = br.readLine()) != null) {
                if (str.equals("")) {
                    Seznam s = new Seznam();
                    tabela[indeks] = s;
                    indeks++;
                    continue;
                }
                String vrstica[] = str.split(",");
                stElementov += vrstica.length;
                Seznam s = new Seznam();
                for (int i = 0; i < vrstica.length; i++) {
                    s.addLast(vrstica[i]);
                }
                tabela[indeks] = s;
                indeks++;
            }
            steviloElementov=stElementov;
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*
    private void writeFile(String izhod) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(izhod)));
            for (int i = 0; i < steviloElementov; i++) {
                String element = (String) tabela[i % stVrstic].first().element;
                // System.out.print(naloga.prevajalnik[Integer.parseInt(element)]);
                bw.write(prevajalnik[Integer.parseInt(element)]);
    
                tabela[i % stVrstic].removeFirst();
            }
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */
    private void writeFile(String izhod) {
        try {
            PrintWriter pw=new PrintWriter(new File(izhod),"UTF-8");
            for (int i = 0; i < steviloElementov; i++) {
                String element = (String) tabela[i % stVrstic].first().element;
                pw.write(prevajalnik[Integer.parseInt(element)]);
    
                tabela[i % stVrstic].removeFirst();
            }
            pw.println();
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Naloga3(String[] args){
        // String vhod = args[0];
        String vhod = "Testi tretja naloga/I3_8"; // I4_10.txt
        // String izhod = args[1];
        String izhod = "Izhod.txt";
        readFile(vhod);
        decode();
        writeFile(izhod);
    }
    public static void main(String[] args) throws IOException {
        new Naloga3(args);
    }
}
