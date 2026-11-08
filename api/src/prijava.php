<?php
namespace Prijava;
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
function prijava($metoda, $_)
{
    if (!filtriraj($metoda)) {
        return;
    }

    $upIme = $_POST["upIme"];
    $upGeslo = $_POST["upGeslo"];
    $uspelaPrijava = false;
    try {
        $uspelaPrijava = preveriUporabnika($upIme, $upGeslo);

    } catch (PrijavaException $e) {
        echo json_encode(["info" => $e->getMessage()]);
        return;
    }
    if ($uspelaPrijava) {
        try {
            $podatki = uporabnikPodatki($upIme);
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

}

function
    preveriUporabnika(
    $upIme,
    $upGeslo
) {
    require_once __DIR__ . "/baza.php";

    $povezava = Baza::poveziSe();
    $sql = "SELECT geslo FROM USER WHERE ime = ?";
    $ps = $povezava->prepare($sql);
    $ps->bind_param("s", $upIme);
    $ps->execute();
    $r = $ps->get_result();
    if ($r->num_rows < 1) {
        throw new PrijavaException("Uporabnik ne obstaja");
    } else {
        $bazaHash = $r->fetch_assoc()["geslo"];
        if (password_verify($upGeslo, $bazaHash)) {
            return true;
        } else {
            throw new PrijavaException("Napacno Geslo");
        }
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
