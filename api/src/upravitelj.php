<?php

require_once "baza.php";
require_once __DIR__ . "/prijava.php";
function obdelajZahtevo($metoda, $vir, $id)
{
    Baza::init();
    $dovoljeneFunkcije = ["prijava"];
    if (in_array($vir, $dovoljeneFunkcije)) {
        $vir($metoda, $id);
    } else {
        echo json_encode(["uporabi primeren endpoint"]);
    }
}

