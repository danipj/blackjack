
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Jogador {

    String nome;
    ArrayList<Integer> mao;
    ObjectOutputStream output;
    int status; //1 21, 2 parou, 3 estourou
    boolean tem1 = false;
    

    public Jogador(String n) {
        this.nome = n;
    }

    public Jogador(String n, ObjectOutputStream out) {
        this.nome = n;
        mao = new ArrayList();
        this.output = out;
        status = 0;
    }

    public int getPontos() {
        int s = 0;
        for (int i = 0; i < mao.size(); i++) {
            if (mao.get(i) > 10) {
                s += 10;
            } else {
                s += mao.get(i);
            }
        }
        if (tem1 && s <= 11) {
            s += 10;
        }
        System.out.println("Soma no jogador "+this.nome+": " +s);
        return s;
    }

    public void setCartaMao(int carta) {
        if (mao == null) {
            mao = new ArrayList();
            tem1 = false;
        }
        mao.add(carta);
    }
}
