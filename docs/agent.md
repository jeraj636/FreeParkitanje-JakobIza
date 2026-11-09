# AI Agent

## Naloga
AI-agent uporabniku pomaga pri iskanju prostih parkirnih mest, pregledu obstoječih rezervacij, ustvarjanju novih rezervacij in preklicu rezervacij z uporabo naravnega jezika.

## Kje deluje
Agent deluje v klepetalnem vmesniku spletne ali mobilne aplikacije. Zahteve prejema prek endpointa `POST /api/agent/chat` v zalednem sistemu Java + Spring Boot. Za razumevanje uporabnikovih zahtev uporablja LLM, ki lahko kliče vnaprej določena orodja prek backend API-ja.

## Orodja

| Orodje | Opis (kot ga prejme model) | Parametri | API endpoint | Pisanje? (potrebna potrditev) |
|---|---|---|---|:---:|
| `findAvailableParking` | Poišče prosta parkirna mesta v izbranem časovnem intervalu. | `start`, `end` | `GET /api/parking/available` | Ne |
| `getMyReservations` | Vrne rezervacije trenutno prijavljenega uporabnika. | Brez; uporabnik se določi iz avtentikacije. | `GET /api/reservations/me` | Ne |
| `createReservation` | Ustvari rezervacijo izbranega parkirnega mesta za določen čas, če je mesto še prosto. | `parkingId`, `avtoId`, `start`, `end` | `POST /api/reservations` | Da |
| `cancelReservation` | Prekliče rezervacijo prijavljenega uporabnika, če ima uporabnik dovoljenje za preklic. | `reservationId` | `DELETE /api/reservations/{id}` | Da |

## Varnostna pravila

- [x] API se kliče z žetonом trenutno prijavljenega uporabnika.
- [x] Pred ustvarjanjem ali preklicem rezervacije je potrebna potrditev uporabnika, ki se preveri tudi v kodi.
- [x] Največje število korakov agenta: 5 na posamezno uporabniško zahtevo.
- [x] Parametri orodij se preverijo pred izvedbo, vključno z datumi, časovnimi intervali in identifikatorji.
- [x] Vsak korak se zabeleži v dnevnik, vključno z uporabljenim orodjem, rezultatom in morebitno napako.
- [x] Agent ne sme dostopati do rezervacij drugih uporabnikov ali samovoljno spreminjati podatkov.
- [x] Razpoložljivost parkirnega mesta se ponovno preveri v backendu neposredno pred ustvarjanjem rezervacije.
- [x] API-ključi in žetoni se ne zapisujejo v dnevnik.

## Scenariji vrednotenja

| Scenarij | Pričakovani rezultat | Rezultat |
|---|---|---|
| Uporabnik išče prosta mesta za določen termin. | Agent pokliče `findAvailableParking` in prikaže razpoložljiva mesta. | Za preveriti |
| Uporabnik želi pregledati svoje rezervacije. | Agent pokliče `getMyReservations` in prikaže samo rezervacije prijavljenega uporabnika. | Za preveriti |
| Uporabnik želi rezervirati prosto mesto. | Agent pridobi potrebne podatke, preveri razpoložljivost, zahteva potrditev in šele nato ustvari rezervacijo. | Za preveriti |
| Dva uporabnika hkrati rezervirata isto mesto v istem terminu. | Backend prepreči dvojno rezervacijo. | Za preveriti |
| Uporabnik želi preklicati rezervacijo. | Agent zahteva potrditev in prekliče rezervacijo samo, če je uporabnik do tega upravičen. | Za preveriti |
| Uporabnik poskuša pregledati rezervacijo druge osebe. | API zavrne dostop. | Za preveriti |
| Uporabnik vnese neveljaven časovni interval. | Agent zahteva popravek in ne pošlje neveljavne zahteve za rezervacijo. | Za preveriti |
| LLM vrne neveljavne parametre ali API odpove. | Sistem zavrne neveljavne parametre oziroma varno obravnava napako ter uporabniku poda razumljivo sporočilo. | Za preveriti |
