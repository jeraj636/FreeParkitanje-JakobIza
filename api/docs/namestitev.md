# Baza MariaDB

```
CREATE OR REPLACE USER ime@streznik IDENTIFIED BY 'mocno geslo';
CREATE DATABASE fp;
GRANT ALL PRIVILEGES ON fp.* TO ime@straznik;
FLUSH PRIVILEGES;
```

# Inicializacija okolja

```
php install.php
```

## Interaktivno dodjaanje uporabnika

```
php ustvariAdmin.php
```
