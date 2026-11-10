import { useState } from 'react'
import Prijava from './Prijava'
import Avti from './Avti'
import Rezerviraj from './Rezerviraj'
import Rezervacije from './Rezervacije'
import Admin from './Admin'
import Predogled from './Predogled'
import { kdoSem, odjava } from './api'

function App() {
  const [ime, setIme] = useState<string | null>(null)
  // "u" = uporabnik, "a" = administrator (tako ga vrne API).
  const [role, setRole] = useState('u')
  // Poveča se ob novi rezervaciji; "key" prisili seznam, da se naloži znova.
  const [stevecSeznam, setStevecSeznam] = useState(0)
  // Poveča se ob preklicu; zemljevid mest ga opazi in se osveži.
  const [stevecMesta, setStevecMesta] = useState(0)

  // Začasno: predogled zemljevida brez prijave (naslov /?predogled).
  if (window.location.search.includes('predogled')) {
    return <Predogled />
  }

  // Po uspešni prijavi vprašamo strežnik še za vlogo.
  async function priPrijavi(novoIme: string) {
    try {
      const jaz = await kdoSem()
      setRole(jaz.role ?? 'u')
    } catch {
      setRole('u')
    }
    setIme(novoIme)
  }

  async function odjaviSe() {
    try {
      await odjava()
    } catch {
      // Če strežnik poti še nima, se vseeno odjavimo v brskalniku.
    }
    setIme(null)
    setRole('u')
    setStevecSeznam(0)
    setStevecMesta(0)
  }

  if (ime === null) {
    return <Prijava priPrijavi={priPrijavi} />
  }

  return (
    <main className="min-h-screen bg-base-200 p-6">
      <div className="max-w-3xl mx-auto grid gap-4">
        <div className="flex items-center justify-between">
          <p>
            Prijavljena: <b>{ime}</b>
            {role === 'a' && <span className="badge badge-primary ml-2">administrator</span>}
          </p>
          <button className="btn btn-ghost btn-sm" onClick={odjaviSe}>
            Odjava
          </button>
        </div>

        {role === 'a' && <Admin />}

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