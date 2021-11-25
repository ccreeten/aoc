package go.solve.it.y2020;

import go.solve.it.util.container.Collectionsβ;
import go.solve.it.util.input.Input;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static go.solve.it.util.container.Collectionsβ.copy;
import static go.solve.it.util.container.Collectionsβ.difference;
import static go.solve.it.util.container.Maps.deepCopy;
import static go.solve.it.util.container.Maps.findKeyByValue;
import static go.solve.it.util.string.Regex.findAll;
import static java.util.Map.Entry.comparingByValue;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.joining;

public final class Day21 {

    public static void main(final String... args) {
        System.out.println(part1(Input.lines("y2020/day21/input")));
        System.out.println(part2(Input.lines("y2020/day21/input")));
    }

    private static long part1(final String... foodList) {
        final var foods = parseIngredients(foodList);
        final var inferred = inferAllergens(foods);
        return foods.keySet().stream().flatMap(Set::stream).filter(not(inferred.keySet()::contains)).count();
    }

    private static String part2(final String... foodList) {
        final var foods = parseIngredients(foodList);
        final var inferred = inferAllergens(foods);
        return inferred.entrySet().stream().sorted(comparingByValue()).map(Entry::getKey).collect(joining(","));
    }

    private static Map<Set<String>, Set<String>> parseIngredients(final String... foodList) {
        final var allergens = new HashMap<Set<String>, Set<String>>();
        for (final var food : foodList) {
            allergens.put(new HashSet<>(findAll(food.split("\\(")[0], "(\\w+)")), new HashSet<>(findAll(food.split("contains")[1], "(\\w+)")));
        }
        return allergens;
    }

    private static Map<String, String> inferAllergens(final Map<Set<String>, Set<String>> foods) {
        return inferAllergens(foods, new HashMap<>());
    }

    private static Map<String, String> inferAllergens(final Map<Set<String>, Set<String>> foods, final Map<String, String> inferred) {
        if (foods.values().stream().allMatch(Set::isEmpty)) {
            return inferred;
        }
        final var ingredients = findKeyByValue(foods, allergens -> !allergens.isEmpty());
        final var allergens = copy(foods.get(ingredients));

        for (final var ingredient : difference(ingredients, inferred.keySet())) {
            trying:
            for (final var allergen : allergens) {
                final var updatedFoods = deepCopy(foods, Collectionsβ::copy);
                for (final var food : updatedFoods.entrySet()) {
                    if (food.getValue().remove(allergen) && !food.getKey().contains(ingredient)) {
                        continue trying;
                    }
                }
                inferred.put(ingredient, allergen);
                var result = inferAllergens(updatedFoods, inferred);
                if (result != null) {
                    return result;
                }
                inferred.remove(ingredient, allergen);
            }
        }
        return null;
    }
}