/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable;

/**
 *
 * @author u11161
 */
public class Carta implements Serializable {

    int valor;
    String imagem;
    String naipe;

    public Carta(int v, String n, String i) {
        this.valor = v;
        this.naipe = n;
        this.imagem = i;
    }
    
}
