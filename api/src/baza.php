<?php
require_once __DIR__ . '/../vendor/autoload.php';
Dotenv\Dotenv::createUnsafeImmutable(__DIR__ . '/../')->load();

class BazaException extends Exception
{

}
class Baza
{
    private static $pbUser = "";
    private static $pbGeslo = "";
    private static $pbStreznik = "";
    private static $pbPort = "";
    private static $pbIme = "";
    public static function init()
    {
        $napakaBesedilo = "Napaka pri branju .env za spremenljivko: ";
        // branje .env
        Baza::$pbUser = getenv("USER_PB") ?? throw new BazaException($napakaBesedilo . "USER_PB");
        Baza::$pbGeslo = getenv("GESLO_PB") ?? throw new BazaException($napakaBesedilo . "GESLO_PB");
        Baza::$pbStreznik = getenv("STREZNIK_PB") ?? throw new BazaException($napakaBesedilo . "STREZNIK_PB");
        Baza::$pbPort = getenv("PORT_PB") ?? throw new BazaException($napakaBesedilo . "PORT_PB");
        Baza::$pbIme = getenv("IME_PB") ?? throw new BazaException($napakaBesedilo . "IME_PB");

    }
    public static function poveziSe(): mysqli
    {
        $povezava = new mysqli(Baza::$pbStreznik, Baza::$pbUser, Baza::$pbGeslo, Baza::$pbIme);
        if ($povezava->connect_error) {
            throw new BazaException("Napaka pri povezavi");
        }
        return $povezava;
    }

}


