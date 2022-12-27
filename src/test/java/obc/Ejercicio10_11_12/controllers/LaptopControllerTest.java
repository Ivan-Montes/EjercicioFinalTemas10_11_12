package obc.Ejercicio10_11_12.controllers;

import obc.Ejercicio10_11_12.entities.Laptop;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LaptopControllerTest {

/*
## IMPORTANTE ##
Como esta clase no ejecuta la clase principal, método main,
no hay nada en la lista-repositorio
En los ejemplos del curso, el metodo test de create se ejecuta antes,
con lo que añade un elemento a la lista-repo compartida
*/

    private TestRestTemplate testRestTemplate;
    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        restTemplateBuilder = restTemplateBuilder.rootUri("http://localhost:" + port);
        testRestTemplate = new TestRestTemplate(restTemplateBuilder);

    }

    @Test
    void findAll() {
        ResponseEntity<List> responseEntity = testRestTemplate.getForEntity("/api/findAll", List.class);
        assertNotNull(responseEntity);

        if ( responseEntity != null ){

            if ( responseEntity.getStatusCode() == HttpStatus.NO_CONTENT){
                /*  //Pruebas para lista vacia, ResponseEntity.noContent */
                System.out.println("\n###### ==>> Test findAll = " + HttpStatus.NO_CONTENT + " <<== ######\n");
                assertEquals(responseEntity.getStatusCode(),HttpStatus.NO_CONTENT);
                assertEquals(responseEntity.getStatusCodeValue(),204);

            }else{
                /* Pruebas si la lista contuviera algún elemento, si no como devuelve noContent daría fallo */
                System.out.println("\n###### ==>> Test findAll = " + HttpStatus.OK + " <<== ######\n");
                assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
                assertEquals(responseEntity.getStatusCodeValue(),200);
                assertNotNull(responseEntity.getBody());
                if (responseEntity.getBody() != null ){
                    System.out.println("\n###### ==>> findAll Lista.size = " + responseEntity.getBody().size() + " <<== ######");
                    responseEntity.getBody().forEach(System.out::println);
                }
            }
        }
    }
    @Test
    @DisplayName("Método getLaptopList == findAll")
    void getLaptopList() {
        //Esta api devuelve una lista siempre aunque esté vacia
        ResponseEntity<List> responseEntity = testRestTemplate.getForEntity("/api/laptops", List.class);

        assertNotNull(responseEntity);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertEquals(responseEntity.getStatusCodeValue(),200);
        assertNotNull(responseEntity.getBody());

        if (responseEntity.getBody() != null ){
            System.out.println("\n###### ==>> Test getLaptopList Lista.size = " + responseEntity.getBody().size() + " <<== ######\n");
        }
    }
    @Test
    void findOneById() {
        ResponseEntity<Laptop>responseEntity = testRestTemplate.getForEntity("/api/findOneById/1",Laptop.class);

        assertNotNull(responseEntity);

        if ( responseEntity != null ){

            if ( responseEntity.getStatusCode() == HttpStatus.NO_CONTENT){
                System.out.println("\n###### ==>> Test findOneById = " + HttpStatus.NO_CONTENT + " <<== ######\n");
                assertEquals(responseEntity.getStatusCode(),HttpStatus.NO_CONTENT);
                assertEquals(responseEntity.getStatusCodeValue(),204);

            }else{
                System.out.println("\n###### ==>> Test findOneById = " + HttpStatus.OK + " <<== ######\n");
                assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
                assertEquals(responseEntity.getStatusCodeValue(),200);
                assertNotNull(responseEntity.getBody());
                assertTrue( responseEntity.getBody().getId() == 1L );

                if ( responseEntity.getBody() != null ){
                    System.out.println("\n###### ==>> Objeto encontrado = " + responseEntity.getBody() + " <<== ######\n");
                }
            }
        }
    }

    @Test
    void create() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        String json = """
                    {
                        "marca": "HipHopero",
                        "modelo": "BZR 2.3",
                        "peso": 0.777
                    }                
                """;

        HttpEntity<String> request = new HttpEntity(json, httpHeaders);
        ResponseEntity<Laptop>response = testRestTemplate.exchange("/api/create",HttpMethod.POST,request,Laptop.class);

        assertNotNull(response);
        assertEquals(response.getStatusCode(),HttpStatus.OK);
        assertEquals(response.getStatusCodeValue(),200);

        Laptop laptopCreado = response.getBody();
        assertNotNull(laptopCreado);
        assertTrue( laptopCreado.getId() > 0 );
        assertTrue( laptopCreado.getMarca().equals("HipHopero"));

        if ( laptopCreado.getMarca().equals("HipHopero") ){
            System.out.println("\n###### ==>> Test create Objeto creado = " + laptopCreado + " <<== ######");
        }
    }

    @Test
    void update() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        String json = """
                    {
                        "id": 1,
                        "marca": "Popero",
                        "modelo": "BZR 2.3",
                        "peso": 0.777
                    }                
                """;
        HttpEntity<String> request = new HttpEntity(json, httpHeaders);
        ResponseEntity<Laptop>response = testRestTemplate.exchange("/api/update",HttpMethod.PUT,request,Laptop.class);

        assertNotNull(response);

        if ( response != null ){

            if ( response.getStatusCode() == HttpStatus.BAD_REQUEST){
                System.out.println("\n###### ==>> Test update = " + HttpStatus.BAD_REQUEST + " <<== ######\n");
                assertEquals(response.getStatusCode(),HttpStatus.BAD_REQUEST);
                assertEquals(response.getStatusCodeValue(),204);
            } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                assertEquals(response.getStatusCode(),HttpStatus.NOT_FOUND);
                //assertEquals(response.getStatusCodeValue(),204);
            }
            else{
                assertEquals(response.getStatusCode(),HttpStatus.OK);
                assertEquals(response.getStatusCodeValue(),200);

                Laptop laptopMod = response.getBody();
                assertNotNull(laptopMod);
                assertTrue( laptopMod.getId() > 0 );
                assertTrue( laptopMod.getMarca().equals("Popero"));

                if ( laptopMod.getMarca().equals("Popero") ){
                    System.out.println("\n###### ==>> Test update = " + laptopMod + " <<== ######");
                }
            }

        }
    }

    @Test
    void delete() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        String json = """
                    {
                        "marca": "DeleteBrand",
                        "modelo": "Ilusion Max 3",
                        "peso": 0.639
                    }                
                """;

        HttpEntity<String> request = new HttpEntity(json, httpHeaders);
        ResponseEntity<Laptop>response = testRestTemplate.exchange("/api/create",HttpMethod.POST,request,Laptop.class);

        assertNotNull(response);
        assertEquals(response.getStatusCode(),HttpStatus.OK);
        assertEquals(response.getStatusCodeValue(),200);

        Laptop laptopCreado = response.getBody();
        assertNotNull(laptopCreado);
        assertTrue( laptopCreado.getId() > 0 );
        assertTrue( laptopCreado.getMarca().equals("DeleteBrand"));

        if ( laptopCreado.getMarca().equals("DeleteBrand") && laptopCreado.getModelo().equals("Ilusion Max 3") ){
            System.out.println("\n###### ==>> Test delete Objeto creado = " + laptopCreado + " <<== ######");
            testRestTemplate.delete("/api/delete/" + laptopCreado.getId() );
            System.out.println("\n###### ==>> Test delete Eliminamos objeto = " + laptopCreado + " <<== ######");
            ResponseEntity<Laptop>responseEntity = testRestTemplate.getForEntity("/api/findOneById/" + laptopCreado.getId(),Laptop.class);
            assertNotNull(responseEntity);

            if ( responseEntity != null ){

                if ( responseEntity.getStatusCode() == HttpStatus.NO_CONTENT){
                    System.out.println("\n###### ==>> Test delete = " + HttpStatus.NO_CONTENT + " <<== ######\n");
                    assertEquals(responseEntity.getStatusCode(),HttpStatus.NO_CONTENT);
                    assertEquals(responseEntity.getStatusCodeValue(),204);
                    System.out.println("\n###### ==>> Test delete Pasado <<== ######\n");
                }else{
                    System.out.println("\n###### ==>> Test delete = " + HttpStatus.OK + " <<== ######\n");
                    assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
                    assertEquals(responseEntity.getStatusCodeValue(),200);
                    assertNotNull(responseEntity.getBody());

                    if ( responseEntity.getBody() != null ){
                        System.out.println("\n###### ==>> Objeto encontrado = " + responseEntity.getBody() + " <<== ######\n");
                    }
                    System.out.println("\n###### ==>> Test delete FAIL <<== ######\n");

                }
            }

        }else{
            fail();
        }
    }

    @Test
    @DisplayName("Test DeleteAll")
    //@AfterAll
    //@Order(7)
    public void testDelete() {
        System.out.println("\n###### ==>> Inicio Test testDeleteAll <<== ######\n");
        testRestTemplate.delete("/api/deleteAll");
        ResponseEntity<List> responseEntity = testRestTemplate.getForEntity("/api/laptops",List.class);
        assertNotNull(responseEntity);
        assertNotNull(responseEntity.getBody());

        if ( responseEntity.getBody().isEmpty() ){
            assertTrue(responseEntity.getBody().isEmpty());
            System.out.println("\n###### ==>> Final Test testDelete PASS. Size = " + responseEntity.getBody().size()+ " <<== ######\n");

        }else{
            System.out.println("\n###### ==>> Final Test testDelete FAIL. Size = " + responseEntity.getBody().size()+ " <<== ######\n");
            fail();
        }
    }
}