-- ============================================================
-- Indices de apoyo para Dashboard y Reportes.
-- Solo lectura: no cambian el modelo de datos.
--   - filtro por rango de fecha en /api/reportes  -> publicaciones.created_at
--   - agrupado por ubicacion en /api/dashboard/por-ubicacion
-- (los indices por user_id / raza_id / estado ya los crea V1)
-- ============================================================

CREATE INDEX IF NOT EXISTS idx_publicaciones_created_at
    ON publicaciones (created_at);

CREATE INDEX IF NOT EXISTS idx_publicaciones_ubicacion
    ON publicaciones (ubicacion);
