<?php
// Zagon (samo iz ukazne vrstice na strežniku): php parkingi.php potrdi
// POZOR: izbriše vse rezervacije in vsa parkirna mesta ter vstavi nova.

// Skript se ne sme izvesti prek spleta.
if (PHP_SAPI !== "cli") {
    http_response_code(403);
    exit;
}

require_once __DIR__ . "/src/baza.php";

if (($argv[1] ?? "") !== "potrdi") {
    echo "Dodaj argument 'potrdi'. To izbriše vse rezervacije in mesta.\n";
    exit;
}

Baza::init();
function dodajParking($x, $y, $smer)
{
    $povazava = Baza::poveziSe();
    $sql = "INSERT INTO PARKING(x,y,smer) VALUES(?,?,?)";
    $ps = $povazava->prepare($sql);
    $ps->bind_param("iii", $x, $y, $smer);
    $ps->execute();
}

// Najprej izbrišemo stara mesta (in rezervacije, ker so vezane nanja).
$povezava = Baza::poveziSe();
$povezava->query("DELETE FROM REZERVACIJA");
$povezava->query("DELETE FROM PARKING");
$povezava->query("ALTER TABLE PARKING AUTO_INCREMENT = 1");

// smer 1 = navpično mesto, smer 0 = vodoravno mesto.

// Zgornja vrsta: 17 navpičnih mest.
for ($i = 0; $i < 17; $i++) {
    dodajParking(3 + $i, 0, 1);
}

// Dve srednji vrsti: po 15 navpičnih mest.
for ($i = 0; $i < 15; $i++) {
    dodajParking(3 + $i, 5, 1);
}
for ($i = 0; $i < 15; $i++) {
    dodajParking(3 + $i, 7, 1);
}

// Spodnja vrsta: 13 navpičnih mest (proti vhodu levo spodaj).
for ($i = 0; $i < 13; $i++) {
    dodajParking(8 + $i, 12, 1);
}

// Dve vodoravni vrsti po 6 mest, na sredini.
for ($i = 0; $i < 6; $i++) {
    dodajParking(5 + 2 * $i, 3, 0);
}
for ($i = 0; $i < 6; $i++) {
    dodajParking(5 + 2 * $i, 10, 0);
}

// Levi rob: 4 vodoravna mesta.
for ($i = 0; $i < 4; $i++) {
    dodajParking(0, 2 + $i, 0);
}

// Desni rob: 10 vodoravnih mest.
for ($i = 0; $i < 10; $i++) {
    dodajParking(21, 2 + $i, 0);
}

echo "Vstavljenih mest: 86\n";