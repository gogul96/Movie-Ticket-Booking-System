package com.gogul.tickets;

import static org.assertj.core.api.Assertions.assertThat;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParseException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TicketsApplication.class)
class TicketsApplicationTests {

	@Autowired
    private TestRestTemplate restTemplate;
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	private HttpEntity<String> getStringHttpEntity(String token,Object object) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if(!token.isEmpty()) {
        	headers.setBearerAuth(token);
        }
        return (HttpEntity<String>) new HttpEntity(object, headers);
    }
    
    @Test
    public void shouldStoreUserAndLogin() throws JsonProcessingException {	
    	String employeeBuilder = createDummyUser(3001);
    	HttpEntity<String> entity = getStringHttpEntity("",employeeBuilder);
        ResponseEntity<String> response = restTemplate.postForEntity("/api/signup/users", entity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        response = restTemplate.postForEntity("/api/login", getStringHttpEntity("", createLoginUser()), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.hasBody()).isEqualTo(true);
        JSONObject responseBody;
        try {
        	responseBody = new JSONObject(response.getBody());
        }catch(Exception e) {
        	throw new JsonParseException();
        }
        assertThat(responseBody.has("token")).isEqualTo(true);
        assertThat(restTemplate.exchange("/api/users/3001", HttpMethod.GET,getHeaderHttpEntity(responseBody.getString("token")),String.class)
        		.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    
	private HttpEntity<String> getHeaderHttpEntity(String token) {
		HttpHeaders headers = new HttpHeaders();
        if(!token.isEmpty()) {
        	headers.setBearerAuth(token);
        }
		return new HttpEntity<String>(headers);
	}
	 
	private String createLoginUser() {
		return "{\n"
				+ "    \"username\":\"user1\",\n"
				+ "    \"password\":\"user1\",\n"
				+ "}";
	}

	private String createDummyUser(long userId) {
		return "{\n"
				+ "    \"userId\": "+ userId +",\n"
				+ "    \"username\":\"user1\",\n"
				+ "    \"emailAddress\":\"abcd@xyz.com\"\n"
				+ "}";
	}
    

}
