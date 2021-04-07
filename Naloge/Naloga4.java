package Izboljsave;

import java.io.*;

public class Naloga4 {
    private int n;
    private String[] ukazi;
    private List polje;
    //private boolean[] bloki;
    private HashMap bloki;
    private int free; // Imamo vrzel ce je vecji od nic

    public Naloga4(String[] args) {
        long start = System.currentTimeMillis();
        // String vhod = args[0];
        String vhod = "Testi cetrta naloga/I4_10.txt"; // I4_10.txt
        // String izhod = args[1];
        String izhod = "Izhod.txt";
        readFile(vhod);
        run();
        writeFile(izhod);
        long end = System.currentTimeMillis();
        System.out.println("Cas izvajanja " + (end - start));
    }

    // To funkcijo bi lahko premaknili direktno med branje elementov
    public void run() {

        for (int i = 0; i < ukazi.length; i++) {
            String line[] = ukazi[i].split(",");
            // System.out.print(line[0] + ",");
            switch (line[0]) {
                case "i": // Poklici fuknckijo init
                    init(Integer.parseInt(line[1]));
                    break;
                case "a": // Poklici fuknckijo alloc
                    boolean alloc = alloc(Integer.parseInt(line[1]), Integer.parseInt(line[2]));
                    break;
                case "f": // Poklici fuknckijo free
                    int sprosceno = free(Integer.parseInt(line[1]));
                    // free += sprosceno;
                    break;
                case "d": // Poklici fuknckijo defrag
                    defrag(Integer.parseInt(line[1]));
                    break;
            }

        }
    }

    // Inicializiraj prazno statično polje
    public void init(int size) {
        //System.out.println(size);
        polje = new List(size);
        // bloki = new boolean[size];
        bloki = new HashMap();
    }

    // Dodelimo size bajtov spremenljivki z oznako id
    // Dodeli se prvih size bajtov prostora v polju
    public boolean alloc(int size, int id) {
        // System.out.print(size + "," + id+" ");
        //if (bloki[id] == true) {
        if (bloki.vrniVrednost(id) == 1) {
            return false;
        }
        // Ce obstaja vrzel ga poskusamo dodati vanjo
        if (free > 0 && free >= size) {
            Block temp = polje.prvi;
            boolean vrzel = false;
            while (temp.next != polje.zadnji.next) {
                if (temp.next.id == 0 && temp.next.size >= size) {
                    vrzel = true;
                    break;
                }
                temp = temp.next;
            }

            // Ce smo nasli ustrezno vrzel vstavimo blok
            if (vrzel == true) {
                Block nov = new Block(id, size);
                // Zmanjsamo velikost praznega bloka ter skupnega praznega prostora
                temp.next.size -= size;
                free -= size;
                // Ce je velikost praznega bloka 0 to pomeni da ne obstaja vec in ga odstranimo
                // in dodamo nov element
                if (temp.next.size == 0) {
                    polje.remove(temp);
                    polje.insert(nov, temp);
                } else {
                    // V verigo vstavimo nov element
                    polje.insert(nov, temp);
                }
                //bloki[id] = true;
                bloki.assign(id, 1);
                // System.out.print("|"+free+" "+polje.dolzina+" |");
                // izpisi();
                return true;
            }
        }
        // Ce smo prisli do sem nismo mogli bloka vstaviit v nobeno vrzel
        // Preverimo ce blok lahko vstavimo cene vrnemo false
        if (polje.dolzina <= 0 || polje.dolzina < size) {
            System.out.println("Ni prostora");
            return false;
        } else {
            polje.addLast(id, size);
            bloki.assign(id, 1);
            polje.dolzina -= size;
            // System.out.print("|"+free+" "+polje.dolzina+" |");
            // izpisi();
        }

        return true;
    }

    // Izbrise spremenjivko id iz polja
    // Metoda vrne st sproscenih bajtov
    public int free(int id) {
        // System.out.print(id + " ");
        if (bloki.vrniVrednost(id)!=1) {
            return 0;
        }

        Block temp = polje.prvi;
        int sprosceno = 0;

        if (polje.zadnji == null) {
            int a = polje.prvi.next.size;
            polje.deletelast();
            free = 0;
            return a;
        }

        // Ce brisemo zadnje polje ga lahko zbrisemo direktno
        if (polje.zadnji.next.id == id) {
            sprosceno = polje.zadnji.next.size;
            // Posodobimo spremenljivke potrebne za nadzor dolzine seznama
            polje.dolzina += polje.zadnji.next.size;
            free -= polje.zadnji.next.size;
            polje.deletelast();
            if (polje.zadnji != null) {

                while (polje.zadnji.next.id == 0) {
                    polje.dolzina += polje.zadnji.next.size;
                    free -= polje.zadnji.next.size;
                    polje.deletelast();
                    if (polje.zadnji == null) {
                        break;
                    }
                }
            }
        } else {
            while (temp != polje.zadnji.next) {
                if (temp.next.id == id) {
                    temp.next.id = 0;
                    sprosceno = temp.next.size;
                    // if(temp.id==0){
                    polje.uredi(temp);
                    // }
                    break;
                }
                temp = temp.next;
            }
        }
        // System.out.print("|"+(free+bloki[id])+" "+polje.dolzina+" |");
        // izpisi();
        // Blok v tabeli nastavimo na nic v primeru da ponovno dodamo blok z istim id
        bloki.changeValue(id,0);
        free += sprosceno;
        return sprosceno;
    }

    void izpis() {
        Block temp = polje.prvi.next;
        int i = 0;
        while (temp != null) {
            if (temp.id == 0) {
                i += temp.size;
                temp = temp.next;
                // System.out.println("null size"+temp.size);
                continue;
            }
            System.out.println(temp.id + "," + i + "," + (i + temp.size - 1));
            i += temp.size;
            temp = temp.next;
        }
    }

    // Izbrise n blokov praznega prostora
    public void defrag(int n) {
        // System.out.print(n + " ");
        // Preverimo ce je seznam prazen
        if (polje.zadnji == null) {
            return;
        }
        Block temp = polje.prvi;
        for (int i = 0; i < n; i++) {
            while (temp != polje.zadnji.next) {
                if (temp.next.id == 0) {
                    Block prazen = temp.next;// Shranimo prazen blok
                    if (prazen == polje.zadnji) {// Ce je prazen blok predzadnji ga je potrebno le zbrisati
                        polje.remove(temp);
                        free = 0;
                        polje.dolzina += prazen.size;
                    } else {
                        polje.remove(temp);// Prvi blok povezemo z desim sosedom od praznega(V bistvu zbrisemo prazen
                                           // blok iz vrste)
                        polje.insert(prazen, temp.next);// Nazaj dodamo prazen blok za eno bolj v desno kot prej
                        polje.uredi(prazen);
                    }
                    // System.out.print("|"+free+" "+polje.dolzina+" |");
                    // izpisi();
                    break;
                }
                temp = temp.next;
            }
        }
    }

    // Seznam, ki v nasem primeru simulira pomnilniški prostor
    private class List {
        private int dolzina;
        private Block prvi;
        private Block zadnji;

        public List(int n) {
            makenull(n);
        }

        public void makenull(int n) {
            dolzina = n;
            prvi = new Block(-1, -1);
            zadnji = null;
        }

        private void uredi(Block block) {
            if (block.id != 0) {
                block = block.next;
            }
            Block temp = block.next;
            boolean z = false;
            while (temp.id == 0) {
                if (temp == zadnji) {
                    z = true;
                }
                block.size += temp.size;
                temp = temp.next;
            }
            block.next = temp;
            if (z) {
                zadnji = block;
            }
        }

        public void addLast(int obj, int size) {
            Block temp = new Block(obj, size);
            if (zadnji == null) {
                prvi.next = temp;
                zadnji = prvi;
            } else {
                zadnji.next.next = temp;
                zadnji = zadnji.next;
            }
        }

        public void remove(Block obj) {
            // Dobi element prednjim
            if (obj.next == zadnji) {
                obj.next = obj.next.next;
                zadnji = obj;
            } else if (obj == zadnji) {
                deletelast();
            } else {
                obj.next = obj.next.next;
            }

        }

        public void insert(Block nov, Block prejsni) {
            // BLock nov vstavimo za blok prejsni
            if (prejsni == zadnji.next) {
                addLast(nov.id, nov.size);
                return;
            }
            nov.next = prejsni.next;
            prejsni.next = nov;
            if (prejsni == zadnji) {
                zadnji = nov;
            }
        }

        public void deletelast() {
            // SeznamElement temp=prvi.naslednji;
            Block temp = prvi;
            if (prvi == zadnji) {
                // ce samo 1 element v seznamu
                zadnji = null;
                prvi.next = null;
                return;
            }

            if (zadnji == null) {
                // seznam prazen;
                return;
            }

            while (temp.next != zadnji) {
                // System.out.println(temp.element);
                temp = temp.next;
            }
            zadnji = temp;
            zadnji.next.next = null;
        }

    }

    // EN blok simulira blok v pomnilniku
    private class Block {
        private int id;
        private int size;
        private Block next;
        private Block previous;

        public Block(int id, int size) {
            this.id = id;
            next = null;
            previous = null;
            this.size = size;
        }
    }


    // Metoda ki prebere vhodo datoteko in shrani njene podatke
    private void readFile(String vhod) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(vhod)));
            String str = br.readLine();
            n = Integer.parseInt(str);
            ukazi = new String[n];
            int i = 0;
            free = 0;
            while ((str = br.readLine()) != null) {
                // String line[] = str.split(",");
                ukazi[i] = str;
                i++;
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeFile(String izhod) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(izhod)));
            Block temp = polje.prvi.next;
        int i = 0;
        while (temp != null) {
            if (temp.id == 0) {
                i += temp.size;
                temp = temp.next;
                continue;
            }
            //System.out.println(temp.id + "," + i + "," + (i + temp.size - 1));
            bw.write(temp.id + "," + i + "," + (i + temp.size - 1));
            //Kaj pa new line ob zadnjem klicu
            bw.newLine();
            i += temp.size;
            temp = temp.next;
        }
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class HashMap {
        Set[] tabela;

        public HashMap() {
            makenull(1000);
        }

        public void makenull(int size) {
            tabela = new Set[size];

            for (int i = 0; i < tabela.length; i++) {
                tabela[i] = new Set();
            }
        }

        private int hash(Object kljuc) {
            return Math.abs(kljuc.hashCode()) % tabela.length;
        }

        // Dodajanje elementa po kljucu v mnozico
        public boolean assign(int kljuc, int vrednost) {
            HashMapElement par = new HashMapElement(kljuc, vrednost);
            // Iracunamp mesto v tabeli glede na kljuc
            int i = hash(kljuc);
            boolean vstavljen = tabela[i].insert(par);
            // Vrnemo logicno ki pove ce je element bil vstavljen
            return vstavljen;
        }

        public void delete(int kljuc) {
            // Funkcija zbrise par (d, r) iz preslikave M.
            // To pomeni, da vrednost M(d) ni vec definirana.
            Set l = tabela[hash(kljuc)];
            SetElement pos = l.locate(new HashMapElement(kljuc, 0));

            if (pos != null) {
                l.delete(pos);
            }
        }

        public void changeValue(int kljuc, int vrednost) {
            Set l = tabela[hash(kljuc)];
            HashMapElement node = new HashMapElement(kljuc, vrednost);

            Object pos = l.locate(node);

            if (pos != null) {
                ((HashMapElement) l.retrieve((SetElement) pos)).vrednost = vrednost;
            }
        }

        public int vrniVrednost(int kljuc) {
            // Funkcija vrne vrednost M(d).
            // Ce vrednost M(d) ni definirana, funkcija vrne null.
            Set l = tabela[hash(kljuc)];
            SetElement pos = l.locate(new HashMapElement(kljuc, 0));
            if (pos != null) {
                return ((HashMapElement) l.retrieve(pos)).vrednost;
            }
            return -1;
        }
    }
    
    class HashMapElement {
        private int kljuc;
        private int vrednost;

        public HashMapElement(int kljuc, int vrednost) {
            this.kljuc = kljuc;
            this.vrednost = vrednost;
        }

        // Metoda s katermo preverimo ce imata dva elementa enak kljuc
        public boolean equals(Object obj) {
            if (obj instanceof HashMapElement) {
                HashMapElement node = (HashMapElement) obj;
                return kljuc == node.kljuc;
            }
            return false;
        }
    }
    // Element mnozice
    class SetElement {
        Object element;
        SetElement next;

        SetElement() {
            element = null;
            next = null;
        }
    }
    
    public class Set {
        private SetElement first;

        public Set() {
            first = new SetElement();
        }

        public SetElement first() {
            return first;
        }

        public SetElement next(SetElement pos) {
            return pos.next;
        }

        // Vstavimo element, se prej preverimo ce je element ze v mnozici. Ce je vrnemo
        // false drugace true
        public boolean insert(Object obj) {
            if (locate(obj) == null) {
                SetElement nov = new SetElement();
                nov.element = obj;
                nov.next = first.next;
                first.next = nov;
                return true;
            }
            return false;
        }

        public void delete(SetElement pos) {
            pos.next = pos.next.next;
        }

        // Preverimo ce je konec mnozice
        public boolean overEnd(SetElement pos) {
            if (pos.next == null)
                return true;
            else
                return false;
        }

        // Preverimo ce je mnozica prazna
        public boolean empty() {
            return first.next == null;
        }

        public Object retrieve(SetElement pos) {
            return pos.next.element;
        }

        // Lociramo element v mnozici
        public SetElement locate(Object obj) {
            for (SetElement iter = first(); !overEnd(iter); iter = next(iter))
                if (obj.equals(retrieve(iter)))
                    return iter;

            return null;
        }
    }   
    public static void main(String[] args) {
        new Naloga4(args);
    }
}
