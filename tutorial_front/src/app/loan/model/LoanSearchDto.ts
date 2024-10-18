export interface LoanSearchDto {
    gameName?: string; // Nombre del juego para filtrar (opcional)
    idClient?: number; // ID del cliente para filtrar (opcional)
    date?: string; // Fecha para filtrar en formato 'yyyy-MM-dd' (opcional)
    pageable: {
      pageNumber: number; // Número de página
      pageSize: number; // Tamaño de la página
    };
  }