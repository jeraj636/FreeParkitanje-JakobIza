import { useState } from 'react'
import Prijava from './Prijava'
import Avti from './Avti'
import Rezerviraj from './Rezerviraj'
import Rezervacije from './Rezervacije'

function App() {
  const [ime, setIme] = useState<string | null>(null)
  // Poveča se ob novi rezervaciji; "key" prisili seznam, da se naloži znova.
  const [stevecSeznam, setStevecSeznam] = useState(0)
  // Poveča se ob preklicu; zemljevid mest ga opazi in se osveži.
  const [stevecMesta, setStevecMesta] = useState(0)

  if (ime === null) {
    return <Prijava priPrijavi={setIme} />
  }

  return (
    <main className="min-h-screen bg-base-200 p-6">
      <div className="max-w-3xl mx-auto grid gap-4">
        <p>Prijavljena: <b>{ime}</b></p>
        <Avti />
        <Rezerviraj
          priRezervaciji={() => setStevecSeznam((s) => s + 1)}
          osveziMesta={stevecMesta}
        />
        <Rezervacije key={stevecSeznam} priPreklicu={() => setStevecMesta((s) => s + 1)} />
      </div>
    </main>
  )
}

export default App