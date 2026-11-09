<?php
namespace Jaz;
use Exception;
use Baza;
require_once __DIR__ . "/seje.php";
function filtriraj($metoda)
{
    $ok = true;
    // Filtracija vhoda
    if ($metoda != "POST") {
        echo json_encode(["info" => "Napacna poizvedba uporabi POST"]);
        $ok = false;
    }

    return $ok;
}

function jaz($metoda)
{
    if (!filtriraj($metoda)) {
        return;
    }
    if (!ustvariSejo()) {
        echo json_encode(["info" => "seja potekla"]);
        return;
    }

    // Brskanje po bazi
    $id = $_SESSION["id"];
    $povezava = Baza::poveziSe();
    $sql = "SELECT ime,role FROM USER WHERE id = ?";
    $ps = $povezava->prepare($sql);
    $ps->bind_param("i", $id);
    $ps->execute();
    $r = $ps->get_result();

    $v = $r->fetch_assoc();
    $uporabnikPodatki = ["info" => "ok"];
    $uporabnikPodatki["role"] = $v["role"];
    $uporabnikPodatki["ime"] = $v["ime"];

    $sql = "SELECT registrska FROM AVTO WHERE userId = ?";
    $ps = $povezava->prepare($sql);
    $ps->bind_param("i", $id);
    $ps->execute();
    $r = $ps->get_result();

    $uporabnikPodatki["avti"] = [];

    while ($v = $r->fetch_row()) {
        $uporabnikPodatki["avti"][] = $v[0];
    }

    echo json_encode($uporabnikPodatki);
}
