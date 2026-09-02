-- ============================================================
-- Seed de DEMO (opcional). NO lo corre Flyway (esta fuera de db/migration).
--
-- Uso:
--   & "C:\Program Files\PostgreSQL\18\bin\psql.exe" -U postgres -h localhost -d bovine_db ^
--       -f src/main/resources/db/seed_demo.sql
--
-- Todos los usuarios demo tienen la contrasena:  demo1234
-- El hash bcrypt lo genera PostgreSQL con pgcrypto (crypt + bf), asi no depende
-- de pegar un hash a mano (que se corrompe facil por los '$').
-- Es idempotente: correrlo de nuevo REPARA los usuarios demo (deja password = demo1234).
-- ============================================================

CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- ---------- usuarios ----------
INSERT INTO users (name, email, telefono, password, rol_id) VALUES
 ('Admin Demo',     'admin@bovinelink.test', '70000000', crypt('demo1234', gen_salt('bf', 10)), (SELECT id FROM roles WHERE name = 'ADMIN')),
 ('Carlos Benitez', 'carlos@demo.test',      '70010001', crypt('demo1234', gen_salt('bf', 10)), (SELECT id FROM roles WHERE name = 'USUARIO')),
 ('Jazmin Lopez',   'jazmin@demo.test',      '70010002', crypt('demo1234', gen_salt('bf', 10)), (SELECT id FROM roles WHERE name = 'USUARIO')),
 ('Jamie Flores',   'jamie@demo.test',       '70010003', crypt('demo1234', gen_salt('bf', 10)), (SELECT id FROM roles WHERE name = 'USUARIO'))
ON CONFLICT (email) DO UPDATE SET
    name     = EXCLUDED.name,
    telefono = EXCLUDED.telefono,
    password = EXCLUDED.password,
    rol_id   = EXCLUDED.rol_id;

-- ---------- publicaciones ----------
INSERT INTO publicaciones
    (titulo_venta, descripcion, ubicacion, precio, estado, vendido_en, created_at, updated_at, user_id, raza_id)
SELECT
    v.titulo, v.descripcion, v.ubicacion, v.precio, v.estado,
    CASE WHEN v.estado = 'VENDIDO' THEN v.fecha ELSE NULL END,
    v.fecha, v.fecha,
    (SELECT id FROM users WHERE email = v.email),
    (SELECT id FROM razas WHERE nombre = v.raza)
FROM (VALUES
    ('Vaca Holstein lechera',  'Vaca mansa, 1 parto, buena produccion.',        'Santa Ana',        1500.00, 'EN_VENTA', TIMESTAMP '2026-08-27 09:00:00', 'carlos@demo.test', 'Holstein'),
    ('Toro Brahman PO',        'Reproductor Brahman puro, 3 anios.',            'San Miguel',       2900.00, 'VENDIDO',  TIMESTAMP '2026-08-14 13:30:00', 'carlos@demo.test', 'Brahman'),
    ('Novillo Angus engorde',  'Novillo Angus para engorde, 380 kg.',          'Santa Ana',         900.00, 'VENDIDO',  TIMESTAMP '2026-08-20 11:00:00', 'jazmin@demo.test', 'Angus'),
    ('Vaquilla Jersey',        'Vaquilla Jersey prenada, mansa.',              'La Libertad',      1300.00, 'EN_VENTA', TIMESTAMP '2026-08-26 15:10:00', 'jazmin@demo.test', 'Jersey'),
    ('Lote Girolando',         'Lote de 5 vaquillas Girolando.',               'Nueva Concepcion', 6500.00, 'EN_VENTA', TIMESTAMP '2026-08-25 08:30:00', 'jamie@demo.test',  'Girolando'),
    ('Toro Nelore',            'Reproductor Nelore, buena alzada.',            'Metapan',          3200.00, 'VENDIDO',  TIMESTAMP '2026-08-18 14:00:00', 'jamie@demo.test',  'Nelore'),
    ('Vaca Pardo Suizo',       'Alta produccion lechera, doble proposito.',    'San Salvador',     1800.00, 'EN_VENTA', TIMESTAMP '2026-08-22 09:15:00', 'carlos@demo.test', 'Pardo Suizo'),
    ('Novillos Brangus (3)',   'Grupo de 3 novillos Brangus de engorde.',      'Aguilares',        2700.00, 'EN_VENTA', TIMESTAMP '2026-08-19 16:45:00', 'jazmin@demo.test', 'Brangus'),
    ('Vaquilla Simmental',     'Vaquilla Simmental prenada.',                  'Nueva Concepcion', 2100.00, 'VENDIDO',  TIMESTAMP '2026-08-16 10:00:00', 'jamie@demo.test',  'Simmental'),
    ('Ternero Holstein',       'Ternero Holstein destetado, sano.',            'Metapan',           650.00, 'EN_VENTA', TIMESTAMP '2026-08-28 11:20:00', 'carlos@demo.test', 'Holstein')
) AS v(titulo, descripcion, ubicacion, precio, estado, fecha, email, raza)
WHERE NOT EXISTS (SELECT 1 FROM publicaciones);

-- ---------- imagenes (una portada por publicacion) ----------
INSERT INTO imagenes (url, foto_portada, publicacion_id)
SELECT
    'https://placehold.co/600x400?text=' || replace(p.titulo_venta, ' ', '+'),
    TRUE,
    p.id
FROM publicaciones p
WHERE NOT EXISTS (SELECT 1 FROM imagenes);

-- Resumen
SELECT
    (SELECT count(*) FROM users)         AS users,
    (SELECT count(*) FROM publicaciones) AS publicaciones,
    (SELECT count(*) FROM imagenes)      AS imagenes;
