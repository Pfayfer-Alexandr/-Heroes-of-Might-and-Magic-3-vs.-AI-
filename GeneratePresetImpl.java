package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.*;

public class GeneratePresetImpl implements GeneratePreset {

    @Override
    public Army generate(List<Unit> unitList, int maxPoints) {
        // Создаем список для хранения юнитов армии компьютера
        List<Unit> armyUnits = new ArrayList<>();
        int totalPoints = 0;

        // Сортируем юниты по эффективности (атака/стоимость и здоровье/стоимость)
        unitList.sort(Comparator.comparingDouble((Unit u) -> (double) u.getBaseAttack() / u.getCost())
                .thenComparingDouble(u -> (double) u.getHealth() / u.getCost())
                .reversed());

        // Словарь для отслеживания количества юнитов каждого типа
        Map<String, Integer> unitCounts = new HashMap<>();

        // Перебираем юниты и добавляем их в армию, пока не исчерпаем очки
        for (Unit unit : unitList) {
            // Максимальное количество юнитов этого типа, которое можно добавить
            int maxUnits = Math.min(11, (maxPoints - totalPoints) / unit.getCost());
            if (maxUnits <= 0) continue;

            // Добавляем юниты в армию
            for (int i = 0; i < maxUnits; i++) {
                // Создаем новый юнит на основе текущего
                Unit newUnit = new Unit(
                        unit.getName(),
                        unit.getUnitType(),
                        unit.getHealth(),
                        unit.getBaseAttack(),
                        unit.getCost(),
                        unit.getAttackType(),
                        unit.getAttackBonuses(),
                        unit.getDefenceBonuses(),
                        unit.getxCoordinate(),
                        unit.getyCoordinate()
                );
                armyUnits.add(newUnit);
                totalPoints += unit.getCost();

                // Обновляем счетчик юнитов этого типа
                unitCounts.put(unit.getUnitType(), unitCounts.getOrDefault(unit.getUnitType(), 0) + 1);
            }
        }

        // Создаем армию и добавляем в нее юнитов
        Army computerArmy = new Army();
        computerArmy.setUnits(armyUnits);
        computerArmy.setPoints(totalPoints);

        // Возвращаем армию компьютера
        return computerArmy;
    }
}