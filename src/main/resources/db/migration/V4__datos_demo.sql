-- ============================================================
-- Datos de DEMO. La corre Flyway al arrancar la app: no hay que
-- ejecutar nada a mano.
--
-- Usuarios: contrasena  demo1234
-- El hash bcrypt va literal (Flyway lo lee del classpath y lo ejecuta por
-- JDBC, no pasa por ninguna shell, asi que los '$' no se corrompen).
--
-- Guardas:
--   - users: ON CONFLICT (email) DO UPDATE -> si ya existen, les corrige la clave
--   - publicaciones / imagenes: solo si las tablas estan vacias
-- ============================================================

-- ---------- usuarios demo ----------
-- $2a$10$I3zLcL.XF/RrdGvT.BIkfuA3Nr2xRphSm1KrrGhdQ.BLT5Muv2c1S  =  bcrypt('demo1234')
INSERT INTO users (name, email, telefono, password, rol_id) VALUES
 ('Admin Demo',     'admin@bovinelink.test', '70000000', '$2a$10$I3zLcL.XF/RrdGvT.BIkfuA3Nr2xRphSm1KrrGhdQ.BLT5Muv2c1S', (SELECT id FROM roles WHERE name = 'ADMIN')),
 ('Carlos Benitez', 'carlos@demo.test',      '70010001', '$2a$10$I3zLcL.XF/RrdGvT.BIkfuA3Nr2xRphSm1KrrGhdQ.BLT5Muv2c1S', (SELECT id FROM roles WHERE name = 'USUARIO')),
 ('Jazmin Lopez',   'jazmin@demo.test',      '70010002', '$2a$10$I3zLcL.XF/RrdGvT.BIkfuA3Nr2xRphSm1KrrGhdQ.BLT5Muv2c1S', (SELECT id FROM roles WHERE name = 'USUARIO')),
 ('Jamie Flores',   'jamie@demo.test',       '70010003', '$2a$10$I3zLcL.XF/RrdGvT.BIkfuA3Nr2xRphSm1KrrGhdQ.BLT5Muv2c1S', (SELECT id FROM roles WHERE name = 'USUARIO'))
ON CONFLICT (email) DO UPDATE SET
    name     = EXCLUDED.name,
    telefono = EXCLUDED.telefono,
    password = EXCLUDED.password,
    rol_id   = EXCLUDED.rol_id;

-- ---------- publicaciones demo (solo si no hay ninguna) ----------
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

-- ---------- imagenes (una portada por publicacion, solo si no hay ninguna) ----------
INSERT INTO imagenes (url, foto_portada, publicacion_id)
SELECT
    'https://placehold.co/600x400?text=' || replace(p.titulo_venta, ' ', '+'),
    TRUE,
    p.id
FROM publicaciones p
WHERE NOT EXISTS (SELECT 1 FROM imagenes);
