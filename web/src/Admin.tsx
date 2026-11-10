import { useEffect, useState } from 'react'
import { vsaMesta, rezervacijeMesta, izbrisiRezervacijoAdmin, uspelo } from './api'
import type { Mesto, RezervacijaAdmin } from './api'
import Zemljevid from './Zemljevid'

// Iz "2026-11-10 08:00:00" naredi lepši zapis za prikaz.
function lepCas(v: string) {
  return new Date(v.replace(' ', 'T')).toLocaleString('sl-SI', {
    dateStyle: 'short',
    timeStyle: 'short',
  })
}

export default function Admin() {
  const [mesta, setMesta] = useState<Mesto[]>([])
  const [izbrano, setIzbrano] = useState<number | null>(null)
  const [seznam, setSeznam] = useState<RezervacijaAdmin[]>([])
  const [nalaganje, setNalaganje] = useState(true)
  const [nalagamSeznam, setNalagamSeznam] = useState(false)
  const [napaka, setNapaka] = useState('')

  // Ob prvem prikazu naloži vsa mesta.
  useEffect(() => {
    async function naloziMesta() {
      try {
        const m = await vsaMesta()
        if (uspelo(m)) setMesta(m.parkirplaci)
        else setNapaka(m.info)
      } catch (err) {
        setNapaka(err instanceof Error ? err.message : 'Neznana napaka')
      } finally {
        setNalaganje(false)
      }
    }
    naloziMesta()
  }, [])

  // Naloži prihodnje rezervacije izbranega mesta.
  async function izberi(id: number) {
    setIzbrano(id)
    setNapaka('')
    setNalagamSeznam(true)
    try {
      const r = await rezervacijeMesta(id)
      if (!uspelo(r)) {
        setNapaka(r.info)
        setSeznam([])
        return
      }
      setSeznam(r.rezervacije)
    } catch (err) {
      setNapaka(err instanceof Error ? err.message : 'Neznana napaka')
    } finally {
      setNalagamSeznam(false)
    }
  }

  async function izbrisi(r: RezervacijaAdmin) {
    // Brisanja ni mogoče razveljaviti, zato vprašamo za potrditev.
    if (
      !window.confirm(
        `Res želiš izbrisati rezervacijo uporabnika ${r.uporabnikID} (${lepCas(r.zac)})?`,
      )
    ) {
      return
    }
    setNapaka('')
    try {
      const odgovor = await izbrisiRezervacijoAdmin(r.id)
      if (!uspelo(odgovor)) {
        setNapaka(odgovor.info)
        return
      }
      await izberi(r.parkingID) // osveži seznam tega mesta
    } catch (err) {
      setNapaka(err instanceof Error ? err.message : 'Neznana napaka')
    }
  }

  return (
    <div className="card bg-base-100 shadow-sm">
      <div className="card-body gap-3">
        <h2 className="card-title">Administracija rezervacij</h2>
        <p className="text-sm opacity-70">
          Klikni na parkirno mesto, da vidiš njegove prihodnje rezervacije.
        </p>

        {napaka && <div className="alert alert-error">{napaka}</div>}

        {nalaganje ? (
          <span className="loading loading-spinner" />
        ) : (
          <>
            <Zemljevid
              mesta={mesta}
              razred={(m) => (m.id === izbrano ? 'btn-primary' : 'btn-outline')}
              priKliku={(m) => izberi(m.id)}
            />

            {izbrano !== null && (
              <div className="grid gap-2">
                <h3 className="font-semibold">Mesto {izbrano}</h3>
                {nalagamSeznam ? (
                  <span className="loading loading-spinner" />
                ) : seznam.length === 0 ? (
                  <p>To mesto nima prihodnjih rezervacij.</p>
                ) : (
                  <ul className="grid gap-2">
                    {seznam.map((r) => (
                      <li key={r.id} className="flex items-center justify-between gap-2">
                        <span>
                          Uporabnik <b>{r.uporabnikID}</b>, {r.registrska}
                          <br />
                          <span className="text-sm opacity-70">
                            {lepCas(r.zac)} – {lepCas(r.kon)}
                          </span>
                        </span>
                        <button className="btn btn-ghost btn-sm" onClick={() => izbrisi(r)}>
                          Izbriši
                        </button>
                      </li>
                    ))}
                  </ul>
                )}
              </div>
            )}
          </>
        )}
      </div>
    </div>
  )
}