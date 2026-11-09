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



// Podatki prijavljenega uporabnika (odgovor na /jaz).
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