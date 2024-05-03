package com.skillstorm.taxguruplatform.utils.mappers;

public interface Mapper<O, D> {

    D mapTo(O o);

    O mapFrom(D d);

}
