"""
================================================================================
SERVEUR MOCK TEMPORAIRE POUR DÉMO / PRÉSENTATION
================================================================================

Ce fichier simule le backend FastAPI pour la démonstration du frontend JavaFX.
Il fournit des données statiques réalistes SANS vraie logique métier.

⚠️  IMPORTANT :
- Ce serveur est UNI QUEMENT pour la démo visuelle
- NE PAS utiliser en production
- Tous les endpoints répondent avec des données factices cohérentes
- Les timestamps sont générés dynamiquement mais les données métier sont statiques
- Les tags [HERMAN] et [MIRA] indiquent QUI DOIT remplacer ce code lors de
  l'implémentation réelle (voir commentaires ci-dessous)

LANCEMENT :
  pip install fastapi uvicorn python-dateutil --break-system-packages
  python mock_server.py

  L'API répond sur http://localhost:8000
  Disponible en parallèle avec : mvn javafx:run

================================================================================
"""

from fastapi import FastAPI, HTTPException, Query
from fastapi.middleware.cors import CORSMiddleware
import asyncio
import uuid
from datetime import datetime, timedelta
from typing import List, Optional
import json

app = FastAPI(title="RenewGuard Mock API")

# CORS pour JavaFX (peu probable d'être strictement nécessaire mais sécuritaire)
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# ============================================================================
# CONSTANTES ET DONNÉES STATIQUES
# ============================================================================

# Délai artificiel pour que les loaders soient visibles pendant la démo
SIMULATE_DELAY = 0.3  # secondes

# [DEMO UNIQUEMENT] État de coupure réseau pour la vidéo de présentation
secteur_coupe = False

# Données équipements (varié : serveur, sécurité, éclairage, clim, imprimante)
STATIC_EQUIPMENT = [
    {
        "id": 1,
        "name": "Serveur Data Center",
        "location": "Salle technique - B1",
        "icon": "server",
        "priority": "CRITICAL",
        "status": "ON",
        "power_watts": 3500,
        "last_activity": "2026-08-16T14:30:00",
        "ai_managed": False,
    },
    {
        "id": 2,
        "name": "Système de Sécurité",
        "location": "Entrée principale",
        "icon": "shield",
        "priority": "CRITICAL",
        "status": "ON",
        "power_watts": 850,
        "last_activity": "2026-08-16T14:32:15",
        "ai_managed": True,
    },
    {
        "id": 3,
        "name": "Climatisation Bureau A",
        "location": "Étage 1",
        "icon": "snowflake",
        "priority": "LOW",
        "status": "ON",
        "power_watts": 2200,
        "last_activity": "2026-08-16T14:25:00",
        "ai_managed": True,
    },
    {
        "id": 4,
        "name": "Éclairage Couloirs",
        "location": "Tous étages",
        "icon": "lightbulb",
        "priority": "LOW",
        "status": "ON",
        "power_watts": 450,
        "last_activity": "2026-08-16T14:15:30",
        "ai_managed": True,
    },
    {
        "id": 5,
        "name": "Imprimante Réseau",
        "location": "Bureau administratif",
        "icon": "printer",
        "priority": "LOW",
        "status": "OFF",
        "power_watts": 320,
        "last_activity": "2026-08-16T10:45:00",
        "ai_managed": False,
    },
    {
        "id": 6,
        "name": "Compteur Énergie Principal",
        "location": "Armoire électrique",
        "icon": "gauge",
        "priority": "CRITICAL",
        "status": "ON",
        "power_watts": 50,
        "last_activity": "2026-08-16T14:35:00",
        "ai_managed": False,
    },
]

# Données alertes statiques
STATIC_ALERTS = [
    {
        "id": 1,
        "severity": "WARNING",
        "title": "Batterie faible",
        "description": "Le niveau de batterie est descendu à 25%. L'autonomie diminue.",
        "timestamp": (datetime.now() - timedelta(minutes=15)).isoformat(),
        "resolved": False,
        "resolved_at": None,
        "suggested_action": "Activer économies d'énergie ou augmenter la production solaire",
    },
    {
        "id": 2,
        "severity": "INFO",
        "title": "Maintenance serveur programmée",
        "description": "Maintenance préventive du serveur data center prévue ce week-end.",
        "timestamp": (datetime.now() - timedelta(hours=2)).isoformat(),
        "resolved": False,
        "resolved_at": None,
        "suggested_action": "Vérifier la disponibilité et planifier l'arrêt",
    },
    {
        "id": 3,
        "severity": "CRITICAL",
        "title": "Réseau indisponible",
        "description": "Perte de connexion avec le réseau. Mode autonome activé.",
        "timestamp": (datetime.now() - timedelta(seconds=30)).isoformat(),
        "resolved": True,
        "resolved_at": datetime.now().isoformat(),
        "suggested_action": "Vérifier les connexions réseau",
    },
]

# Données décisions IA statiques
STATIC_DECISIONS = [
    {
        "id": 1,
        "action": "Éteindre",
        "equipment_name": "Éclairage Couloirs",
        "equipment_id": 4,
        "reason": "Capteur de présence : zone vide depuis 30 min",
        "impact_label": "Économie : 450W",
        "confidence_percent": 92,
        "status": "DONE",
        "timestamp": (datetime.now() - timedelta(minutes=45)).isoformat(),
        "acknowledged": True,
    },
    {
        "id": 2,
        "action": "Réduire puissance",
        "equipment_name": "Climatisation Bureau A",
        "equipment_id": 3,
        "reason": "Température stable + batterie < 30%",
        "impact_label": "Économie : 400W",
        "confidence_percent": 87,
        "status": "PENDING",
        "timestamp": (datetime.now() - timedelta(minutes=5)).isoformat(),
        "acknowledged": False,
    },
    {
        "id": 3,
        "action": "Adapter charge",
        "equipment_name": "Serveur Data Center",
        "equipment_id": 1,
        "reason": "Pic solaire détecté - charge différée possible",
        "impact_label": "Optimisation réseau",
        "confidence_percent": 78,
        "status": "DONE",
        "timestamp": (datetime.now() - timedelta(hours=1)).isoformat(),
        "acknowledged": True,
    },
]

# Données prédictions statiques
STATIC_PREDICTIONS = {
    "consumption_curve": [
        {"label": "10:00", "predicted": 5.2, "lower_bound": 4.8, "upper_bound": 5.6},
        {"label": "11:00", "predicted": 5.5, "lower_bound": 5.0, "upper_bound": 6.0},
        {"label": "12:00", "predicted": 4.8, "lower_bound": 4.2, "upper_bound": 5.4},
        {"label": "13:00", "predicted": 5.9, "lower_bound": 5.2, "upper_bound": 6.6},
        {"label": "14:00", "predicted": 6.2, "lower_bound": 5.5, "upper_bound": 6.9},
        {"label": "15:00", "predicted": 6.0, "lower_bound": 5.3, "upper_bound": 6.7},
        {"label": "16:00", "predicted": 5.7, "lower_bound": 5.0, "upper_bound": 6.4},
    ],
    "production_curve": [
        {"label": "10:00", "real": 4.1, "predicted": 4.0, "lower_bound": 3.5, "upper_bound": 4.5},
        {"label": "11:00", "real": 5.8, "predicted": 5.9, "lower_bound": 5.2, "upper_bound": 6.6},
        {"label": "12:00", "real": 7.2, "predicted": 7.1, "lower_bound": 6.4, "upper_bound": 7.8},
        {"label": "13:00", "real": 6.5, "predicted": 6.4, "lower_bound": 5.8, "upper_bound": 7.0},
        {"label": "14:00", "predicted": 5.8, "lower_bound": 5.1, "upper_bound": 6.5},
        {"label": "15:00", "predicted": 4.2, "lower_bound": 3.5, "upper_bound": 4.9},
        {"label": "16:00", "predicted": 2.1, "lower_bound": 1.4, "upper_bound": 2.8},
    ],
    "battery_forecast_label": "50% à 16:30",
    "solar_forecast_label": "Nuages attendus 15:00-16:00",
    "autonomy_forecast_label": "Autonomie : 5h estimée",
    "consumption_forecast_kw": 5.85,
    "production_forecast_kw": 5.15,
    "battery_forecast_percent": 50,
}

# Données historique statiques
STATIC_HISTORY = {
    "DAILY": [
        {"label": "Lun 12 Aug", "production_kwh": 42.5, "consumption_kwh": 38.2, "saved_kwh": 4.3, "co2_kg": 1.8, "grid_import_kwh": 0.0, "grid_export_kwh": 4.3},
        {"label": "Mar 13 Aug", "production_kwh": 45.1, "consumption_kwh": 40.3, "saved_kwh": 4.8, "co2_kg": 1.9, "grid_import_kwh": 0.0, "grid_export_kwh": 4.8},
        {"label": "Mer 14 Aug", "production_kwh": 38.9, "consumption_kwh": 42.1, "saved_kwh": 0.0, "co2_kg": 0.0, "grid_import_kwh": 3.2, "grid_export_kwh": 0.0},
        {"label": "Jeu 15 Aug", "production_kwh": 48.3, "consumption_kwh": 41.5, "saved_kwh": 6.8, "co2_kg": 2.7, "grid_import_kwh": 0.0, "grid_export_kwh": 6.8},
        {"label": "Ven 16 Aug", "production_kwh": 52.1, "consumption_kwh": 39.8, "saved_kwh": 12.3, "co2_kg": 4.9, "grid_import_kwh": 0.0, "grid_export_kwh": 12.3},
    ],
    "SUMMARY": {
        "total_saved_kwh": 28.2,
        "total_co2_kg": 11.3,
        "financial_savings_eur": 142.80,
        "solar_coverage_percent": 87.5,
        "grid_export_eur": 18.45,
        "grid_import_cost_eur": 0.0,
        "net_benefit_eur": 161.25,
        "monthly_projection_eur": 1847.50,
    },
}

# Données priorités statiques
STATIC_PRIORITIES = [
    {
        "level": 1,
        "priority": "CRITICAL",
        "label": "Critique",
        "description": "Équipements essentiels : sécurité, serveurs, systèmes de base",
        "equipment_ids": [1, 2, 6],
        "total_power_watts": 4400,
    },
    {
        "level": 2,
        "priority": "IMPORTANT",
        "label": "Important",
        "description": "Équipements confort et productivité",
        "equipment_ids": [],
        "total_power_watts": 0,
    },
    {
        "level": 3,
        "priority": "LOW",
        "label": "Non prioritaire",
        "description": "Équipements pouvant être arrêtés en cas de besoin",
        "equipment_ids": [3, 4, 5],
        "total_power_watts": 2970,
    },
]

# Données règles statiques
STATIC_RULES = [
    {
        "id": 1,
        "condition": "battery_percent < 20",
        "action": "turn_off_low_priority",
        "active": True,
        "priority_level": 1,
        "threshold_value": 20.0,
        "threshold_unit": "%",
    },
    {
        "id": 2,
        "condition": "solar_production > 70",
        "action": "defer_heavy_loads",
        "active": True,
        "priority_level": 2,
        "threshold_value": 70.0,
        "threshold_unit": "kW",
    },
    {
        "id": 3,
        "condition": "grid_frequency < 49.5",
        "action": "stabilize_load",
        "active": True,
        "priority_level": 1,
        "threshold_value": 49.5,
        "threshold_unit": "Hz",
    },
]

# ============================================================================
# [HERMAN] À REMPLACER : lire/écrire dans Firestore au lieu de données
# statiques. Voir collection "equipment" dans le schéma Firebase.
# ============================================================================

EQUIPMENT_DB = {eq["id"]: eq for eq in STATIC_EQUIPMENT}
ALERT_DB = {al["id"]: al for al in STATIC_ALERTS}
DECISION_DB = {dec["id"]: dec for dec in STATIC_DECISIONS}

next_equipment_id = max(eq["id"] for eq in STATIC_EQUIPMENT) + 1
next_alert_id = max(al["id"] for al in STATIC_ALERTS) + 1
next_decision_id = max(dec["id"] for dec in STATIC_DECISIONS) + 1


async def simulate_delay():
    """Délai artificiel pour que les loaders soient visibles en démo."""
    await asyncio.sleep(SIMULATE_DELAY)


def apply_sector_cutoff(equipment_list):
    """Applique la coupure secteur : équipements priorité 3 (LOW) passent à OFF."""
    if not secteur_coupe:
        return equipment_list

    result = []
    for eq in equipment_list:
        eq_copy = eq.copy()
        if eq_copy["priority"] == "LOW":
            eq_copy["status"] = "OFF"
        result.append(eq_copy)
    return result


# ============================================================================
# AUTH ENDPOINTS
# ============================================================================
# [HERMAN] À REMPLACER : implémenter vraie authentification avec Firebase Auth
# ============================================================================

@app.post("/auth/token")
async def auth_token(credentials: dict):
    """
    Accepte N'IMPORTE QUELS identifiants et renvoie un token JWT factice.
    [HERMAN] À remplacer par vraie validation Firebase Auth.
    """
    await simulate_delay()

    # Token factice (pas validé, pas signé réellement)
    token = str(uuid.uuid4())

    return {
        "access_token": token,
        "token_type": "Bearer",
        "expires_in": 86400,
        "username": credentials.get("username", "demo_user"),
        "site_name": "RenewGuard Demo Site",
        "role": "admin",
    }


# ============================================================================
# ENERGY ENDPOINTS
# ============================================================================

@app.get("/energy/snapshot")
async def energy_snapshot():
    """Retourne un snapshot énergie actuel (données statiques + variations légères)."""
    await simulate_delay()

    return {
        "solar_production": 6.45,
        "consumption": 6.12,
        "battery_percent": 73,
        "battery_eta": "Charge complète dans 2h30",
        "battery_charging": True,
        "battery_charge_rate_kw": 1.8,
        "grid_available": False if secteur_coupe else True,
        "grid_voltage": 230.2,
        "grid_frequency": 50.0,
        "grid_import_kw": 0.0,
        "ai_score": 89,
        "active_equipment_count": 5,
        "total_equipment_count": 6,
        "energy_mix": {
            "solar_percent": 92.0,
            "battery_percent": 5.0,
            "grid_percent": 3.0,
        },
        "energy_saved_kwh": 28.2,
        "co2_avoided_kg": 11.3,
        "last_ai_decision": {
            "id": 2,
            "action": "Réduire puissance",
            "equipment_name": "Climatisation Bureau A",
            "equipment_id": 3,
            "reason": "Température stable + batterie < 30%",
            "impact_label": "Économie : 400W",
            "confidence_percent": 87,
            "status": "PENDING",
            "timestamp": (datetime.now() - timedelta(minutes=5)).isoformat(),
            "acknowledged": False,
        },
    }


@app.post("/energy/snapshot/refresh")
async def energy_snapshot_refresh():
    """Retourne un nouveau snapshot (peut varier légèrement ou être identique)."""
    await simulate_delay()
    return await energy_snapshot()


# ============================================================================
# [HERMAN] À REMPLACER : lire/écrire dans Firestore au lieu de données
# statiques. Voir collection "equipment" dans le schéma Firebase.
# ============================================================================

@app.get("/equipment")
async def list_equipment():
    """Liste tous les équipements."""
    await simulate_delay()
    return apply_sector_cutoff(list(EQUIPMENT_DB.values()))


@app.get("/equipment/{equipment_id}")
async def get_equipment(equipment_id: int):
    """Récupère un équipement par ID."""
    await simulate_delay()

    if equipment_id not in EQUIPMENT_DB:
        raise HTTPException(status_code=404, detail="Equipment not found")

    eq = EQUIPMENT_DB[equipment_id].copy()
    if secteur_coupe and eq["priority"] == "LOW":
        eq["status"] = "OFF"
    return eq


@app.post("/equipment")
async def create_equipment(equipment: dict):
    """Crée un nouvel équipement et renvoie ce qui est envoyé avec un nouvel ID."""
    global next_equipment_id
    await simulate_delay()

    equipment["id"] = next_equipment_id
    EQUIPMENT_DB[next_equipment_id] = equipment
    next_equipment_id += 1

    return equipment


@app.put("/equipment/{equipment_id}")
async def update_equipment(equipment_id: int, equipment: dict):
    """Met à jour un équipement complètement."""
    await simulate_delay()

    if equipment_id not in EQUIPMENT_DB:
        raise HTTPException(status_code=404, detail="Equipment not found")

    equipment["id"] = equipment_id
    EQUIPMENT_DB[equipment_id] = equipment
    return equipment


@app.patch("/equipment/{equipment_id}")
async def patch_equipment(equipment_id: int, partial: dict):
    """Met à jour un équipement partiellement (ex. toggle statut)."""
    await simulate_delay()

    if equipment_id not in EQUIPMENT_DB:
        raise HTTPException(status_code=404, detail="Equipment not found")

    EQUIPMENT_DB[equipment_id].update(partial)
    return EQUIPMENT_DB[equipment_id]


@app.delete("/equipment/{equipment_id}")
async def delete_equipment(equipment_id: int):
    """Supprime un équipement."""
    await simulate_delay()

    if equipment_id not in EQUIPMENT_DB:
        raise HTTPException(status_code=404, detail="Equipment not found")

    del EQUIPMENT_DB[equipment_id]
    return {"status": "success", "message": f"Equipment {equipment_id} deleted"}


# ============================================================================
# [MIRA] À REMPLACER : brancher sur le vrai moteur de décision +
# modèles ML de prédiction. Cette réponse statique doit devenir le
# résultat réel de l'arbitrage 3 sources.
# ============================================================================

@app.get("/ai/decisions")
async def list_decisions(limit: Optional[int] = Query(None)):
    """Liste les décisions passées factices."""
    await simulate_delay()

    decisions = list(DECISION_DB.values())
    if limit:
        decisions = decisions[:limit]

    return decisions


@app.patch("/ai/decisions/{decision_id}")
async def patch_decision(decision_id: int, partial: dict):
    """Met à jour une décision (ex. acknowledge)."""
    await simulate_delay()

    if decision_id not in DECISION_DB:
        raise HTTPException(status_code=404, detail="Decision not found")

    DECISION_DB[decision_id].update(partial)
    return DECISION_DB[decision_id]


@app.get("/ai/predictions")
async def get_predictions():
    """
    Retourne prédictions statiques (conso/production à venir).
    [MIRA] À remplacer par vraie prédiction ML.
    """
    await simulate_delay()
    return STATIC_PREDICTIONS


# ============================================================================
# [MIRA] À REMPLACER : brancher LIA sur un vrai moteur conversationnel
# (LLM + contexte des décisions/état système) au lieu d'une réponse fixe.
# ============================================================================

@app.post("/ai/chat")
async def ai_chat(request: dict):
    """
    Simule LIA : renvoie une réponse texte factice selon le message.
    [MIRA] À remplacer par vrai LLM conversationnel.
    """
    await simulate_delay()

    message = request.get("message", "").lower()

    # Cas spécial : climatisation + (pourquoi ou coupé) + coupure secteur
    if (("climat" in message or "clim" in message) and
        ("pourquoi" in message or "coup" in message) and
        secteur_coupe):
        response = "La climatisation a été coupée car le réseau est actuellement indisponible. La priorité a été donnée aux équipements critiques (serveur, sécurité) pour préserver l'autonomie de la batterie."
    # Cas : climatisation + (pourquoi ou coupé) + pas de coupure secteur
    elif ("climat" in message or "clim" in message) and ("pourquoi" in message or "coup" in message):
        response = "La climatisation fonctionne normalement. Le réseau est actuellement disponible et tous les équipements restent actifs."
    # Réponses factices simples selon le contexte
    elif "batterie" in message:
        response = "La batterie actuelle est à 73%. Elle se charge à 1.8 kW et sera pleine dans environ 2h30. Pas de soucis à prévoir."
    elif "climat" in message or "clim" in message:
        response = "La climatisation du Bureau A consomme actuellement 2200W. Sa température est stable. Je suggère une réduction de 400W pour optimiser l'énergie."
    elif "économ" in message or "sauv" in message:
        response = "Depuis le début du mois, vous avez économisé 28.2 kWh grâce à votre système solaire. Cela représente une réduction de CO2 de 11.3 kg et des économies financières de 142.80€."
    elif "prédic" in message or "prévision" in message:
        response = "La consommation devrait rester autour de 5.85 kW aujourd'hui. La production solaire diminuera après 15h00 du fait des nuages attendus."
    else:
        response = "Je suis l'IA de RenewGuard. Demandez-moi sur la batterie, la climatisation, les prédictions ou les économies réalisées !"

    return {
        "message": response,
        "timestamp": datetime.now().isoformat(),
        "model_version": "mock-v1",
        "tokens_used": len(response.split()),
    }


# ============================================================================
# [HERMAN] À REMPLACER : lire/écrire dans Firestore au lieu de données
# statiques. Voir collection "alerts" dans le schéma Firebase.
# ============================================================================

@app.get("/alerts")
async def list_alerts(severity: Optional[str] = Query(None)):
    """Liste les alertes, optionnellement filtrées par sévérité."""
    await simulate_delay()

    alerts = list(ALERT_DB.values())
    if severity:
        alerts = [a for a in alerts if a["severity"] == severity.upper()]

    return alerts


@app.post("/alerts/{alert_id}/resolve")
async def resolve_alert(alert_id: int):
    """Marque une alerte comme résolue."""
    await simulate_delay()

    if alert_id not in ALERT_DB:
        raise HTTPException(status_code=404, detail="Alert not found")

    ALERT_DB[alert_id]["resolved"] = True
    ALERT_DB[alert_id]["resolved_at"] = datetime.now().isoformat()

    return ALERT_DB[alert_id]


# ============================================================================
# [HERMAN] À REMPLACER : lire/écrire dans Firestore au lieu de données
# statiques. Voir collection "history" dans le schéma Firebase.
# ============================================================================

@app.get("/history")
async def get_history(period: Optional[str] = Query("DAILY"),
                      from_date: Optional[str] = Query(None),
                      to_date: Optional[str] = Query(None)):
    """
    Retourne historique avec points de données.
    period peut être: DAILY, WEEKLY, MONTHLY
    """
    await simulate_delay()

    # Retourne les données du period sélectionné
    if period.upper() == "WEEKLY":
        return [
            {"label": "Sem 33", "production_kwh": 318.9, "consumption_kwh": 295.5, "saved_kwh": 23.4, "co2_kg": 9.3, "grid_import_kwh": 5.2, "grid_export_kwh": 23.4},
            {"label": "Sem 34", "production_kwh": 325.4, "consumption_kwh": 302.1, "saved_kwh": 23.3, "co2_kg": 9.3, "grid_import_kwh": 4.8, "grid_export_kwh": 23.3},
        ]
    elif period.upper() == "MONTHLY":
        return [
            {"label": "Juillet 2026", "production_kwh": 1280.5, "consumption_kwh": 1145.2, "saved_kwh": 135.3, "co2_kg": 54.1, "grid_import_kwh": 18.5, "grid_export_kwh": 135.3},
            {"label": "Août 2026", "production_kwh": 1320.8, "consumption_kwh": 1180.6, "saved_kwh": 140.2, "co2_kg": 56.1, "grid_import_kwh": 15.2, "grid_export_kwh": 140.2},
        ]
    else:  # DAILY
        return STATIC_HISTORY["DAILY"]


@app.get("/history/summary")
async def get_history_summary(period: Optional[str] = Query("DAILY")):
    """Retourne un résumé factice pour le period."""
    await simulate_delay()

    # Retourne toujours le même résumé pour la démo
    return STATIC_HISTORY["SUMMARY"]


@app.get("/history/export")
async def export_history(period: Optional[str] = Query("DAILY")):
    """Retourne historique en format exportable (CSV ou JSON)."""
    await simulate_delay()

    # Retourne un format factice CSV
    csv_content = """date,production_kwh,consumption_kwh,saved_kwh,co2_kg
2026-08-12,42.5,38.2,4.3,1.8
2026-08-13,45.1,40.3,4.8,1.9
2026-08-14,38.9,42.1,0.0,0.0
2026-08-15,48.3,41.5,6.8,2.7
2026-08-16,52.1,39.8,12.3,4.9"""

    return {
        "status": "success",
        "format": "csv",
        "period": period,
        "data": csv_content,
    }


# ============================================================================
# [HERMAN] À REMPLACER : lire/écrire dans Firestore au lieu de données
# statiques. Voir collections "priorities" et "rules" dans le schéma Firebase.
# ============================================================================

@app.get("/priorities")
async def list_priorities():
    """Liste les niveaux de priorité."""
    await simulate_delay()
    return STATIC_PRIORITIES


@app.put("/priorities")
async def update_priorities(priorities: List[dict]):
    """Met à jour les niveaux de priorité."""
    await simulate_delay()

    # Accepte et retourne ce qui est envoyé
    return priorities


@app.get("/rules")
async def list_rules():
    """Liste les règles."""
    await simulate_delay()
    return STATIC_RULES


@app.put("/rules")
async def update_rules(rules: List[dict]):
    """Met à jour les règles."""
    await simulate_delay()

    # Accepte et retourne ce qui est envoyé
    return rules


@app.patch("/rules/{rule_id}")
async def patch_rule(rule_id: int, partial: dict):
    """Toggle actif/inactif ou met à jour partiellement une règle."""
    await simulate_delay()

    # Cherche la règle et la met à jour
    for rule in STATIC_RULES:
        if rule["id"] == rule_id:
            rule.update(partial)
            return rule

    raise HTTPException(status_code=404, detail="Rule not found")


# ============================================================================
# [DEMO UNIQUEMENT] Endpoints temporaires pour la vidéo de présentation,
# à retirer avant le vrai développement.
# ============================================================================

@app.post("/demo/toggle-coupure")
async def demo_toggle_coupure():
    """[DEMO UNIQUEMENT] Inverse l'état de coupure secteur pour simulation réseau."""
    global secteur_coupe
    await simulate_delay()

    secteur_coupe = not secteur_coupe
    return {
        "status": "success",
        "secteur_coupe": secteur_coupe,
        "message": f"Secteur {'coupé' if secteur_coupe else 'disponible'}"
    }


@app.get("/demo/reset")
async def demo_reset():
    """[DEMO UNIQUEMENT] Réinitialise l'état de coupure secteur."""
    global secteur_coupe
    await simulate_delay()

    secteur_coupe = False
    return {
        "status": "success",
        "secteur_coupe": secteur_coupe,
        "message": "État réinitialisé à la normale (secteur disponible)"
    }


# ============================================================================
# HEALTH CHECK
# ============================================================================

@app.get("/health")
async def health():
    """Health check endpoint."""
    return {"status": "ok", "message": "Mock API is running"}


# ============================================================================
# LANCEMENT
# ============================================================================

if __name__ == "__main__":
    import uvicorn

    print("\n" + "="*70)
    print("🚀 SERVEUR MOCK RENEWGUARD LANCÉ")
    print("="*70)
    print("📍 URL: http://localhost:8000")
    print("📚 Docs: http://localhost:8000/docs")
    print("⚠️  Note: Ce serveur est TEMPORAIRE pour la démo uniquement")
    print("="*70 + "\n")

    uvicorn.run(app, host="0.0.0.0", port=8000)
