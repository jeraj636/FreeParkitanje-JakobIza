import { useState } from 'react'
import Prijava from './Prijava'
import Avti from './Avti'

function App() {
  const [ime, setIme] = useState<string | null>(null)

  if (ime === null) {
    return <Prijava priPrijavi={setIme} />
  }

  return (
    <main className="min-h-screen bg-base-200 p-6">
      <div className="max-w-md mx-auto grid gap-4">
        <p>Prijavljena: <b>{ime}</b></p>
        <Avti />
      </div>
    </main>
  )
}

export default App