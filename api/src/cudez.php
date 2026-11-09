<?php
namespace Cudez;
use Exception;
use Baza;
use Dotenv;
use RuntimeException;
use DateTimeZone;
use DateTimeImmutable;
use JsonException;
require_once __DIR__ . "/seje.php";

require_once __DIR__ . '/../vendor/autoload.php';
Dotenv\Dotenv::createUnsafeImmutable(__DIR__ . '/../')->load();


class CudezException extends Exception
{

}
function filtriraj($metoda, $akcija, $podatki)
{
    $ok = true;
    // Filtracija vhoda
    if ($metoda != "POST") {
        echo json_encode(["info" => "Napacna poizvedba uporabi POST"]);
        $ok = false;
    }
    if (!isset($akcija) || !in_array($akcija, ["poisciRezervacijo", "skenslajRezervacijo"])) {
        echo json_encode(["info" => "Napacen endpoint"]);
        $ok = false;
    }
    if ($podatki === null) {
        echo json_encode(["info" => "Napacna poizvedba specificiraj besedilo"]);

        $ok = false;
    }
    return $ok;
}




function izlusciPodatke($podatki): array
{
    $apiKljuc = getenv("AI_API_KLJUC");

    if (empty($apiKljuc)) {
        throw new CudezException("Manjka AI API ključ.");
    }






    $ch = curl_init("https://openrouter.ai/api/v1/chat/completions");

    try {
        curl_setopt_array($ch, [
            CURLOPT_POST => true,
            CURLOPT_RETURNTRANSFER => true,
            CURLOPT_HTTPHEADER => [
                "Authorization: Bearer " . $apiKljuc,
                "Content-Type: application/json",
                "X-Title: FreeParkiranje"
            ],
            CURLOPT_POSTFIELDS => json_encode(
                $podatki,
                JSON_THROW_ON_ERROR | JSON_UNESCAPED_UNICODE
            ),
            CURLOPT_TIMEOUT => 60
        ]);

        $odgovor = curl_exec($ch);

        if ($odgovor === false) {
            throw new CudezException(curl_error($ch));
        }

        $status = curl_getinfo($ch, CURLINFO_HTTP_CODE);

        if ($status !== 200) {
            throw new CudezException(
                "OpenRouter HTTP $status: $odgovor"
            );
        }

        $rezultat = json_decode(
            $odgovor,
            true,
            512,
            JSON_THROW_ON_ERROR
        );

        $izbira = $rezultat["choices"][0] ?? null;

        if ($izbira === null) {
            throw new CudezException("AI ni vrnil izbire.");
        }

        if (($izbira["finish_reason"] ?? "") === "length") {
            throw new CudezException(
                "AI je porabil omejitev tokenov za generiranje."
            );
        }

        $vsebina = $izbira["message"]["content"] ?? null;

        if (!is_string($vsebina) || trim($vsebina) === "") {
            throw new CudezException("AI ni vrnil vsebine.");
        }

        return json_decode(
            $vsebina,
            true,
            512,
            JSON_THROW_ON_ERROR

        );



    } catch (JsonException $e) {
        throw new CudezException(
            "Napaka pri JSON: " . $e->getMessage()
        );
    }
}



function poisciRezervacijo($besedilo)
{
    try {
        $casovniPas = new DateTimeZone("Europe/Ljubljana");
        $danes = (new DateTimeImmutable("now", $casovniPas))->format("Y-m-d");
        $sistemskiPrompt = "
Iz uporabnikovega sporočila izlušči podatke za parkirno rezervacijo.

Vrni naslednja polja:
- zac: začetek rezervacije
- kon: konec rezervacije

Trenutni datum: $danes
Časovni pas: Europe/Ljubljana.

Pravila:
- Danes in jutri, pojutrišnjem pretvori v ustrezen datum.
- Čas pretvori v obliko YYYY-MM-DD HH:MM.
- Če podatek manjka ali ni jasen, uporabi null.
- Ne dodajaj nobenih drugih polj.
- Ne dodajaj razlage.

Vrni izključno JSON:
{
    \"zac\": null,
    \"kon\": null,
}
";
        $podatki = [
            "model" => "nvidia/nemotron-3.5-lightning:free",
            "temperature" => 0,
            "max_tokens" => 500,
            "reasoning" => [
                "enabled" => false,
                "effort" => "minimal"
            ],
            "messages" => [
                [
                    "role" => "system",
                    "content" => $sistemskiPrompt
                ],
                [
                    "role" => "user",
                    "content" => $besedilo
                ]
            ],
            "response_format" => [
                "type" => "json_object"
            ]
        ];
        $out = izlusciPodatke($besedilo, $podatki);
        $out["info"] = "ok";
        echo json_encode($out);
    } catch (Exception $e) {
        echo json_encode(["info" => "ni ok" . $e->getMessage()]);
    }
}
function skenslajRezervacijo($id, $besedilo)
{
    try {

        $povezava = Baza::poveziSe();
        $sql = "SELECT REZERVACIJA.id,zac,kon,parkingID FROM REZERVACIJA WHERE zac > CURDATE() AND uporabnikID = ?";
        $ps = $povezava->prepare($sql);
        $ps->bind_param("i", $id);
        $ps->execute();
        $r = $ps->get_result();
        $rezervacije = [];
        while ($v = $r->fetch_assoc()) {
            $rezervacije[] = $v;
        }
        $jsonRezervacij = json_encode($rezervacije);

        $besedilo = $jsonRezervacij . $besedilo;
        $casovniPas = new DateTimeZone("Europe/Ljubljana");
        $danes = (new DateTimeImmutable("now", $casovniPas))->format("Y-m-d");
        $sistemskiPrompt = "
Iz uporabnikovega sporočila izlušči primerne id-je rezervacij glede na scpecificiran začetek in konec rezervacije.

Vrni naslednja polja:
- id: array ustreznih id-jev

Trenutni datum: $danes
Časovni pas: Europe/Ljubljana.

Pravila:
- Danes in jutri, pojutrišnjem pretvori v ustrezen datum.
- Čas pretvori v obliko YYYY-MM-DD HH:MM.
- Če podatek manjka ali ni jasen, uporabi null.
- Ne dodajaj nobenih drugih polj.
- Ne dodajaj razlage.

Vrni izključno JSON:
{
    \"id\": null,
}
";
        $podatki = [
            "model" => "nvidia/nemotron-3.5-lightning:free",
            "temperature" => 0,
            "max_tokens" => 1000,
            "reasoning" => [
                "enabled" => false,
                "effort" => "minimal"
            ],
            "messages" => [
                [
                    "role" => "system",
                    "content" => $sistemskiPrompt
                ],
                [
                    "role" => "user",
                    "content" => $besedilo
                ]
            ],
            "response_format" => [
                "type" => "json_object"
            ]
        ];
        $out = izlusciPodatke($besedilo, $podatki);
        $out["info"] = "ok";

        foreach ($out["id"] as $idRezervacije) {
            $sql = "SELECT REZERVACIJA.id,zac,kon,parkingID FROM REZERVACIJA WHERE id = ? AND uporabnikID = ?";
            $ps = $povezava->prepare($sql);
            $ps->bind_param("ii", $idRezervacije, $id);
            $ps->execute();
            $r = $ps->get_result();
            if ($r->num_rows == 1) {
                $out["predlaganeZaIzbris"][] = $r->fetch_assoc();
            }
        }

        echo json_encode($out);

    } catch (Exception $e) {
        echo json_encode(["info" => "Napaka pri pridobivanju rezervacij " . $e->getMessage()]);
    }
}
function cudez($metoda, $akcija)
{

    if (!ustvariSejo()) {
        echo json_encode(["info" => "seja potekla"]);
        return;
    }
    $id = $_SESSION["id"];

    $podatki = json_decode(file_get_contents("php://input"), true);
    $besedilo = $podatki["besedilo"] ?? null;
    if (!filtriraj($metoda, $akcija, $podatki)) {
        return;
    }
    match ($akcija) {
        "poisciRezervacijo" => poisciRezervacijo($besedilo),
        "skenslajRezervacijo" => skenslajRezervacijo($id, $besedilo),
    };

}


