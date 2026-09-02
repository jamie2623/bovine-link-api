-- ============================================================
-- Datos base: roles del sistema + catalogo de razas.
-- 'categoria' guarda el nombre del enum Raza.Categoria (LECHE | CARNE | DOBLE_PROPOSITO).
-- ============================================================

INSERT INTO roles (name) VALUES
    ('USUARIO'),
    ('ADMIN');

INSERT INTO razas (nombre, categoria) VALUES
    ('Holstein',    'LECHE'),
    ('Jersey',      'LECHE'),
    ('Gyr Lechero', 'LECHE'),
    ('Pardo Suizo', 'DOBLE_PROPOSITO'),
    ('Simmental',   'DOBLE_PROPOSITO'),
    ('Girolando',   'DOBLE_PROPOSITO'),
    ('Brahman',     'CARNE'),
    ('Nelore',      'CARNE'),
    ('Angus',       'CARNE'),
    ('Brangus',     'CARNE'),
    ('Charolais',   'CARNE'),
    ('Senepol',     'CARNE');
