<p>
  <img src="https://img.shields.io/github/license/angpolito/ElonAndMartians"/>
  <img src="https://img.shields.io/github/contributors/angpolito/ElonAndMartians"/>
  <img src="https://img.shields.io/github/forks/angpolito/ElonAndMartians"/>
  <img src="https://img.shields.io/github/stars/angpolito/ElonAndMartians"/>
</p>

# Elon And Martians 🚀
**Elon And Martians** is a project in Java developed for the course *Metodi avanzati di programmazione* (*Advanced Programming Methods*) during the 2023/2024 academic year, (2nd year of the BSc program in *Computer Science*) at the *University of Bari Aldo Moro*.

> **Disclaimer**: This project was created as part of a university course, and the code may contain errors or areas for optimization. The design and implementation were directly handled by the authors, with the project outline conceived by the course instructor.

---

## 📝 Project Description

**Elon And Martians** is a Java-based simulation game set in a sci-fi scenario where the protagonist, Elon Musk, and his crew are on a mission to gather information about Mars in order to colonize the planet. However, the mission takes an unexpected turn, and the objective of the game is to escape from a Martian prison and return to Earth.

---

## 👨‍💻 Authors

- **Angelo Polito**: [LinkedIn](https://www.linkedin.com/in/angelopolito)
- **Simone Pugliese**
- **Paolo Scicutella**

---

## ⚙️ Developed with
<img src="https://github.com/devicons/devicon/blob/master/icons/java/java-original.svg" title="Java" alt="Java" width="40" height="40"/>
<img src="https://img.shields.io/github/languages/top/angpolito/ElonAndMartians?color=red"/>

---

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

---

## 🔧 Installation

To run the project, follow these steps:

1. Clone the repository:

   ```bash
   git clone https://github.com/angpolito/ElonAndMartians.git
   ```

2. Navigate to the project folder:

   ```bash
   cd ElonAndMartians
   ```

3. Compile the project:

   ```bash
   javac *.java
   ```

4. Run the program:

   ```bash
   java Main
   ```

---

## 📄 License

This project is licensed under the GNU General Public License v3.0 - see the [LICENSE](LICENSE) file for details.

---

## Contributors
<a href="https://github.com/angpolito/ElonAndMartians/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=angpolito/ElonAndMartians" alt="contrib.rocks image"/>
</a>

---

### 👀 Contributing

If you wish to contribute to this project, fork the repository, make your desired changes, and submit a pull request. I’ll be happy to review it!
