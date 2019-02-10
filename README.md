# [Proxiad Games] API pour le fake extranet

## Installation des serveurs

### [API Rest](https://github.com/Casou/extranet-proxiad-games-api) 
Il s'agit d'un projet Spring Boot à installer sur un serveur Proxiad (avec un nom de domaine si possible). 
Puis il suffit de lancer l'application, elle démarrera sur le **port 8000**.

### [Extranet](https://github.com/Casou/extranet-proxiad-games)
Il s'agit d'un projet React à installer soit sur un serveur Proxiad (avec un nom de domaine si possible)
soit sur le PC connecté à internet.
 
Il faut exécuter les commandes suivantes : 
```
npm install
npm start
``` 
L'application se lancera sur le **port 3000**.

### [IHM joueurs / régie](https://github.com/Casou/ihm-ecran-proxiad-games)
Il s'agit d'un projet html qui se lance via un serveur HTTP. 
A installer sur le Raspberry (pour IHM joueur) ainsi que sur un serveur Proxiad (avec un nom de domaine si possible) pour la régie.

Il faut exécuter les commandes suivantes : 
```
npm install -g http-server
http-server <path-to-folder> -p 9999
```
L'application se lancera sur le **port 9999**.

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
Dans le fichier `/boot/config.txt`, ajouter les lignes suivantes : 
```
# power down monitor when lockscreen enabled
hdmi_blanking=1
```

Dans le fichier `~/.config/lxsession/LXDE-pi/autostart`, ajouter les lignes suivantes :
```
@xset s 0 0
@xset s noblank
@xset s noexpose
@xset dpms 0 0
```



`chromium-browser --enable-speech-synthesis`

```
#!/bin/bash
cd /var/www/ihm-ecran-proxiad-games && http-server . -p 9999 &
chromium-browser --enable-speech-dispatcher -kiosk http://localhost:9999 &
```