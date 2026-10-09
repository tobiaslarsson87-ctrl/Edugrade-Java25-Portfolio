# Assignment 2: Social Media Platform

- **Typ:** Gruppuppgift
- **Arbetstid:** ~2 veckor
- **Betyg:** IG/G/VG
- **Released:** Efter Lektion 7 (Exception Handling)
- **Deadline:** Vecka 1, Tisdag 30e december kl 9:00
- **Presentation:** Vecka 1, Tisdag 30e december kl 09:00-15:00 (15 min per grupp, OBLIGATORISK)

---

## Översikt

Bygg en social media-plattform (tänk liknande Instagram/Twitter) med Java 21, Hibernate och H2.

**G-level core skills:**

- Data model (5 Hibernate entities med relationships)
- Collections & Generics (4 collection types, generic repository)
- Lambda Expressions (5 typer: Comparator, Predicate, Function, Consumer, Supplier)
- Stream API (6 operations: trending, feed, engagement, filter, search, comments)
- Git Collaboration (3 branches/person, 4+ PRs, 2+ merge conflicts)
- Exception Handling (4 custom exceptions, try-with-resources, transaction rollback)
- Optional (3+ usages, chaining, null safety)
- File I/O (export/import/backup med NIO.2)
- Konsol-menu (8 functions)
- Presentation (15 min, alla members deltar)

**VG-level advanced skills (välj minst 3 av 6):**

- Modern Java Features (Records + Sealed + Pattern matching)
- Advanced Streams (custom collector / parallel streams / chained collectors / 2+ extra ops)
- Git Excellence (>8 PRs / detailed reviews / GitHub Actions / 4+ conflicts)
- Extended Konsolmenu (3 av 4: profile, export, analytics, backup)
- Testing (15+ JUnit tests)
- Code Quality (Clean Code + JavaDoc)

---

#### ⚠️ VIKTIGT: Modern Java Features

**Modern Java Features (Records, Sealed classes, Pattern matching) är VG-NIVÅ, INTE G-krav.**

- **För G:** Fokusera på Collections, Lambdas, Streams, Git, Exceptions, Optional, File I/O
- **För VG:** Modern Java är EN av 6 features (välj minst 3 totalt)

**Varför?** Lektion 10 (Modern Java) är självstudier i Week 5-6. G-studenter kan få godkänt baserat på Lektion 1-8.

---

## Teknisk Stack

- **Java 21** (LTS)
- **Maven 3.9+** (group ID: `se.edugrade.java25`)
- **H2 Database** (file-based för produktion, in-memory för tester)
- **Hibernate 6.6+** (ORM)
- **HikariCP 6.2+** (connection pooling)
- **JUnit 5** (testing, optional för G)

---

## Del 1: Data Model (5 Hibernate Entities)

### User Entity

**Attributes:**

-✅ id (primary key, auto-generated)

- ✅username (unique, required)
- ✅bio (text)
- ✅createdAt (timestamp)

**Relationships:**

- ✅followers (many-to-many med User)
- ✅following (many-to-many med User, bidirectional)
- ✅posts (one-to-many med Post)

**Required methods:**

- ✅equals() + hashCode() baserat på id

---

### Post Entity (Abstract)

**Inheritance:** SINGLE_TABLE strategy med discriminator column

**Attributes:**

-✅ id (primary key, auto-generated)

-✅ author (many-to-one med User)

-✅createdAt (timestamp)

**Relationships:**

-✅ comments (one-to-many med Comment)

-✅ likes (one-to-many med Like)

-✅ hashtags (many-to-many med Hashtag)

**Required methods:**

-????? abstract getContentType()

---

### Post Subclasses ✅ (4 typer)

**TextPost:**

- Attributes: text (string)
- Discriminator: "TEXT"

**ImagePost:**

- Attributes: imageUrl (string), caption (string)
- Discriminator: "IMAGE"

**VideoPost:**

- Attributes: videoUrl (string), durationSeconds (int)
- Discriminator: "VIDEO"

**LinkPost:**

- Attributes: url (string), title (string)
- Discriminator: "LINK"

---

### Comment Entity ✅

**Attributes:**

- id (primary key)
- post (many-to-one med Post)
- author (many-to-one med User)
- text (string)
- createdAt (timestamp)

---

### Like Entity ✅

**Attributes:**

- id (primary key)
- post (many-to-one med Post)
- user (many-to-one med User)
- createdAt (timestamp)

---

### Hashtag Entity ✅

**Attributes:**

- id (primary key)
- tag (string, unique)
- posts (many-to-many med Post)
- weeklyGrowth (int)

---

## Del 2: Collections & Generics

### 4 Collection Types (obligatoriska användningsfall)

**List:** ✅

- Använd för: ordered posts (feed, comment threads)

**Set:** ✅

- Använd för: followers/following (no duplicates)

**Map:** ✅

- Använd för: hashtag counts, user statistics

**PriorityQueue:** ?????

- Använd för: trending posts (heap-based sorting)

### Generic Repository Pattern ✅

**Interface:** `Repository<T>`

**Required methods:**

- create(T entity) → T
- findById(Long id) → Optional<T>
- findAll() → List<T>
- update(T entity) → T
- delete(Long id) → void

**Implementations:** UserRepository, PostRepository, CommentRepository, etc.

---

## Del 3: Lambda Expressions (5 typer)

**Comparator:**

- Använd för: sortera posts by date/popularity ✅

**Predicate:**   😕 

- Använd för: filter posts by criteria (popular, recent, etc.) ???

**Function:**

- Använd för: map User → username, Post → content ✅

**Consumer:**    😕 

- Använd för: forEach operations (print, log, etc.) ???

**Supplier:**      😕

- Använd för: factory methods (default objects) ???

**Best practices:**

- Använd method references där möjligt (User::getUsername)
- Lambdas ska vara korta rader

---

## Del 4: Stream API (6 operations för G)

### 1. Trending Hashtags

**Input:** All hashtags
**Output:** Top 10 hashtags (sorted by post count)
**Operations:** groupingBy, counting, sorted, limit

### 2. User Feed

**Input:** User + their following list
**Output:** Combined posts from all followed users (sorted by date, limit 50)
**Operations:** flatMap, sorted, limit

### 3. Engagement Rate

**Input:** User's posts
**Output:** Average engagement (likes + comments per post)
**Operations:** map, averagingInt, orElse

### 4. Filter Posts by Hashtag

**Input:** Hashtag tag
**Output:** All posts with that hashtag
**Operations:** filter, anyMatch

### 5. Search Posts

**Input:** Keyword
**Output:** Posts containing keyword (case-insensitive)
**Operations:** filter, toLowerCase, contains

### 6. Comment Thread

**Input:** Post
**Output:** Comments sorted chronologically (oldest first)
**Operations:** sorted, Comparator.comparing

---

## Del 5: Git samarbete ✅

**Feature Branches:**

- Minimum 3 branches per person
- Naming: `feature/*`, `fix/*`, `refactor/*`

**Pull Requests:**

- Minimum 4 PRs (totalt för gruppen)
- Code reviews från andra medlemmar
- Approved PRs innan merge

**Merge Conflicts:**

- Minimum 2 conflicts dokumenterade
- Dokumentera: fil, orsak, lösning, lärdomar

**Commit Messages:**

- Format: `feat:`, `fix:`, `refactor:`, `docs:`, `test:`

**.gitignore:**

- target/, .idea/, *.iml, data/,*.mv.db, *.log

---

## Del 6: Exception Handling

### Custom Exception Hierarchy (4 exceptions)

**SocialMediaException** (base class, extends RuntimeException)

**Subclasses:**

- UserNotFoundException
- InvalidContentException
- DuplicateUsernameException

### Try-with-resources

Använd för: File operations (BufferedWriter, Files.lines)

### Transaction Rollback

Använd för: Hibernate transactions (begin, commit, rollback on error)

---

## Del 7: Optional (3+ usages)

**Repository methods:** ✅

- findUserByUsername(String) → Optional<User>
- findPostById(Long) → Optional<Post>

**Chaining operations:** ✅

- map() för transformation
- flatMap() för nested Optionals
- orElse() / orElseThrow() för fallback

**Null safety:**

- Inga explicit null checks (if (x != null) förbjudet)

---

## Del 8: File I/O (NIO.2) ✅

### Export User Data 

**Method:** exportUserData(User, Path)
**Format:** JSON
**API:** Files.writeString() med StandardOpenOption.CREATE

### Import Posts 

**Method:** importPosts(Path)
**Format:** JSON eller CSV
**API:** Files.lines() med stream processing

### Database Backup

**Method:** backupDatabase(Path)
**Output:** 3 files (users.json, posts.json, hashtags.json)
**API:** Files.createDirectories()

---

## Del 9: Konsolmenu (8 functions för G)

### Core Functions (1-8)

1. Skapa post (välj typ: text/image/video/link)
2. Visa feed (följda användares posts)
3. Kommentera post
4. Gilla post
5. Följ användare
6. Sök posts (keyword)
7. Visa trending hashtags (top 10)
8. Avsluta

### Extra för VG (9-12, valfritt)

9. Visa user profile (stats)
10. Exportera användardata (JSON)
11. Visa analytics (engagement rate)
12. Backup database

**Requirements:** ✅

- Input validation
- Loop until "Avsluta"
- Clear prompts

---

## Del 10: Presentation (15 min, OBLIGATORISK)

**Demo (5 min):**

- Live execution
- Visa alla 4 post types
- Trending hashtags
- Export/import

**Technical Explanation (8 min):**

- Collections & Generics (2 min)
- Lambda & Stream API (3 min)
- Git Collaboration (1 min)
- Architecture (2 min)

**Q&A (2 min):**

- Alla medlemmar svarar

**VIKTIGT:** Alla gruppmedlemmar MÅSTE delta aktivt.

---

## Bedömning

### Godkänt (G) - ALLA moment krävs

**1. Data Model**

- [✅] 5 Hibernate entities (User, Post, Comment, Like, Hashtag)
- [✅] Post subclasses (TextPost, ImagePost, VideoPost, LinkPost)
- [✅] Relationships korrekt (@ManyToMany, @ManyToOne, @OneToMany)
- [✅] equals() + hashCode() baserat på id

**2. Collections & Generics**

- [ ] 4 collection types (List, Set, Map, PriorityQueue)

- [✅] Generic repository pattern (Repository<T>)
- [??] Type safety (inga raw types)

**3. Lambda Expressions**

- [???] 5 lambda typer (Comparator, Predicate, Function, Consumer, Supplier)
- [✅] Method references där möjligt

**4. Stream API**

- [???] 6 operations implementerade (trending, feed, engagement, filter, search, comments)
- [???] Korrekt collectors (groupingBy, counting, averagingInt, etc.)

**5. Git Collaboration**

- [✅] 3 branches per person (totalt 9+ för grupp om 3)
- [✅] 4+ Pull Requests med code reviews
- [✅] 2+ merge conflicts dokumenterade
- [✅] Conventional commit messages

**6. Exception Handling**

- [✅] 4 custom exceptions (SocialMediaException + 3 subclasses)
- [??] Try-with-resources för file operations
- [??] Transaction rollback vid exception

**7. Optional**

- [✅] 3+ repository methods returnerar Optional
- [??] Chaining (map, flatMap, orElse, orElseThrow)
- [??] Inga explicit null checks

**8. File I/O**

- [✅] Export user data (Files.writeString)
- [??] Import posts (Files.lines)
- [✅ ] Database backup (users.json, posts.json, hashtags.json)

**9. Konsolmenu**

- [??] 8 functions implementerade

- [✅] Input validation
- [??] Loop until "Avsluta"

**10. Presentation**

- [-] Demo utan errors
- [-] Technical explanation (8 min)

- [-] Alla medlemmar deltar

**11. Inlämning**

- [✅] GitHub URL
- [???] Group reflections (alla medlemmar)
- [???] Code compiles (mvn clean compile)

**Om något moment saknas:** IG (Icke Godkänt)

---

### Väl Godkänt (VG) - Välj minst 3 features

**För VG krävs:**

1. ✅ ALLA G-moment klarade
2. ✅ Välj minst 3 VG-features från listan nedan

---

**VG Feature 1: Modern Java Features**

Implementera ALLA 3 komponenter:

- [✅] Records (3 typer: PostDTO, UserProfileDTO, HashtagStatsDTO)
- [✅] Sealed classes (ContentType sealed interface med 4 final implementations)
- [✅] Pattern matching (switch exhaustive + instanceof)

---

**VG Feature 2: Advanced Streams**

Implementera minst EN av följande: ????

- [] Custom Collector implementation, ELLER

- [ ] Parallel streams med performance measurement (>2x speedup), ELLER
- [ ] Chained collectors (groupingBy + mapping + reducing), ELLER
- [ ] 2+ extra Stream operations (totalt 8+)

---

**VG Feature 3: Git Excellence**

Implementera minst TVÅ av följande:

- [ ] > 

- [✅] Detailed code review comments (5+ substantive per PR), ELLER
- [???] GitHub Actions CI (automated tests on PR), ELLER
- [??] 4+ merge conflicts dokumenterade

---

**VG Feature 4: Extended Konsolmenu**

Implementera minst TRE av följande:

- [✅] User profile (stats: followers, following, posts, likes), ELLER
- [✅] Export user data (JSON format), ELLER
- [??] Analytics dashboard (engagement rate, most liked post), ELLER
- [✅] Backup database (timestamped backups)

---

**VG Feature 5: Testing**

Implementera ALLA följande:

- [✅] 15+ JUnit 5 tests (CRUD, Stream API, edge cases)
- [✅] @ParameterizedTest (minimum 1)
- [✅] assertThrows (minimum 2)

---

**VG Feature 6: Code Quality**

Implementera ALLA följande:

- [✅] Clean Code (Java conventions, no magic numbers, methods <20 lines)
- [??] JavaDoc (all public methods)
- [??] Comprehensive README

---

## Inlämning

**GitHub Repository:**

- Gör en fork på tillhandahållet repository
- README.md med setup instructions

**Group Reflections:**

- Template: `group_reflections.md`
- Alla medlemmar lämnar in individuellt (300+ ord)

**Presentation:**

- 15 min live demo + explanation
- Alla medlemmar MÅSTE presentera

---

## FAQ

### 1. Behöver jag Modern Java Features för G?

**NEJ!** Modern Java är VG Feature 1 (optional för G).

För G: Fokusera på Collections, Lambdas, Streams, Git, Exceptions, Optional, File I/O.

### 2. Vilka lektioner måste jag ha gått igenom?

**För G:** Lektion 1-7 (Maven, Hibernate, Git, Lambdas, Streams, Generics, Exceptions)

**För VG:** Lektion 10 (Modern Java Features) - om du väljer VG Feature 1

### 3. Hur många Stream operations krävs?

**G:** 6 operations (trending, feed, engagement, filter, search, comments)

**VG:** Välj 1 av 4 alternativ (custom collector / parallel streams / chained collectors / 2+ extra ops)

### 4. Kan jag jobba individuellt?

**Ja**, men du måste fortfarande skapa 3 branches, 4 PRs, och dokumentera 2 merge conflicts (simulera med olika branches).

### 5. Hur dokumenterar jag merge conflicts?

För varje conflict dokumentera: fil, orsak, lösning, lärdomar (i README eller reflections).

### 6. Kan jag använda Jackson/Gson för JSON?

**Ja!** External libraries är OK. För VG rekommenderas Jackson/Gson (mer professionellt).

### 7. Hur många JUnit-tester krävs?

**G:** Inga tester krävs (optional).

**VG Feature 5:** 15+ tests.

### 8. Vad händer om vi inte hinner alla 8 konsolmenu-funktioner?

Kontakta läraren FÖRE deadline. Om du inte hinner alla 8 får du IG.

### 9. Måste alla gruppmedlemmar presentera?

**JA!** Alla medlemmar MÅSTE delta aktivt.

### 10. Vad händer om gruppen fungerar inte?

1. Kommunicera med gruppmedlemmar
2. Dokumentera problem
3. Kontakta läraren FÖRE deadline

Dåligt samarbete påverkar INTE individuellt betyg om du kan visa din contribution (Git commits, reflections).

---

**Version:** 3.0
**Senast uppdaterad:** 2025-12-09

