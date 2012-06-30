import java.io.*;
import javax.swing.JOptionPane;

public class Progetto{

    public static void main(String[] args) throws IOException{
        
        int SceltaTipoProdotto; int SceltaProdotto; int numscelta; int conferma;
        Object[] TipoProdotto = {"Film","Videogioco"};
        SceltaTipoProdotto=JOptionPane.showOptionDialog(null, "Cosa vuoi acquistare?", "Scelta prodotto", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, TipoProdotto, TipoProdotto[0]);
        
        if(SceltaTipoProdotto==0){  // se viene scelto Film
           int SceltaTipoVisualizzazione; 
           Object[] TipoVisualizzazione = {"Lista Completa","Ricerca Manuale"};
           SceltaTipoVisualizzazione=JOptionPane.showOptionDialog(null, "Come vuoi ricercare il film?", "Tipo visualizzazione", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, TipoVisualizzazione, TipoVisualizzazione[0]);
           
           if(SceltaTipoVisualizzazione==0){  //se viene scelto il metodo Lista
              Creator ls= new Creator1();
              Lista l=ls.getLista();
              l.VisualizzaLista();
              numscelta=l.SceltaListaFilm();
              if(numscelta!=0){
                  conferma=JOptionPane.showConfirmDialog(null,"Acquistare il prodotto?");
                  if(conferma==0){
                      AggiornamentoCreator ac=new Creator2();
                      Aggiornamento a=ac.getAggiornamento();
                      a.SovrascriviFileFilm(numscelta);
                      JOptionPane.showMessageDialog(null,"Grazie per aver acquistato il prodotto.","Finestra di Saluto",JOptionPane.INFORMATION_MESSAGE);
                  }
                  else{
                      JOptionPane.showMessageDialog(null,"Grazie lo stesso per aver usato il nostro servizio","Finestra di Saluto",JOptionPane.INFORMATION_MESSAGE);
                  }
              }
           }
           else{  //metodo ricerca
               Creator ri= new Creator1();
               Ricerca r=ri.getRicerca();
               if(r.CercaFilm()!=0){
                   numscelta=r.SceltaManuFilm();
                   if(numscelta!=0){
                       conferma=JOptionPane.showConfirmDialog(null,"Acquistare il prodotto?");
                       if(conferma==0){
                           AggiornamentoCreator ac=new Creator2();
                           Aggiornamento a=ac.getAggiornamento();
                           a.SovrascriviFileFilm(numscelta);
                           JOptionPane.showMessageDialog(null,"Grazie per aver acquistato il prodotto.","Finestra di Saluto",JOptionPane.INFORMATION_MESSAGE);
                       }
                       else{
                           JOptionPane.showMessageDialog(null,"Grazie lo stesso per aver usato il nostro servizio","Finestra di Saluto",JOptionPane.INFORMATION_MESSAGE);
                       }
                   }
               }    
        }
    }
        else{
            VideoGiochi g1=VideoGiochi.Instance();
            numscelta=g1.SceltaVideogioco();
            if(numscelta!=0){
                conferma=JOptionPane.showConfirmDialog(null,"Acquistare il prodotto?");
                       if(conferma==0){
                           AggiornamentoCreator ac=new Creator2();
                           Aggiornamento a=ac.getAggiornamento();
                           a.SovrascriviFileGiochi(numscelta);
                           JOptionPane.showMessageDialog(null,"Grazie per aver acquistato il prodotto.","Finestra di Saluto",JOptionPane.INFORMATION_MESSAGE);
                       }
                       else{
                           JOptionPane.showMessageDialog(null,"Grazie lo stesso per aver usato il nostro servizio","Finestra di Saluto",JOptionPane.INFORMATION_MESSAGE);
                       }
            }
        }
    }
}

// ABSTRACT FACTORY
interface Lista{
    
    void VisualizzaLista() throws IOException;
    
    int SceltaListaFilm() throws IOException;
}

interface Ricerca {
    
    int CercaFilm() throws IOException;
    
    int SceltaManuFilm() throws IOException;
}

interface Creator{
    
    Lista getLista();
    
    Ricerca getRicerca();
}

class Creator1 implements Creator{
    
    public Lista getLista(){
        
        return new ListaFilm();
    } 
    
    public Ricerca getRicerca(){
        
        return new RicercaFilm();
    }
}

class ListaFilm implements Lista{
    
    private String tempLista,tempLista2;
    private BufferedReader film;
    private BufferedReader costofilm;    
    
    public void VisualizzaLista() throws IOException{
        
        String Lista="";
        film = new BufferedReader(new FileReader("./Film.txt"));
        costofilm = new BufferedReader(new FileReader("./CostiFilm.txt"));
        
        while((tempLista=film.readLine())!=null && (tempLista2=costofilm.readLine())!=null) {
            Lista+=tempLista+" "+tempLista2+"\n";
        }
        
        film.close();
        costofilm.close();
        JOptionPane.showMessageDialog(null,Lista);
    }
    
    public int SceltaListaFilm() throws IOException{
        
        int numScelta; int contFilm=0;//contFilm conta i film totali della Lista
        int contrigalista=1; int cont=1; String temp=""; int temp2=-1;
        film = new BufferedReader(new FileReader("./Film.txt"));
        costofilm = new BufferedReader(new FileReader("./CostiFilm.txt"));
        int verdetto=0;
        
        while(film.readLine()!=null){
            contFilm++;
        }
        film.close();
       
        numScelta = Integer.parseInt(JOptionPane.showInputDialog("Inserire il numero corrispondente al film da acquistare (0 per uscire)"));
         
        while(numScelta!=0 && verdetto==0){
            while(numScelta!=0 && (numScelta<0 || numScelta>contFilm) && verdetto==0){
                numScelta = Integer.parseInt(JOptionPane.showInputDialog("Inserire il numero corrispondente al film da acquistare (0 per uscire)"));
            }
            if(numScelta!=0){               
                while((tempLista2=costofilm.readLine())!=null && numScelta!=cont){  
                     cont++;
                }            
                for(int i=0;i<tempLista2.length();i++){      
                    if(tempLista2.charAt(i)=='='){
                        temp=tempLista2.substring(i+1,tempLista2.length());
                        temp2=Integer.parseInt(temp);
                        if(temp2==0){ 
                            JOptionPane.showMessageDialog(null,"Prodotto non disponibile. Riprovare con un altro.","Errore",JOptionPane.ERROR_MESSAGE);
                            numScelta = Integer.parseInt(JOptionPane.showInputDialog("Inserire il numero corrispondente al film da acquistare (0 per uscire)"));
                        }
                        else{
                            verdetto=1;
                        }
                    }
                }
                cont=1;
                costofilm.close();
                costofilm = new BufferedReader(new FileReader("./CostiFilm.txt"));
            }
        }
              
        costofilm.close();
        if (numScelta==0){
            JOptionPane.showMessageDialog(null,"Grazie lo stesso per aver usato il nostro servizio","Finestra di Saluto",JOptionPane.INFORMATION_MESSAGE);
            return 0;
        }
        else{
            film = new BufferedReader(new FileReader("./Film.txt"));
            while((tempLista=film.readLine())!=null && contrigalista != numScelta){
            contrigalista++;
            }
            contrigalista=1;
            costofilm = new BufferedReader(new FileReader("./CostiFilm.txt"));
            while((tempLista2=costofilm.readLine())!=null && contrigalista != numScelta){
                contrigalista++;
            }
            
            JOptionPane.showMessageDialog(null,"Film scelto\n"+tempLista+" "+tempLista2);
            film.close();
            return numScelta;
        }
    }
}

class RicercaFilm implements Ricerca{
    
    private int[] array=new int[0]; 
    
    public int CercaFilm() throws IOException{
        
        BufferedReader film = new BufferedReader(new FileReader("./Film.txt"));
        String ricerca,x,t; int contriga=1; int contriga2=1; int contcercata=0; int conttrovate=0;
        String RisuRicerca=""; int[] temp=new int[0];
        
        ricerca=JOptionPane.showInputDialog("Inserire il titolo (o parte di esso) del film da cercare");
        
        while((x=film.readLine())!= null){
            contriga++;
            for (int i=0,j=0; i<ricerca.length() && j<x.length(); i++,j++){
               if(ricerca.charAt(i)==x.charAt(j) || ricerca.charAt(i)==x.charAt(j)+32 || ricerca.charAt(i)==x.charAt(j)-32){
                   contcercata++;
               }
               else{
                   contcercata=0;
                   i=-1;
               }
           }
            if(contcercata==ricerca.length()){
              conttrovate++;
              BufferedReader costofilm = new BufferedReader(new FileReader("./CostiFilm.txt"));
              while((t=costofilm.readLine())!=null && contriga2!=contriga-1){
                  contriga2++;
              }
              costofilm.close();
              RisuRicerca+=x+" "+t+"\n";
    
              for(int i=0;i<array.length;i++){         //creo l'array con i codici dei film trovati dalla ricerca
              temp[i]=array[i];
              }
              array=new int[conttrovate];
              for(int i=0;i<array.length-1;i++){
              array[i]=temp[i];
              }
              array[conttrovate-1]=contriga-1;
              temp=new int[array.length];
            }     
        }
        
        film.close();
        int RisultatoRicerca=conttrovate;
        
            if(RisultatoRicerca>0){
                JOptionPane.showMessageDialog(null,"Risultato ricerca\n"+RisuRicerca);
                return 1;
            }
            else{
                JOptionPane.showMessageDialog(null,"Nessun risultato trovato");
                return 0;
            } 
    }
    
    public int SceltaManuFilm() throws IOException{
        
        int numScelta; int verdetto=0; int contrigalista=1;
        BufferedReader costofilm = new BufferedReader(new FileReader("./CostiFilm.txt"));
        String tempLista,tempLista2; int cont=1; int temp2=-1; String temp="";
        boolean esistenza=false;
        
        numScelta = Integer.parseInt(JOptionPane.showInputDialog("Inserire il numero corrispondente al film da acquistare (0 per uscire)"));
        
        while(numScelta!=0 && verdetto==0){
            
            for(int i=0;i<array.length;i++){
                if(numScelta==array[i]){
                    esistenza=true;
                }
            }
            while(numScelta!=0 && esistenza==false && verdetto==0){
                numScelta = Integer.parseInt(JOptionPane.showInputDialog("Inserire il numero corrispondente al film da acquistare (0 per uscire)"));
                esistenza=false;
                for(int i=0;i<array.length;i++){
                    if(numScelta==array[i]){
                         esistenza=true;
                    }
                }
            }
            if(numScelta!=0){               
                while((tempLista2=costofilm.readLine())!=null && numScelta!=cont){  
                     cont++;
                }            
                for(int i=0;i<tempLista2.length();i++){      
                    if(tempLista2.charAt(i)=='='){
                        temp=tempLista2.substring(i+1,tempLista2.length());
                        temp2=Integer.parseInt(temp);
                        if(temp2==0){ 
                            JOptionPane.showMessageDialog(null,"Prodotto non disponibile. Riprovare con un altro.","Errore",JOptionPane.ERROR_MESSAGE);
                            numScelta = Integer.parseInt(JOptionPane.showInputDialog("Inserire il numero corrispondente al film da acquistare (0 per uscire)"));
                        }
                        else{
                            verdetto=1;
                        }
                    }
                }
                cont=1;
                costofilm.close();
                costofilm = new BufferedReader(new FileReader("./CostiFilm.txt"));
            }
        }
        
        costofilm.close();
        if (numScelta==0){
            JOptionPane.showMessageDialog(null,"Grazie lo stesso per aver usato il nostro servizio","Finestra di Saluto",JOptionPane.INFORMATION_MESSAGE);
            return 0;
        }
        else{
            BufferedReader film = new BufferedReader(new FileReader("./Film.txt"));
            while((tempLista=film.readLine())!=null && contrigalista != numScelta){
            contrigalista++;
            }
            contrigalista=1;
            costofilm = new BufferedReader(new FileReader("./CostiFilm.txt"));
            while((tempLista2=costofilm.readLine())!=null && contrigalista != numScelta){
                contrigalista++;
            }
            
            JOptionPane.showMessageDialog(null,"Film scelto\n"+tempLista+" "+tempLista2);
            film.close();
            costofilm.close();
            return numScelta;
        }
    }
}


//FACTORY

interface Aggiornamento{
    
    void SovrascriviFileFilm(int a) throws IOException;
    
    void SovrascriviFileGiochi(int a) throws IOException;
}

interface AggiornamentoCreator{
    
    public Aggiornamento getAggiornamento();
}

class Creator2 implements AggiornamentoCreator{
    
    public Aggiornamento getAggiornamento(){
        
        return new File();
    }
}

class File implements Aggiornamento{
    
    public void SovrascriviFileFilm(int a)throws IOException{
        
        int cont=0; String sotto; int convert; String sotto2;
        BufferedReader costofilm = new BufferedReader(new FileReader("./CostiFilm.txt"));
        while(costofilm.readLine()!=null){
            cont++;
        }
        costofilm.close();
        
        String[] temp=new String[cont]; //creo l'array di String dove inserire tutte e righe del file dei costi
        costofilm = new BufferedReader(new FileReader("./CostiFilm.txt"));
        for(int i=0;i<temp.length;i++){
            temp[i]=costofilm.readLine();
        }
        
        for(int i=0;i<temp[a-1].length();i++){
            if(temp[a-1].charAt(i)=='='){
                sotto=temp[a-1].substring(i+1,temp[a-1].length());
                convert=Integer.parseInt(sotto);
                convert--;
                sotto2=Integer.toString(convert);
                temp[a-1]=temp[a-1].substring(0,i)+"="+sotto2;
            }
        }
        
        costofilm.close();
        BufferedWriter nuovocostofilm = new BufferedWriter(new FileWriter("./CostiFilm.txt"));
        for(int i=0;i<temp.length;i++){
            if(i==temp.length-1){
            nuovocostofilm.write(temp[i]);
            }
            else{
            nuovocostofilm.write(temp[i]+"\n");   
            }
        }
        nuovocostofilm.close();
    }
    
    public void SovrascriviFileGiochi(int a) throws IOException{
        
        int cont=0; String sotto; int convert; String sotto2; int cifredisp=1;
        BufferedReader costogiochi = new BufferedReader(new FileReader("./CostoGiochi.txt"));
        while(costogiochi.readLine()!=null){
            cont++;
        }
        costogiochi.close();
        
        String[] temp=new String[cont]; //creo l'array di String dove inserire tutte e righe del file dei costi
        costogiochi = new BufferedReader(new FileReader("./CostoGiochi.txt"));
        for(int i=0;i<temp.length;i++){
            temp[i]=costogiochi.readLine();
        }
        
        for(int i=0;i<temp[a-1].length();i++){
            if(temp[a-1].charAt(i)=='='){
                sotto=temp[a-1].substring(i+1,i+2);
                if(temp[a-1].charAt(i+2)!=' '){
                    cifredisp++;
                    sotto+=temp[a-1].substring(i+2,i+3);
                }
                convert=Integer.parseInt(sotto);
                convert--;
                sotto2=Integer.toString(convert);
                if(cifredisp==1){
                    temp[a-1]=temp[a-1].substring(0,i)+"="+sotto2+temp[a-1].substring(i+2,temp[a-1].length());
                }
                else{
                    temp[a-1]=temp[a-1].substring(0,i)+"="+sotto2+temp[a-1].substring(i+3,temp[a-1].length());
                }
            }
        }
        costogiochi.close();
        
        BufferedWriter nuovocostogiochi = new BufferedWriter(new FileWriter("./CostoGiochi.txt"));
        for(int i=0;i<temp.length;i++){
            if(i==temp.length-1){
            nuovocostogiochi.write(temp[i]);
            }
            else{
            nuovocostogiochi.write(temp[i]+"\n");    
            }
        }
        nuovocostogiochi.close();
    }
}

//SINGLETON
class VideoGiochi {
    
    boolean[] array; String temp; String temp2;
    BufferedReader costogiochi;
    BufferedReader giochi;
    
    private static VideoGiochi g= new VideoGiochi();
    
    private VideoGiochi() {
        
        try{
        int Eta; int cont=0; int pegi;String Lista="";
        Eta=Integer.parseInt(JOptionPane.showInputDialog("Inserisci la tua eta'"));
        
                costogiochi = new BufferedReader(new FileReader("./CostoGiochi.txt"));
        
        while((temp=costogiochi.readLine())!=null){
            cont++;
        }
        costogiochi.close();
        array=new boolean[cont];
        costogiochi = new BufferedReader(new FileReader("./CostoGiochi.txt"));
        
        int contarray=0;
        while((temp=costogiochi.readLine())!=null){
            for(int i=0;i<temp.length();i++){
                if(temp.charAt(i)==':'){
                    temp2=temp.substring(i+1,temp.length());
                    pegi=Integer.parseInt(temp2);
                    if(Eta>=pegi){
                        array[contarray]=true;
                        contarray++;
                    }
                    else{
                        array[contarray]=false;
                        contarray++;
                    }
                }
            }
        }
        costogiochi.close();
        
        costogiochi = new BufferedReader(new FileReader("./CostoGiochi.txt"));
        giochi = new BufferedReader(new FileReader("./Giochi.txt"));
        cont=0;
        while((temp=giochi.readLine())!=null && (temp2=costogiochi.readLine())!=null){
            cont++;
            if(array[cont-1]==true){ 
                Lista+=temp+" "+temp2+"\n";
            }
        }
        costogiochi.close();
        giochi.close();
        JOptionPane.showMessageDialog(null,Lista);
        }
        
        catch(IOException ex){
        }
    }
    
    public int SceltaVideogioco() throws IOException{
        
        int verdetto=0; boolean esistenza=false; int cont=1;
        int numScelta=Integer.parseInt(JOptionPane.showInputDialog("Inserire il codice corrispondente al Videogioco da acquistare (0 se nessuno)"));
        String temp3=""; int convert; int contrigalista=1;
        
        while(numScelta!=0 && verdetto==0){
            
            if(array[numScelta-1]==true){
                esistenza=true;
            }
            
            while(numScelta!=0 && esistenza==false && verdetto==0){
                numScelta = Integer.parseInt(JOptionPane.showInputDialog("Inserire il numero corrispondente al Videogioco da acquistare (0 per uscire)"));
                esistenza=false;
                
                if(numScelta!=0){
                if(array[numScelta-1]==true){
                    esistenza=true;
                }
                }
            }
            
            if(numScelta!=0){ 
                    costogiochi = new BufferedReader(new FileReader("./CostoGiochi.txt"));
                    while((temp2=costogiochi.readLine())!=null && numScelta!=cont){  
                        cont++;
                    }
                    for(int i=0;i<temp2.length();i++){
                        if(temp2.charAt(i)=='='){
                           temp3+=temp2.substring(i+1,i+2);
                           if(temp2.charAt(i+2)!=' '){
                               temp3+=temp2.substring(i+2,i+3);
                           }
                           convert=Integer.parseInt(temp3);
                           if(convert==0){
                               JOptionPane.showMessageDialog(null,"Prodotto non disponibile. Riprovare con un altro.","Errore",JOptionPane.ERROR_MESSAGE);
                               numScelta = Integer.parseInt(JOptionPane.showInputDialog("Inserire il numero corrispondente al Videogioco da acquistare (0 per uscire)"));
                           }
                           else{
                               verdetto=1;
                           }
                        }
                    }
                    cont=1;
                    costogiochi.close();
                    costogiochi = new BufferedReader(new FileReader("./CostoGiochi.txt"));
                }
            }
        costogiochi.close();
        if (numScelta==0){
            JOptionPane.showMessageDialog(null,"Grazie lo stesso per aver usato il nostro servizio","Finestra di Saluto",JOptionPane.INFORMATION_MESSAGE);
            return 0;
        }
        else{
            giochi = new BufferedReader(new FileReader("./Giochi.txt"));
            while((temp=giochi.readLine())!=null && contrigalista != numScelta){
            contrigalista++;
            }
            contrigalista=1;
            costogiochi = new BufferedReader(new FileReader("./CostoGiochi.txt"));
            while((temp2=costogiochi.readLine())!=null && contrigalista != numScelta){
                contrigalista++;
            }
            
            String TitoloScelto=temp+" "+temp2;
            JOptionPane.showMessageDialog(null,"Videogioco Scelto\n"+TitoloScelto);
            giochi.close();
            costogiochi.close();
            return numScelta;
        }
    }
    
    public static VideoGiochi Instance() throws IOException{
        
        return g;
    }
}