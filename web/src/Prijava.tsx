import { useState } from 'react'
import type { FormEvent } from 'react'
import { prijava, registracija, kdoSem, uspelo } from './api'

type Props = { priPrijavi: (ime: string) => void }

const MIN_DOLZINA_GESLA = 8

export default function Prijava({ priPrijavi }: Props) {
  const [upIme, setUpIme] = useState('')
  const [upGeslo, setUpGeslo] = useState('')
  const [novRacun, setNovRacun] = useState(false)
  const [nalaganje, setNalaganje] = useState(false)
  const [napaka, setNapaka] = useState('')

  async function oddaj(e: FormEvent) {
    e.preventDefault()
    setNapaka('')

    // Dolžino gesla preverimo samo pri novem računu, da se lahko
    // prijavijo tudi obstoječi uporabniki s starejšimi gesli.
    if (novRacun && upGeslo.length < MIN_DOLZINA_GESLA) {
      setNapaka(`Geslo mora imeti vsaj ${MIN_DOLZINA_GESLA} znakov.`)
      return
    }

    setNalaganje(true)
    try {
      if (novRacun) {
        const r = await registracija(upIme, upGeslo)
        if (!uspelo(r)) {
          setNapaka(r.info)
          return
        }
      }
      const p = await prijava(upIme, upGeslo)
      if (!uspelo(p)) {
        setNapaka(p.info)
        return
      }
      const jaz = await kdoSem()
      priPrijavi(jaz.ime ?? upIme)
    } catch (err) {
      setNapaka(err instanceof Error ? err.message : 'Neznana napaka')
    } finally {
      setNalaganje(false)
    }
  }

  return (
    <main className="min-h-screen grid place-items-center bg-base-200 p-6">
      <form onSubmit={oddaj} className="card bg-base-100 shadow-sm w-full max-w-sm">
        <div className="card-body gap-3">
          <h1 className="card-title">{novRacun ? 'Registracija' : 'Prijava'}</h1>

          <input
            className="input w-full"
            placeholder="Uporabniško ime"
            value={upIme}
            onChange={(e) => setUpIme(e.target.value)}
            required
          />
          <input
            className="input w-full"
            type="password"
            placeholder="Geslo"
            value={upGeslo}
            onChange={(e) => setUpGeslo(e.target.value)}
            required
          />
          {novRacun && (
            <p className="text-sm opacity-70">
              Geslo mora imeti vsaj {MIN_DOLZINA_GESLA} znakov.
            </p>
          )}

          {napaka && <div className="alert alert-error">{napaka}</div>}

          <button className="btn btn-primary" disabled={nalaganje}>
            {nalaganje && <span className="loading loading-spinner" />}
            {novRacun ? 'Ustvari račun' : 'Prijava'}
          </button>

          <button
            type="button"
            className="btn btn-ghost btn-sm"
            onClick={() => {
              setNovRacun(!novRacun)
              setNapaka('')
            }}
          >
            {novRacun ? 'Že imam račun' : 'Nimam računa'}
          </button>
        </div>
      </form>
    </main>
  )
}