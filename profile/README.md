# M2: MBRS UML Profil

Ovaj folder sadrži definiciju našeg UML Profila (`mbrs.profile.uml`) koji predstavlja srž naše Model-Driven arhitekture. Profil definiše rečnik (stereotipe) koje ćemo koristiti prilikom crtanja modela.

## Kako otvoriti i urediti profil u Eclipse Papyrus-u

Pošto je `.profile.uml` fajl generisan iz koda, da biste ga bezbedno uređivali i dodavali atribute, uradite sledeće:

1. **Importujte projekat:**
   - Otvorite Eclipse Modeling Tools.
   - Idite na `File -> Import... -> General -> Existing Projects into Workspace`.
   - Izaberite folder `mbrs/profile` i kliknite `Finish`.

2. **Kreirajte Papyrus model iz postojećeg profila:**
   - Ako već nemate `.di` i `.notation` fajlove (Papyrus vizuelni modeli), Papyrus možda neće znati kako da otvori sirovi `.profile.uml` kao dijagram.
   - Da to rešite: Kliknite desni klik na `profile` folder -> `New -> Other... -> Papyrus -> Papyrus Project`.
   - Izaberite `Profile` i nazovite ga npr. `mbrs_visual_profile`. Zatim u taj novi projekat ručno prepišite stereotipe, **ILI** samo otvorite `mbrs.profile.uml` u `Model Explorer` pogledu (bez dijagrama) i tu dodajte šta treba desnim klikom.

## Spisak stereotipa za dodavanje (Preuzeto iz M2 Plana)

Ukoliko generisani kod nije povukao sve, obavezno dodajte sledeće stereotipe i njihove atribute (Tagged Values):

### 1. `<<Entity>>` (proširuje `Class`)
- Označava klasu koja će postati JPA Entitet (i generisati odgovarajuću tabelu).

### 2. `<<Attribute>>` (proširuje `Property`)
Dodati Tagged Values (atribute) za ovaj stereotip:
- `nullable` (EBoolean)
- `unique` (EBoolean)
- `length` (EInt)
- `precision` (EInt)

### 3. `<<Relation>>` (proširuje `Association`)
Dodati Tagged Values:
- `cascade` (String / Enum)
- `fetch` (String / Enum)

### 4. `<<Resource>>` (proširuje `Class`)
Označava REST kontroler. Tagged Values:
- `basePath` (String)

### 5. `<<Operation>>` (proširuje `Operation`)
Označava REST metodu. Tagged Values:
- `httpMethod` (String: GET, POST, PUT, DELETE)
- `path` (String)

## Važna napomena (Commit)
Nakon što uspešno uvezete ovo u Eclipse, podesite stereotipe i sačuvate model (tako da Papyrus izgeneriše stabilne `.di` i `.notation` fajlove), tek onda odradite `git add .` i `git commit` iz konzole.
