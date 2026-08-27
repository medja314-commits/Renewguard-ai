import numpy as np
import pandas as pd
from sklearn.linear_model import LinearRegression
from sklearn.ensemble import RandomForestRegressor
from sklearn.model_selection import train_test_split
from sklearn.metrics import mean_squared_error, r2_score
import joblib
from tests.test_metrics import calcul_mae, calcul_rmse


df = pd.read_csv("data/processed/dataset_features.csv", sep=",")

# Séparation des features et de la target
X = df[['heure', 'jour_semaine', 'lag_1h', 'lag_2h', 'lag_24h']]
y = df['production_solaire_kw']

# Split chronologique — pas de mélange, on respecte l'ordre temporel
X_train, X_test, y_train, y_test = train_test_split(
    X, y, test_size=0.2, shuffle=False
)

# Entraînement
model_lr = LinearRegression()
model_lr.fit(X_train, y_train)

model_rf = RandomForestRegressor(random_state=42)
model_rf.fit(X_train, y_train)

# Évaluation
y_pred_lr = model_lr.predict(X_test)
mae_lr = calcul_mae(y_test, y_pred_lr)
rmse_lr = calcul_rmse(y_test, y_pred_lr)

r2_lr = r2_score(y_test, y_pred_lr)

y_pred_rf = model_rf.predict(X_test)
mae_rf=calcul_mae(y_test, y_pred_rf)
rmse_rf=calcul_rmse(y_test, y_pred_rf)
r2_rf = r2_score(y_test, y_pred_rf)

print("Linear Regression:")
print(f"RMSE: {rmse_lr}")
print(f"R²: {r2_lr}")
print("\nRandom Forest:")
print(f"RMSE: {rmse_rf}")
print(f"R²: {r2_rf}")

# Sauvegarde des modèles
joblib.dump(model_rf, "models/random_forest_model.pkl")

model_rf_loaded = joblib.load("models/random_forest_model.pkl")