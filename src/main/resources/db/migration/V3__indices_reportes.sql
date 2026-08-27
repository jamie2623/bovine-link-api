-- ============================================================
-- Indices de apoyo para Dashboard y Reportes (Persona 3)
-- Solo lectura: no cambia el modelo de datos.
--   - filtro por rango de fecha en /api/reportes
--   - agrupado por ubicacion en /api/dashboard/por-ubicacion
-- ============================================================

CREATE INDEX IF NOT EXISTS idx_publicaciones_fecha_publicacion
    ON publicaciones (fecha_publicacion);

CREATE INDEX IF NOT EXISTS idx_publicaciones_ubicacion
    ON publicaciones (ubicacion);
