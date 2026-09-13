# Setup Guide

Ovaj vodič objašnjava kako članovi tima (Đorđe, Ana, Anđela) treba da konfigurišu svoje okruženje kako bi mogli da rade na ovom repozitorijumu.

## 1. Eclipse Modeling Tools
Za rad na UML profilima i kodu generatora, neophodno je koristiti **Eclipse Modeling Tools** (a ne običan Eclipse za Java developere).
1. Idi na zvanični sajt: [Eclipse Packages](https://www.eclipse.org/downloads/packages/)
2. Skini **Eclipse Modeling Tools** verziju (preporučeno najnovije stabilno izdanje).
3. Raspakuj zip i pokreni `eclipse.exe`.

## 2. Instalacija Papyrus-a
Papyrus se koristi za crtanje UML dijagrama i definisanje profila.
1. U Eclipse-u idi na **Help → Eclipse Marketplace...**
2. U polje za pretragu kucaj `Papyrus`
3. Pronađi **Papyrus UML** i klikni **Install**.
4. Prati uputstva, prihvati licencu i restartuj Eclipse.

## 3. Instalacija Acceleo-a i OCL Tools-a
Acceleo se koristi za prevođenje UML modela u kod (Model to Text).
1. Opet otvori **Eclipse Marketplace**
2. Kucaj `Acceleo` i instaliraj najnoviju verziju.
3. Kucaj `OCL` (Object Constraint Language) i osiguraj da su OCL alati instalirani (često dolaze uz Modeling alate, ali vredi proveriti u Marketplace-u).

## 4. Kloniranje projekta
Nakon što su instalirani dodaci:
1. Kloniraj ovaj repozitorijum:
   `git clone https://github.com/RisticDorde/MBRS---SpringBoot-Generator.git`
2. Iz Eclipse-a idi na **File → Import → General → Existing Projects into Workspace** kako bi uvezao projekte (npr. `profile` i `generator` foldere kad ih popunimo fajlovima).
3. Za `framework` i generisani Spring Boot kod možeš paralelno koristiti **IntelliJ IDEA**.

*(Slobodno ovde kasnije dodajte skrinšotove ukoliko neko iz tima naiđe na probleme)*
