package SteveJobs.encuestas.servicio;

import SteveJobs.encuestas.dao.EncuestaDAO;
import SteveJobs.encuestas.dao.EncuestaDetallePreguntaDAO;
import SteveJobs.encuestas.dao.PreguntaBancoDAO;
import SteveJobs.encuestas.dao.TipoPreguntaDAO;
import SteveJobs.encuestas.dao.ClasificacionPreguntaDAO;
import SteveJobs.encuestas.modelo.Encuesta;
import SteveJobs.encuestas.modelo.EncuestaDetallePregunta;
import SteveJobs.encuestas.modelo.PreguntaBanco;
import SteveJobs.encuestas.modelo.TipoPregunta;
import SteveJobs.encuestas.modelo.ClasificacionPregunta;
import SteveJobs.encuestas.modelo.Usuario;
import SteveJobs.encuestas.dao.PreguntaRegistroDAO; // Necesario para obtener preguntas de registro
import SteveJobs.encuestas.dao.RespuestaUsuarioDAO; // Necesario para obtener respuestas de usuario

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class ServicioEncuestas {
    private EncuestaDAO encuestaDAO;
    private EncuestaDetallePreguntaDAO encuestaDetalleDAO;
    private PreguntaBancoDAO preguntaBancoDAO;
    private TipoPreguntaDAO tipoPreguntaDAO;
    private ClasificacionPreguntaDAO clasificacionPreguntaDAO;
    private PreguntaRegistroDAO preguntaRegistroDAO; // Instancia para acceder a preguntas de registro
    private RespuestaUsuarioDAO respuestaUsuarioDAO; // Instancia para acceder a respuestas de registro

    public ServicioEncuestas() {
        this.encuestaDAO = new EncuestaDAO();
        this.encuestaDetalleDAO = new EncuestaDetallePreguntaDAO();
        this.preguntaBancoDAO = new PreguntaBancoDAO();
        this.tipoPreguntaDAO = new TipoPreguntaDAO();
        this.clasificacionPreguntaDAO = new ClasificacionPreguntaDAO();
        this.preguntaRegistroDAO = new PreguntaRegistroDAO(); // Inicializar
        this.respuestaUsuarioDAO = new RespuestaUsuarioDAO(); // Inicializar
    }

    // CAMBIADO: 'publicoObjetivo' a 'esPublica' (boolean)
    public int registrarNuevaEncuesta(String nombre, String descripcion, Timestamp fechaInicio, Timestamp fechaFin, boolean esPublica, String perfilRequerido, int idAdmin) {
        if (nombre == null || nombre.trim().isEmpty()) {
            System.err.println("Servicio: El nombre de la encuesta es obligatorio.");
            return -1;
        }
        if (fechaFin.before(fechaInicio)) {
            System.err.println("Servicio: La fecha de fin no puede ser anterior a la de inicio.");
            return -1;
        }

        if (fechaInicio.before(new Timestamp(System.currentTimeMillis()))) {
            System.err.println("Servicio: La fecha de inicio no puede ser anterior a la fecha y hora actuales.");
            return -1;
        }
        
        // Si no es pública y no tiene perfil requerido, es un error de configuración
        if (!esPublica && (perfilRequerido == null || perfilRequerido.trim().isEmpty())) {
            System.err.println("Servicio: Si la encuesta no es pública, debe tener un perfil requerido definido.");
            return -1;
        }
        // No hay validación de JSON aquí, ya que hemos adaptado la BD para almacenar cadena simple.
        // Si el perfil requerido es proporcionado, se asume que está en el formato de cadena simple.

        Encuesta nuevaEncuesta = new Encuesta();
        nuevaEncuesta.setNombre(nombre.trim());
        nuevaEncuesta.setDescripcion(descripcion);
        nuevaEncuesta.setFechaInicio(fechaInicio);
        nuevaEncuesta.setFechaFin(fechaFin);
        nuevaEncuesta.setEsPublica(esPublica); // CAMBIO AQUÍ
        nuevaEncuesta.setPerfilRequerido(perfilRequerido);
        nuevaEncuesta.setIdAdminCreador(idAdmin);
        nuevaEncuesta.setEstado("Borrador");
        nuevaEncuesta.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
        
        return encuestaDAO.crearEncuesta(nuevaEncuesta);
    }

    public List<Encuesta> obtenerTodasLasEncuestasOrdenadasPorNombre() {
        List<Encuesta> encuestas = encuestaDAO.obtenerTodasLasEncuestas();
        if (encuestas == null || encuestas.size() <= 1) {
            return encuestas;
        }

        // Algoritmo de ordenamiento por inserción (Insertion Sort)
        for (int i = 1; i < encuestas.size(); i++) {
            Encuesta encuestaActual = encuestas.get(i);
            String nombreActual = encuestaActual.getNombre() != null ? encuestaActual.getNombre() : "";
            int j = i - 1;
            while (j >= 0 && (encuestas.get(j).getNombre() != null ? encuestas.get(j).getNombre() : "").compareToIgnoreCase(nombreActual) > 0) {
                encuestas.set(j + 1, encuestas.get(j));
                j = j - 1;
            }
            encuestas.set(j + 1, encuestaActual);
        }
        return encuestas;
    }

    public Encuesta buscarEncuestaEnListaPorId(List<Encuesta> listaEncuestas, int idBuscado) {
        if (listaEncuestas == null || listaEncuestas.isEmpty()) return null;

        // Implementación de búsqueda binaria
        List<Encuesta> copiaOrdenadaPorId = new ArrayList<>(listaEncuestas);
        copiaOrdenadaPorId.sort(Comparator.comparingInt(Encuesta::getIdEncuesta)); // Asegura que esté ordenada por ID

        int izq = 0, der = copiaOrdenadaPorId.size() - 1;
        while (izq <= der) {
            int med = izq + (der - izq) / 2;
            if (copiaOrdenadaPorId.get(med).getIdEncuesta() == idBuscado) {
                return copiaOrdenadaPorId.get(med);
            }
            if (copiaOrdenadaPorId.get(med).getIdEncuesta() < idBuscado) {
                izq = med + 1;
            } else {
                der = med - 1;
            }
        }
        return null;
    }

    private Timestamp convertirStringATimestamp(String fechaStr) {
        if (fechaStr == null || fechaStr.trim().isEmpty()) return null;
        try {
            SimpleDateFormat dateFormat;
            if (fechaStr.trim().length() > 10) {
                dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            } else {
                dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            }
            dateFormat.setLenient(false);
            Date parsedDate = dateFormat.parse(fechaStr.trim());
            return new Timestamp(parsedDate.getTime());
        } catch (ParseException e) {
            System.err.println("Error al parsear fecha: " + fechaStr + " - " + e.getMessage());
            return null;
        }
    }

    // CAMBIADO: 'nuevoPublicoObj' a 'nuevaEsPublica' (boolean)
    public boolean modificarMetadatosEncuesta(int idEncuesta, String nuevoNombre, String nuevaDescripcion, Timestamp nuevaFechaInicio, Timestamp nuevaFechaFin, boolean nuevaEsPublica, String nuevoPerfilDef) {
        Encuesta encuesta = encuestaDAO.obtenerEncuestaPorId(idEncuesta);
        if (encuesta == null) {
            return false;
        }
        // Validar que si la encuesta no es pública, debe tener un perfil requerido definido.
        if (!nuevaEsPublica && (nuevoPerfilDef == null || nuevoPerfilDef.trim().isEmpty())) {
            System.err.println("Servicio: Si la encuesta no es pública, debe tener un perfil requerido definido.");
            return false;
        }
        // No hay validación de JSON aquí, ya que hemos adaptado la BD para almacenar cadena simple.
        // Si el perfil requerido es proporcionado, se asume que está en el formato de cadena simple.
        
        encuesta.setNombre(nuevoNombre);
        encuesta.setDescripcion(nuevaDescripcion);
        encuesta.setFechaInicio(nuevaFechaInicio);
        encuesta.setFechaFin(nuevaFechaFin);
        encuesta.setEsPublica(nuevaEsPublica); // CAMBIO AQUÍ
        encuesta.setPerfilRequerido(nuevoPerfilDef);

        return encuestaDAO.actualizarEncuesta(encuesta);
    }

    public boolean cambiarEstadoEncuesta(int idEncuesta, String nuevoEstado) {
        Encuesta encuesta = encuestaDAO.obtenerEncuestaPorId(idEncuesta);
        if (encuesta == null) {
            return false;
        }
        if ("Activa".equalsIgnoreCase(nuevoEstado)) {
            if (encuestaDetalleDAO.contarPreguntasEnEncuesta(idEncuesta) != 12) {
                System.err.println("Servicio: No se puede activar. La encuesta debe tener exactamente 12 preguntas.");
                return false;
            }
        }
        return encuestaDAO.actualizarEstadoEncuesta(idEncuesta, nuevoEstado);
    }

    public boolean eliminarEncuesta(int idEncuesta) {
        System.out.println("Servicio: Intentando eliminar preguntas asociadas a encuesta ID " + idEncuesta);
        // La eliminación de respuestas asociadas a encuesta_detalle_pregunta
        // y luego a encuesta_detalle_pregunta se maneja por ON DELETE CASCADE en la BD.
        // Solo necesitamos eliminar la encuesta, lo que debería propagarse.
        // Si no estás seguro de CASCADE, mantener el método eliminarTodasPreguntasDeEncuesta es más seguro.
        boolean preguntasEliminadas = encuestaDetalleDAO.eliminarTodasPreguntasDeEncuesta(idEncuesta);

        System.out.println("Servicio: Eliminando encuesta ID " + idEncuesta);
        return encuestaDAO.eliminarEncuesta(idEncuesta);
    }
    
    // MODIFICADO: Lógica de filtrado para encuestas disponibles
    public List<Encuesta> obtenerEncuestasActivasParaUsuario(Usuario usuario) {
        System.out.println("Servicio: obtenerEncuestasActivasParaUsuario - Aplicando lógica de filtrado por perfil.");
        List<Encuesta> todasActivas = encuestaDAO.obtenerTodasLasEncuestas();
        List<Encuesta> activasFiltradas = new ArrayList<>();

        Timestamp ahora = new Timestamp(System.currentTimeMillis());
        
        // Instanciar ServicioParticipacion aquí para usar haUsuarioRespondidoEncuesta
        ServicioParticipacion servicioParticipacion = new ServicioParticipacion(); 

        for(Encuesta e : todasActivas){
            // 1. Verificar estado Activa y fechas válidas
            if("Activa".equalsIgnoreCase(e.getEstado()) &&
               (e.getFechaInicio() == null || !e.getFechaInicio().after(ahora)) &&
               (e.getFechaFin() == null || !e.getFechaFin().before(ahora)) )
            {
                // 2. Verificar si el usuario ya ha respondido la encuesta
                if (servicioParticipacion.haUsuarioRespondidoEncuesta(usuario.getId_usuario(), e.getIdEncuesta())) {
                    System.out.println("Servicio: Encuesta ID " + e.getIdEncuesta() + " ya respondida por usuario ID " + usuario.getId_usuario());
                    continue; // Saltar a la siguiente encuesta
                }

                // 3. Aplicar lógica de 'esPublica' o 'perfil_requerido'
                if (e.isEsPublica()) { // Si es pública, añadir directamente
                    activasFiltradas.add(e);
                    System.out.println("Servicio: Encuesta ID " + e.getIdEncuesta() + " es pública y disponible para usuario ID " + usuario.getId_usuario());
                } else { // Si no es pública, verificar perfil requerido
                    if (e.getPerfilRequerido() != null && !e.getPerfilRequerido().trim().isEmpty()) {
                        if (cumplePerfil(e.getPerfilRequerido(), usuario)) {
                            activasFiltradas.add(e);
                            System.out.println("Servicio: Encuesta ID " + e.getIdEncuesta() + " cumple perfil para usuario ID " + usuario.getId_usuario());
                        } else {
                            System.out.println("Servicio: Encuesta ID " + e.getIdEncuesta() + " no cumple con el perfil requerido para usuario ID " + usuario.getId_usuario() + ".");
                        }
                    } else {
                        // Si no es pública pero no tiene perfil requerido definido (mala configuración o no aplica)
                        System.out.println("Servicio: Encuesta ID " + e.getIdEncuesta() + " no es pública pero no tiene perfil requerido definido. No se mostrará.");
                    }
                }
            } else {
                System.out.println("Servicio: Encuesta ID " + e.getIdEncuesta() + " no está activa o fuera de fechas.");
            }
        }
        return activasFiltradas;
    }

    // Método cumplePerfil adaptado para parsear formato de cadena simple (no JSON)
    private boolean cumplePerfil(String perfilRequerido, Usuario usuario) {
        if (perfilRequerido == null || perfilRequerido.trim().isEmpty()) {
            return true; // Si no hay perfil requerido, se asume que cumple (esto se usará cuando esPublica sea false y perfilRequerido sea null)
        }

        String[] condiciones = perfilRequerido.trim().split(";");
        
        for (String cond : condiciones) {
            if (cond.trim().isEmpty()) continue;

            String[] partes = cond.trim().split(":", 2);
            if (partes.length != 2) {
                System.out.println("Servicio: Condición de perfil '" + cond + "' mal formada. Se asume que se cumple.");
                continue;
            }

            String clave = partes[0].trim().toUpperCase();
            String valor = partes[1].trim();

            switch (clave) {
                case "GENERO":
                    if (usuario.getGenero() == null || !usuario.getGenero().equalsIgnoreCase(valor)) {
                        System.out.println("No cumple por género. Requerido: " + valor + ", Usuario: " + usuario.getGenero());
                        return false;
                    }
                    break;
                case "DISTRITO":
                    // Si el valor es una lista separada por comas (ej. "Miraflores,San Isidro")
                    // la lógica actual solo compararía el primer valor.
                    // Para soportar múltiples distritos, 'valor' debería ser tratado como una lista de strings.
                    // Para una reestructuración mínima, la lógica actual compara estrictamente el valor.
                    if (usuario.getDistrito_residencia() == null || !usuario.getDistrito_residencia().equalsIgnoreCase(valor)) {
                        System.out.println("No cumple por distrito. Requerido: " + valor + ", Usuario: " + usuario.getDistrito_residencia());
                        return false;
                    }
                    break;
                case "EDAD":
                    if (usuario.getFecha_nacimiento() == null) {
                        System.out.println("Servicio: Condición de EDAD en perfil pero fecha de nacimiento de usuario es nula.");
                        return false;
                    }
                    try {
                        int edadUsuario = Period.between(usuario.getFecha_nacimiento(), LocalDate.now()).getYears();
                        
                        if (valor.startsWith(">=")) {
                            int edadMin = Integer.parseInt(valor.substring(2));
                            if (edadUsuario < edadMin) return false;
                        } else if (valor.startsWith("<=")) {
                            int edadMax = Integer.parseInt(valor.substring(2));
                            if (edadUsuario > edadMax) return false;
                        } else if (valor.startsWith(">")) {
                            int edadMin = Integer.parseInt(valor.substring(1));
                            if (edadUsuario <= edadMin) return false;
                        } else if (valor.startsWith("<")) {
                            int edadMax = Integer.parseInt(valor.substring(1));
                            if (edadUsuario >= edadMax) return false;
                        } else if (valor.startsWith("=")) {
                             int edadExacta = Integer.parseInt(valor.substring(1));
                             if (edadUsuario != edadExacta) return false;
                        } else { // Si es un número simple, se considera como edad mínima
                            int edadMin = Integer.parseInt(valor);
                            if (edadUsuario < edadMin) return false;
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Servicio: Formato de EDAD inválido en perfil requerido: " + valor);
                        return false;
                    }
                    break;
                // Si tienes otras preguntas de registro que se manejan como parte del perfil
                // Puedes añadir más casos aquí, obteniendo las respuestas del usuario
                // para esas preguntas de registro si las has implementado.
                default:
                    System.out.println("Servicio: Clave de perfil '" + clave + "' no reconocida. Se asume que se cumple.");
                    break;
            }
        }
        return true;
    }

    public boolean asociarPreguntaDelBancoAEncuesta(int idEncuesta, int idPreguntaBanco, int orden, boolean esDescarte, String criterioDescarte) {
        if (encuestaDetalleDAO.contarPreguntasEnEncuesta(idEncuesta) >= 12) {
            System.err.println("Servicio: La encuesta ID " + idEncuesta + " ya tiene 12 preguntas.");
            return false;
        }
        PreguntaBancoDAO pbDao = new PreguntaBancoDAO();
        if (pbDao.obtenerPreguntaPorId(idPreguntaBanco) == null) {
             System.err.println("Servicio: Pregunta del banco con ID " + idPreguntaBanco + " no existe.");
             return false;
        }

        EncuestaDetallePregunta detalle = new EncuestaDetallePregunta(idEncuesta, idPreguntaBanco, orden, esDescarte, criterioDescarte);
        return encuestaDetalleDAO.agregarPreguntaAEncuesta(detalle);
    }

    public boolean agregarPreguntaNuevaAEncuesta(int idEncuesta, String textoPregunta, String nombreTipo, String nombreClasificacion, int orden, boolean esDescarte, String criterioDescarte) {
        if (encuestaDetalleDAO.contarPreguntasEnEncuesta(idEncuesta) >= 12) {
            System.err.println("Servicio: La encuesta ID " + idEncuesta + " ya tiene 12 preguntas.");
            return false;
        }
        TipoPregunta tipo = tipoPreguntaDAO.obtenerTipoPreguntaPorNombre(nombreTipo);
        ClasificacionPregunta clasif = null;
        if(nombreClasificacion != null && !nombreClasificacion.trim().isEmpty()){
             clasif = clasificacionPreguntaDAO.obtenerClasificacionPorNombre(nombreClasificacion);
        }

        if (tipo == null) {
            System.err.println("Servicio: Tipo de pregunta '" + nombreTipo + "' no válido.");
            return false;
        }

        EncuestaDetallePregunta detalle = new EncuestaDetallePregunta(
            idEncuesta,
            textoPregunta,
            tipo.getIdTipoPregunta(),
            (clasif != null ? clasif.getIdClasificacion() : null),
            orden,
            esDescarte,
            criterioDescarte
        );
        return encuestaDetalleDAO.agregarPreguntaAEncuesta(detalle);
    }

    public boolean marcarPreguntaComoDescarte(int idEncuestaDetalle, String criterioDescarte) {
        EncuestaDetallePregunta detalle = encuestaDetalleDAO.obtenerPreguntaDetallePorId(idEncuestaDetalle);
        if (detalle == null) {
            System.err.println("Servicio: Pregunta de encuesta (ID detalle: "+idEncuestaDetalle+") no encontrada para marcar como descarte.");
            return false;
        }
        if (criterioDescarte == null || criterioDescarte.trim().isEmpty()){
            System.err.println("Servicio: El criterio de descarte no puede estar vacío al marcar como descarte.");
            return false;
        }
        detalle.setEsPreguntaDescarte(true);
        detalle.setCriterioDescarteValor(criterioDescarte);
        return encuestaDetalleDAO.actualizarDetallePregunta(detalle);
    }

    public boolean desmarcarPreguntaComoDescarte(int idEncuestaDetalle) {
        EncuestaDetallePregunta detalle = encuestaDetalleDAO.obtenerPreguntaDetallePorId(idEncuestaDetalle);
        if (detalle == null) {
            System.err.println("Servicio: Pregunta de encuesta (ID detalle: "+idEncuestaDetalle+") no encontrada para desmarcar.");
            return false;
        }
        detalle.setEsPreguntaDescarte(false);
        detalle.setCriterioDescarteValor(null);
        return encuestaDetalleDAO.actualizarDetallePregunta(detalle);
    }

    public boolean eliminarPreguntaDeEncuesta(int idEncuesta, int idEncuestaDetalle){
        return encuestaDetalleDAO.eliminarPreguntaDeEncuesta(idEncuestaDetalle);
    }

    public boolean eliminarPreguntaDeEncuestaServicio(int idEncuestaDetalle) {
        return encuestaDetalleDAO.eliminarPreguntaDeEncuesta(idEncuestaDetalle);
    }

    public List<EncuestaDetallePregunta> obtenerPreguntasDeEncuesta(int idEncuesta) {
        return encuestaDetalleDAO.obtenerPreguntasPorEncuesta(idEncuesta);
    }

    // CAMBIADO: 'copia.setPublicoObjetivo' a 'copia.setEsPublica' (boolean)
    public Encuesta copiarEncuesta(int idEncuestaOriginal, int idAdminCopia) {
        Encuesta original = encuestaDAO.obtenerEncuestaPorId(idEncuestaOriginal);
        if (original == null) return null;

        Encuesta copia = new Encuesta();
        copia.setNombre("Copia de " + original.getNombre());
        copia.setDescripcion(original.getDescripcion());
        copia.setFechaInicio(original.getFechaInicio());
        copia.setFechaFin(original.getFechaFin());
        copia.setEsPublica(original.isEsPublica()); // CAMBIO AQUÍ
        copia.setPerfilRequerido(original.getPerfilRequerido());
        copia.setIdAdminCreador(idAdminCopia);
        copia.setEstado("Borrador");
        copia.setFechaCreacion(new Timestamp(System.currentTimeMillis()));

        int idNuevaEncuesta = encuestaDAO.crearEncuesta(copia);
        if (idNuevaEncuesta != -1) {
            copia.setIdEncuesta(idNuevaEncuesta);
            List<EncuestaDetallePregunta> detallesOriginales = encuestaDetalleDAO.obtenerPreguntasPorEncuesta(idEncuestaOriginal);
            for(EncuestaDetallePregunta detalle : detallesOriginales) {
                detalle.setIdEncuesta(idNuevaEncuesta);
                detalle.setIdEncuestaDetalle(0); // Para que AUTO_INCREMENT genere un nuevo ID
                encuestaDetalleDAO.agregarPreguntaAEncuesta(detalle);
            }
            return copia;
        }
        return null;
    }

    public Encuesta obtenerDetallesCompletosEncuesta(int idEncuesta) {
        Encuesta encuesta = encuestaDAO.obtenerEncuestaPorId(idEncuesta);
        if (encuesta != null) {
            List<EncuestaDetallePregunta> preguntas = encuestaDetalleDAO.obtenerPreguntasPorEncuesta(idEncuesta);
            // Las instancias de DAO ya se inicializan en el constructor de ServicioEncuestas, no es necesario crearlas de nuevo aquí.
            // PreguntaBancoDAO pbDao = new PreguntaBancoDAO(); 
            // TipoPreguntaDAO tpDao = new TipoPreguntaDAO();
            // ClasificacionPreguntaDAO cpDao = new ClasificacionPreguntaDAO();

            for(EncuestaDetallePregunta edp : preguntas) {
                if (edp.getIdPreguntaBanco() != null) {
                    PreguntaBanco preguntaBanco = preguntaBancoDAO.obtenerPreguntaPorId(edp.getIdPreguntaBanco());
                    if (preguntaBanco != null) {
                        TipoPregunta tipo = tipoPreguntaDAO.obtenerTipoPreguntaPorId(preguntaBanco.getIdTipoPregunta());
                        if(tipo != null) {
                            preguntaBanco.setNombreTipoPregunta(tipo.getNombreTipo());
                            edp.setTipoPreguntaObj(tipo);
                        }

                        if(preguntaBanco.getIdClasificacion() != null && preguntaBanco.getIdClasificacion() > 0){
                            ClasificacionPregunta clasif = clasificacionPreguntaDAO.obtenerClasificacionPorId(preguntaBanco.getIdClasificacion());
                            if(clasif != null) {
                                preguntaBanco.setNombreClasificacion(clasif.getNombreClasificacion());
                                edp.setClasificacionPreguntaObj(clasif);
                            }
                        }
                    }
                    edp.setPreguntaDelBanco(preguntaBanco);
                } else if (edp.getTextoPreguntaUnica() != null) {
                    if (edp.getIdTipoPreguntaUnica() != null) {
                        TipoPregunta tipoUnica = tipoPreguntaDAO.obtenerTipoPreguntaPorId(edp.getIdTipoPreguntaUnica());
                        if(tipoUnica != null) {
                            edp.setTipoPreguntaObj(tipoUnica);
                        }
                    }
                     if (edp.getIdClasificacionUnica() != null) {
                        ClasificacionPregunta clasifUnica = clasificacionPreguntaDAO.obtenerClasificacionPorId(edp.getIdClasificacionUnica());
                        if(clasifUnica != null) {
                            edp.setClasificacionPreguntaObj(clasifUnica);
                        }
                    }
                }
            }
            Collections.sort(preguntas, (p1, p2) -> Integer.compare(p1.getOrdenEnEncuesta(), p2.getOrdenEnEncuesta()));
            encuesta.setPreguntasAsociadas(preguntas);
        }
        return encuesta;
    }

    public EncuestaDetallePregunta buscarPreguntaPorOrden(int idEncuesta, int ordenBuscado) {
        List<EncuestaDetallePregunta> preguntas = obtenerPreguntasDeEncuesta(idEncuesta);

        if (preguntas == null || preguntas.isEmpty()) {
            System.out.println("ServicioEncuestas: No hay preguntas para la encuesta ID " + idEncuesta + " o la encuesta no existe.");
            return null;
        }

        for (EncuestaDetallePregunta edp : preguntas) {
            if (edp.getOrdenEnEncuesta() == ordenBuscado) {
                return edp;
            }
        }

        System.out.println("ServicioEncuestas: No se encontró pregunta con orden " + ordenBuscado + " en la encuesta ID " + idEncuesta + ".");
        return null;
    }

    public EncuestaDetallePregunta obtenerPreguntaDetallePorId(int idEncuestaDetalle) {
        return encuestaDetalleDAO.obtenerPreguntaDetallePorId(idEncuestaDetalle);
    }

    public boolean actualizarDetallePregunta(EncuestaDetallePregunta detalle) {
        return encuestaDetalleDAO.actualizarDetallePregunta(detalle);
    }

    public boolean moverPreguntaEnEncuesta(int idEncuesta, int idEncuestaDetalleAMover, boolean moverArriba) {
        List<EncuestaDetallePregunta> preguntas = encuestaDetalleDAO.obtenerPreguntasPorEncuesta(idEncuesta);
        if (preguntas == null || preguntas.isEmpty()) {
            return false;
        }

        // Asegurarse de que la lista esté ordenada por ordenEnEncuesta
        Collections.sort(preguntas, Comparator.comparingInt(EncuestaDetallePregunta::getOrdenEnEncuesta));

        EncuestaDetallePregunta preguntaAMover = null;
        int indiceAMover = -1;

        for (int i = 0; i < preguntas.size(); i++) {
            if (preguntas.get(i).getIdEncuestaDetalle() == idEncuestaDetalleAMover) {
                preguntaAMover = preguntas.get(i);
                indiceAMover = i;
                break;
            }
        }

        if (preguntaAMover == null) {
            System.err.println("ServicioEncuestas: Pregunta a mover no encontrada con ID detalle: " + idEncuestaDetalleAMover);
            return false;
        }

        int nuevoOrdenParaAMover = -1;
        EncuestaDetallePregunta preguntaAdyacente = null;

        if (moverArriba) { // Mover hacia una posición de orden menor
            if (indiceAMover == 0) { // Ya está en la primera posición
                return false;
            }
            preguntaAdyacente = preguntas.get(indiceAMover - 1);
            nuevoOrdenParaAMover = preguntaAdyacente.getOrdenEnEncuesta(); // El orden del de arriba
        } else { // Mover hacia una posición de orden mayor
            if (indiceAMover == preguntas.size() - 1) { // Ya está en la última posición
                return false;
            }
            preguntaAdyacente = preguntas.get(indiceAMover + 1);
            nuevoOrdenParaAMover = preguntaAdyacente.getOrdenEnEncuesta(); // El orden del de abajo
        }

        // Intercambiar órdenes
        int ordenOriginalAMover = preguntaAMover.getOrdenEnEncuesta();
        int ordenOriginalAdyacente = preguntaAdyacente.getOrdenEnEncuesta();

        boolean exito1 = encuestaDetalleDAO.actualizarOrdenPregunta(preguntaAMover.getIdEncuestaDetalle(), nuevoOrdenParaAMover);
        boolean exito2 = encuestaDetalleDAO.actualizarOrdenPregunta(preguntaAdyacente.getIdEncuestaDetalle(), ordenOriginalAMover);

        return exito1 && exito2;
    }
}