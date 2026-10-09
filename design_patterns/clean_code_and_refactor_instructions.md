# Förtydliganden: Refaktorering & Clean Code

## Refaktorering vs Omskrivning

| Refaktorering (refactor) ✅             | Omskrivning (rewrite) ❌  |
|----------------------------------------|--------------------------|
| Förbättra kodens struktur              | Bygga om från scratch    |
| Beteendet bevaras                      | Ändra vad programmet gör |
| Stegvisa förbättringar                 | Radera och börja om      |
| Koden blir lättare att läsa/underhålla | Skapa "version 2"        |

**Er uppgift**: Städa upp befintlig kod, inte skapa ett nytt spel.

### Vad ni FÅR göra

- Byta namn på klasser, metoder, variabler
- Bryta ut kod till nya klasser/metoder
- Ta bort duplicerad kod
- Ändra menyernas utseende och ordning
- Lägga till en ny feature (inventory, armor, etc.)

### Vad ni INTE ska göra

- Radera projektet och börja om
- Ändra spelets grundläggande koncept
- Byta ut hela arkitekturen utan anledning

---

## Clean Code - Grundprinciper

### 1. Namngivning

Namn ska beskriva vad något ÄR eller GÖR.

```java
// Dåligt
int d;
void proc();

// Bra
int daysSinceCreation;
void calculateDamage();
```

### 2. Korta metoder med ett ansvar

En metod ska göra EN sak.

```java
// Dåligt: gör flera saker
void handleCombat() {
    // beräkna skada
    // uppdatera HP
    // skriv ut resultat
    // spara till fil
}

// Bra: uppdelat
void handleCombat() {
    int damage = calculateDamage();
    updateHealth(damage);
    displayResult(damage);
}
```

### 3. Undvik kodduplicering (DRY)

Ser du samma kod på flera ställen? Bryt ut till en metod.

### 4. Ta bort oanvänd kod (YAGNI)

Kommenterad kod, oanvända metoder, "kanske senare"-funktioner → radera.

### 5. Konsekvent formatering

Samma stil genom hela projektet (indrag, namnkonventioner, struktur).

### 6. Undvik magiska värden

Använd konstanter istället för hårdkodade värden.

```java
// Dåligt
if (player.getHealth() < 20) { ... }

// Bra
private static final int CRITICAL_HEALTH_THRESHOLD = 20;
if (player.getHealth() < CRITICAL_HEALTH_THRESHOLD) { ... }
```

### 7. Fail fast

Hantera felfall tidigt och returnera/kasta exception direkt.

```java
// Dåligt: djup nesting
void attack(Enemy enemy) {
    if (enemy != null) {
        if (enemy.isAlive()) {
            // faktisk logik här
        }
    }
}

// Bra: fail fast
void attack(Enemy enemy) {
    if (enemy == null || !enemy.isAlive()) {
        return;
    }
    // faktisk logik här
}
```

### 8. Kommentarer ska förklara VARFÖR, inte VAD

Koden ska vara självförklarande. Kommentarer behövs bara för att förklara varför.

```java
// Dåligt: upprepar vad koden gör
// Öka health med 10
player.setHealth(player.getHealth() + 10);

// Bra: förklarar varför
// Bonus för att besegra boss - belönar spelaren extra
player.setHealth(player.getHealth() + 10);
```

### 9. En abstraktion per fil/klass

Blanda inte olika ansvarsområden i samma klass.

```java
// Dåligt: Player hanterar både spellogik OCH filsparning
class Player {
    void attack() { ... }
    void saveToFile() { ... }  // Hör inte hemma här
}

// Bra: separata klasser
class Player { void attack() { ... } }
class PlayerRepository { void save(Player p) { ... } }
```

### 10. Använd tidigt return för läsbarhet

Undvik djupa if-else-kedjor.

```java
// Dåligt
String getStatus(Player p) {
    String result;
    if (p.isDead()) {
        result = "Dead";
    } else if (p.getHealth() < 20) {
        result = "Critical";
    } else {
        result = "Healthy";
    }
    return result;
}

// Bra
String getStatus(Player p) {
    if (p.isDead()) return "Dead";
    if (p.getHealth() < 20) return "Critical";
    return "Healthy";
}
```

---

## Definition of Done

En task är klar när den är **specifik, mätbar och verifierbar**.

### ❌ Dåligt exempel 1: Vag task

> "Fixa koden så den blir bättre"

Problem: Vad betyder "bättre"? När är det klart?

### ❌ Dåligt exempel 2: Ofullständig implementation

> "Implementera Logger som Singleton"
>
> *Tasken stängs efter att studenten testat att det "funkar" med testkod i main()*

Problem: "Det funkar i main" är **inte** samma sak som färdigt. En halvfärdig implementation skapar teknisk skuld och förvirring.

### ✅ Bra exempel: Logger Singleton

> "Implementera Logger som Singleton i `util/GameLogger.java`:
>
> - Skapa Singleton-klassen med privat konstruktor
> - Implementera `getInstance()` metod
> - Lägg till `info()`, `warning()`, `error()` metoder
> - Använd loggern i `Game.java` och `Combat.java` som referensexempel
> - Ta bort all `System.out.println` i dessa två filer"

Varför bra:

- Specifik fil där klassen ska skapas
- Tydlig lista på vad klassen ska innehålla
- **Specifika filer** för användning (inte "uppdatera överallt")
- Verifierbart: checklista att bocka av

### Varför specifika filreferenser?

**"Uppdatera alla filer som behöver loggning"** är dåligt eftersom:

- Scope är oklar → tasken blir aldrig "klar"
- Svårt att code-reviewa → vilka filer förväntades ändras?
- Risk för merge-konflikter → alla rör samma filer samtidigt
- Omöjligt att testa → vad ska testas?

**Bättre**: Dela upp i flera tasks med specifika filer per task.

### Checklista för en bra task

- [ ] Kan någon annan förstå vad som ska göras?
- [ ] Vet jag när jag är klar?
- [ ] Kan jag testa/verifiera resultatet?
- [ ] Är scopet begränsat till specifika filer/klasser?

---

## Git & Arbetsflöde

### Main är helig

- **Main är alltid körbar** - man ska kunna klona repot när som helst och köra spelet
- **Ofärdiga features får ALDRIG mergas till main** - ingen "det är nästan klart"
- **Main får aldrig vara bruten** - om det händer (t.ex. vid konflikthantering) fixas det OMEDELBART, inte om 1-10 dagar

### Kort/Issue-krav

- **Inget arbete utan kort** - alla tasks ska ha ett giltigt kort/issue innan arbete påbörjas
- **Ett kort = en person** - ingen jobbar på samma kort som någon annan
  - *Undantag*: Om någon inte levererar trots utlovad deadline får annan ta över
- **Kortet beskriver scopet** - om det inte står i kortet, gör det inte i samma PR

### Små ändringar, ofta

**Gyllene regeln**: 1-3 filer per merge request, max 4 i undantagsfall.

| ✅ Accepteras | ❌ Nekas blankt |
|--------------|----------------|
| 1-3 filer, fokuserad ändring | 15 filer, hundratals rader |
| En klass bryts ut | Hela arkitekturen byts ut |
| En feature, komplett | Halv feature + "fixar sen" |

### Stegvis refaktorering

Ska en klass brytas ut i 3-4 nya klasser?

```
❌ Fel: En mega-PR som gör allt samtidigt

✅ Rätt:
  PR 1: Bryt ut PlayerRepository
  PR 2: Bryt ut PlayerValidator
  PR 3: Bryt ut PlayerFactory
```

Varje PR ska vara komplett, testad och mergbar för sig.

### Branch-strategi

- Jobba **alltid** i feature branches, aldrig direkt i main
- Namnge branches tydligt: `feature/add-inventory`, `fix/combat-damage-calc`
- Håll branches kortlivade - merga ofta, undvik långlivade branches

### Merge/Pull Request

- PR ska reviewas av minst en annan gruppmedlem innan merge
- Kör applikationen lokalt och verifiera att det fungerar innan du godkänner
- Små PR:s = snabbare review = färre konflikter = alla glada

---

## Frihet i implementationen

**Dessa saker spelar ingen roll** – gör som ni vill:

- Menyernas utseende
- Ordning på val (svårighetsgrad först eller sist)
- Färger, formatering i terminalen
- Exakta formuleringar i meddelanden

**Fokusera istället på**:

- Att koden följer SOLID och Clean Code
- Att spelet fungerar
- Att ni kan motivera era refaktoreringar

---

## The Zen of Python

Dessa principer kommer från Python-världen men fångar kärnan i god programmering. Alla är inte direkt applicerbara på Java, men **andemeningen** är universell.

```
Beautiful is better than ugly.
Explicit is better than implicit.
Simple is better than complex.
Complex is better than complicated.
Flat is better than nested.
Sparse is better than dense.
Readability counts.
Special cases aren't special enough to break the rules.
Although practicality beats purity.
Errors should never pass silently.
Unless explicitly silenced.
In the face of ambiguity, refuse the temptation to guess.
There should be one-- and preferably only one --obvious way to do it.
Now is better than never.
Although never is often better than *right* now.
If the implementation is hard to explain, it's a bad idea.
If the implementation is easy to explain, it may be a good idea.
```

**Nyckelprinciper för er uppgift**:

- *"Readability counts"* → Kod läses oftare än den skrivs
- *"Simple is better than complex"* → Välj den enkla lösningen
- *"Explicit is better than implicit"* → Var tydlig, inte "smart"
- *"If the implementation is hard to explain, it's a bad idea"* → Om ni inte kan förklara er refaktorering, tänk om
