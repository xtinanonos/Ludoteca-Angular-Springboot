import { SortPage } from "./SortPage";

export class Pageable {
    pageNumber: number;
    pageSize: number;
    sort: SortPage[];
}

/**
  {
    "content": [ ... <listado con los resultados paginados> ... ],
    "pageable": {
        "pageNumber": <número de página empezando por 0>,
        "pageSize": <tamaño de página>,
        "sort": [
            { 
                "property": <nombre de la propiedad a ordenar>, 
                "direction": <dirección de la ordenación ASC / DESC> 
            }
        ]
    },
    "totalElements": <numero total de elementos en la tabla>
}
 */