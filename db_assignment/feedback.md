# Feedback

Projektet visar god förståelse för DAO-mönstret med tydlig separation mellan datalagring och affärslogik, vilket är värdefullt eftersom det gör koden modulär och testbar. 
Er reflektion om mobbprogrammering som skapade teamkänsla och gemensam förståelse är särskilt intressant. Det visar att ni förstår att kodkvalitet inte bara handlar om teknik 
utan också om hur vi lär tillsammans. Valet av JDBC framför Hibernate motiveras väl i rapporten med transparent koppling mellan kod och SQL, vilket verkligen hjälper när man 
bygger grundförståelse.

Ett utvecklingsområde som skulle kunna stärka projektet är strukturen kring undermenyerna. Ni har identifierat själva att vissa buggar kräver dubbla menyval för att komma tillbaka, 
vilket ofta uppstår när ansvarsfördelningen mellan menyklasser blir otydlig. Det vore intressant att utforska om AddAndShowAuthors, DeleteAuthor och UpdateAuthor skulle kunna 
konsolideras till en AuthorMenu med metodseparation, vilket skulle reducera komplexiteten samtidigt som tydligheten bibehålls. Er observation om att veta vad som är nödvändigt 
och vad som kan förenklas pekar precis på detta, ett centralt steg i att utvecklas från fungerande kod till underhållbar arkitektur.

Grymt jobbat!
