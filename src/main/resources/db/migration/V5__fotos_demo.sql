-- ============================================================
-- Fotos reales para los datos de demo.
-- Reemplaza los placeholder de placehold.co (que V4 puso) por fotos de
-- ganado servidas por loremflickr. El parametro ?lock= las hace estables
-- (siempre la misma imagen).
-- Solo toca las URLs de placehold.co: no altera fotos subidas por usuarios.
-- ============================================================

-- Portada: una foto de ganado por publicacion demo
UPDATE imagenes
SET url = 'https://loremflickr.com/640/480/cattle,cow/?lock=' || publicacion_id
WHERE url LIKE 'https://placehold.co/%';

-- Foto extra en la mitad de las publicaciones demo, para que la galeria del
-- detalle muestre miniaturas.
INSERT INTO imagenes (url, foto_portada, publicacion_id)
SELECT
    'https://loremflickr.com/640/480/cow,farm/?lock=' || (publicacion_id + 200),
    FALSE,
    publicacion_id
FROM imagenes
WHERE url LIKE 'https://loremflickr.com/%'
  AND foto_portada = TRUE
  AND publicacion_id % 2 = 0;
