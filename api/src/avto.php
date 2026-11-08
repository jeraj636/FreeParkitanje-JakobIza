<?php
namespace Avto;
use Exception;
use Baza;
require_once __DIR__ . "/seje.php";
function filtriraj($metoda, $akcija, $registerska)
{
    $ok = true;
    // Filtracija vhoda
    if ($metoda != "POST") {
        echo json_encode(["info" => "Napacna poizvedba uporabi POST"]);
        $ok = false;
    }
    if (!isset($akcija) || !in_array($akcija, ["dodaj", "skenslaj"])) {
        echo json_encode(["info" => "Napacen endpoint"]);
        $ok = false;
    }
    if (!isset($registerska)) {
        echo json_encode(["info" => "Napacna poizvedba specificiraj registerska"]);
        $ok = false;
    }
    return $ok;
}
function dodaj($registerska, $uporabnikId)
{
    $sql = "INSERT INTO AVTO(userId,registrska) VALUES(?,?)";

    $povezava = Baza::poveziSe();

    $ps = $povezava->prepare($sql);
    $ps->bind_param("is", $uporabnikId, $registerska);

    if ($ps->execute()) {
        echo json_encode(["info" => "ok"]);
    } else {
        echo json_encode(["info" => "Uporabnik s to registersko že obstaja"]);
    }
}
function skenslaj($registrska, $upodabnikId)
{
    $sql = "DELETE FROM AVTO WHERE registrska = ? AND userId = ?";

    $povezava = Baza::poveziSe();

    $ps = $povezava->prepare($sql);
    $ps->bind_param("si", $registrska, $upodabnikId);

    if ($ps->execute() && $ps->affected_rows == 1) {
        echo json_encode(["info" => "ok"]);
    } else {
        echo json_encode(["info" => "Uporabnika s to registersko ni"]);
    }

}
function avto($metoda, $akcija)
{
    if (!filtriraj($metoda, $akcija, $_POST["registrska"])) {
        return;
    }
    if (!ustvariSejo()) {
        echo json_encode(["info" => "seja potekla"]);
        return;
    }
    $registrska = $_POST["registrska"];
    $uporabnikId = $_SESSION["id"];
    match ($akcija) {
        "dodaj" => dodaj($registrska, $uporabnikId),
        "skenslaj" => skenslaj($registrska, $uporabnikId),
    };

}

