package io.typedef.polarusbdump.converter;

import io.typedef.polarusbdump.model.Entity;

public interface Converter<F, T extends Entity> {

    T convert(F data);
}
