import numpy as np
import pandas as pd
import matplotlib.pyplot as plt 

df= pd.read_csv("data/processed/dataset_clean.csv", sep=",")    
df[ "timestamp"] = pd.to_datetime(df["timestamp"], format="%Y-%m-%d %H:%M:%S")
#Extraire l'heure depuis timestamp

df['heure']= df['timestamp'].dt.hour

#Extraire le jour de la semaine
df['jour_semaine']= df['timestamp'].dt.dayofweek

#Création lag 
df ['lag_1h']=df['conso_totale_kw'].shift(4)
df ['lag_2h']=df['conso_totale_kw'].shift(8)
df ['lag_24h']=df['conso_totale_kw'].shift(96)


# Correction des valeurs nulles dans les colonnes lag

df = df.dropna(subset=['lag_1h', 'lag_2h', 'lag_24h'])


df.to_csv("data/processed/dataset_features.csv", sep=",", index=False)