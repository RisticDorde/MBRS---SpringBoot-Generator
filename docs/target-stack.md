# Target Stack & Dependencies

Ovaj dokument definiše tačne verzije i tehnologije koje će naš generator praviti (Spring Boot stack). Odlučeno je da se koristi moderna, ali stabilna verzija alata.

## Core
- **Java**: 21 (LTS)
- **Spring Boot**: 3.1.5 (ili novija stabilna 3.x verzija)
- **Build Tool**: Maven

## Baze podataka & JPA
- **Spring Data JPA** (Hibernate kao provider)
- **PostgreSQL**: Za produkciju i glavno okruženje
- **H2 Database**: In-memory baza za lokalno testiranje i brzi razvoj (`dev` profil)

## Dodatne zavisnosti (Dependencies)
- **Spring Web**: Za REST kontrolere.
- **Lombok**: Smanjenje boilerplate koda (getteri, setteri, konstruktori).
- **MapStruct**: Za mapiranje između Entity klasa i DTO objekata (radi brže od refleksije i generiše kod u compile-time).
- **SpringDoc OpenAPI (Swagger)**: Za automatsko generisanje REST API dokumentacije.

## Pravila za generator
Generator (Acceleo) će za generisane projekte ubaciti u njihov `pom.xml` upravo ove verzije i zavisnosti kako bi osigurao da generisani kod uvek uspešno prolazi kompilaciju.
