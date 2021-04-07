package Izboljsave;

import java.io.*;

public class Naloga5 {
    protected int stPolozajev, visinaStropa;
    protected Sklad[] zacetnoStanje;
    protected Sklad[] koncnoStanje;
    protected int najSt;

    public Naloga5(String[] args) {
        long start = System.currentTimeMillis();
        // String vhod = args[0];
        String vhod = "Testi peta naloga/I5_8.txt"; // I5_2.txt
        // String izhod = args[1];
        String izhod = "Izhod.txt";
        readFile(vhod);
        najkrajsaPot(izhod);
        // writeFile(izhod);
        long end = System.currentTimeMillis();
        System.out.println("Cas izvajanja " + (end - start));
    }

    // Izracunamo najkrajso pot
    void najkrajsaPot(String izhod) {

        // Ze vnaprej izracunamo enolicno vrednosti koncne kombinacije skladisca
        int vrednostKoncnega = izracunHash(koncnoStanje);

        int[] koncnaKombinacija = {};
        int dolzinaKombinacije = 0;
        // Ustvaimo hash map da vemo katere kombinacije smo ze porabili
        HashMap uporabljeni = new HashMap();
        Vrsta vrsta = new Vrsta();

        // V hashmap shranimo zacetno kombinacijo skladisca
        int value = izracunHash(zacetnoStanje);
        HashMapElement predzadnji = uporabljeni.assign(value, -1, -1, null);

        // Vstavmo zacetni element v vrsto
        VrstaElement temp = new VrstaElement();
        temp.sklad = kopirajSkladisce(zacetnoStanje);
        vrsta.dodajZadnjega(temp);

        while (vrsta.first != null) {
            VrstaElement element = vrsta.vrniInOdstraniPrvega();
            Sklad[] skladisce = element.sklad;

            // Preverimo ce se skladisce vzeto s sklada ujema s koncnim
            int vmesnaVrednost = izracunHash(skladisce);
            if (vmesnaVrednost == vrednostKoncnega) {
                //System.out.println("konec " + element.indeks);
                //izpisiSklad(skladisce);
                predzadnji = uporabljeni.vrni(vmesnaVrednost);
                int tab[] = new int[50];
                int i = 0;
                while (predzadnji.prejsni != null) {
                    tab[i] = predzadnji.to;
                    tab[i + 1] = predzadnji.from;
                    i += 2;
                    predzadnji = predzadnji.prejsni;
                }
                //izpisiTabelo(tab, i);
                koncnaKombinacija = tab;
                dolzinaKombinacije = i;
                break;
            }

            for (int i = 0; i < skladisce.length; i++) {
                if (skladisce[i].prazen()) {
                    continue;
                }
                // Shranimo element hashmapa da ga lahko kasneje povezemo v verigo elementov
                // katera bo predstavljala zaporedje obiskanih kombinacij
                predzadnji = uporabljeni.vrni(vmesnaVrednost);

                char ele = skladisce[i].top();
                skladisce[i].pop();

                for (int j = 0; j < skladisce.length; j++) {
                    // Ce sta indeksa enaka nadaljujemo ker nocemo dodati elementa na isto mesto iz
                    // kjer smo ga odvzeli
                    if (i == j) {
                        continue;
                    }
                    if (skladisce[j].visina == visinaStropa) {
                        continue;
                    }
                    skladisce[j].push(ele);

                    // Izracunamo enolicno vrednost kombinacij skatel v skladiscu iz katere bomo
                    // potem izracunali
                    // hash
                    int vrednost = izracunHash(skladisce);
                    // Shranimo polozaj skladisca
                    // Ce je elment ze v verigi elementov funkcija vrne null,ne klicemo nove veje
                    // rekurzije
                    HashMapElement dodano = uporabljeni.assign(vrednost, i, j, predzadnji);
                    if (dodano == null) {
                        skladisce[j].pop();
                        continue;
                    }

                    // Novo kombinacijo skladisca dodamo v vrsto
                    VrstaElement nov = new VrstaElement();
                    nov.sklad = kopirajSkladisce(skladisce);
                    vrsta.dodajZadnjega(nov);

                    // Odstranim element
                    skladisce[j].pop();
                }
                // Nazaj dodamo odvzeti element
                skladisce[i].push(ele);
            }
        }
        writeFile(izhod, koncnaKombinacija, dolzinaKombinacije);
    }

    int[] potence = { 1, 29, 841, 24389, 707281, 20511149, 594823321, 249876122, 246407461, 145816292, 228672424,
            631500230, 313506472, 91687589, 658940059, 109261502, 168583525, 888922181, 778742974, 583546004, 922833940,
            762183974, 103335004, 996715094, 904737418, 237384836, 884160178, 640644887, 578701525, 782344049,
            687977179, 951337982, 588801181, 75234062, 181787776, 271845449, 883517944, 622020101, 38582731, 118899188,
            448076419, 994216019, 832264243, 135662783, 934220674, 92399249, 679578199, 707767562 };

    // Funkcija izracuna neko enolicno vrednost skladisca iz katerega pozneje
    // izracunamo hash code

    int izracunHash(Sklad[] temp) {
        int k = 1;
        int vrednost = 0;
        int h = 1000000011;

        for (int i = 0; i < temp.length; i++) {
            Sklad t = temp[i];
            SkladElement top = t.top;

            for (int j = 0; j < visinaStropa - t.visina; j++) {
                vrednost = (vrednost + ('&' - 'A' + 1) * potence[k]) % h;
                k++;
            }

            while (top != null) {
                vrednost = (vrednost + (top.element - 'A' + 1) * potence[k]) % h;
                k++;
                top = top.next;
            }
        }
        return Math.abs(vrednost);
    }

    // Metoda ki prebere vhodo datoteko in shrani njene podatke
    private void readFile(String vhod) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(vhod)));
            String str = br.readLine();
            String[] vrstica = str.split(",");
            // Preberemo paramtere
            stPolozajev = Integer.parseInt(vrstica[0]);
            visinaStropa = Integer.parseInt(vrstica[1]);
            zacetnoStanje = new Sklad[stPolozajev];
            koncnoStanje = new Sklad[stPolozajev];

            int j = 0;
            // Najprej napolnimo tabelo z zacetnimi stanji
            for (int i = 0; i < stPolozajev; i++) {
                str = br.readLine();
                vrstica = str.split(":");
                zacetnoStanje[j] = new Sklad();
                if (vrstica.length > 1) {
                    vrstica = vrstica[1].split(",");
                    for (int k = 0; k < vrstica.length; k++) {
                        zacetnoStanje[j].push(vrstica[k].charAt(0));
                    }
                }
                j++;
            }
            j = 0;
            // Nato pa napolnimo se tabelo z koncnimi
            for (int i = 0; i < stPolozajev; i++) {
                str = br.readLine();
                vrstica = str.split(":");
                koncnoStanje[j] = new Sklad();
                if (vrstica.length > 1) {
                    vrstica = vrstica[1].split(",");
                    for (int k = 0; k < vrstica.length; k++) {
                        koncnoStanje[j].push(vrstica[k].charAt(0));
                    }
                }
                j++;
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class Sklad {
        private SkladElement top;
        private int visina;

        Sklad() {
            top = null;
            visina = 0;
        }

        public boolean prazen() {
            return (top == null);
        }

        public char top() {
            if (prazen() == false) {
                return top.element;
            } else {
                return '-';
            }
        }

        public void push(char obj) {
            SkladElement temp = new SkladElement();
            temp.element = obj;
            temp.next = top;
            top = temp;
            visina++;
        }

        public void addFirst(char obj) {
            if (top == null) {
                push(obj);
                return;
            } else {
                SkladElement temp = top;
                while (temp.next != null) {
                    temp = temp.next;
                }
                SkladElement nov = new SkladElement();
                nov.element = obj;
                temp.next = nov;
            }
            visina++;
        }

        public void pop() {
            if (prazen() == false) {
                top = top.next;
            }
            visina--;
        }

    }

    class SkladElement {
        char element;
        SkladElement next;

        SkladElement() {
            next = null;
        }
    }


    class HashMapElement {
        private int kljuc;
        private int from;
        private int to;

        //Vsak element dodan v hashmap bo kazal na element iz katerega je bil generiran(sta sosedna)
        private HashMapElement prejsni;

        public HashMapElement(int kljuc, int from, int to, HashMapElement prejsni) {
            this.kljuc = kljuc;
            this.from = from;
            this.to = to;
            this.prejsni = prejsni;
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

    public class HashMap {
        Set[] tabela;

        public HashMap() {
            makenull(300000);
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
        public HashMapElement assign(int kljuc, int from, int to, HashMapElement prejsni) {
            HashMapElement par = new HashMapElement(kljuc, from, to, prejsni);
            // Iracunamp mesto v tabeli glede na kljuc
            int i = hash(kljuc);
            boolean vstavljen = tabela[i].insert(par);
            // Vrnemo logicno ki pove ce je element bil vstavljen
            if (vstavljen == false) {
                return null;
            }
            return par;
        }

        public HashMapElement vrni(int kljuc) {
            Set set = tabela[hash(kljuc)];
            SetElement pos = set.locate(new HashMapElement(kljuc, 0, 0, null));
            if (pos != null) {
                return ((HashMapElement) set.retrieve(pos));
            }
            return null;
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

    // Mnozica elementov
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

    class VrstaElement {
        //int element;
        Sklad[] sklad;
        VrstaElement next;

        VrstaElement() {
            //element = 0;
            next = null;
        }
    }

    public class Vrsta {
        private VrstaElement first;
        private VrstaElement last;

        public Vrsta() {
            first = null;
            last = null;
        }

        // Dodajanje na zadnje mesto
        public void dodajZadnjega(VrstaElement temp) {
            // Preverimo ce je seznm prazen
            if (first == null) {
                first = temp;
                last = temp;
            } else {
                last.next = temp;
                last = temp;
            }
        }

        // Odstrani ter vrne prvi element(za kazalec last)
        public VrstaElement vrniInOdstraniPrvega() {
            VrstaElement temp = null;
            if (first != null) {
                temp = first;
                first = first.next;
                if (first == null) {
                    last = null;
                }
            }
            return temp;
        }
    }

    // Funkcija ki kopira sklad
    Sklad[] kopirajSkladisce(Sklad[] skladisce) {
        // Ustvarimo nov sklad
        Sklad[] nov = new Sklad[stPolozajev];
        for (int i = 0; i < skladisce.length; i++) {
            Sklad temp = skladisce[i];
            nov[i] = new Sklad();
            // Ce je sklad katerega kopiramo parazen nadaljujemo
            if (temp.prazen()) {
                continue;
            } else {
                // Ce ni prazen prepisemo sklada
                SkladElement t = temp.top;
                while (t != null) {
                    nov[i].addFirst(t.element);
                    t = t.next;
                }
            }
        }
        return nov;
    }

    private void writeFile(String izhod, int[] t, int length) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(izhod)));
            for (int i = length - 1; i > 0; i = i - 2) {
                bw.write("VZEMI " + (t[i] + 1));
                bw.newLine();
                bw.write("IZPUSTI " + (t[i - 1] + 1));
                bw.newLine();
            }
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Naloga5 naloga = new Naloga5(args);
    }
}
