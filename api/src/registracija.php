<?php
namespace Registracija;
use Exception;
use Baza;
class PrijavaException extends Exception
{

}
class HudaException extends Exception
{

}
function filtriraj($metoda, $upIme, $upGeslo)
{
    $ok = true;
    // Filtracija vhoda
    if ($metoda != "POST") {
        echo json_encode(["info" => "Napacna poizvedba uporabi POST"]);
        $ok = false;
    }
    if ($upIme === null) {
        echo json_encode(["info" => "Napacna poizvedba specificiraj upIme"]);
        $ok = false;
    }
    if ($upGeslo === null) {
        echo json_encode(["info" => "Napacna poizvedba specificiraj upGeslo"]);
        $ok = false;
    }
    return $ok;
}
function aliSmem($upIme)
{
    require_once __DIR__ . "/baza.php";

    $povezava = Baza::poveziSe();
    $sql = "SELECT COUNT(*) FROM USER WHERE ime = ?";
    $ps = $povezava->prepare($sql);
    $ps->bind_param("s", $upIme);
    $ps->execute();
    $r = $ps->get_result();

    if ($r->fetch_column() === 0) {

        return true;
    }
    return false;
}
function vnesiVBazo($upIme, $upGeslo)
{
    $povazava = Baza::poveziSe();

    $hash = password_hash($upGeslo, PASSWORD_DEFAULT);

    $sql = "INSERT INTO USER (ime,role,geslo) VALUES(?,'u',?)";
    $ps = $povazava->prepare($sql);
    $ps->bind_param("ss", $upIme, $hash);


    if ($ps->execute()) {
        return true;
    }
    return false;
}
function registracija($metoda, $_)
{
    $podatki = json_decode(file_get_contents("php://input"), true);
    $upIme = $podatki["upIme"] ?? null;
    $upGeslo = $podatki["upGeslo"] ?? null;
    if (!filtriraj($metoda, $upIme, $upGeslo)) {
        return;
    }



    if (aliSmem($upIme)) {
        vnesiVBazo($upIme, $upGeslo);
        echo json_encode(["info" => "ok"]);
    } else {
        echo json_encode(["info" => "uporabniško ime je že zasedeno"]);
    }
}
