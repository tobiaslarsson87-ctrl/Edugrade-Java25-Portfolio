# Adventure Awaits

## Innehåll

- [Adventure Awaits](#adventure-awaits)
  - [Innehåll](#innehåll)
  - [Översikt](#översikt)
  - [Förutsättningar](#förutsättningar)
  - [Fokus för gruppuppgiften](#fokus-för-gruppuppgiften)
  - [Krav](#krav)
    - [Inlämningskrav](#inlämningskrav)
    - [Dokumentationskrav](#dokumentationskrav)
    - [Presentation](#presentation)
  - [Bästa Praxis](#bästa-praxis)

## Översikt

Refaktorera ett befintligt projekt så att det följer SOLID-principerna, DRY och YAGNI. Lägg till loggning med SLF4J och Logback.

## Förutsättningar

- Java Development Kit (Amazon Corretto) 21 LTS
- Maven (för att bygga och köra projektet)
- Git (för att hantera versionshantering)
- JUnit 5 för enhetstester
- Lägg till loggning med `SLF4J` och `Logback`

## Fokus för gruppuppgiften

Denna uppgift fokuserar på:

- **SOLID-principer:** Refaktorera koden för att följa alla 5 SOLID-principer
- **DRY (Don't Repeat Yourself):** Eliminera all kodduplicering och upprepad logik
- **YAGNI (You Aren't Gonna Need It):** Ta bort onödig kod som inte används
- **Uppdatera dependencies:** Använd senaste stabila versioner av alla bibliotek
- **Loggning:** Implementera SLF4J och Logback för strukturerad loggning
- **Clean Code:** Förbättra kodkvalitet och läsbarhet
- **JavaDoc:** Dokumentera alla publika klasser och metoder

## Krav

- Ni ska refaktorera ett befintligt projekt så att det följer SOLID-principerna.
- Lägg till loggning med SLF4J och Logback.
- Uppdatera alla dependencies till senaste versioner.
- Följ DRY-principen och eliminera kodduplicering.
- Följ YAGNI-principen och ta bort onödig kod.
- Lägg till någon unik egen funktionalitet "feature" t.ex. "inventory, shield, armor, etc" som inte finns i det befintliga projektet.
- Det ska gå att skapa en ny karaktär och skapa ett eget vapen.
- Nödvändiga menyer ska finnas för att användaren ska kunna navigera i spelet.

### Inlämningskrav

- Användaren ska kunna spela spelet via terminalen (via ett menysystem).
- Applikationen ska vara komplett och körbar/Spelet ska fungera att spela och köra i terminalen.
- Koden ska följa SOLID-principerna.
- DRY-principen ska följas - ingen kodduplicering.
- YAGNI-principen ska följas - ingen onödig kod.
- Dependencies ska vara uppdaterade till senaste versioner.
- Loggning med SLF4J och Logback ska vara implementerat.
- Omfattande testning ska genomföras och dokumenteras. 
  - Så mycket kod som möjligt skall testas.
  - Sätte ringet min eller max på antal men testa så mycket ni kan och hinner med.

### Dokumentationskrav

- Kommentera koden enligt JavaDoc-standard.
- Skapa en `instructions.md`-fil med instruktioner för hur man kör projektet och använder applikationen.
- Använd GIT för versionshantering under hela utvecklingen.
- Skriv meningsfulla commit-meddelanden som följer vedertagna konventioner.
  - Skriv namn i commiten på vilka som har deltagit i commiten.

### Presentation

- Skapa en kort presentation av:
  - projektet.
  - er planering.
  - hur det gått.
  - hur ni testat applikationen.

## Bästa Praxis

- **SOLID Principer:** Följ SOLID-principerna i din design, särskilt Single Responsibility Principle för klasser och metoder.
- **Clean Code:** Använd beskrivande namn på klasser, metoder och variabler. Håll metoder korta och fokuserade.
- **Separation of Concerns:** Säkerställ att olika lager (DAO, Service, UI) har tydligt avgränsade ansvarsområden.
- **Exception Handling:** Implementera robust felhantering och visa meningsfulla felmeddelanden för användaren.
- **DRY (Don't Repeat Yourself):** Undvik kodduplicering genom att extrahera gemensam funktionalitet till återanvändbara metoder.
- **Testa Grundligt:** Testa manuellt att applikationen fungerar som den ska.
- **Versionshantering:** Använd Git effektivt, inklusive branches för olika funktioner eller experiment.
