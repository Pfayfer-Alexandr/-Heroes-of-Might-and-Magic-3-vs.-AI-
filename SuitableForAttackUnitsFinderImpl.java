package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;

import java.util.*;

public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {

    @Override
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {
        List<Unit> suitableUnits = new ArrayList<>();

        for (List<Unit> row : unitsByRow) {
            for (int i = 0; i < row.size(); i++) {
                Unit unit = row.get(i);
                if (isLeftArmyTarget) {
                    // Для атаки левой армии (компьютера) юнит не должен быть закрыт справа
                    if (i == row.size() - 1 || !row.get(i + 1).isAlive()) {
                        suitableUnits.add(unit);
                    }
                } else {
                    // Для атаки правой армии (игрока) юнит не должен быть закрыт слева
                    if (i == 0 || !row.get(i - 1).isAlive()) {
                        suitableUnits.add(unit);
                    }
                }
            }
        }

        return suitableUnits;
    }
}