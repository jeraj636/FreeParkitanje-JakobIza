<?php
require_once __DIR__ . '/vendor/autoload.php';
Dotenv\Dotenv::createUnsafeImmutable(__DIR__)->load();

// Source - https://stackoverflow.com/a/41335048
// Posted by Olaf Erlandsen, modified by community. See post 'Timeline' for change history
// Retrieved 2026-11-01, License - CC BY-SA 3.0
$origin = "";
if (array_key_exists('HTTP_ORIGIN', $_SERVER)) {
    $origin = $_SERVER['HTTP_ORIGIN'];
} elseif (array_key_exists('HTTP_REFERER', $_SERVER)) {
    $origin = $_SERVER['HTTP_REFERER'];
} else {
    $origin = $_SERVER['REMOTE_ADDR'];
}
/*
$dovoljeneDomene = explode(' ', getenv("DOVOLJENE_SPLETNE_STRANI"));
if (in_array($origin, $dovoljeneDomene, true)) {
    header("Access-Control-Allow-Origin: " . $origin);
    header("Vary: Origin");
}
*/

header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET, POST, OPTIONS");
header("Access-Control-Allow-Headers: Content-Type");

if ($_SERVER["REQUEST_METHOD"] === "OPTIONS") {
    http_response_code(204);
    exit;
}

header("Content-Type: application/json");

$metoda = $_SERVER['REQUEST_METHOD'];
$pot = parse_url($_SERVER['REQUEST_URI'], PHP_URL_PATH);

// Odstranimo /api/ iz začetka poti
$pot = preg_replace('#^/api/?#', '', $pot);

$zahteva = explode("/", trim($pot, "/"));

$vir = $zahteva[0] ?? null;
$id = $zahteva[1] ?? null;

require_once "src/upravitelj.php";
obdelajZahtevo($metoda, $vir, $id);