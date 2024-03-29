package kajitool.web.service.recipe;

import kajitool.web.domain.model.Recipe;
import kajitool.web.domain.repository.RecipeRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Transactional
public class RecipeService {
    // メモリ版
    // private final AtomicLong sequence = new AtomicLong();
    // private final Map<Long, Recipe> map = new ConcurrentHashMap<>();

    // public Recipe create(final Recipe recipe) {
    //     RecipeValidator.validateOnCreate(recipe);

    //     recipe.setId(sequence.incrementAndGet());
    //     map.put(recipe.getId(), recipe);
    //     return recipe;
    // }
    // public Optional<Recipe> findById(final long id) {
    //     return Optional.ofNullable(map.get(id));
    // }
    // public Recipe save(final Recipe recipe) {
    //     RecipeValidator.validateOnCreate(recipe);

    //     return map.replace(recipe.getId(), recipe);
    // }
    // public void remove(final long id, final int version) {
    //     map.remove(id);
    // }

    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }
    public Recipe create(final Recipe recipe) {
        RecipeValidator.validateOnCreate(recipe);
        return recipeRepository.create(recipe);
    }
    public Optional<Recipe> findById(final long id) {
        return recipeRepository.selectById(id);
    }
    public Recipe save(final Recipe recipe) {
        RecipeValidator.validateOnUpdate(recipe);
        return recipeRepository.update(recipe);
    }
    public void remove(final long id, final int version) {
        recipeRepository.remove(id, version);
    }
}