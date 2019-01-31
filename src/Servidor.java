
import java.awt.HeadlessException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor extends Thread {

    private final static int NUMERO_PORTA = 10001;
    private static Sala[] salas = {new Sala("1"), new Sala("2"), new Sala("3"), new Sala("4"), new Sala("5")};
    static ArrayList<Jogador> jogadorSemSala = new ArrayList();
    public ObjectInputStream entrada;
    public ObjectOutputStream saida;

    public Servidor(Socket socket) {
        try {
            entrada = new ObjectInputStream(socket.getInputStream());
            saida = new ObjectOutputStream(socket.getOutputStream());

            //ja coloca um dealer em cada sala
            for (Sala sa : salas) {
                if (sa.jogadores.isEmpty()) {
                    String nome;
                    for (int i = 0; i < 5; i++) {
                        nome = "Dealer%" + String.valueOf(i + 1);
                        salas[i].setJogador(new Jogador(nome, saida));
                        salas[i].nomeJogs = nome;
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) throws Exception {

        try {
            ServerSocket server = new ServerSocket(NUMERO_PORTA);
            System.out.println("ServidorSocket rodando na porta " + NUMERO_PORTA);
            for (;;) {
                Socket socket = server.accept();
                Thread t1 = new Servidor(socket);
                String msg = (String) ((Servidor) t1).entrada.readObject();
                String[] s = msg.split("_");
                if (s[0].equals("Z")) {
                    Jogador j = new Jogador(s[1], ((Servidor) t1).saida);
                    jogadorSemSala.add(j);
                }
                t1.start();
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
    }

    public void enviaPraSala(int sala, String mensagem) {
        for (Jogador jo : salas[sala].jogadores) {
            String nome = jo.nome;
            String[] n = nome.split("%");
            if (!n[0].equals("Dealer")) {
                try {
                    jo.output.writeObject(mensagem);
                    jo.output.flush();
                } catch (IOException ex) {
                    Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void darCartas(int sala, String player) {
        int c = salas[sala - 1].baralho.sortearCarta().valor;
        String n = salas[sala - 1].baralho.sortearCarta().naipe;
        salas[sala - 1].getJogador(player).setCartaMao(c);
        if (c == 1) {
            salas[sala - 1].getJogador(player).tem1 = true;
        }
        System.out.println("Carta: " + c);
        enviaPraSala(sala - 1, "RESP_C_" + sala + "_" + player + "_" + c + "_" + n);
        String m = null;
        int soma = salas[sala - 1].getJogador(player).getPontos();
        System.out.println("Soma de " + player + ": " + soma);
        if (soma == 21) {
            m = "RESP_G_" + sala + "_" + player;
            salas[sala - 1].getJogador(player).status = 1; //1 21, 2 parou, 3 estourou
            enviaPraSala(sala - 1, m);
        } else if (soma > 21) {
            m = "RESP_X_" + sala + "_" + player;
            salas[sala - 1].getJogador(player).status = 3;
            enviaPraSala(sala - 1, m);
        }
    }

    public void dealerRound(int sala) {
        try {
            if (salas[sala - 1].getJogadores().get(1) != null && salas[sala - 1].getJogadores().get(1).status != 0
                    && salas[sala - 1].getJogadores().get(2) != null && salas[sala - 1].getJogadores().get(2).status != 0
                    && salas[sala - 1].getJogadores().get(3) != null && salas[sala - 1].getJogadores().get(3).status != 0) {
                //se todos os players tiverem alcançado 21 ou estourado ou parado
                int maior = 0;
                String qm = "";
                for (int i = 1; i < 4; i++) {
                    if (salas[sala - 1].getJogadores().get(i).getPontos() > maior) {
                        if (salas[sala - 1].getJogadores().get(i).getPontos() <= 21) {
                            qm = salas[sala - 1].getJogadores().get(i).nome;
                            maior = salas[sala - 1].getJogadores().get(i).getPontos();
                        }
                    }
                }
                System.out.println("maior" + maior);
                while ((salas[sala - 1].getJogador("Dealer%" + sala).getPontos() <= maior) && (salas[sala - 1].getJogador("Dealer%" + sala).getPontos() != 21)) {
                    darCartas(sala, "Dealer%" + sala);
                }
                if (salas[sala - 1].getJogador("Dealer%" + sala).status == 0) { //não fez 21, não estourou, mas é a maior pontuação
                    salas[sala - 1].getJogador("Dealer%" + sala).status = 2;
                    enviaPraSala(sala - 1, "RESP_G_" + sala + "_Dealer%" + sala);
                } else if (salas[sala - 1].getJogador("Dealer%" + sala).status == 3) { //estourado      
                    System.out.println("Quem ganhou???? " + qm);
                    enviaPraSala(sala - 1, "RESP_G_" + sala + "_" + qm);
                }
                salas[sala - 1].começou = false; //encerra a rodada
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                String mensagem = (String) entrada.readObject(); //StreamCorrupted é aqui.
                String[] a = mensagem.split("_");
                char cod = a[0].charAt(0);
                int sala = Integer.parseInt(a[1]);
                String player = a[2];
                switch (cod) {
                    case 'E':
                        System.out.println("SERVIDOR: Player " + player + " entrou na sala " + sala);
                        int aux = 0;
                        if (jogadorSemSala.size() >= 1) { //se houver jog sem sala
                            for (int i = 0; i < jogadorSemSala.size(); i++) { //percorre os sem salas
                                if (jogadorSemSala.get(i).nome.equals(player)) {
                                    aux = i;
                                }
                            }   //coloca o jog antes, daí ele tbm chama o atualizarJog la
                            salas[sala - 1].setJogador(jogadorSemSala.get(aux));
                            salas[sala - 1].nomeJogs += " " + player;
                            jogadorSemSala.remove(aux);
                            enviaPraSala(sala - 1, "RESP_" + mensagem);
                        }
                        break;
                    case 'S':
                        System.out.println("SERVIDOR: Player " + player + " saiu da sala " + sala);
                        jogadorSemSala.add(salas[sala - 1].removeJogador(player));
                        salas[sala - 1].nomeJogs = salas[sala - 1].nomeJogs.replace(" " + player, "");
                        jogadorSemSala.get(jogadorSemSala.size() - 1).mao = new ArrayList(); //zera a mão
                        for (Jogador jog : jogadorSemSala) {
                            jog.output.writeObject("RESP_" + mensagem);
                            jog.output.reset();
                        }
                        if (!salas[sala - 1].jogadores.isEmpty()) {
                            enviaPraSala(sala - 1, "RESP_" + mensagem);
                        }
                        break;
                    case 'C':
                        System.out.println("SERVIDOR: Player " + player + " pediu uma carta ");
                        darCartas(sala, player);
                        break;
                    case 'A':
                        System.out.println("SERVIDOR: atualizar jogadores pedido por " + player);
                        enviaPraSala(sala - 1, "RESP_" + mensagem + "_" + salas[sala - 1].nomeJogs);
                        break;
                    case 'P':
                        System.out.println("SERVIDOR: Player " + player + " parou ");
                        salas[sala - 1].getJogador(player).status = 2;
                        enviaPraSala(sala - 1, "RESP_" + mensagem);
                        break;
                    case 'J':
                        if (salas[sala - 1].getLugaresOcupados() == 4 && !salas[sala - 1].começou) //contando o dealer
                        {
                            for (Jogador jo : salas[sala - 1].jogadores) {
                                String nome = jo.nome;
                                darCartas(sala, nome); //da 2 cartas pra todos
                                darCartas(sala, nome);
                            }
                            salas[sala - 1].começou = true;
                        }
                        break;
                }
                dealerRound(sala);
            } catch (IOException | ClassNotFoundException | HeadlessException e) {
                System.err.println(e);
            }
        } //fim do while
    } //fim do run
} // fim class
