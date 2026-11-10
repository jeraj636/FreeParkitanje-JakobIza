import { useState } from 'react'
import type { Mesto } from './api'
import Zemljevid from './Zemljevid'

// Izmišljena mesta, enaka tistim, ki jih bo vpisal Jakobov skript.
function narediMesta(): Mesto[] {
  const mesta: Mesto[] = []
  let id = 1
  const dodaj = (x: number, y: number, smer: number) => {
    mesta.push({ id: id++, x, y, smer })
  }

  // Navpična mesta (smer 1): zgoraj 17, dve srednji vrsti po 15,
  // spodaj 13 (pomaknjeno proti vhodu levo spodaj).
  const navpicne: { y: number; x0: number; stevilo: number }[] = [
    { y: 0, x0: 3, stevilo: 17 },
    { y: 5, x0: 3, stevilo: 15 },
    { y: 7, x0: 3, stevilo: 15 },
    { y: 12, x0: 8, stevilo: 13 },
  ]
  for (const { y, x0, stevilo } of navpicne) {
    for (let i = 0; i < stevilo; i++) dodaj(x0 + i, y, 1)
  }
  // Dve vodoravni vrsti po 6 mest (smer 0), na sredini parkirišča.
  for (const y of [3, 10]) {
    for (let i = 0; i < 6; i++) dodaj(5 + 2 * i, y, 0)
  }
  // Levi rob: 4 vodoravna mesta (zgoraj), desni rob: 10 (tik ob glavnem delu).
  for (let y = 2; y <= 5; y++) dodaj(0, y, 0)
  for (let y = 2; y <= 11; y++) dodaj(21, y, 0)
  return mesta
}

const MESTA = narediMesta()

// Vsako sedmo mesto je v predogledu "zasedeno".
const zasedeno = (m: Mesto) => m.id % 7 === 0

export default function Predogled() {
  const [izbrano, setIzbrano] = useState<number | null>(null)

  return (
    <main className="min-h-screen bg-base-200 p-6">
      <div className="max-w-3xl mx-auto grid gap-4">
        <div className="card bg-base-100 shadow-sm">
          <div className="card-body gap-4">
            <h1 className="card-title">Predogled zemljevida ({MESTA.length} mest)</h1>
            <p className="text-sm opacity-70">
              Izmišljeni podatki, nič se ne pošilja strežniku. Klikni zeleno mesto.
            </p>
            <div className="flex gap-2 text-sm">
              <span className="badge badge-success">prosto</span>
              <span className="badge badge-error">zasedeno</span>
              <span className="badge badge-primary">izbrano</span>
            </div>
            <Zemljevid
              mesta={MESTA}
              razred={(m) =>
                zasedeno(m) ? 'btn-error' : m.id === izbrano ? 'btn-primary' : 'btn-success'
              }
              onemogoceno={zasedeno}
              priKliku={(m) => setIzbrano(m.id)}
            />
            {izbrano !== null && <p>Izbrano mesto: {izbrano}</p>}
          </div>
        </div>
      </div>
    </main>
  )
}