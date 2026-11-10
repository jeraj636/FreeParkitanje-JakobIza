import { useEffect, useState } from 'react'
import { mojeRezervacije, prekliciRezervacijo, uspelo } from './api'
import type { Rezervacija } from './api'

// Iz "2026-11-10 08:00:00" naredi lepši zapis za prikaz.
function lepCas(v: string) {
  return new Date(v.replace(' ', 'T')).toLocaleString('sl-SI', {
    dateStyle: 'short',
    timeStyle: 'short',
  })
}

export default function Rezervacije({ priPreklicu }: { priPreklicu: () => void }) {
  const [seznam, setSeznam] = useState<Rezervacija[]>([])
  const [nalaganje, setNalaganje] = useState(true)
  const [napaka, setNapaka] = useState('')

  // Prebere moje prihodnje rezervacije s strežnika.
  async function osvezi() {
    try {
      const r = await mojeRezervacije()
      if (!uspelo(r)) {
        setNapaka(r.info)
        return
      }
      setNapaka('')
      setSeznam(r.rezervacije)
    } catch (err) {
      setNapaka(err instanceof Error ? err.message : 'Neznana napaka')
    } finally {
      setNalaganje(false)
    }
  }

  // Ob prvem prikazu zaslona naloži rezervacije.
  useEffect(() => {
    osvezi()
  }, [])

  async function preklici(r: Rezervacija) {
    // Brisanja ni mogoče razveljaviti, zato vprašamo za potrditev.
    if (!window.confirm(`Res želiš preklicati rezervacijo mesta ${r.parkingID} (${lepCas(r.zac)})?`)) {
      return
    }
    setNapaka('')
    try {
      const odgovor = await prekliciRezervacijo(r.id)
      if (!uspelo(odgovor)) {
        setNapaka(odgovor.info)
        return
      }
      await osvezi()
      priPreklicu() // sporoči vratarju, naj osveži zemljevid mest
    } catch (err) {
      setNapaka(err instanceof Error ? err.message : 'Neznana napaka')
    }
  }

  return (
    <div className="card bg-base-100 shadow-sm">
      <div className="card-body gap-3">
        <h2 className="card-title">Moje rezervacije</h2>

        {napaka && <div className="alert alert-error">{napaka}</div>}

        {nalaganje ? (
          <span className="loading loading-spinner" />
        ) : seznam.length === 0 ? (
          <p>Nimaš prihodnjih rezervacij.</p>
        ) : (
          <ul className="grid gap-2">
            {seznam.map((r) => (
              <li key={r.id} className="flex items-center justify-between gap-2">
                <span>
                  Mesto <b>{r.parkingID}</b>, {r.registrska}
                  <br />
                  <span className="text-sm opacity-70">
                    {lepCas(r.zac)} – {lepCas(r.kon)}
                  </span>
                </span>
                <button className="btn btn-ghost btn-sm" onClick={() => preklici(r)}>
                  Prekliči
                </button>
              </li>
            ))}
          </ul>
        )}
      </div>
    </div>
  )
}