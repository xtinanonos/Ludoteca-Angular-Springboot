package com.ccsw.tutorial.common.criteria;

// CLASE PARA GUARDAR INFORMACION DE PREDICADOS/PREDICATES (clausulas 'where' de una consulta SQL)
// Esto lo utilizamos para poder hacer consultas a la BBDD con clausulas where

public class SearchCriteria {

    private String key;
    private String operation;
    private Object value;

    // para el criterio de busqueda se establece la clave/campo, la operacion a realizar y el valor
    public SearchCriteria(String key, String operation, Object value) {

        this.key = key;
        this.operation = operation;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

}
