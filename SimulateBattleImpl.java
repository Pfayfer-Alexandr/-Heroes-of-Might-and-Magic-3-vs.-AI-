package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.programs.SimulateBattle;

import java.util.*;
import java.util.stream.Collectors;

public class SimulateBattleImpl implements SimulateBattle {
    private final PrintBattleLog printBattleLog; // Логгер для вывода логов после каждой атаки

    public SimulateBattleImpl(PrintBattleLog printBattleLog) {
        this.printBattleLog = printBattleLog;
    }

    @Override
    public void simulate(Army playerArmy, Army computerArmy) throws InterruptedException {
        // Получаем списки юнитов обеих армий
        List<Unit> playerUnits = new ArrayList<>(playerArmy.getUnits());
        List<Unit> computerUnits = new ArrayList<>(computerArmy.getUnits());

        // Симуляция боя до тех пор, пока в обеих армиях есть живые юниты
        while (hasAliveUnits(playerUnits) && hasAliveUnits(computerUnits)) {
            // Сортировка юнитов по убыванию атаки перед каждым раундом
            sortUnitsByAttack(playerUnits);
            sortUnitsByAttack(computerUnits);

            // Выполнение раунда боя
            performRound(playerUnits, computerUnits);
        }
    }

    // Проверка наличия живых юнитов в списке
    private boolean hasAliveUnits(List<Unit> units) {
        return units.stream().anyMatch(Unit::isAlive);
    }

    // Сортировка юнитов по убыванию атаки
    private void sortUnitsByAttack(List<Unit> units) {
        units.sort(Comparator.comparingInt(Unit::getBaseAttack).reversed());
    }

    // Выполнение одного раунда боя
    private void performRound(List<Unit> playerUnits, List<Unit> computerUnits) throws InterruptedException {
        // Атака юнитов игрока
        performAttacks(playerUnits, computerUnits);

        // Атака юнитов компьютера
        performAttacks(computerUnits, playerUnits);

        // Удаление мертвых юнитов из списков
        removeDeadUnits(playerUnits);
        removeDeadUnits(computerUnits);
    }

    // Выполнение атак для указанной армии
    private void performAttacks(List<Unit> attackingUnits, List<Unit> defendingUnits) throws InterruptedException {
        Iterator<Unit> iterator = attackingUnits.iterator();

        while (iterator.hasNext()) {
            Unit attackingUnit = iterator.next();

            // Пропускаем мертвых юнитов
            if (!attackingUnit.isAlive()) {
                continue;
            }

            // Юнит атакует противника
            Unit target = attackingUnit.getProgram().attack();

            // Если цель найдена, логируем атаку
            if (target != null) {
                printBattleLog.printBattleLog(attackingUnit, target);
            }

            // Если юнит погиб после атаки, удаляем его из списка
            if (!attackingUnit.isAlive()) {
                iterator.remove();
            }
        }
    }

    // Удаление мертвых юнитов из списка
    private void removeDeadUnits(List<Unit> units) {
        units.removeIf(unit -> !unit.isAlive());
    }
}