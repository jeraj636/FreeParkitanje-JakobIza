<?php

function ustvariSejo()
{
    session_start();
    if (!isset($_SESSION["id"])) {
        return false;
    }
    return true;
}
