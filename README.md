# 🛒 Projet E-Commerce Java Spring Boot

Cette application est une plateforme d'e-commerce pédagogique complète développée avec **Spring Boot 3.3.5**. Elle sert de base solide pour une boutique en ligne, incluant la gestion du catalogue, du panier, des commandes et une interface d'administration, le tout en suivant une architecture logicielle propre et robuste.

---

## Table of Contents
1. [🚀 Fonctionnalités](#-fonctionnalités)
2. [🛠 Stack Technique](#-stack-technique)
3. [📁 Architecture du Projet](#-architecture-du-projet)
4. [💻 Installation et Lancement](#-installation-et-lancement)
5. [🔗 Accès Rapide](#-accès-rapide)
6. [🔑 Comptes de Test](#-comptes-de-test)
7. [🔧 Dépannage](#-dépannage)

## 🚀 Fonctionnalités
- **Catalogue interactif** : Navigation par catégories et visualisation détaillée des produits.
- **Gestion du Panier** : Ajout/suppression d'articles et tunnel d'achat complet.
- **Sécurité & Authentification** : Sécurisation via Spring Security (Session pour le web, support JWT pour les API).
- **Dashboard Admin** : Interface dédiée pour gérer les stocks, les catégories et les commandes.
- **Persistance locale** : Utilisation d'une base de données H2 sur fichier pour un démarrage instantané sans configuration externe.

---

## 🛠 Stack Technique
- **Langage** : Java 17
- **Framework** : Spring Boot 3.3.5
- **Documentation** : SpringDoc OpenAPI (Swagger UI)
- **Persistance** : Spring Data JPA + H2 (Fichier stocké par défaut sous `./data/ecommerce`)
- **Sécurité** : Spring Security (JWT via `jjwt`)
- **Frontend** : Thymeleaf + Ressources statiques (CSS/JS)
- **Build / Run** : Maven Wrapper (`mvnw`)

---

## 📁 Architecture du Projet
Le projet est structuré selon une architecture en couches pour faciliter la maintenance :

- `com.b2.e_commerce.controller` : Contrôleurs MVC pour les pages web et contrôleurs REST (sous le dossier `/REST`).
- `com.b2.e_commerce.service` : Logique métier et orchestration.
- `com.b2.e_commerce.repository` : Interfaces JPA pour l'accès aux données.
- `com.b2.e_commerce.entity` : Entités JPA (`User`, `Role`, `Product`, `Category`, `Order`, etc.).
- `com.b2.e_commerce.config` : Configuration de l'application (CORS, initialisation, sécurité).
- `com.b2.e_commerce.security` : Composants de filtrage et sécurité JWT.
- `src/main/resources/templates` : Vues Thymeleaf (Espace public et dossier `admin`).

---

## 💻 Installation et Lancement

### Prérequis
- **JDK 17** ou version supérieure.
- Git (optionnel).
- *Note : Maven n'a pas besoin d'être installé sur votre machine grâce au Maven Wrapper fourni.*

### Commandes de démarrage
1. **Linux / macOS** :
   ```bash
   ./mvnw clean spring-boot:run
   ```
2. **Windows** :
   ```powershell
   mvnw.cmd clean spring-boot:run
   ```
3. **Générer un exécutable (JAR)** :
   ```bash
   ./mvnw clean package
   java -jar target/*-SNAPSHOT.jar
   ```

---

## 🔗 Accès Rapide
| Service | URL |
| :--- | :--- |
| **Site Public** | [http://localhost:8080/](http://localhost:8080/) |
| **Swagger UI (API)** | [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) |
| **Connexion** | [http://localhost:8080/login](http://localhost:8080/login) |
| **Dashboard Admin** | [http://localhost:8080/admin/dashboard](http://localhost:8080/admin/dashboard) |
| **Console H2** | [http://localhost:8080/h2-console](http://localhost:8080/h2-console) |

> **Configuration H2 :**
> - **JDBC URL** : `jdbc:h2:file:./data/ecommerce`
> - **Utilisateur** : `sa`
> - **Mot de passe** : *(aucun)*

---

## 🔑 Comptes de Test

| Rôle | Email | Mot de passe | État |
| :--- | :--- | :--- | :--- |
| **USER** | `test@test.com` | `1234` | Créé au démarrage (`DataInitializer`). |
| **ADMIN** | `a@gmail.com` | `a@gmail.com` | Créé au démarrage (`DataInitializer`). |

---

## 🔧 Dépannage
- **Réinitialisation** : Pour remettre la base de données à zéro, arrêtez l'app et supprimez le dossier `/data`, attention les deux users seront quand même initialisé (si vous voulez les enlever, il faut aller dans le fichier `src/main/java/com/b2/e_commerce/DataInitializer.java`).
- **Conflit de port** : Si le port 8080 est déjà utilisé, modifiez la propriété `server.port` dans `src/main/resources/application.properties`.