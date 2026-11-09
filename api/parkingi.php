<?php
require_once __DIR__ . "/src/baza.php";

Baza::init();
function dodajParking($x, $y, $smer)
{
    $povazava = Baza::poveziSe();
    $sql = "INSERT INTO PARKING(x,y,smer) VALUES(?,?,?)";
    $ps = $povazava->prepare($sql);
    $ps->bind_param("iii", $x, $y, $smer);
    $ps->execute();
}
for ($i = 0; $i < 20; $i++) {
    dodajParking($i, 0, 1);
    dodajParking($i, 3, 1);
}

for ($i = 0; $i < 10; $i++) {
    dodajParking($i, 1, 0);
    dodajParking($i, 2, 0);
}
