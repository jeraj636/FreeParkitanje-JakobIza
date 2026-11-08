<?php
namespace Parkirplaci;
use Exception;
use Baza;
function parkirplaci()
{
    try {

        $povezava = Baza::poveziSe();
        $sql = "SELECT * FROM PARKING";
        $ps = $povezava->prepare($sql);
        $ps->execute();
        $r = $ps->get_result();
        $out = ["info" => "ok", "parkirplaci" => []];
        while ($v = $r->fetch_assoc()) {
            $out["parkirplaci"][] = $v;
        }
        echo json_encode($out);
    } catch (Exception $_) {
        echo json_encode(["info" => "Napaka pri pridobivanju parkirplacov"]);
    }
}
