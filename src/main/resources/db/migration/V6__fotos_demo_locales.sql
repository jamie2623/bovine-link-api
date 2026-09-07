-- ============================================================
-- Fotos de demo servidas desde el frontend (public/demo/ganado-N.jpg).
-- Reemplazan las de loremflickr, que era un servicio externo poco fiable.
-- Son 4 fotos reales de ganado que rotan por publicacion.
-- Solo toca URLs de loremflickr -> no altera fotos subidas por usuarios.
-- ============================================================

UPDATE imagenes
SET url = '/demo/ganado-' || (((publicacion_id + CASE WHEN foto_portada THEN 0 ELSE 2 END) % 4) + 1) || '.jpg'
WHERE url LIKE 'https://loremflickr.com/%';
