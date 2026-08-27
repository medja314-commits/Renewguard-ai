import pandas as pd 
import numpy as np

df = pd.read_csv("data/raw/renew_dataset_synthetique_14j.csv", sep=",")

df[ "timestamp"] = pd.to_datetime(df["timestamp"], format="%Y-%m-%d %H:%M:%S")


#Vérification valeurs négatives ou nulles et remplacement par la médiane de la colonne
valides_serveur= df['conso_serveur_kw'][df['conso_serveur_kw']>=0]
mediane_serveur = valides_serveur.median()
df['conso_serveur_kw'] = np.where((df['conso_serveur_kw']<0) | (df['conso_serveur_kw'].isnull()), mediane_serveur, df['conso_serveur_kw'])

valides_securite= df['conso_securite_kw'][df['conso_securite_kw']>=0]
mediane_securite = valides_securite.median()
df['conso_securite_kw'] = np.where((df['conso_securite_kw']<0) | (df['conso_securite_kw'].isnull()), mediane_securite, df['conso_securite_kw'])

valides_eclairage= df['conso_eclairage_kw'][df['conso_eclairage_kw']>=0]
mediane_eclairage = valides_eclairage.median()
df['conso_eclairage_kw'] = np.where((df['conso_eclairage_kw']<0) | (df['conso_eclairage_kw'].isnull()), mediane_eclairage, df['conso_eclairage_kw'])

valides_climatisation= df['conso_climatisation_kw'][df['conso_climatisation_kw']>=0]
mediane_climatisation = valides_climatisation.median()
df['conso_climatisation_kw'] = np.where((df['conso_climatisation_kw']<0) | (df['conso_climatisation_kw'].isnull()), mediane_climatisation, df['conso_climatisation_kw'])

valides_imprimante= df['conso_imprimante_kw'][df['conso_imprimante_kw']>=0]
mediane_imprimante = valides_imprimante.median()
df['conso_imprimante_kw'] = np.where((df['conso_imprimante_kw']<0) | (df['conso_imprimante_kw'].isnull()), mediane_imprimante, df['conso_imprimante_kw']) 

valides_totale= df['conso_totale_kw'][df['conso_totale_kw']>=0]
mediane_totale = valides_totale.median()
df['conso_totale_kw'] = np.where((df['conso_totale_kw']<0) | (df['conso_totale_kw'].isnull()), mediane_totale, df['conso_totale_kw'])

valides_solaire= df['production_solaire_kw'][df['production_solaire_kw']>=0]
mediane_solaire = valides_solaire.median()
df['production_solaire_kw'] = np.where((df['production_solaire_kw']<0) | (df['production_solaire_kw'].isnull()), mediane_solaire, df['production_solaire_kw'])


#Vérification doublons
nb_doublons = df['timestamp'].duplicated().sum()
if nb_doublons > 0:
    df = df.drop_duplicates(subset='timestamp')

#Vérification timestamps chronologiquement
df = df.sort_values(by="timestamp").reset_index(drop=True)




df.to_csv("data/processed/dataset_clean.csv", sep=",", index=False)