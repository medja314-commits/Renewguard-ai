# RenewGuard AI — Guide de démarrage

Ce guide t'accompagne pas à pas pour faire tourner l'application sur ta machine, même si tu n'as jamais utilisé WSL ou Git avant. Suis les étapes dans l'ordre — ne saute rien, même si une étape te semble déjà faite.

---

## Étape 0 — Est-ce que tu es sous Windows, Mac ou Linux ?

- **Windows** → suis tout le guide depuis le début (installation de WSL requise).
- **Mac ou Linux** → tu peux sauter directement à l'[Étape 3](#étape-3--cloner-le-dépôt), ton terminal habituel suffit (pas besoin de WSL).

---

## Tu préfères éviter WSL ? → [Va directement à l'Option B](#option-b--installation-windows-native-sans-wsl)

---

## Étape 1 — Installer WSL (Windows uniquement)

WSL (Windows Subsystem for Linux) permet de faire tourner un vrai environnement Linux directement sur Windows, sans machine virtuelle séparée. C'est ce qui nous évite les galères d'installation Java/Maven directement sous Windows.

1. Ouvre le **menu Démarrer**, tape `PowerShell`, fais un clic droit sur "Windows PowerShell" et choisis **"Exécuter en tant qu'administrateur"**.
2. Dans la fenêtre PowerShell qui s'ouvre, tape :
   ```powershell
   wsl --install
   ```
3. Laisse l'installation se faire (ça peut prendre plusieurs minutes et télécharger Ubuntu automatiquement).
4. **Redémarre ton ordinateur** quand c'est demandé.
5. Après le redémarrage, une fenêtre Ubuntu devrait s'ouvrir automatiquement et te demander de créer un nom d'utilisateur et un mot de passe Linux (différents de ton compte Windows — choisis ce que tu veux, retiens-le bien).

Si aucune fenêtre ne s'ouvre après redémarrage, cherche "Ubuntu" dans le menu Démarrer et lance-le manuellement.

**Vérifie que ça fonctionne** : dans le terminal Ubuntu qui s'est ouvert, tape :
```bash
whoami
```
Tu dois voir le nom d'utilisateur que tu viens de créer (pas ton nom Windows).

> ⚠️ **Piège fréquent** : à partir de maintenant, utilise **toujours ce terminal Ubuntu/WSL** pour toutes les commandes qui suivent — pas PowerShell, pas l'invite de commandes Windows (cmd.exe), pas Git Bash. Si tu ouvres un terminal dans VS Code, vérifie qu'il indique bien `WSL` ou `Ubuntu` (regarde en haut à droite du panneau terminal, ou le prompt qui ressemble à `nom@PC:~$` plutôt qu'à `C:\Users\...>`).

---

## Étape 2 — Installer les outils nécessaires (dans le terminal WSL/Ubuntu)

Copie-colle ces commandes une par une :

```bash
sudo apt update
sudo apt install -y openjdk-21-jdk maven python3 python3-pip git
```

(Tu devras taper ton mot de passe Linux créé à l'étape 1 — c'est normal, rien ne s'affiche quand tu tapes, c'est une sécurité, continue et appuie sur Entrée.)

**Vérifie que tout est bien installé :**
```bash
java -version
mvn -version
python3 --version
git --version
```

Chaque commande doit afficher un numéro de version, sans erreur "command not found".

---

## Étape 3 — Cloner le dépôt

> ⚠️ **Piège fréquent** : clone le dépôt **à l'intérieur du système de fichiers Linux** (dans ton dossier personnel WSL, ex. `~/renewguard-ai`), **pas** sous `/mnt/c/...` (qui pointe vers ton disque Windows). C'est plus rapide et évite des problèmes de permissions.

```bash
cd ~
mkdir -p renewguard-ai
cd renewguard-ai
git clone <URL-DU-DÉPÔT> Renewguard-ai
cd Renewguard-ai
```

Remplace `<URL-DU-DÉPÔT>` par l'URL GitHub/GitLab du projet (demande-la si tu ne l'as pas).

**Bascule sur la bonne branche** (demande à l'équipe quelle branche contient le code à jour si tu n'es pas sûr — au moment de ce guide, c'est `mira-frontend`) :

```bash
git checkout mira-frontend
git pull origin mira-frontend
```

**Vérifie que les fichiers sont bien là :**
```bash
ls
```
Tu dois voir `pom.xml`, `src`, `mock_server.py` dans la liste.

---

## Étape 4 — Lancer le serveur mock (données de démo)

Ce serveur simule le backend pour l'instant (le vrai backend est en cours de développement — voir les notes en bas de ce fichier).

**Installe les dépendances Python** (une seule fois) :
```bash
pip install fastapi uvicorn --break-system-packages
```

**Lance le serveur :**
```bash
python3 mock_server.py
```

Tu dois voir un message du type `Uvicorn running on http://0.0.0.0:8000`. **Laisse ce terminal ouvert** — ne tape rien dedans, ne le ferme pas, il doit continuer à tourner pendant toute la durée où tu utilises l'application.

> ⚠️ **Si tu vois une erreur "address already in use"** : un ancien serveur tourne déjà. Trouve-le et arrête-le :
> ```bash
> lsof -i :8000
> kill <PID_affiché>
> ```
> Puis relance `python3 mock_server.py`.

---

## Étape 5 — Lancer l'application JavaFX

**Ouvre un second terminal** (garde le premier avec le serveur mock ouvert et actif) :

- Si tu es dans VS Code : clique sur le **`+`** en haut du panneau terminal pour ouvrir un nouvel onglet de terminal (vérifie qu'il est bien en WSL/Ubuntu, pas PowerShell).
- Sinon, ouvre une nouvelle fenêtre Ubuntu depuis le menu Démarrer.

Dans ce **nouveau** terminal :
```bash
cd ~/renewguard-ai/Renewguard-ai
mvn javafx:run
```

La première fois, cette commande peut prendre 1 à 2 minutes (téléchargement des dépendances). Une fenêtre de l'application devrait ensuite s'ouvrir automatiquement à l'écran.

> ℹ️ Sous Windows avec WSL2 récent, l'affichage graphique fonctionne normalement sans configuration supplémentaire (WSLg). Si aucune fenêtre n'apparaît après 2-3 minutes et qu'aucune erreur ne s'affiche non plus, dis-le à Mira — il peut y avoir une configuration d'affichage à ajuster selon ta version de Windows.

---

## Étape 6 — Utiliser l'application

Une fois la fenêtre ouverte, tu peux naviguer dans les différents écrans (Dashboard, Équipements, Historique, Priorités, chat LIA...). Toutes les données affichées sont **simulées** (voir Notes ci-dessous) — normal pour l'instant, c'est le but de cette phase.

---

## Pour arrêter proprement

1. Ferme la fenêtre de l'application JavaFX normalement.
2. Retourne dans le terminal du serveur mock, fais `Ctrl+C`.

## Pour relancer une prochaine fois

Tu n'as **pas besoin de refaire toutes les étapes** — seulement :
```bash
cd ~/renewguard-ai/Renewguard-ai
git pull origin mira-frontend    # pour récupérer les dernières mises à jour
python3 mock_server.py           # terminal 1
```
puis, dans un second terminal :
```bash
cd ~/renewguard-ai/Renewguard-ai
mvn javafx:run                   # terminal 2
```

---

## Option B — Installation Windows native, sans WSL

> ⚠️ **À savoir avant de commencer** : cette voie a été testée et s'est révélée plus fragile que WSL (les gestionnaires de paquets Windows comme `winget` et `chocolatey` ont échoué lors de nos tests). On privilégie donc ici les **installateurs graphiques officiels** plutôt que des commandes — plus fiable, même si un peu plus manuel. Si tu bloques quelque part, WSL (Option A ci-dessus) reste la solution la plus simple en dernier recours.

### B1 — Installer Git

1. Télécharge et installe : https://git-scm.com/download/win
2. Laisse toutes les options par défaut pendant l'installation.
3. Redémarre VS Code une fois l'installation terminée.

### B2 — Installer Java 21 (JDK)

1. Va sur https://adoptium.net
2. Choisis **Version 21 (LTS)**, package type **JDK**, pour Windows.
3. Télécharge le fichier `.msi` et lance-le.
4. **Important** : pendant l'installation, quand on te propose des options, coche bien **"Set JAVA_HOME variable"** et **"Add to PATH"** si ces cases apparaissent — l'installateur Adoptium les configure automatiquement pour toi, contrairement à une installation manuelle.
5. Une fois installé, **ferme et rouvre VS Code** (et tout terminal ouvert) pour que les nouvelles variables soient prises en compte.

**Vérifie** : ouvre un terminal dans VS Code (Terminal → Nouveau terminal, type "Command Prompt" ou "PowerShell") et tape :
```
java -version
```
Tu dois voir la version 21 s'afficher.

### B3 — Installer Maven

1. Va sur https://maven.apache.org/download.cgi
2. Télécharge le fichier **Binary zip archive** (ex. `apache-maven-3.9.x-bin.zip`).
3. Extrais ce zip dans un dossier simple, par exemple `C:\maven` (clic droit → "Extraire tout...").
4. Ajoute Maven au PATH **via l'interface graphique Windows** (plus fiable qu'en ligne de commande) :
   - Recherche "Variables d'environnement" dans le menu Démarrer, ouvre **"Modifier les variables d'environnement système"**.
   - Clique sur **"Variables d'environnement..."**.
   - Dans la liste du bas ("Variables système"), sélectionne **Path**, clique **Modifier**.
   - Clique **Nouveau**, ajoute le chemin vers le sous-dossier `bin` de Maven, par exemple `C:\maven\apache-maven-3.9.x\bin`.
   - Valide avec OK partout.
5. **Ferme et rouvre VS Code** entièrement (pas juste le terminal) pour que le changement soit pris en compte.

**Vérifie** :
```
mvn -version
```

### B4 — Installer Python

1. Va sur https://www.python.org/downloads/
2. Télécharge la dernière version pour Windows et lance l'installateur.
3. **Important** : sur le tout premier écran de l'installateur, coche bien la case **"Add python.exe to PATH"** en bas avant de cliquer sur "Install Now" — c'est l'erreur la plus fréquente si elle est oubliée.
4. **Ferme et rouvre VS Code** ensuite.

**Vérifie** :
```
python --version
```
(sous Windows natif, c'est `python`, pas `python3`)

### B5 — Cloner le dépôt depuis VS Code (sans ligne de commande)

1. Ouvre VS Code.
2. Clique sur l'icône **Source Control** dans la barre de gauche (icône de branche Git), ou `Ctrl+Shift+G`.
3. Clique sur **"Clone Repository"**.
4. Colle l'URL du dépôt Git, choisis un dossier de destination simple (ex. `C:\Projets\Renewguard-ai`).
5. Une fois cloné, VS Code te propose d'ouvrir le dossier — accepte.
6. En bas à gauche de VS Code, clique sur le nom de branche affiché, puis choisis **`mira-frontend`** dans la liste (ou demande à l'équipe la branche à jour).

### B6 — Lancer le serveur mock

Ouvre un terminal dans VS Code (Terminal → Nouveau terminal, type "Command Prompt") :
```
pip install fastapi uvicorn
python mock_server.py
```
(pas de `--break-system-packages` nécessaire sous Windows natif, contrairement à WSL/Linux)

Laisse ce terminal ouvert et actif.

### B7 — Lancer l'application JavaFX

Ouvre un **second** terminal dans VS Code (clique sur le `+` dans le panneau terminal) :
```
mvn javafx:run
```

La fenêtre de l'application doit s'ouvrir directement (pas besoin de configuration d'affichage supplémentaire sous Windows natif, contrairement à WSL).

---

## Notes techniques

- `mock_server.py` simule le backend pour la démo/présentation — il sera progressivement remplacé par le vrai backend FastAPI en cours de développement. Dans le fichier, les commentaires `[HERMAN]` marquent les parties qui seront branchées sur Firebase, et `[MIRA]` celles qui seront branchées sur le vrai moteur IA (prédiction, décision, LIA).
- Le serveur mock **doit** tourner avant de lancer l'application JavaFX, sinon l'écran de connexion ne pourra pas s'authentifier et l'application affichera une erreur.
- Ce guide part du principe que ton dépôt Git est déjà configuré avec un accès (clé SSH ou token). Si `git clone` te demande un mot de passe et que ça échoue, demande de l'aide pour configurer ton accès avant de continuer.

---

*Un problème pendant l'installation ? Note l'étape exacte et le message d'erreur complet, et demande de l'aide plutôt que d'improviser une solution — plusieurs pièges classiques sont documentés dans ce guide, mais pas forcément tous.*
