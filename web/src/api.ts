// Vsi klici API-ja gredo skozi to datoteko.
// Naslov je relativen: med razvojem ga Vite posreduje na strežnik,
// v produkciji sta stran in API na istem naslovu.
const API_URL = '/api'

// Vsak odgovor vsebuje "info". "OK" (ali "ok") pomeni uspeh,
// vse drugo je sporočilo o napaki.
export type Odgovor = { info: string }

export function uspelo(odgovor: Odgovor): boolean {
  return odgovor.info.toLowerCase() === 'ok'
}

// Pošlje podatke kot JSON in vrne odgovor API-ja.
export async function posli<T extends Odgovor = Odgovor>(
  pot: string,
  podatki: Record<string, unknown> = {},
): Promise<T> {
  const odziv = await fetch(`${API_URL}${pot}`, {
    method: 'POST',
    credentials: 'same-origin', // pošlje piškotek seje
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(podatki),
  })

  if (!odziv.ok) {
    throw new Error(`Napaka strežnika (${odziv.status})`)
  }

  const besedilo = await odziv.text()
  try {
    return JSON.parse(besedilo) as T
  } catch {
    throw new Error('Strežnik je vrnil neveljaven odgovor')
  }
}

// ---------- Uporabnik ----------

// Podatki prijavljenega uporabnika (odgovor na /jaz).
// role: "u" = uporabnik, "a" = administrator.
export type Jaz = Odgovor & { role?: string; ime?: string; avti?: string[] }

export function prijava(upIme: string, upGeslo: string) {
  return posli('/prijava', { upIme, upGeslo })
}

export function registracija(upIme: string, upGeslo: string) {
  return posli('/registracija', { upIme, upGeslo })
}

export function kdoSem() {
  return posli<Jaz>('/jaz')
}

// Konča sejo na strežniku (če pot obstaja).
export function odjava() {
  return posli('/odjava')
}

// ---------- Avti ----------

export function dodajAvto(registrska: string) {
  return posli('/avto/dodaj', { registrska })
}

export function izbrisiAvto(registrska: string) {
  return posli('/avto/skenslaj', { registrska })
}

// ---------- Parkirna mesta in rezervacije ----------

// Eno parkirno mesto v mreži.
export type Mesto = { id: number; x: number; y: number; smer: number }

// Rezervacija, kot jo vrne API.
export type Rezervacija = {
  id: number
  zac: string
  kon: string
  parkingID: number
  registrska: string
}

export function vsaMesta() {
  return posli<Odgovor & { parkirplaci: Mesto[] }>('/parkirplaci')
}

// Vrne samo številke (id) prostih mest v izbranem času.
export function prostaMesta(zac: string, kon: string) {
  return posli<Odgovor & { parkirplaci: number[] }>('/prostost', { zac, kon })
}

export function rezerviraj(zac: string, kon: string, parkingID: number, registrska: string) {
  return posli('/rezervacija/dodaj', { zac, kon, parkingID, registrska })
}

// Moje prihodnje rezervacije.
export function mojeRezervacije() {
  return posli<Odgovor & { rezervacije: Rezervacija[] }>('/rezervacija/prihodnje')
}

// Prekliče (izbriše) rezervacijo s to številko.
export function prekliciRezervacijo(rezervacijaId: number) {
  return posli('/rezervacija/skenslaj', { rezervacijaId })
}

// ---------- Administrator ----------

// Pri adminu ima rezervacija še številko uporabnika, ki jo je naredil.
export type RezervacijaAdmin = Rezervacija & { uporabnikID: number }

// Prihodnje rezervacije izbranega mesta (samo za vlogo "a").
export function rezervacijeMesta(parkingID: number) {
  return posli<Odgovor & { rezervacije: RezervacijaAdmin[] }>('/rezervacija/prihodnjeAdmin', {
    parkingID,
  })
}

// Izbriše katerokoli rezervacijo (samo za vlogo "a").
export function izbrisiRezervacijoAdmin(rezervacijaId: number) {
  return posli('/rezervacija/skenslajAdmin', { rezervacijaId })
}