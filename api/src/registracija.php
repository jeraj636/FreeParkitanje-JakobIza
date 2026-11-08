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
function filtriraj($metoda)
{
    $ok = true;
    // Filtracija vhoda
    if ($metoda != "POST") {
        echo json_encode(["info" => "Napacna poizvedba uporabi POST"]);
        $ok = false;
    }
    if (!isset($_POST["upIme"])) {
        echo json_encode(["info" => "Napacna poizvedba specificiraj upIme"]);
        $ok = false;
    }
    if (!isset($_POST["upGeslo"])) {
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
    if (!filtriraj($metoda)) {
        return;
    }

    $upIme = $_POST["upIme"];
    $upGeslo = $_POST["upGeslo"];

    if (aliSmem($upIme)) {
        vnesiVBazo($upIme, $upGeslo);
        echo json_encode(["info" => "ok"]);
    } else {
        echo json_encode(["info" => "uporabniško ime je že zasedeno"]);
    }
}
