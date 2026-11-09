import { useState } from 'react'
import Prijava from './Prijava'

function App() {
  const [ime, setIme] = useState<string | null>(null)

  if (ime === null) {
    return <Prijava priPrijavi={setIme} />
  }

  return (
    <main className="min-h-screen grid place-items-center bg-base-200 p-6">
      <div className="card bg-base-100 shadow-sm">
        <div className="card-body">
          <p>Prijava uspešna: <b>{ime}</b></p>
        </div>
      </div>
    </main>
  )
}

export default App