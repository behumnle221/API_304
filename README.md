# 🏦 Bank API - Guide de Démarrage

## 📋 Prérequis

- **Java 11+** installé
- **MySQL 5.7+** installé et en cours d'exécution
- **Maven 3.6+** installé
- **Postman/Swagger** pour tester les endpoints (ou utiliser Swagger UI)

---

## 🚀 Démarrage Rapide

### **1. Setup MySQL**

```sql
-- Créer la base de données
CREATE DATABASE bank_db;

-- Vous pouvez vérifier que tout fonctionne
USE bank_db;
SHOW TABLES;
```

### **2. Configurer le Projet**

Ouvrez `src/main/resources/application.properties` et vérifiez:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bank_db
spring.datasource.username=root
spring.datasource.password=password
```

⚠️ **Modifier le password si votre MySQL a un mot de passe différent**

### **3. Lancer l'Application**

**Option A: Avec Maven (Terminal)**
```bash
cd bank-api
mvn clean install
mvn spring-boot:run
```

**Option B: Avec IDE (Eclipse/IntelliJ)**
1. Clic droit sur le projet → Run As → Maven build
2. Ou clic droit sur `BankApplication.java` → Run As → Java Application

**Succès! ✅ Vous verrez:**
```
Tomcat started on port(s): 8080
```

---

## 📡 Tester les Endpoints

### **Méthode 1: Swagger UI (Recommandé)** ⭐

1. Ouvrez votre navigateur: **http://localhost:8080/swagger-ui.html**
2. Vous verrez tous les endpoints documentés
3. Cliquez sur "Try it out" pour tester directement

### **Méthode 2: Postman / Insomnia**

#### **POST - Créer un Compte**
```http
POST http://localhost:8080/api/v1/accounts
Content-Type: application/json

{
  "titulaire": "Jean Dupont",
  "email": "jean@example.com",
  "telephone": "33612345678",
  "solde": 500.00
}
```

**Réponse (201 Created):**
```json
{
  "id": 1,
  "titulaire": "Jean Dupont",
  "email": "jean@example.com",
  "telephone": "33612345678",
  "solde": 500.00,
  "dateCreation": "2026-04-15T10:30:00",
  "statut": "ACTIF"
}
```

#### **GET - Lister tous les comptes**
```http
GET http://localhost:8080/api/v1/accounts
```

**Réponse (200 OK):**
```json
[
  {
    "id": 1,
    "titulaire": "Jean Dupont",
    "email": "jean@example.com",
    "telephone": "33612345678",
    "solde": 500.00,
    "dateCreation": "2026-04-15T10:30:00",
    "statut": "ACTIF"
  }
]
```

---

## ✅ Cas de Test

### **Test 1: Créer un compte valide**
```json
{
  "titulaire": "Alice Martin",
  "email": "alice@example.com",
  "telephone": "33687654321",
  "solde": 1000.00
}
```
✅ Réponse: 201 Created

### **Test 2: Email déjà existant**
```json
{
  "titulaire": "Bob Martin",
  "email": "alice@example.com",
  "telephone": "33612345678",
  "solde": 500.00
}
```
❌ Réponse: 409 Conflict - "L'email alice@example.com est déjà utilisé"

### **Test 3: Données invalides (email manquant)**
```json
{
  "titulaire": "Charlie",
  "telephone": "33612345678",
  "solde": 500.00
}
```
❌ Réponse: 400 Bad Request - erreur de validation

### **Test 4: Téléphone invalide (moins de 8 chiffres)**
```json
{
  "titulaire": "David",
  "email": "david@example.com",
  "telephone": "336",
  "solde": 500.00
}
```
❌ Réponse: 400 Bad Request

### **Test 5: Lister les comptes**
```http
GET http://localhost:8080/api/v1/accounts
```
✅ Réponse: 200 OK avec array de comptes

---

## 📁 Structure du Projet

```
bank-api/
├── pom.xml                          # Dépendances Maven
├── src/main/
│   ├── java/com/bank/
│   │   ├── BankApplication.java     # Point d'entrée
│   │   ├── controller/
│   │   │   └── AccountController.java
│   │   ├── service/
│   │   │   └── AccountService.java
│   │   ├── repository/
│   │   │   └── AccountRepository.java
│   │   ├── model/
│   │   │   └── Account.java
│   │   ├── dto/
│   │   │   ├── CreateAccountDTO.java
│   │   │   └── AccountResponseDTO.java
│   │   └── exception/
│   │       ├── AccountAlreadyExistsException.java
│   │       └── GlobalExceptionHandler.java
│   └── resources/
│       └── application.properties
```

---

## 🛠️ Troubleshooting

### **Erreur: "Unable to connect to database"**
- Vérifié que MySQL est démarré
- Vérifié l'URL, username, password dans `application.properties`
- Créé la base de données `bank_db`

### **Erreur: Port 8080 déjà utilisé**
Changez le port dans `application.properties`:
```properties
server.port=8081
```

### **Erreur de compilation Maven**
```bash
mvn clean install -U
```

---

## 📚 Endpoints Résumé

| Méthode | URL | Description |
|---------|-----|-------------|
| POST | `/api/v1/accounts` | Créer un compte |
| GET | `/api/v1/accounts` | Lister tous les comptes |
| GET | `/swagger-ui.html` | Documentation Swagger |

---

## 🎯 Prochaines Étapes

Ajouter les endpoints:
- `GET /api/v1/accounts/{id}` - Détail d'un compte
- `PUT /api/v1/accounts/{id}` - Modifier un compte
- `POST /api/v1/accounts/{id}/deposit` - Effectuer un dépôt
- `POST /api/v1/accounts/{id}/withdraw` - Effectuer un retrait

---

**Projet créé le:** 15 Avril 2026  
**Version:** 1.0.0
