package ar.com.augustoocchiuzzi.Test;

import ar.com.augustoocchiuzzi.domain.Persona;
import javax.ws.rs.client.*;
import javax.ws.rs.core.*;
import java.util.List;

public class TestPersonaServiceRS {

    private static final String URL_BASE = "http://localhost:8080/RestServiceComplete/webservice";
    private static Client cliente;
    private static WebTarget webtarget;
    private static Persona persona;
    private static List<Persona> personas;
    private static Invocation.Builder invocationBuilder;
    private static Response response;

    public static void main(String[] args) {
        cliente = ClientBuilder.newClient();

        //leer una persona. llamada al metodo Get.
        webtarget = cliente.target(URL_BASE).path("/personas");

        //proporcionamos un idPersona valido
        persona = webtarget.path("/6").request(MediaType.APPLICATION_XML).get(Persona.class);
        System.out.println("Persona recibida: " + persona.getApellido());

        personas = webtarget.request(MediaType.APPLICATION_XML).get(Response.class).readEntity(new GenericType<List<Persona>>(){});
        System.out.println("Persona recuperada");
        imprimirPersonas(personas);

        
        Persona persona1 = new Persona();
        persona1.setNombre("Dowel");
        persona1.setApellido("Haaran");
        persona1.setEmail("thATTn@hotmail.com");
        persona1.setTelefono("11311224");

        invocationBuilder = webtarget.request(MediaType.APPLICATION_XML);
        response = invocationBuilder.post(Entity.entity(persona1, MediaType.APPLICATION_XML));
        System.out.println("Estatus: \n" + response.getStatus());

        //Recuperamos persona recien agregada para modificarla y eliminarla

        Persona persRecuperada = response.readEntity(Persona.class);
        System.out.println(persRecuperada);
        
        Persona personaModify = persRecuperada;
        personaModify.setApellido("Powell");
        String pathId = "/" + personaModify.getIdPersona();
        invocationBuilder = webtarget.path(pathId).request(MediaType.APPLICATION_XML);
        response = invocationBuilder.put(Entity.entity(personaModify, MediaType.APPLICATION_XML));

        System.out.println("Response: " + response.getStatus());
        System.out.println("Persona: " + response.readEntity(Persona.class));

        //E;iminamos a la persona
        Persona persEliminar = persRecuperada;
        String pathEliminar = "/" + persEliminar.getIdPersona();
        invocationBuilder = webtarget.path(pathEliminar).request(MediaType.APPLICATION_XML);
        response = invocationBuilder.delete();

        System.out.println("Status: " + response.getStatus());
        System.out.println("Persona Eliminada:" + persEliminar);


    }

        private static void imprimirPersonas(List<Persona> personas) {

                for(Persona p : personas) {
                    System.out.println("Personas recuperadas:"  + p.getApellido());
                }
        }


}
