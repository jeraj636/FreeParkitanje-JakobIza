import { useEffect, useState } from 'react'
import type { FormEvent } from 'react'
import { kdoSem, dodajAvto, izbrisiAvto, uspelo } from './api'

export default function Avti() {
  const [avti, setAvti] = useState<string[]>([])
  const [registrska, setRegistrska] = useState('')
  const [nalaganje, setNalaganje] = useState(true)
  const [napaka, setNapaka] = useState('')

  // Prebere seznam avtov s strežnika.
  async function osvezi() {
    try {
      const jaz = await kdoSem()
      setAvti(jaz.avti ?? [])
    } catch (err) {
      setNapaka(err instanceof Error ? err.message : 'Neznana napaka')
    } finally {
      setNalaganje(false)
    }
  }

  // Ob prvem prikazu zaslona naloži avte.
  useEffect(() => {
    osvezi()
  }, [])

  async function dodaj(e: FormEvent) {
    e.preventDefault()
    setNapaka('')
    try {
      const r = await dodajAvto(registrska)
      if (!uspelo(r)) {
        setNapaka(r.info)
        return
      }
      setRegistrska('')
      await osvezi()
    } catch (err) {
      setNapaka(err instanceof Error ? err.message : 'Neznana napaka')
    }
  }

  async function izbrisi(reg: string) {
    setNapaka('')
    try {
      const r = await izbrisiAvto(reg)
      if (!uspelo(r)) {
        setNapaka(r.info)
        return
      }
      await osvezi()
    } catch (err) {
      setNapaka(err instanceof Error ? err.message : 'Neznana napaka')
    }
  }

  return (
    <div className="card bg-base-100 shadow-sm">
      <div className="card-body gap-3">
        <h2 className="card-title">Moji avtomobili</h2>

        {napaka && <div className="alert alert-error">{napaka}</div>}

        {nalaganje ? (
          <span className="loading loading-spinner" />
        ) : avti.length === 0 ? (
          <p>Še nimaš dodanega avtomobila.</p>
        ) : (
          <ul className="grid gap-2">
            {avti.map((a) => (
              <li key={a} className="flex items-center justify-between">
                <span>{a}</span>
                <button className="btn btn-ghost btn-sm" onClick={() => izbrisi(a)}>
                  Izbriši
                </button>
              </li>
            ))}
          </ul>
        )}

        <form onSubmit={dodaj} className="flex gap-2">
          <input
            className="input flex-1"
            placeholder="Registrska, npr. LJAB123"
            value={registrska}
            onChange={(e) => setRegistrska(e.target.value)}
            required
          />
          <button className="btn btn-primary">Dodaj</button>
        </form>
      </div>
    </div>
  )
}