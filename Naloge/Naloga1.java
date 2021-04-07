package Izboljsave;

import java.io.*;

public class Naloga1 {
    private int dolzina;
    private int rezervar;
    private int stPostaj;
    private Postaja[] seznamPostaj;
    private Pot najPot;

    public Naloga1(String[] args) {
        long start =System.currentTimeMillis();
        // String vhod = args[0];
        String vhod = "Testi prva naloga/I1_10.txt";
        // String izhod = args[1];
        String izhod = "Izhod.txt";
        preberiDatoteko(vhod);
        //izpisiPodatke();

        //Preverimo ce je velikost rezervarja slucajno vecja od celotne poti
        if(rezervar>=dolzina){
            najPot=new Pot();
            zapisiDatoteko(izhod);
        }else{
            Pot temp = new Pot();
            izracunajNajcenejso(0, temp);
        }
        
        //najPot.izpisi();
        zapisiDatoteko(izhod);
        long end =System.currentTimeMillis();
        //System.out.println();
        System.out.println(end-start);
    }

    
    private void izracunajNajcenejso(int indeks, Pot pot) {
        //Robni pogoj 1
        if (indeks > 0 && dolzina - seznamPostaj[indeks - 1].razdaljaOdIzhodisca <= rezervar || indeks >= stPostaj) {
           //Ce je najPot prazna jo prekopiramo da jo imamo v naslednjem klicu kaj primerjati
            if (najPot.zadnji == null) {
                pot.kopirajSeznam();
            }
            //Ce smo nasli boljso pot jo prekopirajmo
            if (pot.cenaPoti < najPot.cenaPoti) {
                pot.kopirajSeznam();
            } else if (pot.cenaPoti == najPot.cenaPoti) {
                if (pot.dolzinaPoti < najPot.dolzinaPoti) {
                    pot.kopirajSeznam();
                }
            }
            return;
        }

        // Najde mozne premike iz trenutne postaje
        int[] sosedi = najdiSosede(indeks);
        // Ce ni sosedov prekini izvajanje
        if (sosedi == null) {
            return;
        }
        for (int i = 0; i < sosedi.length; i++) {
            // Racunanje cene dotankanega goriva(temp=cena poti do trenutne postaje)
            int temp = 0;
            if (indeks != 0) {
                temp = seznamPostaj[pot.vrniIdZadnjega() - 1].razdaljaOdIzhodisca;
            }
            temp = seznamPostaj[sosedi[i] - 1].razdaljaOdIzhodisca - temp;
            temp = temp * seznamPostaj[sosedi[i] - 1].cena;
            //Ce pot do tega vozlisca ni boljsa od trenutne najboljse nadaljuj drrugace jo poosodobi
            if(seznamPostaj[sosedi[i] - 1].trenutnaNajCenejsaPot==-1){
                seznamPostaj[sosedi[i] - 1].trenutnaNajCenejsaPot=(pot.cenaPoti + temp);
            }else if(seznamPostaj[sosedi[i] - 1].trenutnaNajCenejsaPot<(pot.cenaPoti + temp)){
                continue;
            }
            //Nastavimo novo trenutno najboljso pot   
            seznamPostaj[sosedi[i] - 1].trenutnaNajCenejsaPot=(pot.cenaPoti + temp);

            pot.cenaPoti += temp; // Prištejemo izracunano ceno
            pot.dolzinaPoti++; // Povecamo dolzino poti
            pot.addLast(sosedi[i]); // Dodamo trenutni element
            izracunajNajcenejso(indeks + 1 + i, pot);
            pot.cenaPoti -= temp;
            pot.deletelast(); // Odstejemo 1 od dolzine poti in izbrisemo ravno kar dodani element
            pot.dolzinaPoti--;
        }
    }
    
    int[] najdiSosede(int indeks) {
        int tab[] = new int[stPostaj];
        int i = 0;
        int k = indeks;

        if (indeks == 0) {
            while (seznamPostaj[i].razdaljaOdIzhodisca <= rezervar) {
                tab[i] = seznamPostaj[i].id;
                i++;

                //Na novo dodano preveri ce dela
                if(i==stPostaj){
                    break;
                }
                //!!!
            }
        } else {
            indeks -= 1;
            while ((seznamPostaj[k].razdaljaOdIzhodisca - seznamPostaj[indeks].razdaljaOdIzhodisca) <= rezervar) {
                tab[i] = seznamPostaj[k].id;
                i++;
                k++;
                if (k == stPostaj) {
                    break;
                }
            }
        }

        int[] t = new int[i];
        // System.out.println("Dolzina novega seznama je "+i);
        for (int j = 0; j < t.length; j++) {
            t[j] = tab[j];
            // System.out.print(t[j]+",");
        }
        // System.out.println();
        return t;
    }

     class Postaja {
        private int id;
        private int razdalja;
        private int cena;
        private int razdaljaOdIzhodisca;

        //test
        private int trenutnaNajCenejsaPot;

        protected Postaja(int id, int razdalja, int cena, int razdaljaOdIzhodisca) {
            this.id = id;
            this.razdalja = razdalja;
            this.cena = cena;
            this.razdaljaOdIzhodisca = razdaljaOdIzhodisca;
            trenutnaNajCenejsaPot=-1;
        }
    }

    // Povezan seznam, ki predstavlja pot
     class Pot {
        private PotElement prvi;
        private PotElement zadnji;
        private int cenaPoti;
        private int dolzinaPoti;

        Pot() {
            makeNull();
        }

        public void makeNull() {
            prvi = new PotElement(null, null);
            zadnji = null;
            cenaPoti = 0;
            dolzinaPoti = 0;
        }

        int vrniIdZadnjega() {
            return (int) zadnji.naslednji.element;
        }

        public void kopirajSeznam() {
            PotElement temp = this.prvi.naslednji;
            najPot = new Pot();
            while (temp != null) {
                najPot.addLast(temp.element);
                temp = temp.naslednji;
            }
            najPot.cenaPoti = this.cenaPoti;
            najPot.dolzinaPoti = this.dolzinaPoti;
        }

        public void addLast(Object obj) {
            PotElement temp = new PotElement(obj, null);
            if (zadnji == null) { // Prazen seznam
                prvi.naslednji = temp;
                zadnji = prvi;
            } else {
                zadnji.naslednji.naslednji = temp;
                zadnji = zadnji.naslednji;
            }
        }

        public void deletelast() {
            PotElement temp = prvi;
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
            while (temp.naslednji != zadnji) {
                temp = temp.naslednji;
            }
            zadnji = temp;
            zadnji.naslednji.naslednji = null;
        }

        public void izpisi() {
            PotElement temp = prvi.naslednji;
            if (temp == null && zadnji == null) {
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
        }
    }

    // Vozlišče povezanega seznama Pot
     class PotElement {
        Object element;
        PotElement naslednji;

        public PotElement(Object element, PotElement naslednji) {
            this.element = element;
            this.naslednji = naslednji;
        }
    }

    private void preberiDatoteko(String pot) {
        File vhodnaDatoteka = new File(pot);
        try {
            BufferedReader br = new BufferedReader(new FileReader(vhodnaDatoteka));
            String str = br.readLine();
            String temp[] = str.split(",");
            dolzina = Integer.parseInt(temp[0]);
            rezervar = Integer.parseInt(temp[1]);
            stPostaj = Integer.parseInt(temp[2]);
            seznamPostaj = new Postaja[stPostaj];
            najPot = new Pot();
            int i = 0;
            int sum = 0;
            while ((str = br.readLine()) != null) {
                String t[] = str.split(",");
                String a[] = t[0].split(":");
                sum += Integer.parseInt(a[1]);
                Postaja postaja = new Postaja(Integer.parseInt(a[0]), Integer.parseInt(a[1]), Integer.parseInt(t[1]),sum);
                seznamPostaj[i] = postaja;
                i++;
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void izpisiPodatke() {
        System.out.println(dolzina + " " + rezervar + " " + stPostaj);
        for (int i = 0; i < seznamPostaj.length; i++) {
            System.out.println(seznamPostaj[i].id + ":" + seznamPostaj[i].razdalja + "," + seznamPostaj[i].cena + "  "
                    + seznamPostaj[i].razdaljaOdIzhodisca);
        }
    }

    public void zapisiDatoteko(String izhod){
        try{
           BufferedWriter bw = new BufferedWriter(new FileWriter(new File(izhod))); 
           PotElement t=najPot.prvi.naslednji;
           boolean vejica=false;
           while(t!=null){
               if(vejica==true){
                bw.write(",");
               }
               bw.write(String.valueOf(t.element));
               t=t.naslednji;
               vejica=true;
               //NewLine??
           }
           bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    public static void main(String[] args) {
        new Naloga1(args);
    }
}
