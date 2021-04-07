package Izboljsave;

import java.io.*;
import java.util.*;

public class Naloga9 {
    int stVozliscVzorec;
    int stVozliscDrevo;
    Drevo vzorec;
    Drevo drevo;
    ArrayList<Vozlisce> zacetnaVozlisca;
    ArrayList<Vozlisce> X;
    int st;
    ArrayList<Character> koncno;
    ArrayList<Character> nivo;

    public Naloga9(String[] args) {
        long start = System.currentTimeMillis();
        // String vhod = args[0];
        String vhod = "Testi deveta naloga/I9_1.txt";
        // String izhod = args[1];
        String izhod = "Izhod.txt";
        readFile(vhod);
        run();
        System.out.println(koncno);
        writeFile(izhod);
        long end = System.currentTimeMillis();
        System.out.println("Cas izvajanja " + (end - start));
    }


    void run(){
        koncno = new ArrayList<Character>();
        ArrayList<Primerjava> uporabni = new ArrayList<Primerjava>();

        for (Vozlisce vozlisce : zacetnaVozlisca) {

            Primerjava p = new Primerjava(vozlisce);
            X = new ArrayList<Vozlisce>();
            if (preveriEnakost(vozlisce, vzorec.koren)) {
                p.nepraznaVozlisca = X;
                // Dodamo trenutno vozlisce v vrsto
                uporabni.add(p);
            }
        }

        // Obravnavamo problem ce je ze v korenu drevesa x ali ce je X samo eden
        if (uporabni.size() == 0) {
            preorder(drevo.koren, null);
            return;
        } else if (uporabni.get(0).nepraznaVozlisca.size() == 1) {
            int st = -1;
            int prejsno = -1;
            Vozlisce koncno = null;
            for (Primerjava i : uporabni) {
                st = prestejVozlisca(i.prvo);
                if (prejsno < st) {
                    koncno = i.nepraznaVozlisca.get(0);
                }
                prejsno = st;
            }
            preorderZaEnoVozlisce(vzorec.koren, koncno);
            return;
        }

        //Gremo cez vsa ujemanja
        for (Primerjava i : uporabni) {
            boolean pogoj=true;
            int j=0;
            int globina=0;
            //Dokler lahko iscemo ujemanje
            int stPreiskanihVozlisc=0;
            
            while(pogoj){

                ArrayList<Character>prejsno=new ArrayList<Character>();
                int indeks=0;
                //Sprehodimo se cez vsa vozlisca v trenutnem ujemanju
                for (Vozlisce vozlisce : i.nepraznaVozlisca) {
                    nivo=new ArrayList<Character>();
                    globina(globina,vozlisce,0);

                    if(indeks==0){
                        //prejsno = (ArrayList<Character>) nivo.clone();
                        prejsno=new ArrayList<Character>(nivo);
                        indeks++;

                        if(nivo.size()==0){
                            pogoj=false;
                            break;
                        }
                        continue;
                    }else{
                        //Ce stevilo elementov na prejsnem nivoju enako kot na trenutnem nadaljujemo, ce pride do neujemanja koncamo iskanje
                        if(prejsno.size()==nivo.size()){
                            //Sprehodimo se cez tabelo in preverimo ce se elementi ujemajo
                            for (int k = 0; k < prejsno.size(); k++) {
                                if(prejsno.get(k)!=nivo.get(k)){
                                    pogoj=false;
                                    break;
                                }
                            }
                            if(pogoj==false){
                                break;
                            }
                        }else{
                            pogoj=false;
                            break;
                        }
                        indeks++;
                    }
                }
                if(pogoj==true){
                    stPreiskanihVozlisc+=nivo.size();
                    globina++;
                }
                
                j++;
            }
            //Shranimo globino katero smo dosegli v trenutnem ujemanju drevesa
            i.globina=globina-1; 
            i.stVozPodrevesa=stPreiskanihVozlisc;
        }

        int najStevilo=-1;
        Primerjava najPrimerjava=null;
        for (Primerjava i : uporabni) {
            if(i.stVozPodrevesa>najStevilo){
                najPrimerjava=i;
                najStevilo=i.stVozPodrevesa;
            }
        }
        
        izpisVozlisc(vzorec.koren,najPrimerjava.nepraznaVozlisca.get(0),najPrimerjava.globina);
    }

    //Funkcija, ki pove kateri elementi se nahajajo na dolocenem nivoju
    void globina(int globina, Vozlisce v,int count) {
		if(count==globina){
            //System.out.print(v.id +" ");
            nivo.add(v.id);
            return;
        }
        Vozlisce sin = v.sin;

		while (sin != null) 
		{
			globina(globina, sin,count+1);
			sin = sin.brat;
		}
    }

    private void preorder(Vozlisce v, Primerjava zaporedje) {

        // System.out.print(v.id + ",");
        koncno.add(v.id);
        Vozlisce sin = v.sin;

        ArrayList<Vozlisce> temp = new ArrayList<Vozlisce>();
        while (sin != null) {
            temp.add(sin);
            sin = sin.brat;
        }
        for (int j = temp.size() - 1; j >= 0; j--) {
            Vozlisce t = temp.get(j);
            if (t.id == 'X') {
                for (int i = 0; i < zaporedje.najZaporedje.size(); i++) {
                    // System.out.print(zaporedje.najZaporedje.get(i) + ",");
                    koncno.add(zaporedje.najZaporedje.get(i));
                }
                continue;
            }
            preorder(t, zaporedje);
        }
    }

    //Ce se zgodi da je vzorcnem drevesu samo en X klicemo to funkcijo, kateri podamo X, ki ima najvec sinov
    private void preorderZaEnoVozlisce(Vozlisce v, Vozlisce p) {

        // System.out.print(v.id + ",");
        koncno.add(v.id);
        Vozlisce sin = v.sin;

        ArrayList<Vozlisce> temp = new ArrayList<Vozlisce>();
        while (sin != null) {
            temp.add(sin);
            sin = sin.brat;
        }
        for (int j = temp.size() - 1; j >= 0; j--) {
            Vozlisce t = temp.get(j);
            if (t.id == 'X') {
                preorder(p, null);
                continue;
            }
            preorderZaEnoVozlisce(t, p);
        }
    }

    //Funkcija za preorder drevesa
    private void izpisVozlisc(Vozlisce v, Vozlisce podDrevo,int globina) {

        //System.out.print(v.id + ",");
        koncno.add(v.id);
        Vozlisce sin = v.sin;

        ArrayList<Vozlisce> temp = new ArrayList<Vozlisce>();
        while (sin != null) {
            temp.add(sin);
            sin = sin.brat;
        }
        for (int j = temp.size() - 1; j >= 0; j--) {
            Vozlisce t = temp.get(j);
            if (t.id == 'X') {
                izpisGlobina(globina,podDrevo,0);
                continue;
            }
            izpisVozlisc(t, podDrevo, globina);
        }
    }

    //Fukncija, ki za preorder drevesa od podanega vozlisca do podane globine
    void izpisGlobina(int globina, Vozlisce v,int count) {
		if(count<=globina){
            //System.out.print(v.id +",");
            koncno.add(v.id);
        }else{
            return;
        }

        Vozlisce sin = v.sin;

        ArrayList<Vozlisce> temp = new ArrayList<Vozlisce>();
        while (sin != null) {
            temp.add(sin);
            sin = sin.brat;
        }
        for (int j = temp.size() - 1; j >= 0; j--) {
            Vozlisce t = temp.get(j);

            izpisGlobina(globina, t,count+1);
        }
        
	}

    //Funkcija, ki vrne stevilo naslednikov nekega vozlisca
    private int prestejVozlisca(Vozlisce v) {
        if (v == null)
            return 0;

        // takoj pristejemo trenutno vozlisce
        int stevilo = 1;

        Vozlisce sin = v.sin;
        while (sin != null) {
            stevilo += prestejVozlisca(sin);
            sin = sin.brat;
        }

        return stevilo;
    }

    // Metoda s katero izracunam vse mozne pojavitve vzorcnega grafa v glavnem grafu
    // in si shranim vozlisca v katerih bi se bo zacelo pod drevo
    boolean preveriEnakost(Vozlisce temp, Vozlisce primerjava) {

        if (primerjava.stSinov > 0) {
            if (primerjava.stSinov != temp.stSinov) {
                return false;
            }
        }

        Vozlisce p = primerjava.sin;
        Vozlisce v = temp.sin;
        boolean pogoj = true;
        while (p != null && pogoj == true) {
            if (v == null) {
                return false;
            }
            // Ce smo prisli do X ga dodamo in nadaljujemo
            if (p.id == 'X') {
                X.add(v);
                p = p.brat;
                v = v.brat;
                continue;
            }

            // Preverimo ce se razlikujeta
            if (p.id != v.id) {
                return false;
            }
            pogoj = preveriEnakost(v, p);
            p = p.brat;
            v = v.brat;

        }
        return pogoj;

    }

    class Primerjava {
        Vozlisce prvo;
        int stVozPodrevesa;
        ArrayList<Vozlisce> nepraznaVozlisca;
        ArrayList<Character> najZaporedje;
        int globina;

        Primerjava(Vozlisce prvo) {
            nepraznaVozlisca = new ArrayList<Vozlisce>();
            this.prvo = prvo;
        }
    }

    class Vozlisce {
        char id;
        int idNum;
        Vozlisce oce;
        Vozlisce brat;
        Vozlisce sin;
        int stSinov;

        Vozlisce(char id, Vozlisce oce) {
            this.id = id;
            this.oce = oce;
            stSinov = 0;
            this.idNum = 1;
        }

        public boolean equals(Vozlisce drugo) {
            if (this.id == drugo.id && this.stSinov == drugo.stSinov) {
                return true;
            }
            return false;
        }

    }

    public class Drevo {
        Vozlisce koren;

        Drevo(char id) {
            koren = new Vozlisce(id, null);
        }

        public Vozlisce dodajVozlisce(Vozlisce oce, char sin, int idNum) {

            Vozlisce s = new Vozlisce(sin, oce);
            s.idNum = idNum;
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
                    vzorec.dodajVozlisce(t[Integer.parseInt(vrstica[0])], vrstica[1].charAt(0),
                            Integer.parseInt(vrstica[0]));
                } else {
                    Vozlisce v = vzorec.dodajVozlisce(t[Integer.parseInt(vrstica[0])], vrstica[1].charAt(0),
                            Integer.parseInt(vrstica[0]));
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
                    v = drevo.dodajVozlisce(t[Integer.parseInt(vrstica[0])], vrstica[1].charAt(0),
                            Integer.parseInt(vrstica[0]));
                } else {
                    v = drevo.dodajVozlisce(t[Integer.parseInt(vrstica[0])], vrstica[1].charAt(0),
                            Integer.parseInt(vrstica[0]));
                    for (int j = 2; j < vrstica.length; j++) {
                        t[Integer.parseInt(vrstica[j])] = v;
                    }
                }
                if (vrstica[1].charAt(0) == temp) {
                    zacetnaVozlisca.add(v);
                }
            }
            // System.out.println("---------------");
            // drevo.izpis(0, drevo.koren);
            // System.out.println("---------------");
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeFile(String izhod) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(izhod)));
            for (int i = 0; i < koncno.size(); i++) {
                if (i != 0) {
                    bw.write(",");
                }
                bw.write(koncno.get(i));
            }
            bw.newLine();

            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Naloga9(args);
    }

}
