package ru.spbau.des.roguelike.operation;

import ru.spbau.des.roguelike.dom.environment.FieldPlan;

/**
 * Interface for FieldPlan factories.
 */
public interface FieldPlanCreator {
    FieldPlan createPlan();
}
