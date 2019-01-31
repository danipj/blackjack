
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Cliente extends javax.swing.JFrame {

    int qt = 0; //////////////////////////////////////////////////////////////////////////olha essa merda aqui
    int amor = 0;
    ObjectOutputStream saida;
    final int PORTA = 10001;
    ObjectInputStream entrada;
    Sala[] salas = {new Sala("1"), new Sala("2"), new Sala("3"), new Sala("4"), new Sala("5")};
    Receptor r = null;

    public Cliente() {

        initComponents();
        JLabel lblFundo = new JLabel();
        jPanel1.removeAll();
        jPanel1.add(lblBj);
        jPanel1.add(lblSalas);
        jPanel1.add(jScrollPane1); //CHORAY!!! AMEM IRMAO OH GLORIA
        jPanel1.add(btnEntrar);
        jPanel1.add(lblApel);
        jPanel1.add(lblNomeApel);
        jPanel1.revalidate();
        jPanel1.repaint();
        txtPlayers.setText("");
        txtPlayers.setContentType("text/html");

        Socket socket;

        for (;;) {
            try {
                //String ip = "10.0.4.42";
                String ip = InetAddress.getLocalHost().getHostAddress();
                socket = new Socket(ip, PORTA);
                saida = new ObjectOutputStream(socket.getOutputStream());
                entrada = new ObjectInputStream(socket.getInputStream());

                String n = JOptionPane.showInputDialog(null, "Escolha um apelido:");
                if (n != null) {
                    saida.writeObject("Z_" + n);
                    saida.reset();

                    System.out.println("CLIENTE: Player " + n + " conectado à porta " + PORTA);
                    lblNomeApel.setText(n);

                    r = new Receptor(entrada);
                    r.start();
                    break;
                } else {
                    JOptionPane.showMessageDialog(null, "Ok! Volte sempre (:");
                    System.exit(0);
                    return;
                }
            } catch (IOException ex) {
                System.err.println("Não encontrado!");
                continue;
            }
        }
    }

    private class Receptor extends Thread {

        ObjectInputStream entrada;

        public Receptor(ObjectInputStream en) {
            this.entrada = en;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    String mensagem = ((String) entrada.readObject());
                    String[] a = mensagem.split("_");
                    char cod = a[1].charAt(0);
                    int sala = Integer.parseInt(a[2]);
                    String player = a[3];
                    if (a[0].equals("RESP")) {
                        switch (cod) {
                            case 'E':
                                System.out.println("CLIENTE: Player " + player + " entrou na sala " + sala);
                                if (salas[sala - 1].getJogador(player) == null) {
                                    salas[sala - 1].setJogador(new Jogador(player));
                                }
                                atualizarJog(sala);
                                if (salas[sala - 1].getLugaresOcupados() != 4) {
                                    lblStatus.setText("Aguarde todos os jogadores entrarem");
                                } else //começo do jogo
                                {
                                    jPanel1.add(btnPedir);
                                    jPanel1.add(btnParar);
                                    jPanel1.remove(btnSair);
                                    jPanel1.revalidate();
                                    jPanel1.repaint();
                                }
                                break;
                            case 'S':
                                System.out.println("CLIENTE: Player " + player + " saiu da sala " + sala);
                                salas[sala - 1].removeJogador(player);
                                atualizarJog(sala);
                                break;
                            case 'C':
                                System.out.println("CLIENTE: Player " + player + " pediu uma carta e recebeu: " + a[4] + " de " + a[5]);
                                if (salas[sala - 1].getJogador(player) == null) {
                                    salas[sala - 1].setJogador(new Jogador(player));
                                }
                                if (a[4].equals("1")) {
                                    salas[sala - 1].getJogador(player).tem1 = true;
                                    System.out.println("Tenho às (: by:"+player);
                                }                                
                                Jogador jogAtual = salas[sala - 1].getJogador(player);
                                jogAtual.setCartaMao(Integer.parseInt(a[4]));
                                int qtsCartas = salas[sala - 1].getJogador(player).mao.size();
                                String[] s = player.split("%");
                                if (lblNomeApel.getText().equals(player)) { //se for eu
                                    lblStatus.setText("Pontos: " + jogAtual.getPontos());
                                }
                                String imgCarta = "/img/bj/" + a[4] + "_" + a[5] + ".png";
                                if (lblJog2.getText().equals(player)) {
                                    switch (qtsCartas) {
                                        case 1:
                                            jPanel1.add(lblC1);
                                            lblC1.setBounds(100, 500, 64, 92);
                                            lblC1.setIcon(new ImageIcon(getClass().getResource(imgCarta)));
                                            break;
                                        case 2:
                                            jPanel1.add(lblC2);
                                            lblC2.setBounds(125, 500, 64, 92);
                                            lblC2.setIcon(new ImageIcon(getClass().getResource(imgCarta)));
                                            break;
                                        case 3:
                                            jPanel1.add(lblC3);
                                            lblC3.setBounds(150, 500, 64, 92);
                                            lblC3.setIcon(new ImageIcon(getClass().getResource(imgCarta)));
                                            break;
                                        case 4:
                                            jPanel1.add(lblC4);
                                            lblC4.setBounds(175,500,64, 92);
                                            lblC4.setIcon(new ImageIcon(getClass().getResource(imgCarta)));
                                            break;
                                        case 5:
                                            jPanel1.add(lblC5);
                                            lblC5.setBounds(200,500,64, 92);
                                            lblC5.setIcon(new ImageIcon(getClass().getResource(imgCarta)));
                                            break;
                                        case 6:
                                            jPanel1.add(lblC6);
                                            lblC6.setBounds(225,500,64, 92);
                                            lblC6.setIcon(new ImageIcon(getClass().getResource(imgCarta)));
                                            break;
                                    }
                                } else if (s[0] != null && s[0].equals("Dealer")) {
                                    switch (qtsCartas) {
                                        case 1:
                                            jPanel1.add(lblC24);
                                            lblC24.setBounds(505,238,64, 92);
                                            lblC24.setIcon(new ImageIcon(getClass().getResource(imgCarta)));
                                            break;
                                        case 2:
                                            jPanel1.add(lblC23);
                                            lblC23.setBounds(530,238,64, 92);
                                            lblC23.setIcon(new ImageIcon(getClass().getResource(imgCarta)));
                                            break;
                                        case 3:
                                            jPanel1.add(lblC22);
                                            lblC22.setBounds(555,238,64, 92);
                                            lblC22.setIcon(new ImageIcon(getClass().getResource(imgCarta)));
                                            break;
                                        case 4:
                                            jPanel1.add(lblC21);
                                            lblC21.setBounds(580,238,64, 92);
                                            lblC21.setIcon(new ImageIcon(getClass().getResource(imgCarta)));
                                            break;
                                        case 5:
                                            jPanel1.add(lblC20);
                                            lblC20.setBounds(605,238,64, 92);
                                            lblC20.setIcon(new ImageIcon(getClass().getResource(imgCarta)));
                                            break;
                                        case 6:
                                            jPanel1.add(lblC19);
                                            lblC19.setBounds(630,238,64, 92);
                                            lblC19.setIcon(new ImageIcon(getClass().getResource(imgCarta)));
                                            break;
                                    }
                                } else if (lblJog3.getText().equals(player)) {
                                    switch (qtsCartas) {
                                        case 1:
                                            jPanel1.add(lblC12);
                                            lblC12.setBounds(865,512,64, 92);
                                            lblC12.setIcon(new ImageIcon(getClass().getResource(imgCarta)));
                                            break;
                                        case 2:
                                            jPanel1.add(lblC11);
                                            lblC11.setBounds(895,512,64, 92);
                                            lblC11.setIcon(new ImageIcon(getClass().getResource(imgCarta)));
                                            break;
                                        case 3:
                                            jPanel1.add(lblC10);
                                            lblC10.setBounds(920,512,64, 92);
                                            lblC10.setIcon(new ImageIcon(getClass().getResource(imgCarta)));
                                            break;
                                        case 4:
                                            jPanel1.add(lblC9);
                                            lblC9.setBounds(945,512,64, 92);
                                            lblC9.setIcon(new ImageIcon(getClass().getResource(imgCarta)));
                                            break;
                                        case 5:
                                            jPanel1.add(lblC8);
                                            lblC8.setBounds(970,512,64, 92);
                                            lblC8.setIcon(new ImageIcon(getClass().getResource(imgCarta)));
                                            break;
                                        case 6:
                                            jPanel1.add(lblC7);
                                            lblC7.setBounds(995,512,64, 92);
                                            lblC7.setIcon(new ImageIcon(getClass().getResource(imgCarta)));
                                            break;
                                    }
                                } else if (lblJog1.getText().equals(player)) {
                                    switch (qtsCartas) {
                                        case 1:
                                            jPanel1.add(lblC18);
                                            lblC18.setBounds(485,580,64, 92);
                                            lblC18.setIcon(new ImageIcon(getClass().getResource(imgCarta)));
                                            break;
                                        case 2:
                                            jPanel1.add(lblC17);
                                            lblC17.setBounds(510,580,64, 92);
                                            lblC17.setIcon(new ImageIcon(getClass().getResource(imgCarta)));
                                            break;
                                        case 3:
                                            jPanel1.add(lblC16);
                                            lblC16.setBounds(535,580,64, 92);
                                            lblC16.setIcon(new ImageIcon(getClass().getResource(imgCarta)));
                                            break;
                                        case 4:
                                            jPanel1.add(lblC15);
                                            lblC15.setBounds(560,580,64, 92);
                                            lblC15.setIcon(new ImageIcon(getClass().getResource(imgCarta)));
                                            break;
                                        case 5:
                                            jPanel1.add(lblC14);
                                            lblC14.setBounds(585,580,64, 92);
                                            lblC14.setIcon(new ImageIcon(getClass().getResource(imgCarta)));
                                            break;
                                        case 6:
                                            jPanel1.add(lblC13);
                                            lblC13.setBounds(610,580,64, 92);
                                            lblC13.setIcon(new ImageIcon(getClass().getResource(imgCarta)));
                                            break;
                                    }
                                }
                                break;

                            case 'A':
                                StringBuffer strBuf = new StringBuffer();
                                strBuf.append("<html>");
                                String[] jogs = a[4].split(" ");
                                for (int i = 0; i < jogs.length; i++) {
                                    strBuf.append(jogs[i]).append("<br>");
                                }
                                if (!strBuf.equals("<html>")) {
                                    strBuf.append("</html>");
                                }
                                txtPlayers.setText(strBuf + "");
                                for (int i = 0; i < jogs.length; i++) {
                                    if (salas[sala - 1].getJogador(jogs[i]) == null) {
                                        salas[sala - 1].setJogador(new Jogador(jogs[i]));
                                    }
                                }
                                if (jogs.length == 2) {
                                    jPanel1.add(lblJog1);
                                    lblJog1.setText(jogs[1]);
                                }
                                if (jogs.length == 3) {
                                    jPanel1.add(lblJog1);
                                    jPanel1.add(lblJog2);
                                    lblJog1.setText(jogs[1]);
                                    lblJog2.setText(jogs[2]);
                                }
                                if (jogs.length == 4) {
                                    if (jogs[3].equals(lblNomeApel.getText())) { //se eu for o último                                       
                                        saida.writeObject("J_" + sala + "_" + player);
                                        saida.flush();
                                        jPanel1.add(btnPedir);
                                        jPanel1.add(btnParar);
                                        jPanel1.remove(btnSair);
                                        jPanel1.revalidate();
                                        jPanel1.repaint();
                                    }
                                    jPanel1.add(lblJog1);
                                    jPanel1.add(lblJog2);
                                    jPanel1.add(lblJog3);
                                    lblJog1.setText(jogs[1]);
                                    lblJog2.setText(jogs[2]);
                                    lblJog3.setText(jogs[3]);
                                }
                                break;
                            case 'P':
                                System.out.println("CLIENTE: Player " + player + " parou ");
                                if (lblNomeApel.getText().equals(player)) {
                                    lblStatus.setText("Você parou");
                                    jPanel1.add(lblStatus);
                                    jPanel1.remove(btnPedir);
                                    jPanel1.remove(btnParar);
                                    jPanel1.revalidate();
                                    jPanel1.repaint();
                                }
                                break;
                            case 'G':
                                System.out.println("CLIENTE: Player " + player + " ganhou ");
                                qt++;
                                if (lblNomeApel.getText().equals(player)) {
                                    //  lblStatus.setText("Você ganhou");
                                    JOptionPane.showMessageDialog(null, "Você ganhou!");
//                                    jPanel1.add(lblStatus);
//                                    jPanel1.remove(btnPedir);
//                                    jPanel1.remove(btnParar);
//                                    jPanel1.revalidate();
//                                    jPanel1.repaint();
                                    sairDaSala();
                                } else {
                                    //lblStatus.setText("Player " + player + " ganhou");
                                    JOptionPane.showMessageDialog(null, player + " ganhou!");
                                    sairDaSala();
                                }
                                break;
                            case 'X':
                                System.out.println("CLIENTE: Player " + player + " estourou ");
                                if (lblNomeApel.getText().equals(player)) {//se foi você: fim de jogo, tchau botões
                                    lblStatus.setText("Você estourou");
                                    jPanel1.add(lblStatus);
                                    jPanel1.remove(btnPedir);
                                    jPanel1.remove(btnParar);
                                    jPanel1.revalidate();
                                    jPanel1.repaint();
                                }
                                break;
                        }
                    }
                } catch (IOException | ClassNotFoundException | HeadlessException e) {
                }
            }
        }

        private void atualizarJog(int sala) {
            String cod = "A_" + sala + "_" + lblNomeApel.getText();
            try {
                saida.writeObject(cod);
                saida.reset();
            } catch (IOException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                System.err.println("n escreveu nd");
            }
        }
    }///////////////////fim da classe Receptor

    public void novaCarta(int sala, String nome) throws IOException {
        String m = "C_" + sala + "_" + nome;
        try {
            saida.writeObject(m);
            saida.reset();
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("n escreveu nd");
        }
    }

    public void sairDaSala() {
        String cod = "S_" + lblNomeSala.getText() + "_" + lblNomeApel.getText();
        ((Fundo) jPanel1).pode = false;
        try {
            saida.writeObject(cod);
            saida.reset();

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        jPanel1.remove(btnSair);
        jPanel1.remove(lblSala);
        jPanel1.remove(lblPlayers);
        jPanel1.remove(lblNomeSala);
        jPanel1.remove(btnPedir);
        jPanel1.remove(btnParar);
        jPanel1.remove(jScrollPane2);
        jPanel1.remove(lblStatus);

        jPanel1.remove(lblC1);
        jPanel1.remove(lblC2);
        jPanel1.remove(lblC3);
        jPanel1.remove(lblC6);
        jPanel1.remove(lblC5);
        jPanel1.remove(lblC4);

        jPanel1.remove(lblC7);
        jPanel1.remove(lblC8);
        jPanel1.remove(lblC9);
        jPanel1.remove(lblC10);
        jPanel1.remove(lblC11);
        jPanel1.remove(lblC12);

        jPanel1.remove(lblC13);
        jPanel1.remove(lblC14);
        jPanel1.remove(lblC15);
        jPanel1.remove(lblC16);
        jPanel1.remove(lblC17);
        jPanel1.remove(lblC18);

        jPanel1.remove(lblC19);
        jPanel1.remove(lblC20);
        jPanel1.remove(lblC21);
        jPanel1.remove(lblC22);
        jPanel1.remove(lblC23);
        jPanel1.remove(lblC24);

        jPanel1.remove(lblJog1);
        jPanel1.remove(lblJog2);
        jPanel1.remove(lblDealer);
        jPanel1.remove(lblJog3);
        jPanel1.remove(jButton1);
        jPanel1.add(jScrollPane1);
        jPanel1.add(btnEntrar);
        jList1.setVisible(true);

        jPanel1.revalidate();
        jPanel1.repaint();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new Fundo();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        btnEntrar = new javax.swing.JButton();
        lblApel = new javax.swing.JLabel();
        lblSalas = new javax.swing.JLabel();
        lblBj = new javax.swing.JLabel();
        lblNomeApel = new javax.swing.JLabel();
        btnSair = new javax.swing.JButton();
        lblSala = new javax.swing.JLabel();
        lblNomeSala = new javax.swing.JLabel();
        btnPedir = new javax.swing.JButton();
        btnParar = new javax.swing.JButton();
        lblPlayers = new javax.swing.JLabel();
        lblBg = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtPlayers = new javax.swing.JEditorPane();
        lblStatus = new javax.swing.JLabel();
        lblJog1 = new javax.swing.JLabel();
        lblDealer = new javax.swing.JLabel();
        lblJog3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        lblJog2 = new javax.swing.JLabel();
        jLPane2 = new javax.swing.JLayeredPane();
        lblC1 = new javax.swing.JLabel();
        lblC2 = new javax.swing.JLabel();
        lblC3 = new javax.swing.JLabel();
        lblC4 = new javax.swing.JLabel();
        lblC5 = new javax.swing.JLabel();
        lblC6 = new javax.swing.JLabel();
        jLPaneDealer = new javax.swing.JLayeredPane();
        lblC21 = new javax.swing.JLabel();
        lblC19 = new javax.swing.JLabel();
        lblC20 = new javax.swing.JLabel();
        lblC24 = new javax.swing.JLabel();
        lblC23 = new javax.swing.JLabel();
        lblC22 = new javax.swing.JLabel();
        jLPane1 = new javax.swing.JLayeredPane();
        lblC13 = new javax.swing.JLabel();
        lblC14 = new javax.swing.JLabel();
        lblC15 = new javax.swing.JLabel();
        lblC16 = new javax.swing.JLabel();
        lblC17 = new javax.swing.JLabel();
        lblC18 = new javax.swing.JLabel();
        jLPane3 = new javax.swing.JLayeredPane();
        lblC12 = new javax.swing.JLabel();
        lblC11 = new javax.swing.JLabel();
        lblC10 = new javax.swing.JLabel();
        lblC9 = new javax.swing.JLabel();
        lblC8 = new javax.swing.JLabel();
        lblC7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1210, 750));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(null);

        jPanel1.setBackground(new java.awt.Color(0, 51, 0));
        jPanel1.setMinimumSize(new java.awt.Dimension(1210, 715));
        jPanel1.setPreferredSize(new java.awt.Dimension(1207, 715));

        jList1.setBackground(new java.awt.Color(0, 51, 0));
        jList1.setForeground(new java.awt.Color(255, 255, 255));
        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Sala_1", "Sala_2", "Sala_3", "Sala_4", "Sala_5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList1.setSelectionBackground(new java.awt.Color(204, 255, 204));
        jScrollPane1.setViewportView(jList1);

        btnEntrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Button-Next-icon.png"))); // NOI18N
        btnEntrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEntrarActionPerformed(evt);
            }
        });

        lblApel.setForeground(new java.awt.Color(255, 255, 255));
        lblApel.setText("Apelido:");

        lblSalas.setForeground(new java.awt.Color(255, 255, 255));
        lblSalas.setText("Salas");

        lblBj.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblBj.setForeground(new java.awt.Color(255, 255, 255));
        lblBj.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/nome.png"))); // NOI18N

        lblNomeApel.setForeground(new java.awt.Color(255, 255, 255));
        lblNomeApel.setText("apelidinho");

        btnSair.setText("Sair da sala");
        btnSair.setToolTipText("");
        btnSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSairActionPerformed(evt);
            }
        });

        lblSala.setForeground(new java.awt.Color(255, 255, 255));
        lblSala.setText("Sala:");

        lblNomeSala.setForeground(new java.awt.Color(255, 255, 255));
        lblNomeSala.setText("oi");
        lblNomeSala.setToolTipText("");

        btnPedir.setText("Pedir Carta");
        btnPedir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPedirActionPerformed(evt);
            }
        });

        btnParar.setText("Parar");
        btnParar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPararActionPerformed(evt);
            }
        });

        lblPlayers.setForeground(new java.awt.Color(255, 255, 255));
        lblPlayers.setText("Players:");

        jScrollPane2.setViewportView(txtPlayers);

        lblStatus.setForeground(new java.awt.Color(255, 255, 255));
        lblStatus.setText("status");

        lblJog1.setForeground(new java.awt.Color(255, 255, 255));
        lblJog1.setText("jLabel1");

        lblDealer.setForeground(new java.awt.Color(255, 255, 255));
        lblDealer.setText("Dealer");

        lblJog3.setForeground(new java.awt.Color(255, 255, 255));
        lblJog3.setText("jLabel3");

        jButton1.setText("apostar amor");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        lblJog2.setForeground(new java.awt.Color(255, 255, 255));
        lblJog2.setText("jLabel4");

        lblC1.setBounds(0, 0, 89, 113);
        jLPane2.add(lblC1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        lblC2.setBounds(40, 0, 82, 113);
        jLPane2.add(lblC2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        lblC3.setBounds(100, 0, 75, 110);
        jLPane2.add(lblC3, javax.swing.JLayeredPane.DEFAULT_LAYER);
        lblC4.setBounds(160, 0, 89, 113);
        jLPane2.add(lblC4, javax.swing.JLayeredPane.DEFAULT_LAYER);
        lblC5.setBounds(210, 0, 82, 113);
        jLPane2.add(lblC5, javax.swing.JLayeredPane.DEFAULT_LAYER);
        lblC6.setBounds(290, 0, 75, 110);
        jLPane2.add(lblC6, javax.swing.JLayeredPane.DEFAULT_LAYER);

        lblC21.setBounds(240, 0, 80, 110);
        jLPaneDealer.add(lblC21, javax.swing.JLayeredPane.DEFAULT_LAYER);
        lblC19.setBounds(280, 0, 80, 110);
        jLPaneDealer.add(lblC19, javax.swing.JLayeredPane.DEFAULT_LAYER);
        lblC20.setBounds(240, 0, 80, 110);
        jLPaneDealer.add(lblC20, javax.swing.JLayeredPane.DEFAULT_LAYER);
        lblC24.setBounds(0, 0, 89, 113);
        jLPaneDealer.add(lblC24, javax.swing.JLayeredPane.DEFAULT_LAYER);
        lblC23.setBounds(60, 0, 82, 113);
        jLPaneDealer.add(lblC23, javax.swing.JLayeredPane.DEFAULT_LAYER);
        lblC22.setBounds(140, 0, 80, 110);
        jLPaneDealer.add(lblC22, javax.swing.JLayeredPane.DEFAULT_LAYER);

        lblC13.setBounds(0, 0, 0, 0);
        jLPane1.add(lblC13, javax.swing.JLayeredPane.DEFAULT_LAYER);
        lblC14.setBounds(0, 0, 0, 0);
        jLPane1.add(lblC14, javax.swing.JLayeredPane.DEFAULT_LAYER);
        lblC15.setBounds(0, 0, 0, 0);
        jLPane1.add(lblC15, javax.swing.JLayeredPane.DEFAULT_LAYER);
        lblC16.setBounds(0, 0, 0, 0);
        jLPane1.add(lblC16, javax.swing.JLayeredPane.DEFAULT_LAYER);
        lblC17.setBounds(0, 0, 0, 0);
        jLPane1.add(lblC17, javax.swing.JLayeredPane.DEFAULT_LAYER);
        lblC18.setBounds(0, 20, 89, 113);
        jLPane1.add(lblC18, javax.swing.JLayeredPane.DEFAULT_LAYER);

        lblC12.setBounds(0, 0, 0, 0);
        jLPane3.add(lblC12, javax.swing.JLayeredPane.DEFAULT_LAYER);
        lblC11.setBounds(0, 0, 0, 0);
        jLPane3.add(lblC11, javax.swing.JLayeredPane.DEFAULT_LAYER);
        lblC10.setBounds(0, 0, 0, 0);
        jLPane3.add(lblC10, javax.swing.JLayeredPane.DEFAULT_LAYER);
        lblC9.setBounds(0, 0, 0, 0);
        jLPane3.add(lblC9, javax.swing.JLayeredPane.DEFAULT_LAYER);
        lblC8.setBounds(0, 0, 0, 0);
        jLPane3.add(lblC8, javax.swing.JLayeredPane.DEFAULT_LAYER);
        lblC7.setBounds(0, 0, 0, 0);
        jLPane3.add(lblC7, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(146, 146, 146)
                        .addComponent(lblSalas)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblBg))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(342, 342, 342)
                                .addComponent(lblDealer))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap(21, Short.MAX_VALUE)
                        .addComponent(lblStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 353, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(60, 60, 60)
                        .addComponent(lblBj)
                        .addGap(469, 469, 469))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(142, 142, 142)
                        .addComponent(lblJog2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblJog3)
                        .addGap(164, 164, 164))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(105, 105, 105)
                        .addComponent(jLPaneDealer, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(40, 40, 40))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addComponent(jLPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblJog1)
                                .addGap(155, 155, 155)))
                        .addComponent(jLPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnParar, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(btnPedir)
                    .addComponent(lblPlayers)
                    .addComponent(btnSair)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblSala)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblNomeSala, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblApel)
                    .addComponent(btnEntrar)
                    .addComponent(lblNomeApel)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblStatus)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblBj)
                        .addGap(49, 49, 49)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblSalas)
                            .addComponent(lblDealer))))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(85, 85, 85)
                                .addComponent(lblBg))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLPaneDealer, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(68, 68, 68)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblJog2)
                                    .addComponent(lblJog3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(177, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblJog1)
                                .addGap(29, 29, 29)
                                .addComponent(jLPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(43, 43, 43))))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSala)
                    .addComponent(lblNomeSala))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSair)
                .addGap(130, 130, 130)
                .addComponent(lblPlayers)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(btnPedir)
                .addGap(11, 11, 11)
                .addComponent(btnParar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addGap(79, 79, 79)
                .addComponent(lblApel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblNomeApel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEntrar)
                .addContainerGap())
        );

        getContentPane().add(jPanel1);
        jPanel1.setBounds(0, 0, 1210, 740);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
    }//GEN-LAST:event_formWindowClosed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (amor == 0) {
            jButton1.setText("<3");
        } else if (amor == 6) {
            jButton1.setText("<3<3");
        } else if (amor == 12) {
            jButton1.setText("<3<3<3");
        } else {
            jButton1.setText(jButton1.getText() + "3");
        }
        amor++;
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnPararActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPararActionPerformed
        String cod = "P_" + lblNomeSala.getText() + "_" + lblNomeApel.getText();
        try {
            saida.writeObject(cod);
            saida.reset();
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnPararActionPerformed

    private void btnPedirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPedirActionPerformed
        try {
            novaCarta(Integer.parseInt(lblNomeSala.getText()), lblNomeApel.getText());
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnPedirActionPerformed

    private void btnSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSairActionPerformed
        sairDaSala();
    }//GEN-LAST:event_btnSairActionPerformed

    private void btnEntrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEntrarActionPerformed
        if (jList1.isSelectionEmpty()) {
            JOptionPane.showMessageDialog(null, "Escolha uma sala!");
        } else {
            String pegaSala = (String) jList1.getSelectedValue();
            String[] n = pegaSala.split("_");
            String cod = "E_" + n[1] + "_" + lblNomeApel.getText();
            try {
                saida.writeObject(cod);
                saida.reset();

                jPanel1.remove(jScrollPane1);
                jPanel1.remove(btnEntrar);
                jPanel1.remove(lblSalas);
                jPanel1.add(btnSair);
                jPanel1.add(lblSala);
                jPanel1.add(lblNomeSala);
                jPanel1.add(lblDealer);
                lblNomeSala.setText(n[1]);
                jPanel1.add(lblPlayers);
                jPanel1.add(jScrollPane2);
                jPanel1.add(lblStatus);
                jPanel1.add(jButton1);

                ((Fundo) jPanel1).pode = true;
                jPanel1.repaint();

            } catch (IOException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnEntrarActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        sairDaSala();
    }//GEN-LAST:event_formWindowClosing

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;










                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Cliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Cliente().setVisible(true);
            }
        });
    }

    public class Fundo extends javax.swing.JPanel {

        BufferedImage b;
        Rectangle2D rect;
        public volatile boolean pode = false;

        public Fundo() {
            try {
                b = ImageIO.read(getClass().getResourceAsStream("/img/mesa.png"));
                rect = new Rectangle(0, 0, 550, 412);
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
            }
        }

        @Override
        public void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            if (pode) {
                rect = new Rectangle(0, 0, this.getWidth(), this.getHeight());

                TexturePaint p = new TexturePaint(b, rect);
                g2.setPaint(p);
                g2.fillRect(0, 0, this.getWidth(), this.getHeight());
                return;
            }
            g2.setColor(new Color(0, 51, 0));
            g2.fillRect(0, 0, this.getWidth(), this.getHeight());
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEntrar;
    private javax.swing.JButton btnParar;
    private javax.swing.JButton btnPedir;
    private javax.swing.JButton btnSair;
    private javax.swing.JButton jButton1;
    private javax.swing.JLayeredPane jLPane1;
    private javax.swing.JLayeredPane jLPane2;
    private javax.swing.JLayeredPane jLPane3;
    private javax.swing.JLayeredPane jLPaneDealer;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblApel;
    private javax.swing.JLabel lblBg;
    private javax.swing.JLabel lblBj;
    private javax.swing.JLabel lblC1;
    private javax.swing.JLabel lblC10;
    private javax.swing.JLabel lblC11;
    private javax.swing.JLabel lblC12;
    private javax.swing.JLabel lblC13;
    private javax.swing.JLabel lblC14;
    private javax.swing.JLabel lblC15;
    private javax.swing.JLabel lblC16;
    private javax.swing.JLabel lblC17;
    private javax.swing.JLabel lblC18;
    private javax.swing.JLabel lblC19;
    private javax.swing.JLabel lblC2;
    private javax.swing.JLabel lblC20;
    private javax.swing.JLabel lblC21;
    private javax.swing.JLabel lblC22;
    private javax.swing.JLabel lblC23;
    private javax.swing.JLabel lblC24;
    private javax.swing.JLabel lblC3;
    private javax.swing.JLabel lblC4;
    private javax.swing.JLabel lblC5;
    private javax.swing.JLabel lblC6;
    private javax.swing.JLabel lblC7;
    private javax.swing.JLabel lblC8;
    private javax.swing.JLabel lblC9;
    private javax.swing.JLabel lblDealer;
    private javax.swing.JLabel lblJog1;
    private javax.swing.JLabel lblJog2;
    private javax.swing.JLabel lblJog3;
    private javax.swing.JLabel lblNomeApel;
    private javax.swing.JLabel lblNomeSala;
    private javax.swing.JLabel lblPlayers;
    private javax.swing.JLabel lblSala;
    private javax.swing.JLabel lblSalas;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JEditorPane txtPlayers;
    // End of variables declaration//GEN-END:variables
}
