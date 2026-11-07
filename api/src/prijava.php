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
    // Filtracija vhoda
    if ($metoda != "POST") {
        echo json_encode(["PHPSESSID" => "-1", "info" => "Napacna poizvedba uporabi POST"]);
        return false;
    }
    if (!isset($_POST["upIme"])) {
        echo json_encode(["PHPSESSID" => "-1", "info" => "Napacna poizvedba specificiraj upIme"]);
        return false;
    }
    if (!isset($_POST["upGeslo"])) {
        echo json_encode(["PHPSESSID" => "-1", "info" => "Napacna poizvedba specificiraj upGeslo"]);
        return false;
    }
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
        echo json_encode(["PHPSESSID" => "-1", "info" => $e->getMessage()]);
        return;
    }
    if ($uspelaPrijava) {
        try {
            $id = uporabnikId($upIme);
            // inicializacija seje
            session_start();
            $_SESSION["id"] = $id;

            echo json_encode(["PHPSESSID" => session_id(), "info" => "OK"]);
        } catch (HudaException $_) {
            echo json_encode(["PHPSESSID" => "-1", "info" => "huda napaka"]);
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
function uporabnikId($upIme)
{
    require_once __DIR__ . "/baza.php";

    $povezava = Baza::poveziSe();
    $sql = "SELECT id FROM USER WHERE ime = ?";
    $ps = $povezava->prepare($sql);
    $ps->bind_param("s", $upIme);
    $ps->execute();
    $r = $ps->get_result();


    if ($r->num_rows < 1) {
        throw new HudaException();
    } else {
        return $r->fetch_assoc()["id"];
    }
}
