--Tabela Strefa
CREATE TABLE IF NOT EXISTS Strefa (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nazwa TEXT NOT NULL,
    typ TEXT NOT NULL CHECK(typ IN ('LEKKOATLETYCZNA', 'CROSSFITOWA')),
    cenaZaGodzine REAL NOT NULL,
    maksymalnaLiczbaOsob INTEGER,
    opis TEXT
    );

--Tabela Harmonogram (asocjacja 1→7 ze Strefą)
CREATE TABLE IF NOT EXISTS Harmonogram (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    strefaId INTEGER NOT NULL,
    dzienTygodnia TEXT NOT NULL CHECK(dzienTygodnia IN ('MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY')),
    godzinaOtwarcia TEXT NOT NULL,
    godzinaZamkniecia TEXT NOT NULL,
    FOREIGN KEY (strefaId) REFERENCES Strefa(id) ON DELETE CASCADE
    );

--Tabela Klient
CREATE TABLE IF NOT EXISTS Klient (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    imie TEXT NOT NULL,
    nazwisko TEXT NOT NULL,
    email TEXT UNIQUE NOT NULL,
    telefon TEXT,
    stadiumZaawansowania TEXT CHECK(stadiumZaawansowania IN ('POCZATKUJACY', 'SREDNIOZAAWANSOWANY', 'ZAAWANSOWANY')),
    nazwaInstytucji TEXT  -- NULL dla osób fizycznych
    );

--Tabela TrenerPersonalny
CREATE TABLE IF NOT EXISTS TrenerPersonalny (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    imie TEXT NOT NULL,
    nazwisko TEXT NOT NULL,
    email TEXT UNIQUE NOT NULL,
    telefon TEXT,
    typTrenera TEXT NOT NULL CHECK(typTrenera IN ('SILOWY', 'CARDIO', 'STRETCHING')),
    stawkaZaGodzine REAL NOT NULL,
    maksymalnaLiczbaKlientow INTEGER
    );

--Tabela Karnet
CREATE TABLE IF NOT EXISTS Karnet (
id INTEGER PRIMARY KEY AUTOINCREMENT,
numer TEXT UNIQUE NOT NULL,
klientId INTEGER NOT NULL,
dataWydania DATE NOT NULL,
dataWaznosci DATE NOT NULL,
typ TEXT NOT NULL CHECK(typ IN ('JEDNORAZOWY', 'MIESIECZNY', 'KWARTALNY', 'ROCZNY')),
    liczbaDostepnychWejsc INTEGER,
    status TEXT NOT NULL CHECK(status IN ('AKTYWNY', 'NIEAKTYWNY')),
    FOREIGN KEY (klientId) REFERENCES Klient(id)
    );

--Tabela Rezerwacja
CREATE TABLE IF NOT EXISTS Rezerwacja (
id INTEGER PRIMARY KEY AUTOINCREMENT,
numer TEXT UNIQUE NOT NULL,
klientId INTEGER NOT NULL,
strefaId INTEGER NOT NULL,
trenerId INTEGER,
karnetId INTEGER,
dataRezerwacji DATE NOT NULL,
godzinaRozpoczecia TIME NOT NULL,
godzinaZakonczenia TIME NOT NULL,
status TEXT NOT NULL CHECK(status IN ('NOWA', 'OCZEKUJACA', 'ZAAKCEPTOWANA', 'ODRZUCONA', 'ANULOWANA', 'ZREALIZOWANA')),
    kosztStrefy REAL NOT NULL,
    kosztTrenera REAL DEFAULT 0,
    FOREIGN KEY (klientId) REFERENCES Klient(id),
    FOREIGN KEY (strefaId) REFERENCES Strefa(id),
    FOREIGN KEY (trenerId) REFERENCES TrenerPersonalny(id),
    FOREIGN KEY (karnetId) REFERENCES Karnet(id)
    );

--Tabela Platnosc (kompozycja z Rezerwacją)
CREATE TABLE IF NOT EXISTS Platnosc (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    rezerwacjaId INTEGER UNIQUE NOT NULL,
    kwota REAL NOT NULL,
    dataPlatnosci DATETIME NOT NULL,
    metodaPlatnosci TEXT NOT NULL CHECK(metodaPlatnosci IN ('KARTA', 'PRZELEW')),
    statusPlatnosci TEXT NOT NULL CHECK(statusPlatnosci IN ('ZREALIZOWANA', 'ANULOWANA')),
    typPlatnosci TEXT NOT NULL CHECK(typPlatnosci IN ('KARNET', 'REZERWACJA')),
    FOREIGN KEY (rezerwacjaId) REFERENCES Rezerwacja(id) ON DELETE CASCADE
    );

-- Tabela Promocja
CREATE TABLE IF NOT EXISTS Promocja (
id INTEGER PRIMARY KEY AUTOINCREMENT,
kod TEXT UNIQUE NOT NULL,
znizka REAL NOT NULL CHECK(znizka >= 0 AND znizka <= 100),
dataWaznosci DATE NOT NULL,
opis TEXT,
status TEXT NOT NULL CHECK(status IN ('AKTYWNA', 'DEZAKTYWOWANA'))
);

-- Tabela PlanTreningowy
CREATE TABLE IF NOT EXISTS PlanTreningowy (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    trenerId INTEGER NOT NULL,
    klientId INTEGER NOT NULL,
    dataUtworzenia DATE NOT NULL,
    okresWaznosci INTEGER NOT NULL,
    opis TEXT,
    cel TEXT,
    FOREIGN KEY (trenerId) REFERENCES TrenerPersonalny(id),
    FOREIGN KEY (klientId) REFERENCES Klient(id)
);

-- Tabela ImprezaSportowa
CREATE TABLE IF NOT EXISTS ImprezaSportowa (
 id INTEGER PRIMARY KEY AUTOINCREMENT,
 strefaId INTEGER NOT NULL,
 nazwa TEXT NOT NULL,
 dataRozpoczecia DATETIME NOT NULL,
 dataZakonczenia DATETIME NOT NULL,
 opis TEXT,
 status TEXT NOT NULL CHECK(status IN ('PLANOWANA', 'ZREALIZOWANA', 'ANULOWANA')),
 FOREIGN KEY (strefaId) REFERENCES Strefa(id)
);

--PRZYKŁADOWE DANE
INSERT INTO Promocja (kod, znizka, dataWaznosci, opis, status) VALUES
('ZIMA2026', 20.0, '2026-02-28', 'Zimowa promocja 20%', 'AKTYWNA'),
('STUDENT10', 10.0, '2026-12-31', 'Zniżka studencka 10%', 'AKTYWNA');

INSERT INTO PlanTreningowy (trenerId, klientId, dataUtworzenia, okresWaznosci, opis, cel) VALUES
(1, 1, '2026-01-01', 30, 'Plan treningowy siłowy dla średniozaawansowanych', 'Budowa masy mięśniowej'),
(2, 2, '2026-01-05', 60, 'Plan cardio dla zaawansowanych', 'Poprawa wytrzymałości');

INSERT INTO ImprezaSportowa (strefaId, nazwa, dataRozpoczecia, dataZakonczenia, opis, status) VALUES
(1, 'Maraton Noworoczny', '2026-01-20 10:00:00', '2026-01-20 14:00:00', 'Bieg maratoński', 'PLANOWANA'),
(2, 'Turniej CrossFit', '2026-02-15 09:00:00', '2026-02-15 18:00:00', 'Zawody crossfitowe', 'PLANOWANA');

--Strefy
INSERT INTO Strefa (nazwa, typ, cenaZaGodzine, maksymalnaLiczbaOsob, opis) VALUES
('Strefa lekkoatletyczna', 'LEKKOATLETYCZNA', 80.0, 15, 'Bieżnia, tor lekkoatletyczny, długość 200m'),
('Strefa crossfitowa', 'CROSSFITOWA', 80.0, 10, 'Kettlebell, hantla, box, rope');

--Harmonogramy (7 dni * 2 strefy = 14)
INSERT INTO Harmonogram (strefaId, dzienTygodnia, godzinaOtwarcia, godzinaZamkniecia) VALUES
--Strefa 1 (Lekkoatletyczna)
(1, 'MONDAY', '08:00', '22:00'),
(1, 'TUESDAY', '08:00', '22:00'),
(1, 'WEDNESDAY', '08:00', '22:00'),
(1, 'THURSDAY', '08:00', '22:00'),
(1, 'FRIDAY', '08:00', '22:00'),
(1, 'SATURDAY', '10:00', '20:00'),
(1, 'SUNDAY', '10:00', '18:00'),
--Strefa 2 (Crossfitowa)
(2, 'MONDAY', '08:00', '22:00'),
(2, 'TUESDAY', '08:00', '22:00'),
(2, 'WEDNESDAY', '08:00', '22:00'),
(2, 'THURSDAY', '08:00', '22:00'),
(2, 'FRIDAY', '08:00', '22:00'),
(2, 'SATURDAY', '10:00', '20:00'),
(2, 'SUNDAY', '10:00', '18:00');

--Klienci
INSERT INTO Klient (imie, nazwisko, email, telefon, stadiumZaawansowania, nazwaInstytucji) VALUES
('Jan', 'Kowalski', 'jan.kowalski@example.com', '123456789', 'SREDNIOZAAWANSOWANY', NULL),
('Anna', 'Nowak', 'anna.nowak@example.com', '987654321', 'ZAAWANSOWANY', NULL),
('Firma ABC Sp. z o.o.', '', 'kontakt@firmaabc.pl', '555123456', 'POCZATKUJACY', 'Firma ABC Sp. z o.o.');

--Trenerzy
INSERT INTO TrenerPersonalny (imie, nazwisko, email, telefon, typTrenera, stawkaZaGodzine, maksymalnaLiczbaKlientow) VALUES
('Jan', 'Kowalski', 'jan.kowalski.trener@example.com', '111222333', 'SILOWY', 150.0, 5),
('Anna', 'Nowak', 'anna.nowak.trener@example.com', '444555666', 'CARDIO', 120.0, 8),
('Piotr', 'Wiśniewski', 'piotr.wisniewski@example.com', '777888999', 'STRETCHING', 100.0, 10);

--Karnety (przykładowy aktywny karnet dla testów)
INSERT INTO Karnet (numer, klientId, dataWydania, dataWaznosci, typ, liczbaDostepnychWejsc, status) VALUES
('K2026-00001', 1, '2026-01-01', '2026-12-31', 'MIESIECZNY', NULL, 'AKTYWNY');

DELETE FROM Karnet WHERE numer = 'K2026-00001';
DELETE FROM Karnet;
-- ═══════════════════════════════════════════════════════════
-- KROK 1: Usuń stare harmonogramy
-- ═══════════════════════════════════════════════════════════
DELETE FROM Harmonogram;

-- ═══════════════════════════════════════════════════════════
-- KROK 2: Dodaj nowe harmonogramy z RÓŻNYMI GODZINAMI
-- ═══════════════════════════════════════════════════════════

-- Strefa 1: Lekkoatletyczna - 8:00-16:00 (wszystkie dni)
-- Przedziały: 8-10, 10-12, 12-14, 14-16
INSERT INTO Harmonogram (strefaId, dzienTygodnia, godzinaOtwarcia, godzinaZamkniecia) VALUES
(1, 'MONDAY', '08:00', '16:00'),
(1, 'TUESDAY', '08:00', '16:00'),
(1, 'WEDNESDAY', '08:00', '16:00'),
(1, 'THURSDAY', '08:00', '16:00'),
(1, 'FRIDAY', '08:00', '16:00'),
(1, 'SATURDAY', '08:00', '16:00'),
(1, 'SUNDAY', '08:00', '16:00');

-- Strefa 2: Crossfitowa - 10:00-18:00 (wszystkie dni)
-- Przedziały: 10-12, 12-14, 14-16, 16-18
INSERT INTO Harmonogram (strefaId, dzienTygodnia, godzinaOtwarcia, godzinaZamkniecia) VALUES
(2, 'MONDAY', '10:00', '18:00'),
(2, 'TUESDAY', '10:00', '18:00'),
(2, 'WEDNESDAY', '10:00', '18:00'),
(2, 'THURSDAY', '10:00', '18:00'),
(2, 'FRIDAY', '10:00', '18:00'),
(2, 'SATURDAY', '10:00', '18:00'),
(2, 'SUNDAY', '10:00', '18:00');

SELECT
    s.nazwa AS strefa,
    h.dzienTygodnia,
    h.godzinaOtwarcia,
    h.godzinaZamkniecia
FROM Harmonogram h
         JOIN Strefa s ON h.strefaId = s.id
ORDER BY s.id, h.dzienTygodnia;

SELECT * FROM Klient WHERE id = 1;

DELETE FROM Platnosc;
DELETE FROM Rezerwacja;