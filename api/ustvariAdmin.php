<?php
require_once __DIR__ . "/src/baza.php";

// Napisal četko
function preberiGeslo(string $vprasanje): string
{
    echo $vprasanje;

    $geslo = shell_exec(
        'powershell -Command "$p = Read-Host -AsSecureString; ' .
        '$b = [Runtime.InteropServices.Marshal]::SecureStringToBSTR($p); ' .
        '[Runtime.InteropServices.Marshal]::PtrToStringBSTR($b)"'
    );

    return trim($geslo ?? '');
}

echo "Ustvarjanje admin uporabnika\n";

$upIme = readline("Ime: ");
$upGeslo = preberiGeslo("Vnesi geslo: ");

$hash = password_hash($upGeslo, PASSWORD_DEFAULT);

// Baza in shranjevanje
Baza::init();
$povazava = Baza::poveziSe();

$sql = "INSERT INTO USER (ime,role,geslo) VALUES(?,'a',?)";
$ps = $povazava->prepare($sql);
$ps->bind_param("ss", $upIme, $hash);

if ($ps->execute()) {
    echo "Uporabnik uspesno dodan";

} else {
    echo "Napaka pri dodajanju uporabnika!";
}