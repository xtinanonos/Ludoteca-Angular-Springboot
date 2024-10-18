import { LoanPage } from "./LoanPage";

export const LOAN_DATA: LoanPage = {
    content: [
        { 
            id: 1, 
            game: { 
                id: 1, 
                title: 'Juego 1', 
                age: 6, 
                category: { id: 1, name: 'Categoría 1' }, 
                author: { id: 1, name: 'Autor 1', nationality: 'Nacionalidad 1' }
            }, 
            client: { id: 1, name: 'Nombre de prueba' }, 
            date_start: new Date('2024-02-02'), 
            date_end: new Date('2024-02-10')
        },
        { 
            id: 2, 
            game: { 
                id: 2, 
                title: 'Juego 2', 
                age: 8, 
                category: { id: 2, name: 'Categoría 2' }, 
                author: { id: 2, name: 'Autor 2', nationality: 'Nacionalidad 2' }
            }, 
            client: { id: 2, name: 'Nombre de prueba 2' }, 
            date_start: new Date('2024-02-12'), 
            date_end: new Date('2024-02-20')
        },
        { 
            id: 3, 
            game: { 
                id: 3, 
                title: 'Juego 3', 
                age: 10, 
                category: { id: 3, name: 'Categoría 3' }, 
                author: { id: 3, name: 'Autor 3', nationality: 'Nacionalidad 3' }
            }, 
            client: { id: 3, name: 'Nombre de prueba 3' }, 
            date_start: new Date('2024-02-22'), 
            date_end: new Date('2024-02-28')
        },
        { 
            id: 4, 
            game: { 
                id: 4, 
                title: 'Juego 4', 
                age: 6, 
                category: { id: 4, name: 'Categoría 4' }, 
                author: { id: 2, name: 'Autor 2', nationality: 'Nacionalidad 2' }
            }, 
            client: { id: 4, name: 'Nombre de prueba 4' }, 
            date_start: new Date('2024-03-02'), 
            date_end: new Date('2024-03-10')
        },
        { 
            id: 5, 
            game: { 
                id: 2, 
                title: 'Juego 2', 
                age: 8, 
                category: { id: 2, name: 'Categoría 2' }, 
                author: { id: 1, name: 'Autor 2', nationality: 'Nacionalidad 2' }
            }, 
            client: { id: 5, name: 'Nombre de prueba 5' }, 
            date_start: new Date('2024-02-12'), 
            date_end: new Date('2024-02-20')
        }
    ],
    pageable : {
        pageSize: 3,
        pageNumber: 0,
        sort: [
            {property: "id", direction: "ASC"}
        ]
    },
    totalElements: 5
}