package com.demo.cashcards;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "sarah1")
class CashCardsApplicationTests {
	@Autowired
	private MockMvc mvc;


	

	@Test
	void shouldReturnACashCardWhenRequested() throws Exception{
		this.mvc.perform(get("/cashcards/99"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("@.id").value(99L))
		.andExpect(jsonPath("@.amount").value(123.45));
	}

	@Test
	@DirtiesContext
	void shouldCreateACashCard() throws Exception{
		String location = this.mvc.perform(post("/cashcards/create")
		.with(csrf())
		.contentType("application/json")
		.content("""
				{
				"amount": 123.45,
				"owner": "sarah1"
				}
				"""))
				.andExpect(status().isCreated())
				.andExpect(header().exists("Location"))
				.andReturn().getResponse().getHeader("Location");
		this.mvc.perform(get(location))
		.andExpect(status().isOk())
		.andExpect(jsonPath("@.amount").value(123.45))
		.andExpect(jsonPath("@.owner").value("sarah1"));
	}

	@Test
	void shouldReturnAllCashCards() throws Exception{
		this.mvc.perform(get("/cashcards"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.length()").value(2))
		.andExpect(jsonPath("$..owner").value(everyItem(equalTo("sarah1"))));
	}
	@Test
	void contextLoads() {
	}

}
