<?php
namespace Prostost;
use Exception;
use Baza;
function filtriraj($metoda, $kon, $zac)
{
    $ok = true;
    // Filtracija vhoda
    if ($metoda != "POST") {
        echo json_encode(["info" => "Napacna poizvedba uporabi POST"]);
        $ok = false;
    }
    if ($zac === null) {
        echo json_encode(["info" => "Napacna poizvedba specificiraj zac"]);
        $ok = false;
    }
    if ($kon === null) {
        echo json_encode(["info" => "Napacna poizvedba specificiraj kon"]);
        $ok = false;
    }
    return $ok;
}




function prostost($metoda)
{

    $podatki = json_decode(file_get_contents("php://input"), true);
    $zac = $podatki["zac"] ?? null;
    $kon = $podatki["kon"] ?? null;
    if (!filtriraj($metoda, $kon, $zac)) {
        return;
    }
    try {
        $povezava = Baza::poveziSe();
        $sql = "SELECT id FROM PARKING p WHERE NOT EXISTS (SELECT 1 FROM REZERVACIJA r WHERE p.id = r.parkingID AND zac < ? AND kon > ?)";
        $ps = $povezava->prepare($sql);
        $ps->bind_param("ss", $kon, $zac);
        $ps->execute();
        $r = $ps->get_result();
        $out = ["info" => "ok", "parkirplaci" => []];
        while ($v = $r->fetch_assoc()) {
            $out["parkirplaci"][] = $v["id"];
        }
        echo json_encode($out);
    } catch (Exception $e) {
        echo json_encode(["info" => "Napaka pri pridobivanju zasedenosti" . $e->getMessage()]);
    }
}


