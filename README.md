# ElonAndMartians

**Elon And Martians** is a project in Java developed for the course *Metodi avanzati di programmazione* (*Advanced Programming Methods*) during the 2023/2024 academic year, (2nd year of the BSc program in *Computer Science*) at the *University of Bari Aldo Moro*.<br>
Authors: Angelo Polito, Simone Pugliese, Paolo Scicutella.<br><br>
Consult the documentation [here](Documentation.md).

## Repository Tree (Structure):

    ElonAndMartians
    │   Documentation.md
    │   gitignore
    │   LICENSE
    │   pom.xml
    │   README.md
    │
    ├───javadoc
    │
    ├───resources
    │   │   stopwords.file
    │   │
    │   ├───images
    │   │   │   map.png
    │   │   │   ufo.png
    │   │   │   UMLdiagram.png
    │   │   │
    │   │   └───screenshots
    │   │           chart.png
    │   │           commands.png
    │   │           insertchart.png
    │   │           interface.png
    │   │           menugame.png
    │   │           menuother.png
    │   │           notifybug.png
    │   │
    │   ├───music
    │   │       elon&martians_song.wav
    │   │
    │   └───saves
    │ 
    └───src
        ├───main
        │   └───java
        │       └───map
        │           └───adventure
        │               └───elonandmartians
        │                   ├───database
        │                   │       Database.java
        │                   │
        │                   ├───game
        │                   │       ElonAndMartians.java
        │                   │       Engine.java
        │                   │       GameDescription.java
        │                   │       GameObservable.java
        │                   │       Utils.java
        │                   │
        │                   ├───observer
        │                   │       GameObserver.java
        │                   │       InventoryObserver.java
        │                   │       LookAtObserver.java
        │                   │       MoveObserver.java
        │                   │       OpenObserver.java
        │                   │       PickUpObserver.java
        │                   │       PushObserver.java
        │                   │       ShootObserver.java
        │                   │       TalkObserver.java
        │                   │       TurnOffObserver.java
        │                   │       UseObserver.java
        │                   │
        │                   ├───parser
        │                   │       Parser.java
        │                   │       ParserOutput.java
        │                   │
        │                   ├───rest
        │                   │       ChatGPTIntegration.java
        │                   │
        │                   ├───socket
        │                   │   ├───client
        │                   │   │       ErrorReport.java
        │                   │   │
        │                   │   └───server
        │                   │           ClientHandler.java
        │                   │           JabberServer.java
        │                   │
        │                   ├───swing
        │                   │       App.form
        │                   │       App.java
        │                   │
        │                   ├───thread
        │                   │       MusicPlayer.java
        │                   │       Time.java
        │                   │
        │                   └───type
        │                           AdvObject.java
        │                           AdvObjectContainer.java
        │                           Command.java
        │                           CommandType.java
        │                           Room.java
        │
        └───test
            └───java
