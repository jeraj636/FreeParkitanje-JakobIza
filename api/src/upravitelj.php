<?php

require_once "baza.php";
require_once __DIR__ . "/prijava.php";
require_once __DIR__ . "/registracija.php";
require_once __DIR__ . "/jaz.php";
require_once __DIR__ . "/avto.php";
require_once __DIR__ . "/rezervacija.php";
require_once __DIR__ . "/prostost.php";
function obdelajZahtevo($metoda, $vir, $id)
{
    Baza::init();
    $dovoljeneFunkcije = [
        "prijava" => "Prijava\\prijava",
        "registracija" => "Registracija\\registracija",
        "jaz" => "Jaz\\jaz",
        "avto" => "Avto\\avto",
        "rezervacija" => "Rezervacija\\rezervacija",
        "prostost" => "prostost\\prostost"
    ];
    if (isset($dovoljeneFunkcije[$vir])) {
        $dovoljeneFunkcije[$vir]($metoda, $id);
    } else {
        echo json_encode(["uporabi primeren endpoint"]);
    }
}

