<?php
namespace Rezervacija;
use Exception;
use Baza;
require_once __DIR__ . "/seje.php";
function filtriraj($metoda, $akcija, $zac, $kon, $parkingID, $registrska, $rezervacijaId)
{
    $ok = true;
    // Filtracija vhoda
    if ($metoda != "POST") {
        echo json_encode(["info" => "Napacna poizvedba uporabi POST"]);
        $ok = false;
    }
    if (!isset($akcija) || !in_array($akcija, ["dodaj", "skenslaj", "prihodnje"])) {
        echo json_encode(["info" => "Napacen endpoint"]);
        $ok = false;
    }
    if ($akcija == "dodaj") {

        if (!isset($zac)) {
            echo json_encode(["info" => "Napacna poizvedba specificiraj zac"]);
            $ok = false;
        }
        if (!isset($kon)) {
            echo json_encode(["info" => "Napacna poizvedba specificiraj kon"]);
            $ok = false;
        }
        if (!isset($parkingID)) {
            echo json_encode(["info" => "Napacna poizvedba specificiraj parkingID"]);
            $ok = false;
        }
        if (!isset($registrska)) {
            echo json_encode(["info" => "Napacna poizvedba specificiraj registrska"]);
            $ok = false;
        }
    }
    if ($akcija == "skenslaj" && !isset($rezervacijaId)) {
        $ok = false;
    }

    return $ok;
}
function dodaj($registerska, $uporabnikId, $zac, $kon, $parkingID)
{
    $napaka = 'this is a duplicate';

    $povezava = Baza::poveziSe();

    $sql = "SELECT COUNT(*) AS st FROM REZERVACIJA WHERE parkingID = ? AND zac < ? AND kon > ?";
    $ps = $povezava->prepare($sql);
    $ps->bind_param("iss", $parkingID, $kon, $zac);
    $ps->execute();
    $r = $ps->get_result();
    $vr = $r->fetch_assoc()["st"];
    if ($vr === null || $vr != 0) {
        echo json_encode(["info" => $napaka]);
        return;
    }
    try {

        $sql = "INSERT INTO REZERVACIJA(uporabnikID,zac,kon,parkingID,avtoID) SELECT ?,?,?,?, id FROM AVTO WHERE registrska = ? AND userId = ?";
        $ps = $povezava->prepare($sql);
        $ps->bind_param("issisi", $uporabnikId, $zac, $kon, $parkingID, $registerska, $uporabnikId);

        if ($ps->execute() && $ps->affected_rows === 1) {
            echo json_encode(["info" => "ok"]);
        } else {
            echo json_encode(["info" => $napaka]);
        }
    } catch (Exception $_) {
        echo json_encode(["info" => $napaka]);
    }
}
function skenslaj($rezervacijaId, $upodabnikId)
{
    $sql = "DELETE FROM REZERVACIJA WHERE id = ? AND uporabnikID = ?";

    $povezava = Baza::poveziSe();

    $ps = $povezava->prepare($sql);
    $ps->bind_param("si", $rezervacijaId, $upodabnikId);

    if ($ps->execute() && $ps->affected_rows == 1) {
        echo json_encode(["info" => "ok"]);
    } else {
        echo json_encode(["info" => "Rezervacije ni mogoče izbrisati"]);
    }

}

function prihodnje($uporabnikID)
{
    try {

        $povezava = Baza::poveziSe();
        $sql = "SELECT REZERVACIJA.id,zac,kon,parkingID,registrska FROM REZERVACIJA INNER JOIN AVTO ON AVTO.id = REZERVACIJA.avtoID WHERE zac > CURDATE() AND uporabnikID = ?";
        $ps = $povezava->prepare($sql);
        $ps->bind_param("i", $uporabnikID);
        $ps->execute();
        $r = $ps->get_result();
        $out = ["info" => "ok", "rezervacije" => []];
        while ($v = $r->fetch_assoc()) {
            $out["rezervacije"][] = $v;
        }
        echo json_encode($out);
    } catch (Exception $_) {
        echo json_encode(["info" => "Napaka pri pridobivanju rezervacij"]);
    }
}
function rezervacija($metoda, $akcija)
{

    if (!filtriraj($metoda, $akcija, $_POST["zac"] ?? null, $_POST["kon"] ?? null, $_POST["parkingID"] ?? null, $_POST["registrska"] ?? null, $_POST["rezervacijaId"] ?? null)) {
        return;
    }
    if (!ustvariSejo()) {
        echo json_encode(["info" => "seja potekla"]);
        return;
    }
    $registrska = $_POST["registrska"] ?? null;
    $uporabnikId = $_SESSION["id"] ?? null;
    $zac = $_POST["zac"] ?? null;
    $kon = $_POST["kon"] ?? null;
    $parkingID = $_POST["parkingID"] ?? null;
    $rezervacijaId = $_POST["rezervacijaId"] ?? null;

    match ($akcija) {
        "dodaj" => dodaj($registrska, $uporabnikId, $zac, $kon, $parkingID),
        "skenslaj" => skenslaj($rezervacijaId, $uporabnikId),
        "prihodnje" => prihodnje($uporabnikId),
    };

}

