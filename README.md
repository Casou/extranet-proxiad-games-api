# [Proxiad Games] Escape Game

## Installation des postes

### [API Rest + Serveur WebSocket](https://github.com/Casou/extranet-proxiad-games-api)
Il s'agit d'un projet Spring Boot à installer sur le Raspberry Pi. Puis il suffit de lancer l'application, elle démarrera sur le **port 8000**.
* Cloner le repository
* Lancer la commande `mvn package` dans le dossier du projet
* Lancer la commande `java -jar target/extranet-0.0.1-SNAPSHOT.jar` dans le dossier du projet

Pour inclure un `application.properties` personnalisé au moment du lancement, vous pouvez lancer la commande :
```
java -Dspring.config.location=target\application.properties -jar target\extranet-0.0.1-SNAPSHOT.jar
```

### [Fake Extranet](https://github.com/Casou/extranet-proxiad-games)
Il s'agit d'un projet React à installer soit sur le PC connecté à internet (PC portable donné aux joueurs).
 
* Cloner le repository
* Lancer les commandes suivantes dans le dossier du projet 
```
npm install
npm start
``` 
L'application se lancera sur le **port 3000**.

### [IHM joueurs / régie](https://github.com/Casou/ihm-ecran-proxiad-games)
Il s'agit d'un projet html à installer sur le PC d'affichage du compteur (PC portable branché sur l'écran). Il se lance via un serveur HTTP.

* Cloner le repository
* Lancer les commandes suivantes dans le dossier du projet 
```
npm install -g http-server
http-server . -p 9999
```
L'application se lancera sur le **port 9999**. Les urls sont :
* [http://localhost:9999](http://localhost:9999) pour l'affichage du compteur.
* [http://localhost:9999/admin.html](http://localhost:9999/admin.html) pour la régie.

#### Alternative
Si npm ne veut pas installer http-server, vous pouvez installer Python 3.7 et lancer la commande :
```
cd <path>
py -m http.server 9999
```

## Préparation du matériel

### Clefs USB vérolées
Copier le fichier troll.exe sur chacune des "fausses" clefs USB et le renommer dans un nom un peu moins évident.

Copier le fichier troll-properties.txt dans le dossier `C:\temp` du PC connecté à Internet et l'éditer pour renseigner l'url du serveur et le nom de la salle.

## Installation du Raspberry

Pour installer le Raspberry, il suffit de graver sur la carte SD l'ISO disponible sur le drive. La suite des instructions décrit comment installer le Raspberry depuis zéro.

### OS

[Dietpi](https://dietpi.com/)

Télécharger l'ISO de Dietpi et le graver sur la carte SD.
Lors du premier lancement, Dietpi lance l'assistant de configuration. 
Installez une interface graphique et la définir comme démarrage par défaut (avec autologin)

[Raspbian](https://www.raspberrypi.org/downloads/)

[...]

Activer la connection SSH via la commande `raspi-config` et changer le mot de passe en "proxiadgames" (pour éviter le warning au démarrage).

Mettre un joli fond d'écran Proxiad !

### Configuration

* `sudo apt-get install nodejs npm git vim`
* `sudo npm i -g http-server`
* `sudo apt-get install speech-dispatcher espeak pulseaudio`

#### Lancement automatique des programes
Pour lancer automatiquement le serveur http, on crée un fichier `autostart.sh` dans `/home/pi` avec le contenu suivant : 

```
#!/bin/bash
cd /var/www/ihm-ecran-proxiad-games/ && http . -p 9999 &
```

Pour lancer le navigateur en mode plein écran, éditer le fichier `/etc/xdg/lxsession/LXDE-pi/autostart` et y ajouter la ligne
`@/usr/lib/chromium-browser/chromium-browser --start-fullscreen http://localhost:9999`

#### Désactiver l'extinction automatique de l'écran
* Installation des utilitaires X11 :
`sudo apt-get install x11-xserver-utils`

* Modification du fichier de configuration de LXDE
    * Chercher et supprimer : `/etc/xdg/lxsession/LXDE/autostart` 
    * Ajouter les lignes :
```
@xset s off
@xset -dpms
@xset s noblank
```

* Redémarrer le Raspberry