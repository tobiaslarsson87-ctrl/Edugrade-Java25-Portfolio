# Gruppuppgift: Bibliotekssystem med JDBC/Hibernate

**Utlämnad:** 2025-10-23
**Deadline:** 2025-11-06 kl 8:00
**Redovisning:** 2025-11-06 kl 9:00-15:00
**Tid:** 2 veckor
**Gruppstorlek:** 3-5 personer
**Betyg:** IG/G/VG

---

## Syfte

Denna uppgift syftar till att ge er färdigheter i att:

- Designa och implementera en relationsdatabas
- Använda JDBC eller Hibernate för persistens i Java
- Arbeta med Docker och MySQL
- Samarbeta i grupp med Git och GitHub
- Dokumentera och presentera ett projekt

---

## Uppgiftsbeskrivning

Bygg ett **enkelt bibliotekssystem** som en konsol-applikation i Java med MySQL som databas.

### Kärnfunktionalitet

Systemet ska kunna hantera:

- 📚 **Böcker** - titel, ISBN, utgivningsår
- ✍️ **Författare** - namn, födelsedatum, nationalitet
- 📋 **Utlåning** - vilken bok, när lånad, när återlämnad

---

## Krav

### 1. Datamodellering (Obligatoriskt)

- Skapa ett **ER-diagram** med minst 3 entiteter
- Definiera relationer mellan entiteter:
  - Book ↔ Author (många-till-en eller många-till-många)
  - Loan → Book (många-till-en)
  - Loan → Member (om ni väljer att ha medlemmar)
- Dokumentera i er rapport

**Exempel på struktur:**

```
┌─────────────┐         ┌─────────────┐
│   Author    │         │    Book     │
├─────────────┤         ├─────────────┤
│ id (PK)     │────<    │ id (PK)     │
│ name        │         │ title       │
│ birth_date  │         │ isbn        │
│ nationality │         │ pub_year    │
└─────────────┘         │ author_id(FK)│
                        └─────────────┘
                               │
                               │
                               ▼
                        ┌─────────────┐
                        │    Loan     │
                        ├─────────────┤
                        │ id (PK)     │
                        │ book_id (FK)│
                        │ loan_date   │
                        │ return_date │
                        └─────────────┘
```

### 2. Databas (MySQL)

- Använd **MySQL med Docker**
- Inkludera en `docker-compose.yml` i projektet
- DDL-scripts för att skapa tabeller (`schema.sql`)
- Seed-data för testning (`data.sql`)
- Minst **10 böcker, 5 författare, 5 lån** i testdata

### 3. Java-applikation (Maven)

- **Maven-projekt** med korrekt `pom.xml`
- Välj **antingen JDBC eller Hibernate** (motivera ert val!)
- Implementera **CRUD-operationer** för alla entiteter:
  - **Create** - Lägg till ny bok/författare
  - **Read** - Visa alla böcker, sök böcker
  - **Update** - Uppdatera bokinfo
  - **Delete** - Ta bort bok/författare

### 4. Avancerade Queries (Minst 2)

Implementera minst 2 av följande:

- 🔍 **JOIN**: Visa alla böcker med författarnamn
- 📅 **WHERE + ORDER BY**: Sök böcker utgivna efter år X, sortera efter titel
- 📊 **GROUP BY / Aggregering**: Visa antal böcker per författare
- 🔎 **Subquery**: Hitta alla böcker av författare från Sverige
- 📋 **Complex WHERE**: Sök böcker som är utlånade just nu

### 5. Funktionalitet (Konsol-menysystem)

Skapa ett textbaserat menyval:

```
=== Bibliotekssystem ===
1. Visa alla böcker
2. Lägg till ny bok
3. Sök bok efter titel
4. Låna ut bok
5. Återlämna bok
6. Visa aktiva lån
7. Lägg till författare
0. Avsluta
Välj alternativ:
```

**OBS:** En enkel konsol-app räcker.

### 6. Git & GitHub (Obligatoriskt)

- Forka **GitHub-repository** för projektet
- Använd **branches** för nya features
- Minst **2 commits per gruppmedlem**
- Använd **Kanban board** (GitHub Projects eller Trello)
- Inkludera en bra **README.md** med:
  - Projektbeskrivning
  - Setup-instruktioner (hur man kör projektet)
  - Team-medlemmar

### 7. Dokumentation (2-3 sidor)

Lämna in en kort rapport som innehåller:

1. **ER-diagram** (bild eller textformat)
2. **Tekniska val**:
   - Varför valde ni JDBC eller Hibernate?
   - Vilka för- och nackdelar såg ni?
3. **Reflektion**:
   - Hur fungerade grupparbetet?
   - Vad gick bra? Vad var utmanande?
   - Vad skulle ni göra annorlunda nästa gång?

### 8. Redovisning (15 minuter)

Varje grupp redovisar 2025-11-06:

- **Demo** (5-7 min): Kör er applikation, visa funktionalitet
- **ER-diagram** (2 min): Förklara er datamodell
- **Tekniska val** (3 min): JDBC vs Hibernate, varför valde ni som ni gjorde?
- **Q&A** (3 min): Svara på frågor från lärare/klasskamrater

---

## Tekniska detaljer

### Docker Compose (Exempel)

```yaml
version: '3.8'
services:
  mysql:
    image: mysql:8.0
    container_name: library_db
    environment:
      MYSQL_ROOT_PASSWORD: rootpassword
      MYSQL_DATABASE: library
      MYSQL_USER: user
      MYSQL_PASSWORD: password
    ports:
      - "3306:3306"
    volumes:
      - ./sql:/docker-entrypoint-initdb.d
```

### Maven Dependencies (Exempel)

```xml
<!-- För JDBC -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.33</version>
</dependency>

<!-- För Hibernate -->
<dependency>
    <groupId>org.hibernate</groupId>
    <artifactId>hibernate-core</artifactId>
    <version>6.2.7.Final</version>
</dependency>
```

### Projektstruktur (Exempel)

```
library-system/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── se/gritacademy/library/
│   │   │       ├── Main.java
│   │   │       ├── dao/
│   │   │       │   ├── BookDAO.java
│   │   │       │   └── AuthorDAO.java
│   │   │       ├── model/
│   │   │       │   ├── Book.java
│   │   │       │   ├── Author.java
│   │   │       │   └── Loan.java
│   │   │       └── util/
│   │   │           └── DatabaseConnection.java
│   │   └── resources/
│   │       ├── hibernate.cfg.xml (om Hibernate)
│   │       └── application.properties
├── sql/
│   ├── schema.sql
│   └── data.sql
├── docker-compose.yml
├── pom.xml
└── README.md
```

---

## Bedömning

### Godkänt (G)

För att få godkänt ska gruppen:

- ✅ Ha skapat ett fungerande ER-diagram med 3 entiteter
- ✅ Implementerat alla CRUD-operationer
- ✅ Valt och implementerat JDBC eller Hibernate
- ✅ Ha minst 2 avancerade queries (JOIN, WHERE, etc.)
- ✅ Använt MySQL med Docker
- ✅ Använt Git/GitHub med branches och commits
- ✅ Lämnat in rapport med ER-diagram och reflektion
- ✅ Genomfört en fungerande redovisning med demo

### Väl Godkänt (VG)

För VG krävs **alla G-krav plus**:

- 🌟 **Välgrundad teknisk motivering**: Djup analys av JDBC vs Hibernate med konkreta exempel från ert projekt
- 🌟 **Självständig datamodellering**: Komplexare ER-diagram (t.ex. många-till-många med mellantabell, fler entiteter)
- 🌟 **Avancerad implementation**:
  - Korrekt användning av transaktioner där relevant
  - Connection pooling eller session management
  - Felhantering med try-catch och logging
- 🌟 **God kodstruktur**:
  - Separation of concerns (DAO-pattern)
  - Återanvändbar och läsbar kod
  - Kommentarer där det behövs

---

## Tips för framgång

### Vecka 1

1. **Planera tillsammans** (Dag 1):
   - Rita ER-diagram på whiteboard
   - Sätt upp GitHub-repo och Kanban board
   - Fördela uppgifter
2. **Setup** (Dag 2-3):
   - Docker + MySQL
   - Maven-projekt
   - Skapa tabeller
3. **Börja koda** (Dag 4-5):
   - Implementera CRUD för en entitet först (t.ex. Book)
   - Testa att det fungerar innan ni går vidare

### Vecka 2 (Dag 6-7)

1. **Färdigställ funktionalitet** (Dag 6-7):
   - Alla CRUD-operationer
   - Avancerade queries
   - Menysystem
2. **Dokumentation & Redovisning** (Dag 8):
   - Skriv rapport
   - Förbered demo
   - Öva redovisning
3. **Redovisning** 2025-11-06:
   - Demo
   - ER-diagram
   - Tekniska val
   - Reflektion

### Git-workflow

```bash
# Skapa branch för ny feature
git checkout -b feature/add-book-crud

# Arbeta och commit
git add .
git commit -m "Add Book CRUD operations"

# Pusha och skapa Pull Request
git push origin feature/add-book-crud
```

---

## Vanliga frågor

**Q: Måste vi ha GUI?**
A: Nej! En konsol-app med textmeny räcker utmärkt.

**Q: Kan vi välja annan domän än bibliotek?**
A: Ja, men diskutera med läraren först. Bibliotek är testat och lagom komplext.

**Q: Måste alla i gruppen koda lika mycket?**
A: Ja, alla ska bidra. Använd Git för att visa vem som gjort vad.

**Q: Kan vi använda både JDBC och Hibernate?**
A: Ja, men det är inte nödvändigt. Välj en och förklara varför.

**Q: Vad händer om vi inte hinner klart?**
A: Lämna in det ni har. Bättre att visa något ofärdigt än inget alls.

**Q: Hur lång ska rapporten vara?**
A: 2-3 sidor. Fokus på kvalitet, inte kvantitet.

---

## Resurser

### JDBC

- [JDBC Tutorial](https://docs.oracle.com/javase/tutorial/jdbc/basics/index.html)
- [Try-with-resources](https://www.baeldung.com/java-try-with-resources)

### Hibernate

- [Hibernate Getting Started](https://hibernate.org/orm/documentation/getting-started/)
- [JPA/Hibernate Tutorial](https://www.baeldung.com/jpa-hibernate-tutorials)

### Docker

- [Docker Compose Tutorial](https://docs.docker.com/compose/gettingstarted/)
- [MySQL Docker Image](https://hub.docker.com/_/mysql)

### Git

- [Git Branching](https://git-scm.com/book/en/v2/Git-Branching-Basic-Branching-and-Merging)
- [GitHub Flow](https://guides.github.com/introduction/flow/)

---

**Lycka till med projektet! 🚀📚**

*Kom ihåg: Det viktigaste är att ni lär er och samarbetar bra. Perfektion är inte målet!*
