package guru.springframework.controllers;

import guru.springframework.domain.Recipe;
import guru.springframework.services.RecipeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class IndexControllerTest {

    IndexController indexController;
    @Mock
    RecipeService recipeService;

    @Mock
    Model model;

    private AutoCloseable closable;

    @BeforeEach
    void setUp() {
        closable = MockitoAnnotations.openMocks(this);
        indexController = new IndexController(recipeService);
    }

    @AfterEach
    void closeService() throws Exception {
        closable.close();
    }

    @Test
    void getIndexPage() {
        var recipeData = new HashSet<Recipe>();
        recipeData.add(new Recipe());
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipeData.add(recipe);

        var argumentCaptor = ArgumentCaptor.forClass(Set.class);
        when(recipeService.getRecipies()).thenReturn(recipeData);

        String indexPage = indexController.getIndexPage(model);

        assertEquals("index", indexPage);

        verify(recipeService, times(1)).getRecipies();
        verify(model, times(1)).addAttribute(eq("recipes"), argumentCaptor.capture());
        var recipesCaptured = argumentCaptor.getValue();
        assertEquals(2, recipesCaptured.size());
    }
}