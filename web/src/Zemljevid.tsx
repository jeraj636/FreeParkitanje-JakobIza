import type { Mesto } from './api'

type Props = {
  mesta: Mesto[]
  razred: (m: Mesto) => string // DaisyUI razred za barvo mesta
  onemogoceno?: (m: Mesto) => boolean
  priKliku: (m: Mesto) => void
}

// Položaj mesta na zemljevidu (brez številke mesta).
type Polozaj = { x: number; y: number; smer: number }

// 0 = vodoravno mesto (širše kot visoko), 1 = navpično mesto (potrdi z Jakobom).
const SMER_VODORAVNO = 0

// Velikost mesta v enotah: navpično 1 x 2, vodoravno 2 x 1.
function velikost(smer: number) {
  return smer === SMER_VODORAVNO ? { w: 2, h: 1 } : { w: 1, h: 2 }
}

// Razporeditev 86 mest. Vrstni red mora biti enak kot v api/parkingi.php,
// da se številke mest (1 do 86) ujemajo.
function razporeditev(): Polozaj[] {
  const p: Polozaj[] = []
  for (let i = 0; i < 17; i++) p.push({ x: 3 + i, y: 0, smer: 1 }) // zgornja vrsta
  for (let i = 0; i < 15; i++) p.push({ x: 3 + i, y: 5, smer: 1 }) // srednja vrsta 1
  for (let i = 0; i < 15; i++) p.push({ x: 3 + i, y: 7, smer: 1 }) // srednja vrsta 2
  for (let i = 0; i < 13; i++) p.push({ x: 8 + i, y: 12, smer: 1 }) // spodnja vrsta
  for (let i = 0; i < 6; i++) p.push({ x: 5 + 2 * i, y: 3, smer: 0 }) // vodoravna vrsta 1
  for (let i = 0; i < 6; i++) p.push({ x: 5 + 2 * i, y: 10, smer: 0 }) // vodoravna vrsta 2
  for (let i = 0; i < 4; i++) p.push({ x: 0, y: 2 + i, smer: 0 }) // levi rob
  for (let i = 0; i < 10; i++) p.push({ x: 21, y: 2 + i, smer: 0 }) // desni rob
  return p
}

const RAZPOREDITEV = razporeditev()

type PlosciProps = Props & {
  prazna?: Polozaj[] // mesta, ki jih še ni v bazi (narisana sivo)
}

function ZemljevidPlosca({ mesta, prazna = [], razred, onemogoceno, priKliku }: PlosciProps) {
  // Skupna velikost parkirišča v enotah.
  const vsa: Polozaj[] = [...mesta, ...prazna]
  const sirina = Math.max(...vsa.map((m) => m.x + velikost(m.smer).w))
  const visina = Math.max(...vsa.map((m) => m.y + velikost(m.smer).h))

  const polozaj = (p: Polozaj) => {
    const { w, h } = velikost(p.smer)
    return {
      left: `${(p.x / sirina) * 100}%`,
      top: `${(p.y / visina) * 100}%`,
      width: `${(w / sirina) * 100}%`,
      height: `${(h / visina) * 100}%`,
    }
  }

  return (
    <div className="mx-auto w-full max-w-3xl">
      <div
        className="relative w-full rounded-lg border border-base-300 bg-gray-50"
        style={{ aspectRatio: `${sirina} / ${visina}` }}
      >
        {mesta.map((m) => (
          <div key={m.id} className="absolute p-[2px]" style={polozaj(m)}>
            <button
              title={`Mesto ${m.id}`}
              className={`btn btn-xs h-full w-full rounded-sm p-0 ${razred(m)}`}
              style={{ minHeight: 0, minWidth: 0 }}
              disabled={onemogoceno ? onemogoceno(m) : false}
              onClick={() => priKliku(m)}
            />
          </div>
        ))}

        {/* Mesta, ki jih še ni v bazi: sivo, črtkano, brez klika. */}
        {prazna.map((p, i) => (
          <div key={`prazno-${i}`} className="absolute p-[2px]" style={polozaj(p)}>
            <div
              title="To mesto še ni v bazi"
              className="h-full w-full rounded-sm border border-dashed border-gray-300 bg-gray-100"
            />
          </div>
        ))}

        {/* Vhod na parkirišče: spodaj levo, kjer je prostor ob krajši spodnji vrsti. */}
        <div
          className="absolute flex flex-col items-center justify-center rounded-sm bg-primary text-xs font-bold leading-tight text-primary-content"
          style={{ left: '2%', bottom: '2%', width: '28%', height: '11%' }}
        >
          <span>▲</span>
          <span>VHOD</span>
        </div>
      </div>
    </div>
  )
}

export default function Zemljevid(props: Props) {
  if (props.mesta.length === 0) {
    return <p className="text-sm opacity-70">Ni parkirnih mest.</p>
  }

  // Stari podatki imajo največ štiri vrste (y od 0 do 3). Takrat mesta iz baze
  // postavimo na položaje po vrsti (po številki), manjkajoča pa narišemo sivo.
  const staroStanje = Math.max(...props.mesta.map((m) => m.y)) <= 3
  if (!staroStanje) {
    return <ZemljevidPlosca {...props} />
  }

  const urejena = [...props.mesta].sort((a, b) => a.id - b.id)
  const prikaz: Mesto[] = []
  const prazna: Polozaj[] = []
  RAZPOREDITEV.forEach((p, i) => {
    const mesto = urejena[i]
    if (mesto) prikaz.push({ ...mesto, x: p.x, y: p.y, smer: p.smer })
    else prazna.push(p)
  })

  return <ZemljevidPlosca {...props} mesta={prikaz} prazna={prazna} />
}