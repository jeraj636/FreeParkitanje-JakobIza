<?php
namespace Prostost;
use Exception;
use Baza;
function filtriraj($metoda)
{
    $ok = true;
    // Filtracija vhoda
    if ($metoda != "POST") {
        echo json_encode(["info" => "Napacna poizvedba uporabi POST"]);
        $ok = false;
    }
    if (!isset($_POST["zac"])) {
        echo json_encode(["info" => "Napacna poizvedba specificiraj zac"]);
        $ok = false;
    }
    if (!isset($_POST["kon"])) {
        echo json_encode(["info" => "Napacna poizvedba specificiraj kon"]);
        $ok = false;
    }
    return $ok;
}




function prostost($metoda)
{
    if (!filtriraj($metoda)) {
        return;
    }
    $zac = $_POST["zac"];
    $kon = $_POST["kon"];

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


