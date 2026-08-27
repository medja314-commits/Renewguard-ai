"""
RenewGuard AI - Generation du dataset synthetique
Etape F4 - Dataset Synthetique (partie deleguee du plan d'apprentissage)

Ce script genere UNIQUEMENT les donnees brutes simulees sur 14 jours :
- consommation de 5 equipements
- production solaire
Aucun nettoyage, aucune feature engineering, aucun modele ML ici.
"""

import numpy as np
import pandas as pd

# ---------------------------------------------------------------------
# 1. Parametres generaux
# ---------------------------------------------------------------------

# Graine aleatoire pour que le dataset soit reproductible
# (utile pour comparer tes resultats d'un entrainement a l'autre)
np.random.seed(42)

NB_JOURS = 14
PAS_MINUTES = 15  # resolution temporelle : une mesure toutes les 15 min
POINTS_PAR_JOUR = (24 * 60) // PAS_MINUTES  # = 96 points par jour

# On genere l'axe temporel complet : 14 jours x 96 points = 1344 lignes
DATE_DEBUT = "2026-08-01 00:00:00"
timestamps = pd.date_range(
    start=DATE_DEBUT,
    periods=NB_JOURS * POINTS_PAR_JOUR,
    freq=f"{PAS_MINUTES}min",
)

# ---------------------------------------------------------------------
# 2. Fonctions utilitaires
# ---------------------------------------------------------------------

def heure_decimale(ts_index):
    """
    Convertit chaque timestamp en heure decimale (ex: 14h30 -> 14.5).
    Ce n'est PAS une feature du dataset final : c'est juste une valeur
    intermediaire utilisee ici pour calculer des courbes realistes
    (jour/nuit, pic solaire, etc.). Elle n'est pas sauvegardee.
    """
    return ts_index.hour + ts_index.minute / 60.0


def est_weekend(ts_index):
    """
    Renvoie un tableau de booleens : True si le jour est un samedi
    ou un dimanche. Utilise uniquement pour moduler certains usages
    (climatisation, imprimante), pas sauvegarde comme colonne.
    """
    return ts_index.dayofweek >= 5  # 5 = samedi, 6 = dimanche


# ---------------------------------------------------------------------
# 3. Production solaire
# ---------------------------------------------------------------------

def generer_production_solaire(ts_index):
    """
    Simule une production solaire en cloche entre 6h et 18h, nulle
    en dehors. La hauteur du pic varie chaque jour (effet "nuage"),
    et un bruit fin est ajoute pour eviter une courbe parfaitement lisse.
    """
    heures = heure_decimale(ts_index)

    # Cloche de base : une demi-sinusoide centree sur 12h, active entre 6h et 18h
    cloche = np.sin((heures - 6) / 12 * np.pi)
    cloche = np.clip(cloche, 0, None)  # on force a 0 en dehors de 6h-18h

    # Facteur "nuage" : un coefficient different par jour (entre 0.5 = tres
    # nuageux et 1.0 = grand soleil), applique a toutes les mesures du jour
    nb_points = len(ts_index)
    jour_index = np.arange(nb_points) // POINTS_PAR_JOUR
    facteur_nuage_par_jour = np.random.uniform(0.5, 1.0, size=NB_JOURS)
    facteur_nuage = facteur_nuage_par_jour[jour_index]

    puissance_max_kw = 5.0  # puissance crete de l'installation simulee
    production = puissance_max_kw * cloche * facteur_nuage

    # Bruit realiste (passages nuageux courts, mesures imparfaites)
    bruit = np.random.normal(0, 0.1, size=nb_points)
    production = production + bruit

    # La production ne peut jamais etre negative
    production = np.clip(production, 0, None)

    return production


# ---------------------------------------------------------------------
# 4. Consommation des equipements
# ---------------------------------------------------------------------

def generer_conso_serveur(ts_index):
    """Serveur : consommation quasi constante, jour et nuit, faible bruit."""
    nb_points = len(ts_index)
    base = 0.5  # kW, allume en permanence
    bruit = np.random.normal(0, 0.02, size=nb_points)
    return base + bruit


def generer_conso_securite(ts_index):
    """
    Securite (cameras, alarme) : consommation faible et stable,
    legerement plus elevee la nuit (eclairage infrarouge, etc.).
    """
    heures = heure_decimale(ts_index)
    est_nuit = (heures < 6) | (heures >= 20)
    base = np.where(est_nuit, 0.15, 0.10)
    bruit = np.random.normal(0, 0.01, size=len(ts_index))
    return base + bruit


def generer_conso_eclairage(ts_index):
    """
    Eclairage : pics le matin tot et le soir, quasi nul en pleine journee
    (lumiere naturelle) et la nuit profonde.
    """
    heures = heure_decimale(ts_index)

    pic_matin = np.exp(-((heures - 6.5) ** 2) / (2 * 1.0 ** 2))
    pic_soir = np.exp(-((heures - 19.5) ** 2) / (2 * 1.5 ** 2))

    base = 0.3 * (pic_matin + pic_soir)
    bruit = np.random.normal(0, 0.02, size=len(ts_index))
    return np.clip(base + bruit, 0, None)


def generer_conso_climatisation(ts_index):
    """
    Climatisation : suit un pic en debut d'apres-midi (chaleur maximale),
    quasi eteinte la nuit. Fonctionne moins le week-end (bureaux fermes).
    """
    heures = heure_decimale(ts_index)
    weekend = est_weekend(ts_index)

    pic_chaleur = np.exp(-((heures - 14) ** 2) / (2 * 3.0 ** 2))
    base = 2.5 * pic_chaleur

    # Le week-end, la climatisation tourne au ralenti (bureaux fermes)
    base = np.where(weekend, base * 0.2, base)

    bruit = np.random.normal(0, 0.1, size=len(ts_index))
    return np.clip(base + bruit, 0, None)


def generer_conso_imprimante(ts_index):
    """
    Imprimante : utilisation ponctuelle, uniquement en heures de bureau
    (8h-18h) en semaine, avec des pics aleatoires representant des
    impressions. Quasi nulle le reste du temps et le week-end.
    """
    heures = heure_decimale(ts_index)
    weekend = est_weekend(ts_index)
    heures_bureau = (heures >= 8) & (heures < 18)

    nb_points = len(ts_index)
    # Veille (consommation minimale) par defaut
    conso = np.full(nb_points, 0.02)

    # Pendant les heures de bureau en semaine, on tire aleatoirement
    # des pics d'impression (environ 15% des mesures)
    utilisable = heures_bureau & (~weekend)
    tirage = np.random.random(nb_points) < 0.15
    pics = np.random.uniform(0.3, 0.8, size=nb_points)

    conso = np.where(utilisable & tirage, conso + pics, conso)
    bruit = np.random.normal(0, 0.01, size=nb_points)
    return np.clip(conso + bruit, 0, None)


# ---------------------------------------------------------------------
# 5. Assemblage du dataset final
# ---------------------------------------------------------------------

conso_serveur = generer_conso_serveur(timestamps)
conso_securite = generer_conso_securite(timestamps)
conso_eclairage = generer_conso_eclairage(timestamps)
conso_climatisation = generer_conso_climatisation(timestamps)
conso_imprimante = generer_conso_imprimante(timestamps)
production_solaire = generer_production_solaire(timestamps)

conso_totale = (
    conso_serveur
    + conso_securite
    + conso_eclairage
    + conso_climatisation
    + conso_imprimante
)

dataset = pd.DataFrame({
    "timestamp": timestamps,
    "conso_serveur_kw": conso_serveur.round(3),
    "conso_securite_kw": conso_securite.round(3),
    "conso_eclairage_kw": conso_eclairage.round(3),
    "conso_climatisation_kw": conso_climatisation.round(3),
    "conso_imprimante_kw": conso_imprimante.round(3),
    "conso_totale_kw": conso_totale.round(3),
    "production_solaire_kw": production_solaire.round(3),
})

# ---------------------------------------------------------------------
# 6. Export
# ---------------------------------------------------------------------

CHEMIN_SORTIE = "renew_dataset_synthetique_14j.csv"
dataset.to_csv(CHEMIN_SORTIE, index=False)

print(f"Dataset genere : {len(dataset)} lignes, {len(dataset.columns)} colonnes")
print(f"Periode : {dataset['timestamp'].min()} -> {dataset['timestamp'].max()}")
print(f"Fichier sauvegarde : {CHEMIN_SORTIE}")
print(dataset.head())
