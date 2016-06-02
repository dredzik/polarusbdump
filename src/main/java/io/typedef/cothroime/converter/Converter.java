package io.typedef.cothroime.converter;

import io.typedef.cothroime.model.Entity;

public interface Converter<F, T extends Entity> {

    T convert(F data);
}
