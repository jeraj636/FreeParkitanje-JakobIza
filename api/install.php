<?php
require_once __DIR__ . "/src/baza.php";

// Source - https://stackoverflow.com/a/10209702
// Posted by Ibrahim Lawal, modified by community. See post 'Timeline' for change history
// Retrieved 2026-11-07, License - CC BY-SA 3.0

function narediUkazeIzDatoteke($location)
{
    //load file
    $commands = file_get_contents($location);

    //delete comments
    $lines = explode("\n", $commands);
    $commands = '';
    foreach ($lines as $line) {
        $line = trim($line);
        if ($line && !startsWith($line, '--')) {
            $commands .= $line . "\n";
        }
    }

    //convert to array
    $commands = explode(";", $commands);
    return $commands;
}


// Here's a startsWith function
function startsWith($haystack, $needle)
{
    $length = strlen($needle);
    return substr($haystack, 0, $length) === $needle;
}


$script = "assets/struktura.sql";
echo "Grajenje strukture pb\n";

$ukazi = narediUkazeIzDatoteke($script);

Baza::init();
$povezava = Baza::poveziSe();
foreach ($ukazi as $ukaz) {
    echo "Postavljam strukturo baze ...\n";
    if (strlen($ukaz) == 0 || in_array($ukaz, ["\n", ";", "", " "])) {
        continue;
    }

    if (!$povezava->query($ukaz)) {
        echo "Napaka";
        throw new BazaException("Napaka pri postavljanju baze");
    }
}

echo "Struktura baze postavljena\n";






