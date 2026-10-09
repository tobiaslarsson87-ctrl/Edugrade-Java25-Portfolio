# Adventure Awaits - Design Patterns Assignment

## Översikt

Bygg vidare på en delvis refaktorerad kodbas genom att identifiera kvarvarande SOLID-brott, städa legacy-kod och implementera designmönster. Lägg till minst en unik feature.

**Betyg**: G (Pass) eller VG (Väl Godkänd) + Bonus

---

## Innehållsförteckning

- [Adventure Awaits - Design Patterns Assignment](#adventure-awaits---design-patterns-assignment)
  - [Översikt](#översikt)
  - [Innehållsförteckning](#innehållsförteckning)
  - [Utgångspunkt](#utgångspunkt)
  - [Kända problem i kodbasen](#kända-problem-i-kodbasen)
    - [Legacy Methods (Backward Compatibility Code)](#legacy-methods-backward-compatibility-code)
    - [Shop - Static Methods (SRP/OCP-brott)](#shop---static-methods-srpocp-brott)
    - [Player - Too Many Responsibilities (SRP-brott)](#player---too-many-responsibilities-srp-brott)
    - [Menu - Duplicerad Logic (DRY-brott)](#menu---duplicerad-logic-dry-brott)
    - [GameEngine - Tight Coupling \& UI Mixed with Business Logic (SRP-brott)](#gameengine---tight-coupling--ui-mixed-with-business-logic-srp-brott)
  - [Betygskriterier](#betygskriterier)
    - [Godkänd (G)](#godkänd-g)
    - [Väl Godkänd (VG)](#väl-godkänd-vg)
    - [Bonus (Frivilligt)](#bonus-frivilligt)
  - [Designmönster - Var passar de?](#designmönster---var-passar-de)
    - [Command Pattern → Shop-systemet (G)](#command-pattern--shop-systemet-g)
    - [Decorator Pattern → Weapon-systemet (G)](#decorator-pattern--weapon-systemet-g)
    - [Observer Pattern → Game Events (VG)](#observer-pattern--game-events-vg)
    - [Chain of Responsibility → Damage System (Bonus)](#chain-of-responsibility--damage-system-bonus)
  - [Tekniska Krav](#tekniska-krav)
  - [Dokumentationskrav](#dokumentationskrav)
  - [Presentation (5-10 min)](#presentation-5-10-min)
  - [Inlämning](#inlämning)
  - [Support](#support)

---

## Utgångspunkt

Du får en **delvis refaktorerad kodbas** från live-kodningen med:

✅ CombatService (SRP), MonsterFactory (OCP), Combatant interface (DIP), Interface Segregation, Location/Difficulty enums
✅ SLF4J/Logback logging, grundläggande enhetstester, Maven build

**Din uppgift**: Identifiera kvarvarande SOLID/DRY/YAGNI-brott, städa legacy-kod, implementera designmönster.

---

## Kända problem i kodbasen

### Legacy Methods (Backward Compatibility Code)

Duplicerad funktionalitet från refaktoreringen finns bland annat i filerna:

- `Player.java`
- `Monster.java`
- `Boss.java`

**Åtgärd**: Refaktorera bort legacy-metoder, använd endast Combatant-interface metoder.

---

### Shop - Static Methods (SRP/OCP-brott)

**Shop.java**: Helt statiska metoder (`upgradeWeapon()`, `restoreHp()`) - svår att testa, bryter OCP.

**Åtgärd**: Command Pattern (se G-krav).

---

### Player - Too Many Responsibilities (SRP-brott)

**Player.java**: 145 rader, hanterar combat, gold, HP, XP, weapon, leveling.

**Åtgärd**: Överväg att bryta ut managers (valfritt för VG).

---

### Menu - Duplicerad Logic (DRY-brott)

**Menu.java rad 21-32 och 34-46**: Samma "play again"-logik för både död och vinst.

**Åtgärd**: Extrahera till metod `handlePlayAgain(Player, GameEngine)`.

---

### GameEngine - Tight Coupling & UI Mixed with Business Logic (SRP-brott)

**GameEngine.java**: `System.out.println()` i business logic (ex: `victoryEarnings()`), och game events är hårdkodade utan möjlighet till utökning.

**Åtgärd**: Observer Pattern för event-notifiering och UI-separation (VG-nivå).

---

## Betygskriterier

### Godkänd (G)

**Kodförbättringar**:

- Identifiera och dokumentera minst 3 SOLID/DRY-brott (kan väljas från "Kända problem" eller hittas själv)
- Åtgärda alla identifierade problem
- Städa bort alla legacy methods
- All ny kod följer SOLID-principer

**Designmönster (obligatoriska)**:

**Command Pattern - Shop-systemet**:

- Skapa `ShopCommand` interface med `execute(Player)`, `canExecute(Player)`, `getCost()`, `getDescription()`
- Minst 3 konkreta commands (ex: `UpgradeWeaponCommand`, `RestoreHealthCommand`, `BuyFireEnchantmentCommand`)
- Refaktorera Shop från static methods till instans med `List<ShopCommand>`
- Nya commands kan läggas till utan att ändra befintlig kod (OCP)

**Decorator Pattern - Weapon-systemet**:

- Skapa `WeaponComponent` interface med `getName()`, `getDamage()`
- Refaktorera befintliga `Weapon`-klassen till `BaseWeapon` som implementerar `WeaponComponent`
- Uppdatera `Player` att använda `WeaponComponent` istället för `Weapon`
- Minst 3 konkreta decorators (ex: `FireEnchantment`, `PoisonEnchantment`, `SharpnessEnchantment`)
- Decorators kan staplas (wrapping)
- Integrera med Shop (köp enchantments via Command)

**Funktionalitet**:

- Applikationen körbar via terminal med menysystem
- En unik feature implementerad (ex: save/load-system, inventory, quest-log)
- SLF4J/Logback logging fungerar

**Testing**:

- Command Pattern testas (minst 2 commands)
- Decorator Pattern testas (stacking, damage calculation)
- Minst 60% kodtäckning på ny/refaktorerad kod
- Hög täckning på resterande kodbas är önskvärt men inte krav

**Clean Code**:

- Metoder < 20 rader, klasser < 200 rader
- Konsekvent kodstil genom hela projektet
- Separation of Concerns genomgående

**Dokumentation**:

- JavaDoc för alla nya publika klasser och metoder (beskrivning, `@param`, `@return`, `@throws`)
- Git commits med tydliga meddelanden (format: `<type>: <description>`)

---

### Väl Godkänd (VG)

**Alla G-krav + följande**:

**Designmönster (obligatoriskt)**:

**Observer Pattern - Game Events**:

- Skapa `GameEventListener` interface med `onEvent(GameEvent, Object)`
- Skapa `GameEvent` enum (LEVEL_UP, COMBAT_WIN, PLAYER_DEATH, GOLD_EARNED, ITEM_PURCHASED)
- GameEngine implementerar Observable med `List<GameEventListener>` och `notifyListeners(GameEvent, Object)`
- Minst 3 konkreta listeners:
  - `StatisticsTracker` (räknar vinster, deaths, gold earned)
  - `AchievementSystem` (unlocks achievements baserat på events)
  - `ConsoleUIListener` (skriver ut game events till konsolen och loggar med SLF4J - ersätter `System.out.println` i GameEngine)
- Loose coupling: GameEngine känner inte till konkreta listeners

**Ytterligare SOLID-förbättringar**:

- Player SRP-refactoring (valfritt men rekommenderat)
- Minimera kvarvarande code smells

**Testing**:

- Observer Pattern testas (event notification, multiple listeners)
- Edge cases testade (null-hantering, tom lista)
- Integration tests (Command + Decorator + Observer)
- Minst 75% kodtäckning på ny/refaktorerad kod

---

### Bonus (Frivilligt)

**Chain of Responsibility - Damage System**:

- Skapa `DamageHandler` abstract class med `setNext(DamageHandler)` och `int handle(int damage, Combatant target)`
- Minst 3 konkreta handlers:
  - `ArmorHandler` (reducerar damage baserat på armor value)
  - `ShieldBlockHandler` (chans att blockera attack)
  - `CriticalHitHandler` (multiplicerar damage vid crit)
- Chain kan konfigureras per Combatant
- Integrera i CombatService
- Enhetstester för chain (test varje handler + full chain)

**Krav**: Korrekt GoF-implementation, fungerar med befintligt combat system, dokumenterat i presentation.

**Observera**: Bonus höjer inte betyget, men är en bra utmaning för den som vill fördjupa sig i designmönster.

---

## Designmönster - Var passar de?

<details>
<summary>Förklaring</summary>

### Command Pattern → Shop-systemet (G)

- **Problem**: Shop är helt statisk med hårdkodade actions
- **Metoder att ersätta**: `Shop.upgradeWeapon()`, `Shop.restoreHp()`
- **Lösning**: Skapa `ShopCommand` interface, konkreta commands för varje action, Shop blir instans med lista
- **Integration**: Shop itererar commands i menu, exekverar vald

---

### Decorator Pattern → Weapon-systemet (G)

- **Problem**: Weapon har inga enchantments/modifiers
- **Nuvarande**: `Weapon` har bara `name` och `damage`
- **Lösning**: `WeaponComponent` interface, decorators wraps andra components
- **Stacking**: `new FireEnchantment(new PoisonEnchantment(new BaseWeapon("Sword", 50)))`
- **Integration**: Shop kan sälja enchantments via commands (ex: `BuyFireEnchantmentCommand`)

---

### Observer Pattern → Game Events (VG)

- **Problem**: Tight coupling GameEngine → UI/logging
- **Metoder att lyssna på**: `GameEngine.victoryEarnings()` → COMBAT_WIN, `Player.levelUp()` → LEVEL_UP
- **Lösning**: GameEngine notifierar listeners istället för direct println
- **Listeners**: StatisticsTracker, AchievementSystem, ConsoleUIListener
- **Setup**: Main skapar listeners och registrerar via `gameEngine.addListener()`

---

### Chain of Responsibility → Damage System (Bonus)

- **Problem**: Ingen armor/shield mechanics, damage calculation är spread out
- **Metoder att ersätta**: `Monster.getDamageMultiplier()`, `Player.takeDamage()`
- **Lösning**: Damage går genom chain av handlers innan appliceras
- **Chain setup**: Player kan ha ShieldBlockHandler → ArmorHandler, Enemy kan ha CriticalHitHandler
- **Usage**: `int finalDamage = playerChain.handle(baseDamage, player); player.takeDamage(finalDamage);`

</details>

---

## Tekniska Krav

**Dependencies** (uppdatera i `pom.xml`):

- SLF4J: 2.x
- Logback: 1.4.x
- JUnit Jupiter: 5.10.x

**Loggning** (`logback.xml`):

- Console appender (utveckling) + File appender (produktion)
- INFO-nivå för viktiga events (game start/end, combat, level up)
- ERROR-nivå för exceptions (aldrig `printStackTrace()`)

**Applikationen**:

- Körbar via `IntelliJ`, `mvn exec:java` eller `java -jar`
- Fungerar komplett i terminal
- Inga runtime errors

---

## Dokumentationskrav

**INSTRUCTIONS.md**:

1. Systemkrav (Java 21, Maven)
2. Byggkommando (`mvn clean install`)
3. Körkommando
4. Spelmekanik (hur man spelar)
5. Implementerade features

---

## Presentation (5-10 min)

**Innehåll**:

1. **Projekt** (1-2 min): Demo av applikation och features
2. **Planering** (1-2 min): Tidlogg, utmaningar, lösningar
3. **Designmönster** (3-5 min): Visa kod för patterns, resonera om refactoreringen (t.ex. vad som gått bra dåligt)
4. **Testing** (1-2 min): Kodtäckning, exempel på tester
5. **Reflektion** (1-2 min): Lärdomar, vad kunde ha gjorts annorlunda

---

## Inlämning

**Deadline**: 2026-02-09 08:00

**Format**:

- Forkat GitHub repository **ej forkade repon kommer inte att rättas!**
- Presentation slides (PDF eller länk)

**Repository-struktur**:

```
root/
├── src/
├── pom.xml
├── INSTRUCTIONS.md
├── README.md
├── .gitignore
└── documentation/
    ├── personal_reflections.md
    └── presentation.pdf (eller länk i README)
```

**README.md minimum**:

- Projektnamn, författare, beskrivning
- Länk till INSTRUCTIONS.md
- Betygsambition (G/VG/VG+Bonus)

---

## Support

- Tekniska frågor: Teams
- Tips: Börja tidigt, committa ofta, testa löpande
