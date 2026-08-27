-- ============================================================
-- Catalogo base de razas bovinas
-- La columna 'categoria' guarda el nombre del enum Java
-- (Raza.Categoria -> LECHE | CARNE | DOBLE_PROPOSITO)
-- ============================================================

INSERT INTO razas (nombre, categoria) VALUES
    ('Holstein',          'LECHE'),
    ('Jersey',            'LECHE'),
    ('Gyr Lechero',       'LECHE'),
    ('Pardo Suizo',       'DOBLE_PROPOSITO'),
    ('Simmental',         'DOBLE_PROPOSITO'),
    ('Girolando',         'DOBLE_PROPOSITO'),
    ('Brahman',           'CARNE'),
    ('Nelore',            'CARNE'),
    ('Angus',             'CARNE'),
    ('Brangus',           'CARNE'),
    ('Charolais',         'CARNE'),
    ('Senepol',           'CARNE');
