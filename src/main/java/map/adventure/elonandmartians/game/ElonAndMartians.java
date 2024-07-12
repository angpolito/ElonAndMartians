/**
 * @author APS Dev (Angelo Polito, Paolo Scicutella, Simone Pugliese)
 */
package map.adventure.elonandmartians.game;

import map.adventure.elonandmartians.observer.GameObserver;
import map.adventure.elonandmartians.observer.InventoryObserver;
import map.adventure.elonandmartians.observer.LookAtObserver;
import map.adventure.elonandmartians.observer.MoveObserver;
import map.adventure.elonandmartians.observer.OpenObserver;
import map.adventure.elonandmartians.observer.PickUpObserver;
import map.adventure.elonandmartians.observer.PushObserver;
import map.adventure.elonandmartians.observer.ShootObserver;
import map.adventure.elonandmartians.observer.TalkObserver;
import map.adventure.elonandmartians.observer.TurnOffObserver;
import map.adventure.elonandmartians.observer.UseObserver;
import map.adventure.elonandmartians.parser.ParserOutput;

import java.io.PrintStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import map.adventure.elonandmartians.type.*;

/**
 * La classe ElonAndMartians è concreta ed estende GameDescription e implementa GameObservable,
 * serve a implementare i dettagli specifici del gioco, come regole, logica di movimento dei giocatori e gestione
 * dello stato del gioco, utilizzando i metodi ereditati.
 */
public class ElonAndMartians extends GameDescription implements GameObservable, Serializable {

    private final List<GameObserver> observer = new ArrayList<>();
    private ParserOutput parserOutput;

    private final List<String> messages = new ArrayList<String>();

    private static ElonAndMartians instance;

    /**
     * Costruttore di ElonAndMartians.
     */
    public ElonAndMartians() {
        super();
    }

    /**
     * Ottiene l'istanza singleton di ElonAndMartians.
     *
     * @return istanza di ElonAndMartians
     */
    public static ElonAndMartians getInstance() {
        if (instance == null) {
            instance = new ElonAndMartians();
        }
        return instance;
    }

    /**
     * Gestisce i comandi, le stanze, la mappa, gli oggetti e gli observer.
     *
     * @throws Exception se si verifica un errore durante l'inizializzazione
     */
    @Override
    public void init() throws Exception {
        messages.clear();
        setupCommands();
        setupRoomsObjects();
        setupObservers();
    }

    /**
     * Inizializza i comandi del gioco.
     */
    public void setupCommands() {
        Command nord = new Command(CommandType.NORD, "nord");
        nord.setAlias(new String[]{"Nord", "NORD"});
        getCommands().add(nord);
        Command iventory = new Command(CommandType.INVENTORY, "inventario");
        iventory.setAlias(new String[]{"inv"});
        getCommands().add(iventory);
        Command sud = new Command(CommandType.SOUTH, "sud");
        sud.setAlias(new String[]{"Sud", "SUD"});
        getCommands().add(sud);
        Command est = new Command(CommandType.EAST, "est");
        est.setAlias(new String[]{"Est", "EST"});
        getCommands().add(est);
        Command ovest = new Command(CommandType.WEST, "ovest");
        ovest.setAlias(new String[]{"Ovest", "OVEST"});
        getCommands().add(ovest);
        Command end = new Command(CommandType.END, "end");
        end.setAlias(new String[]{"end", "fine", "esci", "muori", "ammazzati", "ucciditi", "suicidati", "exit",
                "basta", "addio", "abbandona"});
        getCommands().add(end);
        Command look = new Command(CommandType.LOOK_AT, "osserva");
        look.setAlias(new String[]{"guarda", "vedi", "trova", "cerca", "descrivi"});
        getCommands().add(look);
        Command pickup = new Command(CommandType.PICK_UP, "raccogli");
        pickup.setAlias(new String[]{"prendi", "afferra"});
        getCommands().add(pickup);
        Command open = new Command(CommandType.OPEN, "apri");
        open.setAlias(new String[]{"sblocca"});
        getCommands().add(open);
        Command push = new Command(CommandType.PUSH, "premi");
        push.setAlias(new String[]{"spingi", "attiva", "schiaccia"});
        getCommands().add(push);
        Command use = new Command(CommandType.USE, "usa");
        use.setAlias(new String[]{"utilizza"});
        getCommands().add(use);
        Command turnoff = new Command(CommandType.TURN_OFF, "spegni");
        turnoff.setAlias(new String[]{"disattiva"});
        getCommands().add(turnoff);
        Command shoot = new Command(CommandType.SHOOT, "spara");
        shoot.setAlias(new String[]{"uccidi", "ammazza", "colpisci"});
        getCommands().add(shoot);
        Command talk = new Command(CommandType.TALK_TO, "parla");
        talk.setAlias(new String[]{"comunica", "interagisci"});
        getCommands().add(talk);
    }

    /**
     * Inizializza le stanze, i collegamenti tra di esse e gli oggetti.
     */
    public void setupRoomsObjects() {
        Room shuttle = new Room(0, "Shuttle", "Sei nello shuttle PROXIMA di SpaceX in direzione verso "
                + "Marte.\nIl tuo obiettivo e' quello di scoprire informazioni importanti sul pianeta "
                + "al fine di colonizzarlo.\nIntorno a te c'e' il resto dell'equipaggio:\n\t- Smith (Pilota)\n\t"
                + "- Miller (Ingegnere di volo)");
        Room prisons = new Room(1, "Prigioni", "Sei nelle prigioni della base spaziale, "
                + "maledetti alieni.");
        Room corridor = new Room(2, "Corridoio", "Un lungo corridoio che sembra perdersi nel buio "
                + "in lontananza.\nA sinistra una stanza e davanti a te un'altra. "
                + "Sulla destra solo ritratti di Zylok... che egocentrico!");
        Room astronomicalObservatory = new Room(3, "Osservatorio astronomico", "Sei in una stanza "
                + "grande, anzi, MOLTO grande, piena di aggeggi strani e modellini di pianeti.\nLe pareti,"
                + " di un metallo blu luminescente, sono coperte da pulsanti e pannelli di controllo, mentre al centro"
                + " della stanza un trasmettitore radio, in grado di instaurare comunicazioni con mondi lontani.");
        Room laboratory = new Room(4, "Laboratorio", "La sala della base aliena marziana"
                + " dedicata alla trasformazione fisica.\nAl centro della stanza, su un piedistallo circolare di puro"
                + " cristallo verde, si trova il dispositivo di metamorfosi, intorno al quale ci sono scaffali"
                + " contenenti strumenti e utensili di ogni genere");
        Room teleportationRoom = new Room(5, "Stanza del teletrasporto", "La stanza del"
                + " teletrasporto nella base aliena marziana e' un angolo oscuro e misterioso.\nLe pareti sono"
                + " rivestite di metallo scuro e intarsiate con simboli luminescenti che emanano una luce blu pallida,"
                + " proiettando ombre contorte.\nAl centro della stanza, un portale circolare giace immobile,"
                + " circondato da un'aura di energia statica.");
        Room caveau = new Room(6, "Caveau", "Il Caveau della base aliena, in ogni angolo"
                + " scaffali e vetrine ospitano artefatti preziosi: armi aliene, dispositivi che pulsano"
                + " con energie ignote, e reliquie antiche");
        Room prisoncellElon = new Room(8, "Cella di Elon",
                "Ti trovi dietro le sbarre di una cella.");
        Room prisoncellCrew = new Room(9, "Cella di Smith e Miller",
                "Smith e Miller sono stati rinchiusi in questa cella.");

        //map (collega le stanze)
        prisoncellCrew.setEast(prisons);
        prisoncellElon.setNorth(prisons);
        prisons.setWest(prisoncellCrew);
        prisons.setNorth(corridor);
        prisons.setSouth(prisoncellElon);
        corridor.setWest(astronomicalObservatory);
        corridor.setSouth(prisons);
        corridor.setNorth(laboratory);
        astronomicalObservatory.setEast(corridor);
        laboratory.setSouth(corridor);
        laboratory.setEast(teleportationRoom);
        teleportationRoom.setWest(laboratory);
        getRooms().add(shuttle);
        getRooms().add(prisoncellElon);
        getRooms().add(prisoncellCrew);
        getRooms().add(prisons);
        getRooms().add(corridor);
        getRooms().add(astronomicalObservatory);
        getRooms().add(laboratory);
        getRooms().add(teleportationRoom);
        getRooms().add(caveau);

        //objects
        AdvObject gem = new AdvObject(1, "gemma", "La Gemma risplende di tonalita' verdi"
                + " e viola, capace di risuonare con energie cosmiche e aprire portali segreti e antichi.");
        gem.setAlias(new String[]{"pietra", "cristallo"});

        AdvObjectContainer hologramGenerator = new AdvObjectContainer(2, "dispositivo",
                "Una sfera scintillante di cristallo liquido, al suo centro, due alloggiamenti luminescenti"
                + " attendono campioni di DNA umano e marziano, in grado di sincronizzare e alterare la struttura"
                + " cellulare per adattarsi all'ambiente alieno.");
        hologramGenerator.setAlias(new String[]{"generatore", "sfera"});
        laboratory.getObjects().add(hologramGenerator);
        hologramGenerator.setOpenable(true);
        hologramGenerator.setPickupable(false);

        AdvObject alienSpaceSuit = new AdvObject(3, "tuta",
                "Dalla puzza che emana credo che non utilizzino la lavatrice qui.");
        alienSpaceSuit.setAlias(new String[]{"tuta", "divisa"});
        astronomicalObservatory.getObjects().add(alienSpaceSuit);
        alienSpaceSuit.setWearable(true);

        AdvObject scissors = new AdvObject(4, "paio di forbici",
                "Scintillanti e dalla punta arrotondata... non e' il momento giusto per un art attack!");
        scissors.setAlias(new String[]{"forbici"});
        laboratory.getObjects().add(scissors);

        AdvObject laserGun = new AdvObject(5, "fucile", "E' un'arma futuristica, con"
                + " linee fluide e una struttura composta di un materiale iridescente che riflette la luce.");
        laserGun.setAlias(new String[]{"fucile", "arma", "pistola", "blaster"});
        caveau.getObjects().add(laserGun);

        AdvObjectContainer shrine = new AdvObjectContainer(6, "scrigno",
                "Uno scrigno di cristallo trascendente, con intarsi sottili, che sembrano intrecciarsi"
                + " come segni stellari e ne decorano i bordi, mentre un meccanismo di sicurezza vibra appena"
                + " percepibilmente attorno al perimetro.");
        shrine.setAlias(new String[]{"baule", "cassa", "forziere"});
        caveau.getObjects().add(shrine);
        shrine.setOpenable(true);
        shrine.setPickupable(false);
        shrine.add(gem);

        AdvObject motor = new AdvObject(7, "motore",
                " Il cuore dello shuttle PROXIMA.\nI flussi di plasma emessi dal propulsore si fondono"
                + " con il vuoto cosmico, spingendo la navicella verso il suo destino rosso-oro su Marte.");
        motor.setAlias(new String[]{"propulsore"});
        shuttle.getObjects().add(motor);
        motor.setPickupable(false);
        motor.setStateOn(true);
        motor.setSwitchable(true);

        AdvObject exitDoor = new AdvObject(8, "porta", "L'imponente portone della base"
                + " marziana.\nAl centro del portone, incastonato come il cuore di un'antica reliquia, c'e' un incavo"
                + " perfettamente scolpito a forma di diamante.");
        exitDoor.setAlias(new String[]{"portone", "cancello"});
        prisons.getObjects().add(exitDoor);
        exitDoor.setOpenable(true);
        exitDoor.setOpen(false);
        exitDoor.setPickupable(false);

        AdvObject gravityButton = new AdvObject(9, "pulsante", "Un pulsante unico nel"
                + " suo genere, incastonato in un pannello di cristallo nero. Emana un bagliore cangiante che varia"
                + " dal blu al viola, pulsando ritmicamente come un cuore alieno. Attorno alla sua base,"
                + " piccoli simboli luminescenti fluttuano nell'aria, rappresentando i diversi livelli di gravita' che"
                + " puo' impostare.");
        gravityButton.setAlias(new String[]{"switcher", "bottone", "tasto", "interruttore"});
        prisons.getObjects().add(gravityButton);
        gravityButton.setPushable(true);
        gravityButton.setPickupable(false);

        AdvObject telescope = new AdvObject(10, "trasmettitore",
                "Il radio trasmettitore interplanetario della base marziana.\nAccessibile solo con una"
                + " tessera di riconoscimento, permette comunicazioni istantanee tra pianeti, emettendo onde"
                + " luminescenti che attraversano l'oscurita' dello spazio.");
        telescope.setAlias(new String[]{"trasmittente", "ripetitore"});
        telescope.setPickupable(false);
        astronomicalObservatory.getObjects().add(telescope);

        AdvObject transporter = new AdvObject(11, "teletrasporto",
                "Una piattaforma circolare con riconoscimento facciale nettamente migliore di quello di "
                        + "Apple...");
        transporter.setAlias(new String[]{"portale", "piattaforma"});
        transporter.setPickupable(false);
        teleportationRoom.getObjects().add(transporter);
        caveau.getObjects().add(transporter);

        AdvObject humanDNA = new AdvObject(12, "campione",
                "Un capello ricco di DNA... Cosa vuoi che ci sia di interessante?");
        humanDNA.setAlias(new String[]{"pelo"});
        humanDNA.setVisible(false);
        getInventory().add(humanDNA);

        AdvObject alienDNA = new AdvObject(13, "pezzo",
                "Un ritaglio di tuta aliena logora e sudaticcia ricca di DNA alieno... "
                        + "Trovo piu' usuale collezionare francobolli.");
        alienDNA.setAlias(new String[]{"ritaglio"});
        alienDNA.setVisible(false);
        getInventory().add(alienDNA);

        AdvObject hair = new AdvObject(14, "capelli",
                "I tuoi capelli. Cosa vuoi che ci sia di interessante?");
        hair.setAlias(new String[]{"capello", "ciuffo", "ciocca"});
        hair.setVisible(false);
        getInventory().add(hair);
        hair.setPickupable(false);

        AdvObject lockpick = new AdvObject(15, "nano-chiave universale",
                "Dispositivo in grado di riprogrammare qualunque tipo di serratura.\n");
        lockpick.setAlias(new String[]{"chiave", "grimaldello", "nano-chiave"});
        lockpick.setVisible(false);
        getInventory().add(lockpick);

        AdvObject hole = new AdvObject(16, "buco nel pavimento",
                "Emana un tanfo terribile, adesso si spiega l'assenza del WC, meglio che scappi..."
                        + " prima che ti scappi.");
        hole.setAlias(new String[]{"buco", "buco nel pavimento"});
        prisoncellElon.getObjects().add(hole);
        hole.setPickupable(false);


        AdvObject doorlockElon = new AdvObject(17, "serratura",
                "Una serratura metallica, un po' arrugginita, impossibile forzarla, ma con la giusta"
                        + " chiave dovrebbe aprirsi.");
        doorlockElon.setAlias(new String[]{"lucchetto"});
        prisoncellElon.getObjects().add(doorlockElon);
        doorlockElon.setPickupable(false);

        AdvObject pass = new AdvObject(18, "tessera",
                "Una tessera nominativa: \"Kif Kroker\". Tipico nome da marziano.");
        pass.setAlias(new String[]{"card", "pass", "tesserino"});
        pass.setVisible(false);
        corridor.getObjects().add(pass);

        AdvObject axe = new AdvObject(19, "ascia", "Un'ascia dalla lama tagliente,"
                + " sottile come un raggio di luce, e' incisa con rune antiche. Maneggiala con cura.");
        axe.setAlias(new String[]{"mannaia", "scure"});
        caveau.getObjects().add(axe);

        AdvObject alien = new AdvObject(20, "alieno", "Ha la pelle verde olivastra e un"
                + " corpo snello coperto da un'uniforme militare grigia.");
        alien.setAlias(new String[]{"marziano", "nemico"});
        corridor.getObjects().add(alien);
        alien.setVisible(false);
        alien.setPickupable(false);

        AdvObject smith = new AdvObject(21, "Smith", "Pilota del tuo shuttle.");
        smith.setAlias(new String[]{"pilota", "smith"});
        smith.setSentences(new String[]{"Capitano Musk, sto facendo il possibile per ritardare l'esplosione ma deve"
                + " decidere in fretta!",
                "Capitano, e' stato un onore servirla, ci lasci qui, non perda tempo, Lei e' l'unico che potra'"
                + " colonizzare Marte",
                "Presto capitano, dobbiamo fuggire prima che si accorgano della nostra evasione.",
                "Non si aspettera' certo che caschi nel vecchio tranello del travestimento… ho imparato la lezione"
                + " quando da bambino, un uomo in un furgone bianco mi ha offerto una scorta a vita di caramelle."});
        shuttle.getObjects().add(smith);
        smith.setVisible(true);
        smith.setPickupable(false);

        AdvObject miller = new AdvObject(22, "Miller", "Ingegnere di bordo .");
        miller.setAlias(new String[]{"miller", "ingegnere"});
        miller.setSentences(new String[]{"Capitano non dimentichi che quando il flusso di particelle quantiche entra"
                + " in risonanza con l'iperdrive del motore a curvatura inversa, si genera un campo di tachioni"
                + " sinaptici che induce una temporizzazione anisotropica nel continuum spazio-temporale. Questo,"
                + " ovviamente, porta a una dislocazione dei quasar nella fascia di Higgs. Spero di esserle stato di"
                + " aiuto nella sua decisione.",
                "Signor Musk, La prego, non ci lasci qui, vengono serviti solo piatti a base di carne di visitatori"
                + " non graditi, Lei sa che sono vegano...",
                "E' il momento di darsela a gambe!",
                "Non sono razzista... ma... non mi fido dei verdi."});
        shuttle.getObjects().add(miller);
        miller.setVisible(true);
        miller.setPickupable(false);

        AdvObject doorlock2 = new AdvObject(23, "cella di Smith e Miller",
                "Pero'... la loro cella e' molto piu' avanzata della mia, vetri antiproiettile, sensori"
                + " di movimento, devono averli scambiati per quelli intelligenti... Anche la serratura sembra diversa...");
        doorlock2.setAlias(new String[]{"cella", "cella di Smith e Miller"});
        prisons.getObjects().add(doorlock2);
        doorlock2.setPickupable(false);

        //set starting room
        setCurrentRoom(shuttle);

        // setLook delle stanze
        shuttle.setLook(createLook(shuttle));
        prisoncellElon.setLook(createLook(prisoncellElon));
        prisoncellCrew.setLook(createLook(prisoncellCrew));
        prisons.setLook(createLook(prisons));
        corridor.setLook(createLook(corridor));
        astronomicalObservatory.setLook(createLook(astronomicalObservatory));
        teleportationRoom.setLook(createLook(teleportationRoom));
        laboratory.setLook(createLook(laboratory));
        caveau.setLook(createLook(caveau));
    }

    /**
     * @param room la stanza attuale
     * @return lista di stringhe con articolo indeterminato prima di ogni oggetto
     */
    public static List<String> createLook(final Room room) {
        List<String> objects = new ArrayList<>();
        for (int i = 0; i < room.getObjects().size(); i++) {
            objects.add(Utils.getArticle(room.getObjects().get(i).getName()));
        }
        return objects;
    }

    /**
     * Inizializza gli osservatori.
     */
    public void setupObservers() {
        GameObserver moveObserver = new MoveObserver();
        this.attach(moveObserver);
        GameObserver invObserver = new InventoryObserver();
        this.attach(invObserver);
        GameObserver pushObserver = new PushObserver();
        this.attach(pushObserver);
        GameObserver lookatObserver = new LookAtObserver();
        this.attach(lookatObserver);
        GameObserver pickupObserver = new PickUpObserver();
        this.attach(pickupObserver);
        GameObserver openObserver = new OpenObserver();
        this.attach(openObserver);
        GameObserver useObserver = new UseObserver();
        this.attach(useObserver);
        GameObserver turnOffObserver = new TurnOffObserver();
        this.attach(turnOffObserver);
        GameObserver talkObserver = new TalkObserver();
        this.attach(talkObserver);
        GameObserver shootObserver = new ShootObserver();
        this.attach(shootObserver);
    }

    /**
     * Gestisce l'output del parser.
     *
     * @param p il risultato del parser
     * @param out il flusso di output per la stampa dei risultati
     */
    @Override
    public void nextMove(final ParserOutput p, final PrintStream out) {
        parserOutput = p;
        messages.clear();
        if (p.getCommand() == null) {
            out.println("Non credo di aver capito... Prova con un altro comando.");
        } else {
            Room cr = getCurrentRoom();
            notifyObservers();
            boolean move = !cr.equals(getCurrentRoom()) && getCurrentRoom() != null;
            if (!messages.isEmpty()) {
                for (String m : messages) {
                    if (m.length() > 0) {
                        Engine.appendToOutput(m);
                    }
                }
            }
            if (move) {
                Utils.printCurrentLocation();
            }
        }
    }

    /**
     * Aggiunge un osservatore alla lista degli osservatori.
     *
     * @param o l'osservatore da aggiungere
     */
    @Override
    public void attach(final GameObserver o) {
        if (!observer.contains(o)) {
            observer.add(o);
        }
    }

    /**
     * Notifica tutti gli osservatori il registrarsi di un cambiamento.
     */
    @Override
    public void notifyObservers() {
        for (GameObserver o : observer) {
            messages.add(o.update(this, parserOutput));
        }
    }

    /**
     * Restituisce il messaggio di benvenuto.
     *
     * @return String messaggio di benvenuto
     */
    @Override
    public String getWelcomeMsg() {
        return "Benvenuto a bordo di questa missione nei panni di Mr. Elon Musk!\n"
                + "Allaccia le cinture e preparati al decollo...\nSi parte!\n"
                + "(digita \"help\" per visualizzare i comandi in qualsiasi momento)";
    }

    /**
     * Restituisce uno dei tre finali possibili del gioco.
     *
     * @return String uno dei tre finali possibili
     */
    public String getEnding() {
        String endingMessage;
        if (Engine.getGame().getCharacterLook()) {
            if (Engine.getGame().getCrewIsFree()) {
                endingMessage = "L’avventura e' giunta a termine. Arrivano i soccorsi: e' Starman! A bordo della"
            + " sua Tesla Roadster recupera Elon e  il suo equipaggio per poi fare rotta verso la Terra.\n";
            } else {
                endingMessage = "L’avventura e' giunta a termine. Arrivano i soccorsi: e' Starman! A bordo della"
            + " sua Tesla Roadster recupera Elon per poi fare rotta verso la Terra.\n"
                        + "- Starman: La vedo pensieroso, va tutto bene?\n"
                        + "- Elon: credo di aver dimenticato qualcosa…\n"
                        + "- Starman: se l'ha dimenticata, non era importante.\n"
                        + "- Elon: gia', hai ragione!";
            }

        }  else {
            endingMessage =  "Elon abbandona la base spaziale travestito da marziano, vede i soccorsi in lontananza:"
                    + " e' Starman a bordo della sua Tesla Roadster.\n"
                    + "Elon si sbraccia e grida per farsi notare, tuttavia Starman non e' in grado di riconoscerlo.\n"
                    + "- Starman: non pare esserci traccia di vita intelligente qui...\n"
                    + "Seccato, riparte verso l'orbita terrestre lasciando Elon sul Pianeta rosso.";

        }
        return endingMessage;
    }

    /**
     * Restituisce la grafica ASCII del titolo del gioco.
     *
     * @return String grafica ASCII del titolo del gioco
     */
    @Override
    public String getAsciiArt() {
        return ("  ______________________________________________________________________________________________ \n"
                + "|                               ================================                               |\n"
                + "|                               * Elon And Martian - 2023-2024 *                               |\n"
                + "|                               *         developed by         *                               |\n"
                + "|                               *            APS Dev           *                               |\n"
                + "|                               ================================                               |\n"
                + "|                                                                                              |\n"
                + "|                                          .------.                                            |\n"
                + "|                                         /        \\                                           |\n"
                + "|                                        /__      __\\                                          |\n"
                + "|                                       ||  |    |  ||                                         |\n"
                + "|                                       ||__|    |__||                                         |\n"
                + "|                                        \\    ||    /                                          |\n"
                + "|                                         \\        /                                           |\n"
                + "|                             o  o  o      \\  __  /       o  o  o                              |\n"
                + "|                              \\ | /        '.__.'         \\ | /                               |\n"
                + "|                                ||          |  |           ||                                 |\n"
                + "|                                 \\\\_________|  |__________//                                  |\n"
                + "|            _____ _            _____       _    _____         _   _                           |\n"
                + "|           |   __| |___ ___   |  _  |___ _| |  |     |___ ___| |_|_|___ ___ ___               |\n"
                + "|           |   __| | . |   |  |     |   | . |  | | | | .'|  _|  _| | .'|   |_ -|              |\n"
                + "|           |_____|_|___|_|_|  |__|__|_|_|___|  |_|_|_|__,|_| |_| |_|__,|_|_|___|              |\n"
                + "|______________________________________________________________________________________________|\n");
    }

    /**
     * Stampa l'elenco dei comandi disponibili nel gioco.
     */
    @Override
    public void showHelp() {
        Engine.appendToOutput("ELENCO DEI COMANDI:\n"
                + "MOVIMENTO\n"
                + "\t- nord : muoviti verso nord\n"
                + "\t- sud : muoviti verso sud\n"
                + "\t- est : muoviti verso est\n"
                + "\t- ovest : muoviti verso ovest\n"
                + "TASTI FUNZIONE\n"
                + "\t- A : inventario\n"
                + "\t- B : osserva la stanza\n"
                + "INTERAZIONE\n"
                + "\t- parla con <personaggio> : interagisci con un personaggio\n"
                + "OGGETTO\n"
                + "\t- raccogli <oggetto> : raccogli un oggetto\n"
                + "\t- usa <oggetto> : usa un oggetto\n"
                + "\t- usa <oggetto inventario> con <oggetto> : usa un oggetto di inventario con un altro oggetto\n"
                + "\t- apri <oggetto> : apri un oggetto contenitore\n"
                + "\t- spegni <oggetto> : spegni un oggetto\n"
                + "\t- premi <oggetto> : premi un oggetto\n"
                + "\t- osserva <oggetto> : ottieni la descrizione dell'oggetto\n"
                + "STANZA\n"
                + "\t- osserva : ottieni la descrizione della stanza\n"
                + "INVENTARIO\n"
                + "\t- inventario : visualizza il contenuto dell'inventario\n"
                + "GIOCO\n"
                + "\t- abbandona : abbandona il gioco");
    }
}
