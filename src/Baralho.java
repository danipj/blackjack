import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class Baralho implements Serializable {

    public final static int AS = 1;
    public final int VALETE = 11;
    public final int RAINHA = 12;
    public final int REI = 13;
    public ArrayList<Carta> cartas;
    
    public Baralho() {
        cartas = new ArrayList();        
        cartas.add(new Carta(2, "copas", "..\\bj\\2_copas.png"));
        cartas.add(new Carta(2, "ouros", "..\\bj\\2_ouros.png"));
        cartas.add(new Carta(2, "paus", "..\\bj\\2_paus.png"));
        cartas.add(new Carta(2, "espadas", "..\\bj\\2_espadas.png"));
        cartas.add(new Carta(AS, "copas", "..\\bj\\A_copas.png"));
        cartas.add(new Carta(AS, "ouros", "..\\bj\\A_ouros.png"));
         cartas.add(new Carta(3, "copas", "..\\bj\\3_copas.png"));
        cartas.add(new Carta(3, "ouros",  "..\\bj\\3_ouros.png"));
        cartas.add(new Carta(3, "paus", "..\\bj\\3_paus.png"));
        cartas.add(new Carta(3, "espadas", "..\\bj\\3_espadas.png"));
        cartas.add(new Carta(AS, "paus", "..\\bj\\A_paus.png"));
        cartas.add(new Carta(AS, "espadas", "..\\bj\\A_espadas.png"));       
       
        cartas.add(new Carta(4, "copas", "..\\bj\\4_copas.png"));
        cartas.add(new Carta(4, "ouros", "..\\bj\\4_ouros.png"));
        cartas.add(new Carta(4, "paus", "..\\bj\\4_paus.png"));
        cartas.add(new Carta(4, "espadas", "..\\bj\\4_espadas.png"));
        cartas.add(new Carta(5, "copas", "..\\bj\\5_copas.png"));
        cartas.add(new Carta(5, "ouros", "..\\bj\\5_ouros.png"));
        cartas.add(new Carta(5, "paus", "..\\bj\\5_paus.png"));
        cartas.add(new Carta(5, "espadas", "..\\bj\\5_espadas.png"));
        cartas.add(new Carta(6, "copas", "..\\bj\\6_copas.png"));
        cartas.add(new Carta(6, "ouros", "..\\bj\\6_ouros.png"));
        cartas.add(new Carta(6, "paus", "..\\bj\\6_paus.png"));
        cartas.add(new Carta(6, "espadas", "..\\bj\\6_espadas.png"));
        cartas.add(new Carta(7, "copas", "..\\bj\\7_copas.png"));
        cartas.add(new Carta(7, "ouros", "..\\bj\\7_ouros.png"));
        cartas.add(new Carta(7, "paus", "..\\bj\\7_paus.png"));
        cartas.add(new Carta(7, "espadas", "..\\bj\\7_espadas.png"));
        cartas.add(new Carta(8, "copas", "..\\bj\\8_copas.png"));
        cartas.add(new Carta(8, "ouros", "..\\bj\\8_ouros.png"));
        cartas.add(new Carta(8, "paus", "..\\bj\\8_paus.png"));
        cartas.add(new Carta(8, "espadas", "..\\bj\\8_espadas.png"));
        cartas.add(new Carta(9, "copas", "..\\bj\\9_copas.png"));
        cartas.add(new Carta(9, "ouros", "..\\bj\\9_ouros.png"));
        cartas.add(new Carta(9, "paus", "..\\bj\\9_paus.png"));
        cartas.add(new Carta(9, "espadas", "..\\bj\\9_espadas.png"));
        cartas.add(new Carta(10, "copas", "..\\bj\\10_copas.png"));
        cartas.add(new Carta(10, "ouros", "..\\bj\\10_ouros.png"));
        cartas.add(new Carta(10, "paus", "..\\bj\\10_paus.png"));
        cartas.add(new Carta(10, "espadas", "..\\bj\\10_espadas.png"));
        cartas.add(new Carta(VALETE, "copas", "..\\bj\\J_copas.png"));
        cartas.add(new Carta(VALETE, "ouros", "..\\bj\\J_ouros.png"));
        cartas.add(new Carta(VALETE, "paus", "..\\bj\\J_paus.png"));
        cartas.add(new Carta(VALETE, "espadas", "..\\bj\\J_espadas.png"));
        cartas.add(new Carta(RAINHA, "copas", "..\\bj\\Q_copas.png"));
        cartas.add(new Carta(RAINHA, "ouros", "..\\bj\\Q_ouros.png"));
        cartas.add(new Carta(RAINHA, "paus", "..\\bj\\Q_paus.png"));
        cartas.add(new Carta(RAINHA, "espadas", "..\\bj\\Q_espadas.png"));
        cartas.add(new Carta(REI, "copas", "..\\bj\\K_copas.png"));
        cartas.add(new Carta(REI, "ouros", "..\\bj\\K_ouros.png"));
        cartas.add(new Carta(REI, "paus", "..\\bj\\K_paus.png"));
        cartas.add(new Carta(REI, "espadas", "..\\bj\\K_espadas.png"));
       // Collections.shuffle(cartas);
    }

    public Carta sortearCarta() {
        return cartas.remove(0);
    }
}
