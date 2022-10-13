import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;


public class TesteCliente {

    String enderecoAPICliente = "http://localhost:8080/";
    String endpointCliente = "cliente";
    String endpontiApagaTodos = "/apagaTodos";



    @Test
    @DisplayName("Quando pegar todos os clientes sem cadastrar, então a lista deve estar vazia")
    public void pegaTodososClientes() {

        deletaTodosClientes ();

        String respostaEsperada = "{}";
        given()
                .contentType(ContentType.JSON)
        .when()
                .get(enderecoAPICliente)
        .then()
                .statusCode(200)
                .assertThat().body(new IsEqual<>(respostaEsperada));


    }

    @Test
    @DisplayName("Quando cadastrar um cliente, então ele deve estar disponivel nos resultados")
    public void CadastraCliente() {

        String clienteparaCadastrar = "{\n" +
                "  \"id\": 1001,\n" +
                "  \"idade\": 25,\n" +
                "  \"nome\": \"Steve Jobs\",\n" +
                "  \"risco\": 0\n" +
                "}";

        String respostaEsperada = "{\"1001\":{\"nome\":\"Steve Jobs\",\"idade\":25,\"id\":1001,\"risco\":0}}";
        given()
                .contentType(ContentType.JSON)
                .body(clienteparaCadastrar)
        .when()
                .post(enderecoAPICliente + endpointCliente)
        .then()
                .statusCode(201)
                .assertThat().body(containsString(respostaEsperada));

    }

    @Test
    @DisplayName("Quando enviar um novo dado, então o cadastro do cliente será atualizado")
    public void atualizaCliente () {

        String clienteparaCadastrar = "{\n" +
                "  \"id\": 1001,\n" +
                "  \"idade\": 25,\n" +
                "  \"nome\": \"Steve Jobs\",\n" +
                "  \"risco\": 0\n" +
                "}";

        String DadoParaAtualizar = "{\n" +
                "  \"id\": 1001,\n" +
                "  \"idade\": 35,\n" +
                "  \"nome\": \"Steve Jobs\",\n" +
                "  \"risco\": 0\n" +
                "}";

        String resultadoEsperado = "{\"1001\":{\"nome\":\"Steve Jobs\",\"idade\":35,\"id\":1001,\"risco\":0}}";

        given()
                .contentType(ContentType.JSON)
                .body(clienteparaCadastrar)
        .when()
                .post(enderecoAPICliente + endpointCliente)
        .then()
                .statusCode(201);


        given()
                .contentType(ContentType.JSON)
                .body(DadoParaAtualizar)
        .when()
                .put(enderecoAPICliente+endpointCliente)
        .then()
                .statusCode(200)
                .assertThat().body(containsString(resultadoEsperado));
    }

    @Test
    @DisplayName("Quando deletar um cliente, então ele deve ser ecluído")
    public void DeletaClienteCadastrado () {

        String clienteparaCadastrar = "{\n" +
                "  \"id\": 1001,\n" +
                "  \"idade\": 25,\n" +
                "  \"nome\": \"Steve Jobs\",\n" +
                "  \"risco\": 0\n" +
                "}";


        String RespostaEsperada = "CLIENTE REMOVIDO: { NOME: Steve Jobs, IDADE: 25, ID: 1001 }";

        given()
                .contentType(ContentType.JSON)
                .body(clienteparaCadastrar)
        .when()
                .post(enderecoAPICliente + endpointCliente)
        .then()
                .statusCode(201);


        given()
                .contentType(ContentType.JSON)

        .when()
                .delete(enderecoAPICliente+endpointCliente+"/1001")

        .then()
                .statusCode(200)
                .assertThat().body(new IsEqual<>(RespostaEsperada));


    }

    //Metódo de Apoio
    public void deletaTodosClientes () {

        String respostaEsperada= "{}";

        given()
                .contentType(ContentType.JSON)
        .when()
                .delete(enderecoAPICliente+endpointCliente+endpontiApagaTodos)
        .then()
                .statusCode(200)
                .assertThat().body(new IsEqual<>(respostaEsperada));
    }
}

