<?php
namespace PrijavaZToken;
use Exception;
use Baza;
class PrijavaException extends Exception
{

}
class HudaException extends Exception
{

}
function filtriraj($metoda, $token)
{
    $ok = true;
    // Filtracija vhoda
    if ($metoda != "POST") {
        echo json_encode(["info" => "Napacna poizvedba uporabi POST"]);
        $ok = false;
    }
    if ($token === null) {
        echo json_encode(["info" => "Napacna poizvedba specificiraj token"]);
        $ok = false;
    }
    return $ok;
}
function prijava($metoda, $_)
{
    $podatki = json_decode(file_get_contents("php://input"), true);
    $token = $podatki["token"] ?? null;
    if (!filtriraj($metoda, $token)) {
        return;
    }


    try {
        $uporabnik = preveriUporabnika($token);

    } catch (PrijavaException $e) {
        echo json_encode(["info" => $e->getMessage()]);
        return;
    }
    try {
        $podatki = uporabnikPodatki($uporabnik);
        // inicializacija seje
        session_start();
        session_regenerate_id(true);
        $_SESSION["id"] = $podatki["id"];
        $_SESSION["role"] = $podatki["role"];


        echo json_encode(["info" => "OK"]);
    } catch (HudaException $_) {
        echo json_encode(["info" => "huda napaka"]);
        return;
    }


}

function
    preveriUporabnika(
    $token
) {
    require_once __DIR__ . "/baza.php";

    $povezava = Baza::poveziSe();
    $sql = "SELECT ime FROM USER WHERE token = ?";
    $ps = $povezava->prepare($sql);
    $zgosceno = hash("sha256", $token);
    $ps->bind_param("s", $zgosceno);
    $ps->execute();
    $r = $ps->get_result();
    if ($r->num_rows < 1) {
        throw new PrijavaException("Napačno uporabniško ime ali geslo");
    } else {
        return $r->fetch_assoc()["ime"];
    }
}
function uporabnikPodatki($upIme)
{
    require_once __DIR__ . "/baza.php";

    $povezava = Baza::poveziSe();
    $sql = "SELECT id,role FROM USER WHERE ime = ?";
    $ps = $povezava->prepare($sql);
    $ps->bind_param("s", $upIme);
    $ps->execute();
    $r = $ps->get_result();


    if ($r->num_rows < 1) {
        throw new HudaException();
    } else {
        return $r->fetch_assoc();
    }
}
