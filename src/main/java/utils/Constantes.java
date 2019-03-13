package utils;

import seguridad.UsuarioService;


public class Constantes {

    // JNDI del EJB UsuarioService que se inyecta en el proceso de autenticacion
    //public static final String EJB_JNDI_BASE = "java:global/cte-api/";
	public static final String EJB_JNDI_BASE = "java:global/sti/";
    public static final String EJB_JNDI_USUARIO_SERVICE = EJB_JNDI_BASE
            + UsuarioService.class.getSimpleName() + "!" 
            + UsuarioService.class.getCanonicalName();

    public static final String PATH_DOCUMENTOS = System.getProperty("jboss.server.data.dir");
    
    public static final String SEXO_MASCULINO = "MASCULINO";
    public static final String SEXO_FEMENINO = "FEMENINO";

    public static final String ROL_PUBLICO = "PUBLICO";
    public static final String ROL_ALUMNO = "ALUMNO";
    public static final String ROL_PROFESOR = "PROFESOR";
    
    public static final String ESTADO_CARGO_DISPONIBLE = "DISPONIBLE";

}
