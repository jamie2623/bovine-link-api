-- ============================================================
-- V8: catalogo de razas definitivo para el sidebar del catalogo.
--
-- Deja la tabla `razas` exactamente con estas 13. El sidebar las
-- agrupa por categoria y las muestra en el orden en que quedan
-- insertadas (id ascendente):
--
--   Ganado Lechero          : Pardo Suizo, Jersey, Gir Lechero, Holstein
--   Ganado de Carne         : Brahman, Nelore, Angus, Guzerat, Brangus
--   Doble Proposito y Criollo: Girolando, Simmental, Simbrah, Barroso Salvadoreño
--
-- Como `publicaciones.raza_id` referencia `razas`, se reconstruye la
-- tabla y se re-mapea cada publicacion por NOMBRE de raza:
--   * 'Gyr Lechero' pasa a llamarse 'Gir Lechero'
--   * 'Charolais' -> 'Angus'   (raza que se elimina)
--   * 'Senepol'   -> 'Brahman' (raza que se elimina)
--   * cualquier otro caso sin equivalencia -> 'Brahman' (ultimo recurso)
-- ============================================================

-- 1. Guardar la raza (por nombre) que tiene hoy cada publicacion.
CREATE TEMPORARY TABLE tmp_pub_raza AS
SELECT p.id AS pub_id, r.nombre AS raza_nombre
FROM publicaciones p
JOIN razas r ON r.id = p.raza_id;

-- 2. Reconstruir el catalogo con ids 1..13 en el orden deseado.
ALTER TABLE publicaciones DROP CONSTRAINT fk_publicaciones_raza;

DELETE FROM razas;
ALTER TABLE razas ALTER COLUMN id RESTART WITH 1;

INSERT INTO razas (nombre, categoria) VALUES
    ('Pardo Suizo',         'LECHE'),
    ('Jersey',              'LECHE'),
    ('Gir Lechero',         'LECHE'),
    ('Holstein',            'LECHE'),
    ('Brahman',             'CARNE'),
    ('Nelore',              'CARNE'),
    ('Angus',               'CARNE'),
    ('Guzerat',             'CARNE'),
    ('Brangus',             'CARNE'),
    ('Girolando',           'DOBLE_PROPOSITO'),
    ('Simmental',           'DOBLE_PROPOSITO'),
    ('Simbrah',             'DOBLE_PROPOSITO'),
    ('Barroso Salvadoreño', 'DOBLE_PROPOSITO');

-- 3. Re-mapear cada publicacion a la nueva raza equivalente (por nombre).
UPDATE publicaciones p SET raza_id = COALESCE(
    (SELECT r.id FROM razas r WHERE r.nombre = (
        SELECT CASE t.raza_nombre
                   WHEN 'Gyr Lechero' THEN 'Gir Lechero'
                   WHEN 'Charolais'   THEN 'Angus'
                   WHEN 'Senepol'     THEN 'Brahman'
                   ELSE t.raza_nombre
               END
        FROM tmp_pub_raza t WHERE t.pub_id = p.id
    )),
    (SELECT id FROM razas WHERE nombre = 'Brahman')
);

-- 4. Restaurar la llave foranea.
ALTER TABLE publicaciones
    ADD CONSTRAINT fk_publicaciones_raza FOREIGN KEY (raza_id) REFERENCES razas (id);

DROP TABLE tmp_pub_raza;
