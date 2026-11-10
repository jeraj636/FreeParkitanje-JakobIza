import { useEffect, useState } from 'react'
import { kdoSem, vsaMesta, prostaMesta, rezerviraj, uspelo } from './api'
import type { Mesto } from './api'
import Zemljevid from './Zemljevid'

// Iz "2026-11-10T08:00" naredi "2026-11-10 08:00:00", kot ga razume baza.
function zaBazo(v: string) {
  return v.replace('T', ' ') + ':00'
}

// Sporočila strežnika prevedemo v razumljivo besedilo.
function prevedi(info: string) {
  if (info === 'this is a duplicate') return 'To mesto je v izbranem času že zasedeno.'
  if (info === 'Invalid car or unauthorized action.') return 'Izbrani avto ni tvoj.'
  return info
}

// Rezervacija je možna samo, če se začne v naslednji uri.
const NAJVEC_VNAPREJ_MINUT = 60

// Vrne besedilo napake ali prazen niz, če je čas v redu.
function preveriCas(zac: string, kon: string): string {
  if (!zac || !kon) return 'Izberi začetek in konec.'
  if (kon <= zac) return 'Konec mora biti po začetku.'
  const zdaj = Date.now()
  const zacetek = new Date(zac).getTime()
  if (zacetek < zdaj) return 'Začetek je v preteklosti.'
  if (zacetek - zdaj > NAJVEC_VNAPREJ_MINUT * 60 * 1000) {
    return `Rezervacija je možna največ ${NAJVEC_VNAPREJ_MINUT} minut pred prihodom.`
  }
  return ''
}

type Props = {
  priRezervaciji: () => void // pokličemo po uspešni rezervaciji
  osveziMesta: number // poveča se, ko je treba osvežiti zemljevid (npr. po preklicu)
}

export default function Rezerviraj({ priRezervaciji, osveziMesta }: Props) {
  const [avti, setAvti] = useState<string[]>([])
  const [registrska, setRegistrska] = useState('')
  const [zac, setZac] = useState('')
  const [kon, setKon] = useState('')
  const [mesta, setMesta] = useState<Mesto[]>([])
  const [prosta, setProsta] = useState<Set<number> | null>(null)
  const [izbrano, setIzbrano] = useState<number | null>(null)
  const [nalaganje, setNalaganje] = useState(true)
  const [napaka, setNapaka] = useState('')
  const [sporocilo, setSporocilo] = useState('')

  // Ob prvem prikazu naloži moje avte in vsa mesta.
  useEffect(() => {
    async function naloziVse() {
      try {
        const jaz = await kdoSem()
        const seznam = jaz.avti ?? []
        setAvti(seznam)
        setRegistrska(seznam[0] ?? '')
        const m = await vsaMesta()
        if (uspelo(m)) setMesta(m.parkirplaci)
        else setNapaka(m.info)
      } catch (err) {
        setNapaka(err instanceof Error ? err.message : 'Neznana napaka')
      } finally {
        setNalaganje(false)
      }
    }
    naloziVse()
  }, [])

  // Tiho osveži zemljevid (brez brisanja sporočil), če je iskanje že bilo narejeno.
  async function osveziProsta() {
    try {
      const r = await prostaMesta(zaBazo(zac), zaBazo(kon))
      if (uspelo(r)) {
        const nova = new Set(r.parkirplaci)
        setProsta(nova)
        // Če izbrano mesto ni več prosto, izbiro ponastavimo.
        setIzbrano((i) => (i !== null && !nova.has(i) ? null : i))
      }
    } catch {
      // Zemljevid se bo osvežil ob naslednjem iskanju.
    }
  }

  // Ko se poveča števec iz vratarja (preklic rezervacije), osveži zemljevid.
  useEffect(() => {
    if (osveziMesta > 0 && prosta !== null) {
      osveziProsta()
    }
  }, [osveziMesta])

  // Vpraša strežnik, katera mesta so v izbranem času prosta.
  async function poisci() {
    setNapaka('')
    setSporocilo('')
    setIzbrano(null)
    const tezava = preveriCas(zac, kon)
    if (tezava) {
      setNapaka(tezava)
      return
    }
    try {
      const r = await prostaMesta(zaBazo(zac), zaBazo(kon))
      if (!uspelo(r)) {
        setNapaka(r.info)
        return
      }
      setProsta(new Set(r.parkirplaci))
    } catch (err) {
      setNapaka(err instanceof Error ? err.message : 'Neznana napaka')
    }
  }

  // Ustvari rezervacijo za izbrano mesto in avto.
  async function potrdi() {
    if (izbrano === null || !registrska) {
      setNapaka('Izberi mesto in avto.')
      return
    }
    setNapaka('')
    const tezava = preveriCas(zac, kon)
    if (tezava) {
      setNapaka(tezava)
      return
    }
    try {
      const r = await rezerviraj(zaBazo(zac), zaBazo(kon), izbrano, registrska)
      if (!uspelo(r)) {
        setNapaka(prevedi(r.info))
        return
      }
      const stevilka = izbrano
      await poisci() // osveži zemljevid, mesto je zdaj zasedeno
      setSporocilo(`Rezervacija za mesto ${stevilka} je ustvarjena.`)
      priRezervaciji() // sporoči vratarju, naj osveži seznam rezervacij
    } catch (err) {
      setNapaka(err instanceof Error ? err.message : 'Neznana napaka')
    }
  }

  // Barva mesta glede na stanje.
  function barva(m: Mesto) {
    if (prosta === null) return 'btn-outline'
    if (!prosta.has(m.id)) return 'btn-error'
    return m.id === izbrano ? 'btn-primary' : 'btn-success'
  }

  return (
    <div className="card bg-base-100 shadow-sm">
      <div className="card-body gap-4">
        <h2 className="card-title">Rezerviraj parkirno mesto</h2>

        {nalaganje ? (
          <span className="loading loading-spinner" />
        ) : (
          <>
            {avti.length === 0 && (
              <div className="alert alert-warning">
                Najprej dodaj avto (zgoraj), potem lahko rezerviraš. Po dodajanju osveži stran (F5).
              </div>
            )}

            <div className="grid gap-3 sm:grid-cols-3">
              <label className="grid gap-1">
                <span className="text-sm">Začetek</span>
                <input
                  type="datetime-local"
                  className="input w-full"
                  value={zac}
                  onChange={(e) => {
                    setZac(e.target.value)
                    setProsta(null)
                    setIzbrano(null)
                  }}
                />
              </label>
              <label className="grid gap-1">
                <span className="text-sm">Konec</span>
                <input
                  type="datetime-local"
                  className="input w-full"
                  value={kon}
                  onChange={(e) => {
                    setKon(e.target.value)
                    setProsta(null)
                    setIzbrano(null)
                  }}
                />
              </label>
              <label className="grid gap-1">
                <span className="text-sm">Avto</span>
                <select
                  className="select w-full"
                  value={registrska}
                  onChange={(e) => setRegistrska(e.target.value)}
                >
                  {avti.map((a) => (
                    <option key={a}>{a}</option>
                  ))}
                </select>
              </label>
            </div>

            <button className="btn btn-primary" onClick={poisci}>
              Poišči prosta mesta
            </button>

            {napaka && <div className="alert alert-error">{napaka}</div>}
            {sporocilo && <div className="alert alert-success">{sporocilo}</div>}

            <div className="flex gap-2 text-sm">
              <span className="badge badge-success">prosto</span>
              <span className="badge badge-error">zasedeno</span>
              <span className="badge badge-primary">izbrano</span>
            </div>

            <Zemljevid
              mesta={mesta}
              razred={barva}
              onemogoceno={(m) => prosta === null || !prosta.has(m.id)}
              priKliku={(m) => setIzbrano(m.id)}
            />

            {izbrano !== null && (
              <button className="btn btn-primary" onClick={potrdi} disabled={!registrska}>
                Rezerviraj mesto {izbrano}
              </button>
            )}
          </>
        )}
      </div>
    </div>
  )
}