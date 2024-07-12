/**
 * @author APS Dev (Angelo Polito, Paolo Scicutella, Simone Pugliese)
 */
package map.adventure.elonandmartians.swing;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;

import map.adventure.elonandmartians.socket.client.ErrorReport;
import map.adventure.elonandmartians.game.Engine;
import map.adventure.elonandmartians.game.Utils;
import map.adventure.elonandmartians.database.Database;
import map.adventure.elonandmartians.game.ElonAndMartians;
import map.adventure.elonandmartians.game.GameDescription;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.SQLException;

import map.adventure.elonandmartians.thread.MusicPlayer;
import map.adventure.elonandmartians.thread.Time;

/**
 * Classe principale dell'applicazione "Elon and Martians".
 * Estende {@link javax.swing.JFrame} per creare l'interfaccia grafica.
 * Gestisce l'inizializzazione e il funzionamento del gioco, inclusa la musica, il timer e l'input dell'utente.
 */
public class App extends javax.swing.JFrame {
    private Engine engine;
    private JTextField inputTextField;
    private JWindow backgroundWindow;
    private MusicPlayer musicPlayer;
    private Thread musicThread;
    static Time.TimerCallback callback = new TimeUpdateListener();
    static Time time = new Time(callback);
    static Thread timeThread = new Thread(time);
    private static App instance;

    /**
     * Crea una nuova finestra di gioco.
     * Inizializza i componenti grafici e imposta gli ascoltatori degli eventi.
     */
    public App() {
        this.setTitle("Elon and Martians");
        initComponents();
        ImageIcon icon = new ImageIcon("./resources/images/ufo.png");
        this.setIconImage(icon.getImage());
        redirectSystemStreams();
        getContentPane().setBackground(new Color(232, 100, 27, 236));
        jTextArea1.setBackground(new Color(255, 255, 209, 119));
        engine = new Engine(new ElonAndMartians(), jTextArea1);
        inputTextField = jTextField1;
        jButton9.addActionListener(e -> handleInputSubmission());
        jMenuItem7.addActionListener(e -> exitGame());
        inputTextField.addActionListener(e -> handleInputSubmission());
        setLocationRelativeTo(null);
        backgroundWindow = createBackgroundWindow();
        backgroundWindow.setVisible(true);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                int result = JOptionPane.showConfirmDialog(App.this,
                        "Sei sicuro di voler abbandonare?",
                        "Conferma uscita",
                        JOptionPane.YES_NO_OPTION);

                if (result == JOptionPane.YES_OPTION) {
                    backgroundWindow.dispose();
                    System.exit(0);
                }
            }
        });

        new Thread(() -> engine.execute()).start();

        // Inizializza il music player e avvia la musica
        String musicFilePath = "./resources/music/elon&martians_song.wav";
        musicPlayer = new MusicPlayer(musicFilePath);
        musicThread = new Thread(musicPlayer);
        musicThread.start();

        jButton7.addActionListener(e -> {
            try {
                toggleMusic();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    /**
     * Ottiene l'istanza corrente di App.
     * Utilizza il pattern Singleton per garantire che esista solo una istanza di App.
     * @return l'istanza corrente di App
     */
    public static App getInstance() {
        if (instance == null) {
            instance = new App();
        }
        return instance;
    }

    /**
     * Classe interna che implementa il callback per l'aggiornamento del timer.
     */
    public static class TimeUpdateListener implements Time.TimerCallback {
        @Override
        public void onTimeUpdate(String formattedTime) {
            SwingUtilities.invokeLater(() -> {
                jLabel1.setText(formattedTime); // Aggiorna la JLabel con il tempo
            });
        }
    }

    /**
     * Restituisce un'istanza del callback per l'aggiornamento del timer.
     * @return istanza di Time.TimerCallback
     */
    public Time.TimerCallback getCallback() {
        return new TimeUpdateListener();
    }

    /**
     * Ferma la riproduzione della musica.
     * Interrompe il thread della musica se è attivo.
     */
    public void stopMusic() {
        if (musicThread != null && musicThread.isAlive()) {
            musicPlayer.stop();
            musicThread.interrupt();
        }
    }

    /**
     * Attiva o disattiva la riproduzione della musica.
     * @throws IOException se si verifica un errore
     */
    private void toggleMusic() throws IOException {
        musicPlayer.toggleMute();
        jButton7.setText(musicPlayer.isPlaying() ? "Musica: ON" : "Musica: OFF");
    }

    /**
     * Gestisce la chiusura dell'applicazione chiedendo conferma all'utente.
     */
    public void exitGame() {
        int result = JOptionPane.showConfirmDialog(App.this,
                "Sei sicuro di voler abbandonare?",
                "Conferma uscita",
                JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            backgroundWindow.dispose();
            System.exit(0);
        }
    }

    /**
     * Classe interna che ridefinisce {@link OutputStream} per redirigere l'output alla JTextArea.
     */
    private class CustomOutputStream extends OutputStream {
        private final JTextArea textArea;
        private final StringBuilder sb = new StringBuilder();

        public CustomOutputStream(JTextArea textArea) {
            this.textArea = textArea;
        }

        @Override
        public void write(int b) {
            sb.append((char) b);
            flush();
        }

        @Override
        public void flush() {
            SwingUtilities.invokeLater(() -> {
                textArea.append(sb.toString());
                sb.setLength(0);

                textArea.setCaretPosition(textArea.getDocument().getLength());
            });
        }
    }

    /**
     * Riceve in input ed elabora il testo inserito dall'utente e lo passa al motore di gioco.
     */
    private void handleInputSubmission() {
        String userInput = inputTextField.getText();
        if (userInput.isEmpty()) {
            userInput = "\n";
        }
        engine.processInput(userInput);
        inputTextField.setText("");
    }

    /**
     * Reindirizza l'output di sistema alla JTextArea.
     */
    private void redirectSystemStreams() {
        CustomOutputStream cos = new CustomOutputStream(jTextArea1);
        PrintStream ps = new PrintStream(cos, true);

        // Redirect System.out to the JTextArea
        System.setOut(ps);
        System.setErr(ps);
    }

    /**
     * Crea una finestra di sfondo semi-trasparente.
     * @return la JWindow modificata.
     */
    private JWindow createBackgroundWindow() {
        JWindow window = new JWindow();
        window.setBackground(new Color(0, 0, 0, 150)); // Colore semi-trasparente
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        window.setSize(screenSize);
        window.setLocationRelativeTo(null);
        return window;
    }

    /**
     * Salva lo stato attuale del gioco su file.
     */
    public void saveGame() {
        try {
            FileOutputStream fos = new FileOutputStream("./resources/saves/Adv.sav");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(Engine.getGame());
            oos.flush();
            oos.close();
            saveTime();
            Engine.appendToOutput("Partita salvata");
        } catch (Exception e){
            Engine.appendToOutput("impossibile salvare la partita. \n"
                    + e.getClass() + ": " + e.getMessage() + "\n");
        }
    }

    /**
     * Carica lo stato del gioco da file.
     */
    public void loadGame() {
        try {
            FileInputStream fis = new FileInputStream("./resources/saves/Adv.sav");
            ObjectInputStream ois = new ObjectInputStream(fis);
            engine.setGame((GameDescription) ois.readObject());
            ois.close();
            jTextArea1.setText("");
            Engine.appendToOutput("Partita caricata");
            loadTime();
            Utils.printCurrentLocation();

            //Ferma e resetta il timer corrente
            if (timeThread != null && timeThread.isAlive()) {
                time.stop();
                try {
                    timeThread.join(); // Aspetta che il thread finisca
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Crea e avvia un nuovo thread del timer
            time = new Time(getCallback());
            timeThread = new Thread(time);
            timeThread.start();


        } catch (Exception e) {
            Engine.appendToOutput("impossibile caricare la partita. \n"
                    + e.getClass() + ": " + e.getMessage() + "\n");
        }
    }

    /**
     * Salva il tempo di gioco su file.
     */
    public void saveTime() {
        try {
            FileOutputStream fos = new FileOutputStream("./resources/saves/Time.sav");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(Time.getElapsedTime());
            oos.flush();
            oos.close();
            //Engine.appendToOutput("Tempo di gioco salvato");
        } catch (Exception e) {
            Engine.appendToOutput("Impossibile salvare il tempo \n"
                    + e.getClass() + ": " + e.getMessage() + "\n");
        }
    }

    /**
     * Carica il tempo di gioco da file.
     */
    public void loadTime() {
        try {
            FileInputStream fis = new FileInputStream("./resources/saves/Time.sav");
            ObjectInputStream ois = new ObjectInputStream(fis);
            long savedTime = (long) ois.readObject();
            ois.close();
            Time.getInstance().setElapsedTime(savedTime);
            Engine.appendToOutput("Tempo di gioco caricato");
        } catch (Exception e) {
            Engine.appendToOutput("Impossibile caricare il tempo. \n"
                    + e.getClass() + ": " + e.getMessage() + "\n");
        }
    }

    /**
     * Mostra una finestra di dialogo per permettere all'utente di segnalare bug o problemi.
     * @return la segnalazione dell'utente
     */
    public String reportErrors() {
        String userInput = JOptionPane.showInputDialog(this, "Segnala bug o problemi che possono compromettere l'esperienza di gioco:", "Segnala un problema", JOptionPane.DEFAULT_OPTION);
        return userInput == null ? "" : userInput; //Fix: la pressione del tasto ESC restituisce la stringa "null"
    }

    /**
     * Mostra una finestra di messaggio che consente all'utente di visualizzare la classifica di gioco.
     */
    public void showChart() {
        JOptionPane.showMessageDialog(this, Database.showDatabase(), "Classifica", JOptionPane.DEFAULT_OPTION);
    }

    /**
     * Mostra una finestra di messaggio che consente all'utente di visualizzare i crediti del gioco.
     */
    public void showCredits() {
        String message = "L'avventura testuale \"Elon And Martians\", concepita come progetto universitario\nrelativo al"
                + " corso di Metodi avanzati di programmazione, nasce da un'idea\nsviluppata integralmente dal team"
                + " \"APS Dev\" composto da:\n\nAngelo Polito (GitHub: angpolito)\nPaolo Scicutella (GitHub: paoloscicu02)\nSimone Pugliese (GitHub: simonepugliese0)";
        JOptionPane.showMessageDialog(this, message, "Crediti", JOptionPane.DEFAULT_OPTION);
    }

    /**
     * Mostra una finestra di dialogo per permettere all'utente di inserire il proprio nome/nickname.
     * @return il nome inserito dall'utente
     */
    public String inputPlayerName() {
        String userInput = JOptionPane.showInputDialog(this, "Inserisci il tuo username...", "Entra nella classifica ufficiale di Elon And Martians", JOptionPane.PLAIN_MESSAGE);
        return userInput == null ? "" : userInput;    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jButton9 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);
        setBackground(new java.awt.Color(255, 51, 51));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setLocation(new java.awt.Point(200, 100));

        jButton1.setText("A");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("B");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Ovest");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Est");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Sud");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("Nord");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setText("Musica: ON");
        jButton7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jButton9.setText("Invio");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jMenu1.setText("Menu");

        jMenuItem4.setText("Nuova partita");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuItem5.setText("Salva partita");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem5);

        jMenuItem6.setText("Carica partita");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem6);

        jMenuItem7.setText("Esci dal gioco");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });

        jMenu1.add(jMenuItem7);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Altro");

        jMenuItem1.setText("Classifica");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    jMenuItem1ActionPerformed(evt);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        jMenu2.add(jMenuItem1);

        jMenuItem2.setText("Help");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        jMenuItem3.setText("Crediti");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuItem8.setText("Segnala un problema");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    String error = reportErrors();
                    ErrorReport.reportErrors(error);
                } catch (IOException e) {
                    Engine.appendToOutput("Errore: impossibile connettersi al server per inviare la segnalazione. Riprova piu' tardi.");
                }
            }
        });
        jMenu2.add(jMenuItem8);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jTextField1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jButton9)
                                                .addGap(29, 29, 29))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(42, 42, 42)
                                                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(56, 56, 56)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(82, 82, 82))
                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(117, 117, 117))))))
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1)
                                .addContainerGap())
                        .addGroup(layout.createSequentialGroup()
                                .addGap(290, 290, 290)
                                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(299, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 441, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton9))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jButton6)
                                                .addGap(4, 4, 4)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jButton4)
                                                        .addComponent(jButton3))
                                                .addGap(4, 4, 4)
                                                .addComponent(jButton5)
                                                .addGap(45, 45, 45)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addContainerGap())
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jButton1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton2)
                                                .addGap(86, 86, 86))))
        );


        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        engine.processInput("est");
        inputTextField.setText("");
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        engine.processInput("nord");
        inputTextField.setText("");
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // text field
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        engine.processInput("ovest");
        inputTextField.setText("");
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        engine.processInput("sud");
        inputTextField.setText("");
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        engine.processInput("inventario");
        inputTextField.setText("");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // tasto invioo
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        engine.processInput("guarda");
        inputTextField.setText("");
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) throws SQLException {//GEN-FIRST:event_jMenuItem1ActionPerformed
        showChart();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        engine.processInput("help");
        inputTextField.setText("");
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        showCredits();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //Ferma e resetta il timer corrente
                if (timeThread != null && timeThread.isAlive()) {

                    time.stop();
                    try {
                        timeThread.join(); // Aspetta che il thread finisca
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // Chiudi la finestra di sfondo esistente
                backgroundWindow.dispose();

                // Ferma la musica corrente
                stopMusic();

                // Dispose della vecchia finestra e crea una nuova istanza di App
                dispose();
                App newApp = new App();
                newApp.setVisible(true);

                //Avvia un nuovo timer
                time.reset();
                time = new Time(newApp.getCallback());
                timeThread = new Thread(time);
                timeThread.start();
            }
        });

    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed

    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        saveGame();
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        loadGame();
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) throws IOException {//GEN-FIRST:event_jMenuItem6ActionPerformed
        String error = reportErrors();
        ErrorReport.reportErrors(error);
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    /**
     * Metodo principale per gestire l'applicazione.
     * @param args
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(App.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(App.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(App.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(App.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new App().setVisible(true);
            }
        });

        JLabel jLabel1 = new JLabel();

        // Sincronizza l'avvio del thread del timer

        timeThread.start();
    }

    public JLabel getTimeLabel() {
        return jLabel1;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton9;
    private static javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
