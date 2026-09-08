-- ============================================================
-- Reemplaza las publicaciones demo por un set con fotos reales de
-- ganado (carpeta bovine-link-app/public/ganado del frontend).
--
-- Cada publicacion referencia sus imagenes como '/ganado/<archivo>.jpg';
-- Vite las sirve desde public/. La portada es la primera imagen de cada
-- publicacion (foto_portada = TRUE).
--
-- Razas usadas: Brahman, Holstein, Gyr Lechero, Jersey.
-- Usuarios: los demo de V4 (carlos@, jazmin@, jamie@ demo.test).
-- ============================================================

-- Borra todo lo anterior y reinicia los IDs.
TRUNCATE TABLE imagenes, publicaciones RESTART IDENTITY CASCADE;

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
    ('Toro Brahman P.O.',       'Reproductor Brahman puro de origen, 3 anios. Excelente alzada, pigmentacion y aplomos. Manso para el manejo.',        'San Miguel',   3200.00, 'EN_VENTA', TIMESTAMP '2026-09-01 08:30:00', 'carlos@demo.test', 'Brahman'),
    ('Vaca Brahman con cria',   'Vaca Brahman de 4 anios, segundo parto. Buena madre y productora, se vende con ternero al pie.',                     'Usulutan',     1900.00, 'EN_VENTA', TIMESTAMP '2026-09-02 10:15:00', 'jazmin@demo.test', 'Brahman'),
    ('Vaca Holstein lechera',   'Vaca Holstein de primer parto, mansa, produciendo 22 L diarios. Ideal para iniciar hato lechero.',                  'Santa Ana',    1650.00, 'EN_VENTA', TIMESTAMP '2026-09-03 07:45:00', 'carlos@demo.test', 'Holstein'),
    ('Ternero Holstein',        'Ternero Holstein destetado, sano y desparasitado. Documentacion al dia.',                                           'Chalatenango',  700.00, 'EN_VENTA', TIMESTAMP '2026-09-04 09:00:00', 'jamie@demo.test',  'Holstein'),
    ('Vaca Gyr Lechero',        'Vaca Gyr Lechero adaptada al tropico, resistente al calor y a la garrapata. Ubre bien insertada.',                  'Sonsonate',    2100.00, 'EN_VENTA', TIMESTAMP '2026-09-04 16:20:00', 'jazmin@demo.test', 'Gyr Lechero'),
    ('Toro Gyr Lechero',        'Toro Gyr Lechero probado, con hijas en produccion. Genetica para cruzar con Holstein (Girolando).',                 'La Paz',       3500.00, 'VENDIDO',  TIMESTAMP '2026-08-21 11:00:00', 'jamie@demo.test',  'Gyr Lechero'),
    ('Vaquilla Jersey',         'Vaquilla Jersey proxima a parir, muy mansa. Raza de alta grasa y proteina en leche.',                               'La Libertad',  1450.00, 'EN_VENTA', TIMESTAMP '2026-09-05 08:10:00', 'carlos@demo.test', 'Jersey'),
    ('Toro Jersey',             'Toro Jersey joven, ideal para mejorar solidos en leche. Aplomos correctos y buen temperamento.',                    'Ahuachapan',   2600.00, 'VENDIDO',  TIMESTAMP '2026-08-19 14:30:00', 'jazmin@demo.test', 'Jersey')
) AS v(titulo, descripcion, ubicacion, precio, estado, fecha, email, raza);

-- ---------- imagenes (la primera de cada publicacion es la portada) ----------
INSERT INTO imagenes (url, foto_portada, publicacion_id)
SELECT '/ganado/' || f.archivo, f.portada,
       (SELECT id FROM publicaciones WHERE titulo_venta = f.titulo)
FROM (VALUES
    ('Toro Brahman P.O.',      'brahman-toro-1.jpg',      TRUE),
    ('Toro Brahman P.O.',      'brahman-toro-2.jpg',      FALSE),
    ('Vaca Brahman con cria',  'brahman-vaca-1.jpg',      TRUE),
    ('Vaca Brahman con cria',  'brahman-vaca-2.jpg',      FALSE),
    ('Vaca Brahman con cria',  'brahman-vaca-3.jpg',      FALSE),
    ('Vaca Holstein lechera',  'holstein-vaca-1.jpg',     TRUE),
    ('Vaca Holstein lechera',  'holstein-vaca-2.jpg',     FALSE),
    ('Ternero Holstein',       'holstein-ternero-1.jpg',  TRUE),
    ('Ternero Holstein',       'holstein-ternero-2.jpg',  FALSE),
    ('Vaca Gyr Lechero',       'gyr-lechero-vaca-1.jpg',  TRUE),
    ('Vaca Gyr Lechero',       'gyr-lechero-vaca-2.jpg',  FALSE),
    ('Vaca Gyr Lechero',       'gyr-lechero-vaca-3.jpg',  FALSE),
    ('Toro Gyr Lechero',       'gyr-lechero-toro-1.jpg',  TRUE),
    ('Toro Gyr Lechero',       'gyr-lechero-toro-2.jpg',  FALSE),
    ('Toro Gyr Lechero',       'gyr-lechero-toro-3.jpg',  FALSE),
    ('Vaquilla Jersey',        'jersey-vaca-1.jpg',       TRUE),
    ('Vaquilla Jersey',        'jersey-vaca-2.jpg',       FALSE),
    ('Vaquilla Jersey',        'jersey-vaca-3.jpg',       FALSE),
    ('Toro Jersey',            'jersey-toro-1.jpg',       TRUE),
    ('Toro Jersey',            'jersey-toro-2.jpg',       FALSE)
) AS f(titulo, archivo, portada);
