<?php

require_once "baza.php";
require_once __DIR__ . "/prijava.php";
require_once __DIR__ . "/registracija.php";
function obdelajZahtevo($metoda, $vir, $id)
{
    Baza::init();
    $dovoljeneFunkcije = ["prijava" => "Prijava\\prijava", "registracija" => "Registracija\\registracija"];
    if (isset($dovoljeneFunkcije[$vir])) {
        $dovoljeneFunkcije[$vir]($metoda, $id);
    } else {
        echo json_encode(["uporabi primeren endpoint"]);
    }
}

