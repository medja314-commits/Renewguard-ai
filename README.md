# Renewguard-ai
Ce dépôt est crée en vu de développer une solution pour l'hackathon SIREX édition 2026

## Prérequis
- Java 21 (JDK)
- Maven
- Python 3 avec pip
- (Windows : recommande WSL pour éviter les galères d'installation)

## Lancer le serveur mock (données de démo)
1. `pip install fastapi uvicorn --break-system-packages`
2. `python3 mock_server.py`
   (doit écouter sur http://localhost:8000)

## Lancer l'application JavaFX
Dans un second terminal :
```
mvn javafx:run
```

## Notes
- `mock_server.py` simule le backend pour la démo — sera remplacé par le vrai backend FastAPI (voir tags [HERMAN] et [MIRA] dans le fichier).
- Le serveur mock doit tourner AVANT de lancer l'appli JavaFX, sinon l'écran de connexion ne pourra pas s'authentifier.
