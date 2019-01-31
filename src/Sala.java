
import java.util.ArrayList;
import javax.swing.JOptionPane;

class Sala {
    
    String nome;
    ArrayList<Jogador> jogadores;
    Baralho baralho;
    boolean começou;
    Jogador dealer;
    String nomeJogs;
    
    public Sala(String n) {
        this.nome = n;
        baralho = new Baralho();
        jogadores = new ArrayList();
        começou=false;
        nomeJogs = "";
    }
    
    public ArrayList<Jogador> getJogadores() {
        return jogadores;
    }
    
    public String getNome() {
        return this.nome;
    }
    
    public void setJogador(Jogador j) {
        if (jogadores.size() <= 4) {
            jogadores.add(j);
        } else {
            JOptionPane.showMessageDialog(null, "Sala cheia.");
        }
    }
    
    public Jogador removeJogador(String j) {
        for (int i = 0; i < jogadores.size(); i++) {
            if (jogadores.get(i).nome.equals(j)) {
                if (this.nomeJogs.contains(j)) {
                    this.nomeJogs = this.nomeJogs.replace(" " + j, "");
                }
                return jogadores.remove(i);
            }
        }
        return null;
    }
    
    public Jogador getJogador(String j) {
        for (int i = 0; i < jogadores.size(); i++) {
            if (jogadores.get(i).nome.equals(j)) {
                return jogadores.get(i);
            }
        }
        return null;
    }
    
    int getLugaresOcupados() {
        return jogadores.size();
    }

}
