# Predlog projekta

## Problem

Parkirišča pred Fakulteto za računalništvo in informatiko (FRI) so pogosto zasedena, zato študenti, zaposleni in obiskovalci težko najdejo prosto parkirno mesto. Zaradi iskanja prostega mesta izgubljajo čas, zamujajo na predavanja ali sestanke in po nepotrebnem vozijo po parkirišču.

Cilj projekta je razviti aplikacijo, ki uporabnikom omogoča pregled razpoložljivih parkirnih mest ter rezervacijo izbranega mesta za določen čas.

## Uporabniki in vloge

| Vloga | Opis | Kaj lahko počnejo |
|---|---|---|
| Uporabnik | Študent, zaposleni ali obiskovalec FRI-ja. | Pregleduje prosta parkirna mesta, upravlja svoje avtomobile, ustvarja in preklicuje rezervacije. |
| Administrator | Oseba, ki upravlja sistem parkirišč. | Upravlja parkirna mesta, pregleduje rezervacije in upravlja podatke o parkirišču. |

## Uporabniške zgodbe

Oblika: *Kot <vloga> želim <cilj>, da bi <korist>.*

| # | Uporabniška zgodba | Prioriteta | Mejnik | Issue |
|---|---|---|---|---|
| 1 | Kot uporabnik želim pregledati prosta parkirna mesta, da lahko hitro najdem ustrezno mesto. | M | Midterm | TBD |
| 2 | Kot uporabnik želim rezervirati prosto parkirno mesto za izbran čas, da si zagotovim mesto ob prihodu. | M | Final | TBD |
| 3 | Kot uporabnik želim pregledati in preklicati svoje rezervacije, da lahko upravljam svoje načrte. | M | Final | TBD |
| 4 | Kot uporabnik želim shraniti podatke o svojem avtomobilu, da ga lahko uporabim pri rezervaciji. | M | Midterm | TBD |
| 5 | Kot administrator želim upravljati parkirna mesta, da so podatki o parkirišču posodobljeni. | S | Final | TBD |
| 6 | Kot uporabnik želim pomočnika z umetno inteligenco, da lahko parkirna mesta iščem z naravnimi stavki. | S | Final | TBD |
| 7 | Kot uporabnik želim uporabljati aplikacijo na telefonu, da lahko rezervacijo opravim tudi na poti. | M | Final | TBD |

Opomba: `TBD` pomeni, da je treba za uporabniško zgodbo še ustvariti GitHub issue in vpisati njegovo povezavo.

### Pogoji sprejemljivosti za zgodbo 1

- **Given:** Uporabnik je odprl aplikacijo.
- **When:** Izbere časovni interval, za katerega želi parkirati.
- **Then:** Sistem prikaže parkirna mesta, ki so v izbranem intervalu prosta.
- **And:** Zasedena mesta niso prikazana kot prosta.
- **And:** Če ni prostih mest, sistem prikaže ustrezno obvestilo.

## Primer uporabe mobilne aplikacije

Uporabnik na telefonu odpre aplikacijo pred prihodom na FRI. Izbere čas prihoda in odhoda ter pregleda prosta parkirna mesta. Nato izbere mesto, svoj avtomobil in potrdi rezervacijo.

Mobilna aplikacija omogoča hitro uporabo na poti, pregled rezervacij in iskanje prostih mest brez uporabe računalnika. Uporabnik lahko po potrebi rezervacijo tudi prekliče.

## Funkcionalnosti umetne inteligence

- **Funkcionalnost LLM:** Iskanje parkirnih mest z naravnim jezikom. Uporabnik lahko na primer napiše: »Poišči mi prosto parkirno mesto jutri med 9. in 11. uro.« LLM zahtevo pretvori v strukturirane parametre, ki jih uporabi zaledni API.
- **Naloga agenta:** Agent na podlagi uporabnikove zahteve poišče prosta parkirna mesta, predstavi razpoložljive možnosti in po uporabnikovi potrditvi ustvari rezervacijo prek API-ja.

Agent lahko uporablja naslednja orodja:
- `findAvailableParking`: poišče prosta parkirna mesta v izbranem časovnem intervalu.
- `getMyReservations`: vrne rezervacije prijavljenega uporabnika.
- `createReservation`: ustvari rezervacijo po preverjanju razpoložljivosti.
- `cancelReservation`: prekliče rezervacijo po preverjanju dovoljenj.

Končno preverjanje razpoložljivosti, avtentikacija in dovoljenja se izvajajo v zalednem sistemu, ne v LLM-ju.

## Načrt

| Mejnik | Uporabniške zgodbe in funkcionalnosti |
|---|---|
| Midterm (7. teden) | Osnovna podatkovna baza, uporabniki in vloge, upravljanje avtomobilov, prikaz prostih parkirnih mest ter osnovni API. |
| Final (12. teden) | Ustvarjanje in preklic rezervacij, mobilni vmesnik, administratorske funkcionalnosti, LLM-pomočnik, integracija agenta in testiranje. |

## Tveganja

- **Dvojne rezervacije:** Dva uporabnika lahko hkrati poskusita rezervirati isto mesto. Backend mora razpoložljivost preveriti ob ustvarjanju rezervacije in preprečiti prekrivanje časovnih intervalov.
- **Napačni odgovori LLM-ja:** Model lahko napačno razume datum ali čas. Pred izvedbo rezervacije mora sistem preveriti parametre in od uporabnika zahtevati potrditev.
- **Neavtoriziran dostop:** Uporabnik lahko poskusi dostopati do rezervacij druge osebe. Backend mora preverjati identiteto uporabnika in njegova dovoljenja.
- **Nedosegljiv LLM:** Če storitev umetne inteligence ne deluje, mora osnovna funkcionalnost rezervacij še vedno delovati prek običajnega uporabniškega vmesnika.
- **Napačni podatki o parkirišču:** Če podatki o parkirnih mestih niso posodobljeni, lahko sistem prikazuje napačno razpoložljivost. Podatke je treba vzdrževati in preverjati.
- **Prevelik obseg projekta:** Razvoj vseh funkcionalnosti hkrati lahko povzroči zamude. Najprej je treba dokončati osnovno rezervacijo, nato dodati mobilno aplikacijo in LLM-agenta.
