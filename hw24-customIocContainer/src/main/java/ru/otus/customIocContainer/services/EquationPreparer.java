package ru.otus.customIocContainer.services;

import ru.otus.customIocContainer.model.Equation;

import java.util.List;

public interface EquationPreparer {
    List<Equation> prepareEquationsFor(int base);
}
